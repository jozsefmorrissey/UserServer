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

//    @Bean("jasyptStringEncryptor")
//    public StringEncryptor stringEncryptor() {
//        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
//        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
//        String password = GenUtils.getSalt(token);
//        config.setPassword(password);
//        config.setAlgorithm("PBEWithMD5AndDES");
//        config.setKeyObtentionIterations("1000");
//        config.setPoolSize("1");
//        config.setProviderName("SunJCE");
//        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
//        config.setStringOutputType("base64");
//        encryptor.setConfig(config);
//        return encryptor;
//    }
	private void addProperties() {
//      if (args.length > 0) {
//				String token = args[0];
//				String configPort = args [1];
//				String port = GenUtils.getPassword("PORT", token, configPort);
//				System.out.println(dbpass + ":" + dbUser + ":" + dbUrl + ":" + port);
//	//	      properties.put("spring.datasource.password", "ENC(" + dbPassEnc + ")");
//				properties.put("server.port", port);
//		} else {
//			InputStream input = null;
//			input = new FileInputStream("./src/main/resources/application-test.properties");
//			properties.load(input);
//		}
	}

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

		System.out.println("DB URL: '" + dbUrl + "'");
		System.out.println("DB USER: '" + dbUser + "'");
		System.out.println("DB PASS: 'youllJustHaveToTrustMe'");

		application.setDefaultProperties(properties);
		application.run(args);
	}

	private static void setup(String...args) throws IOException {
		SpringApplication application = new SpringApplication(ServerApplication.class);
		Properties properties = new Properties();

	if (args.length > 0) {
		String token = args[0];
		String configPort = args [1];
		String port = GenUtils.getPassword("PORT", token, configPort);
		String dbUrl = "jdbc:oracle:thin:@" + GenUtils.getPassword("DB_URL", token, configPort);
		String dbUser = GenUtils.getPassword("DB_USER", token, configPort);
		String dbpass = GenUtils.getPassword("dbPass", token, configPort);

		properties.put("spring.datasource.password", dbpass);
		properties.put("spring.datasource.url", dbUrl);
		properties.put("spring.datasource.username", dbUser);

		System.out.println("DB URL: '" + dbUrl + "'");
		System.out.println("DB USER: '" + dbUser + "'");
		System.out.println("DB PASS: '" + dbpass + "'");
		System.out.println(dbpass + ":" + dbUser + ":" + dbUrl + ":" + port);
	//	      properties.put("spring.datasource.password", "ENC(" + dbPassEnc + ")");
		properties.put("server.port", port);
	} else {
		InputStream input = null;
		input = new FileInputStream("./src/main/resources/application.properties");
		properties.load(input);
		input = new FileInputStream("./src/main/resources/application-test.properties");
		properties.load(input);
	}

		application.setDefaultProperties(properties);
		application.run(args);

	}


	public static void main(String[] args) throws IOException {
//		cloudSetupHardCoded(args);
		 setup(args);
	}
}
