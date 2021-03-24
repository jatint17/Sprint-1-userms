package com.cg.userms.service;

import com.cg.userms.entity.User;
import com.cg.userms.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.EntityManager;

@ExtendWith(SpringExtension.class)
@Import(UserServiceImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class UserServiceImpIntegrationTest {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    EntityManager entityManager;

    /**
     * Scenario: Username is empty
     */
    @Test
    public void testCheckCredentials_1() {
        String username = "";
        String password = "password";
        boolean result = userService.checkCredentials(username, password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: Password is empty
     */
    @Test
    public void testCheckCredentials_2() {
        String username = "user";
        String password = "";
        boolean result = userService.checkCredentials(username, password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: username does not match the database
     */
    @Test
    public void testCheckCredentials_3() {
        String username = "user";
        String password = "password";
        String enteredUsername = "wrong";
        User user = new User(username, password);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(enteredUsername, password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: password does not match the database
     */
    @Test
    public void testCheckCredentials_4() {
        String username = "user";
        String password = "password";
        String enteredPassword = "wrong";
        User user = new User(username, password);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(username, enteredPassword);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: credentials are matching
     */
    @Test
    public void testCheckCredentials_5()
    {
        String username = "user";
        String password = "password";
        User user = new User(username,password);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(username,password);
        Assertions.assertTrue(result);
    }
}