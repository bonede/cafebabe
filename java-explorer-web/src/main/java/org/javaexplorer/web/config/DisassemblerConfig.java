package org.javaexplorer.web.config;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "disassembler")
@Data
@Validated
public class DisassemblerConfig {
    /**
     * URL of compiler service.
     */
    @NotNull
    @URL
    private String url;
}
