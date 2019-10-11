package com.example.project.controller;

import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.api.UserApi;
import com.example.project.api.WeatherApi;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
import com.example.project.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WeatherController {

    private static final int days=8;
    @Autowired
    WeatherService weatherService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherApi weatherApi;

    @Autowired
    UserApi userApi;

    /**
     * SEARCH WEATHER
     *
     * @param city
     * @param model
     * @param modelMap
     * @return
     */
    @GetMapping("/search-city")
    public String findWeatherByCity(@RequestParam String city, Model model, ModelMap modelMap) {
        // Return Weather or Not Found
        try {
            WeatherEntity currentWeather = weatherApi.restJsonWeather(city);
            model.addAttribute("currentWeather", currentWeather);
        } catch (Exception e) {
            model.addAttribute("message", "City is not found!!!");
        }
        // Show History Weathers
        User user = userApi.getAuthUser();
        List<WeatherEntity> listCity = weatherApi.getCitiesByUser(user);
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = weatherApi.weatherGroupByCity(user);
        model.addAttribute("weatherList0", weatherGroupByCity);
        // Handle Button ADD --> UPDATE by Comparing Current Date and Latest Date of Current Weather
        WeatherEntity oldWeather = weatherApi.filterWeather(city);
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
     * @param city
     * @param model
     * @return
     */
    @GetMapping("/weather-detail/{city}")
    public String getWeatherDetail(@PathVariable String city, Model model) throws ParseException {
        WeatherDetailDTO futureWeather = weatherApi.restJsonWeatherDetail(city);
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
        for (int j = 0; j < 40; j = j + days) {
            String icon = (Constants.ICON_PATH + futureWeather.getList().get(j).getWeather().get(0).getIcon() + Constants.TAIL_ICON_PATH);
            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
            String pressure = futureWeather.getList().get(j).getMain().getPressure();
            String wind = futureWeather.getList().get(j).getWind().getSpeed();
            Double temp = CommonUtil.kelvinToCelsius(futureWeather.getList().get(j).getMain().getTemp());
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
     * @param city
     * @return
     */
    @GetMapping("/save-weather/{city}")
    public String saveWeather(@PathVariable String city) {
        User user = userApi.getUser();
        WeatherEntity currentWeather = weatherApi.restJsonWeather(city);

        WeatherEntity weather1 = new WeatherEntity(currentWeather.getIcon(), currentWeather.getNameCity(),
                currentWeather.getTemp(), currentWeather.getDescription(), currentWeather.getWind(),
                currentWeather.getHumidity(), currentWeather.getPressure(), currentWeather.getDate());
        weather1.setUsers(new HashSet<>(Arrays.asList(userService.findUserByUsername(user.getUsername()))));
        // Get List Weather by User
        List<WeatherEntity> lstByUser = weatherService.getWeatherByUser(user);
        // Get List City By idUser va cityName
        List<WeatherEntity> lstByUserByCity = lstByUser.stream()
                .filter(weather -> city.trim().toLowerCase().equals(weather.getNameCity().trim().toLowerCase()))
                .collect(Collectors.toList());
        if (lstByUserByCity.size() < 3) {
            weatherService.saveWeather(weather1);
            return "redirect:/";
        } else {
            lstByUserByCity
                    .sort((WeatherEntity w1, WeatherEntity w2) -> w1.getDate().compareTo(w2.getDate()));
            Optional<WeatherEntity> entity = lstByUserByCity.stream().findFirst();
            weatherService.deleteWeather(entity.get().getWeatherId());
            insertWeather(city, user);
            return "redirect:/";
        }
    }

    public void insertWeather(String name, User userEntity) {
        WeatherEntity result = weatherApi.restJsonWeather(name);
        result.setDate(new Date());
        result.setUsers(new HashSet<>(Arrays.asList(userEntity)));
        weatherService.saveWeather(result);
    }

    /**
     * UPDATE WEATHER
     *
     * @param city
     * @return
     */
    @GetMapping("/update-weather/{city}")
    public String updateWeather(@PathVariable String city, Model model) {
        User user = userApi.getUser();
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        WeatherEntity oldWeather = weatherApi.filterWeather(city);
        WeatherEntity currentWeather = weatherApi.restJsonWeather(city);
        //Set Data of Current Weather
        oldWeather.setIcon(currentWeather.getIcon());
        oldWeather.setTemp(CommonUtil.kelvinToCelsius(currentWeather.getTemp()));
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
    public String deleteWeather(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
