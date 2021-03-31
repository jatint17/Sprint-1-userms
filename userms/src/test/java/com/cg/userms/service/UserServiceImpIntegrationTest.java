package com.cg.userms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cg.userms.entity.User;
import com.cg.userms.exceptions.AddUserException;
import com.cg.userms.exceptions.InvalidIdException;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.exceptions.UserNotFoundException;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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
     * Scenario: user added successfully
     * input: valid username,valid password, valid set of roles
     * expectation: verifying if username, password and set of roles stored is the same as the one entered
     */
    @Test
    public void testAddUser_1() {
        String username = "arpit";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        User result = userService.addUser(username, password,roles);
        assertNotNull(result);
        List<User> users = entityManager.createQuery("from User", User.class).getResultList();
        assertEquals(1,users.size());
        User stored = users.get(0);
        assertEquals(stored.getUserId(), result.getUserId());
        assertEquals(username, result.getUsername());
        assertEquals(username, stored.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(password, stored.getPassword());
        assertEquals(roles, result.getRoles());
        assertEquals(roles, stored.getRoles());
    }
    /*
     * Scenario: user not added successfully because username is blank
     * input: blank username, valid password and valid set of roles
     * expectation: verifying if InvalidUsernameException is thrown
     */
    @Test
    public void testAddUser_2() {
        String username = "";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        Executable executable=()-> userService.addUser(username, password, roles);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * Scenario: user not added successfully because password is blank
     * input: valid username, blank password and valid set of roles
     * expectation: verifying if InvalidPasswordException is thrown
     */
    @Test
    public void testAddUser_3() {
        String username = "arpit";
        String password = "";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        Executable executable=()-> userService.addUser(username, password, roles);
        assertThrows(InvalidPasswordException.class, executable);
    }

    /*
     * Scenario: User not added successfully because username is null
     * input: null username, valid password and valid set of roles
     * expectation: verifying if InvalidUsernameException is thrown
     */
    @Test
    public void testAddUser_4() {
        String username = null;
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        Executable executable=()-> userService.addUser(username, password, roles);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * Scenario: user not added successfully because password is null
     * input: valid username, null password and valid set of roles
     * expectation: verifying if InvalidPasswordException is thrown
     */
    @Test
    public void testAddUser_5() {
        String username = "arpit";
        String password = null;
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        Executable executable=()-> userService.addUser(username, password, roles);
        assertThrows(InvalidPasswordException.class, executable);
    }

    /*
     * Scenario: user not added successfully because username already exists
     * input: valid username, valid password and valid set of roles
     * expectation: verifying if AddUserException is thrown
     */
    @Test
    public void testAddUser_6() {
        String username = "arpit";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        User user = userService.addUser(username,password,roles);
        Executable executable=()-> userService.addUser(username,password,roles);
        assertThrows(AddUserException.class, executable);
    }

    /**
     * Scenario: Username is empty
     * Input: empty username on userService#checkCredentials(username,password)
     * Output: Assert the result to be false
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
     * Input: empty password on userService#checkCredentials(username,password)
     * Output: Assert the result to be false
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
     * Input: wrong username on userService#checkCredentials(username,password)
     * Output: Assert the result to be false
     */
    @Test
    public void testCheckCredentials_3() {
        String username = "user";
        String password = "password";
        String enteredUsername = "wrong";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(enteredUsername, password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: password does not match the database
     * Input: wrong password on userService#checkCredentials(username,password)
     * Output: Assert the result to be false
     */
    @Test
    public void testCheckCredentials_4() {
        String username = "user";
        String password = "password";
        String enteredPassword = "wrong";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(username, enteredPassword);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: credentials are matching
     * Input: correct username and password on userService#checkCredentials(username,password)
     * Output: Assert the result to be true
     */
    @Test
    public void testCheckCredentials_5()
    {
        String username = "user";
        String password = "password";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        boolean result = userService.checkCredentials(username,password);
        Assertions.assertTrue(result);
    }

    /**
     * Scenario: user exists in the database
     * Input: correct username on userService#findUserByUsername(username)
     * Output: Assert the result to be not null and assert saved user and result user to be equal
     */
    @Test
    public void testFindUserByUsername_1()
    {
        String username = "user";
        String password = "password";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User saved = new User(username,password,roles);
        entityManager.persist(saved);
        User result = userService.findUserByUsername(username);
        assertNotNull(result);
        assertEquals(saved,result);
    }

    /**
     * Scenario: user does not exist in the database
     * Input: incorrect username on userService#findUserByUsername(enteredUsername)
     * Output: Assert the result to throw UserNotFoundException
     */
    @Test
    public void testFindUserByUsername_2()
    {
        String username = "user";
        String password = "password";
        String enteredUsername = "wrong";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        Executable executable = ()-> userService.findUserByUsername(enteredUsername);
        assertThrows(UserNotFoundException.class,executable);
    }

    /**
     * Scenario: negative userId
     */
    @Test
    public void testFindById_1()
    {
    	long userId = -1l;
        Executable executable = ()->userService.findById(userId);
        Assertions.assertThrows(InvalidIdException.class,executable);
    }


    /**
     * Scenario: userId does not exist in the database
     */
    @Test
    public void testFindById_2()
    {
    	long  userId = 100;
    	Executable executable=()->userService.findById(userId);
    	Assertions.assertThrows(UserNotFoundException.class,executable);
   }

    /**
     * Scenario: userId exist in database
     */
    @Test
    public void testFindById_3()
    {
    	String username = "user";
        String password = "password";
        String role = "role";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        User user = new User(username, password, roles);
        entityManager.persist(user);
        Long assignedUserId=user.getUserId();
	  	User result=userService.findById(assignedUserId);
	  	Assertions.assertNotNull(result);
	  	Assertions.assertEquals(assignedUserId,result.getUserId());
	  	Assertions.assertEquals(user,result);
   }

}
