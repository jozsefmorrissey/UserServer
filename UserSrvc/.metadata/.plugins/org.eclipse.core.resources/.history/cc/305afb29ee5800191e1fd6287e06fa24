package com.userSrvc.server.service.impl;

import java.util.HashMap;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.repo.UserRepo;
import com.userSrvc.server.service.UserSrvc;
import com.userSrvc.server.utils.GenUtils;
import com.userSrvc.server.utils.HtmlString;

@Service
public class UserSrcvImpl implements UserSrvc {

	@Autowired
	UserRepo userRepo;

	@Override
	public void addUser(User user) throws Exception {
		validateEmail(user);
		User dbUser = userRepo.getByEmail(user.getEmail());
		if (dbUser != null) {
			throw new Exception("Email address already registered.");
		}
		validateUsername(user);
		validatePassword(user);
		
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		user.setPassword(hashed);
		userRepo.save(user);
	}
	
	private String setToken(User user) {
		User dbUser = userRepo.getByEmail(user.getEmail());
		String token = GenUtils.randStringSecure(256);
		dbUser.setUserToken(token);
		userRepo.save(dbUser);
		return token;
	}

	@Override
	public User loginUser(User user) throws Exception {
		User dbUser = userRepo.getByEmail(user.getEmail());
		if (dbUser == null) {
			throw new Exception("Invalid Email");
		}
		if (!BCrypt.checkpw(user.getPassword(), dbUser.getPassword())) {
			throw new Exception("Invalid password");
		}

		user.setUserToken(setToken(dbUser));
		user.setPassword(null);
		return user;
	}

	private User authinticate(User user, boolean external) throws Exception {
		if (user.getUserToken() == null) {
			throw new Exception("No user token provided");
		}
		validateEmail(user);
		User u = userRepo.getByEmail(user.getEmail());
		if (u != null && user != null && user.getUserToken().equals(u.getUserToken())) {
			if (external) {
				u.setPassword(null);
			}
			return u;
		}
		
		throw new Exception("Access Denied");
	}

	@Override
	public User authinticate(User user) throws Exception {
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
	public User getUser(String email) throws Exception {
		User user = userRepo.getByEmail(email);
		if (user == null) {
			throw new Exception("Invalid Email");
		}

		user.setPassword(null);
		user.setUserToken(null);
		return user;
	}

	@Override
	public void updatePassword(User user) throws Exception {
		User dbUser = authinticate(user, false);
		validatePassword(user);
		String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
		dbUser.setPassword(hashed);
		setToken(dbUser);
		userRepo.save(dbUser);
	}

	@Override
	public void updateEmail(User user, String newEmail) throws Exception {
		validateEmail(newEmail);
		User dbUser = authinticate(user, false);
		dbUser.setEmail(newEmail);
		userRepo.save(dbUser);
	}

	@Override
	public void resetPassword(User user, String url) throws Exception {
		validateEmail(user);
		User dbUser = userRepo.getByEmail(user.getEmail());
		HashMap<String, Object> scope = new HashMap<String, Object>();
		if(dbUser == null) {
			throw new Exception("Email not registered");
		}

		String token = setToken(dbUser);
		String email = dbUser.getEmail();
		scope.put("name", dbUser.getName());
		scope.put("url", url + email + "/" + token);
		scope.put("token", token);
		HtmlString htmlString = new HtmlString(scope, "./src/main/resources/static/emailTemplates/password-reset.html");
		sendEmail(email, "Password Reset", htmlString.toString());
	}
	
	private boolean validateEmail(User user) throws Exception {
		return validateEmail(user.getEmail());
	}

	private boolean validateEmail(String email) throws Exception {
		Pattern pat = Pattern.compile(".{1,}@.{1,}\\..{1,}");
		if (pat.matcher(email) == null) {
			throw new Exception("Email format invalid");
		}
		return true;
	}
	
	// TODO: do to hashing on the ui level we cannot insure the length of the password.
	private boolean validatePassword(User user) {
		return true;
	}
	
	private boolean validateUsername(User user) throws Exception {
		String name = user.getName();
		if (name == null || name.equals("")) {
			throw new Exception ("Username must be defined");
		}
		return true;
	}
}
