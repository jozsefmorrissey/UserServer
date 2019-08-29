package com.userSrvc.client.aop;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.userSrvc.client.constant.ToJson;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.services.UserSrvc;

@Aspect
public abstract class  AopAuth <U extends UUserAbs> extends ToJson {
	
	private static AopAuth<?> instance;
	
	@Autowired
	ApplicationContext appContext;
		
	protected abstract UserSrvc<U> getUserSrvc();
	
	public static AopAuth<?> getBean() {
		return instance;
	}
	
	public AopAuth () {instance = this;}

	@Pointcut("execution(* com..*.controller..*(..))")
	public void allControllers() {}
	
	public static final String NOT_STARTED = "not started";
	public static final String PROCESSING = "processing";
	public static final String RETURNING = "returning";
	private static final String REQUEST_ID = "requestId";
	
	public static final String TOKEN = "Token";
	public static final String PASSWORD = "Password";
	public static final String EMAIL = "Email";
	
	private class RequestState {
		private U user;
		private String state = NOT_STARTED;
		private List<AopSecure> objects = new ArrayList<AopSecure>();
	}

	Logger log = LogManager.getLogger();

	private Long threadCount = 0l;
	private HashMap<String, RequestState> userInfoMap = new HashMap<String, RequestState>();
		
	private RequestState getState() {
		if (userInfoMap.get(MDC.get(REQUEST_ID)) == null)  {
			MDC.put(REQUEST_ID, threadCount.toString());
			userInfoMap.put(threadCount.toString(), new RequestState());
			threadCount++;
		}
		return userInfoMap.get(MDC.get(REQUEST_ID));
	}
	
	public boolean isReturning() {
		if (getState() == null) return true;
		return RETURNING.equals(getState().state);
	}

	public void addObject(AopSecure obj) {
		getState().objects.add(obj);
	}
	
	public U requriredUser() throws AccessDeniedException {
		U user = getCurrentUser();
		if (user != null) {
			return user;
		}
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_CREDENTIALS);
	}
	
	public U getCurrentUser() {
		return getState().user;
	}

	public HttpServletRequest getCurrentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		
	}
	
	@Before("allControllers()")
	public Object validateUser(JoinPoint joinPoint) {
		System.out.println("In: " + MDC.get(REQUEST_ID));
		
		HttpServletRequest curRequest = getCurrentRequest();
		
		String password = curRequest.getHeader(PASSWORD);
		String token = curRequest.getHeader(TOKEN);
		String email = curRequest.getHeader(EMAIL);
	
		U user = (U) appContext.getBean("UUser");
		user.setEmail(email);
		user.setToken(token);
		getState().user = user;
		
		try {
			user = getUserSrvc().authinticate(user);
		} catch (Exception e) {
			log.warn("Unable to Authinticate User.");
		}
		user.setPassword(password);
		getState().user = user;
		
		System.out.println("State: " + PROCESSING);
		return null;
	}
	
	@AfterReturning("allControllers()")
	public Object returning(JoinPoint joinPoint) {
		userInfoMap.get(MDC.get(REQUEST_ID)).state = RETURNING;
		
		RequestState rs = userInfoMap.get(MDC.get(REQUEST_ID));
		Set<Long> userIds = new HashSet<Long>();
		List<U> users = new ArrayList<U>();
		for (int i = 0; i < rs.objects.size(); i += 1) {
			Object obj = rs.objects.get(i);
			if (obj instanceof UUserAbs) {
				userIds.add(((UUserAbs) obj).getId());
				users.add((U) obj);
			}
		}
		
		try {
			List<U> dbUsers = getUserSrvc().get(userIds);
			UUserAbs.merg(users, dbUsers);
		} catch (Exception e) {
			log.warn("Unable to merge users: " + e.getMessage());
		}

		for (int i = 0; i < rs.objects.size(); i += 1) {
			rs.objects.get(i).lockdown();
		}
				
		return uponExit(joinPoint);
	}

	public Object uponExit(JoinPoint joinPoint) {
		return null;
	}
	
	public HttpHeaders getHeaders() {
		return UserSrvc.getHeaders(getCurrentUser());
	}
}
