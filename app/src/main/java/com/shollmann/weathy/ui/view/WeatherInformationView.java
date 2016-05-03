package com.shollmann.weathy.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shollmann.weathy.R;
import com.shollmann.weathy.api.model.MainInformation;
import com.shollmann.weathy.api.model.Wind;
import com.shollmann.weathy.helper.Constants;
import com.shollmann.weathy.helper.ResourcesHelper;

public class WeatherInformationView extends LinearLayout {
    private static final int NO_ELEVATION = 0;

    private TextView txtFirstTitle;
    private TextView txtFirstValue;
    private TextView txtSecondTitle;
    private TextView txtSecondValue;
    private TextView txtThirdTitle;
    private TextView txtThirdValue;
    private CardView cardviewContainer;

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

        cardviewContainer = (CardView) findViewById(R.id.weather_info_cardview);
    }

    public void setWeatherInfo(MainInformation mainInformation) {
        txtFirstTitle.setText(ResourcesHelper.getString(R.string.min));
        txtFirstValue.setText(String.valueOf(mainInformation.getIntTemperatureMin()) + Constants.SpecialChars.CELSIUS_DEGREES);

        txtSecondTitle.setText(ResourcesHelper.getString(R.string.max));
        txtSecondValue.setText(String.valueOf(mainInformation.getIntTemperatureMax()) + Constants.SpecialChars.CELSIUS_DEGREES);

        txtThirdTitle.setText(ResourcesHelper.getString(R.string.humidity));
        txtThirdValue.setText(String.valueOf(mainInformation.getIntHumidity()) + Constants.SpecialChars.PERCENT);
    }

    public void setWeatherInfo(MainInformation mainInformation, Wind windInformation) {
        txtFirstTitle.setText(ResourcesHelper.getString(R.string.wind_speed));
        txtFirstValue.setText(String.valueOf(windInformation.getIntSpeed()) + Constants.SpecialChars.KM_PER_HOUR);

        txtSecondTitle.setText(ResourcesHelper.getString(R.string.wind_direction));
        txtSecondValue.setText(String.valueOf(windInformation.getIntDegrees()) + Constants.SpecialChars.DEGREE);

        txtThirdTitle.setText(ResourcesHelper.getString(R.string.pressure));
        txtThirdValue.setText(String.valueOf(mainInformation.getIntPressure()) + Constants.SpecialChars.HECTOPASCALS);

        cardviewContainer.setCardElevation(NO_ELEVATION);
    }

}
