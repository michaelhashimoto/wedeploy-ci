package io.wedeploy.ci.spring.boot;

import io.wedeploy.ci.jenkins.node.JenkinsMasters;
import io.wedeploy.ci.util.EnvironmentUtil;

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

	@GetMapping("/masters")
	public String masters() {
		JenkinsMasters jenkinsMasters = new JenkinsMasters();

		return jenkinsMasters.toString();
	}

}