package com.cg.userms.service;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.userms.entity.User;
import com.cg.userms.exceptions.*;
import com.cg.userms.repository.IUserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest
{

	@Mock
	IUserRepository userRepository;
	@Spy
	@InjectMocks
	UserServiceImpl userService;

    /**
     * Scenario: user added successfully
     * input: username, password and set of roles and stubbing the following methods-
     * 		UserServiceImpl# validateUsername(username),UserServiceImpl# validatePassword(password), IUserrepository# findUserByUsername(username), IUserrepository# save(user)
     * expectation: verifying if all stubbed methods have been called and user is added successfully
     */
    @Test
    public void testAddUser_1() {
        String username = "arpit";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        User user = mock(User.class);
        User saved = mock(User.class);
        doNothing().when(userService).validateUsername(username);
        doNothing().when(userService).validatePassword(password);
        when(userRepository.findUserByUsername(username)).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(saved);
        User result = userService.addUser(username, password, roles);
        assertNotNull(result);
        assertEquals(saved, result);
        verify(userService).validatePassword(password);
        verify(userService).validateUsername(username);
        verify(userRepository).save(any(User.class));
        verify(userRepository).findUserByUsername(username);
    }

    /**
     * Scenario: user not added successfully because empty username is entered
     * input: empty username with valid password and set of roles, also stubbing the following methods-
     * 		UserServiceImpl# validateUsername(username)
     * expectation: verifying if InvalidUsernameException is thrown 
     */
    @Test
    public void testAddUser_2() {
        String username = "";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        doThrow(InvalidUsernameException.class).when(userService).validateUsername(username);
        Executable executable = () -> userService.addUser(username, password, roles);
        assertThrows(InvalidUsernameException.class, executable);
        verify(userService).validateUsername(username);
    }

    /**
     * Scenario: user not added successfully because empty password is entered
     *  input: empty password with valid username and set of roles, also stubbing the following methods-
     * 		UserServiceImpl# validatePassword(password)
     * expectation: verifying if InvalidPasswordException is thrown 
     */
    @Test
    public void testAddUser_3() {
        String username = "arpit";
        String password = "";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        doThrow(InvalidPasswordException.class).when(userService).validatePassword(password);
        Executable executable = () -> userService.addUser(username, password, roles);
        assertThrows(InvalidPasswordException.class, executable);
        verify(userService).validatePassword(password);
    }

    /**
     * Scenario: user not added successfully because password is null
     * input: null password with valid username and set of roles, also stubbing the following methods-
     * 		UserServiceImpl# validatePassword(password)
     * expectation: verifying if InvalidPasswordException is thrown 
     */
    @Test
    public void testAddUser_4() {
        String username = "arpit";
        String password = null;
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        doThrow(InvalidPasswordException.class).when(userService).validatePassword(password);
        Executable executable = () -> userService.addUser(username, password, roles);
        assertThrows(InvalidPasswordException.class, executable);
        verify(userService).validatePassword(password);
    }

    /**
     * Scenario: user not added successfully because username is null
     * input: null username with valid password and set of roles, also stubbing the following methods-
     * 		UserServiceImpl# validateUsername(username)
     * expectation: verifying InvalidUsernameException is thrown 
     */
    @Test
    public void testAddUser_5() {
        String username = null;
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        doThrow(InvalidUsernameException.class).when(userService).validateUsername(username);
        Executable executable = () -> userService.addUser(username, password, roles);
        assertThrows(InvalidUsernameException.class, executable);
        verify(userService).validateUsername(username);
    }

    /**
     * Scenario: user not added successfully because username already exists
     * input: existing valid username with valid password and set of roles and stubbing the following methods: 
     * 		UserServiceImpl# validateUsername(username),UserServiceImpl# validatePassword(password), IUserrepository# findUserByUsername(username)
     * expectation: verifying if AddUserException is thrown 
     */
    @Test
    public void testAddUser_6() {
        String username = "arpit";
        String password = "password";
        String role = "role1";
        Set<String> roles = new HashSet<>();
        roles.add(role);
        User user = mock(User.class);
        doNothing().when(userService).validateUsername(username);
        doNothing().when(userService).validatePassword(password);
        when(userRepository.findUserByUsername(username)).thenReturn(user);
        Executable executable = () -> userService.addUser(username, password, roles);
        assertThrows(AddUserException.class,executable);
        verify(userService).validatePassword(password);
        verify(userService).validateUsername(username);
        verify(userRepository).findUserByUsername(username);
    }

    /*
     * Scenario: empty username as input
     */
    @Test
    public void testValidateUserName_1() {
        String username = "";
        Executable executable = () -> userService.validateUsername(username);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * Scenario: null username as input
     */
    @Test
    public void testValidateUserName_2() {
        String username = null;
        Executable executable = () -> userService.validateUsername(username);
        assertThrows(InvalidUsernameException.class, executable);
    }

    /*
     * Scenario: valid username as input
     */
    @Test
    public void testValidateUserName_3() {
        String username = "arpit";
        userService.validateUsername(username);
    }

    /*
     * Scenario: empty password as input
     */
    @Test
    public void testValidatePassword_1() {
        String password = "";
        Executable executable = () -> userService.validatePassword(password);
        assertThrows(InvalidPasswordException.class, executable);
    }

    /*
     * Scenario: null password as input
     */
    @Test
    public void testValidatePassword_2() {
        String password = null;
        Executable executable = () -> userService.validatePassword(password);
        assertThrows(InvalidPasswordException.class, executable);
    }

    /*
     * Scenario: valid password as input
     */
    @Test
    public void testValidatePassword_3() {
        String password = "password";
        userService.validatePassword(password);
    }

    /**
     * Scenario: username is empty
     * input: empty username and password in IUserService##checkCredentials(username,password)
     * expectation: asserting IUserService##checkCredentials(username,password) is false
     */
    @Test
    public void testCheckCredentials_1()
    {
        String username="";
        String password="password";
        boolean result = userService.checkCredentials(username,password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: password is empty
     * input: username and empty password in IUserService#checkCredentials(username,password)
     * expectation: asserting IUserService##checkCredentials(username,password) is false
     */
    @Test
    public void testCheckCredentials_2()
    {
        String username="username";
        String password="";
        boolean result = userService.checkCredentials(username,password);
        Assertions.assertFalse(result);
    }

    /**
     * Scenario: username does not match the database
     * input: mocking IUserRepository#findUserByUsername(username), returning null and verifying it is called
     * expectation: asserting IUserService##checkCredentials(enteredUsername,password) is false
     */
    @Test
    public void testCheckCredentials_3()
    {
        String username = "username";
        String password = "password";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        String enteredUsername = "wrong";
        User user = new User(username,password,roles);
        Mockito.when(userRepository.findUserByUsername(enteredUsername)).thenReturn(null);
        boolean result = userService.checkCredentials(enteredUsername,password);
        Assertions.assertFalse(result);
        Mockito.verify(userRepository).findUserByUsername(enteredUsername);
    }

    /**
     * Scenario: password does not match the database
     * input: mocking IUserRepository#findUserByUsername(username), returning user and verifying it is called
     * expectation: asserting IUserService#checkCredentials(username,enteredPassword) is false
     */
    @Test
    public void testCheckCredentials_4()
    {
        String username = "username";
        String password = "password";
        String enteredPassword = "wrong";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username,password,roles);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(user);
        boolean result = userService.checkCredentials(username,enteredPassword);
        Assertions.assertFalse(result);
        Mockito.verify(userRepository).findUserByUsername(username);
    }

    /**
     * Scenario: credentials are matching the database
     * input: mocking IUserRepository#findUserByUsername(username), returning user and verifying it is called
     * expectation: asserting IUserService#checkCredentials(username,password) is true
     */
    @Test
    public void testCheckCredentials_5()
    {
        String username = "username";
        String password = "password";
        Set<String> roles = new HashSet<>();
        roles.add("role1");
        User user = new User(username,password,roles);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(user);
        boolean result = userService.checkCredentials(username,password);
        Assertions.assertTrue(result);
        Mockito.verify(userRepository).findUserByUsername(username);
    }

	/**
	 * Scenario: userid is negative
	 */
	@Test
	public void testFindById_1()
	{
		long userid=-1;
		doThrow(InvalidIdException.class).when(userService).validateId(userid);
		Executable executable=()->userService.findById(userid);
		Assertions.assertThrows(InvalidIdException.class, executable);
	}


	/**
	 * Scenario: userid does not exist in the database
	 */

	@Test
	public void testFindById_2()
	{
		long userid=100;
		doNothing().when(userService).validateId(userid);
		User user=mock(User.class);
		Optional<User>optional=Optional.empty();

		when(userRepository.findById(userid)).thenReturn(optional);
		Executable executable=()->userService.findById(userid);
		Assertions.assertThrows(UserNotFoundException.class,executable);
	}
	/**
	 * Scenario: userid exist in the database
	 */
	@Test
	public void testFindById_3()
	{
		long userid=3;
		doNothing().when(userService).validateId(userid);
		User user=mock(User.class);
		Optional<User>optional = Optional.of(user);
		when (userRepository.findById(userid)).thenReturn(optional);
		User result=userService.findById(userid);
		Assertions.assertEquals(user,result);
		verify (userRepository).findById(userid);
	}

    /**
     * Scenario: user exists
     * input: stubbing validateUsername method, mocking IUserRepository#findUserByUsername(username), returning saved user and verifying it is called
     * expectation: asserting IUserService#findUserByUsername(username) is not null, saved user and result user are equal
     */
    @Test
    public void testFindUserByUsername_1()
    {
        String username="user";
        User saved = mock(User.class);
        doNothing().when(userService).validateUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(saved);
        User result = userService.findUserByUsername(username);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(saved,result);
        verify(userRepository).findUserByUsername(username);
    }

    /**
     * Scenario: user does not exist
     * input: stubbing validateUsername method, mocking IUserRepository#findUserByUsername(username), returning null and verifying it is called
     * expectation: asserting IUserService#findUserByUsername(username) throws UserNotFoundException
     */
    @Test
    public void testFindUserByUsername_2()
    {
        String username="user";
        doNothing().when(userService).validateUsername(username);
        when(userRepository.findUserByUsername(username)).thenReturn(null);
        Executable executable = ()-> userService.findUserByUsername(username);
        Assertions.assertThrows(UserNotFoundException.class,executable);
        verify(userRepository).findUserByUsername(username);
    }

}
