package com.cg.userms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.slf4j.*;

@SpringBootApplication
public class UserApplication {

    private static final Logger Log=LoggerFactory.getLogger(UserApplication.class);

    public static void main(String[] args){
       ConfigurableApplicationContext context= SpringApplication.run(UserApplication.class, args);
    }

}
