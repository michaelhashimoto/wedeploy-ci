package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public class JenkinsSlave extends JenkinsNode {

	public JenkinsSlave(String hostname) {
		this.hostname = hostname;
		this.localURL = "http://" + hostname + "/";
		this.remoteURL = "https://" + hostname + ".liferay.com/";
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("hostname", hostname);
		jsonObject.put("remote_url", remoteURL);
		jsonObject.put("local_url", localURL);

		return jsonObject;
	}

}