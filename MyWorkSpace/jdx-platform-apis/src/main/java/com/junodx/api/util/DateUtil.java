package com.junodx.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";

  public static Date convertStringToDate(String value, String dateFormat) {
    if (value.isBlank()) {
      return null;
    }
    Date date = null;
    dateFormat = dateFormat.isBlank() ? DATE_FORMAT_YYYY_MM_DD : dateFormat;
    try {
      date = new SimpleDateFormat(dateFormat).parse(value);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return date;
  }

  public static Calendar convertStringToCalendar(String value, String dateFormat) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(convertStringToDate(value, dateFormat));
    return calendar;
  }

  public static Calendar nowCalendar() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(new Date(System.currentTimeMillis()));
    return calendar;
  }

  public static Date nowUtilDate() {
    return new Date(System.currentTimeMillis());
  }

  public static String convertDateToString(Calendar date, String dateFormat) {
    if (date == null)
      return null;

    String convertedDate = null;
    try {
      convertedDate = new SimpleDateFormat(dateFormat).format(date);
    } catch (Exception e) {
      return null;
    }
    return convertedDate;
  }
}
