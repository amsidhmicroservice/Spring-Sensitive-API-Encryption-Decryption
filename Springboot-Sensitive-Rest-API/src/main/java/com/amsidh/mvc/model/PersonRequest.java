package com.amsidh.mvc.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
@XmlRootElement(name = "PersonRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonRequest implements Serializable {

    @NotNull
    @NotBlank
    @Size(min = 3, max = 100)
    @XmlElement(name = "username")
    private String username;

    @Email
    @XmlElement(name = "emailId")
    private String emailId;

    @NotNull
    @NotBlank
    @Size(min = 8, max = 50)
    @XmlElement(name = "password")
    private String password;
}
