package org.javaexplorer.web.client;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.DisassembleReq;
import org.javaexplorer.model.vo.DisassembleResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "disassembler", url = "${disassembler.url}")
public interface DisassemblerClient {
    @PostMapping(value = "/disassemble")
    ApiResp<DisassembleResult> disassemble(@RequestBody DisassembleReq disassembleReq);
}
