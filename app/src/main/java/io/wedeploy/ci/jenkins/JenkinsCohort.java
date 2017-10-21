package io.wedeploy.ci.jenkins;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;

import io.wedeploy.ci.util.EnvironmentUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsCohort {

	public JenkinsCohort(JenkinsLegion jenkinsLegion, String name)
		throws Exception {

		_id = String.valueOf(CollectionUtil.getUniqueTimestamp());
		_jenkinsLegion = jenkinsLegion;
		_name = name;
		_offlineSlaveCount = 0;

		String[] jenkinsMasterHostnames = _JENKINS_MASTER_HOSTNAMES.split(",");

		for (String jenkinsMasterHostname : jenkinsMasterHostnames) {
			if (jenkinsMasterHostname.startsWith(_name)) {
				JenkinsMaster jenkinsMaster = new JenkinsMaster(
					this, jenkinsMasterHostname);

				_masterIDs.add(jenkinsMaster.getID());
				_masters.add(jenkinsMaster);

				JenkinsMaster.addJenkinsMaster(jenkinsMaster);
				JenkinsMaster.addJenkinsMasterToBeWritten(jenkinsMaster);

				_offlineSlaveCount += jenkinsMaster.getOfflineSlaveCount();
			}
		}

		JenkinsMaster.writeJenkinsMastersToDatabase();
	}

	public JenkinsCohort(JenkinsLegion jenkinsLegion, JSONObject jsonObject)
		throws Exception {

		_id = jsonObject.getString("id");
		_jenkinsLegion = jenkinsLegion;
		_name = jsonObject.getString("name");
		_offlineSlaveCount = jsonObject.getInt("offline_slave_count");

		JSONArray masterIDsJSONArray = jsonObject.getJSONArray("master_ids");

		for (int i = 0; i < masterIDsJSONArray.length(); i++) {
			String masterID = masterIDsJSONArray.getString(i);

			_masterIDs.add(masterID);
		}

		JSONArray masterDatabaseJSONArray = CollectionUtil.getMasterDatabaseJSONArray();

		for (int i = 0; i < masterDatabaseJSONArray.length(); i++) {
			JSONObject masterJSONObject = masterDatabaseJSONArray.getJSONObject(i);

			JenkinsMaster jenkinsMaster = new JenkinsMaster(this, masterJSONObject);

			String masterID = jenkinsMaster.getID();

			if (_masterIDs.contains(masterID)) {
				_masters.add(jenkinsMaster);
			}
		}
	}

	public String getID() {
		return _id;
	}

	public JenkinsLegion getJenkinsLegion() {
		return _jenkinsLegion;
	}

	public String getName() {
		return _name;
	}

	public List<JenkinsMaster> getMasters() {
		return _masters;
	}

	public List<String> getMasterIDs() {
		return _masterIDs;
	}

	public int getOfflineSlaveCount() {
		return _offlineSlaveCount;
	}

	public void update() {
		_offlineSlaveCount = 0;

		for (JenkinsMaster jenkinsMaster : _masters) {
			int jenkinsMasterOfflineSlaveCount = jenkinsMaster.getOfflineSlaveCount();

			jenkinsMaster.update();

			_offlineSlaveCount += jenkinsMaster.getOfflineSlaveCount();

			if (jenkinsMasterOfflineSlaveCount != jenkinsMaster.getOfflineSlaveCount()) {
				JenkinsMaster.updateJenkinsMasterOnDatabase(jenkinsMaster);
			}
		}
	}

	public static void addJenkinsCohort(JenkinsCohort jenkinsCohort) {
		_jenkinsCohorts.add(jenkinsCohort);
	}

	public static void addJenkinsCohortToBeWritten(JenkinsCohort jenkinsCohort) {
		_jenkinsCohortsToBeWritten.add(jenkinsCohort);
	}

	public static void updateJenkinsCohortOnDatabase(JenkinsCohort jenkinsCohort) {
		try {
			JSONObject jsonObject = new JSONObject()
				.put("offline_slave_count", jenkinsCohort.getOfflineSlaveCount());

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.update("cohort/" + jenkinsCohort.getID(), jsonObject)
				.execute();

			System.out.println("Updating cohort: " + jenkinsCohort.getName());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeJenkinsCohortsToDatabase() {
		try {
			JSONArray jsonArray = new JSONArray();

			for (JenkinsCohort jenkinsCohort : _jenkinsCohortsToBeWritten) {
				JenkinsLegion jenkinsLegion = jenkinsCohort.getJenkinsLegion();

				JSONObject jsonObject = new JSONObject()
					.put("id", jenkinsCohort.getID())
					.put("name", jenkinsCohort.getName())
					.put("legion_id", jenkinsLegion.getID())
					.put("master_ids", jenkinsCohort.getMasterIDs())
					.put("offline_slave_count", jenkinsCohort.getOfflineSlaveCount());

				jsonArray.put(jsonObject);
			}

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.create("cohort", jsonArray)
				.execute();

			_jenkinsCohortsToBeWritten.clear();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final String _id;
	private final JenkinsLegion _jenkinsLegion;
	private final List<String> _masterIDs = new ArrayList<>();
	private final List<JenkinsMaster> _masters = new ArrayList<>();
	private final String _name;
	private int _offlineSlaveCount;
	private final static String _JENKINS_MASTER_HOSTNAMES = EnvironmentUtil.get(
		"JENKINS_MASTER_HOSTNAMES");
	private static final List<JenkinsCohort> _jenkinsCohorts = new ArrayList<>();
	private static final List<JenkinsCohort> _jenkinsCohortsToBeWritten = new ArrayList<>();

}