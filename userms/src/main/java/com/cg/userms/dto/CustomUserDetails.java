package com.cg.userms.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cg.userms.entity.User;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails
{
    private User user;

    /**
     * parameterized constructor to convert User to CustomUserDetails
     * @param user
     */
    public CustomUserDetails(User user)
    {
        this.user=user;
    }

    /**
     * grants authority to each role in a set and returns the new converted set.
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        Set<String> roles = user.getRoles();
        Set<GrantedAuthority> authorities = roles.stream().
                map(role -> {
                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role.toUpperCase());
                    return authority;
                })
                .collect(Collectors.toSet());
        return authorities;
    }

    /**
     * gets password from user
     * @return
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * gets username from user
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * set the boolean value to set whether the account is expired or not
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * set the boolean value to set whether the account is locked or not
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * set the boolean value to set whether the credentials are expired or not
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
