package io.wedeploy.ci.jenkins.node;

import io.wedeploy.ci.util.CurlUtil;

import java.io.IOException;

import org.json.JSONObject;

public class JenkinsSlave extends JenkinsNode {

	public JenkinsSlave(String hostname, JenkinsMaster jenkinsMaster) throws IOException {
		this.hostname = hostname;

		_jenkinsMaster = jenkinsMaster;

		String masterHostname = _jenkinsMaster.getHostname();

		localURL = "http://" + masterHostname + "/computer/" + hostname + "/";
		remoteURL = "https://" + masterHostname + ".liferay.com/computer/" + hostname + "/";

		JSONObject slaveJSONObject = new JSONObject(CurlUtil.curl(
			remoteURL + "api/json?tree=offlineCauseReason,offline"));

		_offline = slaveJSONObject.getBoolean("offline");
		_offlineCause = slaveJSONObject.getString("offlineCauseReason");
	}

	public boolean isOffline() {
		return _offline;
	}

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("hostname", hostname);
		jsonObject.put("offline", _offline);
		jsonObject.put("offline_cause", _offlineCause);
		jsonObject.put("remote_url", remoteURL);
		jsonObject.put("local_url", localURL);

		return jsonObject;
	}

	private boolean _offline;
	private String _offlineCause;
	private JenkinsMaster _jenkinsMaster;

}