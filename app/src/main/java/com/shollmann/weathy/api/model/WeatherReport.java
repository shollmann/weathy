
package com.shollmann.weathy.api.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherReport implements Serializable {
    private Coordinates coord;
    private ArrayList<Weather> weather;
    private String base;
    private MainInformation main;
    private int visibility;
    private Wind wind;
    private Clouds clouds;
    private int dt;
    private Sys sys;
    private int id;
    private String name;
    private int cod;


    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }

    public Weather getWeather() {
        if (weather != null && !weather.isEmpty()) {
            return weather.get(0);
        }
        return null;
    }

    public ArrayList<Weather> getWeatherList() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public MainInformation getMain() {
        return main;
    }

    public void setMain(MainInformation main) {
        this.main = main;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public int getDt() {
        return dt;
    }

    public void setDt(int dt) {
        this.dt = dt;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getCompleteLocation() {
        if (sys != null && !TextUtils.isEmpty(sys.getCountry())) {
            return String.format("%1$s, %2$s", name, sys.getCountry());
        } else {
            return name;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

}
