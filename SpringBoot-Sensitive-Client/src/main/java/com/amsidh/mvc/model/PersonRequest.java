package com.amsidh.mvc.model;

import com.amsidh.mvc.util.CryptoUtil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PersonRequest implements Serializable {
    @NotNull
    @NotBlank
    private String username;

    private String emailId;

    @NotNull
    @NotBlank
    private String password;

    public void encryptSensitiveData() {
        this.username = CryptoUtil.encrypt(this.username);
        this.password = CryptoUtil.encrypt(this.password);
    }

    public void decryptSensitiveData() {
        this.username = CryptoUtil.decrypt(this.username);
        this.password = CryptoUtil.decrypt(this.password);
    }
}
