package com.example.project.service.Impl;

import com.example.project.DTO.request.UserRegistrationDto;
import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.repository.PasswordResetTokenRepository;
import com.example.project.repository.RoleRepository;
import com.example.project.repository.UserRepository;
import com.example.project.repository.WeatherRepository;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain_http;
    @Value("${tail.icon.path}")
    private String tail_icon_path;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

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
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User saveUserDto(UserRegistrationDto accountDto) {
        User user = new User();
        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setUsername(accountDto.getUsername());
        user.setEmail(accountDto.getEmail());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        Roles userRole = roleRepository.findByRoleName("ROLE_USER").get();
        user.setRoleName(userRole);
        user.setActive(true);
        return userRepository.save(user);
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
        Roles role = roleRepository.findByRoleName(roleName).get();
        if (role.getRoleName().equals("ROLE_ADMIN")) {
            HashSet<Roles> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName("ROLE_ADMIN").get());
            roles.remove(roleRepository.findByRoleName("ROLE_USER"));
            user.get().setRoleName(roles.stream().findFirst().get());
            userRepository.save(user.get());
        } else if (role.getRoleName().equals("ROLE_USER")) {
            HashSet<Roles> roles = new HashSet<>();
            roles.remove(roleRepository.findByRoleName("ROLE_ADMIN"));
            roles.add(roleRepository.findByRoleName("ROLE_USER").get());
            user.get().setRoleName(roles.stream().findFirst().get());
            userRepository.save(user.get());
        }
    }

    @Override
    public void updatePassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    }

    /**
     * Get Authentication User from Security Context Holder of Spring Security
     * Set icon of weather for user
     *
     * @return
     */
    @Override
    public User getAuthUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherRepository.findAllByUsers(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(host_http + domain_http + "/img/w/" + weatherList.get(i).getIcon() + tail_icon_path);
        }
        return user;
    }

    /**
     * Get Authentication User from Security Context Holder of Spring Security
     *
     * @return
     */
    @Override
    public User getUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authUser.getName());
        return user;
    }

    @Override
    public Boolean existsByUserName(String userName) {
        return userRepository.existsByUsername(userName);
    }
}
