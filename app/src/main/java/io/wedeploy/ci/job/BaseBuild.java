package io.wedeploy.ci.job;

import io.wedeploy.ci.util.CurlUtil;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

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
		_remoteURL = "https://" + hostName + ".liferay.com/job/" + jobName + "/" + _buildNumber;

		try {
			JSONObject jsonObject = new JSONObject(
				CurlUtil.curl(_remoteURL + "/api/json?tree=duration,id,result"));

			if (jsonObject.opt("result") != null) {
				_result = Result.valueOf(jsonObject.getString("result"));
			}
			else {
				_result = Result.NOT_BUILT;
			}

			if (_result == Result.NOT_BUILT) {
				return;
			}

			_buildStartTime = new BuildStartTime(jsonObject.getString("id"));
			_topLevelDuration = new BuildDuration(jsonObject.getInt("duration"));
		}
		catch (Throwable t) {
			System.out.println(t.getMessage());
		}
	}

	public BaseBuild(Job job, JSONObject buildJSONObject) {
		this(job, buildJSONObject.getString("url"));
	}

	@Override
	public BuildDuration getTopLevelDuration() {
		return _topLevelDuration;
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

	@Override
	public Result getResult() {
		return _result;
	}

	private BuildStartTime _buildStartTime;
	private final String _localURL;
	private final Integer _buildNumber;
	private final String _remoteURL;
	private Result _result = Result.NOT_FOUND;
	private BuildDuration _topLevelDuration;

	private static Pattern _pattern = Pattern.compile(
		"https?://[^/]+/job/[^/]+/(?<buildNumber>\\d+)/?");

}