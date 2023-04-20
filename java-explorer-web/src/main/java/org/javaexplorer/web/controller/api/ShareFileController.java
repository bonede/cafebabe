package org.javaexplorer.web.controller.api;

import org.javaexplorer.model.ShareFile;
import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CreateShareReq;
import org.javaexplorer.model.vo.DeleteShareFileReq;
import org.javaexplorer.model.vo.ShareResp;
import org.javaexplorer.web.service.ShareFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/share")
@CrossOrigin(originPatterns = {"http://localhost:5173/"})
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
