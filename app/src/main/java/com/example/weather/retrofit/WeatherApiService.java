package com.example.weather.retrofit;

import com.example.weather.model.WeatherModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {

    @GET("weather")
    Observable<WeatherModel> getWeatherFromCityName(@Query("q") String query, @Query("appid") String appId);

    @GET("weather")
    Observable<WeatherModel> getWeatherFromLatLong(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appId);

}
