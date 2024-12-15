package com.makeitreal.weatherapp.rest.service;

import com.makeitreal.weatherapp.repository.WeatherRepository;
import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;

@Service
public class WeatherService {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    @Value("${openweathermap.api.key}")
    private String apiKey;
    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    public String getWeatherByPostalCode(String postalCode, String user) {
        String url = String.format("%s?zip=%s,US&appid=%s", API_URL, postalCode, apiKey);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            saveRequestToDatabase(postalCode, user);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("Client error while fetching weather data: " + e.getMessage(), e);
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("Server error while fetching weather data: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while fetching weather data: " + e.getMessage(), e);
        }
    }
    private void saveRequestToDatabase(String postalCode, String user) {
        WeatherRequestLog log = new WeatherRequestLog();
        log.setPostalCode(postalCode);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());
        weatherRepository.save(log);
    }
}
