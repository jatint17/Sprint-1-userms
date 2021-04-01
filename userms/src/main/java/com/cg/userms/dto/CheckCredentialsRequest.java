package com.cg.userms.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CheckCredentialsRequest
{
	@NotBlank
    private String username;
	@NotBlank
	@Size(min=4)
    private String password;

    public CheckCredentialsRequest(String username, String password) {
        this.username = username;
        this.password = password;
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
}
