package com.userSrvc.client.aop;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.userSrvc.client.constant.ToJson;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.AppAccessToken;
import com.userSrvc.client.entities.ConnectionState;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserAccessToken;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.services.AppSrvc;
import com.userSrvc.client.services.UserSrvc;
import com.userSrvc.client.util.DebugGui;

@Aspect
public abstract class  AopAuth <U extends UUserAbs, A extends App> extends ToJson {
	
	public static App backendApp;
	
	public static AopAuth<?,?> instance;
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	AopAuthGates gates;
	
	@Autowired 
	private HttpServletResponse response;
	
	{
		DebugGui.setHost("https://www.jozsefmorrissey.com/debug-gui");
		DebugGui.setRoot("UUsvc");
	}
	
	public AopAuth() {
		AopAuth.instance = this;
	}
	
	public static AopAuth<?,?> getBean() {
		return instance;
	}

	protected abstract U getNewUser();
	protected abstract A getNewApp();
	protected abstract UserSrvc<U> getUserSrvc();
	protected abstract AppSrvc<A> getAppSrvc();
	
	@Pointcut("execution(* com..*.controller..*(..))")
	public void allControllers() {}
	
	public static final String NOT_STARTED = "not started";
	public static final String PROCESSING = "processing";
	public static final String RETURNING = "returning";
		
	private static class RequestState <U extends UUserAbs, A extends App> {
		private U user;
		private A app;
		private String state = NOT_STARTED;
		private ConnectionState connState = new ConnectionState();
		private List<AopSecure> objects = new ArrayList<AopSecure>();
	}

	Logger log = LogManager.getLogger();

	private Long threadCount = 0l;
	private HashMap<Integer, RequestState<U, A>> userInfoMap = new HashMap<Integer, RequestState<U, A>>();
		
	private RequestState<U, A> getState() {
		HttpServletRequest req = getCurrentRequest();
		int requestHash;
		if (req != null) {
			requestHash = getCurrentRequest().hashCode();
		} else {
			requestHash = 0;
		}
		if (userInfoMap.get(requestHash) == null)  {
			userInfoMap.put(requestHash, new RequestState<U, A>());
			threadCount++;
		}
		return userInfoMap.get(requestHash);
	}
	
	public boolean isReturning() {
		if (getState() == null) return true;
		return RETURNING.equals(getState().state);
	}

	public void addObject(AopSecure obj) {
		getState().objects.add(obj);
	}
	
	public static final boolean is() {
		return true;
	}
	
	public U userRequrired() throws AccessDeniedException {
		U user = getCurrentUser();
		if (user != null) {
			return user;
		}
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_USER_CREDENTIALS);
	}
	
	public A appRequrired() throws AccessDeniedException {
		A app = getCurrentApp();
		if (app != null) {
			return app;
		}
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_APP_CREDENTIALS);
	}
	
	public U getCurrentUser() {
		return (U) getState().user;
	}

	public A getCurrentApp() {
		return (A) getState().app;
	}

	public void setCurrentUser(U user) {
		RequestState<U, A> state = getState();
		state.user = user;
		state.connState = new ConnectionState(state.user, state.app);
	}

	public void setCurrentApp(A app) {
		RequestState<U, A> state = getState();
		state.app = app;
		state.connState = new ConnectionState(state.user, state.app);
	}

	public HttpServletRequest getCurrentRequest() {
		try {
			return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
		} catch (IllegalStateException e) {
			return null;
		}
	}

	public HttpServletResponse getCurrentResponse() {
		return response;
	}
	
	public String getDeviceIdentifier() {
		return getState().connState.getDeviceId();
	}
	
	@Before("allControllers()")
	public Object beforeControllers(JoinPoint joinPoint) {
		DebugGui.init(getCurrentRequest());
		ConnectionState connState = new ConnectionState(getCurrentRequest());
		getState().connState = connState;
		
		validateApp(connState);
		validateUser(connState);
		
		gates.securityCheck(joinPoint);
		this.response.setStatus(203);
		return null;
	}
	
	private void validateApp(ConnectionState connState) {
		A app = getNewApp();
		app.setToken(new AppAccessToken(connState));
		app.setId(connState.getAppId());
		app.setAccessKey(connState.getAccessKey());
		
		try {
			app = getAppSrvc().authinticate(app);
		} catch (Exception e) {
			DebugGui.exception("AopAuth", "Authinticate App", e);
			app = null;
		}
		getState().app = app;
		DebugGui.value("AopAuth", "user", app);
	}

	private void validateUser(ConnectionState connState) {
		U user = getNewUser();
		user.setPassword(connState.getUserPassword());
		user.setId(connState.getUserId());
		user.setToken(new UserAccessToken(connState));
		
		try {
			user = getUserSrvc().authinticate(user);
		} catch (Exception e) {
			DebugGui.exception("AopAuth", "Authinticate User", e);
			user = null;
		}
		getState().user = user;
		DebugGui.value("AopAuth", "user", user);
	}
	
	@AfterReturning("allControllers()")
	public Object returning(JoinPoint joinPoint) {
		
		getState().connState.addAuthorization(getCurrentResponse());
		return uponExit(joinPoint);
	}

	public Object uponExit(JoinPoint joinPoint) {
		return null;
	}
	
	public HttpHeaders getHeaders() {
		return getHeaders(null);
	}
	
	public HttpHeaders getHeaders(HttpHeaders httpHeaders) {
		return getState().connState.addAuthorization(httpHeaders);
	}

	public boolean addField(String name) {
		return name.matches("(TOKEN|PASSWORD|EMAIL|DEVICE_IDENTIFIER|APP_.*)");
	}

	public static void setBackendApp(App backendApp) {
		AopAuth.backendApp = backendApp;
	}
	
	public static App getBackendApp() {
		return AopAuth.backendApp;
	}
}
