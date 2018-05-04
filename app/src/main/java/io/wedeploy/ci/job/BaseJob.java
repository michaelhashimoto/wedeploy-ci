package io.wedeploy.ci.job;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseJob implements Job {

	public BaseJob(String url) {
		Matcher matcher = _pattern.matcher(url);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid url " + url);
		}

		_jobName = matcher.group("jobName");
		_hostName = matcher.group("hostName");

		_localURL = "http://" + _hostName + "/job/" + _jobName;
		_remoteURL = "https://" + _hostName + ".lax.liferay.com/job/" + _jobName;
	}

	@Override
	public String getHostName() {
		return _hostName;
	}

	@Override
	public String getLocalURL() {
		return _localURL;
	}

	@Override
	public String getJobName() {
		return _jobName;
	}

	@Override
	public String getRemoteURL() {
		return _remoteURL;
	}

	private final String _hostName;
	private final String _localURL;
	private final String _jobName;
	private final String _remoteURL;

	private static Pattern _pattern = Pattern.compile(
		"https?://(?<hostName>test-\\d+-\\d+)[^/]*/job/(?<jobName>[^/]+)/?");

}