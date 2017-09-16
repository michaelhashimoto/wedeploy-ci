package io.wedeploy.ci;

import io.wedeploy.ci.jenkins.Master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@EnableAutoConfiguration
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

	@RequestMapping("/master")
	public String masters() {
		Master master = new Master("test-1-0");

		return master.toJSONString();
	}

}