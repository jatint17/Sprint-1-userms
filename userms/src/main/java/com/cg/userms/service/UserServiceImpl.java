package com.cg.userms.service;


import com.cg.userms.exceptions.InvalidIdException;
import com.cg.userms.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cg.userms.entity.User;
import com.cg.userms.exceptions.InvalidPasswordException;
import com.cg.userms.exceptions.InvalidUsernameException;
import com.cg.userms.repository.IUserRepository;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository userRepository;
	
	@Transactional
	@Override
	public User addUser(String username, String password) {
		boolean checker = checkCredentials(username, password);
		if (checker == false) {
			validateUsername(username);
			validatePassword(password);
			User user = new User(username, password);
			user = userRepository.save(user);
			return user;
		}
		return null;
	}

	@Override
	public User findById(Long userId)
	{
		validateId(userId);
		Optional<User> optional = userRepository.findById(userId);
		if(!optional.isPresent())
		{
			throw new UserNotFoundException("User not found");
		}
		User user =optional.get();
		return user;
	}

	@Override
    public boolean checkCredentials(String username, String password)
    {
        if(username==null||username.isEmpty()||password==null||password.isEmpty())
        {
            return false;
        }
        User user = userRepository.findUserByUsername(username);
        if(user==null)
        {
            return false;
        }
        boolean result = user.getUsername().equals(username) && user.getPassword().equals(password);
        return result;
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
	public void validateId (long userId)
	{
		if(userId<0)
		{
			throw new InvalidIdException("Invalid id");
		}
	}
}