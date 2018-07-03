package com.example.gucio.astroweather.Dataa;

import org.json.JSONObject;

public class Wind implements JSONPopulator {

    private String chill;
    private String direction;
    private double speed;

    public String getChill() {
        return chill;
    }

    public void setChill(String chill) {
        this.chill = chill;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public void populate(JSONObject data) {

        chill = data.optString("chill");
        direction = data.optString("direction");
        speed = data.optDouble("speed");

    }
}
