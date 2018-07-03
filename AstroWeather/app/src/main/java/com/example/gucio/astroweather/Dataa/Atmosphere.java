package com.example.gucio.astroweather.Dataa;

import org.json.JSONObject;

public class Atmosphere implements JSONPopulator {

    private String humidity;
    private String pressure;
    private String visibility;

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @Override
    public void populate(JSONObject data) {

        humidity = data.optString("humidity");
        pressure = data.optString("pressure");
        visibility = data.optString("visibility");

    }
}
