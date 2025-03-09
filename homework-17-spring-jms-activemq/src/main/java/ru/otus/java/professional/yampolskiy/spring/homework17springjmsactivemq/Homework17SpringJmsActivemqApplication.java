package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJms
@EnableScheduling
@SpringBootApplication
public class Homework17SpringJmsActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(Homework17SpringJmsActivemqApplication.class, args);
    }

}
