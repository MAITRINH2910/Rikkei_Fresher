package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HandleErrorController {
    @Autowired
    UserService userService;

    /**
     * PAGE 401
     *
     * @return
     */
    @GetMapping("/401")
    public ModelAndView disableAccount() {
        ModelAndView model = new ModelAndView();
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        model.setViewName("error/401");
        return model;
    }

    /**
     * PAGE 403
     *
     * @return
     */
    @GetMapping("/403")
    public ModelAndView accesssDenied() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        ModelAndView model = new ModelAndView();

        if (user != null) {
            model.addObject("msg", "Hi " + user.getFirstName()
                    + ", you do not have permission to access this page!");
        } else {
            model.addObject("msg",
                    "You do not have permission to access this page!");
        }
        model.setViewName("error/403");
        return model;
    }
}