package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Homework15RestServiceOnSpringApplication{

    private static final Logger logger = LoggerFactory.getLogger(Homework15RestServiceOnSpringApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(Homework15RestServiceOnSpringApplication.class, args);
    }
}
