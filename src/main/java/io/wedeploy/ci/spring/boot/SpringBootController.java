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

	@GetMapping("/")
	public String masters() {
		JSONObject jsonObject = new JSONObject();

		jsonObject.put("name", "value");

		return jsonObject.toString();
	}

}