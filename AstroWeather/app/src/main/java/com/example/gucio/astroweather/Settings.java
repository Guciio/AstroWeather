package com.example.gucio.astroweather;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;


import com.example.gucio.*;
import com.example.gucio.astroweather.Dataa.Item;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;


public class Settings extends AppCompatActivity {

    private TextClock textClock1;
    private Button saveButton1, addLocalizationButton, delLocalizationButton;
    private TextView latitudeText, longitudeText;
    private EditText latitudeEdit, longitudeEdit, cityNameEdit,countryNameEdit;
    private Spinner spinner,spinnerLocalization,unitSpiner;
    public static int mode=1;
    Integer[] item = new Integer[]{5,15,30,45,60,120};
    private Set<String> localizationSpinnerValues = new HashSet<>();
    private Set<String> unitSpineValues = new HashSet<>();
    private OnlineDataBase onlineDataBase;
    private Cursor data;
    float latitude, longitude;
    private Set<String> modeSpinerValue = new HashSet<>();
    private Spinner modeSpiner;
    //LocalDataBase LocalDataBase = new LocalDataBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        onlineDataBase = new OnlineDataBase(this);

        textClock1 = findViewById(R.id.textClock);
        
        textClock1.is24HourModeEnabled();
        textClock1.setFormat24Hour("k:mm:ss");
        initialize();

        saveButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saving();
            }
        });
        addLocalizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addLocalization();
            }
        });
        delLocalizationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delLocalization();
            }
        });
    }

    public void initialize() {

        latitudeText = findViewById(R.id.textViewLatitude);
        longitudeText = findViewById(R.id.textViewLongitude);

        latitudeEdit = findViewById(R.id.editTextLatitude);
        latitudeEdit.setText(String.valueOf(Data.latitude));

        longitudeEdit = findViewById(R.id.editTextLongitude);
        longitudeEdit.setText(String.valueOf(Data.longitude));
        cityNameEdit = findViewById(R.id.cityName);
        countryNameEdit = findViewById(R.id.countryName);
        saveButton1 = findViewById(R.id.buttonSave);
        addLocalizationButton = findViewById(R.id.addLocalizationButton);
        delLocalizationButton = findViewById(R.id.deleteLocalization);

        spinner = (Spinner)findViewById(R.id.spinnerTime);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, item);
        spinner.setAdapter(adapter);

        setSpinnerCity();
        setSpinnerUnit();
        setSpinnerMode();

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(longitudeEdit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void setSpinnerUnit(){

        unitSpiner = findViewById(R.id.spinerUnits);

        unitSpineValues.add("C");
        unitSpineValues.add("F");

        ArrayAdapter<String> unitSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, unitSpineValues.toArray(new String[unitSpineValues.size()]) );
        unitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unitSpiner.setAdapter(unitSpinnerAdapter);

    }

    public void setSpinnerMode(){

        modeSpiner = findViewById(R.id.spinerMode);

        modeSpinerValue.add("City");
        modeSpinerValue.add("Cords");

        ArrayAdapter<String> modeSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, modeSpinerValue.toArray(new String[modeSpinerValue.size()]) );
        modeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        modeSpiner.setAdapter(modeSpinnerAdapter);

    }

    public void setSpinnerCity(){

        spinnerLocalization = findViewById(R.id.favouriteLocalizationSpinner);
        data = onlineDataBase.getListContents();


        while(data.moveToNext()) {
            localizationSpinnerValues.add(data.getString(1));
        }

        ArrayAdapter<String> localizationSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, localizationSpinnerValues.toArray(new String[localizationSpinnerValues.size()]) );
        localizationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLocalization.setAdapter(localizationSpinnerAdapter);

    }

    public void saving() {

        try {

            String[] splitStr = spinnerLocalization.getSelectedItem().toString().split("\\s+");

            float val = Float.parseFloat(splitStr[0]);
            float val2 = Float.parseFloat(splitStr[1]);

            latitude = Float.valueOf(splitStr[0]);
            longitude = Float.valueOf(splitStr[1]);

        } catch (NumberFormatException e) {
            latitude = Item.latitute;
            longitude = Item.longitute;
            LocalDataBase.setCurrentCityName((String) spinnerLocalization.getSelectedItem());
        }

        if(modeSpiner.getSelectedItem().toString().matches("City")){
            mode =1;
        }else{
            mode =2;
        }

        Data.setLatitude(latitude);
        Data.setLongitude(longitude);

        Integer timeValue = Integer.valueOf(spinner.getSelectedItem().toString());
        Data.setRefreshTime(timeValue);

        LocalDataBase.unitChoose = unitSpiner.getSelectedItem().toString();

        setSpinnerCity();
        setSpinnerUnit();

        Intent i = new Intent(this, StartScreen.class);
        i.setFlags(i.FLAG_ACTIVITY_NEW_TASK | i.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i); // Launch the HomescreenActivity
        finish();
    }

    public void addLocalization() {
        String city;

        city = String.valueOf(cityNameEdit.getText().toString().substring(0,1).toUpperCase() +cityNameEdit.getText().toString().substring(1) +" "+countryNameEdit.getText().toString());

        if(!countryNameEdit.getText().toString().matches("")){
            city = String.valueOf(cityNameEdit.getText().toString().substring(0,1).toUpperCase()+cityNameEdit.getText().toString().substring(1).toUpperCase() +" "+countryNameEdit.getText().toString().substring(0,1).toUpperCase()+countryNameEdit.getText().toString().substring(1));

        }

        latitude = Float.valueOf(latitudeEdit.getText().toString());
        longitude = Float.valueOf(longitudeEdit.getText().toString());

        if(cityNameEdit.getText().toString().matches("") && countryNameEdit.getText().toString().matches(""))
        {
            city = String.valueOf(latitude)+ " " + String.valueOf(longitude);
            Item.latitute = latitude;
            Item.longitute = longitude;
        }else{
            Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show();
        }

        onlineDataBase.addData(city);
        setSpinnerCity();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public void delLocalization() {
        String city;
        city = String.valueOf(cityNameEdit.getText().toString().substring(0,1).toUpperCase()+cityNameEdit.getText().toString().substring(1) +" "+countryNameEdit.getText().toString());

        latitude = Float.valueOf(latitudeEdit.getText().toString());
        longitude = Float.valueOf(longitudeEdit.getText().toString());

        if(!countryNameEdit.getText().toString().matches("")){
            city = String.valueOf(cityNameEdit.getText().toString().substring(0,1).toUpperCase()+cityNameEdit.getText().toString().substring(1).toUpperCase() +" "+countryNameEdit.getText().toString().substring(0,1).toUpperCase()+countryNameEdit.getText().toString().substring(1));

        }

        if(cityNameEdit.getText().toString().matches("") && countryNameEdit.getText().toString().matches(""))
        {
            city = String.valueOf(latitude)+ " " + String.valueOf(longitude);
        }
        onlineDataBase.deleteData(city);
        setSpinnerCity();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}