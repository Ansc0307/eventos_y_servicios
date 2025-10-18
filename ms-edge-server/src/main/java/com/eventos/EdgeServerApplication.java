package com.eventos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//añadir luego notaciones de @EnableEurekaClient y @EnableDiscoveryClient
public class EdgeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EdgeServerApplication.class, args);
    }
}