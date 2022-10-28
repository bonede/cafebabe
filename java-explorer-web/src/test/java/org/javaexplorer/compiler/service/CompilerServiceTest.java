package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(properties = {"compiler.workingDir=javac"})
@Slf4j
class CompilerServiceTest {
    @Autowired
    private CompilerService compilerService;
    private String javaFileName = "Main.java";

    @Test
    void compile() throws IOException {

    }
}