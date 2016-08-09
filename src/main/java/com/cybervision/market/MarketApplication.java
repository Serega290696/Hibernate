package com.cybervision.market;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class MarketApplication {

    public static void main(String[] args) {
//        SpringApplication.run(MarketApplication.class, args);
        SpringApplication.run(new Object[]{HibernateConfiguration.class}, args);
    }

}
