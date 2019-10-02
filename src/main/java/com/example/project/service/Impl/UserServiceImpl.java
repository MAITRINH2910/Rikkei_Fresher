package com.example.project.service.Impl;

import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    //    @Override
//    public User saveUser(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setActive(true);
//        Roles userRole = roleRepository.findByRoleName("ROLE_USER");
//        user.setRoleName(new HashSet<Roles>(Arrays.asList(userRole)));
//        return userRepository.save(user);
//    }
    @Override
    public User saveUser(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setActive(true);
        HashSet<Roles> roles = new HashSet<>();
        roles.add(roleRepository.findByRoleName("ROLE_ADMIN"));
        roles.add(roleRepository.findByRoleName("ROLE_USER"));
        admin.setRoleName(roles);
        return userRepository.save(admin);
    }

    @Override
    public void editStatusUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (userRepository.findById(id).get().isActive()) {
            user.get().setActive(false);
        } else {
            user.get().setActive(true);
        }
        userRepository.save(user.get());
    }

    @Override
    public void editRoleUser(Long id, String roleName) {
        Optional<User> user = userRepository.findById(id);
        Roles role = roleRepository.findByRoleName(roleName);
        user.get().setRoleName(new HashSet<Roles>(Arrays.asList(role)));
        userRepository.save(user.get());
    }




}
