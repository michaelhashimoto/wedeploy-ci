package io.wedeploy.ci.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildDuration {

	protected BuildDuration(Integer totalDuration) {
		_totalDuration = totalDuration;

		Integer duration = totalDuration;

		_days =	(duration / (1000 * 60 * 60 * 24));
		duration = (duration % (1000 * 60 * 60 * 24));

		_hours = (duration / (1000 * 60 * 60));
		duration = (duration % (1000 * 60 * 60));

		_minutes = (duration / (1000 * 60));
		duration = (duration % (1000 * 60));

		_seconds = (duration / 1000);
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
	private final Integer _totalDuration;
}