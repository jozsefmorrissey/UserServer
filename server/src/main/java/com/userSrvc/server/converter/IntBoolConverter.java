package com.userSrvc.server.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class IntBoolConverter implements AttributeConverter<Boolean, Integer>{

	@Override
	public Integer convertToDatabaseColumn(Boolean arg0) {
		return arg0 ? 1 : 0;
	}

	@Override
	public Boolean convertToEntityAttribute(Integer arg0) {
		return arg0 == 1 ? true : false;
	}
}
