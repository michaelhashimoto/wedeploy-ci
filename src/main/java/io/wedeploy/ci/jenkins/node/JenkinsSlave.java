package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public class JenkinsSlave extends JenkinsNode {

	public JenkinsSlave(String hostname, JenkinsMaster jenkinsMaster) {
		this.hostname = hostname;

		_jenkinsMaster = jenkinsMaster;

		String masterHostname = _jenkinsMaster.getHostname();

		localURL = "http://" + masterHostname + "/computer/" + hostname;
		remoteURL = "https://" + masterHostname + ".liferay.com/computer/" + hostname;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("hostname", hostname);
		jsonObject.put("remote_url", remoteURL);
		jsonObject.put("local_url", localURL);

		return jsonObject;
	}

	private JenkinsMaster _jenkinsMaster;

}