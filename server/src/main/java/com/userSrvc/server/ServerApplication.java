package com.userSrvc.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.userSrvc.server.utils.GenUtils;

@SpringBootApplication
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

	public static void main(String[] args) throws IOException {
        SpringApplication application = new SpringApplication(ServerApplication.class);
        Properties properties = new Properties();
		String dbUrl;// = "jdbc:oracle:thin:@" + GenUtils.getPassword("DB_URL", token, configPort);
		dbUrl="jdbc:oracle:thin:@qfxvhm8okemwoc8_db201902042116_low?TNS_ADMIN=/home/ubuntu/Wallet/";
		String dbpass;// = GenUtils.getPassword("dbPass", token, configPort);
		dbpass = "iey0jee3souKaisheiJu5kaichishi";
		String dbUser = "HLWA";//GenUtils.getPassword("DB_USER", token, configPort);
		properties.put("spring.datasource.password", dbpass);
		properties.put("spring.datasource.url", dbUrl);
		properties.put("spring.datasource.username", dbUser);

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

        application.setDefaultProperties(properties);
        application.run(args);
	}
}
