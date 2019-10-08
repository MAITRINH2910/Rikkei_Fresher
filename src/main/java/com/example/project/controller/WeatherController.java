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
     *
     * @param city
     * @param model
     * @param modelMap
     * @return
     */
    @GetMapping("/search-city")
    public String findWeatherByCity(@RequestParam String city, Model model, ModelMap modelMap) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
        for (int i = 0; i < weatherList.size(); i++) {
            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
        }

        List<WeatherEntity> listCity = weatherService.findCity(user.getId());
        model.addAttribute("listCities", listCity);
        List<List<WeatherEntity>> weatherGroupByCity = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++) {
            weatherGroupByCity.add((weatherService.findWeatherGroupByCityName(listCity.get(i).getNameCity(), user.getId())));
        }
        model.addAttribute("weatherList0", weatherGroupByCity);

        // filter weather by city
        List<WeatherEntity> listWeatherByCity = weatherList.stream()
                .filter(x -> x.getNameCity().equals(city)).collect(Collectors.toList());
        Date currentDate = new Date();
//        WeatherEntity curWeather = listWeatherByCity.stream().
//                filter(x -> x.getDate().equals(CommonUtil.formatDate())).findAny().orElse(null);
        WeatherEntity curWeather = listWeatherByCity.stream().
                filter(x -> x.getDate().equals(currentDate)).findAny().orElse(null);
        if (curWeather != null) {
            modelMap.addAttribute("status", "update");
        } else {
            modelMap.addAttribute("status", "add");
        }

        try {
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<WeatherDTO> responseWeather = restTemplate.
//                    getForEntity(Constant.WEATHER_URL + city +
//                                    Constant.APPID,
//                            WeatherDTO.class);
//            WeatherDTO currentWeather = responseWeather.getBody();
//            String temp = CommonUtil.KelvintoCelsius(Double.parseDouble(currentWeather.getMain().getTemp()));
//            String currentDate = CommonUtil.formatDate();
//            String iconPath = Constant.ICON_PATH + currentWeather.getWeather().get(0).getIcon() + Constant.TAIL_ICON_PATH;
//            String humidity = currentWeather.getMain().getHumidity();
//            String pressure = currentWeather.getMain().getPressure();
//            String wind = currentWeather.getWind().getSpeed();
//            String general = wind + "m/s.   " + humidity + "%.   " + pressure + "hpa.";
//            model.addAttribute("icon", iconPath);
//            model.addAttribute("city", currentWeather.getName());
//            model.addAttribute("date", currentDate);
//            model.addAttribute("temp", temp);
//            model.addAttribute("description", currentWeather.getWeather().get(0).getMain());
//            model.addAttribute("general", general);
//            model.addAttribute("currentWeather", currentWeather);
//            RestTemplate restTemplate = new RestTemplate();
//            ResponseEntity<WeatherEntity> responseWeather = restTemplate.
//                    getForEntity(Constant.WEATHER_URL + city +
//                                    Constant.APPID,
//                            WeatherEntity.class);
//            WeatherEntity currentWeather = responseWeather.getBody();
            WeatherEntity currentWeather = weatherApi.restJsonData(city);
            model.addAttribute("currentWeather", currentWeather);
        }catch (Exception e){
            model.addAttribute("message","City is not found!!!");
        }

        return "user/home";
    }

    /**
     * WEATHER DETAIL
     *
     * @param city
     * @param model
     * @return
     */
//    @GetMapping("/weather-detail/{city}")
//    public String getWeatherDetail(@PathVariable String city, Model model) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<WeatherDetailDTO> response = restTemplate.
//                getForEntity(Constant.FORECAST_URL + city +
//                                Constant.APPID,
//                        WeatherDetailDTO.class);
//
//        WeatherDetailDTO futureWeather = response.getBody();
//        List<WeatherEntity> futureWeatherList = new ArrayList<WeatherEntity>();
//
//        for (int j = 0; j < 40; j = j + 8) {
//            String icon = (Constant.ICON_PATH + futureWeather.getList().get(j).getWeather().get(0).getIcon() + Constant.TAIL_ICON_PATH);
//            String clouds = futureWeather.getList().get(j).getWeather().get(0).getDescription();
//            String humidity = futureWeather.getList().get(j).getMain().getHumidity();
//            String pressure = futureWeather.getList().get(j).getMain().getPressure();
//            String wind = futureWeather.getList().get(j).getWind().getSpeed();
//            String temp = CommonUtil.KelvintoCelsius(Double.parseDouble(futureWeather.getList().get(j).getMain().getTemp()));
//            String city1 = futureWeather.getCity().getName();
//            String date = futureWeather.getList().get(j).getDt_txt();
//            WeatherEntity detail = new WeatherEntity(icon, city1, temp, clouds, wind, humidity, pressure, date);
//            futureWeatherList.add(detail);
//        }
//        model.addAttribute("detail", futureWeatherList);
//        return "user/weather";
//    }

    /**
     * ADD WEATHER
     * DATABASE < 3 RECORDS
     *
     * @param city
     * @return
     */
    @GetMapping("/save-weather/{city}")
    public String saveWeather(@PathVariable String city) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(authUser.getName());
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
//                getForEntity(Constant.WEATHER_URL + city +
//                                Constant.APPID,
//                        WeatherDTO.class);
//        String currentDate = CommonUtil.formatDate();
//        WeatherDTO currentWeather = responseWeather.getBody();
        WeatherEntity currentWeather = weatherApi.restJsonData(city);
        System.out.println(currentWeather.getDate());
//        WeatherEntity weather1 = new WeatherEntity(currentWeather.getWeather().get(0).getIcon(),
//                currentWeather.getName(), currentWeather.getMain().getTemp(), currentWeather.getWeather().get(0).getDescription(),
//                currentWeather.getWind().getSpeed(), currentWeather.getMain().getHumidity(), currentWeather.getMain().getPressure(),
//                currentDate);
        WeatherEntity weather1 = new WeatherEntity(currentWeather.getIcon(),currentWeather.getNameCity(),
                currentWeather.getTemp(),currentWeather.getDescription(),currentWeather.getWind(),
                currentWeather.getHumidity(),currentWeather.getPressure(),currentWeather.getDate());
        weather1.setUsers(new HashSet<>(Arrays.asList(userService.findUserByUsername(user.getUsername()))));
        // lst weather by user
        List<WeatherEntity> lstByUser = weatherService.getWeatherByUser(user);
        // lay ra ds city theo iduser va nameCity
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
        WeatherEntity result = weatherApi.restJsonData(name);
//        result.setDate(CommonUtil.formatDate());
        result.setDate(new Date());
        result.setUsers(new HashSet<User>(Arrays.asList(userEntity)));
        weatherService.saveWeather(result);
    }
//
//    /**
//     * UPDATE WEATHER
//     *
//     * @param city
//     * @return
//     */
//    @GetMapping("/update-weather/{city}")
//    public String updateWeather(@PathVariable String city, Model model) {
//        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
//        User user = userService.findUserByUsername(authUser.getName());
//        List<WeatherEntity> weatherList = weatherService.getWeatherByUser(user);
////        for (int i = 0; i < weatherList.size(); i++) {
////            weatherList.get(i).setIcon(Constant.ICON_PATH + weatherList.get(i).getIcon() + Constant.TAIL_ICON_PATH);
////        }
//        model.addAttribute("weatherList", weatherList);
//
//
//        // filter weather by city
//        List<WeatherEntity> listWeatherByCity = weatherList.stream()
//                .filter(x -> x.getNameCity().equals(city)).collect(Collectors.toList());
//        // filter weather by date
//        WeatherEntity curWeather = listWeatherByCity.stream().
//                filter(x -> x.getDate().equalsIgnoreCase(CommonUtil.formatDate())).findAny().orElse(null);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<WeatherDTO> responseWeather = restTemplate.
//                getForEntity(Constant.WEATHER_URL + city +
//                                Constant.APPID,
//                        WeatherDTO.class);
//
//        WeatherDTO currentWeather = responseWeather.getBody();
//        curWeather.setIcon(currentWeather.getWeather().get(0).getIcon());
//        curWeather.setTemp(CommonUtil.KelvintoCelsius(Double.parseDouble(currentWeather.getMain().getTemp())));
//        curWeather.setWind(currentWeather.getWind().getSpeed());
//        curWeather.setHumidity(currentWeather.getMain().getHumidity());
//        curWeather.setPressure(currentWeather.getMain().getPressure());
//        curWeather.setDescription(currentWeather.getWeather().get(0).getDescription());
//        weatherService.saveWeather(curWeather);
//        return "redirect:/";
//    }
//
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
