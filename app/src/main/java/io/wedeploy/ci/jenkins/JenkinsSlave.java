package io.wedeploy.ci.jenkins;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

public class JenkinsSlave {

	public JenkinsSlave(
			JenkinsMaster jenkinsMaster, String name, boolean offline,
			String offlineCause)
		throws Exception {

		_id = String.valueOf(CollectionUtil.getUniqueTimestamp());
		_jenkinsMaster = jenkinsMaster;
		_name = name;
		_offline = offline;
		_offlineCause = offlineCause;
		_remoteURL = "https://" + _jenkinsMaster.getName() + ".liferay.com/computer/" + name + "/";
	}

	public JenkinsSlave(JenkinsMaster jenkinsMaster, JSONObject jsonObject)
		throws Exception {

		_id = jsonObject.getString("id");
		_jenkinsMaster = jenkinsMaster;
		_name = jsonObject.getString("name");
		_offline = jsonObject.getBoolean("offline");
		_offlineCause = jsonObject.getString("offline_cause");
		_remoteURL = jsonObject.getString("remote_url");
	}

	public String getID() {
		return _id;
	}

	public JenkinsMaster getJenkinsMaster() {
		return _jenkinsMaster;
	}

	public String getName() {
		return _name;
	}

	public String getOfflineCause() {
		return _offlineCause;
	}

	public boolean isOffline() {
		return _offline;
	}

	public String getRemoteURL() {
		return _remoteURL;
	}

	public void setOffline(boolean offline) {
		_offline = offline;
	}

	public void setOfflineCause(String offlineCause) {
		_offlineCause = offlineCause;
	}

	public static void addJenkinsSlave(JenkinsSlave jenkinsSlave) {
		_jenkinsSlaves.add(jenkinsSlave);
	}

	public static void addJenkinsSlaveToBeWritten(JenkinsSlave jenkinsSlave) {
		_jenkinsSlavesToBeWritten.add(jenkinsSlave);
	}

	public static void updateJenkinsSlaveOnDatabase(JenkinsSlave jenkinsSlave) {
		try {
			JSONObject jsonObject = new JSONObject()
				.put("offline", jenkinsSlave.isOffline())
				.put("offline_cause", jenkinsSlave.getOfflineCause());

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.lfr.io")
				.update("slave/" + jenkinsSlave.getID(), jsonObject)
				.execute();

			//System.out.println(new Timestamp(System.currentTimeMillis()) + " Updating information for slave: " + jenkinsSlave.getName());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void writeJenkinsSlavesToDatabase() {
		try {
			JSONArray jsonArray = new JSONArray();

			for (JenkinsSlave jenkinsSlave : _jenkinsSlavesToBeWritten) {
				JenkinsMaster jenkinsMaster = jenkinsSlave.getJenkinsMaster();

				JSONObject jsonObject = new JSONObject()
					.put("id", jenkinsSlave.getID())
					.put("master_id", jenkinsMaster.getID())
					.put("name", jenkinsSlave.getName())
					.put("offline", jenkinsSlave.isOffline())
					.put("offline_cause", jenkinsSlave.getOfflineCause())
					.put("remote_url", jenkinsSlave.getRemoteURL());

				jsonArray.put(jsonObject);
			}

			CollectionUtil.getWeDeploy()
				.data("https://data-ci.lfr.io")
				.create("slave", jsonArray)
				.execute();

			_jenkinsSlavesToBeWritten.clear();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final String _id;
	private final JenkinsMaster _jenkinsMaster;
	private final String _name;
	private boolean _offline;
	private String _offlineCause;
	private final String _remoteURL;

	private static final List<JenkinsSlave> _jenkinsSlaves = new ArrayList<>();
	private static final List<JenkinsSlave> _jenkinsSlavesToBeWritten = new ArrayList<>();

}