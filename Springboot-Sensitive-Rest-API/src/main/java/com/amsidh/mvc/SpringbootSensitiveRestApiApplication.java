package com.amsidh.mvc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.security.Security;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringbootSensitiveRestApiApplication {

    public static void main(String[] args) {
        // Register BouncyCastle provider
        Security.addProvider(new BouncyCastleProvider());
        SpringApplication.run(SpringbootSensitiveRestApiApplication.class, args);
    }

}
