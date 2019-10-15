package com.example.project.service.Impl;

import com.example.project.DTO.request.SignUpForm;
import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
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

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Roles userRole = roleRepository.findByRoleName("ROLE_USER");
        user.setRoleName(new HashSet<Roles>(Arrays.asList(userRole)));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User registerNewUserAccount(SignUpForm accountDto) {
        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setEmail(accountDto.getEmail());
        user.setUsername(accountDto.getFirstName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Roles userRole = roleRepository.findByRoleName("ROLE_USER");
        user.setRoleName(new HashSet<Roles>(Arrays.asList(userRole)));
        user.setActive(true);
        return userRepository.save(user);
    }
    private boolean emailExists(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
//    @Override
//    public User saveUser(User admin) {
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//        admin.setActive(true);
//        HashSet<Roles> roles = new HashSet<>();
//        roles.add(roleRepository.findByRoleName("ROLE_ADMIN"));
////        roles.add(roleRepository.findByRoleName("ROLE_USER"));
//        admin.setRoleName(roles);
//        return userRepository.save(admin);
//    }

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
        if (role.getRoleName().equals("ROLE_ADMIN")) {
            HashSet<Roles> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName("ROLE_ADMIN"));
            roles.remove(roleRepository.findByRoleName("ROLE_USER"));
            user.get().setRoleName(roles);
            userRepository.save(user.get());
        } else if (role.getRoleName().equals("ROLE_USER")) {
            HashSet<Roles> roles = new HashSet<>();
            roles.remove(roleRepository.findByRoleName("ROLE_ADMIN"));
            roles.add(roleRepository.findByRoleName("ROLE_USER"));
            user.get().setRoleName(roles);
            userRepository.save(user.get());
        }
    }
}
