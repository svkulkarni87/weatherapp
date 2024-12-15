package com.makeitreal.weatherapp.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeitreal.weatherapp.repository.WeatherRepository;
import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeatherService {

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather";
    @Value("${openweathermap.api.key}")
    private String apiKey;
    private final WeatherRepository weatherRepository;

    public WeatherService(WeatherRepository weatherRepository) {
        this.weatherRepository = weatherRepository;
    }

    /***
     *
     * @param postalCode has input to fetches weather report
     * @param user input to store which user requested the report
     * @return weather information, such as temperature, humidity, weather conditions
     */
    public String getWeatherByPostalCode(String postalCode, String user) {
        String url = String.format("%s?zip=%s,US&appid=%s", API_URL, postalCode, apiKey);
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // Parse response to extract temperature, humidity, and weather condition
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONObject main = jsonResponse.getJSONObject("main");
            String temperature = main.get("temp").toString();
            String humidity = main.get("humidity").toString();
            String weatherCondition = jsonResponse.getJSONArray("weather").getJSONObject(0).get("description").toString();
            saveRequestToDatabase(postalCode, user, temperature, humidity, weatherCondition);
            return String.format("Temperature: %s°C, Humidity: %s%%, Condition: %s", temperature, humidity, weatherCondition);
            //return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle 4xx errors
            throw new ResponseStatusException(e.getStatusCode(), "Client Error: " + e.getResponseBodyAsString(), e);
        } catch (HttpServerErrorException e) {
            // Handle 5xx errors
            throw new ResponseStatusException(e.getStatusCode(), "Server Error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // Handle other exceptions
            throw new ResponseStatusException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error", e);
        }
    }

    /***
     * saves the weather request report to database table
     * @param postalCode
     * @param user
     * @param temperature
     * @param humidity
     * @param weatherCondition
     */
    private void saveRequestToDatabase(String postalCode, String user,String temperature,
                                       String humidity , String weatherCondition ) {
        WeatherRequestLog log = new WeatherRequestLog();
        log.setPostalCode(postalCode);
        log.setUser(user);
        log.setTimestamp(LocalDateTime.now());
        log.setTemperature(temperature);
        log.setHumidity(humidity);
        log.setWeatherCondition(weatherCondition);
        weatherRepository.save(log);
    }

    // returns weather report requested by the user
    public List<WeatherRequestLog> getUserRequestHistory(String user) {
        return weatherRepository.findByUser(user);
    }
    // returns weather report requested by the postalCode
    public List<WeatherRequestLog> getPostalCodeRequestHistory(String postalCode) {
        return weatherRepository.findByPostalCode(postalCode);
    }
}
