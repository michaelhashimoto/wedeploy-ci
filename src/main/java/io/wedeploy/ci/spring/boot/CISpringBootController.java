package io.wedeploy.ci.spring.boot;

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

	private JenkinsMasters _jenkinsMasters;

}