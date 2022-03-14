package com.divary.cimbtestrequirement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableWebMvc
@EnableOpenApi
@SpringBootApplication
public class CimbTestRequirementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CimbTestRequirementApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
    }
}
