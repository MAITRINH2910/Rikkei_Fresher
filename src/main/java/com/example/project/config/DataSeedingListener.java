package com.example.project.config;

import java.sql.Timestamp;
import java.util.HashSet;

import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${account.admin.user}")
    private String userAdmin;

    @Value("${account.admin.password}")
    private String passwordAdmin;

    @Value("${my.email}")
    private String emaildAdmin;

    @Value("${admin.firstName}")
    private String firstName;

    @Value("${admin.lastName}")
    private String lastName;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Set Role entity
        // Check ROLE_ADMIN have been existed
        if (!roleRepository.findByRoleName(Constants.ADMIN).isPresent()) {
            // If result = false, Create ROLE_ADMIN
            roleRepository.save(new Roles(Constants.ADMIN));
        }
        // Check ROLE_USER have been existed
        if (!roleRepository.findByRoleName(Constants.USER).isPresent()) {
            // If result = false, Create ROLE_USER
            roleRepository.save(new Roles(Constants.USER));
        }

        // Set account admin
        // Check userAdmin have been existed
        if(!userRepository.existsByUsername(userAdmin)) {
            // Create one UserEntity Admin
            User accountAdmin = new User();
            // Set property for User has ROLE_ADMIN
            accountAdmin.setUsername(userAdmin);
            accountAdmin.setEmail(emaildAdmin);
            accountAdmin.setPassword(passwordEncoder.encode(passwordAdmin));
            accountAdmin.setFirstName(firstName);
            accountAdmin.setLastName(lastName);
            accountAdmin.setActive(Constants.ACTIVE);
            HashSet<Roles> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName(Constants.ADMIN).get());
            accountAdmin.setRoleName(roles);
            //Add USER Admin in DB
            userRepository.save(accountAdmin);

        }
    }

}