package com.prowo.persist.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WebUtils {

	public static String getNDay(String var, int lday) {
		int strTo = lday;
		String tmp = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = new GregorianCalendar();
			Date date = sdf.parse(var);
			calendar.setTime(date);
			calendar.add(5, strTo);
			int yy = calendar.get(1);
			int mm = calendar.get(2) + 1;
			String mmStr = mm > 10 ? String.valueOf(mm) : "0" + String.valueOf(mm);
			int dd = calendar.get(5);
			String ddStr = dd > 10 ? String.valueOf(dd) : "0" + String.valueOf(dd);
			tmp = (new StringBuilder(String.valueOf(yy))).append("-").append(mmStr).append("-").append(ddStr).toString();
		} catch (Exception exception) {
		}
		return tmp;
	}

	public static String DateFormat(String var) {
		String tmp = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Calendar calendar = new GregorianCalendar();
			Date date = sdf.parse(var);
			calendar.setTime(date);
			int yy = calendar.get(1);
			int mm = calendar.get(2) + 1;
			String mmStr = mm > 10 ? String.valueOf(mm) : "0" + String.valueOf(mm);
			int dd = calendar.get(5);
			String ddStr = dd > 10 ? String.valueOf(dd) : "0" + String.valueOf(dd);
			tmp = (new StringBuilder(String.valueOf(yy))).append("-").append(mmStr).append("-").append(ddStr).toString();
		} catch (Exception exception) {
		}
		return tmp;
	}
}
