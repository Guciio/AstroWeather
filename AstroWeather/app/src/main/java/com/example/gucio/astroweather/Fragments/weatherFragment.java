package com.example.gucio.astroweather.Fragments;


import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gucio.astroweather.LocalDataBase;
import com.example.gucio.astroweather.Data;
import com.example.gucio.astroweather.Dataa.Atmosphere;
import com.example.gucio.astroweather.Dataa.Channel;
import com.example.gucio.astroweather.Dataa.Item;
import com.example.gucio.astroweather.Dataa.Wind;
import com.example.gucio.astroweather.R;
import com.example.gucio.astroweather.Service.WeatherServiceCallback;
import com.example.gucio.astroweather.Service.YahooWeatherService;
import com.example.gucio.astroweather.Settings;


public class weatherFragment extends Fragment implements WeatherServiceCallback {


    private TextView temperatureView,humibityView, presureView, windSpeedView, windDirectionView, cloudsView;
    private YahooWeatherService service;
    private static final int WEEK_DAY = 5;
    private TextView[] weatherTextView = new TextView[WEEK_DAY];
    private TextView[] temperatureHighTextView = new TextView[WEEK_DAY];
    private TextView[] temperatureLowTextView = new TextView[WEEK_DAY];
    private SharedPreferences sharedPreferencesWithCustomValues;
    private SharedPreferences offlineDataSharedPreferences;
    Thread t;
    private int option = Settings.mode;


    public weatherFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initialize();

        temperatureView = view.findViewById(R.id.temperatureValue);
        humibityView = view.findViewById(R.id.humidityValue);
        presureView = view.findViewById(R.id.preasureValue);
        windSpeedView = view.findViewById(R.id.windSpeedValue);
        windDirectionView = view.findViewById(R.id.windDirectionValue);
        cloudsView = view.findViewById(R.id.cloudsValue);

        for (int i = 0; i < WEEK_DAY; i++) {
            weatherTextView[i] = view.findViewById(getResources().getIdentifier("weatherText" + (i + 1), "id", getContext().getPackageName()));
            temperatureHighTextView[i] = view.findViewById(getResources().getIdentifier("temperatureHighText" + (i + 1), "id", getContext().getPackageName()));
            temperatureLowTextView[i] = view.findViewById(getResources().getIdentifier("temperatureLowText" + (i + 1), "id", getContext().getPackageName()));
        }


        createWeatherInfo();
        showOfflineData();

        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60*1000 * Data.getRefreshTime());
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                createWeatherInfo();
                                                                showOfflineData();
                                                                Toast.makeText(getActivity(), "Update Data: ", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                            );
                        }
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();


        return view;
    }

    @SuppressLint("SetTextI18n")
    public void createWeatherInfo() {

        service = new YahooWeatherService(this, option);
        Toast.makeText(getActivity(), "Loading... ", Toast.LENGTH_SHORT).show();
        service.refreshWeather(LocalDataBase.getCurrentCityName());

    }

    @Override
    public void onResume() {
        super.onResume();
        createWeatherInfo();
    }

    @Override
    public void onPause() {
        t.interrupt();
        super.onPause();
    }

    private void initialize() {

        offlineDataSharedPreferences = getActivity().getSharedPreferences("offline_data.xml", 0);
    }

    private void showOfflineData() {
        Toast.makeText(getActivity(), "Offline localization: " + String.valueOf(offlineDataSharedPreferences.getString("cityOffline", "Lodz"))
                + ", " + String.valueOf(offlineDataSharedPreferences.getString("countryOffline", "PL")) , Toast.LENGTH_SHORT).show();

        temperatureView.setText(String.valueOf(offlineDataSharedPreferences.getInt("temperatureOffline", 0)));
        humibityView.setText(offlineDataSharedPreferences.getString("humidityOffline", "humidity"));
        presureView.setText(offlineDataSharedPreferences.getString("pressureOffline", "pressure"));
        windSpeedView.setText(offlineDataSharedPreferences.getString("windSpeedOffline", "windSpeed"));
        windDirectionView.setText(offlineDataSharedPreferences.getString("windDirectionOffline", "windDirection"));
        cloudsView.setText(offlineDataSharedPreferences.getString("visibilityOffline", "visibility"));
        for (int i = 0; i < WEEK_DAY; i++) {
            weatherTextView[i].setText(offlineDataSharedPreferences.getString("dayOffline"+ String.valueOf(i), "day"));
            temperatureHighTextView[i].setText(String.valueOf(offlineDataSharedPreferences.getInt("temperatureHighOffline"+ String.valueOf(i), 0)));
            temperatureLowTextView[i].setText(String.valueOf(offlineDataSharedPreferences.getInt("temperatureLowOffline"+ String.valueOf(i), 0)));

        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void serviceSuccess(Channel channel) {

        Item item = channel.getItem();
        Atmosphere atmosphere = channel.getAtmosphere();
        Wind wind = channel.getWind();
        Toast.makeText(getActivity(), "Connected", Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "Localization: " + LocalDataBase.getCurrentCityName(), Toast.LENGTH_SHORT).show();


        temperatureView.setText(item.getCondition().getTemperature() + "\u00B0"+ channel.getUnits().getTemperature() );
        humibityView.setText(atmosphere.getHumidity());
        presureView.setText(atmosphere.getPressure());
        windSpeedView.setText(String.valueOf(wind.getSpeed()));
        windDirectionView.setText(wind.getDirection());
        cloudsView.setText(atmosphere.getVisibility());
        for (int i = 0; i < WEEK_DAY; i++) {
                weatherTextView[i].setText(String.valueOf(item.getForecast(i).getDay()));
                temperatureHighTextView[i].setText(String.valueOf(item.getForecast(i).getTemperatureHigh()));
                temperatureLowTextView[i].setText(String.valueOf(item.getForecast(i).getTemperatureLow()));

        }

    }

    @Override
    public void serviceFailure(Exception exception) {
        showOfflineData();
        Toast.makeText(getContext(), "no internet connection.\n", Toast.LENGTH_LONG).show();

    }
}

