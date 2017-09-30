package io.wedeploy.ci.jenkins.node;

import org.json.JSONObject;

public interface JenkinsSlave {

	public boolean isOffline();

	public JSONObject toJSONObject();

}