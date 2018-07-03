package com.example.gucio.astroweather;

import java.util.ArrayList;
import java.util.List;

public class LocalDataBase {

    private static String cityName, currentCityName = "Lodz, PL";
    public static String unitChoose="C";
    public static String getCurrentCityName() {
        return currentCityName;
    }

    public static void setCurrentCityName(String currentCityName) {
        LocalDataBase.currentCityName = currentCityName;
    }

    static List<String> favoriteLocalizationList = new ArrayList<String>();

    public static List<String> getFavoriteLocalizationList() {
        return favoriteLocalizationList;
    }

    public void setFavoriteLocalizationList(List<String> favoriteLocalizationList) {
        this.favoriteLocalizationList = favoriteLocalizationList;
    }


    public static String getCityName() {
        return cityName;
    }

    public static void setCityName(String cityName) {
        LocalDataBase.cityName = cityName;
    }
}
