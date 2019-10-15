package com.example.project.api;

import com.example.project.DTO.WeatherDTO;
import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.converter.WeatherConverterToEntity;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeatherApi {
    @Autowired
    WeatherService weatherService;

    @Autowired
    UserApi userApi;

    @Autowired
    WeatherConverterToEntity weatherConverter;

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain;
    @Value("${ver.app}")
    private String ver_app;
    @Value("${key}")
    private String key;

    public WeatherEntity getJsonWeather(String name) {
        String weatherUrl = host_http + "api." + domain + "/data/" + ver_app + "weather?q=" + name + key;
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(weatherUrl, WeatherDTO.class);
        return weatherConverter.weatherEntityConverter(weatherDTO);
    }

    public WeatherDetailDTO getJsonWeatherDetail(String name) {
        String weatherDetailUrl = host_http + "api." + domain + "/data/" + ver_app + "forecast?q=" + name + key;
        RestTemplate restTemplate = new RestTemplate();
        WeatherDetailDTO futureWeather = restTemplate.
                getForObject(weatherDetailUrl, WeatherDetailDTO.class);
        return futureWeather;
    }

    public List<WeatherEntity> getWeathersByUser(User user) {
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        return weatherList;
    }

    public List<WeatherEntity> getCitiesByUser(User user) {
        List<WeatherEntity> cityList = weatherService.findCity(user.getId());
        return cityList;
    }

    public List<List<WeatherEntity>> weatherGroupByCity(User user) {
        List<WeatherEntity> cityList = weatherService.findCity(user.getId());
        List<List<WeatherEntity>> weatherGroupByCity = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            weatherGroupByCity.add((weatherService.findWeatherGroupByCityName(cityList.get(i).getNameCity(), user.getId())));
        }
        return weatherGroupByCity;
    }

    public WeatherEntity filterWeather(String city) {
        User user = userApi.getUser();
        // Get List Weather
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getNameCity().equals(city)).collect(Collectors.toList());
        // filter weather by date
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> CommonUtil.dateToString(x.getDate()).equalsIgnoreCase(CommonUtil.dateToString(new Date()))).findAny().orElse(null);

        return curWeather;
    }

}
