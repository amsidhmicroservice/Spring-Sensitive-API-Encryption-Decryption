package com.amsidh.mvc.exception;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class ErrorDetail implements Serializable {

    private String code;
    private String message;
    private Instant timestamp;
}
