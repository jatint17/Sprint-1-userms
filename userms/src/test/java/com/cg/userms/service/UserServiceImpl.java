package com.cg.userms.service;

import static org.assertj.core.api.Assertions.useDefaultRepresentation;
import static org.assertj.core.api.Assertions.useRepresentation;

import java.util.Optional;

import com.cg.userms.entity.User;
import com.cg.userms.exception.InvalidIdException;
import com.cg.userms.exception.UserNotFoundException;
import com.cg.userms.repository.IUserRepository;

public class UserServiceImpl implements IUserService {
	
	private IUserRepository userRepository;
	
	@Override
	public User addUser(String username,String password)
	{
		return null;
	}
	
	
	@Override
	public User findById(Long userId)
	{
		validateId(userId);
		Optional<User>optional = userRepository.findById(userId);
		if(!optional.isPresent())
		{
			throw new UserNotFoundException("User not found");
		}
		User user =optional.get();
		return user;
	}
		

	@Override
	public User checkCredentials(String username,String password)
	{
		return null;
	}
	
	public void validateId (long userId)
	{
		if(userId<0){
			throw new InvalidIdException("Invalid id");
			}
	}

}
