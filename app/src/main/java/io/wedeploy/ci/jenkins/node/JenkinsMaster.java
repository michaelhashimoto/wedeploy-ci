package io.wedeploy.ci.jenkins.node;

import io.wedeploy.ci.util.CurlUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsMaster extends JenkinsNode {

	public JenkinsMaster(String hostname) throws IOException {
		this.hostname = hostname;
		this.localURL = "http://" + hostname + "/";
		this.remoteURL = "https://" + hostname + ".liferay.com/";

		_jenkinsSlaves = new ArrayList<>();

		JSONObject computerJSONObject = new JSONObject(CurlUtil.curl(
			remoteURL + "computer/api/json?tree=computer[displayName]"));

		JSONArray computerJSONArray = computerJSONObject.getJSONArray(
			"computer");

		for (int i = 0; i < computerJSONArray.length(); i++) {
			JSONObject nodeJSONObject = computerJSONArray.getJSONObject(i);

			String displayName = nodeJSONObject.getString("displayName");

			if (displayName.startsWith("cloud-")) {
				JenkinsSlave jenkinsSlave = new JenkinsSlave(displayName, this);

				if (jenkinsSlave.isOffline()) {
					_offlineSlaveCount++;
				}

				_jenkinsSlaves.add(jenkinsSlave);
			}
		}
	}

	public int getOfflineSlaveCount() {
		return _offlineSlaveCount;
	}

	public List<JenkinsSlave> getJenkinsSlaves() {
		return _jenkinsSlaves;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("hostname", hostname);
		jsonObject.put("remote_url", remoteURL);
		jsonObject.put("local_url", localURL);

		JSONArray jsonArray = new JSONArray();

		for (JenkinsSlave jenkinsSlave : _jenkinsSlaves) {
			jsonArray.put(jenkinsSlave.toJSONObject());
		}

		jsonObject.put("slaves", jsonArray);

		return jsonObject;
	}

	private int _offlineSlaveCount = 0;
	private List<JenkinsSlave> _jenkinsSlaves;

}