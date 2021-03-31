package com.cg.userms.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.userms.dto.AddRequest;
import com.cg.userms.dto.CheckCredentialsRequest;
import com.cg.userms.dto.UserDetailsResponse;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.AddUserException;
import com.cg.userms.exceptions.InvalidIdException;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.exceptions.UserNotFoundException;
import com.cg.userms.service.IUserService;
import com.cg.userms.util.UserUtil;

import java.util.HashSet;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class UserRestControllerUnitTest
{

    @Mock
    IUserService userService;
    @Mock
    UserUtil userUtil;
    @Spy
    @InjectMocks
    UserController userController;

    /**
     * Scenario: credentials are correct
     * Input: mocking IUserService#checkCredentials(username,password), returning true and verifying it is called
     * Expectation: asserting UserController#checkCredentials(request) is true
     */
    @Test
    public void testCheckCredentials_1()
    {
        String username="user";
        String password="password";
        CheckCredentialsRequest request = new CheckCredentialsRequest(username,password);
        when(userService.checkCredentials(username,password)).thenReturn(true);
        boolean result = userController.checkCredentials(request);
        Assertions.assertTrue(result);
        verify(userService).checkCredentials(username,password);
    }

    /**
     * Scenario: credentials are incorrect
     * Input: mocking IUserService#checkCredentials(username,password), returning false and verifying it is called
     * Expectation: asserting UserController#checkCredentials(request) is false
     */
    @Test
    public void testCheckCredentials_2()
    {
        String username="user";
        String password="password";
        CheckCredentialsRequest request = new CheckCredentialsRequest(username,password);
        when(userService.checkCredentials(username,password)).thenReturn(false);
        boolean result = userController.checkCredentials(request);
        Assertions.assertFalse(result);
        verify(userService).checkCredentials(username,password);
    }


    /**
     * Scenario: user found
     * Input: mocking IUserService#findUserByUsername(username), returning User and verifying it is called,
     * 		  mocking userUtil#toUserDetails(user), returning UserDetailsResponse and verifying it is called
     * Expectation: asserting UserDetailsResponse userDetails and result is same
     */
    @Test
    public void testFindByUsername_1()
    {
        String username="user";
        User user = mock(User.class);
        UserDetailsResponse userDetails = mock(UserDetailsResponse.class);
        when(userService.findUserByUsername(username)).thenReturn(user);
        when(userUtil.toUserDetails(user)).thenReturn(userDetails);
        UserDetailsResponse result = userController.findByUsername(username);
        Assertions.assertSame(userDetails,result);
        verify(userService).findUserByUsername(username);
        verify(userUtil).toUserDetails(user);
    }

    /**
     * Scenario: user not found
     * Input: stubbing IUserService#findUserByUsername(username), and throwing UserNotFoundException and verifying it is called
     * Expectation: asserting UserController#findUserByUsername(username) throws UserNotFoundException
     */
    @Test
    public void testFindByUsername_2()
    {
        String username="user";
        doThrow(UserNotFoundException.class).when(userService).findUserByUsername(username);
        Executable executable = ()-> userController.findByUsername(username);
        Assertions.assertThrows(UserNotFoundException.class,executable);
        verify(userService).findUserByUsername(username);
    }

    
    /**
	 * Scenario: User added successfully
	 * input: object of CreateUserRequest class and stubbing the following methods-
     * 		UserServiceImpl# addUser(username, password, set of roles), UserUtil# toUserDetails(user)
     * expectation: verifying if all stubbed methods have been called and user is added successfully
	 */
	@Test
	public void testAddAdmin_1() {
		String username = "username";
		String password = "password";
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		User user = mock(User.class);
		when(userService.addUser(username, password, roles)).thenReturn(user);
		UserDetailsResponse details = mock(UserDetailsResponse.class);
		when(userUtil.toUserDetails(user)).thenReturn(details);
		UserDetailsResponse response = userController.addAdmin(request);
		assertEquals(details, response);
		verify(userService).addUser(username, password, roles);
		verify(userUtil).toUserDetails(user);
	}
	
	/**
	 * Scenario: User not added successfully due to username validation failure
	 * input: object of CreateUserRequest class and stubbing the following methods- 
	 * 			UserServiceImpl# addUser(blank username, password, roles)
	 * expectation: verifying if InvalidUsernameException is thrown
	 */
	@Test
	public void testAddAdmin_2() {
		String username = "";
		String password = "password";
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		doThrow(InvalidUsernameException.class).when(userService).addUser(username, password, roles);
		Executable executable = ()-> userController.addAdmin(request);
		assertThrows(InvalidUsernameException.class, executable);
		verify(userService).addUser(username, password, roles);
	}
	
	/**
	 * Scenario: user not added successfully because blank password provided
	 * input: object of CreateUserRequest class and stubbing the following methods- 
	 * 			UserServiceImpl# addUser(username, blank password, roles)
	 * expectation: verifying if InvalidPasswordException is thrown
	 */
	@Test
	public void testAddAdmin_3() {
		String username = "username";
		String password = "";
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		doThrow(InvalidPasswordException.class).when(userService).addUser(username, password, roles);
		Executable executable = ()-> userController.addAdmin(request);
		assertThrows(InvalidPasswordException.class, executable);
		verify(userService).addUser(username, password, roles);
	}
	
	/**
	 * Scenario: user not added successfully because null username provided
	 * input: object of CreateUserRequest class and stubbing the following methods- 
	 * 			UserServiceImpl# addUser(null username, password, roles)
	 * expectation: verifying if InvalidUsernameException is thrown
	 */
	@Test
	public void testAddAdmin_4() {
		String username = null;
		String password = "password";
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		doThrow(InvalidUsernameException.class).when(userService).addUser(username, password, roles);
		Executable executable = ()-> userController.addAdmin(request);
		assertThrows(InvalidUsernameException.class, executable);
		verify(userService).addUser(username, password, roles);
	}
	
	/**
	 * Scenario: user not added successfully because null password provided
	 * input: object of CreateUserRequest class and stubbing the following methods- 
	 * 			UserServiceImpl# addUser(username, null password, roles)
	 * expectation: verifying if InvalidPasswordException is thrown
	 */
	@Test
	public void testAddAdmin_5() {
		String username = "username";
		String password = null;
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		doThrow(InvalidPasswordException.class).when(userService).addUser(username, password, roles);
		Executable executable = ()-> userController.addAdmin(request);
		assertThrows(InvalidPasswordException.class, executable);
		verify(userService).addUser(username, password, roles);
	}
	
	/**
	 * Scenario: user not added successfully because username already exists
	 * input: object of CreateUserRequest class and stubbing the following methods- 
	 * 			UserServiceImpl# addUser(username, password, roles)
	 * expectation: verifying if AddUserException is thrown
	 */
	@Test
	public void testAddAdmin_6() {
		String username = "username";
		String password = "password";
		String role = "admin";
		Set<String> roles = new HashSet<>();
		roles.add(role);
		AddRequest request = new AddRequest(username, password);
		doThrow(AddUserException.class).when(userService).addUser(username, password, roles);
		Executable executable = ()-> userController.addAdmin(request);
		assertThrows(AddUserException.class, executable);
		verify(userService).addUser(username, password, roles);
	}

	/**
     * Scenario: userId matches the database, user found
     * Input: mocking IUserService#findById(userId), returning a User and verifying it is called,
     * 		  mocking userUtil#toUserDetails(user), returning UserDetailsResponse and verifying it is called
     * Expectation: asserting UserDetailsResponse userDetails and result is same
     */
    @Test
	public void testFindById_1() 
	{
		long userId = 1;
		User user = mock(User.class);
		UserDetailsResponse userDetails = mock(UserDetailsResponse.class);
		when(userService.findById(userId)).thenReturn(user);
		when(userUtil.toUserDetails(user)).thenReturn(userDetails);
		UserDetailsResponse result = userController.findById(userId);
		assertSame(userDetails, result);
		verify(userService).findById(userId);
		verify(userUtil).toUserDetails(user);
	}
    

    /**
     * Scenario: user not found
     */
    @Test
    public void testFindById_2() 
    {
        long userId=100;
        doThrow(UserNotFoundException.class).when(userService).findById(userId);
        Executable executable = ()-> userController.findById(userId);
        Assertions.assertThrows(UserNotFoundException.class,executable);
        verify(userService).findById(userId);
    }
    /**
	 * Scenario: userid is negative
	 */
	@Test
	public void testFindById_3()
	{
		long userId=-1;
		doThrow(InvalidIdException.class).when(userService).findById(userId);
		Executable executable=()->userService.findById(userId);
		Assertions.assertThrows(InvalidIdException.class, executable);
	}
}
