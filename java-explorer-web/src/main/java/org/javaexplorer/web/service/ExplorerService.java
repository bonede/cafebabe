package org.javaexplorer.web.service;

import org.javaexplorer.error.ApiException;
import org.javaexplorer.model.vo.*;
import org.javaexplorer.web.client.CompilerClient;
import org.javaexplorer.web.client.DisassemblerClient;
import org.javaexplorer.web.config.CompilerClientConfig;
import org.javaexplorer.web.config.DisassemblerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class ExplorerService {
    @Autowired
    private CompilerClient compilerClient;

    @Autowired
    private DisassemblerClient disassemblerClient;

    @Autowired
    private CompilerClientConfig compilerClientConfig;

    @Autowired
    private DisassemblerConfig disassemblerConfig;

    @Autowired
    private StorageService storageService;

    public ApiResp<CompileResult> compile(String compilerNickname, CompileReq compileReq){
        URI uri = compilerClientConfig.getUrl() != null ? java.net.URI.create(compilerClientConfig.getUrl()) :
                java.net.URI.create("http://" + compilerNickname);
        return compilerClient.compile(uri, compileReq);
    }

    public ApiResp<DisassembleResult> disassemble(DisassembleReq disassembleReq){
        return disassemblerClient.disassemble(disassembleReq);
    }

    public ExplorerResult explore(ExplorerReq explorerReq){
        CompileReq compileReq = new CompileReq();
        compileReq.setCompilerNickname(explorerReq.getCompilerNickname());
        compileReq.setJavaFiles(explorerReq.getJavaFiles());
        compileReq.setCompilerOptions(explorerReq.getCompilerOptions());
        ExplorerResult explorerResult = new ExplorerResult();
        ApiResp<CompileResult> compileResult = compile(explorerReq.getCompilerNickname(), compileReq);
        if(compileResult.isSuccess()){
            throw ApiException.error(compileResult.getMsg());
        }
        if(!compileResult.getData().isSuccess()){
            explorerResult.setSuccess(false);
            explorerResult.setMsg(compileResult.getMsg());
            return explorerResult;
        }
        DisassembleReq disassembleReq = new DisassembleReq();
        disassembleReq.setClassFiles(compileResult.getData().getClassFiles());
        ApiResp<DisassembleResult> disassembleResult = disassemble(disassembleReq);
        if(disassembleResult.isSuccess()){
            throw ApiException.error(disassembleResult.getMsg());
        }

        if(!disassembleResult.getData().isSuccess()){
            explorerResult.setSuccess(false);
            explorerResult.setMsg(disassembleResult.getMsg());
            return explorerResult;
        }

        explorerResult.setSuccess(true);
        explorerResult.setDisassembledClassFiles(disassembleResult.getData().getDisassembledClassFiles());
        if(explorerReq.isSave()){
            explorerResult.setShareUrl(storageService.storeWithUrl(
                    compileReq.getCompilerNickname(),
                    compileReq.getCompilerNickname(),
                    compileReq.getJavaFiles(),
                    disassembleResult.getData().getDisassembledClassFiles()
            ));
        }
        return explorerResult;
    }


}
