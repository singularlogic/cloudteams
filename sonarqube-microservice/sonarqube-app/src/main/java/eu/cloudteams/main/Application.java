package eu.cloudteams.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan({
    //Configuration imports
    "eu.cloudteams.configuration",
    "eu.cloudteams.repository.service",
    "eu.cloudteams.controller",
    "eu.cloudteams.authentication",})
@EntityScan({"eu.cloudteams.repository.domain"})
@EnableJpaRepositories({"eu.cloudteams.repository.dao"})
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
