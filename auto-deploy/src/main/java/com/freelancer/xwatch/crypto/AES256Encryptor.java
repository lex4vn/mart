package com.freelancer.xwatch.crypto;

import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.*;

import com.google.common.io.*;

public class AES256Encryptor implements ITokenEncryptor {
    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private String key;
    private String iv;

    public AES256Encryptor(String key, String iv) throws Exception {
        if (!checkJavaCryptographyExtension()) {
            throw new Exception("Current Cipher doesn't allow AES key size 256. Java Cryptography Extension is missing or invalid.");
        }
        this.key = key.trim();
        this.iv = iv.trim();
    }

    private boolean checkJavaCryptographyExtension() {
        try {
            return (Cipher.getMaxAllowedKeyLength("AES") >= 256);
        } catch (NoSuchAlgorithmException e) {
            return false;
        }
    }

    @Override
    public String encrypt(String data) throws Exception {

        Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

        byte[] encVal = cipher.doFinal(data.getBytes());
        String encryptedValue = BaseEncoding.base64().encode(encVal);
        return encryptedValue;

    }

    @Override
    public String decrypt(String encryptedData) throws Exception {

        Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

        byte[] decordedValue = BaseEncoding.base64().decode(encryptedData);
        byte[] decValue;
        decValue = cipher.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;

    }

    private Cipher getCipher(int mode) throws Exception {
        Key key = getSecretKey();

        Cipher c = Cipher.getInstance(ALGO);
        c.init(mode, key, new IvParameterSpec(iv.getBytes()));
        return c;

    }

    private Key getSecretKey() throws Exception {

        // hash the secret by SHA 256 to make sure secret key's length
        MessageDigest digester = MessageDigest.getInstance("SHA-256");
        digester.update(String.valueOf(key).getBytes("UTF-8"));
        byte[] hash = digester.digest();
        SecretKeySpec spec = new SecretKeySpec(hash, "AES");
        return spec;

    }
}
