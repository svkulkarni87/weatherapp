package com.makeitreal.weatherapp.rest.controller;

import com.makeitreal.weatherapp.rest.entity.dto.WeatherRequest;
import com.makeitreal.weatherapp.rest.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/{postalCode}")
    public ResponseEntity<?> getWeather(@PathVariable String postalCode) {
        try {
            String weatherData = weatherService.getWeatherByPostalCode(postalCode, null);
            return ResponseEntity.ok(weatherData);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    @PostMapping("/weather")
    public String getWeather(@RequestBody WeatherRequest request) {
        return weatherService.getWeatherByPostalCode(request.getPostalCode(), request.getUser());
    }
}
