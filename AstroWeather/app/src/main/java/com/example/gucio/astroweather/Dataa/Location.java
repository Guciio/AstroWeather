package com.example.gucio.astroweather.Dataa;

import org.json.JSONObject;

public class Location implements JSONPopulator {

    private String city;
    private String region;
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public void populate(JSONObject data) {

        city = data.optString("city");
        region = data.optString("region");
        country = data.optString("country");

    }
}
