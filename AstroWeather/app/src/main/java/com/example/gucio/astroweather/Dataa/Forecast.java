package com.example.gucio.astroweather.Dataa;

import org.json.JSONObject;

public class Forecast implements JSONPopulator {

    private int code;
    private int temperatureHigh;
    private int temperatureLow;
    private String description;
    private String day;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(int temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public int getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(int temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public void populate(JSONObject data) {

        code = data.optInt("code");
        temperatureHigh = data.optInt("high");
        temperatureLow = data.optInt("low");
        description = data.optString("text");
        day = data.optString("day");

    }
}
