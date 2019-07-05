package com.userSrvc.server.controller;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.client.constant.URI;
import com.userSrvc.server.service.UserPhotoSrvc;
import com.userSrvc.server.utils.ImageUtils;

@CrossOrigin
@RestController
@RequestMapping
public class UserPhotoCtrl {

	@Autowired
	UserPhotoSrvc userPhotoSrvc;
	
	@PostConstruct
	public void init() throws UnsupportedEncodingException {
		ImageUtils.initTest(userPhotoSrvc);
	}
	
	@RequestMapping(value = URI.USER_PHOTO_USER + "png", 
			produces = "image/png",
			method = RequestMethod.GET)
	@ResponseBody
	public Byte[] getPhotoPNG(@PathVariable long userId) throws FileNotFoundException {
		return userPhotoSrvc.getPhoto(userId);
	}

	@RequestMapping(value = URI.USER_PHOTO_USER + "jpg", 
			produces = "image/jpg",
			method = RequestMethod.GET)
	@ResponseBody
	public Byte[] getPhotoJPG(@PathVariable long userId) throws FileNotFoundException {
		return userPhotoSrvc.getPhoto(userId);
	}

	@RequestMapping(value = URI.USER_PHOTO_USER + "gif", 
			produces = "image/gif",
			method = RequestMethod.GET)
	@ResponseBody
	public Byte[] getPhotoGIF(@PathVariable long userId) throws FileNotFoundException {
		return userPhotoSrvc.getPhoto(userId);
	}

	@RequestMapping(value = URI.USER_PHOTO_USER + "jpeg", 
			produces = "image/jpeg",
			method = RequestMethod.GET)
	@ResponseBody
	public Byte[] getPhotoJPEG(@PathVariable long userId) throws FileNotFoundException {
		return userPhotoSrvc.getPhoto(userId);
	}
}
