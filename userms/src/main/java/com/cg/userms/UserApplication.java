package com.cg.userms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class UserApplication
{
    public static void main(String[] args)
    {
       ConfigurableApplicationContext context= SpringApplication.run(UserApplication.class, args);
//       UserUI userUI=context.getBean(UserUI.class);
//       userUI.start();
    }
}
