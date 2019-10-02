package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    @Autowired
    public UserService userService;

    @Autowired
    public WeatherService weatherService;

    @Autowired
    public RoleService roleService;

    @GetMapping("/register")
    public String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "common/register";
    }

    @PostMapping("/register")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
        User userNameExists = userService.findUserByUsername(user.getUsername());
        if (userNameExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the username provided!");
        }
        if (bindingResult.hasErrors()) {
            return "common/register";
        } else {
            userService.saveUser(user);
            model.addAttribute("successMessage", "User has been registered successfully");
            model.addAttribute("user", new User());
            return "common/register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "common/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
        }
        model.addAttribute("weatherList", weatherList);

        if (user.isActive()) {
            if (user.getRoleName().size() == 2) {
                return "user/home";
            } else {
                return "user/home";
            }
        } else {
            return "error/403";
        }
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "error/403";
    }

    /**
     * Admin Management
     * @param model
     * @return
     */
    @GetMapping("/admin/management")
    public String userManagement(Model model) {
        List<User> users = userService.findAllUser();
        model.addAttribute("listUsers", users);
        return "admin/management";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteStudent(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/management";
    }

    @GetMapping("/admin/edit-status-user")
    public String edit(@RequestParam Long id) {
        userService.editStatusUser(id);
        return "redirect:/admin/management";
    }

    @GetMapping("/admin/change-role")
    @ResponseBody
    public void changeRole(@RequestParam Long id, @RequestParam String role) {
        userService.editRoleUser(id, role);
    }
}

