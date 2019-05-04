package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.exceptions.StatisticallyImpossible;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.entities.UserPhoto;
import com.userSrvc.server.repo.UserPhotoRepo;
import com.userSrvc.server.repo.UserRepo;
import com.userSrvc.server.service.UserSrvc;
import com.userSrvc.server.utils.GenUtils;
import com.userSrvc.server.utils.HtmlString;

@Service
public class UserSrcvImpl implements UserSrvc {
	private static final short ADD_ATTEMPTS = 3;

	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserPhotoRepo userPhotoRepo;

	@Override
	public UUser addUser(UUser user) throws ConstraintViolationException, PropertyValueException, StatisticallyImpossible {
		validateEmail(user);
		UUser dbUser = (UUser) userRepo.getByEmail(user.getEmail());
		if (dbUser != null) {
			throw new ConstraintViolationException(
					ERROR_MSGS.EMAIL_ALREADY_REGISTERED.toString(), new SQLIntegrityConstraintViolationException(), 
					"EMAIL UNIQUE");
		}
		validateUsername(user);
		validatePassword(user);
		
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		for (int i = 0; i < ADD_ATTEMPTS; i ++) {
			user.setId(new Random().nextLong());
			if (user.getId() != 0) {
				try {
					UUser u = userRepo.save(user);
					u.setPassword(null);
					return u;
				} catch (Exception e) {}
			}
		}
		throw new StatisticallyImpossible("If you see this error there are far to many users on this system. Try again, sorry for the inconvienance.");
	}
	
	private String setToken(UUser user) {
		UUser dbUser = (UUser) userRepo.getByEmail(user.getEmail());
		String token = GenUtils.randStringSecure(256);
		dbUser.setUserToken(token);
		userRepo.save(dbUser);
		return token;
	}

	@Override
	public UUser loginUser(UUser user) throws PropertyValueException, DataException {
		UUser dbUser = (UUser) userRepo.getByEmail(user.getEmail());
		if (dbUser == null) {
			throw new DataException(ERROR_MSGS.EMAIL_DOES_NOT_EXIST, null);
		}
		if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			throw new PropertyValueException(ERROR_MSGS.INVALID_PASSWORD, "user", "password");
		}
		
		user.merge(dbUser);
		user.setUserToken(setToken(dbUser));
		user.setPassword(null);
		return user;
	}

	private UUser authinticate(UUser user, boolean external) throws AccessDeniedException {
		if (user.getUserToken() == null) {
			throw new AccessDeniedException(ERROR_MSGS.NO_TOKEN_PROVIDED);
		}
		validateEmail(user);
		UUser u = (UUser) userRepo.getByEmail(user.getEmail());
		if (u != null && user != null && user.getUserToken().equals(u.getUserToken())) {
			if (external) {
				u.setPassword(null);
			}
			return u;
		}
		
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_CREDENTIALS);
	}

	@Override
	public UUser authinticate(UUser user) throws AccessDeniedException {
		return authinticate(user, true);
	}
	
	public static void sendEmail(String to, String subject, String htmlContent) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("AboutMeMailServer","sho2shaoNgoiphieghiebaeti8thae");
				}
			});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("AboutMeMailServer@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(htmlContent, "text/html");

			Transport.send(message);

			System.out.println("Email Sent");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public UUser getUser(UUser user) throws PropertyValueException {
		UUser dbUser;
		if (user.getId() > 0) {
			dbUser = (UUser) userRepo.getOne(user.getId());
			if (dbUser == null) {
				throw new PropertyValueException(ERROR_MSGS.INVALID_ID, "user", "email");
			}
		} else {
			dbUser = (UUser) userRepo.getByEmail(user.getEmail());
			if (dbUser == null) {
				throw new PropertyValueException(ERROR_MSGS.EMAIL_DOES_NOT_EXIST, "user", "email");
			}
		}

		dbUser.setPassword(null);
		dbUser.setUserToken(null);
		return dbUser;
	}

	@Override
	public void updatePassword(UUser user) throws PropertyValueException, AccessDeniedException {
		UUser dbUser = authinticate(user, false);
		validatePassword(user);
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		dbUser.setPassword(hashed);
		setToken(dbUser);
		userRepo.save(dbUser);
	}

	@Override
	public void updateEmail(UUser user, String newEmail) throws PropertyValueException, AccessDeniedException {
		validateEmail(newEmail);
		UUser dbUser = authinticate(user, false);
		dbUser.setEmail(newEmail);
		userRepo.save(dbUser);
	}

	@Override
	public void resetPassword(UUser user, String url) throws PropertyValueException {
		validateEmail(user);
		UUser dbUser = (UUser) userRepo.getByEmail(user.getEmail());
		HashMap<String, Object> scope = new HashMap<String, Object>();
		if(dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.EMAIL_NOT_REGISTERED, "user", "email");
		}

		String token = setToken(dbUser);
		String email = dbUser.getEmail();
		scope.put("name", dbUser.getName());
		scope.put("url", url + email + "/" + token);
		scope.put("token", token);
		HtmlString htmlString = new HtmlString(scope, "./src/main/resources/static/emailTemplates/password-reset.html");
		sendEmail(email, "Password Reset", htmlString.toString());
	}
	
	private boolean validateEmail(UUser user) throws PropertyValueException {
		return validateEmail(user.getEmail());
	}

	private boolean validateEmail(String email) throws PropertyValueException {
		if (!email.matches(".{1,}@.{1,}\\..{1,}")) {
			throw new PropertyValueException(
					ERROR_MSGS.EMAIL_INVALID_FORMAT, "user", "email");
		}
		return true;
	}
	
	// TODO: do to hashing on the ui level we cannot insure the length of the password.
	private boolean validatePassword(UUser user) {
		return true;
	}
	
	private boolean validateUsername(UUser user) throws PropertyValueException {
		String name = user.getName();
		if (name == null || name.equals("")) {
			throw new PropertyValueException (ERROR_MSGS.USERNAME_NOT_DEFINED, "user", "username");
		}
		return true;
	}

	@Override
	public void update(UUser user) throws PropertyValueException, AccessDeniedException {
		UUser dbUser = authinticate(user, false);
		user.setId(dbUser.getId());
		userRepo.save(user);
	}

	@Override
	public List<Byte[]> photo(long id) {
		List<UserPhoto> photoObjs = userPhotoRepo.getByUserId(id);
		List<Byte[]> photos = new ArrayList<Byte[]>();
		for (UserPhoto upo : photoObjs) {
			photos.add(upo.getPhoto());
		}
		return photos;
	}
}
