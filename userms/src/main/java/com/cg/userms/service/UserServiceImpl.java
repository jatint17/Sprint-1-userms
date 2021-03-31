package com.cg.userms.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.userms.dto.CustomUserDetails;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.*;
import com.cg.userms.repository.IUserRepository;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService
{

    @Autowired
    private IUserRepository userRepository;

    /**
     * checks if username is unique or not and adds user to the database.
     * @param username
     * @param password
     * @param roles
     * @return
     */
    @Transactional
    @Override
    public User addUser(String username, String password, Set<String> roles)
    {
        validateUsername(username);
        validatePassword(password);
        User user = userRepository.findUserByUsername(username);
        if (user!=null)
        {
            throw new AddUserException("Error! Username *"+username+"* already exists, please enter a different username");
        }
        User created = new User(username, password, roles);
        created = userRepository.save(created);
        return created;
    }

    /**
     * finds and return user from database by userId, throws UserNotFoundException if user does not exist
     * @param userId
     * @return
     */
    @Override
    public User findById(Long userId)
    {
        validateId(userId);
        Optional<User> optional = userRepository.findById(userId);
        if (!optional.isPresent())
        {
            throw new UserNotFoundException("Error! user with id *"+userId+"* not found");
        }
        User user = optional.get();
        return user;
    }

    /**
     * checks the username and password of the user from database and returns true if credentials match, else returns false
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean checkCredentials(String username, String password)
    {
        if (username == null || username.isEmpty() || password == null || password.isEmpty())
        {
            return false;
        }
        User user = userRepository.findUserByUsername(username);
        if (user == null)
        {
            return false;
        }
        boolean result = user.getUsername().equals(username) && user.getPassword().equals(password);
        return result;
    }

    /**
     * finds and returns user by username from database, throws UserNotFoundException if user does not exist.
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username)
    {
        validateUsername(username);
        User user = userRepository.findUserByUsername(username);
        if (user==null)
        {
            throw new UserNotFoundException("Error! user *"+username+"* does not exist.");
        }
        return user;
    }

    /**
     * finds and returns details of user by username from database, throws a checked exception, UserNameNotFoundException.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = findUserByUsername(username);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        return userDetails;
    }

    /**
     * validates if username is null or empty.
     * @param username
     */
    public void validateUsername(String username) {
        if (username == null || username.isEmpty() || username.trim().isEmpty()) {
            throw new InvalidUsernameException("Username cannot be null or empty");
        }
    }

    /**
     * validates if password is null or empty.
     * @param password
     */
    public void validatePassword(String password) {
        if (password == null || password.isEmpty() || password.trim().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be null or empty");
        }
    }

    /**
     * validates if id is valid or not.
     * @param userId
     */
    public void validateId(Long userId) {
        if (userId < 0) {
            throw new InvalidIdException("Invalid id");
        }
    }

}