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
    public List<WeatherEntity> getWeatherByUser(User user) {
        return weatherRepository.findAllByUsers(user);
    }

    @Override
    public List<WeatherEntity> findWeatherByNameCity(String city) {
        return (List<WeatherEntity>) weatherRepository.findByNameCity(city);
    }

    @Override
    public void deleteWeather(Long id) {
        WeatherEntity weatherEntity = weatherRepository.findByWeatherId(id);
        weatherEntity.getUsers().removeAll(weatherEntity.getUsers());
        weatherRepository.delete(weatherEntity);
    }


}
