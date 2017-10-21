package io.wedeploy.ci.spring.boot;

import com.commsen.wedeploy.client.WeDeployClient;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.CollectionDTO;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;
import com.commsen.wedeploy.client.data.WeDeployDataService;
import com.commsen.wedeploy.client.data.WeDeployDataStorage;

import com.wedeploy.android.WeDeploy;
import com.wedeploy.android.WeDeploy.Builder;
import com.wedeploy.android.exception.WeDeployException;
import com.wedeploy.android.query.Query;
import com.wedeploy.android.query.SortOrder;
import com.wedeploy.android.query.filter.Filter;
import com.wedeploy.android.transport.Response;

import io.wedeploy.ci.jenkins.JenkinsCohort;
import io.wedeploy.ci.jenkins.JenkinsLegion;
import io.wedeploy.ci.jenkins.JenkinsMaster;
import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class CISpringBootRestController {

	@GetMapping("/counts")
	public String counts() throws Exception {
		JSONObject jsonObject = new JSONObject();

		JenkinsLegion jenkinsLegion = JenkinsLegion.getJenkinsLegion();

		List<JenkinsCohort> jenkinsCohorts = jenkinsLegion.getCohorts();

		int totalOfflineSlaveCount = 0;

		for (JenkinsCohort jenkinsCohort : jenkinsCohorts) {
			List<JenkinsMaster> jenkinsMasters = jenkinsCohort.getMasters();

			for (JenkinsMaster jenkinsMaster : jenkinsMasters) {
				jsonObject.put(jenkinsMaster.getName(), jenkinsMaster.getOfflineSlaveCount());
			}

			totalOfflineSlaveCount += jenkinsCohort.getOfflineSlaveCount();
		}

		jsonObject.put("offline_slave_count", totalOfflineSlaveCount);

		return jsonObject.toString();
	}

}