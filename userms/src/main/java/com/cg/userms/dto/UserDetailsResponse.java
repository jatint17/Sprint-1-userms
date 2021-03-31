package com.cg.userms.dto;


import java.util.Set;

public class UserDetailsResponse {

    private Long userId;
    private String username;
    private String password;
    private Set<String> roles;

    public UserDetailsResponse(){}
    public UserDetailsResponse(Long userId, String username, String password, Set<String> roles)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
