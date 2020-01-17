function updateSrvc() {
  const scope = {};
  const UPDATE_OBJ = {};

  const WAITING = '_waiting';
  const GETTER = '_getter';
  const SUCCESS = '_success';
  const FAILURE = '_failure';
  const ON_SUCCESS = `_on_${SUCCESS}`;
  const ON_FAILURE = `_on_${FAILURE}`;
  const RETRIES = '_retries';
  const TRIES = '_tried';
  const FILTER = '_filter'

  function encodeHtml(data) {
    data = FILTERS.xhr(data);
    var elem = document.createElement('p');
    elem.innerText = data;
    return elem.innerHTML;
  }

  function xhrJson(data) {
    data = FILTERS.xhr(data);
    return JSON.parse(data);
  }

  const FILTERS = {
    none: function (data) {return data},
    xhr: function (data) {return data.currentTarget.response;},
    xhrJson: xhrJson,
    text: encodeHtml
  }

  function runFuncs(type, id, data, onType) {
    const funcs = UPDATE_OBJ[type][id][onType];
    for (let index = 0; index < funcs.length; index += 1) {
      funcs[index](data, id);
    }
  }

  function setData(data) {

  }

  function callbacks(type) {
    const parentArgs = arguments;
    function success(data) {
      for (let i = 1; i < parentArgs.length; i += 1) {
        const id = parentArgs[i];
        if (id !== undefined) {
          init(type, id); // eslint-disable-line
          const typeObj = UPDATE_OBJ[type];
          const updateObj = typeObj[id];
          updateObj[TRIES] = 0;
          updateObj.lastRefresh = new Date().getTime();
          updateObj.data = !typeObj[FILTER] ?
              FILTERS.xhrJson(data) :
              typeObj[FILTER](data);
          runFuncs(type, id, updateObj.data, ON_SUCCESS);
        }
      }
    }

    function failure(e) {
      for (let i = 1; i < parentArgs.length; i += 1) {
        const id = parentArgs[i];
        if (id !== undefined) {
          init(type, id); // eslint-disable-line
          if (UPDATE_OBJ[type][id][TRIES] < UPDATE_OBJ[type][id][RETRIES]) {
            run(type, id, true); // eslint-disable-line
          } else {
            runFuncs(type, id, e, ON_FAILURE);
          }
        }
      }
    }

    return { success, failure };
  }

  function init(type, id) {
    if (UPDATE_OBJ[type] === undefined) {
      UPDATE_OBJ[type] = {};
      UPDATE_OBJ[type][WAITING] = [];
    }

    const typeObj = UPDATE_OBJ[type];
    if (id !== undefined && typeObj[id] === undefined) {
      typeObj[id] = {};
      typeObj[id][ON_SUCCESS] = [];
      typeObj[id][ON_FAILURE] = [];
      typeObj[id][TRIES] = 0;
    }
    if (id !== undefined && typeObj[id][SUCCESS] === undefined) {
      const funcs = callbacks(type, id);
      typeObj[id][SUCCESS] = funcs.success;
      typeObj[id][FAILURE] = funcs.failure;
    }
  }

  function parseRefresh(refreshVal) {
    if (refreshVal !== undefined && String(refreshVal).match(/^[0-9]*$/)) {
      return Number.parseInt(refreshVal, 10) * 1000;
    }
    return Number.MAX_VALUE;
  }

  function run(type, id, shouldRefresh) {
    init(type, id);
    const typeObj = UPDATE_OBJ[type];

    if (!typeObj[id].called || shouldRefresh) {
      typeObj[id].called = true;
      typeObj[GETTER](id, typeObj[id][SUCCESS], typeObj[id][FAILURE]);
      UPDATE_OBJ[type][id][TRIES] += 1;
    }
  }

  function on(type, id, func, refreshVal) {
    if ((typeof func) !== 'function' || id === undefined) {
      return;
    }
    const refreshInt = parseRefresh(refreshVal);
    init(type, id);
    const typeObj = UPDATE_OBJ[type];
    if (typeObj[id][ON_SUCCESS].indexOf(func) === -1) {
      typeObj[id][ON_SUCCESS].push(func);
    }
    const data = typeObj[id].data;
    const notTried = UPDATE_OBJ[type][id][TRIES] === 0;
    const shouldRefresh = new Date().getTime() - refreshInt > typeObj[id].lastRefresh;
    const getterIsFunction = (typeof typeObj[GETTER]) === 'function';
    if (data !== undefined) {
      func(data);
      if (shouldRefresh && getterIsFunction) {
        run(type, id, true);
      }
    } else if (notTried && getterIsFunction) {
      run(type, id);
    } else if (typeObj[WAITING].indexOf(id) === -1) {
      typeObj[WAITING].push(id);
    }
  }

  function onFail(type, id, func) {
    if ((typeof func) !== 'function') {
      return;
    }
    init(type, id);
    const typeObj = UPDATE_OBJ[type];
    if (typeObj[id][ON_FAILURE].indexOf(func) === -1) {
      typeObj[id][ON_FAILURE].push(func);
    }
    const data = typeObj[id].data;
    const notTried = UPDATE_OBJ[type][id][TRIES] === 0;
    const getterIsFunction = (typeof typeObj[GETTER]) === 'function';
    if (data !== undefined) {
      return; // eslint-disable-line
    } else if (notTried && getterIsFunction) {
      run(type, id);
    } else if (notTried && typeObj[WAITING].indexOf(id) === -1) {
      typeObj[WAITING].push(id);
    }
  }

  function refresh(type, id, timeout) {
    function runRefresh() {
      run(type, id, true);
    }
    $timeout(runRefresh, timeout);
  }

  function runWaiting(type) {
    init(type);
    const typeObj = UPDATE_OBJ[type];
    for (let i = 0; i < typeObj[WAITING].length; i += 1) {
      const id = typeObj[WAITING][i];
      init(type, id);
      run(type, id);
    }
  }

  function config(type, getter, filter, retries) {
      if (UPDATE_OBJ[type] !== undefined &&
            UPDATE_OBJ[type][GETTER] !== undefined &&
            UPDATE_OBJ[type][GETTER] !== getter) {
        throw new Error('Config Type "' + type + '" already cofigured');
      } else {
        configOverwite(type, getter, filter, retries);
      }
  }

  function configOverwite(type, getter, filter, retries) {
    init(type);
    const typeObj = UPDATE_OBJ[type];
    typeObj[GETTER] = getter;
    UPDATE_OBJ[type][FILTER] = filter;
    UPDATE_OBJ[type][RETRIES] = retries || 0;

    runWaiting(type);
  }

  function set(type, id, data) {
    init(type, id);
    UPDATE_OBJ[type][id].data = data;
    runFuncs(type, id, data, ON_SUCCESS);
  }

  function setAll(type, idAttr, list) {
    for (let index = 0; index < list.length; index += 1) {
      const data = list[index];
      const id = data[idAttr];
      set(type, id);
    }
  }

  function getRawUrl(url, success, failure) {
    var xhr = new XMLHttpRequest();
    xhr.onload = success;
    xhr.onerror = failure
    xhr.open('GET', url, true);
    xhr.send();
  }

  config('URL-RAW', getRawUrl);
  config('URL-JSON', getRawUrl, xhrJson);
  config('URL-TEXT', getRawUrl, encodeHtml);

  scope.on = on;
  scope.onFail = onFail;
  scope.config = config;
  scope.configOverwite = configOverwite;
  scope.refresh = refresh;
  scope.set = set;
  scope.setAll = setAll;
  scope.callbacks = callbacks;
  return scope;
}

updateSrvc = updateSrvc();
