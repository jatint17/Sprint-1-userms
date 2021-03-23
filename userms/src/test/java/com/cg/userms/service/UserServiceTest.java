package com.cg.userms.service;

import com.cg.userms.entity.User;
import com.cg.userms.exception.InvalidPasswordException;
import com.cg.userms.exception.InvalidUsernameException;
import com.cg.userms.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    IUserRepository userRepository;

    @Spy
    @InjectMocks
    IUserService userService;

    /**
     * Scenario: username is empty
     */
    @Test
    public void testCheckCredentials_1()
    {
        Executable executable = ()->userService.checkCredentials("","password");
        Assertions.assertThrows(InvalidUsernameException.class,executable);
    }

    /**
     * Scenario: password is empty
     */
    @Test
    public void testCheckCredentials_2()
    {
        Executable executable = ()->userService.checkCredentials("username","");
        Assertions.assertThrows(InvalidPasswordException.class,executable);
    }

    /**
     * Scenario: username does not match the database
     */
    public void testCheckCredentials_3()
    {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        userRepository.save(user);

        Executable executable = ()-> userService.checkCredentials("wrong",password);
        Assertions.assertThrows(InvalidUsernameException.class,executable);
    }

    /**
     * Scenario: password does not match the database
     */
    public void testCheckCredentials_4()
    {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        userRepository.save(user);

        Executable executable = ()-> userService.checkCredentials(username,"wrong");
        Assertions.assertThrows(InvalidUsernameException.class,executable);
    }

    /**
     * Scenario: credentials are matching
     */
    public void testCheckCredentials_5()
    {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
        userRepository.save(user);

        User checked = userService.checkCredentials(username,password);
        Assertions.assertNotNull(checked);
        Assertions.assertEquals(username,checked.getUsername());
        Assertions.assertEquals(password,checked.getPassword());
    }
}