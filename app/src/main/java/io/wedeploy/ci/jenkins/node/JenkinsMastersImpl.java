package io.wedeploy.ci.jenkins.node;

import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsMastersImpl implements JenkinsMasters {

	public JenkinsMastersImpl() throws IOException {
		String[] jenkinsMasterHostnames = _JENKINS_MASTER_HOSTNAMES.split(",");

		for (String jenkinsMasterHostname : jenkinsMasterHostnames) {
			JenkinsMaster jenkinsMaster = new JenkinsMasterImpl(
				jenkinsMasterHostname);

			_offlineSlaveCount += jenkinsMaster.getOfflineSlaveCount();

			_jenkinsMasters.add(jenkinsMaster);
		}
	}

	public int getOfflineSlaveCount() {
		return _offlineSlaveCount;
	}

	public String getOverviewInformation() {
		JSONObject jo = new JSONObject();

		jo.put("offline_slave_count", _offlineSlaveCount);
		for (JenkinsMaster jenkinsMaster : _jenkinsMasters) {
			jo.put(jenkinsMaster.getHostname(), jenkinsMaster.getOfflineSlaveCount());
		}

		return jo.toString();
	}

	public String toString() {
		JSONObject jsonObject = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (JenkinsMaster jenkinsMaster : _jenkinsMasters) {
			jsonArray.put(jenkinsMaster.toJSONObject());
		}

		jsonObject.put("masters", jsonArray);
		jsonObject.put("offline_slave_count", _offlineSlaveCount);

		return jsonObject.toString();
	}

	private List<JenkinsMaster> _jenkinsMasters = new ArrayList<>();
	private int _offlineSlaveCount = 0;
	private final static String _JENKINS_MASTER_HOSTNAMES =
		EnvironmentUtil.get("JENKINS_MASTER_HOSTNAMES");

}