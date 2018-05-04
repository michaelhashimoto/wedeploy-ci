package io.wedeploy.ci.job;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseJob implements Job {

	public BaseJob(String url) {
		_url = url;

		Matcher matcher = _pattern.matcher(url);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid url " + _url);
		}

		_name = matcher.group("jobName");
	}

	public String getName() {
		return _name;
	}

	public String getURL() {
		return _name;
	}

	private String _name;
	private String _url;

	private static Pattern _pattern = Pattern.compile(
		"https?://[^/]+/job/(?<jobName>[^/]+)/");

}