package com.cg.userms.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.cg.userms.dto.AddRequest;
import com.cg.userms.dto.CheckCredentialsRequest;
import com.cg.userms.dto.UserDetailsResponse;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.AddUserException;
import com.cg.userms.exceptions.InvalidIdException;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.exceptions.UserNotFoundException;
import com.cg.userms.service.UserServiceImpl;
import com.cg.userms.util.UserUtil;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@Import({UserController.class, UserServiceImpl.class, UserUtil.class})
@DataJpaTest
@AutoConfigureTestDatabase
public class UserRestControllerIntegrationTest
{
    @Autowired
    UserController userController;
    @Autowired
    EntityManager entityManager;

    /**
     * Scenario: Credentials match the database
     * Input: Persisting a new user in the database, and checking the credentials
     * Expectation: Asserts true for UserController#checkCredentials(CheckCredentialsRequest)
     */
    @Test
    public void testCheckCredentials_1()
    {
        String username = "user";
        String password = "password";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        CheckCredentialsRequest request = new CheckCredentialsRequest(username, password);
        boolean result = userController.checkCredentials(request);
        Assertions.assertTrue(result);
    }
    
    /**
     * Scenario: Credentials do not match the database
     * Input: Persisting a new user in the database, and checking the credentials
     * Expectation: Asserts false for UserController#checkCredentials(CheckCredentialsRequest)
     */
    @Test
    public void testCheckCredentials_2()
    {
        String username = "user";
        String password = "password";
        String enteredUsername = "wrong";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username, password, roles);
        entityManager.persist(user);
        CheckCredentialsRequest request = new CheckCredentialsRequest(enteredUsername, password);
        boolean result = userController.checkCredentials(request);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: User found
     * Input: Persisting a new user in the database, and finding it by userId
     * Expectation: Asserts whether the saved user details and response details are equal
     */
    @Test
    public void testFindById_1()
    {
    	String username = "user";
    	String password = "pass";
    	String role = "role";
    	Set<String> roles = new HashSet<>();
    	roles.add(role);
    	User user = new User(username,password,roles);
    	entityManager.persist(user);
    	Long userId = user.getUserId();
    	UserDetailsResponse response = userController.findById(userId);
    	Assertions.assertNotNull(response);
    	Assertions.assertEquals(userId, response.getUserId());
    	Assertions.assertEquals(username, response.getUsername());
    	Assertions.assertEquals(roles, response.getRoles());
    }
    /**
     * Scenario: userId is negative
     * Input: negative userId in UserController#findById(userId)
     * Expectation: asserts whether InvalidIdException is thrown or not
     */
    @Test
    public void testFindById_2()
    {
    	long userId = -1l;
        Executable executable = ()-> userController.findById(userId);
        Assertions.assertThrows(InvalidIdException.class,executable);
    }


    /**
     * Scenario: userId does not exist in the database
     * Input: userId in UserController#findById(userId)
     * Expectation: asserts whether UserNotFoundException is thrown or not
     */
    @Test
    public void testFindById_3()
    {
    	long  userId = 100;
    	Executable executable=()-> userController.findById(userId);
    	Assertions.assertThrows(UserNotFoundException.class,executable);
   }
    
    /**
     * Scenario: Admin added sucessfully
     * input: object of AddRequest
     * expection: added username and password are the same as the one entered
     */
    @Test
    public void testAddAdmin_1() {
    	String username = "username";
    	String password = "password";
    	AddRequest request = new AddRequest(username, password);
    	UserDetailsResponse response = userController.addAdmin(request);
    	assertEquals(username,response.getUsername());
    	assertEquals(password, response.getPassword());
    }
    
    /**
     * Scenario: Admin not added sucessfully because blank username is provided
     * input: object of AddRequest
     * expection: verifying if InvalidUsernameException is thrown
     */
    @Test
    public void testAddAdmin_2() {
    	String username = "";
    	String password = "password";
    	AddRequest request = new AddRequest(username, password);
    	Executable executable = () -> userController.addAdmin(request);
    	assertThrows(InvalidUsernameException.class, executable);
    }
    
    /**
     * Scenario: Admin not added sucessfully because blank password is provided
     * input: object of AddRequest
     * expection: verifying if InvalidPasswordException is thrown
     */
    @Test
    public void testAddAdmin_3() {
    	String username = "username";
    	String password = "";
    	AddRequest request = new AddRequest(username, password);
    	Executable executable = () -> userController.addAdmin(request);
    	assertThrows(InvalidPasswordException.class, executable);
    }
    
    /**
     * Scenario: Admin not added sucessfully because null username is provided
     * input: object of AddRequest
     * expection: verifying if InvalidUsernameException is thrown
     */
    @Test
    public void testAddAdmin_4() {
    	String username = null;
    	String password = "password";
    	AddRequest request = new AddRequest(username, password);
    	Executable executable = () -> userController.addAdmin(request);
    	assertThrows(InvalidUsernameException.class, executable);
    }
    
    /**
     * Scenario: Admin not added sucessfully because null password is provided
     * input: object of AddRequest
     * expection: verifying if InvalidPasswordException is thrown
     */
    @Test
    public void testAddAdmin_5() {
    	String username = "username";
    	String password = null;
    	AddRequest request = new AddRequest(username, password);
    	Executable executable = () -> userController.addAdmin(request);
    	assertThrows(InvalidPasswordException.class, executable);
    }
    
    /**
     * Scenario: Admin not added sucessfully because username already exists
     * input: object of AddRequest and EntityManage#persist(user)
     * expection: verifying if AddUserException is thrown
     */
    @Test
    public void testAddAdmin_6() {
    	String username = "username";
    	String password = "password";
    	Set<String> roles = new HashSet<>();
        roles.add("admin");
    	AddRequest request = new AddRequest(username, password);
    	User user = new User(username, password, roles);
    	entityManager.persist(user);
    	Executable executable = () -> userController.addAdmin(request);
    	assertThrows(AddUserException.class, executable);
    }
    
}
