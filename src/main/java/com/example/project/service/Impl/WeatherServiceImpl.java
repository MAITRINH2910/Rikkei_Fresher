package com.example.project.service.Impl;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.repository.WeatherRepository;
import com.example.project.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    WeatherRepository weatherRepository;

    @Override
    public void saveWeather(WeatherEntity weatherEntity) {
        weatherRepository.save(weatherEntity);
    }

    @Override
    public WeatherEntity findByCityName(String city) {
        return weatherRepository.findByNameCity(city);
    }

    @Override
    public List<WeatherEntity> getWeatherByUser(User user) {
        return weatherRepository.findWeatherEntitiesByUsers(user);
    }
}
