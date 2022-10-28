package org.javaexplorer.web.service;

import org.javaexplorer.compiler.service.CompilerService;
import org.javaexplorer.model.vo.CompileResult;
import org.javaexplorer.model.vo.ExplorerReq;
import org.javaexplorer.model.vo.ExplorerResult;
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


    public ExplorerResult explore(ExplorerReq explorerReq) throws IOException {
        ExplorerResult explorerResult = new ExplorerResult();
        explorerResult.setClassImages(new ArrayList<>());
        CompileResult compileResult = compilerService.compile(
                explorerReq.getCompilerName(),
                explorerReq.getCompilerOptions(),
                explorerReq.getSrcFiles()
        );
        explorerResult.setCode(compileResult.getCode());
        explorerResult.setOutput(compileResult.getOutput());
        if(explorerResult.getCode() == 0){
            explorerResult.setClassImages(parserService.parse(compileResult.getClassFiles()));
        }
        return explorerResult;
    }


}
