package com.userSrvc.server.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ImageUtils {
	
	public static byte[] convertToBytes(String fileLocation){
		File fi = new File(fileLocation);
		return convertToBytes(fi);
	}
	
	public static byte[] convertToBytes(File file){
		byte[] photo = null;
		try {
			photo = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return photo;
	}
	
}
