package com.airtel.assistant.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {

    private static final String FORMAT = "yyyy-MM-dd";

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(date);
    }

    public static Date parseDate(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.parse(dateStr);
    }
}