package io.wedeploy.ci.job;

import io.wedeploy.ci.util.CurlUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.json.JSONObject;

public class BaseBuild implements Build {

	public BaseBuild(Job job, String url) {
		Matcher urlMatcher = _urlPattern.matcher(url);

		if (!urlMatcher.find()) {
			throw new RuntimeException("Invalid url " + url);
		}

		_buildNumber = Integer.valueOf(urlMatcher.group("buildNumber"));

		String hostName = job.getHostName();
		String jobName = job.getJobName();

		_localURL = "http://" + hostName + "/job/" + jobName + "/" + _buildNumber;
		_remoteURL = "https://" + hostName + ".liferay.com/job/" + jobName + "/" + _buildNumber;

		try {
			JSONObject jsonObject = new JSONObject(
				CurlUtil.curl(_remoteURL + "/api/json?tree=description,duration,id,result"));

			if (jsonObject.opt("result") != null) {
				_result = Result.valueOf(jsonObject.getString("result"));
			}
			else {
				_result = Result.NOT_BUILT;
			}

			if (_result == Result.NOT_BUILT) {
				return;
			}

			_buildStartTime = new BuildTime(jsonObject.getString("id"));
			_topLevelDuration = new BuildDuration(jsonObject.getInt("duration"));

			if (jsonObject.opt("description") == null) {
				_totalDuration = _topLevelDuration;

				System.out.println("WARNING: No description found " + _remoteURL);

				return;
			}

			String description = jsonObject.getString("description");

			Matcher descriptionMatcher = _descriptionPattern.matcher(description);

			if (!descriptionMatcher.find()) {
				_totalDuration = _topLevelDuration;

				System.out.println("WARNING: No jenkins report found " + _remoteURL);

				return;
			}

			String jenkinsReportURL = descriptionMatcher.group();

			String totalCpuUsage = _getTotalCpuUsage(jenkinsReportURL);

			_totalDuration = new BuildDuration(totalCpuUsage);
		}
		catch (Throwable t) {
			System.out.println("SKIPPED: " + url);
		}
	}

	public BaseBuild(Job job, JSONObject buildJSONObject) {
		this(job, buildJSONObject.getString("url"));
	}

	@Override
	public Integer getBuildNumber() {
		return _buildNumber;
	}

	@Override
	public BuildTime getBuildStartTime() {
		return _buildStartTime;
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

	@Override
	public BuildDuration getTopLevelDuration() {
		return _topLevelDuration;
	}

	@Override
	public BuildDuration getTotalDuration() {
		return _totalDuration;
	}

	private String _getTotalCpuUsage(String jenkinsReportURL) {
		Element rootElement = _getRootElement(CurlUtil.curl(jenkinsReportURL));

		Element bodyElement = rootElement.element("body");

		Element divElement = bodyElement.element("div");

		List pElements = divElement.elements("p");

		for (Object o : pElements) {
			if ((o == null) || (!(o instanceof Element))) {
				continue;
			}

			Element pElement = (Element)o;

			String text = pElement.getText();

			if (text.contains("Total CPU Usage Time:")) {
				return text;
			}
		}

		return null;
	}

	private Element _getRootElement(String content) {
		try {
			InputStream inputStream = new ByteArrayInputStream(
				content.getBytes("UTF-8"));

			SAXReader saxReader = new SAXReader();

			Document document = saxReader.read(inputStream);

			Element rootElement = document.getRootElement();

			return document.getRootElement();
		}
		catch (DocumentException | UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	private BuildTime _buildStartTime;
	private final String _localURL;
	private final Integer _buildNumber;
	private final String _remoteURL;
	private Result _result = Result.NOT_FOUND;
	private BuildDuration _topLevelDuration;
	private BuildDuration _totalDuration;

	private static Pattern _descriptionPattern = Pattern.compile(
		"https://test-\\d+-\\d+.liferay.com/.*/jenkins-report.html");
	private static Pattern _urlPattern = Pattern.compile(
		"https?://[^/]+/job/[^/]+/(?<buildNumber>\\d+)/?");

}