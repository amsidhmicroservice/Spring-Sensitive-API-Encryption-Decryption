package com.amsidh.mvc.model;

import com.amsidh.mvc.util.RSAUtil;
import lombok.*;

import java.io.Serializable;
import java.security.PublicKey;
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

    public void encryptSensitiveData(PublicKey publicKey) throws Exception {
        this.username = Base64.getEncoder().encodeToString(RSAUtil.encrypt(this.username, publicKey));
    }
}
