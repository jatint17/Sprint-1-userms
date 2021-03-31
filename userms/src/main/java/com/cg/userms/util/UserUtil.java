package com.cg.userms.util;

import org.springframework.stereotype.Component;

import com.cg.userms.dto.UserDetailsResponse;
import com.cg.userms.entity.User;

@Component
public class UserUtil
{

    /**
     * utility method to convert the entity object, User, to it's details class, UserDetailsResponse.
     * @param user
     * @return
     */
    public UserDetailsResponse toUserDetails(User user)
    {
        UserDetailsResponse userDetails=new UserDetailsResponse(user.getUserId(), user.getUsername(),
                user.getPassword(), user.getRoles());
        return userDetails;
    }

}
