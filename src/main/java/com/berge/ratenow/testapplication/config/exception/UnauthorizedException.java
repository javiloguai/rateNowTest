package com.berge.ratenow.testapplication.config.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UnauthorizedException extends RuntimeException {
    
    private Object[] parameters;
    
    public UnauthorizedException() {

    }
    
    public UnauthorizedException(String message,Object[] parameters) {
        super(message);
        this.parameters = parameters;
    }
    
    public UnauthorizedException(String message) {
        super(message);
    }

}
