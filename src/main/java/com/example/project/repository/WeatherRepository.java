package com.example.project.repository;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    List<WeatherEntity> findAllByUsers(User user);

    WeatherEntity findByWeatherId(Long id);

    @Query(value = "SELECT * FROM weather_project.user_weather JOIN weather_project.weather ON user_weather.weather_id = weather.weather_id WHERE weather.name_city= :city AND user_weather.user_id= :userId", nativeQuery = true)
    List<WeatherEntity> findWeatherGroupByCityName(
            @Param("city") String status,
            @Param("userId") Long id);

    @Query(value = "SELECT weather.weather_id, name_city FROM weather_project.weather JOIN weather_project.user_weather ON user_weather.weather_id = weather.weather_id WHERE user_weather.user_id= :userId GROUP BY name_city", nativeQuery = true)
    List<WeatherEntity> findCity(@Param("userId") Long id);

}
