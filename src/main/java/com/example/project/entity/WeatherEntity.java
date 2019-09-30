package com.example.project.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "weather")
public class WeatherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weather_id")
    private Long weatherId;

    @Column(name = "icon")
    private String icon;

    @Column(name = "name_city")
    private String nameCity;

    @Column(name = "date")
    private String date;

    @Column(name = "temp")
    private String temp;

    @Column(name = "description")
    private String description;

    @Column(name = "wind")
    private String wind;

    @Column(name = "humidity")
    private String humidity;

    @Column(name = "pressure")
    private String pressure;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_weather", joinColumns = @JoinColumn(name = "weather_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public WeatherEntity() {
    }

    public WeatherEntity(String icon, String nameCity, String temp, String description, String wind, String humidity, String pressure,String date ) {
        this.icon = icon;
        this.nameCity = nameCity;
        this.temp = temp;
        this.description = description;
        this.wind = wind;
        this.humidity = humidity;
        this.pressure = pressure;
        this.date =date;
    }

    public Long getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(Long weatherId) {
        this.weatherId = weatherId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public String getNameCity() {
        return nameCity;
    }

    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }

      public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

}
