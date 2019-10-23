package com.example.project.service.Impl;

import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
            Roles roles = user.getRoleName();

            grantedAuthorities.add(new SimpleGrantedAuthority(roles.getRoleName()));


            return new UserDetails() {
                @Override
                public String getUsername() {
                    return user.getUsername();
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    return grantedAuthorities;
                }

                @Override
                public boolean isEnabled() {
                    return user.isActive();
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }
            };
        } else {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }
    }
}
