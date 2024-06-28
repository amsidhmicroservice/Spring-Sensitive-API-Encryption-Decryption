package com.amsidh.mvc.model;

import com.amsidh.mvc.util.RSAUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PersonRequest implements Serializable {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @Email
    private String emailId;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    public void encryptSensitiveData(PublicKey publicKey) throws Exception {
        this.username = Base64.getEncoder().encodeToString(RSAUtil.encrypt(this.username, publicKey));
        this.emailId = Base64.getEncoder().encodeToString(RSAUtil.encrypt(this.emailId, publicKey));
        this.password = Base64.getEncoder().encodeToString(RSAUtil.encrypt(this.password, publicKey));
    }

    public void decryptSensitiveData(PrivateKey privateKey) throws Exception {
        this.username = RSAUtil.decrypt(Base64.getDecoder().decode(this.username), privateKey);
        this.emailId = RSAUtil.decrypt(Base64.getDecoder().decode(this.emailId), privateKey);
        this.password = RSAUtil.decrypt(Base64.getDecoder().decode(this.password), privateKey);
    }
}
