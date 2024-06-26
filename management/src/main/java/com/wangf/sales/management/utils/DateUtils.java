package com.wangf.sales.management.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	public static Date getFirstDayOfCurrentMonth() {
		Calendar firstDayOfCurrentMonth = Calendar.getInstance();
		setToFirstDayOfMonth(firstDayOfCurrentMonth);
		Date result = firstDayOfCurrentMonth.getTime();
		return result;
	}

	private static void setToFirstDayOfMonth(Calendar firstDayMonth) {
		firstDayMonth.set(Calendar.DAY_OF_MONTH, 1);
		firstDayMonth.set(Calendar.HOUR_OF_DAY, 0);
		firstDayMonth.set(Calendar.MINUTE, 0);
		firstDayMonth.set(Calendar.SECOND, 0);
		firstDayMonth.set(Calendar.MILLISECOND, 0);
	}

	public static Date getFirstDayOfNextMonth() {
		Calendar firstDayOfNextMonth = Calendar.getInstance();
		firstDayOfNextMonth.add(Calendar.MONTH, 1);
		setToFirstDayOfMonth(firstDayOfNextMonth);
		Date result = firstDayOfNextMonth.getTime();
		return result;
	}

	public static Date getFirstDayOfLastMonth() {
		Calendar firstDayOfLastMonth = Calendar.getInstance();
		firstDayOfLastMonth.add(Calendar.MONTH, -1);
		setToFirstDayOfMonth(firstDayOfLastMonth);
		Date result = firstDayOfLastMonth.getTime();
		return result;
	}

	public static Date getFirstDayOfMonth(Date month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		setToFirstDayOfMonth(calendar);
		Date result = calendar.getTime();
		return result;
	}

	public static Date getFirstDayOfNextMonth(Date thisMonth) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(thisMonth);
		calendar.add(Calendar.MONTH, 1);
		setToFirstDayOfMonth(calendar);
		Date result = calendar.getTime();
		return result;
	}

	public static String getDateStringAsYYYYMM(Date date) {
		String pattern = "yyyy-MM";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String key = format.format(date);
		return key;
	}
}
