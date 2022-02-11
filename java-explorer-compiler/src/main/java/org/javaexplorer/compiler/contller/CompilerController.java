package org.javaexplorer.compiler.contller;

import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.CompileResult;
import org.javaexplorer.model.CompilerInfo;
import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CompileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/compile")
@Validated
public class CompilerController {
    @Autowired
    private CompilerService compilerService;

    @PostMapping
    ApiResp<CompileResult> compile(@RequestBody @Valid CompileReq compileReq){
        return ApiResp.ok(compilerService.compile(
                compileReq.getCompilerNickname(),
                compileReq.getCompilerOptions(),
                compileReq.getJavaFiles()
        ));
    }

    @GetMapping
    ApiResp<CompilerInfo> getCompilerInfo(){
        return ApiResp.ok(compilerService.getCompilerInfo());
    }
}
