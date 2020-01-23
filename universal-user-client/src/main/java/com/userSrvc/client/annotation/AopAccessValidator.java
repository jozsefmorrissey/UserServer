package com.userSrvc.client.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AopAccessValidator {
	public String[] accessValidator() default {};
}