package org.javaexplorer.web.client;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.model.vo.CompileResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URI;

@FeignClient(value = "compiler", url = "${compiler.url}")
public interface CompilerClient {
    @PostMapping("/compile")
    ApiResp<CompileResult> compile(URI baseUrl, @RequestBody @Valid CompileReq compileReq);
}
