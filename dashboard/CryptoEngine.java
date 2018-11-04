/* This is the main encryption class, you call it using an 'operation' value
 * which specifies what you want to do: 
 * 
 * You can change settings such as the algorithm in the UserSettings
 * 
 * Sources:
 * [1] - http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html
 * [2] - http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html
 * [3] - http://en.wikipedia.org/wiki/Block_cipher_mode_of_operation
 * */
package dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import logs.Log;

import org.apache.commons.codec.binary.Base64;

public class CryptoEngine 
{    
    // Used to directories, lists all inner files
    private static ArrayList<String> fileList = new ArrayList<String>();
    
    public static String algo = "AES"; // Algorithm to use, edit in UserSettings
    private static int keyLength;
    private static byte[] ivBytes;
    private static Cipher cipher;
    
    public static void algorithmInit()
    {
	if(algo == "DES")
	{
	    ivBytes = new byte[8];
	    keyLength = 64;
	}
	else if (algo == "AES") // Max 256
	{
	    ivBytes = new byte[16];
	    keyLength = 128;
	}
	else if (algo == "Blowfish") // Max 448
	{
	    ivBytes = new byte[8];
	    keyLength = 128;
	}
	else if (algo == "DESede")
	{
	    ivBytes = new byte[8];
	    keyLength = 192;
	}
	
	try {
	    cipher = Cipher.getInstance(algo + "/CBC/PKCS5Padding");
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	} catch (NoSuchPaddingException e) {
	    e.printStackTrace();
	}
    }
    
    // Encrypts text
    public static String encryptText(String password, String data)
    {
	String output = null;
	algorithmInit();
	new SecureRandom().nextBytes(ivBytes);
	byte[] salt = ivBytes;
	byte[] inputBytes = data.getBytes();
	
	// Writes operatings to the log. 
	Log.appendText("> Encrypting text (using " + algo + ") : " + data + "\n");
			
	try {

	    // Specifies how the key will be built
	    SecretKeyFactory method = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    
	    /* Generates a strong binary key from a user character password, a salt and iterations. */
	    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, keyLength);
	    
	    /* Takes the key specification as bytes and creates the key using the
	     * selected algorithm. */
	    SecretKey secretKey = method.generateSecret(spec);
	    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), algo);
	    
	    /* Secure random set of bytes used in the cryptographic process to 
	     * ensure the first block does not look the same if two equal messages
	     * were computed [3] */
	    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);	   
	    
	    /* Specifies the algorithm in use, mode of operation which makes use 
	     * of the IV and the padding scheme. The IV is used to randomise the
	     * first block of data. [1] */
	    cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec); // Takes in the key and IV
	    byte[] ciphertext = cipher.doFinal(inputBytes);
	   
	    // Output in the form of "IV:ciphertext"
	    output = new Base64().encodeToString(ivBytes) + ":" + 
		     new Base64().encodeToString(ciphertext);
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
			
	Log.appendText("> Finished text encryption: " + data + "\n\n");
	return output;
    }
    
    public static String decryptText(String password, String data)
    {
	Log.appendText("> Decrypting text: (using " + algo + ") : " + data + "\n");
	
	// Splits the input where the ":" occures
	String[] part = data.split(":");
	String output = null;
	algorithmInit();
	new SecureRandom().nextBytes(ivBytes);
	byte[] salt = new Base64().decode(part[0]);
	
	try {
	    IvParameterSpec internalIvSpec = new IvParameterSpec(new Base64().decode(part[0]));
	   
	    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, keyLength);   
	    SecretKeyFactory method = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    SecretKey secretKey = method.generateSecret(spec);
	    SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), algo);
	    
	    cipher.init(Cipher.DECRYPT_MODE, secret, internalIvSpec);
	
	    byte[] ciphertext = new Base64().decode(part[1]);
	    byte[] plaintext = cipher.doFinal(ciphertext);
	
	    // Returns plaintext
	    output = new String(plaintext);
	} catch (Exception e) {
	    e.printStackTrace();
	    Dashboard.setWarning("Did you type the right password?");
	}
	
	Log.appendText("> Finished text decryption: " + data + "\n\n");
	return output;
    }
    
    public static void encryptFileFolder(String password, String data)
    {
	fileList.clear();
	algorithmInit();
	new SecureRandom().nextBytes(ivBytes);
	byte[] salt = ivBytes;
	
	// Finds all files in directory
	checkDirectory(new File(data));
	int successCount = 0;
	System.out.println("Size: " + fileList.size());
		
	// Loops through file list and encrypts each individually
	for (int j = 0; j < fileList.size(); j++)
	{
	    Log.appendText("> Encrypting file: (using " + algo + ") : " + fileList.get(j) + "\n");
	    
	    updateProgBar(j + 1);
	    	
	    try {
		FileInputStream inputStream = new FileInputStream(fileList.get(j));	
				   
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, keyLength);   
		SecretKeyFactory method = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey secretKey = method.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), algo);
		
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		    
		cipher.init(Cipher.ENCRYPT_MODE, secret, ivSpec);
		
    	    	/* Creates a new file with .secure appended, doesn't overwrite original file
    	    	 * as if error occures during process the file would become corrupt */	    		
		
		FileOutputStream secureOut = new FileOutputStream(fileList.get(j) + ".secure");
		CipherOutputStream cos = new CipherOutputStream(secureOut, cipher);
    		
		byte[] block = new byte[8];
		int i = 0;
    	    
		// Writes IV to beginning of file.
		secureOut.write(ivBytes);
    		
    	    	/* Loops through input file, copies data to output.secure file
    	    	 * doing 8 bytes at a time using the CipherOutputStream method
    	     	* for encrypting the data before writing. */
    	    	while ((i = inputStream.read(block)) != -1) 
    	    	{
    	    	    cos.write(block, 0, i);
    	    	}	
    	    	
    	    	cos.flush();
    	    	cos.close();
    	    	inputStream.close();
    	    	
    	    	
	    } catch (Exception e) {
		System.out.println(e);
	    }
		
	    Log.appendText("> File encryption output: " + fileList.get(j) + ".secure" + "\n");
		
	    // Deletes old file
	    File oldFile = new File(fileList.get(j));
	    oldFile.delete();
		
	    Log.appendText("> Finished cleaning up file. \n\n");
	    successCount++;
	}
	Log.appendText("> DONE - " + fileList.size() + " files affected. \n\n");
    }
    
    public static void decryptFileFolder(String password, String data) 
    {
	fileList.clear();
	algorithmInit();

	checkDirectory(new File(data));
	int successCount = 0;
	try {
	    for (int j = 0; j < fileList.size(); j++)
	    {
		Log.appendText("> Decrypting file: (using " + algo + ") : " + fileList.get(j) + "\n");
	    
		updateProgBar(j + 1);
	    
		FileInputStream inputStream = new FileInputStream(fileList.get(j));
	
		// Extracts the IV from the file, size depends on algorithm
		byte[] myIv = new byte[ivBytes.length];
		inputStream.read(myIv, 0, ivBytes.length);
		byte[] salt = myIv;
	    	
		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 10000, keyLength);   
		SecretKeyFactory method = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		SecretKey secretKey = method.generateSecret(spec);
		SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), algo);
		
		cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(myIv));
	    		
		int firstDot = fileList.get(j).indexOf(".");
	    	int lastDot = fileList.get(j).lastIndexOf(".");
	    	String oldFileName = fileList.get(j);
	
	    	if (firstDot != lastDot && lastDot != -1)
	    	{
	    	    oldFileName = fileList.get(j).substring(0, lastDot);
	    	}
	    	
	    	CipherOutputStream cos = new CipherOutputStream(new FileOutputStream(oldFileName), cipher);

	    	byte[] block = new byte[8];
	    	int i = ivBytes.length;
	
	    	while ((i = inputStream.read(block)) != -1) 
	   	{
	    	    cos.write(block, 0, i);
	   	} 
	    	
	    	cos.flush();
	    	cos.close();
	    	inputStream.close();
	    	
	
	    	Log.appendText("> File decryption output: " + oldFileName + "\n");
	
	   	File oldFile = new File(fileList.get(j));
	   	oldFile.delete();
	
	   	Log.appendText("> Finished cleaning up file. \n\n");
	   	successCount++;
	    }
	    Log.appendText("> DONE - " + fileList.size() + " files affected. \n\n");
	    System.out.println(successCount + " / " + fileList.size() + " files processed.");
	
	} catch (Exception e) {
	    e.printStackTrace();
	    Dashboard.setWarning("Did you type the right password?");
	}
    }
    
    private static void updateProgBar(final int current)
    {
	new Thread() {
	    public void run() {
		Log.mainBar.setValue((Math.round(((float)100 / fileList.size()) * (current + 1))));
		Log.mainBar.revalidate();
	    }
	}.start();
    }
    
    /* Checks passed file, adds all single files to an arraylist,
     * traverses all directories to find files within 
     * (including .hidden files). */
    private static void checkDirectory(File node)
    {
	if (node.isDirectory())
	{
	    for (File child : node.listFiles()) 
	    {   	
	   	if (child.isDirectory())
	   	{
	   	    checkDirectory(child);
	   	}
	   	else
	   	{
	   	    fileList.add(child.toString());
	   	}
	    }
	}
	else 
	{
	    fileList.add(node.toString());
	}
    }
}
