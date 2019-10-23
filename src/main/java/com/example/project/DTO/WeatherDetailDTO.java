package com.example.project.DTO;

import com.example.project.DTO.WeatherPropertyDTO.City;

import java.util.List;

public class WeatherDetailDTO {
    private Integer cod;
    private String message;
    private Integer cnt;
    private List<com.example.project.DTO.WeatherPropertyDTO.List> list;
    private City city;


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public List<com.example.project.DTO.WeatherPropertyDTO.List> getList() {
        return list;
    }

    public void setList(List<com.example.project.DTO.WeatherPropertyDTO.List> list) {
        this.list = list;
    }
}
