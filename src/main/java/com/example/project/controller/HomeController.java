package com.example.project.controller;

import com.example.project.api.UserApi;
import com.example.project.api.WeatherApi;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    public UserApi userApi;

    @Autowired
    public WeatherApi weatherApi;

    @GetMapping
    public String homePage(Model model) {
        User user = userApi.getAuthUser();
        List<WeatherEntity> listCity = weatherApi.getCitiesByUser(user);
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = weatherApi.weatherGroupByCity(user);
        model.addAttribute("weatherList0", weatherGroupByCity);
        return "user/home";
    }

}

