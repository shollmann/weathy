
package com.shollmann.weathy.api.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MainInformation implements Serializable {

    private Double temp;
    private int pressure;
    private int humidity;
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

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
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
