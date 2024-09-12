package com.ucms.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDetails {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String reason;
    private String path;
}
