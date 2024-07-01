package com.amsidh.mvc.controller;

import com.amsidh.mvc.util.EncryptDecryptUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EncryptDecryptController {


    @PostMapping(value = "/encrypt", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public String encryptMessage(@RequestBody String message) {
        return EncryptDecryptUtil.encrypt(message);
    }

    @PostMapping(value = "/decrypt", consumes = {MediaType.TEXT_PLAIN_VALUE})
    public String decryptMessage(@RequestBody String message) {
        return EncryptDecryptUtil.decrypt(message);
    }


}
