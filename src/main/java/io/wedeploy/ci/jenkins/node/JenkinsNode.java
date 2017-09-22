package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public abstract class JenkinsNode {

	public abstract JSONObject toJSONObject();

	public String getHostname() {
		return hostname;
	}

	public String toString() {
		JSONObject jsonObject = toJSONObject();

		return jsonObject.toString();
	}

	protected String hostname;
	protected String localURL;
	protected String remoteURL;

}