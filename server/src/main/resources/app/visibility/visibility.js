


function onLoad() {
	var userInfo = document.querySelector('user-info');
	if (userInfo) {
		var user = {};
		var email = userInfo.getAttribute('email');
		var deviceId = userInfo.getAttribute('deviceIdentifier');
		var token = userInfo.getAttribute('token');
		UUserSrvc.setCredentials(email, token, deviceId);
	}
	
	var object = JSON.parse(document.getElementById('object').innerText);
	
	console.log(object);
}

window.onload = onLoad;
