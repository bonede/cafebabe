package org.javaexplorer.web.client;

import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.DisassembleReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "disassembler", url = "${disassembler.url}")
public interface DisassemblerClient {
    @PostMapping(value = "/disassemble")
    ApiResp<Map<String, Object>> disassemble(@RequestBody DisassembleReq disassembleReq);
}
