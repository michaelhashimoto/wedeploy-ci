$.getJSON('/masters', function(data) {
	for (var i in data) {
		var masterName = data[i].masterName;

		var masterElement = document.createElement("li");
		var mastersElement = document.getElementById("masters");
		var slavesElement = document.createElement("ul");

		masterElement.innerHTML = masterName;

		mastersElement.appendChild(masterElement);
	}
});