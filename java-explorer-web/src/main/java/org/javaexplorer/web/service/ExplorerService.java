package org.javaexplorer.web.service;

import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.vo.CompileOutput;
import org.javaexplorer.model.vo.CompileReq;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.parser.service.ParserService;
import org.javaexplorer.web.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
public class ExplorerService {
    @Autowired
    private CompilerService compilerService;

    @Autowired
    private ParserService parserService;

    @Autowired
    private AppConfig appConfig;


    public CompileResult explore(CompileReq compileReq) throws IOException {
        CompileResult compileResult = new CompileResult();
        compileResult.setClassImages(new ArrayList<>());
        CompileOutput compileOutput = compilerService.compile(
                compileReq.getCompilerName(),
                compileReq.getCompilerOptions(),
                compileReq.getSrcFiles()
        );
        compileResult.setReturnCode(compileOutput.getReturnCode());
        compileResult.setStdout(compileOutput.getStdout());
        compileResult.setStderr(compileOutput.getStderr());
        if(compileResult.getReturnCode() == 0){
            compileResult.setClassImages(parserService.parse(compileOutput.getClassFiles()));
        }
        return compileResult;
    }


}
