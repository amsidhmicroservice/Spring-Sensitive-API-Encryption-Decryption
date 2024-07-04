package com.amsidh.mvc.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.StringReader;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Component
public class HybridEncryptionDecryptionUtil {
    public static final String RSA_ALGORITHM = "RSA";
    public static final Integer AES_KEY_SIZE = 256;

    public static final String AES_ALGORITHM = "AES";
    private static String PRIVATE_KEY_CONTENT;
    private static String PUBLIC_KEY_CONTENT;

    //Springboot property injection for static variable
    @Value("${rsa.private-key}")
    public void setPrivateKeyResource(String privateKeyContent) {
        PRIVATE_KEY_CONTENT = privateKeyContent;
    }

    //Springboot property injection for static variable
    @Value("${rsa.public-key}")
    public void setPublicKeyResource(String publicKeyContent) {
        PUBLIC_KEY_CONTENT = publicKeyContent;
    }

    public static String encrypt(String data) {
        try {
            //Generate random AES Key
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE);
            SecretKey randomAesSecretKey = keyGenerator.generateKey();

            // Encrypt data with AES key
            Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
            aesCipher.init(Cipher.ENCRYPT_MODE, randomAesSecretKey);
            byte[] encryptedData = aesCipher.doFinal(data.getBytes());

            // Encrypt AES key with RSA public key
            PublicKey publicKey = generatePublicKey();
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedAesKey = rsaCipher.doFinal(randomAesSecretKey.getEncoded());

            //Combine encrypted AES key and encrypted message
            String encryptedAESData = Base64.getEncoder().encodeToString(encryptedAesKey) + ":" + Base64.getEncoder().encodeToString(encryptedData);
            log.info("Encrypted data {}", encryptedAESData);
            return encryptedAESData;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.error("Unable to encrypt {}", e.getMessage(), e);
            throw new RuntimeException("Unable to encrypt");
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            String[] parts = encryptedData.split(":");
            byte[] encryptedRandomAesSecretKey = Base64.getDecoder().decode(parts[0]);
            byte[] encryptedMessage = Base64.getDecoder().decode(parts[1]);
            // Decrypt AES key with RSA private key
            PrivateKey privateKey = generatePrivateKey();
            Cipher rsaCipher = Cipher.getInstance(RSA_ALGORITHM);
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] randomAesSecretKey = rsaCipher.doFinal(encryptedRandomAesSecretKey);
            SecretKeySpec secretKeySpec = new SecretKeySpec(randomAesSecretKey, AES_ALGORITHM);

            //Decrypt message with AES key
            Cipher aesCipher = Cipher.getInstance(AES_ALGORITHM);
            aesCipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decryptedMessage = aesCipher.doFinal(encryptedMessage);
            String message = new String(decryptedMessage);
            return message;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException e) {
            log.error("Unable to decrypt {}", e.getMessage(), e);
            throw new RuntimeException("Unable to decrypt");
        }
    }


    public static PrivateKey generatePrivateKey() {
        try {
            KeyFactory factory = getKeyFactory();
            PemReader pemReader = new PemReader(new StringReader(PRIVATE_KEY_CONTENT));
            byte[] content = pemReader.readPemObject().getContent();
            return factory.generatePrivate(new PKCS8EncodedKeySpec(content));
        } catch (IOException | InvalidKeySpecException e) {
            log.error("Exception occurred in method generatePrivateKey with error message {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static PublicKey generatePublicKey() {
        try {
            KeyFactory factory = getKeyFactory();
            PemReader pemReader = new PemReader(new StringReader(PUBLIC_KEY_CONTENT));
            byte[] content = pemReader.readPemObject().getContent();
            return factory.generatePublic(new X509EncodedKeySpec(content));
        } catch (IOException | InvalidKeySpecException e) {
            log.error("Exception occurred in method generatePublicKey with error message {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private static KeyFactory getKeyFactory() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            return KeyFactory.getInstance("RSA", "BC");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            log.error("Exception occurred in method getKeyFactory with error message {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
