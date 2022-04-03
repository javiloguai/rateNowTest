package com.berge.ratenow.testapplication.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessRuleViolatedException extends RuntimeException {

    private Map criteria;
    
    private Object[] parameters;

    public BusinessRuleViolatedException(String message) {
        super(message);
    }
    
    public BusinessRuleViolatedException(String message,Object[] parameters) {
        super(message);
        this.parameters = parameters;
    }
}
