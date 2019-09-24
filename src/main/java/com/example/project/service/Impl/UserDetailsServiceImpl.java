package com.example.project.service.Impl;

import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        Set<Roles> roles = user.getRoleName();
        for (Roles role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), grantedAuthorities);
    }

//
  	public User saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Roles userRole = roleRepository.findByRoleName("ROLE_USER");
		user.setRoleName(new HashSet<Roles>(Arrays.asList(userRole)));
		return userRepository.save(user);
	}

//  /*   //Đăng kí cho admin
//    public User saveUser(User admin) {
//        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
//        admin.setActive(1);
//        HashSet<Roles> roles = new HashSet<>();
//        roles.add(roleRepository.findByRoleName("ROLE_ADMIN"));
//        roles.add(roleRepository.findByRoleName("ROLE_USER"));
//        admin.setRoleName(roles);
//        return userRepository.save(admin);
//    }


}
