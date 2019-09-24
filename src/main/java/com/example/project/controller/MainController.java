package com.example.project.controller;


import com.example.project.entity.Roles;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.Impl.UserDetailsServiceImpl;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class MainController {
    @Autowired
    public UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserService userService;

    @Autowired
    public WeatherService weatherService;

    @GetMapping("/login")
    public String login() {
        return "main/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login";
    }

    @GetMapping("/admin/home")
    public ModelAndView homeAdmin() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping("/admin/management")
    public ModelAndView userManagement(Authentication authentication, String authority) {
        ModelAndView modelAndView = new ModelAndView();
        List<User> users = userService.findAllUser();

        modelAndView.addObject("listUsers", users);
        modelAndView.setViewName("admin/management");
        return modelAndView;
    }

    @GetMapping("/")
    public ModelAndView homePage(Authentication authentication) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(authUser.getName());
        boolean isAdmin = false;
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
        }
        ModelAndView modelAndView = new ModelAndView();
//        for (int i = 0; i < weatherList.size(); i++) {
//            String tempInCelcius = CommonUtil.FahtoCelsius(Double.parseDouble(weatherList.get(i).getTemp()));
//            String currentDate = CommonUtil.formatDate();
//            String city = weatherList.get(i).getNameCity();
//            String iconPath = Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH;
//            String humidity = weatherList.get(i).getHumidity();
//            String pressure = weatherList.get(i).getPressure();
//            String wind = weatherList.get(i).getWind();
//            String description = weatherList.get(i).getDescription();
//            Long id = weatherList.get(i).getWeatherId();
////            System.out.println(tempInCelcius);
////            System.out.println(currentDate);
////            System.out.println(city);
////            System.out.println(iconPath);
////            System.out.println(humidity);
////            System.out.println(pressure);
////            System.out.println(wind);
////            System.out.println(description);
//            System.out.println(id);
////            modelAndView.addObject("weatherList", weatherList);
////            modelAndView.addObject("id", id);
//            modelAndView.addObject("icon", iconPath);
////            modelAndView.addObject("city", city);
////            modelAndView.addObject("date", currentDate);
////            modelAndView.addObject("temp", tempInCelcius);
////            modelAndView.addObject("description", description);
////            modelAndView.addObject("general", general);
//        }
        modelAndView.addObject("weatherList", weatherList);
        isAdmin = checkRole(authentication, "ROLE_ADMIN");
        if (isAdmin) {
            modelAndView.setViewName("admin/home");
        } else {
            modelAndView.setViewName("user/home");
        }
        return modelAndView;
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "main/403";
    }

    @GetMapping("/register")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("main/register");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User emailExists = userService.findUserByEmail(user.getEmail());
        if (emailExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("main/register");
        } else {
            userDetailsServiceImpl.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("main/register");
        }
        return modelAndView;
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteStudent(@PathVariable long id) {
        userService.delete(id);
        return "redirect:/admin/management";
    }

    private boolean checkRole(Authentication authentication, String authority) {
        boolean isRole = false;

        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            List<GrantedAuthority> currentAuthority = authentication.getAuthorities().parallelStream()
                    .collect(Collectors.toList());

            isRole = currentAuthority.stream()
                    .anyMatch(authotity -> authotity.getAuthority().equalsIgnoreCase(authority));
        }

        return isRole;
    }
}

