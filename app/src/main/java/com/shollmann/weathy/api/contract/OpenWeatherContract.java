package com.shollmann.weathy.api.contract;

import com.shollmann.weathy.api.model.WeatherReport;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface OpenWeatherContract {
    @GET("/data/2.5/weather")
    void getWeatherForCityName(
            @Query("q") String cityName,
            Callback<WeatherReport> callback);

}
