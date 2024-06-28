package com.amsidh.mvc.model;

import com.amsidh.mvc.annotation.Decrypt;
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

    @Decrypt
    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @Decrypt
    @Email
    private String emailId;

    @Decrypt
    @NotNull
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

}
