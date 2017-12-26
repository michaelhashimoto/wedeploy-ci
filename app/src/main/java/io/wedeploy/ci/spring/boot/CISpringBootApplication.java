package io.wedeploy.ci.spring.boot;

import io.wedeploy.ci.jenkins.JenkinsLegion;
import io.wedeploy.ci.jenkins.JenkinsUpdaterJob;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import org.quartz.CronExpression;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

@SpringBootApplication
public class CISpringBootApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CISpringBootApplication.class);
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(CISpringBootApplication.class, args);

		JenkinsLegion jenkinsLegion = JenkinsLegion.getJenkinsLegion();

		JenkinsUpdaterJob.start();
	}

}