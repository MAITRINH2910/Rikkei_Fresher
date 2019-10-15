package com.example.project.api;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain_http;
    @Value("${tail.icon.path}")
    private String tail_icon_path;

    public User getAuthUser() {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(host_http + domain_http +"/img/w/"+weatherList.get(i).getIcon() + tail_icon_path);
        }
        return user;
    }

    public User getUser(){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        return user;
    }
}
