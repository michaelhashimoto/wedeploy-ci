package io.wedeploy.ci.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildDuration {

	protected BuildDuration(Integer duration) {
		_days =	(duration / (1000 * 60 * 60 * 24));
		duration = (duration % (1000 * 60 * 60 * 24));

		_hours = (duration / (1000 * 60 * 60));
		duration = (duration % (1000 * 60 * 60));

		_minutes = (duration / (1000 * 60));
		duration = (duration % (1000 * 60));

		_seconds = (duration / 1000);
	}

	protected BuildDuration(String totalCpuUsage) {
		Matcher matcher = _pattern.matcher(totalCpuUsage);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid CPU Usage: " + totalCpuUsage);
		}

		String days = matcher.group("days");

		if (days != null) {
			_days =	Integer.valueOf(days);
		}
		else {
			_days = 0;
		}

		String hours = matcher.group("hours");

		if (hours != null) {
			_hours =	Integer.valueOf(hours);
		}
		else {
			_hours = 0;
		}

		String minutes = matcher.group("minutes");

		if (minutes != null) {
			_minutes = Integer.valueOf(minutes);
		}
		else {
			_minutes = 0;
		}

		String seconds = matcher.group("seconds");

		if (seconds != null) {
			_seconds =	Integer.valueOf(seconds);
		}
		else {
			_seconds = 0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if (_days > 0) {
			sb.append(_days);
			sb.append(" Day");

			if (_days > 1) {
				sb.append("s");
			}

			sb.append(" ");
		}

		if (_hours > 0) {
			sb.append(_hours);
			sb.append(" Hour");

			if (_hours > 1) {
				sb.append("s");
			}

			sb.append(" ");
		}

		if (_minutes > 0) {
			sb.append(_minutes);
			sb.append(" Minute");

			if (_minutes > 1) {
				sb.append("s");
			}

			sb.append(" ");
		}

		if (_seconds > 0) {
			sb.append(_seconds);
			sb.append(" Second");

			if (_seconds > 1) {
				sb.append("s");
			}
		}

		return sb.toString().trim();
	}

	private final Integer _days;
	private final Integer _hours;
	private final Integer _minutes;
	private final Integer _seconds;

	private static Pattern _pattern = Pattern.compile(
		"Total CPU Usage Time: " +
		"((?<days>\\d+) days?)?\\s*" +
		"((?<hours>\\d+) hours?)?\\s*" +
		"((?<minutes>\\d+) minutes?)?\\s*" +
		"((?<seconds>\\d+) seconds?)?\\s*" +
		"((?<milliseconds>\\d+) ms)?");
}