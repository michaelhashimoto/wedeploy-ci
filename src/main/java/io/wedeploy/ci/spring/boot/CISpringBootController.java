package io.wedeploy.ci.spring.boot;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.transport.Response;

import io.wedeploy.ci.jenkins.node.JenkinsMasters;
import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CISpringBootController {

	public CISpringBootController() throws IOException {
		_jenkinsMasters = new JenkinsMasters();
	}

	@GetMapping("/masters")
	public String masters() throws IOException {
		return _jenkinsMasters.toString();
	}

	@GetMapping("/api")
	public String restAPI() throws WeDeployException {
		WeDeploy weDeploy = new WeDeploy.Builder().build();

		/* Adding data */

		JSONObject movie1JsonObject = new JSONObject()
			.put("title", "Star Wars III")
			.put("year", 2005)
			.put("rating", 8.0);

		JSONObject movie2JsonObject = new JSONObject()
			.put("title", "Star Wars II")
			.put("year", 2002)
			.put("rating", 8.6);

		JSONArray moviesJsonArray = new JSONArray()
			.put(movie1JsonObject)
			.put(movie2JsonObject);

		Response response = weDeploy
			.data("https://data-ci.wedeploy.io")
			.create("movies", moviesJsonArray)
			.execute();

		System.out.println(response);

		/* Retrieving data */

		response = weDeploy
			.data("https://data-ci.wedeploy.io")
			.get("movies")
			.execute();

		System.out.println(response);

		/* Deleting data */

		response = weDeploy
			.data("https://data-ci.wedeploy.io")
			.delete("movies")
			.execute();

		System.out.println(response);

		return response.toString();
	}

	private JenkinsMasters _jenkinsMasters;

}