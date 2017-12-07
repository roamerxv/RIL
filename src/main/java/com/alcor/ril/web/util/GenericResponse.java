package com.alcor.ril.web.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created with admin.
 * User: duduba - 邓良玉
 * Date: 2017/12/5
 * Time: 23:44
 */
public class GenericResponse {
    @Getter @Setter private String message;
    @Getter @Setter private String error;

    public GenericResponse(final String message) {
        super();
        this.message = message;
    }

    public GenericResponse(final String message, final String error) {
        super();
        this.message = message;
        this.error = error;
    }

    public GenericResponse(List<ObjectError> allErrors, String error) {
        this.error = error;
        this.message = allErrors.stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(","));
    }
    
}
