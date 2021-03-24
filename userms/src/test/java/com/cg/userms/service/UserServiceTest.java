package com.cg.userms.service;

import com.cg.userms.entity.User;
import com.cg.userms.exception.InvalidIdException;
import com.cg.userms.exception.InvalidPasswordException;
import com.cg.userms.exception.InvalidUsernameException;
import com.cg.userms.exception.NullIdException;
import com.cg.userms.exception.UserNotFoundException;
import com.cg.userms.repository.IUserRepository;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @Mock
    IUserRepository userRepository;

    @Spy
    @InjectMocks
    UserServiceImpl userService;

 
    
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
    	Optional<User>optional =Optional.of(user);
    	when (userRepository.findById(userid)).thenReturn(optional);
    	User result=userService.findById(userid);
    	Assertions.assertEquals(user,result);
    	verify (userRepository).findById(userid);
    }
    
//    /**
//     * Scenario: userid is null;
//     */
//    @Test
//    public void testFindById_4()
//    {
//    
//		long userid=(Long) null;
//    	doThrow(NullIdException.class).when(userService).validateId(userid);
//    	Executable executable=()->userService.findById(userid);
//    	Assertions.assertThrows(NullIdException.class, executable);
//    	
//    }
   


}