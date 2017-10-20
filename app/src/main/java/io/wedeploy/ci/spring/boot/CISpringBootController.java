package io.wedeploy.ci.spring.boot;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import io.wedeploy.ci.jenkins.node.JenkinsMasters;
import io.wedeploy.ci.jenkins.node.JenkinsMastersImpl;

@Controller
public class CISpringBootController {

	@RequestMapping("/")
	public String index(Map<String, Object> model) throws IOException {
		JenkinsMasters jenkinsMasters = new JenkinsMastersImpl();

		model.put("jenkinsMasters", jenkinsMasters);

		return "index";
	}

}