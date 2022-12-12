package org.javaexplorer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties("compiler")
@Component
@Data
public class CompilerConfig {
    private String workingDir;
    private String buildDir;
    private List<Compiler> compilers;
    @Data
    public static class Compiler{
        private String name;
        private String cmd;
        private String example;
    }
}
