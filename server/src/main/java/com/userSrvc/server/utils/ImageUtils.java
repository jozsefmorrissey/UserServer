package com.userSrvc.server.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class ImageUtils {
	
	public static Byte[] convertToBytes(String fileLocation){
		File fi = new File(fileLocation);
		return convertToBytes(fi);
	}
	
	public static Byte[] convertToBytes(File file){
		byte[] photo = null;
		try {
			photo = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toObjects(photo);
	}
	
	private static Byte[] toObjects(byte[] bytesPrim) {
	    Byte[] bytes = new Byte[bytesPrim.length];
	    Arrays.setAll(bytes, n -> bytesPrim[n]);
	    return bytes;
	}
}
