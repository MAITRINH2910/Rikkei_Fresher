package com.example.project.repository;

import com.example.project.entity.User;
import com.example.project.entity.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {
    List<WeatherEntity> findAllByUsers(User user);

    WeatherEntity findByWeatherId(Long id);

    WeatherEntity findByNameCity(String nameCity);

    Boolean existsByNameCity(String nameCity);

    List<WeatherEntity> findAllByDate(Date date);

    Long countAllByNameCity(String nameCity);
}
