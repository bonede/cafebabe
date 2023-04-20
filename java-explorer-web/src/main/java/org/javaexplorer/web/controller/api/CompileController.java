package org.javaexplorer.web.controller.api;

import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/compile")
@Validated
@CrossOrigin(originPatterns = {"http://localhost:5173/"})
public class CompileController {
    @Autowired
    private CompilerService compilerService;

    @PostMapping
    public ApiResp<CompileOutput> explorer(@RequestBody @Valid CompileReq compileReq) throws IOException {
        return ApiResp.ok(compilerService.compile(compileReq));
    }


}
