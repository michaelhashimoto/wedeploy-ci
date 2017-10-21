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

public class CollectionUtil {

	public static JSONArray getCohortDatabaseJSONArray() {
		return _cohortDatabaseJSONArray;
	}

	public static JSONArray getLegionDatabaseJSONArray() {
		return _legionDatabaseJSONArray;
	}

	public static JSONArray getMasterDatabaseJSONArray() {
		return _masterDatabaseJSONArray;
	}

	public static JSONArray getSlaveDatabaseJSONArray() {
		return _slaveDatabaseJSONArray;
	}

	public static long getUniqueTimestamp() {
		long current = System.nanoTime();

		while(true) {
			long previous = _atomicLong.get();

			if (previous >= current) {
				current = previous + 1;
			}

			if (_atomicLong.compareAndSet(previous, current)) {
				return current;
			}
		}
	}

	public static WeDeploy getWeDeploy() {
		return _weDeploy;
	}

	private static final AtomicLong _atomicLong = new AtomicLong();
	private static final JSONArray _cohortDatabaseJSONArray;
	private static final JSONArray _legionDatabaseJSONArray;
	private static final JSONArray _masterDatabaseJSONArray;
	private static final JSONArray _slaveDatabaseJSONArray;
	private static final WeDeploy _weDeploy = new WeDeploy.Builder().build();

	static {
		try {
			Response cohortResponse = getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.orderBy("name", SortOrder.ASCENDING)
				.get("cohort")
				.execute();
			_cohortDatabaseJSONArray = new JSONArray(cohortResponse.getBody());

			Response legionResponse = getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.orderBy("name", SortOrder.ASCENDING)
				.get("legion")
				.execute();
			_legionDatabaseJSONArray = new JSONArray(legionResponse.getBody());

			Response masterResponse = getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.orderBy("name", SortOrder.ASCENDING)
				.get("master")
				.execute();
			_masterDatabaseJSONArray = new JSONArray(masterResponse.getBody());

			Response slaveResponse = getWeDeploy()
				.data("https://data-ci.wedeploy.io")
				.orderBy("name", SortOrder.ASCENDING)
				.get("slave")
				.execute();
			_slaveDatabaseJSONArray = new JSONArray(slaveResponse.getBody());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}