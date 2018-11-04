/* Provides the encryption for the application password 
 * Source of Info: http://docs.oracle.com/javase/7/docs/technotes/guides/security/SunProviders.html
 * */
package config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import dashboard.Dashboard;

public class Security {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    private static String decryptedString;
    private static String encryptedString;
    @SuppressWarnings("unused")
    private static String strToDecrypt;
    private static String output = "null";

    /*
     */

    public static String Encryption(String action, String strToProcess, String keyPassword) 
    {
	if (action.equals("en")) // Encryption process
	{
	    Security.setKey(keyPassword); // Sets the encryption key as the user password
	    Security.encrypt(strToProcess.trim());
	    output = Security.getEncryptedString();
	} else if (action.equals("de")) // Decryption process
	{
	    Security.setKey(keyPassword);
	    strToDecrypt = strToProcess;
	    Security.decrypt(strToProcess.trim());
	    output = Security.getDecryptedString();
	}
	return output; // Return encrypted or decrypted string
    }

    private static void setKey(String myKey) // Sets the sha-1 key
    {
	MessageDigest sha = null;
	try {
	    key = myKey.getBytes("UTF-8"); // Returns password in bytes
	    sha = MessageDigest.getInstance("SHA-1");
	    key = sha.digest(key);
	    key = Arrays.copyOf(key, 16); // use only first 128 bit
	    secretKey = new SecretKeySpec(key, "AES");

	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
    }

    // Setters and getters for the various actions on the strings
    private static String getDecryptedString() {
	return decryptedString;
    }

    private static void setDecryptedString(String decryptedString) {
	Security.decryptedString = decryptedString;
    }

    private static String getEncryptedString() {
	return encryptedString;
    }

    private static void setEncryptedString(String encryptedString) {
	Security.encryptedString = encryptedString;
    }

    private static String encrypt(String strToEncrypt) {
	try {
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

	    cipher.init(Cipher.ENCRYPT_MODE, secretKey);

	    setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));
	} catch (Exception e) {
	    System.out.println("Error while encrypting: " + e.toString());
	}
	return null;
    }

    private static String decrypt(String strToDecrypt) {
	try {
	    Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");

	    cipher.init(Cipher.DECRYPT_MODE, secretKey);
	    setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));

	} catch (Exception e) {
	    Dashboard.setOutText("Problem Decrypting Text, Check Password.");
	    System.out.println("Error while decrypting: " + e.toString());
	}
	return null;
    }

}