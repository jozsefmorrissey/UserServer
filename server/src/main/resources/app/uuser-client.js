var UUserSrvcCount = 0;
function UuserClient(cookieId, APP_ID, appPassword, userSrvcRoot) {
  const scope = {};
  const ASSOCS = {};
  const UPDATE = 'USER_SRVC_UPDATE' + UUserSrvcCount;
  const UPDATED = 'USER_SRVC_UPDATED' + UUserSrvcCount++;
  const CURRENT_USER = 'CURRENT_USER';
  const USER_SERVICE_ROOT = userSrvcRoot || '';

  let user;
  let APP_TOKEN;
  let TOKEN;
  let DEVICE_IDENTIFIER;

  let HEADERS;
  let getUrl;

  function setCredentials(email, token, deviceId) {
	  user = user || {};
	  user.email = email;
	  TOKEN = token;
	  DEVICE_IDENTIFIER = deviceId;
  }
  
  function getCookie(name) {
    return document.cookie
        .replace(new RegExp("(.*?; |^)" + name + "=(.*?)(;.*|$)"), '$2');
  }

  function getRequestHeaders(method, url, email, token, password) {
    var xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    if (user === undefined) {
      user = {};
    }
    xhr.setRequestHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, PATCH, OPTIONS');
    xhr.setRequestHeader('Access-Control-Allow-Headers', 'X-Requested-With, content-type, Authorization');
    xhr.setRequestHeader('Access-Control-Allow-Origin', '*');
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader(HEADERS.PASSWORD.value, password);
    xhr.setRequestHeader(HEADERS.DEVICE.IDENTIFIER.value, DEVICE_IDENTIFIER);
    xhr.setRequestHeader(HEADERS.APP.ID.value, APP_ID);
    if (APP_TOKEN === undefined) {
      xhr.setRequestHeader(HEADERS.APP.PASSWORD.value, appPassword);
    } else {
      xhr.setRequestHeader(HEADERS.APP.TOKEN.value, APP_TOKEN);
    }

    if (email === undefined) {
      xhr.setRequestHeader(HEADERS.EMAIL.value, user.email);
      xhr.setRequestHeader(HEADERS.TOKEN.value, TOKEN);
    } else {
      xhr.setRequestHeader(HEADERS.EMAIL.value, email);
      xhr.setRequestHeader(HEADERS.TOKEN.value, token);
    }
    return xhr;
  }

  function onUpdate(func, id) {
    if (id === undefined) {
      updateSrvc.on(UPDATE, CURRENT_USER, func);
    } else {
      updateSrvc.on(UPDATE, id, func);
    }
  }

  function onStateChange(success) {
    if (this.readyState != 4) return;

    if (this.status > 199 && this.status < 300) {
      var data = JSON.parse(this.responseText);
      if (onSuccess) {
        success(data);
      }
    }
  };

  function request(method, url, success, failure, email, token, password) {
    var xhr = getRequestHeaders(method, url, email, token, password);
    xhr.onload = success;
    xhr.onerror = failure;
    return xhr;
  }

  function getUser() {
    if (user) {
      return user;
    }

    try {
      user = JSON.parse(getCookie(cookieId));
      TOKEN = user.token.token;
      DEVICE_IDENTIFIER = user.token.id.deviceIdentifier;
    } catch (e) {
      // TODO: implement logging....
    }
    return user;
  }

  function setUser(u) {
    user = u;
    const cookieData = { email: u.email || '', token: u.token};
    document.cookie = `${cookieId}=${JSON.stringify(cookieData)}`;
  }

  function failedToLogin() {
    function onFail(e) {
      document.dispatchEvent(new CustomEvent("UUser-Logout", {detail: e}));
    }
    return onFail;
  }

  function initialLogin(success, failure) {
    getUser();
    if (user) {
      let url = `${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.AUTHINTICATE.value}`;
      request('GET', url, success, failedToLogin(failure)).send();
    } else {
      failedToLogin(failure)();
    }
  }

  function reqUser(id, success, failure) {
    if (id === CURRENT_USER) {
      initialLogin(success, failure);
    } else {
      const url = getUrl.replace('{emailOid}', id);
      request('GET', url, success, failure).send();
    }
  }

  function updateHeaders(data) {
    HEADERS = data;
    updateSrvc.config(UPDATE, reqUser);
    onUpdate(setUser);
  }

  function updateConfig(data) {
    updateSrvc.on('URL-JSON', `${USER_SERVICE_ROOT}${data.CONST.AUTH.value}`, updateHeaders);
    getUrl = `${USER_SERVICE_ROOT}${data.USER.EMAIL.OR.ID.value}`;
    SRVC_CONFIG = {
      host: USER_SERVICE_ROOT,
      endpoints: data,
    };
  }

  function isLoggedIn() {
    const currUser = getUser();
    return currUser !== undefined && Object.keys(currUser).length > 0;
  }

  function logout() {
    setUser({});
    failedToLogin()();
  }

  function register(success, failure, data) {
    let  url = `${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.ADD.value}`;
    request('POST', url, success, failedToLogin(failure)).send(data);
  }

  function setUpdate(body) {
    updateSrvc.set(UPDATE, CURRENT_USER, body.data);
  }

  function successfulLogin(data) {
    const user = JSON.parse(data.currentTarget.response);
    updateSrvc.set(UPDATE, CURRENT_USER, user);
  }

  function login(email, password, success, failure) {
    let url =`${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.LOGIN.value}`;
    request('GET', url, successfulLogin, failedToLogin(failure),
        email, undefined, password).send();
    updateSrvc.on(UPDATE, CURRENT_USER, setUser);
  }

  function reset(success, failure, data, email) {
    let url = `${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.RESET.PASSWORD.value}`;
    request('POST', url, success, failedToLogin(failure), email).send(data);
  }

  function updatePassword(success, failure, data) {
    url: `${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.UPDATE.PASSWORD.value}`,
    request('GET', url, success, failedToLogin(failure),
        data.email, data.token, data.password).send();
  }

  function reloadPage(newUser) {
    setUser(newUser);
    window.location.reload();
  }

  function update(data) {
    const url = `${SRVC_CONFIG.host}${SRVC_CONFIG.endpoints.USER.UPDATE.value}`;
    const funcs = updateSrvc.callbacks(UPDATED, CURRENT_USER);
    request('POST', url, funcs.success, funcs.failure,
        user.email, user.token, data.password).send(data);
    updateSrvc.on(UPDATED, CURRENT_USER, reloadPage);
  }

  function checkLoginStatus() {
    getUser();
    if (user === undefined || Object.keys(user).length === 0 || !user.token) {
      failedToLogin()({ message: 'User is undefined.' });
    }
  }

  updateSrvc.on('URL-JSON',USER_SERVICE_ROOT + '/const/endpoints', updateConfig);
  onUpdate(setUser, CURRENT_USER);

  scope.setCredentials = setCredentials;
  scope.register = register;
  scope.login = login;
  scope.reset = reset;
  scope.setUser = setUser;
  scope.getUser = getUser;
  scope.isLoggedIn = isLoggedIn;
  scope.logout = logout;
  scope.onUpdate = onUpdate;
  scope.update = update;
  scope.request = request;
  scope.updatePassword = updatePassword;
  return scope;
}
