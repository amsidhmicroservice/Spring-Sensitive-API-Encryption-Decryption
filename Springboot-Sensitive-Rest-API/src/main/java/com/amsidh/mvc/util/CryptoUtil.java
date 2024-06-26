package com.amsidh.mvc.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
public class CryptoUtil {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY = "ThisIsASecretK12"; // Ensure this is 16, 24, or 32 characters long

    private static SecretKeySpec getSecretKeySpec() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String value) {

        try {
            SecretKeySpec keySpec = getSecretKeySpec();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException |
                 NoSuchAlgorithmException exception) {
            log.error("Encryption failed with an error message {}", exception.getMessage(), exception);
            throw new RuntimeException(String.format("Encryption failed with an error message is %s", exception.getMessage()));
        }
    }

    public static String decrypt(String encryptedValue) {
        try {
            SecretKeySpec keySpec = getSecretKeySpec();
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedValue);
            byte[] decrypted = cipher.doFinal(decodedBytes);
            return new String(decrypted);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchPaddingException |
                 NoSuchAlgorithmException exception) {
            log.error("Decryption failed with an error message {}", exception.getMessage(), exception);
            throw new RuntimeException(String.format("Decryption failed with an error message is %s", exception.getMessage()));
        }
    }
}
