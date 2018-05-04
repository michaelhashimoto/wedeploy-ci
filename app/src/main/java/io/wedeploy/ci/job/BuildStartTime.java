package io.wedeploy.ci.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildStartTime {

	protected BuildStartTime(String id) {
		_id = id;

		Matcher matcher = _pattern.matcher(id);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid id " + id);
		}

		_year = Integer.valueOf(matcher.group("year"));
		_month = Integer.valueOf(matcher.group("month"));
		_day = Integer.valueOf(matcher.group("day"));
		_hour = Integer.valueOf(matcher.group("hour"));
		_minute = Integer.valueOf(matcher.group("minute"));
		_second = Integer.valueOf(matcher.group("second"));
	}

	public Integer getDay() {
		return _day;
	}

	public Integer getMonth() {
		return _month;
	}

	public Integer getYear() {
		return _year;
	}

	@Override
	public String toString() {
		return _id;
	}

	private final String _id;
	private final Integer _year;
	private final Integer _month;
	private final Integer _day;
	private final Integer _hour;
	private final Integer _minute;
	private final Integer _second;

	private static Pattern _pattern = Pattern.compile(
		"(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})_" +
		"(?<hour>\\d{2})-(?<minute>\\d{2})-(?<second>\\d{2})");

}