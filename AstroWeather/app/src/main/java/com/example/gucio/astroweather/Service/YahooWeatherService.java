package com.example.gucio.astroweather.Service;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.gucio.astroweather.Data;
import com.example.gucio.astroweather.Dataa.Channel;
import com.example.gucio.astroweather.Dataa.Item;
import com.example.gucio.astroweather.LocalDataBase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class YahooWeatherService {
    private WeatherServiceCallback listener;
    private Exception error;
    private SharedPreferences sharedPreferences;
    private int option;
    private String temperatureUnit = "C";
    private String endpoint,YQLC,YQLF,YQL,YQL2;
    private Spinner spinnerLocalizationn;

    public YahooWeatherService(WeatherServiceCallback listener, int option) {
        this.listener = listener;
       // this.sharedPreferences = sharedPref;
        this.option = option;
    }


    private String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        this.temperatureUnit = temperatureUnit;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshWeather(String location) {

        new AsyncTask<String, Void, Channel>() {
            @Override
            protected Channel doInBackground(String[] locations) {

                String location = locations[0];

                Channel channel = new Channel();


                if(option == 1){
                    YQLC = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='C'" , location);
                    YQLF = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='F'" , location);

                    if(LocalDataBase.unitChoose.equals("F")){
                        endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQLF));
                    }else if (LocalDataBase.unitChoose.equals("C")){
                        endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQLC));
                    }

                }else{

                    String searchLatLong = Math.round(Data.latitude)+","+ Math.round(Data.longitude);
                    System.out.println(searchLatLong);
                    YQL = String.format("select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(%s)\") and u='C'", searchLatLong);
                    YQL2 = String.format("select * from weather.forecast where woeid in (SELECT woeid FROM geo.places WHERE text=\"(%s)\") and u='F'", searchLatLong);

                    if(LocalDataBase.unitChoose.equals("F")){
                        endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL2));
                    }else if (LocalDataBase.unitChoose.equals("C")){
                        endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));
                    }
                }



                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();
                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    reader.close();

                    JSONObject data = new JSONObject(result.toString());

                    JSONObject queryResults = data.optJSONObject("query");

                    int count = queryResults.optInt("count");

                    if (count == 0) {
                        error = new LocationWeatherException("No weather information found for " + location);
                        return null;
                    }

                    JSONObject channelJSON = queryResults.optJSONObject("results").optJSONObject("channel");
                    channel.populate(channelJSON);

                    return channel;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Channel channel) {

                if (channel == null && error != null) {
                    listener.serviceFailure(error);
                } else {
                    listener.serviceSuccess(channel);
                }

            }

        }.execute(location);
    }

    private class LocationWeatherException extends Exception {
        LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}