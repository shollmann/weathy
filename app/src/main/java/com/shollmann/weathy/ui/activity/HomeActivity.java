package com.shollmann.weathy.ui.activity;

import android.animation.Animator;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
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

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    private static final int CIRCULAR_REVEAL_DURATION = 400;

    private Toolbar toolbar;
    private TextView txtCurrentTemperature;
    private TextView txtCurrentLocation;
    private TextView txtAdvanceInfoHint;
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
        setupTaskDescription();
        setupToolbar();
        setOnClickListener();
        //TODO handle previous search
        getCurrentWeather("Capital Federal");
    }

    private void setOnClickListener() {
        viewBasicWeatherInformation.setOnClickListener(this);
        viewAdvanceWeatherInformation.setOnClickListener(this);
    }

    private void getCurrentWeather(String query) {
        Snackbar.make(coordinatorLayout, R.string.updating_weather, Snackbar.LENGTH_SHORT).show();
        CallId weatherForCityNameCallId = new CallId(CallOrigin.HOME, CallType.WEATHER_REPORT_FOR_CITY_NAME);
        weatherApi.getWeatherForCityName(query.trim(), weatherForCityNameCallId, generateGetCurrentWeatherForCityCallback());
    }

    private Callback<WeatherReport> generateGetCurrentWeatherForCityCallback() {
        return new Callback<WeatherReport>() {

            @Override
            public void success(WeatherReport weatherReport, Response response) {
                updateWeatherInfo(weatherReport);
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(coordinatorLayout, R.string.error_get_current_weather, Snackbar.LENGTH_LONG).show();
            }
        };
    }

    private void updateWeatherInfo(WeatherReport weatherReport) {
        if (weatherReport.getCod() != Constants.ErrorCode._404) {
            txtCurrentTemperature.setText(String.valueOf(weatherReport.getMain().getIntTemperature()) + Constants.SpecialChars.CELSIUS_DEGREES);
            txtCurrentLocation.setText(weatherReport.getName());
            txtCurrentTemperature.setVisibility(View.VISIBLE);
            txtCurrentLocation.setVisibility(View.VISIBLE);
            if (weatherReport.getWeather() != null) {
                imgCurrentWeatherIcon.setImageDrawable(ResourcesHelper.getCurrentWeatherDrawable(weatherReport.getWeather().getMain()));
                imgCurrentWeatherIcon.setVisibility(View.VISIBLE);
            } else {
                imgCurrentWeatherIcon.setVisibility(View.GONE);
            }

            viewAdvanceWeatherInformation.setWeatherInfo(weatherReport.getMain(), weatherReport.getWind());
            viewBasicWeatherInformation.setWeatherInfo(weatherReport.getMain());
            viewBasicWeatherInformation.setVisibility(View.VISIBLE);
            txtAdvanceInfoHint.setVisibility(View.VISIBLE);
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
        viewAdvanceWeatherInformation = (WeatherInformationView) findViewById(R.id.home_advance_weather_info);
        txtAdvanceInfoHint = (TextView) findViewById(R.id.home_advance_info_hint);
    }

    @Override
    public void onClick(View view) {
        txtAdvanceInfoHint.setVisibility(View.GONE);
        if (view.getId() == R.id.home_basic_weather_info) {
            displayAdvanceWeatherInfo(true);
        } else {
            displayAdvanceWeatherInfo(false);
        }
    }

    private void setupTaskDescription() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(ResourcesHelper.getResources(),
                    R.drawable.ic_sun);
            ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(ResourcesHelper.getString(R.string.app_name), icon, ResourcesHelper.getResources().getColor(R.color.colorPrimary));
            this.setTaskDescription(taskDescription);
        }
    }

    private void displayAdvanceWeatherInfo(boolean isDisplay) {
        View baseView = viewBasicWeatherInformation;
        View viewToReveal = viewAdvanceWeatherInformation;

        int centerX = (viewToReveal.getLeft() + viewToReveal.getRight()) / 2;
        int centerY = (viewToReveal.getTop() + viewToReveal.getBottom()) / 2;
        int startRadius = 0;
        int endRadius = Math.max(baseView.getWidth(), baseView.getHeight());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(viewToReveal, centerX, centerY, isDisplay ? startRadius : endRadius, isDisplay ? endRadius : startRadius);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.setDuration(CIRCULAR_REVEAL_DURATION);
            if (!isDisplay) {
                hideAdvanceInfoAfterAnimation(anim);
            }
            anim.start();
            viewToReveal.setVisibility(View.VISIBLE);
        } else {
            viewToReveal.setVisibility(isDisplay ? View.VISIBLE : View.GONE);
        }
    }

    private void hideAdvanceInfoAfterAnimation(Animator anim) {
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                viewAdvanceWeatherInformation.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuSearch = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);
        searchView.setOnQueryTextListener(this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getCurrentWeather(query);
        hideKeyboard();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) WeathyApplication.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
}
