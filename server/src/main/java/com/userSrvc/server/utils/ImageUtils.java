package com.userSrvc.server.utils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.userSrvc.server.entities.UserPhoto;
import com.userSrvc.server.service.UserPhotoSrvc;

public class ImageUtils {
	
	public static void initTest(UserPhotoSrvc userPhotoSrvc) throws UnsupportedEncodingException {
		String picLoc = "../Setup files/DATA BASE/images/profilePic/";
		List<String> files = Arrays.asList(new String[] {
				"female1.jpg",
				"female2.jpeg",
				"female3.jpeg",
				"male1.jpeg",
				"male2.jpeg",
				"male3.jpeg",
				"giph1.gif"
		});
		
		List<UserPhoto> photos = new ArrayList<UserPhoto>();
		for (int i = 537; i < 1000; i += 1) {
			String file = files.get((int)(Math.random() * files.size()));
			long id = i;
			long userId = (int)(Math.random() * 500);
			String ext = file.replaceAll("^.*\\.(.*)$", "$1");
			long appUserId = 1l;
			short position = (short)(Math.random() * 10);
			Byte[] photo = convertToBytes(picLoc + file);
			
			String vars = "(ID, USER_ID, EXT, APP_USER_ID, POSTIONT, PHOTO)";
			String values = "(" + id + ", " + userId + ", " + ext + ", " + appUserId + ", " + position + ", " + photo + ")";
			
			UserPhoto userPhoto = new UserPhoto(id, ext, userId, appUserId, position, photo);
			photos.add(userPhoto);
			System.out.println(userPhoto);
			userPhotoSrvc.update(userPhoto);
		}
		
//		userPhotoSrvc.update(photos);
	}
	
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
