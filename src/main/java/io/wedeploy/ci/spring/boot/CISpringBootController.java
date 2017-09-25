package io.wedeploy.ci.spring.boot;

import com.commsen.wedeploy.client.WeDeployClient;
import com.commsen.wedeploy.client.WeDeployClientException;
import com.commsen.wedeploy.client.data.CollectionDTO;
import com.commsen.wedeploy.client.data.WeDeployDataCollection;
import com.commsen.wedeploy.client.data.WeDeployDataDocument;
import com.commsen.wedeploy.client.data.WeDeployDataService;
import com.commsen.wedeploy.client.data.WeDeployDataStorage;

import io.wedeploy.ci.jenkins.node.JenkinsMasters;
import io.wedeploy.ci.util.EnvironmentUtil;

import java.io.IOException;

import java.util.Collection;
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

	@GetMapping("/api")
	public String restAPI() throws WeDeployClientException {
		WeDeployClient weDeploy = new WeDeployClient();

		WeDeployDataService weDeployDataService = weDeploy.data();

		WeDeployDataStorage weDeployDataStorage = weDeployDataService.connect(
			"ci", "data");

		CollectionDTO moviesCollectionDTO = CollectionDTO.from("movies");

		if (!weDeployDataStorage.collectionExists("movies")) {
			weDeployDataStorage.createCollection(moviesCollectionDTO);

			WeDeployDataCollection weDeployDataCollection =
				weDeployDataStorage.collection("movies");

			JSONObject jsonObject = new JSONObject();

			jsonObject.put("title", "starwars");
			jsonObject.put("rating", 9.8);

			WeDeployDataDocument<JSONObject> weDeployDataDocument =
				new WeDeployDataDocument<JSONObject>("movie-1", jsonObject);

			System.out.println(weDeployDataDocument.getId());
			System.out.println(weDeployDataDocument.getObject());

			weDeployDataCollection.save(weDeployDataDocument);

			return "Created a 'movies' collection";
		}

		weDeployDataStorage.deleteCollections(moviesCollectionDTO);

		return "Deleted a 'movies' collection";
	}

	private JenkinsMasters _jenkinsMasters;

}