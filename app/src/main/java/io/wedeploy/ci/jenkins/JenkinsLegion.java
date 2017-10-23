package io.wedeploy.ci.jenkins;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;

import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsLegion {

	public JenkinsLegion() throws Exception {
		Response jenkinsLegionResponse = CollectionUtil.getWeDeploy()
			.data("https://data-ci.wedeploy.io")
			.get("legion")
			.execute();

		String jenkinsLegionResponseBody = jenkinsLegionResponse.getBody();

		if (jenkinsLegionResponseBody.equals("[]")) {
			_id = String.valueOf(CollectionUtil.getUniqueTimestamp());

			Set<String> cohortNames = new HashSet<>();

			String[] jenkinsMasterHostnames = _JENKINS_MASTER_HOSTNAMES.split(",");

			for (String jenkinsMasterHostname : jenkinsMasterHostnames) {
				String cohortName = _getCohortName(jenkinsMasterHostname);

				cohortNames.add(cohortName);
			}

			for (String cohortName : cohortNames) {
				JenkinsCohort jenkinsCohort = new JenkinsCohort(this, cohortName);

				_cohortIDs.add(jenkinsCohort.getID());
				_cohorts.add(jenkinsCohort);

				JenkinsCohort.addJenkinsCohort(jenkinsCohort);
				JenkinsCohort.addJenkinsCohortToBeWritten(jenkinsCohort);
			}

			JenkinsCohort.writeJenkinsCohortsToDatabase();

			addJenkinsLegion(this);
			addJenkinsLegionToBeWritten(this);

			writeJenkinsLegionsToDatabase();

			System.out.println(new Timestamp(System.currentTimeMillis()) + " Jenkins Legion loaded data into database.");
		}
		else {
			JSONArray jenkinsLegionJSONArray = new JSONArray(jenkinsLegionResponseBody);

			JSONObject jenkinsLegionJSONObject = jenkinsLegionJSONArray.getJSONObject(0);

			_id = jenkinsLegionJSONObject.getString("id");

			JSONArray cohortIDsJSONArray = jenkinsLegionJSONObject.getJSONArray("cohort_ids");

			for (int i = 0; i < cohortIDsJSONArray.length(); i++) {
				String cohortID = cohortIDsJSONArray.getString(i);

				_cohortIDs.add(cohortID);
			}

			JSONArray cohortDatabaseJSONArray = CollectionUtil.getCohortDatabaseJSONArray();

			for (int i = 0; i < cohortDatabaseJSONArray.length(); i++) {
				JSONObject cohortJSONObject = cohortDatabaseJSONArray.getJSONObject(i);

				JenkinsCohort jenkinsCohort = new JenkinsCohort(this, cohortJSONObject);

				String cohortID = jenkinsCohort.getID();

				if (_cohortIDs.contains(cohortID)) {
					_cohorts.add(jenkinsCohort);
				}
			}

			addJenkinsLegion(this);

			System.out.println(new Timestamp(System.currentTimeMillis()) + " Jenkins Legion loaded from database.");
		}

		_lastUpdate = new Date();
	}

	public String getID() {
		return _id;
	}

	public String getName() {
		return _JENKINS_LEGION_NAME;
	}

	public List<String> getCohortIDs() {
		return _cohortIDs;
	}

	public List<JenkinsCohort> getCohorts() {
		return _cohorts;
	}
	public String getLastUpdate() {
		return _lastUpdate.toString();
	}

	public void update() {
		for (JenkinsCohort jenkinsCohort : _cohorts) {
			int offlineSlaveCount = jenkinsCohort.getOfflineSlaveCount();

			jenkinsCohort.update();

			if (offlineSlaveCount != jenkinsCohort.getOfflineSlaveCount()) {
				JenkinsCohort.updateJenkinsCohortOnDatabase(jenkinsCohort);
			}
		}

		_lastUpdate = new Date();

		updateJenkinsCohortOnDatabase(this);
	}

	public static void addJenkinsLegion(JenkinsLegion jenkinsLegion) {
		_jenkinsLegions.add(jenkinsLegion);
	}

	public static void addJenkinsLegionToBeWritten(JenkinsLegion jenkinsLegion) {
		_jenkinsLegionsToBeWritten.add(jenkinsLegion);
	}

	public static JenkinsLegion getJenkinsLegion() throws Exception {
		if (_jenkinsLegions.isEmpty()) {
			return new JenkinsLegion();
		}

		return _jenkinsLegions.get(0);
	}

	public static void updateJenkinsCohortOnDatabase(JenkinsLegion jenkinsLegion) {
		try {
			JSONObject jsonObject = new JSONObject()
				.put("last_update", jenkinsLegion.getLastUpdate());

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.update("legion/" + jenkinsLegion.getID(), jsonObject)
				.execute();

			//System.out.println(new Timestamp(System.currentTimeMillis()) + " Updating information for legion: " + jenkinsLegion.getName());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeJenkinsLegionsToDatabase() {
		try {
			JSONArray jsonArray = new JSONArray();

			for (JenkinsLegion jenkinsLegion : _jenkinsLegionsToBeWritten) {
				JSONObject jsonObject = new JSONObject()
					.put("cohort_ids", jenkinsLegion.getCohortIDs())
					.put("last_update", jenkinsLegion.getLastUpdate())
					.put("id", jenkinsLegion.getID())
					.put("name", jenkinsLegion.getName());

				jsonArray.put(jsonObject);
			}

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.create("legion", jsonArray)
				.execute();

			_jenkinsLegionsToBeWritten.clear();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String _getCohortName(String masterHostname) {
		if (!masterHostname.matches("test-\\d+-\\d+")) {
			throw new RuntimeException(
				"Invalid Master Hostname: " + masterHostname);
		}

		return masterHostname.substring(0, masterHostname.lastIndexOf("-"));
	}

	private String _id;
	private Date _lastUpdate;
	private final List<JenkinsCohort> _cohorts = new ArrayList<>();
	private final List<String> _cohortIDs = new ArrayList<>();
	private final static String _JENKINS_LEGION_NAME = "liferay-ci";
	private final static String _JENKINS_MASTER_HOSTNAMES = EnvironmentUtil.get(
		"JENKINS_MASTER_HOSTNAMES");
	private static final List<JenkinsLegion> _jenkinsLegions = new ArrayList<>();
	private static final List<JenkinsLegion> _jenkinsLegionsToBeWritten = new ArrayList<>();

	/* Temporary workaround to always have a fresh database. */
	/*static {
		try {
			WeDeploy weDeploy = new WeDeploy.Builder().build();

			weDeploy.data("https://data-ci.wedeploy.io")
				.delete("legion")
				.execute();

			weDeploy.data("https://data-ci.wedeploy.io")
				.delete("cohort")
				.execute();

			weDeploy.data("https://data-ci.wedeploy.io")
				.delete("master")
				.execute();

			weDeploy.data("https://data-ci.wedeploy.io")
				.delete("slave")
				.execute();
		}
		catch (Exception e) {
		}
	}*/

}