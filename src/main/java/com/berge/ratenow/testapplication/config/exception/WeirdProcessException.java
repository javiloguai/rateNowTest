package com.berge.ratenow.testapplication.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class WeirdProcessException extends RuntimeException {

    private Map criteria;
    
    private Object[] parameters;

    public WeirdProcessException(String message) {
        super(message);
    }
    
    public WeirdProcessException(String message,Object[] parameters) {
        super(message);
        this.parameters = parameters;
    }
}
