package com.makeitreal.weatherapp.repository;

import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherRepository extends JpaRepository<WeatherRequestLog, Long> {
    List<WeatherRequestLog> findByUser(String user);
    List<WeatherRequestLog> findByPostalCode(String postalCode);
}
