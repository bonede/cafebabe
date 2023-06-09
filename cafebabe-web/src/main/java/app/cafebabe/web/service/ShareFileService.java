package app.cafebabe.web.service;

import app.cafebabe.error.ApiException;
import app.cafebabe.model.ShareFile;
import app.cafebabe.model.ShareFile.PubShareFile;
import app.cafebabe.model.vo.CreateShareReq;
import app.cafebabe.model.vo.DeleteShareFileReq;
import app.cafebabe.model.vo.ShareResp;
import app.cafebabe.utils.JsonUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@Validated
public class ShareFileService {

    @Autowired
    private AppService.AppConfig appConfig;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RateLimitService rateLimitService;

    private String genShareId(){
        return RandomStringUtils.randomAlphanumeric(16);
    }

    private String genDeletingToken(){
        return RandomStringUtils.randomAlphanumeric(32);
    }

    private String shareUrl(String shareId){
        return appConfig.getUrl() + "/?s=" + shareId;
    }

    private String deletingUrl(String shareId, String deletingToken){
        return appConfig.getUrl() + "/?s=" + shareId + "&d=" + deletingToken;
    }

    private String redisKey(String id){
        return ShareFile.REDIS_KEY + ":" + id;
    }

    public ShareResp share(@Valid CreateShareReq req, HttpServletRequest httpServletRequest){
        rateLimitService.limit(httpServletRequest, "Share", appConfig.shareLimit, appConfig.getShareLimitWindow());
        ShareFile shareFile = new ShareFile();
        shareFile.setId(genShareId());
        shareFile.setHoursToLive(req.getHoursToLive());
        shareFile.setSrcFiles(req.getSrcFiles());
        shareFile.setDeletingToken(genDeletingToken());
        shareFile.setOps(req.getOps());
        shareFile.setCreatedAt(LocalDateTime.now());
        if(req.getHoursToLive() != null && !appConfig.getShareLiveHours().contains(req.getHoursToLive())){
            throw ApiException.error("Invalid hours to live");
        }
        if(req.getHoursToLive() != null){
            redisTemplate.opsForValue().set(redisKey(shareFile.getId()), JsonUtils.toJson(shareFile), req.getHoursToLive(), TimeUnit.HOURS);
        }else {
            redisTemplate.opsForValue().set(redisKey(shareFile.getId()), JsonUtils.toJson(shareFile));
        }

        ShareResp shareResp = new ShareResp();
        shareResp.setUrl(shareUrl(shareFile.getId()));
        shareResp.setDeletingUrl(deletingUrl(shareFile.getId(), shareFile.getDeletingToken()));
        shareResp.setId(shareFile.getId());
        return shareResp;
    }

    public PubShareFile gerPubFilesByShareId(String shareId){

        ShareFile shareFile = findById(shareId);
        if(shareFile == null){
            throw ApiException.error("Share file not found");
        }
        PubShareFile pubShareFile = new PubShareFile();
        pubShareFile.setId(shareFile.getId());
        pubShareFile.setSrcFiles(shareFile.getSrcFiles());
        pubShareFile.setOps(shareFile.getOps());
        return pubShareFile;
    }

    public ShareFile findById(String id){
        String json = redisTemplate.opsForValue().get(redisKey(id));
        if(json == null){
            return null;
        }
        return JsonUtils.fromJson(json, ShareFile.class);
    }

    public boolean delete(@Valid DeleteShareFileReq req){
        MultiValueMap<String, String> parameters = UriComponentsBuilder
                        .fromUriString(req.getUrl())
                        .build()
                        .getQueryParams();
        String id = parameters.getFirst("s");
        if(id == null){
            throw ApiException.error("Invalid url");
        }
        ShareFile shareFile = findById(id);
        if(shareFile == null){
            throw ApiException.error("Invalid share id");
        }
        if(!shareFile.getDeletingToken().equals(req.getDeletingToken())){
            throw ApiException.error("Invalid delete token");
        }
        return redisTemplate.delete(redisKey(id));

    }
}
