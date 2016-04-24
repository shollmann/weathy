package com.shollmann.weathy.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shollmann.weathy.R;
import com.shollmann.weathy.api.OpenWeatherApi;
import com.shollmann.weathy.api.baseapi.CallId;
import com.shollmann.weathy.api.baseapi.CallOrigin;
import com.shollmann.weathy.api.baseapi.CallType;
import com.shollmann.weathy.api.model.WeatherReport;
import com.shollmann.weathy.helper.Constants;
import com.shollmann.weathy.helper.ResourcesHelper;
import com.shollmann.weathy.ui.WeathyApplication;
import com.shollmann.weathy.ui.view.WeatherInformationView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView txtCurrentTemperature;
    private TextView txtCurrentLocation;
    private ImageView imgCurrentWeatherIcon;
    private WeatherInformationView viewBasicWeatherInformation;
    private CoordinatorLayout coordinatorLayout;
    private OpenWeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
        weatherApi = WeathyApplication.getApplication().getOpenWeatherApi();
        setupToolbar();

        getCurrentWeather();
    }

    private void getCurrentWeather() {
//    showUpdating();
        CallId weatherForCityNameCallId = new CallId(CallOrigin.HOME, CallType.WEATHER_REPORT_FOR_CITY_NAME);
        weatherApi.getWeatherForCityName("buenos+aires", weatherForCityNameCallId, generateGetCurrentWeatherForCityCallback());
    }

    private Callback<WeatherReport> generateGetCurrentWeatherForCityCallback() {
        return new Callback<WeatherReport>() {

            @Override
            public void success(WeatherReport weatherReport, Response response) {
//                    hideUpdating();
                updateWeatherInfo(weatherReport);
            }

            @Override
            public void failure(RetrofitError error) {
//                    hideUpdating();
                Snackbar.make(coordinatorLayout, R.string.error_get_current_weather, Snackbar.LENGTH_LONG);
            }
        };
    }

    private void updateWeatherInfo(WeatherReport weatherReport) {
        viewBasicWeatherInformation.setWeatherInfo(weatherReport.getMain());
        txtCurrentTemperature.setText(String.valueOf(weatherReport.getMain().getIntTemperature()) + "*C");//TODO Remove hardcoded string
        txtCurrentLocation.setText(weatherReport.getName());
        if (weatherReport.getWeather() != null) {
            imgCurrentWeatherIcon.setImageDrawable(getCurrentWeatherDrawable(weatherReport.getWeather().getMain()));
            imgCurrentWeatherIcon.setVisibility(View.VISIBLE);
        } else {
            imgCurrentWeatherIcon.setVisibility(View.GONE);
        }
    }

    private Drawable getCurrentWeatherDrawable(String weatherType) {
        if (Constants.WeatherType.RAIN.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_rain);
        } else if (Constants.WeatherType.CLEAR.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_sun);
        } else if (Constants.WeatherType.CLOUDS.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_cloud);
        } else if (Constants.WeatherType.DRIZZLE.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_snow); //TODO Change for drizzle
        } else if (Constants.WeatherType.THUDERSTORM.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_rain); //TODO Change for thunder storm
        } else if (Constants.WeatherType.HAZE.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_snow); //TODO change for haze
        } else if (Constants.WeatherType.SNOW.equalsIgnoreCase(weatherType)) {
            return ResourcesHelper.getDrawable(R.drawable.ic_snow);
        } else {
            return ResourcesHelper.getDrawable(R.drawable.ic_cloud);
        }
    }


    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtCurrentTemperature = (TextView) findViewById(R.id.home_current_weather_temperature);
        txtCurrentLocation = (TextView) findViewById(R.id.home_current_location);
        imgCurrentWeatherIcon = (ImageView) findViewById(R.id.home_current_weather_icon);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home_coordinator_layout);
        viewBasicWeatherInformation = (WeatherInformationView) findViewById(R.id.home_basic_weather_info);
    }

}
