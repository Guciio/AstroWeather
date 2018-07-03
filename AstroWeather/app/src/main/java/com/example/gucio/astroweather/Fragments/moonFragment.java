package com.example.gucio.astroweather.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gucio.astroweather.Data;
import com.example.gucio.astroweather.Dataa.Item;
import com.example.gucio.astroweather.R;

public class moonFragment extends Fragment {

    View view;
    private TextView dataView,cordView;
    Thread t;

    public moonFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_moon, container, false);

        dataView = view.findViewById(R.id.moonText);
        cordView = view.findViewById(R.id.Coordinates2);

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
