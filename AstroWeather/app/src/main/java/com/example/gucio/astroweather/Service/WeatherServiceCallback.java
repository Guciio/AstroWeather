package com.example.gucio.astroweather.Service;

import com.example.gucio.astroweather.Dataa.Channel;


public interface WeatherServiceCallback {
    void serviceSuccess(Channel channel);
    void serviceFailure(Exception exception);
}
