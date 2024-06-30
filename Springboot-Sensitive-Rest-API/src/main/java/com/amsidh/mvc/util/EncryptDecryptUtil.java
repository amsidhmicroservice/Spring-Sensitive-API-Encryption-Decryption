package com.amsidh.mvc.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

@Component
@Slf4j
public class EncryptDecryptUtil {

    private static final String ALGORITHM = "RSA";
    private static final String PROVIDER = "BC";

    private static Resource PRIVATE_KEY_RESOURCE;
    private static Resource PUBLIC_KEY_RESOURCE;

    private static PrivateKey getPrivateKey() throws Exception {
        PEMParser pemParser = new PEMParser(new FileReader(PRIVATE_KEY_RESOURCE.getFile().getAbsolutePath()));
        Object object = pemParser.readObject();
        if (object instanceof org.bouncycastle.openssl.PEMKeyPair pemKeyPair) {
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(PROVIDER);
            return converter.getPrivateKey(pemKeyPair.getPrivateKeyInfo());
        } else {
            throw new IllegalArgumentException("Unsupported private key format: " + object.getClass());
        }
    }

    private static PublicKey getPublicKey() throws Exception {
        PEMParser pemParser = new PEMParser(new FileReader(PUBLIC_KEY_RESOURCE.getFile().getAbsolutePath()));
        SubjectPublicKeyInfo publicKeyInfo = (SubjectPublicKeyInfo) pemParser.readObject();
        byte[] keyBytes = publicKeyInfo.getEncoded();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(spec);
    }

    public static byte[] encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());
            return cipher.doFinal(data.getBytes());
        } catch (Exception e) {
            log.error("Error encrypting data: {}", e.getMessage(), e);
            throw new RuntimeException("Encryption error", e);
        }
    }

    public static String decrypt(byte[] data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
            return new String(cipher.doFinal(data));
        } catch (Exception e) {
            log.error("Error decrypting data: {}", e.getMessage(), e);
            throw new RuntimeException("Decryption error", e);
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







