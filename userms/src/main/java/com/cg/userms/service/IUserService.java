package com.cg.userms.service;

import org.springframework.transaction.annotation.Transactional;

import com.cg.userms.entity.User;

import java.util.Set;

public interface IUserService
{

    User addUser(String username, String password, Set<String> roles);
    User findById(Long userId);
    boolean checkCredentials(String username, String password);
    User findUserByUsername(String username);
}
