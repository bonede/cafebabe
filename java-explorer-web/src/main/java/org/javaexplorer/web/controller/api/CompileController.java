package org.javaexplorer.web.controller.api;

import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
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
    private CompilerService compilerService;

    @PostMapping
    public ApiResp<CompileOutput> explorer(@RequestBody @Valid CompileReq compileReq) throws IOException {
        return ApiResp.ok(compilerService.compile(compileReq));
    }


}
