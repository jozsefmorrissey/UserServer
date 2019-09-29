package com.userSrvc.client.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.userSrvc.client.error.TryingToProtectRequiredFieldError;
import com.userSrvc.client.marker.HasType;

import lombok.Data;

@Data
public abstract class AopSecure implements HasType {
	
	private static Logger log = LogManager.getLogger();

	@JsonIgnore 
	private List<String> requiredFields = new ArrayList<String>();
	@JsonIgnore 
	private List<String> privateFields = new ArrayList<String>();
	@JsonIgnore 
	private List<String> protectedFields = new ArrayList<String>();
	
	private boolean associate = false;
	
	private void clean(List<String> propertyNames) {
		List<Method> fieldList = new ArrayList<Method>();
		Method[] methods = this.getClass().getMethods();
		if (methods != null && methods.length > 0) {
			fieldList.addAll(Arrays.asList(methods));
		}

		methods = this.getClass().getDeclaredMethods();
		if (methods != null && methods.length > 0) {
			fieldList.addAll(Arrays.asList(methods));
		}
		
		for (int index = 0; fieldList != null && index < fieldList.size(); index += 1) {
			Method method = fieldList.get(index);
			String name = method.getName();
			if (name.charAt(0) == 's' && name.charAt(1) == 'e' && name.charAt(2) == 't') {
				String property = name.substring(3, 4).toLowerCase() + 
						name.substring(4);
				if (propertyNames.contains(property)) {
					try {
						method.invoke(this, new Object[] {null});
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						log.warn("Failed to secure property via method: " + method);;
					}
				}				
			}
		}
	}

	public AopSecure() {}
	
	public AopSecure(AopSecure aopSecure) {
		this.setRequiredFields(aopSecure.getRequiredFields());
		this.setPrivateFields(aopSecure.getPrivateFields());
		this.setProtectedFields(aopSecure.getProtectedFields());
		this.associate = aopSecure.isAssociate();
	}

	@PostConstruct
	public void init() {
		if(AopAuth.getBean() != null
				&& !AopAuth.getBean().isReturning()) {
			AopAuth.getBean().addObject(this);
		}
	}
	
	public boolean lockdown() {
//		clean();
		return true;
	}
	
	public void setRequiredFields(String name) {
		this.requiredFields = Arrays.asList(name.split(","));
		requiredConflict();
	}

	public void setPrivateFields(String name) {
		this.privateFields = Arrays.asList(name.split(","));
		requiredConflict();
	}
	
	public void setProtectedFields(String name) {
		this.protectedFields = Arrays.asList(name.split(","));
		requiredConflict();
	}

	public String getPrivateFields() {
		return String.join(",", privateFields);
	}
	
	public String getProtectedFields() {
		return String.join(",", protectedFields);
	}
	
	public String getRequiredFields() {
		return String.join(",", protectedFields);
	}
	
	
	private void requiredConflict() {
		List<String> targetList = new ArrayList<String>();
		targetList.addAll(this.privateFields);
		targetList.addAll(this.protectedFields);
		for (String target : targetList) {
			if (!target.equals("") && this.requiredFields.contains(target)) {
				throw new TryingToProtectRequiredFieldError("Field '" + target + "' is required.");
			}
		}
	}
	
	public void clean() {
		List<String> targetList = new ArrayList<String>();
		targetList.addAll(this.privateFields);
		if (!this.associate) {
			targetList.addAll(this.protectedFields);
		}
		clean(targetList);
	}
	
	public void setIsAssociate(boolean value) {
		this.associate = value;
	}
	
	public boolean isAssociate() {
		return this.associate;
	}
	
	public String getObjectType() {
		return "AOP_SECURE";
	}
}
