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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private static final int NO_FLAGS = 0;
    private static final String FIRST_LOCATION_TO_DISPLAY = "Hong Kong";

    private RelativeLayout layoutMainWeatherContainer;
    private Toolbar toolbar;
    private TextView txtCurrentTemperature;
    private TextView txtCurrentLocation;
    private TextView txtAdvanceInfoHint;
    private TextView txtNoReport;
    private TextView txtWaitFirstReport;
    private ImageView imgCurrentWeatherIcon;
    private SearchView searchView;
    private MenuItem menuSearch;
    private WeatherInformationView viewBasicWeatherInformation;
    private WeatherInformationView viewAdvanceWeatherInformation;
    private CoordinatorLayout coordinatorLayout;
    private OpenWeatherApi weatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        weatherApi = WeathyApplication.getApplication().getOpenWeatherApi();

        findViews();
        setupTaskDescription();
        setupToolbar();
        setOnClickListener();

        getCurrentWeather(FIRST_LOCATION_TO_DISPLAY);
    }

    private void setOnClickListener() {
        viewBasicWeatherInformation.setOnClickListener(this);
        viewAdvanceWeatherInformation.setOnClickListener(this);
    }

    private void getCurrentWeather(String query) {
        if (!TextUtils.isEmpty(query)) {
            Snackbar.make(coordinatorLayout, R.string.updating_weather, Snackbar.LENGTH_SHORT).show();
            CallId weatherForCityNameCallId = new CallId(CallOrigin.HOME, CallType.WEATHER_REPORT_FOR_CITY_NAME);
            weatherApi.getWeatherForCityName(query.trim(), weatherForCityNameCallId, generateGetCurrentWeatherForCityCallback());
        } else {
            Snackbar.make(coordinatorLayout, R.string.please_enter_a_location, Snackbar.LENGTH_SHORT).show();
        }
    }

    private Callback<WeatherReport> generateGetCurrentWeatherForCityCallback() {
        return new Callback<WeatherReport>() {

            @Override
            public void success(WeatherReport weatherReport, Response response) {
                updateWeatherInfo(weatherReport);
            }

            @Override
            public void failure(RetrofitError error) {
                handleWeatherReportFailure();
            }
        };
    }

    private void handleWeatherReportFailure() {
        txtWaitFirstReport.setVisibility(View.GONE);
        layoutMainWeatherContainer.setVisibility(View.GONE);
        txtNoReport.setVisibility(View.VISIBLE);
    }

    private void updateWeatherInfo(WeatherReport weatherReport) {
        txtWaitFirstReport.setVisibility(View.GONE);
        txtNoReport.setVisibility(View.GONE);

        if (weatherReport.getCod() != Constants.ErrorCode._404) {
            layoutMainWeatherContainer.setVisibility(View.VISIBLE);
            txtCurrentTemperature.setText(String.valueOf(weatherReport.getMain().getIntTemperature()) + Constants.SpecialChars.CELSIUS_DEGREES);
            txtCurrentLocation.setText(weatherReport.getCompleteLocation());
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
        } else {
            handleWeatherReportFailure();
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void findViews() {
        layoutMainWeatherContainer = (RelativeLayout) findViewById(R.id.home_main_weather_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtCurrentTemperature = (TextView) findViewById(R.id.home_current_weather_temperature);
        txtCurrentLocation = (TextView) findViewById(R.id.home_current_location);
        txtAdvanceInfoHint = (TextView) findViewById(R.id.home_advance_info_hint);
        txtNoReport = (TextView) findViewById(R.id.home_txt_no_results);
        txtWaitFirstReport = (TextView) findViewById(R.id.home_txt_wait_first_time);
        imgCurrentWeatherIcon = (ImageView) findViewById(R.id.home_current_weather_icon);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.home_coordinator_layout);
        viewBasicWeatherInformation = (WeatherInformationView) findViewById(R.id.home_basic_weather_info);
        viewAdvanceWeatherInformation = (WeatherInformationView) findViewById(R.id.home_advance_weather_info);
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
        menuSearch = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuSearch);
        searchView.setOnQueryTextListener(this);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        getCurrentWeather(query);
        searchView.setQuery(Constants.EMPTY_STRING, false);
        searchView.setIconified(true);
        hideKeyboard();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) WeathyApplication.getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), NO_FLAGS);
        }
    }
}
