package com.amsidh.mvc;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Security;

@SpringBootTest
class SpringBootSensitiveClientApplicationTests {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @Test
    void contextLoads() {
    }

}
