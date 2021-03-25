package com.cg.userms.service;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.*;
import com.cg.userms.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {
	@Mock
	IUserRepository userRepository;
	@Spy
	@InjectMocks
	UserServiceImpl userService;

	/*
	 * successfully added user
	 */
	@Test
	public void testAddUser_1() {
		String username = "arpit";
		String password = "password";
		User user = Mockito.mock(User.class);
		User saved = Mockito.mock(User.class);
		doNothing().when(userService).validateUsername(username);
		doNothing().when(userService).validatePassword(password);
		doReturn(false).when(userService).checkCredentials(username,password);
		when(userRepository.save(any(User.class))).thenReturn(saved);
		User result = userService.addUser(username, password);
		assertNotNull(result);
		assertEquals(saved, result);
		verify(userService).validatePassword(password);
		verify(userService).validateUsername(username);
		verify(userRepository).save(any(User.class));
		verify(userService).checkCredentials(username, password);
	}

	/*
	 * empty username
	 */
	@Test
	public void testAddUser_2() {
		String username = "";
		String password = "password";
		doThrow(InvalidUsernameException.class).when(userService).validateUsername(username);
		when(userService.checkCredentials(username, password)).thenReturn(false);
		Executable executable = () -> userService.addUser(username, password);
		assertThrows(InvalidUsernameException.class, executable);
		verify(userService).validateUsername(username);
		verify(userService).checkCredentials(username, password);
	}

	/*
	 * empty password
	 */
	@Test
	public void testAddUser_3() {
		String username = "arpit";
		String password = "";
		doThrow(InvalidPasswordException.class).when(userService).validatePassword(password);
		when(userService.checkCredentials(username, password)).thenReturn(false);
		Executable executable = () -> userService.addUser(username, password);
		assertThrows(InvalidPasswordException.class, executable);
		verify(userService).validatePassword(password);
		verify(userService).checkCredentials(username, password);
	}

	/*
	 * password is null
	 */
	@Test
	public void testAddUser_4() {
		String username = "arpit";
		String password = null;
		doThrow(InvalidPasswordException.class).when(userService).validatePassword(password);
		when(userService.checkCredentials(username, password)).thenReturn(false);
		Executable executable = () -> userService.addUser(username, password);
		assertThrows(InvalidPasswordException.class, executable);
		verify(userService).validatePassword(password);
		verify(userService).checkCredentials(username, password);
	}

	/*
	 * username is null
	 */
	@Test
	public void testAddUser_5() {
		String username = null;
		String password = "password";
		doThrow(InvalidUsernameException.class).when(userService).validateUsername(username);
		when(userService.checkCredentials(username, password)).thenReturn(false);
		Executable executable = () -> userService.addUser(username, password);
		assertThrows(InvalidUsernameException.class, executable);
		verify(userService).validateUsername(username);
		verify(userService).checkCredentials(username, password);
	}

	/*
	 * empty username as input
	 */
	@Test
	public void testValidateUserName_1() {
		String username = "";
		Executable executable = () -> userService.validateUsername(username);
		assertThrows(InvalidUsernameException.class, executable);
	}

	/*
	 * null username as input
	 */
	@Test
	public void testValidateUserName_2() {
		String username = null;
		Executable executable = () -> userService.validateUsername(username);
		assertThrows(InvalidUsernameException.class, executable);
	}

	/*
	 * valid username as input
	 */
	@Test
	public void testValidateUserName_3() {
		String username = "arpit";
		userService.validateUsername(username);
	}

	/*
	 * empty password as input
	 */
	@Test
	public void testValidatePassword_1() {
		String password = "";
		Executable executable = () -> userService.validatePassword(password);
		assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * null password as input
	 */
	@Test
	public void testValidatePassword_2() {
		String password = null;
		Executable executable = () -> userService.validatePassword(password);
		assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * valid password as input
	 */
	@Test
	public void testValidatePassword_3() {
		String password = "password";
		userService.validatePassword(password);
	}

    /**
     * Scenario: username is empty
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
     */
    @Test
    public void testCheckCredentials_3()
    {
        String username = "username";
        String password = "password";
        String enteredUsername = "wrong";
        User user = new User(username,password);
        Mockito.when(userRepository.findUserByUsername(enteredUsername)).thenReturn(null);
        boolean result = userService.checkCredentials(enteredUsername,password);
        Assertions.assertFalse(result);
        Mockito.verify(userRepository).findUserByUsername(enteredUsername);
    }

    /**
     * Scenario: password does not match the database
     */
    @Test
    public void testCheckCredentials_4()
    {
        String username = "username";
        String password = "password";
        String enteredPassword = "wrong";
        User user = new User(username,password);
        Mockito.when(userRepository.findUserByUsername(username)).thenReturn(user);
        boolean result = userService.checkCredentials(username,enteredPassword);
        Assertions.assertFalse(result);
        Mockito.verify(userRepository).findUserByUsername(username);
    }

    /**
     * Scenario: credentials are matching
     */
    @Test
    public void testCheckCredentials_5()
    {
        String username = "username";
        String password = "password";
        User user = new User(username,password);
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
}
