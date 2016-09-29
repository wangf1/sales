package com.wangf.sales.management.utils;

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
}
