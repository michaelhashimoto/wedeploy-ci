package io.wedeploy.ci;

import io.wedeploy.ci.jenkins.Master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
class WeDeployRESTContoller {
	@GetMapping("/master")
	public String master(@RequestParam("name") String name) {
		Master master = new Master(name);

		return master.toJSONString();
	}
}