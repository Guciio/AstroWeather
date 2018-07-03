package com.example.gucio.astroweather;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Data {

    public static Double longitude=0.0,latitude=0.0;

    public static Integer getRefreshTime() {
        return refreshTime;
    }

    public static void setRefreshTime(Integer refreshTime) {
        Data.refreshTime = refreshTime;
    }

    static Integer refreshTime=30;

    private static Calendar calendar = Calendar.getInstance();

    private static AstroDateTime astroDateTime = new AstroDateTime(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            Calendar.HOUR,
            Calendar.MINUTE,
            Calendar.SECOND,
            Calendar.ZONE_OFFSET/360000,
            true
    );

    private static AstroCalculator.Location location = new AstroCalculator.Location(latitude, longitude);
    private static AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, location);

    private Data() {    }

    public static AstroDateTime getAstroDateTime() {
        return astroDateTime;
    }

    public static void setAstroDateTime(AstroDateTime astroDateTime) {
        Data.astroDateTime = astroDateTime;
    }

    public static AstroCalculator.Location getLocation() {
        return location;
    }

    public static AstroCalculator getAstroCalculator() {
        return astroCalculator;
    }

    public static void setAstroCalculator(AstroCalculator astroCalculator) {
        Data.astroCalculator = astroCalculator;
    }

    public static void setLatitude(double latitude) {
        Data.latitude = latitude;
    }

    public static void setLongitude(double longitude) {
        Data.longitude = longitude;
    }
}
