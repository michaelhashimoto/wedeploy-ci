package io.wedeploy.ci.jenkins.node;

import io.wedeploy.ci.util.EnvironmentUtil;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsMasters {

	public JenkinsMasters() {
		String[] jenkinsMasterHostnames = _JENKINS_MASTER_HOSTNAMES.split(",");

		for (String jenkinsMasterHostname : jenkinsMasterHostnames) {
			_jenkinsMasters.add(new JenkinsMaster(jenkinsMasterHostname));
		}
	}

	public String toString() {
		JSONArray jsonArray = new JSONArray();

		for (JenkinsMaster jenkinsMaster : _jenkinsMasters) {
			jsonArray.put(jenkinsMaster.toJSONObject());
		}

		return jsonArray.toString();
	}

	private List<JenkinsMaster> _jenkinsMasters = new ArrayList<>();
	private final static String _JENKINS_MASTER_HOSTNAMES =
		EnvironmentUtil.get("JENKINS_MASTER_HOSTNAMES");

}