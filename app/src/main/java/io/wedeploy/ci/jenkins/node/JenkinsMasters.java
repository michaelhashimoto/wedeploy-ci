package io.wedeploy.ci.jenkins.node;

import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsMasters {

	public JenkinsMasters() throws IOException {
		String[] jenkinsMasterHostnames = _JENKINS_MASTER_HOSTNAMES.split(",");

		for (String jenkinsMasterHostname : jenkinsMasterHostnames) {
			_jenkinsMasters.add(new JenkinsMaster(jenkinsMasterHostname));
		}
	}

	public String toString() {
		JSONObject jsonObject = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (JenkinsMaster jenkinsMaster : _jenkinsMasters) {
			jsonArray.put(jenkinsMaster.toJSONObject());
		}

		jsonObject.put("masters", jsonArray);

		return jsonObject.toString();
	}

	private List<JenkinsMaster> _jenkinsMasters = new ArrayList<>();
	private final static String _JENKINS_MASTER_HOSTNAMES =
		EnvironmentUtil.get("JENKINS_MASTER_HOSTNAMES");

}