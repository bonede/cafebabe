package app.cafebabe.web.controller.api;

import app.cafebabe.model.ShareFile;
import app.cafebabe.model.vo.ApiResp;
import app.cafebabe.model.vo.CreateShareReq;
import app.cafebabe.model.vo.DeleteShareFileReq;
import app.cafebabe.model.vo.ShareResp;
import app.cafebabe.web.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/share")
public class ShareFileController {
    @Autowired
    private ShareFileService shareFileService;

    @PostMapping
    public ApiResp<ShareResp> share(@RequestBody CreateShareReq req, HttpServletRequest httpServletRequest){
        return ApiResp.ok(shareFileService.share(req, httpServletRequest));
    }
    @GetMapping("/{id}")
    public ApiResp<ShareFile.PubShareFile> getShare(@PathVariable String id){
        return ApiResp.ok(shareFileService.gerPubFilesByShareId(id));
    }

    @DeleteMapping
    public ApiResp<Boolean> deleteShare(@RequestBody DeleteShareFileReq req){
        return ApiResp.ok(shareFileService.delete(req));
    }
}
