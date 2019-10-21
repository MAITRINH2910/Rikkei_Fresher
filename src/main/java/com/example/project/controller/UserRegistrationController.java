package com.example.project.controller;


import com.example.project.DTO.request.UserRegistrationDto;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    /**
     * Show Registration Form when click the link: "/registration"
     *
     * @return
     */
    @GetMapping
    public String showRegistrationForm() {
        return "common/register";
    }

    /**
     * Check Email Existing or Username Existing
     * Create New Account
     * @param userDto
     * @param result
     * @return
     */
    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result) {

        User emailExisting = userService.findUserByEmail(userDto.getEmail());
        User usernameExisting = userService.findUserByUsername(userDto.getUsername());
        if (emailExisting != null) {
            result.rejectValue("email", null, "There is already an account registered with that email");
        }
        if (usernameExisting != null) {
            result.rejectValue("username", null, "There is already an account registered with that username");
        }
        if (result.hasErrors()) {
            return "common/register";
        }

        userService.saveUserDto(userDto);
        return "redirect:/registration?success";

    }
}