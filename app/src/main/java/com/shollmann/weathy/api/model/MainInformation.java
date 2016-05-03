
package com.shollmann.weathy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainInformation implements Serializable {

    private Double temp;
    private Double pressure;
    private Double humidity;
    @SerializedName("temp_min")
    private Double tempMin;
    @SerializedName("temp_max")
    private Double tempMax;


    public Double getTemp() {
        return temp;
    }

    public int getIntTemperature() {
        return temp.intValue();
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getPressure() {
        return pressure;
    }

    public int getIntPressure() {
        return pressure.intValue();
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Double getHumidity() {
        return humidity;
    }

    public int getIntHumidity() {
        return humidity.intValue();
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public int getIntTemperatureMin() {
        return tempMin.intValue();
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public int getIntTemperatureMax() {
        return tempMax.intValue();
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

}
