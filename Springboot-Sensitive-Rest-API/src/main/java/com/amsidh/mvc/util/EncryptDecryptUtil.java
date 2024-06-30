package com.amsidh.mvc.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Component
@Slf4j
public class EncryptDecryptUtil {

    private static final String ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String PROVIDER = "BC";

    private static Resource PRIVATE_KEY_RESOURCE;
    private static Resource PUBLIC_KEY_RESOURCE;

    private static PrivateKey loadPrivateKey() throws IOException {
        PEMParser pemParser = new PEMParser(new FileReader(PRIVATE_KEY_RESOURCE.getFile().getAbsolutePath()));
        Object object = pemParser.readObject();
        pemParser.close();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(PROVIDER);
        return converter.getPrivateKey(((PEMKeyPair) object).getPrivateKeyInfo());
    }

    private static PublicKey loadPublicKey() throws IOException {
        PEMParser pemParser = new PEMParser(new FileReader(PUBLIC_KEY_RESOURCE.getFile().getAbsolutePath()));
        Object object = pemParser.readObject();
        pemParser.close();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(PROVIDER);
        return converter.getPublicKey((org.bouncycastle.asn1.x509.SubjectPublicKeyInfo) object);

    }

    public static String encrypt(String message) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, loadPublicKey());
            return Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException e) {
            log.info("Unable to encrypt {}", e.getMessage(), e);
            throw new RuntimeException("Unable to encrypt");
        }
    }

    public static String decrypt(String encryptedMessage) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, loadPrivateKey());
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException e) {
            log.info("Unable to decrypt {}", e.getMessage(), e);
            throw new RuntimeException("Unable to decrypt");
        }
    }

    //Springboot property injection for static variable
    @Value("${rsa.private-key}")
    public void setPrivateKeyResource(Resource privateKeyResource) {
        PRIVATE_KEY_RESOURCE = privateKeyResource;
    }

    //Springboot property injection for static variable
    @Value("${rsa.public-key}")
    public void setPublicKeyResource(Resource publicKeyResource) {
        PUBLIC_KEY_RESOURCE = publicKeyResource;
    }




}







