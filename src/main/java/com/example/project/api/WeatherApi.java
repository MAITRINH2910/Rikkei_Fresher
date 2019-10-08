package com.example.project.api;

import com.example.project.DTO.WeatherDTO;
import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.converter.WeatherConverterToEntity;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.WeatherService;
import com.example.project.util.Constant;
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
        String URL = Constant.WEATHER_URL + name + Constant.APPID;
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(URL, WeatherDTO.class);
        System.out.println(weatherConverter.weatherEntityConverter(weatherDTO).getDate());
        return weatherConverter.weatherEntityConverter(weatherDTO);
    }

}
