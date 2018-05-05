package io.wedeploy.ci.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildTime {

	protected BuildTime(BuildTime buildTime) {
		_calendar = Calendar.getInstance();

		_calendar.setLenient(false);

		try {
			Date date = _simpleDateFormat.parse(buildTime.toString());

			_calendar.setTime(date);
		}
		catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	protected BuildTime(String id) {
		_calendar = Calendar.getInstance();

		_calendar.setLenient(false);

		try {
			_calendar.setTime(_simpleDateFormat.parse(id));
		}
		catch (ParseException pe) {
			throw new RuntimeException(pe);
		}
	}

	public void addDays(Integer daysToAdd) {
		_calendar.add(Calendar.DATE, daysToAdd);
	}

	public void addMonths(Integer monthsToAdd) {
		_calendar.add(Calendar.MONTH, monthsToAdd);
	}

	public void addYears(Integer yearsToAdd) {
		_calendar.add(Calendar.YEAR, yearsToAdd);
	}

	public int compareTo(BuildTime buildTime) {
		return _calendar.compareTo(buildTime.getCalendar());
	}

	public Calendar getCalendar() {
		return _calendar;
	}

	@Override
	public String toString() {
		return _simpleDateFormat.format(_calendar.getTime());
	}

	private final Calendar _calendar;

	private static SimpleDateFormat _simpleDateFormat = new SimpleDateFormat(
		"yyyy-MM-dd_HH-mm-ss");

}