package com.example.project.api;

import com.example.project.DTO.WeatherDTO;
import com.example.project.converter.WeatherConverterToEntity;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.WeatherService;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApi {
    @Autowired
    WeatherService weatherService;

    @Autowired
    WeatherConverterToEntity weatherConverter;

    public WeatherEntity restJsonData(String name) {
        String URL = Constants.WEATHER_URL + name + Constants.APPID;
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(URL, WeatherDTO.class);
        return weatherConverter.weatherEntityConverter(weatherDTO);
    }



}
