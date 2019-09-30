package com.example.project.controller;

import com.example.project.DTO.WeatherDTO;
import com.example.project.DTO.WeatherDetailDTO;
import com.example.project.api.WeatherApi;
import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import com.example.project.service.UserService;
import com.example.project.service.WeatherService;
import com.example.project.util.CommonUtil;
import com.example.project.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherApi weatherApi;

    //
//    @GetMapping("/search-city")
//    public String findWeatherByCity(@RequestParam String name, Model model, Principal principal, ModelMap modelMap) {
//        // list theo ten search
//        User user = userService.findUserByUsername(principal.getName());
//        List<WeatherEntity> lstWeatherByName;
//        // list weather by user
//        List<WeatherEntity> lstWeather = weatherApi.findAllByUserEntities(user);
//        lstWeatherByName = lstWeather.stream()
//                .filter(curweather -> name.trim().toLowerCase().equals(curweather.getNameCity().trim().toLowerCase()))
//                .collect(Collectors.toList());
//        modelMap.addAttribute("listWeather", lstWeatherByName);
//        WeatherEntity currentWeather = weatherApi.restJsonData(name);
//
//        // lay thoi tiet hien tai cung ten Search trong db , Xy ly nut add va update
//        WeatherEntity curWeather = lstWeatherByName.stream()
//                .filter(weather -> CommonUtil.curTimeToString().equals(weather.getDate()))
//                .findAny().orElse(null);
//        if (curWeather != null) {
//            modelMap.addAttribute("flag", "update");
//        } else {
//            modelMap.addAttribute("flag", "add");
//        }
//
//        String tempInCelcius = CommonUtil.FahtoCelsius(Double.parseDouble(currentWeather.getTemp()));
//        String currentDate = CommonUtil.formatDate();
//        String iconPath = Constant.ICON_PATH + currentWeather.getIcon() + Constant.TAIL_ICON_PATH;
//        String humidity = currentWeather.getHumidity();
//        String pressure = currentWeather.getPressure();
//        String wind = currentWeather.getWind();
//        String general = wind + "m/s.   " + humidity + "%.   " + pressure + "hpa.";
//        model.addAttribute("currentWeather", currentWeather);
//        model.addAttribute("icon", iconPath);
//        model.addAttribute("city", currentWeather.getNameCity());
//        model.addAttribute("date", currentDate);
//        model.addAttribute("temp", tempInCelcius);
//        model.addAttribute("description", currentWeather.getDescription());
//        model.addAttribute("general", general);
//
//        return "user/home";
//    }
    @GetMapping("/search-city")
    public String findWeatherByCity(@RequestParam("city") String city, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
                getForEntity(Constant.WEATHER_URL + city +
                                Constant.APPID,
                        WeatherDTO.class);
        WeatherDTO currentWeather = responseWeather.getBody();
        String tempInCelcius = CommonUtil.FahtoCelsius(Double.parseDouble(currentWeather.getMain().getTemp()));
        String currentDate = CommonUtil.formatDate();
        String iconPath = Constant.ICON_PATH + currentWeather.getWeather().get(0).getIcon() + Constant.TAIL_ICON_PATH;
        String humidity = currentWeather.getMain().getHumidity();
        String pressure = currentWeather.getMain().getPressure();
        String wind = currentWeather.getWind().getSpeed();
        String general = wind + "m/s.   " + humidity + "%.   " + pressure + "hpa.";
        model.addAttribute("currentWeather", currentWeather);
        model.addAttribute("icon", iconPath);
        model.addAttribute("city", currentWeather.getName());
        model.addAttribute("date", currentDate);
        model.addAttribute("temp", tempInCelcius);
        model.addAttribute("description", currentWeather.getWeather().get(0).getMain());
        model.addAttribute("general", general);

        return "user/home";
    }

    @GetMapping("/weather-detail/{city}")
    public String getWeatherDetail(@PathVariable String city, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<WeatherDetailDTO> response = restTemplate.
                getForEntity(Constant.FORECAST_URL + city +
                                Constant.APPID,
                        WeatherDetailDTO.class);

        WeatherDetailDTO futureWeather = response.getBody();
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
        WeatherEntity detail = new WeatherEntity();

        for (int j = 0; j < 40; j = j + 8) {
            String icon = (Constant.ICON_PATH + futureWeather.getList().get(j).getWeather().get(0).getIcon() + Constant.TAIL_ICON_PATH);
            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
            String pressure = futureWeather.getList().get(j).getMain().getPressure();
            String wind = futureWeather.getList().get(j).getWind().getSpeed();
            String tempInCelcius = CommonUtil.FahtoCelsius(Double.parseDouble(futureWeather.getList().get(j).getMain().getTemp()));
            String city1 = futureWeather.getCity().getName();
            String date = futureWeather.getList().get(j).getDt_txt();
            detail = new WeatherEntity(icon, city1, tempInCelcius, clouds, wind, humidity, pressure, date);

            futureWeatherList.add(detail);
        }
        model.addAttribute("detail", futureWeatherList);
        return "user/weather";
    }

    @GetMapping("/save/{city}")
    public String saveWeather(@PathVariable String city, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
                getForEntity(Constant.WEATHER_URL + city +
                                Constant.APPID,
                        WeatherDTO.class);
        String currentDate = CommonUtil.formatDate();
        WeatherDTO currentWeather = responseWeather.getBody();
        WeatherEntity weather1 = new WeatherEntity(currentWeather.getWeather().get(0).getIcon(),
                currentWeather.getName(), currentWeather.getMain().getTemp(), currentWeather.getWeather().get(0).getDescription(),
                currentWeather.getWind().getSpeed(), currentWeather.getMain().getHumidity(), currentWeather.getMain().getPressure(),
                currentDate);
        weather1.setUsers(new HashSet<User>(Arrays.asList(userService.findUserByUsername(name))));
        weatherService.saveWeather(weather1);
        model.addAttribute("successMessage", "Added successfully!");

        return "redirect:/";
    }


    @GetMapping("/delete-weather/{id}")
    public String deleteUser(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
