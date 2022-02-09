package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.compiler.model.CompilerConfig;
import org.javaexplorer.error.ApiException;
import org.javaexplorer.model.CompileResult;
import org.javaexplorer.model.JavaFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


@Service
@Slf4j
public class CompilerService {
    @Autowired
    private CompilerConfig compilerConfig;

    @Autowired
    private CompilerRunner[] compilerRunners;

    public CompileResult compile(
            String compilerNickname,
            String compilerOptions,
            List<JavaFile> javaFiles){
        if(!compilerConfig.getNickname().equals(compilerNickname)){
            throw ApiException.error(
                    "Invalid compiler %s. Only supports %s",
                    compilerNickname,
                    compilerConfig.getNickname()
            );
        }
        try {
            for(CompilerRunner compilerRunner : compilerRunners){
                if(compilerRunner.getSupportCompilerNicknames().contains(compilerNickname)){
                    return compilerRunner.compile(javaFiles, compilerOptions);
                }
            }
        }catch (IOException e){
            log.error("compile error", e);
        }

        throw ApiException.error("No runner for %s", compilerNickname);
    }
}
