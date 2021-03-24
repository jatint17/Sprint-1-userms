package com.cg.userms.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cg.userms.entity.User;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.repository.IUserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplUnitTest {
	@Mock
	IUserRepository repository;
	@Spy
	@InjectMocks
	UserServiceImpl service;

	/*
	 * successfully added user
	 */
	@Test
	public void testAddUser_1() {
		String username = "arpit";
		String password = "password";
		User user = Mockito.mock(User.class);
		User saved = Mockito.mock(User.class);
		Mockito.doNothing().when(service).validateUsername(username);
		Mockito.doNothing().when(service).validatePassword(password);
		Mockito.doReturn(saved).when(repository).save(user);
		User result = service.addUser(username, password);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(saved, result);
		Mockito.verify(service).validatePassword(password);
		Mockito.verify(service).validateUsername(username);
		Mockito.verify(repository).save(user);
	}

	/*
	 * empty username
	 */
	@Test
	public void testAddUser_2() {
		String username = "";
		String password = "password";
		Executable executable = () -> service.addUser(username, password);
		Assertions.assertThrows(InvalidUsernameException.class, executable);
	}

	/*
	 * empty password
	 */
	@Test
	public void testAddUser_3() {
		String username = "arpit";
		String password = "";
		Executable executable = () -> service.addUser(username, password);
		Assertions.assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * password is null
	 */
	@Test
	public void testAddUser_4() {
		String username = "arpit";
		String password = null;
		Executable executable = () -> service.addUser(username, password);
		Assertions.assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * username is null
	 */
	@Test
	public void testAddUser_5() {
		String username = null;
		String password = "password";
		Executable executable = () -> service.addUser(username, password);
		Assertions.assertThrows(InvalidUsernameException.class, executable);
	}
	
	/*
	 * username already existing 
	 */
	public void testAddUser_6() {
		
	}

	/*
	 * empty username as input
	 */
	@Test
	public void testValidateUserName_1() {
		String username = "";
		Executable executable = () -> service.validateUsername(username);
		Assertions.assertThrows(InvalidUsernameException.class, executable);
	}

	/*
	 * null username as input
	 */
	@Test
	public void testValidateUserName_2() {
		String username = null;
		Executable executable = () -> service.validateUsername(username);
		Assertions.assertThrows(InvalidUsernameException.class, executable);
	}

	/*
	 * valid username as input
	 */
	@Test
	public void testValidateUserName_3() {
		String username = "arpit";
		service.validateUsername(username);
	}

	/*
	 * empty password as input
	 */
	@Test
	public void testValidatePassword_1() {
		String password = "";
		Executable executable = () -> service.validatePassword(password);
		Assertions.assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * null password as input
	 */
	@Test
	public void testValidatePassword_2() {
		String password = null;
		Executable executable = () -> service.validatePassword(password);
		Assertions.assertThrows(InvalidPasswordException.class, executable);
	}

	/*
	 * valid password as input
	 */
	@Test
	public void testValidatePassword_3() {
		String password = "password";
		service.validatePassword(password);
	}

}
