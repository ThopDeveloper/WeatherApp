package com.example.weather.viewmodel;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weather.retrofit.Repository;
import com.example.weather.model.WeatherModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WeatherViewModel extends ViewModel {

    private static final String TAG = "PokemonViewModel";
    private Repository repository;
    private MutableLiveData<WeatherModel> weatherData = new MutableLiveData<>();

    @ViewModelInject
    public WeatherViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<WeatherModel> getWeather() {
        return weatherData;
    }

    @SuppressLint("CheckResult")
    public void getWeatherFromCityName(String query, String appId){
        repository.getWeatherFromCityName(query,appId)
                .subscribeOn(Schedulers.io())
                .map(weatherModel -> weatherModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> weatherData.setValue(result),
                        error-> Log.e(TAG, "getWeather: " + error.getMessage() ));
    }
    @SuppressLint("CheckResult")
    public void getWeatherFromLatLong(String lat,String lon,String appId){
        repository.getWeatherFromLatLong(lat,lon ,appId)
                .subscribeOn(Schedulers.io())
                .map(weatherModel -> weatherModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> weatherData.setValue(result),
                        error-> Log.e(TAG, "getWeather: " + error.getMessage() ));
    }

}
