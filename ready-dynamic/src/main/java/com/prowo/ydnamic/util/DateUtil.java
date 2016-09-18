package com.prowo.ydnamic.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    // private final static Map<String, DateFormat> formatters = new
    // HashMap<String, DateFormat>();

    public enum DATE_FORMAT {
        yyyy_MM_dd_HHmmss("yyyy-MM-dd HH:mm:ss"),
        yy_M_dd_HHmmss("yy-M-dd HH:mm:ss"),
        yyyyMMdd("yyyyMMdd"),
        yyyyMMddHHmmss("yyyyMMddHHmmss");

        private DATE_FORMAT(String format) {
            this.format = format;
        }

        String format;

        String getFormat() {
            return this.format;
        }
    }

    /*
     * static { formatters.put(DATE_FORMAT.yyyy_MM_dd_HHmmss.name(), new
     * SimpleDateFormat(DATE_FORMAT.yyyy_MM_dd_HHmmss.getFormat()));
     * formatters.put(DATE_FORMAT.yy_M_dd_HHmmss.name(), new
     * SimpleDateFormat(DATE_FORMAT.yy_M_dd_HHmmss.getFormat()));
     * formatters.put(DATE_FORMAT.yyyyMMdd.name(), new
     * SimpleDateFormat(DATE_FORMAT.yyyyMMdd.getFormat()));
     * 
     * }
     */

    public static String format(DATE_FORMAT format, Date date) {
        return new SimpleDateFormat(format.getFormat()).format(date);
    }

    public static Date parse(DATE_FORMAT format, String str) throws ParseException {
        return new SimpleDateFormat(format.getFormat()).parse(str);
    }

    public static DateFormat getDateFormat(DATE_FORMAT format) {
        return new SimpleDateFormat(format.getFormat());
    }

    public static Date after(int field, int amount) {
        Date today = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date before(int field, int amount) {
        Date today = new Date();// 取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(field, -amount);
        return calendar.getTime();
    }

    public static Date before(int field, int amount, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field, -amount);
        return calendar.getTime();
    }

    public static Date after(int field, int amount, Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(field, amount);
        return calendar.getTime();
    }
}
