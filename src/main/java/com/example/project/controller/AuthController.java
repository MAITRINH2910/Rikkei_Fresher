package com.example.project.controller;

import com.example.project.DTO.request.LoginForm;
import com.example.project.DTO.request.UserRegistrationDto;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Page Register for New Account
     */
    @GetMapping("/register")
    public String registrationForm(Model model) {
        UserRegistrationDto accountDto = new UserRegistrationDto();
        model.addAttribute("user", accountDto);
        return "common/register";
    }

    /**
     * Register Account
     */
    private User createUserAccount(UserRegistrationDto accountDto, BindingResult result) {
        User registered = null;
        registered = userService.saveUserDto(accountDto);
        return registered;
    }

    @PostMapping("/register")
    public String createNewUser(@ModelAttribute("user") @Valid UserRegistrationDto accountDto, BindingResult bindingResult, Model model, Errors errors, HttpServletResponse response) throws ServiceException {
        User userNameExists = userService.findUserByUsername(accountDto.getUsername());
        User emailExits = userService.findUserByEmail(accountDto.getEmail());

        User registered = new User();
        if (userNameExists != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided!");
        }
        if (emailExits != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided!");
        }
        if (!bindingResult.hasErrors()) {
            registered = createUserAccount(accountDto, bindingResult);
        }
        if (registered == null) {
            bindingResult.rejectValue("email", "message.regError");
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", accountDto);
            return "common/register";
        } else {
            model.addAttribute("successMessage", "Register successfully");
            return "common/register";
        }
    }

    /**
     * Load pageLogin when user access to APP
     *
     * @return
     */

    @ModelAttribute("userForm")
    public LoginForm userLoginDto() {
        return new LoginForm();
    }

    @GetMapping("/login")
    public String loginForm(Model model, @RequestParam(value = "error", required = false) String error) {
        LoginForm accountDto = new LoginForm();
        model.addAttribute("userForm", accountDto);
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "common/login";
    }
//    @GetMapping("/login")
//    public String viewLogin() {
//        return "common/login";
//    }

    @PostMapping("/login")
    public String doLogin(@ModelAttribute("userForm") @Valid LoginForm userForm,
                          BindingResult result) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User username = userService.findUserByUsername(userForm.getUsername());

        if (result.hasErrors()) { /* Chưa nhập 1 trong 2 trường hoặc 2 trường và nhấn nút Login*/
            return "common/login";
        } else {
            if (username == null) {
                result.rejectValue("username", null, "No username in our system");
            } else {
                if (userForm.getUsername().equals(username.getUsername())) {
                    if (encoder.matches(userForm.getPassword(), username.getPassword())) {
                        return "user/home";
                    } else {
                        result.rejectValue("password", null, "Wrong password!");
                    }
                }
            }
            return "common/login";
        }
    }
//    @GetMapping("/login")
//    public String login(@RequestParam(value = "error", required = false) String
//                                error, @RequestParam(value = "logout", required = false) String logout, Model model,
//    @ModelAttribute("userForm") @Valid LoginForm userForm
//    ) {
//        String errorMessage = null;
//        if (error != null) {
//            errorMessage = "Username or Password is incorrect !!";
//        } else if (logout != null) {
//            errorMessage = "You have been successfully logged out !!";
//        }
//        model.addAttribute("errorMessage", errorMessage);
//        return "common/login";
//    }

    /**
     * Logout account on APP
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

}
