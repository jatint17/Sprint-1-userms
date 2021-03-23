package com.cg.userms.service;

import com.cg.userms.entity.User;

public interface IUserService
{
    User addUser(String username, String password);
    User findById(Long userId);
    boolean checkCredentials(String username, String password);
    //should this be boolean or void?
}
