package com.example.gucio.astroweather.Dataa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item implements JSONPopulator{

    private  Condition condition;
    private Forecast[] forecast = new Forecast[6];
    public static float latitute;
    public static float longitute;

    public Forecast getForecast(int dayOfWeek) {
        return forecast[dayOfWeek];
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        latitute = data.optInt("lat");
        longitute = data.optInt("long");


        JSONArray jsonArray = data.optJSONArray("forecast");
        for (int i = 0; i < 6; i++) {
            try {
                forecast[i] = new Forecast();
                forecast[i].populate(jsonArray.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
