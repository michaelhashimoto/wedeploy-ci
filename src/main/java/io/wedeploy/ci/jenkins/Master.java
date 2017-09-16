package io.wedeploy.ci.jenkins;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class Master {

	public Master(String masterName) {
		_masterName = masterName;
	}

	public String toJSONString() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("masterName", _masterName);

		return jsonObject.toString();
	}

	private String _masterName;
}