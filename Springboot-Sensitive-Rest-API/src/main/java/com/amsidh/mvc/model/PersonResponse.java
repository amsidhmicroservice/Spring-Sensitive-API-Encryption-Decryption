package com.amsidh.mvc.model;

import com.amsidh.mvc.util.EncryptDecryptUtil;
import lombok.*;

import java.io.Serializable;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PersonResponse implements Serializable {
    private String username;
    private String message;

    public void encryptSensitiveData() throws Exception {
        this.username = Base64.getEncoder().encodeToString(EncryptDecryptUtil.encrypt(this.username));
    }
}
