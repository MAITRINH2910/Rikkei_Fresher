package com.example.project.converter;

import com.example.project.DTO.WeatherDTO;
import com.example.project.entity.WeatherEntity;
import org.apache.el.parser.ParseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WeatherConverterToEntity {

    public WeatherEntity weatherEntityConverter(WeatherDTO weatherDTO) {
        ModelMapper modelMapper = new ModelMapper();
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
