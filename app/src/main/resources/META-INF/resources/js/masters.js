$.getJSON('/masters', function(data) {
	var jenkinsMasters = data.masters;

	var mastersElement = document.getElementById("masters");

	for (var i in jenkinsMasters) {
		var jenkinsMaster = jenkinsMasters[i];

		var masterHostname = jenkinsMaster.hostname;
		var masterRemoteURL = jenkinsMaster.remote_url;
		var masterSlaves = jenkinsMaster.slaves;

		var masterHeaderElement = document.createElement("h2");

		var masterAnchorElement = document.createElement("a");
		masterAnchorElement.setAttribute("href", masterRemoteURL);
		masterAnchorElement.innerHTML = masterHostname;

		masterHeaderElement.appendChild(masterAnchorElement);

		var slavesOffline = false;

		var masterULElement = document.createElement("ul");

		for (var j in masterSlaves) {
			var jenkinsSlave = masterSlaves[j];

			var slaveHostname = jenkinsSlave.hostname;
			var slaveRemoteURL = jenkinsSlave.remote_url;
			var slaveOffline = jenkinsSlave.offline;
			var slaveOfflineCause = jenkinsSlave.offline_cause;

			if (slaveOffline) {
				var slaveListItemElement = document.createElement("li");

				var spanElement1 = document.createElement("span");
				var spanElement2 = document.createElement("span");

				var slaveAnchorElement = document.createElement("a");
				slaveAnchorElement.setAttribute("href", slaveRemoteURL);
				slaveAnchorElement.innerHTML = slaveHostname;

				slaveListItemElement.appendChild(spanElement1);
				slaveListItemElement.appendChild(spanElement2);

				spanElement1.appendChild(slaveAnchorElement);

				spanElement2.innerHTML = "<br /><pre>" + slaveOfflineCause + "</pre>";

				masterULElement.appendChild(slaveListItemElement);

				slavesOffline = true;
			}
		}

		var masterItemElement = document.createElement("div");
		masterItemElement.setAttribute("class", masterHostname);

		if (slavesOffline) {
			masterItemElement.appendChild(masterHeaderElement);
			masterItemElement.appendChild(masterULElement);

			mastersElement.appendChild(masterItemElement);
		}
	}

	var offlineSlaveCountElement = document.getElementById("offline_slave_count");

	offlineSlaveCountElement.innerHTML = data.offline_slave_count;
});