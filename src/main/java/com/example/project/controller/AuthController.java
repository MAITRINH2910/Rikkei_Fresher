package com.example.project.controller;

import com.example.project.DTO.request.SignUpForm;
import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.hibernate.service.spi.ServiceException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.Set;
import javax.validation.Validator;

@Controller
public class AuthController {
    @Autowired
    UserService userService;
    @Autowired
    private Validator validator;

    /**
     * Redirect to page Register
     */
    @GetMapping("/register")
    public String registration(Model model) {
        SignUpForm accountDto = new SignUpForm();
        model.addAttribute("user", accountDto);
        return "common/register";
    }

    /**
     * Register Account
     */
    private User createUserAccount(SignUpForm accountDto, BindingResult result) {
        User registered = null;
        registered = userService.registerNewUserAccount(accountDto);
        return registered;
    }

    @PostMapping("/register")
    public String createNewUser(@ModelAttribute("user") @Valid SignUpForm accountDto, BindingResult bindingResult, Model model, Errors errors, HttpServletResponse response) throws ServiceException {
//        User userNameExists = userService.findUserByUsername(accountDto.getUsername());
        User emailExits = userService.findUserByEmail(accountDto.getEmail());

        User registered = new User();
//        if (userNameExists != null) {
//            bindingResult
//                    .rejectValue("username", "error.user",
//                            "There is already a user registered with the username provided!");
//        }
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
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String
                                error, @RequestParam(value = "logout", required = false) String logout, Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        } else if (logout != null) {
            errorMessage = "You have been successfully logged out !!";
        }
        model.addAttribute("errorMessage", errorMessage);

        return "common/login";
    }

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
