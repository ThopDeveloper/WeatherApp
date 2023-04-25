package com.example.weather.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Constants {

    public static String APP_ID = "7264ff0a4ee34edc27d1c00bc948afec";


    public static String currentDate(){

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd, MMM yyyy", Locale.getDefault());
        String formattedDate = df.format(c);

        return formattedDate;
    }

    public static String getIcon(String icon){

        String url = "https://openweathermap.org/img/wn/"+icon+"@2x.png";

        return url;
    }

    public static String getTime(Long milliSec){

        long minute = (milliSec / (1000 * 60)) % 60;
        long hour = (milliSec / (1000 * 60 * 60)) % 24;

        return hour+":"+minute;

    }
}
