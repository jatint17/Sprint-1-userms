package com.cg.userms.service;

import com.cg.userms.entity.User;
import com.cg.userms.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService
{
    @Autowired
    IUserRepository userRepository;

    @Override
    public User addUser(String username, String password) {
        return null;
    }

    @Override
    public User findById(Long userId) {
        return null;
    }

    @Override
    public boolean checkCredentials(String username, String password)
    {
        if(username==null||username.isEmpty()||password==null||password.isEmpty())
        {
            return false;
        }
        User user = userRepository.findUserByUsername(username);
        if(user==null)
        {
            return false;
        }
        boolean result = user.getUsername().equals(username) && user.getPassword().equals(password);
        return result;
    }
}
