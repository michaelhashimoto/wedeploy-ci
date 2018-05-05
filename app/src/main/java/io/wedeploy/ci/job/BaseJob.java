package io.wedeploy.ci.job;

import io.wedeploy.ci.util.CurlUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

public class BaseJob implements Job {

	public BaseJob(String url) {
		Matcher matcher = _pattern.matcher(url);

		if (!matcher.find()) {
			throw new RuntimeException("Invalid url " + url);
		}

		_jobName = matcher.group("jobName");
		_hostName = matcher.group("hostName");

		_localURL = "http://" + _hostName + "/job/" + _jobName;
		_remoteURL = "https://" + _hostName + ".liferay.com/job/" + _jobName;
	}

	@Override
	public List<Build> getCompletedBuilds() {
		if (_completedBuilds != null) {
			return _completedBuilds;
		}

		_completedBuilds = new ArrayList<>();

		JSONObject jsonObject = new JSONObject(CurlUtil.curl(_remoteURL + "/api/json?tree=builds[url]"));

		JSONArray jsonArray = jsonObject.getJSONArray("builds");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject buildJSONObject = jsonArray.getJSONObject(i);

			Build build = new BaseBuild(this, buildJSONObject);

			Build.Result result = build.getResult();

			if (result == Build.Result.IN_PROGRESS ||
				result == Build.Result.NOT_FOUND) {

				continue;
			}

			_completedBuilds.add(build);
		}

		return _completedBuilds;
	}

	@Override
	public List<Build> getCompletedBuildsByDay(Integer year, Integer month, Integer day) {
		return getCompletedBuildsByDay(year, month, day, 1);
	}

	@Override
	public List<Build> getCompletedBuildsByDay(Integer year, Integer month, Integer day, Integer numberOfDays) {
		BuildTime startingBuildTime = _getStartingBuildTime(year, month, day);

		BuildTime endingBuildTime = new BuildTime(startingBuildTime);

		endingBuildTime.addDays(numberOfDays);

		return _getCompletedBuilds(startingBuildTime, endingBuildTime);
	}

	@Override
	public List<Build> getCompletedBuildsByMonth(Integer year, Integer month) {
		return getCompletedBuildsByMonth(year, month, 1);
	}

	@Override
	public List<Build> getCompletedBuildsByMonth(Integer year, Integer month, Integer numberOfMonths) {
		BuildTime startingBuildTime = _getStartingBuildTime(year, month, 1);

		BuildTime endingBuildTime = new BuildTime(startingBuildTime);

		endingBuildTime.addMonths(numberOfMonths);

		return _getCompletedBuilds(startingBuildTime, endingBuildTime);
	}

	@Override
	public List<Build> getCompletedBuildsByYear(Integer year) {
		return getCompletedBuildsByYear(year, 1);
	}

	@Override
	public List<Build> getCompletedBuildsByYear(Integer year, Integer yearsToAdd) {
		BuildTime startingBuildTime = _getStartingBuildTime(year, Calendar.JANUARY, 1);

		BuildTime endingBuildTime = new BuildTime(startingBuildTime);

		endingBuildTime.addYears(yearsToAdd);

		return _getCompletedBuilds(startingBuildTime, endingBuildTime);
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

	private BuildTime _getStartingBuildTime(Integer year, Integer month, Integer day) {
		StringBuilder sb = new StringBuilder();

		sb.append(year);
		sb.append("-");
		sb.append(String.format("%02d", (month + 1)));
		sb.append("-");
		sb.append(String.format("%02d", day));
		sb.append("_00-00-00");

		return new BuildTime(sb.toString());
	}

	private List<Build> _getCompletedBuilds(
		BuildTime startingBuildTime, BuildTime endingBuildTime) {

		List<Build> builds = new ArrayList<>();

		for (Build build : getCompletedBuilds()) {
			BuildTime buildStartTime = build.getBuildStartTime();

			if ((buildStartTime.compareTo(startingBuildTime) >= 0) &&
				(buildStartTime.compareTo(endingBuildTime) <= 0)) {

				builds.add(build);
			}
		}

		return builds;
	}

	private List<Build> _completedBuilds;
	private final String _hostName;
	private final String _localURL;
	private final String _jobName;
	private final String _remoteURL;

	private static Pattern _pattern = Pattern.compile(
		"https?://(?<hostName>test-\\d+-\\d+)[^/]*/job/(?<jobName>[^/]+)/?");

}