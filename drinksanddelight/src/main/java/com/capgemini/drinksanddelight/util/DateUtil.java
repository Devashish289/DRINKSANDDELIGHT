package com.capgemini.drinksanddelight.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String DATE_PATTERN ="yyyy/MM/dd";
    private static DateTimeFormatter format = DateTimeFormatter.ofPattern(DATE_PATTERN);


    public static LocalDate toLocalDate(String text) {
        LocalDate date = LocalDate.parse(text, format);
        return date;
    }

    public static LocalDate toLocalDate(String pattern, String text) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern);
        LocalDate date = LocalDate.parse(text, format);
        return date;
    }

    public static String toString(LocalDate date){
       String text=  date.format(format);
       return text;
    }
}
