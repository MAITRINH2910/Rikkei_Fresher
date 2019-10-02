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
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @Autowired
    UserService userService;

    @Autowired
    WeatherApi weatherApi;

    /**
     * SEARCH WEATHER
     * @param city
     * @param model
     * @param modelMap
     * @param principal
     * @return
     */
    @GetMapping("/search-city")
    public String findWeatherByCity(@RequestParam String city, Model model, ModelMap modelMap, Principal principal) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
        }
        model.addAttribute("weatherList", weatherList);


        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getNameCity().equals(city)).collect(Collectors.toList());
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> x.getDate().equalsIgnoreCase(CommonUtil.formatDate())).findAny().orElse(null);
        if (curWeather != null) {
            modelMap.addAttribute("status", "update");
        } else {
            modelMap.addAttribute("status", "add");
        }
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
                getForEntity(Constant.WEATHER_URL + city +
                                Constant.APPID,
                        WeatherDTO.class);
        WeatherDTO currentWeather = responseWeather.getBody();
        String temp = CommonUtil.FahtoCelsius(Double.parseDouble(currentWeather.getMain().getTemp()));
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
        model.addAttribute("temp", temp);
        model.addAttribute("description", currentWeather.getWeather().get(0).getMain());
        model.addAttribute("general", general);
        return "user/home";
    }

    /**
     * WEATHER DETAIL
     * @param city
     * @param model
     * @return
     */
    @GetMapping("/weather-detail/{city}")
    public String getWeatherDetail(@PathVariable String city, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherDetailDTO> response = restTemplate.
                getForEntity(Constant.FORECAST_URL + city +
                                Constant.APPID,
                        WeatherDetailDTO.class);

        WeatherDetailDTO futureWeather = response.getBody();
        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();

        for (int j = 0; j < 40; j = j + 8) {
            String icon = (Constant.ICON_PATH + futureWeather.getList().get(j).getWeather().get(0).getIcon() + Constant.TAIL_ICON_PATH);
            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
            String pressure = futureWeather.getList().get(j).getMain().getPressure();
            String wind = futureWeather.getList().get(j).getWind().getSpeed();
            String temp = CommonUtil.FahtoCelsius(Double.parseDouble(futureWeather.getList().get(j).getMain().getTemp()));
            String city1 = futureWeather.getCity().getName();
            String date = futureWeather.getList().get(j).getDt_txt();
            WeatherEntity detail = new WeatherEntity(icon, city1, temp, clouds, wind, humidity, pressure, date);
            futureWeatherList.add(detail);
        }
        model.addAttribute("detail", futureWeatherList);
        return "user/weather";
    }

    /**
     * ADD WEATHER
     * DATABASE < 3 RECORDS
     * @param city
     * @return
     */
    @GetMapping("/save-weather/{city}")
    public String saveWeather(@PathVariable String city) {
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
        weather1.setUsers(new HashSet<>(Arrays.asList(userService.findUserByUsername(name))));
        User userEntity = userService.findUserByUsername(name);
        // lst weather by user
        List<WeatherEntity> lstByUser = weatherService.getWeatherByUser(userEntity);
        // lay ra ds city theo iduser va nameCity
        List<WeatherEntity> lstByUserByCity = lstByUser.stream()
                .filter(weather -> name.trim().toLowerCase().equals(weather.getNameCity().trim().toLowerCase()))
                .collect(Collectors.toList());
        if (lstByUserByCity.size() < 3) {

            weatherService.saveWeather(weather1);
        }
        weatherService.saveWeather(weather1);

        return "redirect:/";
    }

    /**
     * UPDATE WEATHER
     * @param city
     * @return
     */
    @GetMapping("/update-weather/{city}")
    public String updateWeather(@PathVariable String city, Model model) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
        }
        model.addAttribute("weatherList", weatherList);

        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getNameCity().equals(city)).collect(Collectors.toList());
        // filter weather by date
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> x.getDate().equalsIgnoreCase(CommonUtil.formatDate())).findAny().orElse(null);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
                getForEntity(Constant.WEATHER_URL + city +
                                Constant.APPID,
                        WeatherDTO.class);

        WeatherDTO currentWeather = responseWeather.getBody();
        curWeather.setNameCity(currentWeather.getName());
        curWeather.setTemp(currentWeather.getMain().getTemp());
        curWeather.setWind(currentWeather.getWind().getSpeed());
        curWeather.setHumidity(currentWeather.getMain().getHumidity());
        curWeather.setPressure(currentWeather.getMain().getPressure());

        weatherService.saveWeather(curWeather);
        return "redirect:/";
    }

    /**
     * DELETE WEATHER
     * @param id
     * @return
     */
    @GetMapping("/delete-weather/{id}")
    public String deleteWeather(@PathVariable Long id) {
        weatherService.deleteWeather(id);
        return "redirect:/";
    }
}
