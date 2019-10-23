package com.example.project.DTO.WeatherPropertyDTO;

import com.example.project.DTO.*;

public class List {
    private Long dt;
    private MainDetail main;
    private java.util.List<Weather> weather = null;
    private Clouds clouds;
    private Wind wind;
    private RainDetail rain;
    private SysDetail sys;
    private String dt_txt;

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public SysDetail getSys() {
        return sys;
    }

    public void setSys(SysDetail sys) {
        this.sys = sys;
    }

    public java.util.List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(java.util.List<Weather> weather) {
        this.weather = weather;
    }

    public MainDetail getMain() {
        return main;
    }

    public void setMain(MainDetail main) {
        this.main = main;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public RainDetail getRain() {
        return rain;
    }

    public void setRain(RainDetail rain) {
        this.rain = rain;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }
}
