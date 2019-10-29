package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.error.UUserUnauthAccessException;
import com.userSrvc.client.services.PermissionSrvc;
import com.userSrvc.client.services.UserSrvc;
import com.userSrvc.client.util.Util;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.repo.UserRepo;
import com.userSrvc.server.service.UserPhotoSrvc;

@Service
public class UserSrvcImpl implements UserSrvc<UUserAbs> {
	private static final short ADD_ATTEMPTS = 3;

	@Autowired
	AopAuth<UUserAbs> aopAuth;
	
	@Autowired
	UserPhotoSrvc userPhotoSrvc;

	@Autowired
	UserRepo userRepo;

	@Autowired
	PermissionSrvc<UUserAbs> permSrvc;
	
	@Override
	public UUserAbs add(UUserAbs user) throws Exception {
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
		System.out.println("H:" + hashed);
		UUserAbs u = userRepo.save(user);

		userPhotoSrvc.updateAll(user.getImageUrls(), u.getId(), 1l);
		return u;
	}
	
	private String setToken(UUserAbs user) {
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
		String token = Util.randomString(9, "[a-zA-Z0-9]", ".*");
		dbUser.setToken(token);
		user.setToken(token);
		userRepo.save(dbUser);
		return token;
	}

	@Override
	public UUserAbs login() throws Exception {
		UUserAbs user = aopAuth.getCurrentUser();
		if (user == null) {
			throw new PropertyValueException(ERROR_MSGS.INCORRECT_CREDENTIALS, "user", "password");
		}
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
		if (dbUser == null) {
			throw new UUserUnauthAccessException(ERROR_MSGS.EMAIL_DOES_NOT_EXIST);
		}
		dbUser = new UUser(dbUser);
		if (dbUser.getToken() == null) {
			setToken(dbUser);
		}
		System.out.println("P:" + user.getPassword());
		System.out.println("DBP:" + dbUser.getPassword());
		if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			throw new UUserUnauthAccessException(ERROR_MSGS.INVALID_PASSWORD);
		}
		
		setLists(dbUser);
		return dbUser;
	}

	private UUserAbs authinticate(UUserAbs user, boolean external) throws AccessDeniedException {
		if (user.getToken() == null) {
			try { user = login(); } catch (Exception e) {}
			throw new AccessDeniedException(ERROR_MSGS.NO_TOKEN_PROVIDED);
		}
		validateEmail(user);
		UUserAbs u = (UUserAbs) userRepo.getByEmail(user.getEmail());
		if (u != null && user != null && user.getToken().equals(u.getToken())) {
			UUserAbs retUser = new UUser(u);
			setLists(retUser);
			return retUser;
		}
		
		throw new AccessDeniedException(ERROR_MSGS.INCORRECT_CREDENTIALS);
	}

	public UUserAbs authinticate(UUserAbs user) throws AccessDeniedException {
		return authinticate(user, true);
	}

	@Override
	public UUserAbs authinticate() throws AccessDeniedException {
		return authinticate(aopAuth.getCurrentUser(), true);
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
	public UUserAbs get(String email) throws PropertyValueException {
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(email);
		if (dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.EMAIL_DOES_NOT_EXIST, "user", "email");
		}
		dbUser = new UUser(dbUser);

		setLists(dbUser);
		return dbUser;
	}
	
	private void setLists(List<UUserAbs> users) {
		for (UUserAbs user : users) {
			setLists(user);
		}
	}

	private void setLists(UUserAbs dbUser) {
		dbUser.setImageUrls(userPhotoSrvc.getUris(dbUser.getId(), 1l));
		dbUser.setPermissionTypes(permSrvc.getTypes(dbUser.getId(), 1l));
	}

	@Override
	public void updatePassword() throws PropertyValueException, AccessDeniedException {
		UUserAbs user = aopAuth.getCurrentUser();
		UUserAbs dbUser = authinticate(user, false);
		validatePassword(user);
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		dbUser.setPassword(hashed);
		setToken(dbUser);
		userRepo.save(dbUser);
	}

	@Override
	public void updateEmail(String newEmail) throws Exception {
		validateEmail(newEmail);
		UUserAbs dbUser = authinticate(aopAuth.getCurrentUser(), false);
		dbUser.setEmail(newEmail);
		userRepo.save(dbUser);
	}

	@Override
	public void resetPassword(String url) throws PropertyValueException, UnirestException {
		UUserAbs user = aopAuth.getCurrentUser();
		validateEmail(user);
		UUserAbs dbUser = (UUserAbs) userRepo.getByEmail(user.getEmail());
		if(dbUser == null) {
			throw new PropertyValueException(ERROR_MSGS.EMAIL_NOT_REGISTERED, "user", "email");
		}
		dbUser = new UUser(dbUser);

		EmailServiceImpl.resetPassword(dbUser, url);
	}
	
	private boolean validateEmail(UUserAbs user) throws PropertyValueException {
		return validateEmail(user.getEmail());
	}

	private boolean validateEmail(String email) throws PropertyValueException {
		if (!email.matches(".{1,}@.{1,}\\..{1,}")) {
			throw new PropertyValueException(
					ERROR_MSGS.EMAIL_INVALID_FORMAT + "(" + email + ")", "user", "email");
		}
		return true;
	}
	
	// TODO: do to hashing on the ui level we cannot insure the length of the password.
	private boolean validatePassword(UUserAbs user) {
		return true;
	}
	
	private boolean validateUsername(UUserAbs user) throws PropertyValueException {
		String name = user.getFullname();
//		if (name == null || name.equals("")) {
//			throw new PropertyValueException (ERROR_MSGS.USERNAME_NOT_DEFINED, "user", "username");
//		}
		return true;
	}

	@Override
	public UUserAbs update(UUserAbs user) throws AccessDeniedException {
		aopAuth.requriredUser();
		UUserAbs dbUser = userRepo.getByEmail(user.getEmail());
		String password = dbUser.getPassword();
		long id = dbUser.getId();

		dbUser.merge(user);
		dbUser.setId(id);
		dbUser.setPassword(password);
		userPhotoSrvc.updateAll(user.getImageUrls(), dbUser.getId(), 1l);
		
		UUserAbs retUser = new UUser(userRepo.saveAndFlush(dbUser));
		setLists(dbUser);
		return retUser;
	}

	@Override
	public List<UUserAbs> get(Collection<String> emails) {
		List<UUserAbs> dbList = userRepo.findByEmailIn(emails);
		List<UUserAbs> safeList = new ArrayList<UUserAbs>();
		for (UUserAbs user : dbList) {
			safeList.add(new UUser(user));
		}
		setLists(safeList);
		return safeList;
	}
	
	@Override
	public List<UUserAbs> clean(List<UUserAbs> users) {
		return users;
	}


	@Override
	public UUserAbs updateSrvc(UUserAbs user) throws Exception {
		return update(user);
	}
	
	public HttpHeaders getHeaders(UUserAbs user) {
		HttpHeaders httpHeaders = new HttpHeaders();
	    if (user != null) {
		    httpHeaders.add(AopAuth.EMAIL, "" + user.getEmail());
		    httpHeaders.add(AopAuth.PASSWORD, user.getPassword());
		    httpHeaders.add(AopAuth.TOKEN, user.getToken());
	    }
	    return httpHeaders;
	}
}
