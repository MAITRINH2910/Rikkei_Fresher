package com.example.project.DTO;

import java.util.Set;

public class UserDTO {

    private Long id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private boolean active;
    private Set<Long> roleName;

    public UserDTO(Long id, String userName, String email, String firstName, String lastName, boolean active) {
        super();
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<Long> getRoleName() {
        return roleName;
    }

    public void setRoleName(Set<Long> roleName) {
        this.roleName = roleName;
    }
}
