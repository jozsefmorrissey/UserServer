package com.userSrvc.server;

import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.userSrvc.server.utils.GenUtils;

@SpringBootApplication
public class ServerApplication {

	private static String token = "ooC9phie4Gisie3iu4ahShaigei5p";

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

	public static void main(String[] args) {
		if (args.length > 0) {
			token = args[0];
		}
		
        SpringApplication application = new SpringApplication(ServerApplication.class);

        Properties properties = new Properties();
        String dbpass = GenUtils.getPassword("dbpass", token);
//      properties.put("spring.datasource.password", "ENC(" + dbPassEnc + ")");
        properties.put("spring.datasource.password", dbpass);
        application.setDefaultProperties(properties);

        application.run(args);
	}
}
