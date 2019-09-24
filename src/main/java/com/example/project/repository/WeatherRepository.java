package com.example.project.repository;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    WeatherEntity findByNameCity(String city);

    List<WeatherEntity> findWeatherEntitiesByUsers(User user);
}
