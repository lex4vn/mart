package com.freelancer.xwatch.crypto;

public interface ITokenEncryptor {
    public String encrypt(String raw) throws Exception;

    public String decrypt(String encryptedData) throws Exception;
}
