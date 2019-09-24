package com.example.project.service;

import com.example.project.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {

    User findUserByEmail(String email);

    List<User> findAllUser();

    Optional<User> findById(Long id);

    void delete(Long id);

    User saveUser(User user);

}
