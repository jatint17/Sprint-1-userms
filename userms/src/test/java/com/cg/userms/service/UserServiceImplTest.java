package com.cg.userms.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.cg.userms.entity.User;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;

@ExtendWith(MockitoExtension.class)
@Import(UserServiceImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase
public class UserServiceImplTest {
	@Autowired
	EntityManager entityManager;
	@Autowired
	UserServiceImpl service;
	/*
	 * user added successfully
	 */
	@Test
	public void testAddUser_1() {
		String username = "arpit";
		String password = "password";
		User result = service.addUser(username, password);
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
		Executable executable=()->service.addUser(username, password);
        assertThrows(InvalidUsernameException.class, executable);
	}
	
	/*
	 * password is blank
	 */
	@Test
	public void testAddUser_3() {
		String username = "arpit";
		String password = "";
		Executable executable=()->service.addUser(username, password);
		assertThrows(InvalidPasswordException.class, executable);
	}
	
	/*
	 * username is null
	 */
	@Test
	public void testAddUser_4() {
		String username = null;
		String password = "password";
		Executable executable=()->service.addUser(username, password);
        assertThrows(InvalidUsernameException.class, executable);
	}
	
	/*
	 * password is blank
	 */
	@Test
	public void testAddUser_5() {
		String username = "arpit";
		String password = null;
		Executable executable=()->service.addUser(username, password);
		assertThrows(InvalidPasswordException.class, executable);
	}
}
