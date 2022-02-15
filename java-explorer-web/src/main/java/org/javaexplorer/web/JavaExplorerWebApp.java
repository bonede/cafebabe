package org.javaexplorer.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class JavaExplorerWebApp {
    public static void main(String[] args) {
        SpringApplication.run(JavaExplorerWebApp.class, args);
    }
}
