package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {
    void saveWeather(WeatherEntity weatherEntity);

    List<WeatherEntity> getWeatherByUser(User user);

    void deleteWeather (Long id);

    List<WeatherEntity> findCity(Long id);

    List<WeatherEntity> findWeatherGroupByCityName(String city, Long id);

}
