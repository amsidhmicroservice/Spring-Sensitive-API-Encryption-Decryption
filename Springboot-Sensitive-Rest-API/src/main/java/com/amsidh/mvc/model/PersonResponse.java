package com.amsidh.mvc.model;

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
@XmlRootElement(name = "PersonResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonResponse implements Serializable {

    @XmlElement(name = "username")
    private String username;

    @XmlElement(name = "message")
    private String message;

}
