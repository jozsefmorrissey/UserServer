package com.userSrvc.client.aop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;

@Component
@Aspect
public abstract class  AopAuth <U extends UUserAbs> {
	
	@Autowired
	ApplicationContext appContext;

	protected abstract UserSrvcExt<U> getUserSrvc();

	@Pointcut("execution(* *.controller..*(..))")
	public void allControllers() {}

	
	public static final String NOT_STARTED = "not started";
	public static final String PROCESSING = "processing";
	public static final String RETURNING = "returning";
	private static final String REQUEST_ID = "requestId";
	
	public static final String TOKEN = "Token";
	public static final String Password = "Password";
	public static final String Email = "Email";
	
	private static class RequestState <U extends UUserAbs> {
		private U user;
		private String state = NOT_STARTED;
		private List<AopSecure> objects = new ArrayList<AopSecure>();
	}

	Logger log = LogManager.getLogger();

	private static Long threadCount = 0l;
	private static HashMap<String, RequestState> userInfoMap = new HashMap<String, RequestState>();
		
	private static RequestState getState() {
		System.out.println("Get: " + MDC.get(REQUEST_ID));
		if (userInfoMap.get(MDC.get(REQUEST_ID)) == null)  {
			MDC.put(REQUEST_ID, threadCount.toString());
			userInfoMap.put(threadCount.toString(), new RequestState());
			threadCount++;
		}
		return userInfoMap.get(MDC.get(REQUEST_ID));
	}
	
	public static boolean isReturning() {
		if (getState() == null) return true;
		return RETURNING.equals(getState().state);
	}

	public static void addObject(AopSecure obj) {
		getState().objects.add(obj);
	}

	public static <U extends UUserAbs>  U getCurrentUser() {
		return (U) getState().user;
	}
	
	@Before("allControllers()")
	public Object validateUser(JoinPoint joinPoint) {
		System.out.println("In: " + MDC.get(REQUEST_ID));
		
		HttpServletRequest curRequest = 
				((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		
		String password = curRequest.getHeader(Password);
		String token = curRequest.getHeader(TOKEN);
		String email = curRequest.getHeader(Email);
	
		U user = (U) appContext.getBean("UUser");
		user.setEmail(email);
		user.setPassword(password);
		user.setUserToken(token);
		
		try {
			getState().user = getUserSrvc().authinticateUser(user);
		} catch (RestResponseException e) {
			log.warn("Unable to Authinticate User.");
		}
		
		System.out.println("State: " + PROCESSING);
		return null;
	}
	
	@AfterReturning("allControllers()")
	public Object returning(JoinPoint joinPoint) {
		System.out.println("State: " + RETURNING);
		System.out.println("Out: " + MDC.get(REQUEST_ID));
		userInfoMap.get(MDC.get(REQUEST_ID)).state = RETURNING;
		
		RequestState<U> rs = userInfoMap.get(MDC.get(REQUEST_ID));
		for (int i = 0; i < rs.objects.size(); i += 1) {
			rs.objects.get(i).lockdown();
		}
				
		return null;
	}
}
