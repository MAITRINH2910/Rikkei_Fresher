package com.example.project.service;

import com.example.project.DTO.WeatherDTO;
import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface WeatherService {
    void saveWeather(WeatherEntity weatherEntity);

    List<WeatherEntity> getWeatherByUser(User user);

    void deleteWeather(Long id);

    List<WeatherEntity> findCity(Long id);

    List<WeatherEntity> findWeatherGroupByCityName(String city, Long userId);

    Date getMaxDateByNameCity(String city, Long userId);

    WeatherDTO getCurrentLocalWeather(String lat, String lon);

    WeatherDetailDTO getJsonWeatherDetail(String cityName);

    WeatherEntity getJsonWeather(String cityName);

    List<WeatherEntity> getCitiesByUser(User user);

    List<List<WeatherEntity>> weatherGroupByCity(User user);

    WeatherEntity filterWeather(String cityName);
}
