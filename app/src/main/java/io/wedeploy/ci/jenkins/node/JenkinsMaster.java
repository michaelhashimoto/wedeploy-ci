package io.wedeploy.ci.jenkins.node;

import java.util.List;

import org.json.JSONObject;

public interface JenkinsMaster {

	public String getHostname();

	public int getOfflineSlaveCount();

	public List<JenkinsSlave> getJenkinsSlaves();

	public JSONObject toJSONObject();

}