package io.wedeploy.ci.job;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseBuild implements Build {

	public BaseBuild(Job job, String url) {
		Matcher matcher = _pattern.matcher(url);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid url " + url);
		}

		_buildNumber = Integer.valueOf(matcher.group("buildNumber"));

		String hostName = job.getHostName();
		String jobName = job.getJobName();

		_localURL = "http://" + hostName + "/job/" + jobName + "/" + _buildNumber;
		_remoteURL = "https://" + hostName + ".lax.liferay.com/job/" + jobName + "/" + _buildNumber;
	}

	@Override
	public Integer getBuildNumber() {
		return _buildNumber;
	}

	@Override
	public String getLocalURL() {
		return _localURL;
	}

	@Override
	public String getRemoteURL() {
		return _remoteURL;
	}

	private final String _localURL;
	private final Integer _buildNumber;
	private final String _remoteURL;

	private static Pattern _pattern = Pattern.compile(
		"https?://[^/]+/job/[^/]+/(?<buildNumber>\\d+)/?");

}