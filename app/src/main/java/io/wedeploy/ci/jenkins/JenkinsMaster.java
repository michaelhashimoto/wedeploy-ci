package io.wedeploy.ci.jenkins;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;

import io.wedeploy.ci.util.CurlUtil;

import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsMaster {

	public JenkinsMaster(JenkinsCohort jenkinsCohort, String name)
		throws Exception {

		_id = String.valueOf(CollectionUtil.getUniqueTimestamp());
		_jenkinsCohort = jenkinsCohort;
		_name = name;
		_offlineSlaveCount = 0;
		_remoteURL = "https://" + name + ".liferay.com/";

		JSONObject computerJSONObject = new JSONObject(CurlUtil.curl(
			_remoteURL + "computer/api/json?" +
				"tree=computer[displayName,offline,offlineCauseReason]"));

		JSONArray computerJSONArray = computerJSONObject.getJSONArray(
			"computer");

		for (int i = 0; i < computerJSONArray.length(); i++) {
			JSONObject nodeJSONObject = computerJSONArray.getJSONObject(i);

			String displayName = nodeJSONObject.getString("displayName");

			if (displayName.startsWith("cloud-")) {
				boolean offline = nodeJSONObject.getBoolean("offline");
				String offlineCauseReason = nodeJSONObject.getString(
					"offlineCauseReason");

				JenkinsSlave jenkinsSlave = new JenkinsSlave(
					this, displayName, offline, offlineCauseReason);

				_slaveIDs.add(jenkinsSlave.getID());
				_slaves.add(jenkinsSlave);

				JenkinsSlave.addJenkinsSlave(jenkinsSlave);
				JenkinsSlave.addJenkinsSlaveToBeWritten(jenkinsSlave);

				if (jenkinsSlave.isOffline()) {
					_offlineSlaveCount++;
				}
			}
		}

		JenkinsSlave.writeJenkinsSlavesToDatabase();
	}

	public JenkinsMaster(JenkinsCohort jenkinsCohort, JSONObject jsonObject)
		throws Exception {

		_id = jsonObject.getString("id");
		_jenkinsCohort = jenkinsCohort;
		_name = jsonObject.getString("name");
		_offlineSlaveCount = jsonObject.getInt("offline_slave_count");
		_remoteURL = jsonObject.getString("remote_url");

		JSONArray slaveIDsJSONArray = jsonObject.getJSONArray("slave_ids");

		for (int i = 0; i < slaveIDsJSONArray.length(); i++) {
			String slaveID = slaveIDsJSONArray.getString(i);

			_slaveIDs.add(slaveID);
		}

		JSONArray slaveDatabaseJSONArray = CollectionUtil.getSlaveDatabaseJSONArray();

		for (int i = 0; i < slaveDatabaseJSONArray.length(); i++) {
			JSONObject slaveJSONObject = slaveDatabaseJSONArray.getJSONObject(i);

			JenkinsSlave jenkinsSlave = new JenkinsSlave(this, slaveJSONObject);

			String slaveID = jenkinsSlave.getID();

			if (_slaveIDs.contains(slaveID)) {
				_slaves.add(jenkinsSlave);
			}
		}
	}

	public String getID() {
		return _id;
	}

	public JenkinsCohort getJenkinsCohort() {
		return _jenkinsCohort;
	}

	public String getName() {
		return _name;
	}

	public String getRemoteURL() {
		return _remoteURL;
	}

	public List<JenkinsSlave> getSlaves() {
		return _slaves;
	}

	public List<String> getSlaveIDs() {
		return _slaveIDs;
	}

	public int getOfflineSlaveCount() {
		return _offlineSlaveCount;
	}

	public JenkinsSlave getSlaveByName(String jenkinsSlaveName) {
		for (JenkinsSlave jenkinsSlave : _slaves) {
			if (jenkinsSlaveName.equals(jenkinsSlave.getName())) {
				return jenkinsSlave;
			}
		}

		throw new RuntimeException("Missing cloud server: " + jenkinsSlaveName);
	}

	public void update() {
		_offlineSlaveCount = 0;

		JSONObject computerJSONObject = new JSONObject(CurlUtil.curl(
			_remoteURL + "computer/api/json?" +
				"tree=computer[displayName,offline,offlineCauseReason]"));

		JSONArray computerJSONArray = computerJSONObject.getJSONArray(
			"computer");

		for (int i = 0; i < computerJSONArray.length(); i++) {
			JSONObject nodeJSONObject = computerJSONArray.getJSONObject(i);

			String displayName = nodeJSONObject.getString("displayName");

			if (displayName.startsWith("cloud-")) {
				JenkinsSlave jenkinsSlave = getSlaveByName(displayName);

				boolean isOffline = jenkinsSlave.isOffline();
				String offlineCause = jenkinsSlave.getOfflineCause();

				jenkinsSlave.setOffline(nodeJSONObject.getBoolean("offline"));
				jenkinsSlave.setOfflineCause(nodeJSONObject.getString("offlineCauseReason"));

				if (isOffline != jenkinsSlave.isOffline() || !offlineCause.equals(jenkinsSlave.getOfflineCause())) {
					JenkinsSlave.updateJenkinsSlaveOnDatabase(jenkinsSlave);
				}

				if (jenkinsSlave.isOffline()) {
					_offlineSlaveCount++;
				}
			}
		}
	}

	public static void addJenkinsMaster(JenkinsMaster jenkinsMaster) {
		_jenkinsMasters.add(jenkinsMaster);
	}

	public static void addJenkinsMasterToBeWritten(JenkinsMaster jenkinsMaster) {
		_jenkinsMastersToBeWritten.add(jenkinsMaster);
	}

	public static void updateJenkinsMasterOnDatabase(JenkinsMaster jenkinsMaster) {
		try {
			JSONObject jsonObject = new JSONObject()
				.put("offline_slave_count", jenkinsMaster.getOfflineSlaveCount());

			CollectionUtil.getWeDeploy()
				.data("https://data-cimonitor.lfr.io")
				.update("master/" + jenkinsMaster.getID(), jsonObject)
				.execute();

			//System.out.println(new Timestamp(System.currentTimeMillis()) + " Updating information for master: " + jenkinsMaster.getName());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeJenkinsMastersToDatabase() {
		try {
			JSONArray jsonArray = new JSONArray();

			for (JenkinsMaster jenkinsMaster : _jenkinsMastersToBeWritten) {
				JenkinsCohort jenkinsCohort = jenkinsMaster.getJenkinsCohort();

				JSONObject jsonObject = new JSONObject()
					.put("cohort_id", jenkinsCohort.getID())
					.put("id", jenkinsMaster.getID())
					.put("name", jenkinsMaster.getName())
					.put("offline_slave_count", jenkinsMaster.getOfflineSlaveCount())
					.put("remote_url", jenkinsMaster.getRemoteURL())
					.put("slave_ids", jenkinsMaster.getSlaveIDs());

				jsonArray.put(jsonObject);
			}

			CollectionUtil.getWeDeploy()
				.data("https://data-cimonitor.lfr.io")
				.create("master", jsonArray)
				.execute();

			_jenkinsMastersToBeWritten.clear();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final String _id;
	private final JenkinsCohort _jenkinsCohort;
	private final String _name;
	private int _offlineSlaveCount;
	private final String _remoteURL;
	private final List<JenkinsSlave> _slaves = new ArrayList<>();
	private final List<String> _slaveIDs = new ArrayList<>();

	private static final List<JenkinsMaster> _jenkinsMasters = new ArrayList<>();
	private static final List<JenkinsMaster> _jenkinsMastersToBeWritten = new ArrayList<>();

}