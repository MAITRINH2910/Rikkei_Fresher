package com.example.project.service.Impl;

import com.example.project.DTO.WeatherDTO;
import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.converter.WeatherConverterToEntity;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.repository.UserRepository;
import com.example.project.repository.WeatherRepository;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {
    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain;
    @Value("${ver.app}")
    private String ver_app;
    @Value("${key}")
    private String key;
    @Value("${key_local_weather}")
    private String key_local_weather;
    @Value("${weather.url.current}")
    private String weatherCurrent;
    @Value("${weather.url.forecast}")
    private String weatherForecast;

    @Autowired
    UserRepository userRepository;

    @Autowired
    WeatherRepository weatherRepository;

    @Autowired
    WeatherConverterToEntity weatherConverter;

    @Override
    public void saveWeather(WeatherEntity weatherEntity) {
        weatherRepository.save(weatherEntity);
    }

    @Override
    public List<WeatherEntity> getWeatherByUser(User user) {
        return weatherRepository.findAllByUsers(user);
    }

    @Override
    public List<WeatherEntity> findWeatherGroupByCityName(String city, Long userId) {
        return weatherRepository.findWeatherGroupByCityName(city, userId);
    }

    @Override
    public Date getMaxDateByNameCity(String city, Long userId) {
        return weatherRepository.getMaxDateByNameCity(city, userId);
    }

    @Override
    public void deleteWeather(Long id) {
        WeatherEntity weatherEntity = weatherRepository.findByWeatherId(id);
        weatherEntity.getUsers().removeAll(weatherEntity.getUsers());
        weatherRepository.delete(weatherEntity);
    }

    @Override
    public List<WeatherEntity> findCity(Long id) {
        return weatherRepository.findCity(id);
    }

    /**
     * Convert String to StringBuilder
     *
     * @return
     */
    public StringBuilder convertStringToStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder;
    }

    /**
     * Get Url Api Local Weather by lat and lon
     *
     * @param lat
     * @param lon
     * @return
     */
    private StringBuilder urlApiGetCurrentLocation(String lat, String lon) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherCurrent + "lat=" + lat + "&lon=" + lon + "&APPID=" + key_local_weather + "&&units=metric");
    }

    /**
     * Get Current Local Weather
     *
     * @param lat
     * @param lon
     * @return
     */
    @Override
    public WeatherDTO getCurrentLocalWeather(String lat, String lon) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(urlApiGetCurrentLocation(lat, lon).toString(), WeatherDTO.class);
        return weatherDTO;
    }

    /**
     * @param cityName
     * @return
     */
    private StringBuilder urlApiGetWeather(String cityName) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherCurrent + "q=" + cityName + "&units=metric" + "&APPID=" + key);
    }

    /**
     * Get Weather Data(WeatherDTO) via RestTemplate Object
     * Convert WeatherDTO to Weather Entity
     * Return WeatherEntity type
     *
     * @return
     */
    @Override
    public WeatherEntity getJsonWeather(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDTO weatherDTO = restTemplate.getForObject(urlApiGetWeather(cityName).toString(), WeatherDTO.class);
        return weatherConverter.weatherEntityConverter(weatherDTO);
    }

    /**
     * URL get forecast weather by city name
     *
     * @param cityName
     * @return
     */
    private StringBuilder urlApiGetForecast(String cityName) {
        return convertStringToStringBuilder().append(host_http + "api." + domain + "/data/" + ver_app + weatherForecast + "q=" + cityName + "&units=metric" + "&APPID=" + key);
    }

    /**
     * Get Weather Detail Data(WeatherDetailDTO) via ResTemplate Object
     * Return WeatherDetailDTO type
     *
     * @param cityName
     * @return
     */
    public WeatherDetailDTO getJsonWeatherDetail(String cityName) {
        RestTemplate restTemplate = new RestTemplate();
        WeatherDetailDTO futureWeather = restTemplate.
                getForObject(urlApiGetForecast(cityName).toString(), WeatherDetailDTO.class);
        return futureWeather;
    }

    /**
     * Get List City By User base on weatherService
     *
     * @param user
     * @return
     */
    public List<WeatherEntity> getCitiesByUser(User user) {
        List<WeatherEntity> cityList = weatherRepository.findCity(user.getId());
        return cityList;
    }

    /**
     * Get List Weather By City of User
     *
     * @param user
     * @return
     */
    public List<List<WeatherEntity>> weatherGroupByCity(User user) {
        List<WeatherEntity> cityList = weatherRepository.findCity(user.getId());
        List<List<WeatherEntity>> weatherGroupByCity = new ArrayList<>();
        for (int i = 0; i < cityList.size(); i++) {
            weatherGroupByCity.add((weatherRepository.findWeatherGroupByCityName(cityList.get(i).getNameCity(), user.getId())));
        }
        return weatherGroupByCity;
    }

    /**
     * Get Current Weather by filtering by city and date to handle button Add <-> Update
     *
     * @param cityName
     * @return
     */
    public WeatherEntity filterWeather(String cityName) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authUser.getName());
        // Get List Weather
        List<WeatherEntity> weatherList = weatherRepository.findAllByUsers(user);
        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getNameCity().equals(cityName)).collect(Collectors.toList());
        // filter weather by date
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> CommonUtil.dateToString(x.getDate()).equalsIgnoreCase(CommonUtil.dateToString(new Date()))).findAny().orElse(null);

        return curWeather;
    }
}
