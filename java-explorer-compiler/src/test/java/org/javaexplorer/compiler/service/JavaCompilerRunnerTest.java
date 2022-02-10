package org.javaexplorer.compiler.service;

import lombok.extern.slf4j.Slf4j;
import org.javaexplorer.model.CompileResult;
import org.javaexplorer.model.JavaFile;
import org.javaexplorer.utils.ResourcesUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest(properties = {"compiler.workingDir=/data"})
@Slf4j
class JavaCompilerRunnerTest {
    private static final String JAVA_FILE = "Main.java";
    @Autowired
    private JavaCompilerRunner javaCompilerRunner;

    @Test
    void getCompilerVersion() {
        assertNotNull(javaCompilerRunner.getCompilerVersion());
    }

    @Test
    void getCompiler() {
        assertNotNull(javaCompilerRunner.getCompilerVersion());
    }

    @Test
    void sanitizeCompilerOptions() {
    }


    @Test
    void compileWithRawCompilerOptions() throws IOException {
        JavaFile javaFile = new JavaFile();
        javaFile.setPath("foo/" + JAVA_FILE);
        javaFile.setContent(ResourcesUtils.readResourceToString(
                CompilerServiceTest.class,
                "source/" + JAVA_FILE)
        );
        CompileResult result = javaCompilerRunner.compileWithRawCompilerOptions(
                Arrays.asList(javaFile),
                ""
        );

        log.info("result {}", result);
    }

    @Test
    void compile(){

    }

}