package com.makeitreal.weatherapp;

import com.makeitreal.weatherapp.repository.WeatherRepository;
import com.makeitreal.weatherapp.rest.entity.WeatherRequestLog;
import com.makeitreal.weatherapp.rest.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class WeatherappApplicationTests {

	@Test
	void contextLoads() {
	}
	@Mock
	private RestTemplate restTemplate;

	@Mock
	private WeatherRepository weatherRepository;

	@InjectMocks
	private WeatherService weatherService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetWeatherByPostalCode_Success() throws Exception {
		String postalCode = "10001";
		String countryCode = "US";
		String user = "test_user";
		String mockApiResponse = "{" +
				"\"main\": {\"temp\": 300.15, \"humidity\": 50}," +
				"\"weather\": [{\"description\": \"clear sky\"}]" +
				"}";

		when(restTemplate.getForEntity(anyString(), eq(String.class)))
				.thenReturn(new ResponseEntity<>(mockApiResponse, HttpStatus.OK));

		String response = weatherService.getWeatherByPostalCode(postalCode, user);

		assertNotNull(response);
		verify(weatherRepository, times(1)).save(any(WeatherRequestLog.class));
	}

	@Test
	public void testGetWeatherByPostalCode_ClientError() {
		String postalCode = "12345";
		String countryCode = "US";
		String user = "test_user";

		when(restTemplate.getForEntity(anyString(), eq(String.class)))
				.thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid postal code"));

		Exception exception = assertThrows(Exception.class, () -> {
			weatherService.getWeatherByPostalCode(postalCode, user);
		});

		assertTrue(exception.getMessage().contains("Client Error"));
	}

	@Test
	public void testSaveRequestToDatabase() {
		WeatherRequestLog log = new WeatherRequestLog();
		log.setPostalCode("10001");
		log.setUser("test_user");
		log.setTimestamp(LocalDateTime.now());
		log.setTemperature("300.15");
		log.setHumidity("50");
		log.setWeatherCondition("clear sky");

		when(weatherRepository.save(any(WeatherRequestLog.class))).thenReturn(log);

		weatherService.getWeatherByPostalCode("10001",  "test_user");
		verify(weatherRepository, times(1)).save(any(WeatherRequestLog.class));
	}

	@Test
	public void testGetUserRequestHistory() {
		String user = "test_user";
		WeatherRequestLog log = new WeatherRequestLog();
		log.setPostalCode("10001");
		log.setUser(user);
		log.setTimestamp(LocalDateTime.now());
		log.setTemperature("300.15");
		log.setHumidity("50");
		log.setWeatherCondition("clear sky");

		when(weatherRepository.findByUser(user)).thenReturn(List.of(log));

		List<WeatherRequestLog> history = weatherService.getUserRequestHistory(user);
		assertEquals(1, history.size());
		assertEquals("12345", history.get(0).getPostalCode());
	}

	@Test
	public void testGetPostalCodeRequestHistory() {
		String postalCode = "10001";
		WeatherRequestLog log = new WeatherRequestLog();
		log.setPostalCode(postalCode);
		log.setUser("test_user");
		log.setTimestamp(LocalDateTime.now());
		log.setTemperature("300.15");
		log.setHumidity("50");
		log.setWeatherCondition("clear sky");

		when(weatherRepository.findByPostalCode(postalCode)).thenReturn(List.of(log));

		List<WeatherRequestLog> history = weatherService.getPostalCodeRequestHistory(postalCode);
		assertEquals(1, history.size());
		assertEquals("test_user", history.get(0).getUser());
	}

}
