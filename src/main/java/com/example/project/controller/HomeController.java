package com.example.project.controller;

import com.example.project.DTO.WeatherDTO;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    public UserService userService;

    @Autowired
    public WeatherController weatherController;

    @Autowired
    public WeatherService weatherService;
    /**
     * History Weather
     * @param model
     * @return
     */
    @GetMapping
    public String homePage(Model model) {
        User user = userService.getAuthUser();
        List<WeatherEntity> listCity = weatherService.getCitiesByUser(user);
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = weatherService.weatherGroupByCity(user);
        model.addAttribute("weatherList0", weatherGroupByCity);
        return "user/home";
    }

    /**
     * Show Local Weather
     * @param lat
     * @param lon
     * @return
     */
    @GetMapping("/local-weather")
    @ResponseBody
    public WeatherDTO forecastCurrentWeather(@RequestParam String lat, @RequestParam String lon) {
        return weatherService.getCurrentLocalWeather(lat, lon);
    }
}

