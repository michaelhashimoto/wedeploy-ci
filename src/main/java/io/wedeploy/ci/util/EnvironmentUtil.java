package io.wedeploy.ci.util;

import java.net.URL;

import java.util.Map;

public class EnvironmentUtil {

	public static String get(String key) {
		if (_env.containsKey(key)) {
			return _env.get(key);
		}

		return null;
	}

	private static Map<String, String> _env = System.getenv();

}