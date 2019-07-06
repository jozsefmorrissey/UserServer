package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
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

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.services.PermissionSrvc;
import com.userSrvc.client.util.GenUtils;
import com.userSrvc.exceptions.StatisticallyImpossible;
import com.userSrvc.server.repo.UserRepo;
import com.userSrvc.server.service.UserPhotoSrvc;
import com.userSrvc.server.service.UserSrvc;
import com.userSrvc.server.utils.HtmlString;

@Service
public class UserSrcvImpl implements UserSrvc {
	private static final short ADD_ATTEMPTS = 3;

	@Autowired
	UserPhotoSrvc userPhotoSrvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	PermissionSrvc permSrvc;
	
	@Override
	public UUserAbs addUser(UUserAbs user) throws ConstraintViolationException, PropertyValueException, StatisticallyImpossible {
		validateEmail(user);
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
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
			if (user.getId() != null) {
				try {
					UUserAbs u = userRepo.save(user);
					u.setPassword(null);
					return u;
				} catch (Exception e) {}
			}
		}
		throw new StatisticallyImpossible("If you see this error there are far to many users on this system. Try again, sorry for the inconvienance.");
	}
	
	private String setToken(UUserAbs user) {
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
		String token = GenUtils.randStringSecure(256);
		dbUser.setUserToken(token);
		userRepo.save(dbUser);
		return token;
	}

	@Override
	public UUserAbs loginUser(UUserAbs user) throws PropertyValueException, DataException {
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
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

	private UUserAbs authinticate(UUserAbs user, boolean external) throws AccessDeniedException {
		if (user.getUserToken() == null) {
			try { user = loginUser(user); } catch (Exception e) {}
			throw new AccessDeniedException(ERROR_MSGS.NO_TOKEN_PROVIDED);
		}
		validateEmail(user);
		UUserAbs u = (UUserAbs) userRepo.getByEmail(user.getEmail());
		if (u != null && user != null && user.getUserToken().equals(u.getUserToken())) {
			if (external) {
				u.setPassword(null);
			}
			return u;
		}
		
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_CREDENTIALS);
	}

	@Override
	public UUserAbs authinticate(UUserAbs user) throws AccessDeniedException {
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
	public UUserAbs getUser(long id) throws PropertyValueException {
		UUserAbs dbUser;
		dbUser = (UUserAbs) userRepo.getOne(id);
		if (dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.INVALID_ID, "user", "email");
		}
		
		setLists(dbUser);
		return dbUser;
	}
	
	@Override
	public UUserAbs getUser(String email) throws PropertyValueException {
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(email);
		if (dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.EMAIL_DOES_NOT_EXIST, "user", "email");
		}

		setLists(dbUser);
		return dbUser;
	}
	
	private void setLists(List<UUserAbs> users) {
		for (UUserAbs user : users) {
			setLists(user);
		}
	}

	private void setLists(UUserAbs dbUser) {
		dbUser.setPassword(null);
		dbUser.setUserToken(null);
		// TODO: add generalize for all apps.
		dbUser.setImageUrls(userPhotoSrvc.getUris(1l, 1l));
		dbUser.setPermissionTypes(permSrvc.getTypes(dbUser.getId(), 1l));
	}

	@Override
	public void updatePassword(UUserAbs user) throws PropertyValueException, AccessDeniedException {
		UUserAbs dbUser = authinticate(user, false);
		validatePassword(user);
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		dbUser.setPassword(hashed);
		setToken(dbUser);
		userRepo.save(dbUser);
	}

	@Override
	public void updateEmail(UUserAbs user, String newEmail) throws PropertyValueException, AccessDeniedException {
		validateEmail(newEmail);
		UUserAbs dbUser = authinticate(user, false);
		dbUser.setEmail(newEmail);
		userRepo.save(dbUser);
	}

	@Override
	public void resetPassword(UUserAbs user, String url) throws PropertyValueException {
		validateEmail(user);
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
		HashMap<String, Object> scope = new HashMap<String, Object>();
		if(dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.EMAIL_NOT_REGISTERED, "user", "email");
		}

		String token = setToken(dbUser);
		String email = dbUser.getEmail();
		scope.put("name", dbUser.getFullName());
		scope.put("url", url + email + "/" + token);
		scope.put("token", token);
		HtmlString htmlString = new HtmlString(scope, "./src/main/resources/static/emailTemplates/password-reset.html");
		sendEmail(email, "Password Reset", htmlString.toString());
	}
	
	private boolean validateEmail(UUserAbs user) throws PropertyValueException {
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
	private boolean validatePassword(UUserAbs user) {
		return true;
	}
	
	private boolean validateUsername(UUserAbs user) throws PropertyValueException {
		String name = user.getFullName();
		if (name == null || name.equals("")) {
			throw new PropertyValueException (ERROR_MSGS.USERNAME_NOT_DEFINED, "user", "username");
		}
		return true;
	}

	@Override
	public void update(UUserAbs user) throws PropertyValueException, AccessDeniedException {
		UUserAbs dbUser = authinticate(user, false);
		user.setId(dbUser.getId());
		userRepo.save(user);
	}

	@Override
	public List<UUserAbs> getUsers(List<Long> ids) {
		List<UUserAbs> list = userRepo.findAllById(ids);
		setLists(list);
		return list;
	}
	
	@Override
	public List<UUserAbs> cleanUsers(List<UUserAbs> users) {
		for(UUserAbs user : users) {
			user.setPassword(null);
			user.setUserToken(null);
		}
		return users;
	}
}
