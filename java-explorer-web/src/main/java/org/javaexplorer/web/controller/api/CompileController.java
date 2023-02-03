package org.javaexplorer.web.controller.api;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.web.service.ExplorerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/compile")
@Validated
public class CompileController {
    @Autowired
    private ExplorerService explorerService;

    @PostMapping
    public ApiResp<CompileResult> explorer(
            @RequestBody @Valid CompileReq compileReq) throws IOException {
        return ApiResp.ok(
                explorerService.explore(compileReq)
        );
    }


}