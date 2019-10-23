package com.example.project.controller;

import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.converter.WeatherConverterToEntity;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WeatherController {

    private static final int days = 8;
    @Autowired
    WeatherService weatherService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherConverterToEntity weatherConverter;

    @Value("${host.http}")
    private String host_http;
    @Value("${domain}")
    private String domain_http;
    @Value("${tail.icon.path}")
    private String tail_icon_path;
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

    /**
     * SEARCH WEATHER
     *
     * @param cityName
     * @param model
     * @param modelMap
     * @return
     */
    @GetMapping("/search-city")
    private String findWeatherByCity(@RequestParam String cityName, Model model, ModelMap modelMap) {
        // Return Weather Data or Not Found
        try {
            WeatherEntity currentWeather = weatherService.getJsonWeather(cityName);
            model.addAttribute("currentWeather", currentWeather);
            // Show History Weathers
            User user = userService.getAuthUser();
            List<WeatherEntity> listCity = weatherService.getCitiesByUser(user);
            model.addAttribute("listCities", listCity);
            List<List<WeatherEntity>> weatherGroupByCity = weatherService.weatherGroupByCity(user);
            model.addAttribute("weatherList0", weatherGroupByCity);
        } catch (Exception e) {
            model.addAttribute("message", "City is not found!!!");
        }

        // Handle Button ADD --> UPDATE by Comparing Current Date and Latest Date of Current Weather
        WeatherEntity oldWeather = weatherService.filterWeather(cityName);
        if (oldWeather != null) {
            modelMap.addAttribute("status", "update");
        } else {
            modelMap.addAttribute("status", "add");
        }
        return "user/home";
    }

    /**
     * WEATHER DETAIL
     * 40 records for 5 continuous days => days = 40/5
     *
     * @param cityName
     * @param model
     * @return
     */
    @GetMapping("/weather-detail/{cityName}")
    private String getWeatherDetail(@PathVariable String cityName, Model model) throws ParseException {
        WeatherEntity currentWeather = weatherService.getJsonWeather(cityName);
        model.addAttribute("currentWeather", currentWeather);
        WeatherDetailDTO futureWeather = weatherService.getJsonWeatherDetail(cityName);
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
        for (int j = 7; j < 40; j = j + days) {
            String icon = (host_http + domain_http + "/img/w/" + futureWeather.getList().get(j).getWeather().get(0).getIcon() + tail_icon_path);
            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
            String pressure = futureWeather.getList().get(j).getMain().getPressure();
            String wind = futureWeather.getList().get(j).getWind().getSpeed();
            Double temp = futureWeather.getList().get(j).getMain().getTemp();
            String city1 = futureWeather.getCity().getName();
            Date date = CommonUtil.stringToDate(futureWeather.getList().get(j).getDt_txt());
            WeatherEntity detail = new WeatherEntity(icon, city1, temp, clouds, wind, humidity, pressure, date);
            futureWeatherList.add(detail);
        }
        model.addAttribute("detail", futureWeatherList);
        return "user/weather";
    }

    /**
     * ADD WEATHER
     *
     * @param cityName
     * @return
     */
    @GetMapping("/save-weather/{cityName}")
    private String saveWeather(@PathVariable String cityName) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        WeatherEntity currentWeather = weatherService.getJsonWeather(cityName);

        WeatherEntity weather1 = new WeatherEntity(currentWeather.getIcon(), currentWeather.getNameCity(),
                currentWeather.getTemp(), currentWeather.getDescription(), currentWeather.getWind(),
                currentWeather.getHumidity(), currentWeather.getPressure(), currentWeather.getDate());
        weather1.setUsers(new HashSet<>(Arrays.asList(userService.findUserByUsername(user.getUsername()))));
        // Get List Weather by User
        List<WeatherEntity> lstByUser = weatherService.getWeatherByUser(user);
        // Get List City By idUser va cityName
        List<WeatherEntity> lstByUserByCity = lstByUser.stream()
                .filter(weather -> cityName.trim().toLowerCase().equals(weather.getNameCity().trim().toLowerCase()))
                .collect(Collectors.toList());
        if (lstByUserByCity.size() < 3) {
            weatherService.saveWeather(weather1);
            return "redirect:/";
        } else {
            lstByUserByCity
                    .sort((WeatherEntity w1, WeatherEntity w2) -> w1.getDate().compareTo(w2.getDate()));
            Optional<WeatherEntity> entity = lstByUserByCity.stream().findFirst();
            weatherService.deleteWeather(entity.get().getWeatherId());
            insertWeather(cityName, user);
            return "redirect:/";
        }
    }

    /**
     * Method saveWeather will call this method to set current Date and User for Current Weather
     *
     * @param cityName
     * @param userEntity
     */
    public void insertWeather(String cityName, User userEntity) {
        WeatherEntity result = weatherService.getJsonWeather(cityName);
        result.setDate(new Date());
        result.setUsers(new HashSet<>(Arrays.asList(userEntity)));
        weatherService.saveWeather(result);
    }

    /**
     * UPDATE WEATHER
     *
     * @param cityName
     * @return
     */
    @GetMapping("/update-weather/{cityName}")
    private String updateWeather(@PathVariable String cityName, Model model) {
        User user = userService.getUser();
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        WeatherEntity oldWeather = weatherService.filterWeather(cityName);
        WeatherEntity currentWeather = weatherService.getJsonWeather(cityName);
        //Set New Record for Current Weather
        oldWeather.setIcon(currentWeather.getIcon());
        oldWeather.setTemp(currentWeather.getTemp());
        oldWeather.setWind(currentWeather.getWind());
        oldWeather.setHumidity(currentWeather.getHumidity());
        oldWeather.setPressure(currentWeather.getPressure());
        oldWeather.setDescription(currentWeather.getDescription());
        weatherService.saveWeather(oldWeather);

        model.addAttribute("weatherList", weatherList);
        return "redirect:/";
    }

    /**
     * DELETE WEATHER
     *
     * @param id
     * @return
     */
    @GetMapping("/delete-weather/{id}")
    private String deleteWeather(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
