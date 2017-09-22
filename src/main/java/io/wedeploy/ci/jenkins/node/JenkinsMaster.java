package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public class JenkinsMaster extends JenkinsSlave {

	public JenkinsMaster(String hostname) {
		super(hostname);
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("hostname", hostname);
		jsonObject.put("remote_url", remoteURL);
		jsonObject.put("local_url", localURL);

		return jsonObject;
	}

}