package com.amsidh.mvc.model;

import com.amsidh.mvc.util.CryptoUtil;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PersonResponse implements Serializable {
    private String username;
    private String message;

    public void encryptSensitiveData() {
        this.username = CryptoUtil.encrypt(this.username);
    }

    public void decryptSensitiveData() {
        this.username = CryptoUtil.decrypt(this.username);
    }
}
