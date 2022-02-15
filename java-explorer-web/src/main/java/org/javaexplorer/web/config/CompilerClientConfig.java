package org.javaexplorer.web.config;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "compiler")
@Data
@Validated
public class CompilerClientConfig {
    /**
     * URL of compiler service.
     * Only used in dev mode.
     * It is compiler nickname in prod mode instead.
     */
    @URL
    private String url;
}
