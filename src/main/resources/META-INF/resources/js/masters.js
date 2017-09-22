$.getJSON('/masters', function(data) {
	var jenkinsMasters = data;

	var mastersElement = document.getElementById("masters");

	for (var i in jenkinsMasters) {
		var jenkinsMaster = jenkinsMasters[i];

		var masterHostname = jenkinsMaster.hostname;
		var masterRemoteURL = jenkinsMaster.remote_url;

		var masterAnchorElement = document.createElement("a");
		masterAnchorElement.setAttribute("href", masterRemoteURL);
		masterAnchorElement.innerHTML = masterHostname;

		var masterListItemElement = document.createElement("li");
		masterListItemElement.appendChild(masterAnchorElement)

		mastersElement.appendChild(masterListItemElement);
	}
});