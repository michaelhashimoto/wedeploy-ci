$.getJSON('/masters', function(data) {
	alert(data);

	var jenkinsMasters = data.masters;

	var mastersElement = document.getElementById("masters");

	for (var i in jenkinsMasters) {
		var jenkinsMaster = jenkinsMasters[i];

		var masterHostname = jenkinsMaster.hostname;
		var masterRemoteURL = jenkinsMaster.remote_url;
		var masterSlaves = jenkinsMaster.slaves;

		var masterAnchorElement = document.createElement("a");
		masterAnchorElement.setAttribute("href", masterRemoteURL);
		masterAnchorElement.innerHTML = masterHostname;

		var masterULElement = document.createElement("ul");

		for (var j in masterSlaves) {
			var slaveListItemElement = document.createElement("li");

			var jenkinsSlave = masterSlaves[j];

			var slaveHostname = jenkinsSlave.hostname;
			var slaveRemoteURL = jenkinsSlave.remote_url;

			var slaveAnchorElement = document.createElement("a");
			slaveAnchorElement.setAttribute("href", slaveRemoteURL);
			slaveAnchorElement.innerHTML = slaveHostname;

			slaveListItemElement.appendChild(slaveAnchorElement);

			masterULElement.appendChild(slaveListItemElement);
		}

		var masterListItemElement = document.createElement("li");
		masterListItemElement.appendChild(masterAnchorElement);
		masterListItemElement.appendChild(masterULElement);

		mastersElement.appendChild(masterListItemElement);
	}
});