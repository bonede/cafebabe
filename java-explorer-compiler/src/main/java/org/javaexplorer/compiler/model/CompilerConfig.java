package org.javaexplorer.compiler.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "compiler")
@Data
@Validated
public class CompilerConfig {
    /**
     * Compiler nickname for this instance. <code>java8</code> e.g.
     */
    @NotNull
    private String nickname;
}
