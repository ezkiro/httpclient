package com.toyfactory.httpclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HttpclientApplication {
    static {
        java.security.Security.setProperty ("networkaddress.cache.ttl" , "5");
    }
    public static void main(String[] args) {
        SpringApplication.run(HttpclientApplication.class, args);
    }

}
