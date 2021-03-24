package com.cg.userms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UsermsApplication
{
    public static void main(String[] args)
    {
       ConfigurableApplicationContext context= SpringApplication.run(UsermsApplication.class, args);
    }
}
