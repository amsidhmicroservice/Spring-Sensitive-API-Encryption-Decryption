package com.amsidh.mvc.model;

import com.amsidh.mvc.annotation.Decrypt;
import com.amsidh.mvc.util.RSAUtil;
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
    @Decrypt
    private String username;
    private String message;

   public void encryptSensitiveData() throws Exception {
        this.username = Base64.getEncoder().encodeToString(RSAUtil.encrypt(this.username));
    }
}
