package com.makeitreal.weatherapp.repository;

import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<WeatherRequestLog, Long> {
}
