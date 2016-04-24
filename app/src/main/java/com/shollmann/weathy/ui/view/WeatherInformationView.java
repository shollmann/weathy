package com.shollmann.weathy.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shollmann.weathy.R;
import com.shollmann.weathy.api.model.MainInformation;
import com.shollmann.weathy.helper.ResourcesHelper;

public class WeatherInformationView extends LinearLayout {
    private TextView txtFirstTitle;
    private TextView txtFirstValue;
    private TextView txtSecondTitle;
    private TextView txtSecondValue;
    private TextView txtThirdTitle;
    private TextView txtThirdValue;

    public WeatherInformationView(Context context) {
        super(context);
        initialize();
    }

    public WeatherInformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public WeatherInformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        inflate(getContext(), R.layout.view_weather_info, this);

        txtFirstTitle = (TextView) findViewById(R.id.weather_info_first_title);
        txtFirstValue = (TextView) findViewById(R.id.weather_info_first_value);

        txtSecondTitle = (TextView) findViewById(R.id.weather_info_second_title);
        txtSecondValue = (TextView) findViewById(R.id.weather_info_second_value);

        txtThirdTitle = (TextView) findViewById(R.id.weather_info_third_title);
        txtThirdValue = (TextView) findViewById(R.id.weather_info_third_value);
    }

    public void setWeatherInfo(MainInformation mainInformation) {
        txtFirstTitle.setText(ResourcesHelper.getString(R.string.min));
        txtFirstValue.setText(String.valueOf(mainInformation.getIntTemperatureMin()) + "C"); //TODO remove string hardcoding

        txtSecondTitle.setText(ResourcesHelper.getString(R.string.max));
        txtSecondValue.setText(String.valueOf(mainInformation.getIntTemperatureMax()) + "C"); //TODO remove string hardcoding

        txtThirdTitle.setText(ResourcesHelper.getString(R.string.humidity));
        txtThirdValue.setText(String.valueOf(mainInformation.getHumidity()) + "%"); //TODO remove string hardcoding
    }

}
