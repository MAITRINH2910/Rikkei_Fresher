package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String listUsers(Model model) {
        List<User> users = userService.findAllUser();
        System.out.println("***********");
        model.addAttribute("listUsers", users);
        return "admin/management";
    }

    /**
     * Delete User
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/management";
    }

    /**
     * Edit Status Of User (Active <--> Un Active)
     * @param id
     * @return
     */
    @GetMapping("/edit-status-user")
    public String editStatusUser(@RequestParam Long id) {
        userService.editStatusUser(id);
        return "redirect:/admin/management";
    }

    /**
     * Change Role Of User (ADMIN <--> USER)
     * @param id
     * @param role
     */
    @GetMapping("/change-role")
    @ResponseBody
    public void changeRoleUser(@RequestParam Long id, @RequestParam String role) {
        userService.editRoleUser(id, role);
    }
}
