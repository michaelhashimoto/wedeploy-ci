package io.wedeploy.ci.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.codec.binary.Base64;

public class CurlUtil {

	public static String curl(String curl) {
		try {
			URL url = new URL(curl);

			URLConnection urlConnection = url.openConnection();

			urlConnection.setRequestProperty("X-Requested-With", "Curl");

			String userpass = _JENKINS_USERNAME + ":" + _JENKINS_PASSWORD;
			String basicAuth =
				"Basic " + new String(new Base64().encode(userpass.getBytes()));

			urlConnection.setRequestProperty("Authorization", basicAuth);

			InputStreamReader inputStreamReader = new InputStreamReader(
				urlConnection.getInputStream());

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line = null;

			StringBuilder sb = new StringBuilder();

			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}

			bufferedReader.close();

			return sb.toString();
		}
		catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private final static String _JENKINS_PASSWORD = EnvironmentUtil.get(
		"JENKINS_PASSWORD");
	private final static String _JENKINS_USERNAME = EnvironmentUtil.get(
		"JENKINS_USERNAME");
}