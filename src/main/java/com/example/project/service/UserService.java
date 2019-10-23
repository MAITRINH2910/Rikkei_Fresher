package com.example.project.service;

import com.example.project.DTO.request.UserRegistrationDto;
import com.example.project.entity.Roles;
import com.example.project.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User findUserByUsername(String username);

    User findUserByEmail(String email);

    List<User> findAllUser();

    void delete(Long id);

    User saveUserDto(UserRegistrationDto userDTO);

    void editStatusUser(Long id);

    void editRoleUser(Long id, String roleName);

    void updatePassword(String password, Long userId);

    User getAuthUser();

    User getUser();

    Boolean existsByUserName (String userName);

}
