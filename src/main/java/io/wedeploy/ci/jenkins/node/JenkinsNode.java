package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public abstract class JenkinsNode {

	public abstract JSONObject toJSONObject();

	public String getHostname() {
		return hostname;
	}

	protected String hostname;
	protected String localURL;
	protected String remoteURL;

}