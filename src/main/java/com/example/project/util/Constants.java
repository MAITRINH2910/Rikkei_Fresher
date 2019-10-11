package com.example.project.util;

public class Constants {
    public static final String HOST_HTTP = "http://";
    public static final String DOMAIN = "openweathermap.org";
    public static final String VER_WEATHER = "2.5/";
    public static final String ICON_PATH = HOST_HTTP + DOMAIN + "/img/w/";
    public static final String TAIL_ICON_PATH = ".png";
    public static final String WEATHER_URL = HOST_HTTP + "api." + DOMAIN + "/data/" + VER_WEATHER + "weather?q=";
    public static final String FORECAST_URL = HOST_HTTP + "api." + DOMAIN + "/data/" + VER_WEATHER + "forecast?q=";
    public static final String APPID = "&APPID=d25fa79fdf7dab072800852fcd5e716b";
}

