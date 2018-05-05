package io.wedeploy.ci.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildTime {

	protected BuildTime(BuildTime buildTime) {
		_calendar = Calendar.getInstance();

		_calendar.setLenient(false);

		try {
			_calendar.setTime(_simpleDateFormat.parse(buildTime.toString()));
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

	public Calendar getCalendar() {
		return _calendar;
	}

	public Integer getDay() {
		return _calendar.get(Calendar.DATE);
	}

	public Integer getMonth() {
		return _calendar.get(Calendar.MONTH);
	}

	public Integer getYear() {
		return _calendar.get(Calendar.YEAR);
	}

	@Override
	public String toString() {
		return _simpleDateFormat.format(_calendar.getTime());
	}

	public String getStartOfYear() {
		return _calendar.toString();
	}

	public int compareTo(BuildTime buildTime) {
		return _calendar.compareTo(buildTime.getCalendar());
	}

	private final Calendar _calendar;

	private static SimpleDateFormat _simpleDateFormat = new SimpleDateFormat(
		"yyyy-MM-dd_HH-mm-ss");

}