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
     * AES 암호화/복호화를 위해 SecretKey를 가져오는 메서드입니다. 이 메서드는 애플리케이션 프로퍼티 파일에서 AES_KEY를 가져온 후, Base64로 디코딩하여 byte 배열로 변환합니다. 그런 다음, byte 배열과
     * ALGORITHM(AES)을 사용하여 SecretKeySpec 객체를 생성합니다.
     *
     * @return AES 암호화/복호화에 사용할 SecretKey 객체.
     */
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(AES_KEY);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    /**
     * 이 메서드는 AES 암호화 알고리즘을 사용하여 입력 데이터를 암호화합니다.
     *
     * @param data 암호화할 평문 데이터.
     * @return Base64로 인코딩된 암호화된 데이터 문자열.
     * @throws RuntimeException 암호화 중에 오류가 발생할 경우.
     */
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

    /**
     * 이 메서드는 AES 복호화 알고리즘을 사용하여 입력 암호화된 데이터를 복호화합니다.
     *
     * @param encryptedData Base64로 인코딩된 암호화된 데이터 문자열. 복호화할 것입니다.
     * @return 복호화된 평문 데이터로, String 형식입니다.
     * @throws RuntimeException 복호화 중에 오류가 발생할 경우.
     */
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
