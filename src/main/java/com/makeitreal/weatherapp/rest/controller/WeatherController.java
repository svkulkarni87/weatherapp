package com.makeitreal.weatherapp.rest.controller;

import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import com.makeitreal.weatherapp.rest.entity.dto.WeatherRequest;
import com.makeitreal.weatherapp.rest.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app")
@Tag(name = "Weather", description = "Endpoints for weather operations")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Operation(summary = "Get weather by postal code")
    @PostMapping("/weather")
    public String getWeather(@RequestBody WeatherRequest request) {
        return weatherService.getWeatherByPostalCode(request.getPostalCode(), request.getUser());
    }

    @Operation(summary = "Get weather request history by user")
    @GetMapping("/history")
    public List<WeatherRequestLog> getRequestHistory(@RequestParam String user) {
        return weatherService.getUserRequestHistory(user);
    }

    @Operation(summary = "Get weather by postal code")
    @GetMapping("/history/postalCode")
    public List<WeatherRequestLog> getPostalCodeHistory(@RequestParam int postalCode) {
        return weatherService.getPostalCodeRequestHistory(String.valueOf(postalCode));
    }
}
