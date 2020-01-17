package com.userSrvc.client.aop;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.AccessExceptions;
import com.userSrvc.client.util.DebugGui;

@Service
public class AopAuthGates {

	@Retention(RetentionPolicy.RUNTIME)
	public @interface AopAuthAppOnly {
		long[] ids() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface AopAuthUserOnly {
		long[] ids() default {};
	}

	@Retention(RetentionPolicy.RUNTIME)
	public @interface AopAuthPublic {}

	@Autowired
	AopAuth<?, ?> aopAuth;
	
	public boolean hasAccess(JoinPoint joinPoint) {
		MethodSignature method = (MethodSignature)joinPoint.getSignature();
		Class<?> clazz = method.getDeclaringType();
		Annotation[] annos = method.getMethod().getAnnotations();
		for (Annotation anno : annos) {
			Boolean isAccessible = hasAccess(anno);
			if (Boolean.TRUE.equals(isAccessible)) {
				return true;
			} else if (Boolean.FALSE.equals(isAccessible)){
				return false;
			}
		}
		
		Annotation[] classAnnos = clazz.getAnnotations();
		for (Annotation anno : classAnnos) {
			Boolean isAccessible = hasAccess(anno);
			if (Boolean.TRUE.equals(isAccessible)) {
				return true;
			} else if (Boolean.FALSE.equals(isAccessible)){
				return false;
			}
		}
		
		return false;
	}
	
	public void securityCheck(JoinPoint joinPoint) {
		if (!Boolean.FALSE.equals(hasAccess(joinPoint)) ) {
			if (DebugGui.debugging()) {
				throw new AccessExceptions.NotFound();
			} else {
				throw new AccessExceptions.UnAuthorized();
			}
		}
	}
	
	public Boolean hasAccess(Object obj) {
		if (obj.getClass().equals(AopAuthUserOnly.class)) {
			return userOnly((AopAuthUserOnly)obj);
		} else if (obj.getClass().equals(AopAuthAppOnly.class)) {
			return appOnly((AopAuthAppOnly)obj);
		} else if (obj.getClass().equals(AopAuthPublic.class)) {
			return true;
		}
		return null;
	}

	private Boolean appOnly(AopAuthAppOnly userOnlyAnno) {
		App app = aopAuth.getCurrentApp();
		if (app == null) {
			return false;
		}
		if (userOnlyAnno.ids().length == 0) {
			return true;
		}
		long userId = app.getId();
		for (long id : userOnlyAnno.ids()) {
			if (id == userId) {
				return true;
			}
		}
		return false;
	}
	
	private Boolean userOnly(AopAuthUserOnly userOnlyAnno) {
		UUserAbs user = aopAuth.getCurrentUser();
		if (user == null) {
			return false;
		}
		if (userOnlyAnno.ids().length == 0) {
			return true;
		}
		long userId = user.getId();
		for (long id : userOnlyAnno.ids()) {
			if (id == userId) {
				return true;
			}
		}
		return false;
	}
	
//	private Boolean idRuleSatisfied(HasId hasId) {
//		
//	}
}

