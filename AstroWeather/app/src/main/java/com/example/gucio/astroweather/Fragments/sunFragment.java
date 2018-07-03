package com.example.gucio.astroweather.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gucio.astroweather.Data;
import com.example.gucio.astroweather.Dataa.Item;
import com.example.gucio.astroweather.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class sunFragment extends Fragment {

    View view;
    Thread t;
    final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.GERMANY);

    private TextView dataView,cordView;

    public sunFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sun, container, false);

        dataView = view.findViewById(R.id.sunText);
        cordView = view.findViewById(R.id.Coordinates);

        setTexts();

        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000 * Data.getRefreshTime());
                        if (isAdded()) {
                            getActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                setTexts();
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
    private void setTexts(){
        dataView.setText("Moonrise: " +Data.getAstroCalculator().getMoonInfo().getMoonrise().toString() +
                "\nMoonset: " + Data.getAstroCalculator().getMoonInfo().getMoonset().toString()  +
                "\nIlumination: "+ Data.getAstroCalculator().getMoonInfo().getIllumination()+
                "\nFullMoon: "+ Data.getAstroCalculator().getMoonInfo().getNextFullMoon()+
                "\nNextNewMoon: " + Data.getAstroCalculator().getMoonInfo().getNextNewMoon().toString());
        cordView.setText("Latitude:" + Data.latitude +"  Longitude:" + Data.longitude);

    }

    @Override
    public void onPause(){
        t.interrupt();
        super.onPause();
    }

}