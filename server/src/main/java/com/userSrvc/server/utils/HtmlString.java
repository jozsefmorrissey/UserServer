package com.userSrvc.server.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class HtmlString {
	
	public static void main(String...args) {
//		Properties props = new Properties();
//		try {
//			props.load(new FileInputStream("./src/main/resources/static/UserSrvc.properties"));
//			StandardPBEStringEncryptor sPEBse = new StandardPBEStringEncryptor();
//			String salt = GenUtils.getPassword("propertySalt", );
//			sPEBse.setPassword(salt);
//			EncryptableProperties ep = new EncryptableProperties(props, sPEBse);
//			System.out.println(ep.getProperty("world"));
//			String cmd = "sudo -S bash /usr/bin/confidentalInfo.sh value UserSrvc propertySalt";
//			Process proc = Runtime.getRuntime().exec(cmd);
//			proc.getOutputStream().write("morrissey".getBytes());
//			BufferedReader bf = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec(cmd).getInputStream()));
//
//			String line = "";
//			while((line = bf.readLine()) != null) {
//				System.out.println(line);
//			}
//
//			System.out.println("success");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private HashMap<String, Object> scope;
	private String htmlString;

	public HtmlString (HashMap<String, Object> scope, String fileLoc) {
		this.scope = scope;
		htmlString = process(fileLoc);
	}

	public HtmlString (String fileLoc) {
		scope = new HashMap<String, Object>();
		htmlString = process(fileLoc);
	}

	private String process(String fileLoc) {
		try {
			String html = new String(Files.readAllBytes(Paths.get(fileLoc)));
			html = StringUtils.replaceKeyValues(html, scope, "{{", "}}");
			return html;
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Override
	public String toString() {
		return htmlString;
	}
}
