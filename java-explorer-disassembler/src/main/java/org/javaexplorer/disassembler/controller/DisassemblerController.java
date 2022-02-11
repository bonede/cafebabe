package org.javaexplorer.disassembler.controller;

import org.javaexplorer.disassembler.service.DisassemblerService;
import org.javaexplorer.model.DisassembleResult;
import org.javaexplorer.model.vo.ApiResp;
import org.javaexplorer.model.vo.DisassembleReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/disassemble")
@Validated
public class DisassemblerController {
    @Autowired
    private DisassemblerService disassemblerService;

    @PostMapping
    public ApiResp<DisassembleResult> disassemble(
            @RequestBody @Valid DisassembleReq disassembleReq) {
        return ApiResp.ok(
                disassemblerService.disassemble(disassembleReq.getClassFiles())
        );
    }
}
