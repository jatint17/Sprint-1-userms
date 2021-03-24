package com.cg.userms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.userms.entity.User;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.repository.IUserRepository;

public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository repository;

	@Override
	public User addUser(String username, String password) {
		validateUsername(username);
		validatePassword(password);
		User user = new User(username, password);
		user.setUserId(1L);
		user = repository.save(user);
		return user;
	}

	@Override
	public User findById(Long userId) {
		return null;
	}

	@Override
	public boolean checkCredentials(String username, String password) {
		return false;
	}

	public void validateUsername(String username) {
		if (username == null || username.isEmpty() || username.trim().isEmpty()) {
			throw new InvalidUsernameException("Username cannot be null or empty");
		}
	}

	public void validatePassword(String password) {
		if (password == null || password.isEmpty() || password.trim().isEmpty()) {
			throw new InvalidPasswordException("Password cannot be null or empty");
		}
	}

}
