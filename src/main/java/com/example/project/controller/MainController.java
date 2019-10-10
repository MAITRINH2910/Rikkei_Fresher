package com.example.project.controller;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.RoleService;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/")
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

    @GetMapping
    public String homePage(Model model) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());

        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constants.ICON_PATH + weatherList.get(i).getIcon() + Constants.TAIL_ICON_PATH);
        }

        List<WeatherEntity> listCity = weatherService.findCity(user.getId());
        model.addAttribute("listCities", listCity);

        List<List<WeatherEntity>> weatherGroupByCity = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++) {
            weatherGroupByCity.add((weatherService.findWeatherGroupByCityName(listCity.get(i).getNameCity(), user.getId())));
        }
        model.addAttribute("weatherList0", weatherGroupByCity);

        return "user/home";
    }

    /**
     * PAGE 403
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

