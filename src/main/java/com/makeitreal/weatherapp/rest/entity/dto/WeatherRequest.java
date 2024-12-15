package com.makeitreal.weatherapp.rest.entity.dto;

public class WeatherRequest {
    private String postalCode;
    private String user;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
