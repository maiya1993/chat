package com.company.webChat2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;


@SpringBootApplication
@EnableJpaRepositories
public class Application {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Application.class, args);
        System.out.println("Application started ... launching browser now");
        System.out.println("Application started ... launching browser now");
        System.out.println("Application started ... launching browser now");
        openHomePage();
    }

    private static void openHomePage() throws IOException {
        Runtime rt = Runtime.getRuntime();
        rt.exec("rundll32 url.dll,FileProtocolHandler " + "http://localhost:8080");
    }
}