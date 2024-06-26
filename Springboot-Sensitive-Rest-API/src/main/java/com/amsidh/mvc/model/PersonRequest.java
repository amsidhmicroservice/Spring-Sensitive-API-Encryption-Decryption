package com.amsidh.mvc.model;

import com.amsidh.mvc.util.CryptoUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 100)
    private String username;

    @Email
    private String emailId;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    public void decryptSensitiveData() {
        this.username = CryptoUtil.decrypt(this.username);
        this.password = CryptoUtil.decrypt(this.password);
    }
}
