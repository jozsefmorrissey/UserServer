var config = {
		mainApp: {},
		childApps: [],
		rules: []
}

var clientEndpoint = {
		
};

var state = { config, clientEndpoint };

function buildClientAppDisplay() {
	var childHtml = '';
	for (let index = 0; index < config.childApps.length; index += 1) {
		var child = config.childApps[index];
		childHtml += `<div class='row'>
	        <div class='col-2'></div>
	        <div class='col-4'>
	          <input class='form-control' type='text' placeholder='name' value='${child.name || ""}'
	  					onchange='config.childApps[${index}].name = this.value; updateRaw();'>
	        </div>
   			<div class='col-2'>
				<input type='number' class='form-control number-input' id='token-expiration' placeholder='Minutes' onchange='config.childApps[${index}].tokenExpiration = Number.parseInt(this.value); updateRaw();'>
			</div>
	        <div class='col-4'
					onclick='this.children[0].disabled = !this.children[0].disabled; config.childApps[${index}].newKey = !this.children[0].disabled; display();'>
				<input class='btn btn-primary new-key-btn' value='Create New Key' ${!config.childApps[index].newKey && 'disabled'}>
				<input type='button' value='X' class='btn btn-primary remove-btn'
							onclick='config.childApps.splice(${index}, 1); display();'>
	        </div>
	      </div>`;
	}
	document.getElementById('children').innerHTML = childHtml;
}

var appConfigs = [{mainApp: {name: 'tender'}, childApps: [], rules: []}, 
		{mainApp: {name: 'youtube'}, childApps: [], rules: []}, 
		{mainApp: {name: 'pornhub'}, childApps: [], rules: []}];
var currApp = 0;
function buildNavBar() {
	var navHtml = '';
	for (let index = 0; index < appConfigs.length; index += 1) {
		var app = appConfigs[index].mainApp;
		navHtml += `<li class="nav-item">
			    <a class="nav-link${(index == currApp || '') && ' active'}" 
			    data-toggle="pill" href="#home"
			    onclick='currApp = ${index}; display();'>${app.name || 'Not Named'}</a>
			  </li>`;
	}
	navHtml += `<li class="nav-item">
	    <a class="nav-link" 
	    data-toggle="pill" href="#home"
	    onclick='appConfigs.push({mainApp: {}, childApps: [], rules: []}); display();'>+</a>
	  </li>`;
	document.getElementById('app-nav').innerHTML = navHtml;
}

function buildEndpointRuleDisplay() {
	var rulesHtml = '';
	for (let index = 0; index < config.rules.length; index += 1) {
		var rule = config.rules[index];
		rulesHtml += `<div  class='row'>
				<div class='col-2'></div>
				<div class='col-4'>
					<input class='form-control' type='text' placeholder='App name' value='${rule.name || ""}'
						onchange='config.rules[${index}].name = this.value; updateRaw();'>
				</div>
				<div class='col-4'>
					<input class='form-control' type='text' placeholder='Endpoint Regular Expression' value='${rule.value || ""}'
						onchange='config.rules[${index}].value = this.value; updateRaw();'>
				</div>
				<div class='col-2'><input type='button' value='X' class='btn btn-primary remove-btn'
						onclick='config.rules.splice(${index}, 1); display();'></div>
			</div>`;
	}
	document.getElementById('rules').innerHTML = rulesHtml;
	updateRaw();
}

function display() {
	config = appConfigs[currApp];
	document.getElementById('mainId').value = config.mainApp.name || '';
	document.getElementById('mainAccessKeyBtn').disabled = !config.mainApp.newKey;
	document.getElementById('token-expiration').value = config.mainApp.tokenExpiration || 0;
	
	buildClientAppDisplay();
	buildEndpointRuleDisplay();
	buildNavBar();
}

function updateRaw() {
	document.getElementById('raw-json').value = JSON.stringify(config, null, 2);
}

function rawChange() {
	try {
		config = JSON.parse(document.getElementById('raw-json').value);
		config.mainApp = config.mainApp || {};
		config.childApps = config.childApps || [];
		config.rules = config.rules || [];
		display();
	} catch (e) {
		
	}
}

window.onload = display;




