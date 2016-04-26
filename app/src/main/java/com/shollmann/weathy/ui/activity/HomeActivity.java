package com.shollmann.weathy.ui.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.shollmann.weathy.R;
import com.shollmann.weathy.api.OpenWeatherApi;
import com.shollmann.weathy.api.baseapi.CallId;
import com.shollmann.weathy.api.baseapi.CallOrigin;
import com.shollmann.weathy.api.baseapi.CallType;
import com.shollmann.weathy.api.model.WeatherReport;
import com.shollmann.weathy.helper.ResourcesHelper;
import com.shollmann.weathy.ui.WeathyApplication;
import com.shollmann.weathy.ui.view.WeatherInformationView;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CIRCULAR_REVEAL_DURATION = 400;

    private Toolbar toolbar;
    private TextView txtCurrentTemperature;
    private TextView txtCurrentLocation;
    private ImageView imgCurrentWeatherIcon;
    private WeatherInformationView viewBasicWeatherInformation;
    private WeatherInformationView viewAdvanceWeatherInformation;
    private CoordinatorLayout coordinatorLayout;
    private OpenWeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
        weatherApi = WeathyApplication.getApplication().getOpenWeatherApi();
        setupToolbar();
        setOnClickListener();
        getCurrentWeather();
    }

    private void setOnClickListener() {
        viewBasicWeatherInformation.setOnClickListener(this);
        viewAdvanceWeatherInformation.setOnClickListener(this);
    }

    private void getCurrentWeather() {
//    showUpdating();
        CallId weatherForCityNameCallId = new CallId(CallOrigin.HOME, CallType.WEATHER_REPORT_FOR_CITY_NAME);
        weatherApi.getWeatherForCityName("ushuaia", weatherForCityNameCallId, generateGetCurrentWeatherForCityCallback());
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
        txtCurrentTemperature.setText(String.valueOf(weatherReport.getMain().getIntTemperature()) + "C");//TODO Remove hardcoded string
        txtCurrentLocation.setText(weatherReport.getName());
        if (weatherReport.getWeather() != null) {
            imgCurrentWeatherIcon.setImageDrawable(ResourcesHelper.getCurrentWeatherDrawable(weatherReport.getWeather().getMain()));
            imgCurrentWeatherIcon.setVisibility(View.VISIBLE);
        } else {
            imgCurrentWeatherIcon.setVisibility(View.GONE);
        }

        viewAdvanceWeatherInformation.setWeatherInfo(weatherReport.getMain(), weatherReport.getWind());

        viewBasicWeatherInformation.setWeatherInfo(weatherReport.getMain());
        viewBasicWeatherInformation.setVisibility(View.VISIBLE);
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
        viewAdvanceWeatherInformation = (WeatherInformationView) findViewById(R.id.home_advance_weather_info);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_basic_weather_info) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                View viewToReveal = viewAdvanceWeatherInformation;

                int centerX = (viewToReveal.getLeft() + viewToReveal.getRight()) / 2;
                int centerY = (viewToReveal.getTop() + viewToReveal.getBottom()) / 2;

                int startRadius = 0;
                int endRadius = Math.max(view.getWidth(), view.getHeight());

                Animator anim = ViewAnimationUtils.createCircularReveal(viewToReveal, centerX, centerY, startRadius, endRadius);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(CIRCULAR_REVEAL_DURATION);
                anim.start();
                viewToReveal.setVisibility(View.VISIBLE);
            }
        }
    }
}
