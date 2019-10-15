package com.example.project.DTO.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginForm {
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}$", message = "Username should be valid with our regex")
    @NotEmpty
    @Size(min=3, max = 60)
    private String username;

    @Size(min = 8, message = "At least 8 characters")
    @NotEmpty
    @Size(min = 8, max = 32)
    private String password;

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