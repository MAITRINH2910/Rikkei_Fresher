package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UserService userService;

    /**
     * Admin Management
     *
     * @param model
     * @return
     */
    @GetMapping
    public String userManagement(Model model) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());

        List<User> users = userService.findAllUser();
        System.out.println("***********");
        model.addAttribute("listUsers", users);

        return "admin/management";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/management";
    }

    @GetMapping("/edit-status-user")
    public String editStatusUser(@RequestParam Long id) {
        userService.editStatusUser(id);
        return "redirect:/admin/management";
    }

    @GetMapping("/change-role")
    @ResponseBody
    public void changeRoleUser(@RequestParam Long id, @RequestParam String role) {
        userService.editRoleUser(id, role);
    }
}
