package com.cg.userms.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.repository.IUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Import(UserServiceImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class UserServiceImpIntegrationTest
{

    @Autowired
    UserServiceImpl userService;
    @Autowired
    EntityManager entityManager;

    /*
     * user added successfully
     */
    @Test
    public void testAddUser_1() {
        String username = "arpit";
        String password = "password";
        User result = userService.addUser(username, password);
        assertNotNull(result);
        List<User> users = entityManager.createQuery("from User",User.class).getResultList();
        assertEquals(1,users.size());
        User stored = users.get(0);
        assertEquals(stored.getUserId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(username, stored.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(password, stored.getPassword());
    }
    /*
     * username is blank
     */
    @Test
    public void testAddUser_2() {
        String username = "";
        String password = "password";
        Executable executable=()-> userService.addUser(username, password);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * password is blank
     */
    @Test
    public void testAddUser_3() {
        String username = "arpit";
        String password = "";
        Executable executable=()-> userService.addUser(username, password);
        assertThrows(InvalidPasswordException.class, executable);
    }

    /*
     * username is null
     */
    @Test
    public void testAddUser_4() {
        String username = null;
        String password = "password";
        Executable executable=()-> userService.addUser(username, password);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * password is blank
     */
    @Test
    public void testAddUser_5() {
        String username = "arpit";
        String password = null;
        Executable executable=()-> userService.addUser(username, password);
        assertThrows(InvalidPasswordException.class, executable);
    }

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