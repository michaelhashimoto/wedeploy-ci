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

@Controller
@EnableAutoConfiguration
@RestController
public class WeDeployCIController {

	public WeDeployCIController() {
	}

	public static void main(String[] args) {
		SpringApplication.run(WeDeployCIController.class, args);
	}

	@RequestMapping("/")
	public ModelAndView hello() {
		return new ModelAndView("layout");
	}

	@GetMapping("/master")
	public String masters() {
		Master master = new Master("test-1-0");

		return master.toJSONString();
	}

}