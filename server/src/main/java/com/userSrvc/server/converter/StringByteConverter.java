package com.userSrvc.server.converter;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringByteConverter implements AttributeConverter<String, byte[]>{

	public static void main(String...args) {
		System.out.println(Arrays.toString(new StringByteConverter().convertToDatabaseColumn("lkasjdflksdjafljsdalkfjlasdjfl;asdjfl;kasdjflksdjflksdjfklsjaf;j;")));
	}
	@Override
	public byte[] convertToDatabaseColumn(String arg0) {
		return arg0.getBytes();
	}

	@Override
	public String convertToEntityAttribute(byte[] arg0) {
		return new String(arg0);
	}
}
