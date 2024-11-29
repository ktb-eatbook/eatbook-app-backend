package com.ktb.eatbookappbackend.global.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESUtil {

    @Value("${app.properties.aesKey}")
    private String AES_KEY;

    private static final String ALGORITHM = "AES";

    /**
     * Base64로 인코딩된 키를 바이트 배열로 디코딩하여 SecretKey 생성
     */
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    public String encrypt(String data) {
        try {
            SecretKey secretKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            SecretKey secretKey = getSecretKey();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}
