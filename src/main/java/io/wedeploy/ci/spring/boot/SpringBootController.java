package io.wedeploy.ci.spring.boot;

import java.net.URL;

import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SpringBootController {

	@GetMapping("/base")
	public String base() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("name", "value");

		return jsonObject.toString();
	}

	@GetMapping("/masters")
	public String masters() {
		JSONArray mastersJSONArray = new JSONArray();

		String[] masterNames = new String[] {
			"test-1-1", "test-1-2", "test-1-3"
		};

		for (String masterName : masterNames) {
			JSONObject masterJSONObject = new JSONObject();

			masterJSONObject.put("masterName", masterName);

			mastersJSONArray.put(masterJSONObject);
		}

		return mastersJSONArray.toString();
	}

}