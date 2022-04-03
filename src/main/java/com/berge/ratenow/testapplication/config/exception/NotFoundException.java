package com.berge.ratenow.testapplication.config.exception;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotFoundException extends RuntimeException {
    
    private Object[] parameters;  
    
    public NotFoundException() {

    }
    
    public NotFoundException(String message,Object[] parameters) {
        super(message);
        this.parameters = parameters;
    }
    
    public NotFoundException(String message) {
        super(message);
    }
}
