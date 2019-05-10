package com.userSrvc.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.userSrvc.server.utils.GenUtils;

@SpringBootApplication
@ComponentScan(basePackages = {"com.userSrvc"})
public class ServerApplication {

	private static void cloudSetupHardCoded(String...args) {
		SpringApplication application = new SpringApplication(ServerApplication.class);
		Properties properties = new Properties();
		String dbUrl;																														// = "jdbc:oracle:thin:@" + GenUtils.getPassword("DB_URL", token, configPort);
		dbUrl="jdbc:oracle:thin:@db201902042116_low";
		String dbUser = "HLWA";																												//GenUtils.getPassword("DB_USER", token, configPort);
		String dbpass = "iey0jee3souKaisheiJu5kaichishi";
		properties.put("spring.datasource.password", dbpass);
		properties.put("spring.datasource.url", dbUrl);
		properties.put("spring.datasource.username", dbUser);
		properties.put("spring.datasource.driverClassName", properties.get("oracle.driverClassName"));
		properties.put("spring.jpa.database-platform", properties.get("oracle.database-platform"));

		System.out.println("DB URL: '" + dbUrl + "'");
		System.out.println("DB USER: '" + dbUser + "'");
		System.out.println("DB PASS: 'youllJustHaveToTrustMe'");

		application.setDefaultProperties(properties);
		application.run(args);
	}

	private static void oracleLocalSetup(String...args) throws IOException {
		SpringApplication application = new SpringApplication(ServerApplication.class);
		Properties properties = new Properties();

	if (args.length > 0) {
		String token = args[0];
		String configPort = args [1];
		retrieveConfig(token, configPort, properties);
	} else {
		InputStream input = null;
		input = new FileInputStream("./src/main/resources/application.properties");
		properties.load(input);
		input = new FileInputStream("./src/main/resources/application-test.properties");
		properties.load(input);
	}
	
		properties.put("spring.datasource.driverClassName", properties.get("oracle.driverClassName"));
		properties.put("spring.jpa.database-platform", properties.get("oracle.database-platform"));
		properties.put("spring.datasource.url", properties.get("oracle.url"));
		
		application.setDefaultProperties(properties);
		application.run(args);
	}
	
	private static void mySqlLocalSetup(String...args) throws IOException {
		SpringApplication application = new SpringApplication(ServerApplication.class);
		Properties properties = new Properties();

		if (args.length > 0) {
			String token = args[0];
			String configPort = args [1];
			retrieveConfig(token, configPort, properties);
		} else {
			InputStream input = null;
			input = new FileInputStream("./src/main/resources/application.properties");
			properties.load(input);
			input = new FileInputStream("./src/main/resources/application-test.properties");
			properties.load(input);
		}
	
		properties.put("spring.datasource.url", properties.get("mySql.url"));
		
		application.setDefaultProperties(properties);
		application.run(args);
	}
	
	private static void retrieveConfig(String token, String configPort, Properties properties) {
		String port = GenUtils.getPassword("PORT", token, configPort);
		String dbUrl = "jdbc:oracle:thin:@" + GenUtils.getPassword("DB_URL", token, configPort);
		String dbUser = GenUtils.getPassword("DB_USER", token, configPort);
		String dbpass = GenUtils.getPassword("dbPass", token, configPort);

		properties.put("oracle.password", dbpass);
		properties.put("oracle.url", dbUrl);
		properties.put("oracle.username", dbUser);
		properties.put("server.port", port);	
	}

	public static void main(String[] args) throws IOException {
//		cloudSetupHardCoded(args);
//		 oracleLocalSetup(args);
		mySqlLocalSetup(args);
	}
}
