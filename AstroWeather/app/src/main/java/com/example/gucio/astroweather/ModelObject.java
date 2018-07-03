package com.example.gucio.astroweather;

/**
 * Created by 202160 on 5/11/2018.
 */

public enum ModelObject {

    MOON(R.string.moon,R.layout.moon_layout),
    SUN(R.string.sun,R.layout.sun_layout),
    WEATHER(R.string.weather,R.layout.weather_layout);

    public int mTitleResId;
    public int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        this.mTitleResId = titleResId;
        this.mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
