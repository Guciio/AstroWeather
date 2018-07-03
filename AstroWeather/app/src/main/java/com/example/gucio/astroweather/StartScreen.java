package com.example.gucio.astroweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gucio.astroweather.Dataa.Channel;
import com.example.gucio.astroweather.Service.WeatherServiceCallback;
import com.example.gucio.astroweather.Service.YahooWeatherService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StartScreen extends AppCompatActivity implements WeatherServiceCallback {

    TextView moonText,sunText;
    private Thread myThread;
    private YahooWeatherService yahooWeatherService;
    private SharedPreferences.Editor offlineSharedPreferences;
    private int option = Settings.mode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter( new MyPagerAdapter(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initOfflineData();
        initYahooWeatherService();

        //if(this.getResources().getConfiguration().screenHeightDp >= 600 ){
        //setContentView(R.layout.pageviewre_activity_land);
        //}

        final TextView timer = findViewById(R.id.timerLabel);
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);

        myThread = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted())
                {
                    try
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Date currentTime = Calendar.getInstance().getTime();
                                timer.setText(sdf.format(currentTime));
                            }
                        });

                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };

        myThread.start();


    }

    private void initOfflineData() {
        SharedPreferences offlineDataSharedPreferences = getSharedPreferences("offline_data.xml", 0);
        offlineSharedPreferences = offlineDataSharedPreferences.edit();


    }

    private void downloadOfflineWeatherData(Channel channel) {

        offlineSharedPreferences.putString("windSpeedOffline", String.valueOf(channel.getWind().getSpeed()));
        offlineSharedPreferences.putString("windDirectionOffline", String.valueOf(channel.getWind().getDirection()));
        offlineSharedPreferences.putString("humidityOffline", String.valueOf(channel.getAtmosphere().getHumidity()));
        offlineSharedPreferences.putString("visibilityOffline", String.valueOf(channel.getAtmosphere().getVisibility()));
        offlineSharedPreferences.putString("pressureOffline", String.valueOf(channel.getAtmosphere().getPressure()));
        offlineSharedPreferences.putInt("temperatureOffline", channel.getItem().getCondition().getTemperature());
        offlineSharedPreferences.putString("cityOffline", channel.getLocation().getCity());
        offlineSharedPreferences.putString("countryOffline", channel.getLocation().getCountry());
        for (int i = 0; i < 5; i++) {

            offlineSharedPreferences.putInt("temperatureHighOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getTemperatureHigh());
            offlineSharedPreferences.putInt("temperatureLowOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getTemperatureLow());
            offlineSharedPreferences.putString("dayOffline" + String.valueOf(i), channel.getItem().getForecast(i + 1).getDay());
            offlineSharedPreferences.commit();
        }
        offlineSharedPreferences.commit();
    }

    @Override
    public void serviceSuccess(Channel channel) {

        try {
            downloadOfflineWeatherData(channel);
        } catch (IllegalStateException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initYahooWeatherService() {

        yahooWeatherService = new YahooWeatherService(this , option );
        yahooWeatherService.refreshWeather("Lodz,PL");
    }

    @Override
    public void serviceFailure(Exception exception) {
        Toast.makeText(getApplicationContext(), "There is a problem with getting data from Yahoo Weather API! Try again later.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Settings.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onPause(){
        myThread.interrupt();
        super.onPause();
    }
}
