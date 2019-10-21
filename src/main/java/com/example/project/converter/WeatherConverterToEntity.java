package com.example.project.converter;

import com.example.project.DTO.WeatherDTO;
import com.example.project.entity.WeatherEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WeatherConverterToEntity {

    ModelMapper modelMapper = new ModelMapper();

    public WeatherEntity weatherEntityConverter(WeatherDTO weatherDTO) {
        WeatherEntity weatherEntity = modelMapper.map(weatherDTO, WeatherEntity.class);
        weatherEntity.setIcon(weatherDTO.getWeather().get(0).getIcon());
        weatherEntity.setNameCity(weatherDTO.getName());
        weatherEntity.setDate(weatherDTO.getTimezone());
        weatherEntity.setTemp(weatherDTO.getMain().getTemp());
        weatherEntity.setDescription(weatherDTO.getWeather().get(0).getDescription());
        weatherEntity.setWind(weatherDTO.getWind().getSpeed());
        weatherEntity.setHumidity(weatherDTO.getMain().getHumidity());
        weatherEntity.setPressure(weatherDTO.getMain().getPressure());
        return weatherEntity;
    }



}
