package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The AdminController class is used to illustrate:
 * 1. Handle Business With Role Admin:
 * 2. Table List Users
 * 3. Active User
 * 4. Change Role User
 * 5. Delete User
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * Dashboard Admin Management
     *
     * @param model
     * @return
     */
    @GetMapping
    private String listUsers(Model model) {
        List<User> users = userService.findAllUser();
        model.addAttribute("listUsers", users);
        return "admin/table";
    }

    /**
     * Delete User
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    private String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    /**
     * Edit Status Of User (Active <--> Un Active)
     *
     * @param id
     * @return
     */
    @GetMapping("/edit-status-user")
    private String editStatusUser(@RequestParam Long id) {
        userService.editStatusUser(id);
        return "redirect:/admin";
    }

    /**
     * Change Role Of User (ADMIN <--> USER)
     *
     * @param id
     * @param role
     */
    @GetMapping("/change-role")
    @ResponseBody
    private void changeRoleUser(@RequestParam Long id, @RequestParam String role) {
        userService.editRoleUser(id, role);
    }
}
