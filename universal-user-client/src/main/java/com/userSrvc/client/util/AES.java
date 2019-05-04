package com.userSrvc.client.util;

/**
 *  Reorganized the class found here:
 *  https://howtodoinjava.com/security/java-aes-encryption-example/
 */

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AES {
	@Value("${uu.enc.passphrase:defaultPhrase}")
	public String secret;
	private SecretKeySpec secretKey;
    private byte[] key;
    private static Cipher encCipher;
    private static Cipher decCipher;
    
    @PostConstruct
    public void init() {
        try {
        	setKey(secret);
	        
        	encCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			encCipher.init(Cipher.ENCRYPT_MODE, secretKey);
			
	        decCipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
	        decCipher.init(Cipher.DECRYPT_MODE, secretKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
    private void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt)
    {
        try
        {
            return Base64.getEncoder().encodeToString(encCipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt)
    {
        try
        {
            return new String(decCipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
}