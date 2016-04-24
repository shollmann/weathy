package com.shollmann.weathy.ui;

import android.app.Application;

import com.shollmann.weathy.api.OpenWeatherApi;
import com.shollmann.weathy.db.CachingDbHelper;
import com.shollmann.weathy.helper.Constants;

public class WeathyApplication extends Application {
    private static WeathyApplication instance;
    private CachingDbHelper cachingDbHelper;
    private OpenWeatherApi openWeatherApi;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
        this.openWeatherApi = new OpenWeatherApi(Constants.OpenWeatherApi.URL);
        this.cachingDbHelper = new CachingDbHelper(getApplicationContext());
    }

    public static WeathyApplication getApplication() {
        return instance;
    }

    public CachingDbHelper getCachingDbHelper() {
        return cachingDbHelper;
    }

    public OpenWeatherApi getOpenWeatherApi() {
        return openWeatherApi;
    }
}
