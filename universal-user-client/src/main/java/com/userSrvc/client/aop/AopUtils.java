package com.userSrvc.client.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class AopUtils {
	public static Object getArgByName(JoinPoint joinPoint, String name) {
		MethodSignature codeSignature = (MethodSignature) joinPoint.getSignature();
        for (int i = 0; i < codeSignature.getParameterNames().length; i += 1) {
        	String paramName = codeSignature.getParameterNames()[i];
        	if (paramName.equals(name)) {
        		return joinPoint.getArgs()[i];
        	}
        }
        return null;
	}

}
