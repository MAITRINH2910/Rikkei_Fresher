package com.example.project.api;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserApi {

    @Autowired
    UserService userService;

    @Autowired
    WeatherService weatherService;

    public User getAuthUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constants.ICON_PATH + weatherList.get(i).getIcon() + Constants.TAIL_ICON_PATH);
        }
        return user;
    }

    public User getUser(){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        return user;
    }
}
