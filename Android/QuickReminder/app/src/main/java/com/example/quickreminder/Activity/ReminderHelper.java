package com.example.quickreminder.Activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Dell on 8/27/2016.
 */
public class ReminderHelper {

    public static String getFromattedDate (long milisecond) {
        String formattedDate = "";
        String dateFormat = "dd-MM-yyyy hh:mm a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milisecond);
        formattedDate = simpleDateFormat.format(calendar.getTime());
        return formattedDate;
    }

    public static String getRandomString () {
        String randomString = "";
        randomString = UUID.randomUUID().toString();
        return randomString;
    }
}
