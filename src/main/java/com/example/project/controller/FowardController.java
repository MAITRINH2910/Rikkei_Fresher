package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.UserService;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class FowardController {
    @Autowired
    UserService userService;

    /**
     * Load pageLogin when user access to APP
     *
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, @RequestParam(value = "logout", required = false) String logout, Model model) {
        String errorMessage = null;
        if (error != null) {
            errorMessage = "Username or Password is incorrect !!";
        }
//        else if (logout != null) {
//            errorMessge = "You have been successfully logged out !!";
//        }
//
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

    @GetMapping("/processURL")
    public String processURL() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String URL = urlMappingUser(authentication);

        return URL;
    }

    @GetMapping("/401")
    public ModelAndView disableAccount() {
        ModelAndView model = new ModelAndView();
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
//        model.addObject("msg", "Hi " + user.getFirstName()
//                + ", your account is disable. Please contact with me via 012458982!");

        model.setViewName("error/401");
        return model;
    }

    public String urlMappingUser(Authentication authentication) {
        String url = "";
        List<GrantedAuthority> authorities = getListAuthority(authentication);

        if (checkRoleUser(authorities, Constants.USER))
            url = "redirect:/";
        if (checkRoleUser(authorities, Constants.ADMIN))
            url = "redirect:/admin";
        return url;
    }

    public boolean checkRoleUser(List<GrantedAuthority> userAuthority, String role) {
        return userAuthority.stream().anyMatch(author -> author.getAuthority().equalsIgnoreCase(role));
    }

    public List<GrantedAuthority> getListAuthority(Authentication authentication) {
        List<GrantedAuthority> userAuthority = new ArrayList<GrantedAuthority>();
        @SuppressWarnings("unchecked")
        Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) authentication.getAuthorities();
        userAuthority.addAll(authorities);

        return userAuthority;

    }
}
