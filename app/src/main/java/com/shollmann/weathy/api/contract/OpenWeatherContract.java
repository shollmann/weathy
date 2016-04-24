package com.shollmann.weathy.api.contract;

import com.shollmann.weathy.api.model.WeatherReport;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OpenWeatherContract {
    //    http://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=c9347df1453d44f36c64fe861d46e36c
    @GET("data/2.5/weather")
    void getWeatherForCityName(
            @Query("q") String cityName,
            Callback<WeatherReport> callback);

}
