package com.cg.userms;

import java.util.HashSet;
import java.util.Set;

import com.cg.userms.service.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import com.cg.userms.entity.User;
import com.cg.userms.service.IUserService;

@SpringBootApplication
public class UserApplication
{
    public static void main(String[] args)
    {
       ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args);
       IUserService service = context.getBean(UserServiceImpl.class);
       Set<String> roles = new HashSet<>();
       roles.add("admin");
       User admin = service.addUser("admin", "admin", roles);
    }
}
