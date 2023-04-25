package com.example.weather.retrofit;

import com.example.weather.model.WeatherModel;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class Repository {

    private WeatherApiService weatherApiService;

    @Inject
    public Repository(WeatherApiService weatherApiService) {
        this.weatherApiService = weatherApiService;
    }

    public Observable<WeatherModel> getWeatherFromCityName(String query, String appid) {
        return weatherApiService.getWeatherFromCityName(query, appid);
    }

    public Observable<WeatherModel> getWeatherFromLatLong(String lat, String lon, String appId) {
        return weatherApiService.getWeatherFromLatLong(lat, lon, appId);
    }
}
