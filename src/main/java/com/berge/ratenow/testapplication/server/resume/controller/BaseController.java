package com.berge.ratenow.testapplication.server.resume.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.berge.ratenow.testapplication.config.exception.NotFoundException;
import com.berge.ratenow.testapplication.config.exception.BusinessRuleViolatedException;
import com.berge.ratenow.testapplication.config.exception.UnauthorizedException;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
public class BaseController {

    Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(SQLException exception) {
        Map<String, Object> item = new HashMap<>();
        item.put("message", messageSource.getMessage("default.sql.exception",
                null,
                "INTERNAL_SERVER_ERROR",
                LocaleContextHolder.getLocale()));
        item.put("exception", exception);
        logger.error("INTERNAL_SERVER_ERROR: ", exception);
        return item;
    }
    
   
    /**
     * Handler para Not Found
     *
     * @param exception de no encontrado
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleException(NotFoundException exception) {
        Map<String, Object> item = new HashMap<>();
        item.put("message", messageSource.getMessage(StringUtils.isEmpty(exception.getMessage())
                        ? exception.getMessage()
                        : "default.not.found.exception",
                //Añadimos la posibilidad de manejar parámetros para pasar a los mensajes
                exception.getParameters(),
                StringUtils.isEmpty(exception.getMessage())
                        ? exception.getMessage()
                        : "default.not.found.exception",
                LocaleContextHolder.getLocale()));
        logger.error("NOT_FOUND: ", exception);
        return item;
    }

    /**
     * Handler
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, Object> handleException(UnauthorizedException exception) {
        Map<String, Object> item = new HashMap<>();
        item.put("message", messageSource.getMessage(StringUtils.isEmpty(exception.getMessage())
                        ? exception.getMessage()
                        : "default.unauthorized.exception",
                null,
                StringUtils.isEmpty(exception.getMessage())
                        ? exception.getMessage()
                        : "default.unauthorized.exception",
                LocaleContextHolder.getLocale()));
        logger.error("UNAUTHORIZED: ", exception);
        return item;
    }
    
    /**
     * Handler para Regla de negocio violada
     *
     * @param exception de regla de negocio violada
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Map<String, Object> handleException(BusinessRuleViolatedException exception) {
        Map<String, Object> item = new HashMap<>();
//        item.put("message", exception.getMessage());
        item.put("message", messageSource.getMessage(exception.getMessage(), exception.getParameters(), exception.getMessage(), LocaleContextHolder.getLocale()));            
        item.put("adicional", exception.getCriteria());
//        logger.error("NOT_ACCEPTABLE: ", exception);
        return item;
    }

    /**
     * Handler
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleException(AccessDeniedException exception) {
        Map<String, Object> item = new HashMap<>();
        String mensaje = exception.getLocalizedMessage();
        if (StringUtils.isEmpty(mensaje)) {
            mensaje = messageSource.getMessage("default.denied.exception",
                    null,
                    "default.denied.exception",
                    LocaleContextHolder.getLocale());
        }
        item.put("message", mensaje);
        logger.error("FORBIDDEN: ", exception);
        return item;
    }

    /**
     * Handler
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception exception) {
        Map<String, Object> item = new HashMap<>();
        item.put("message", messageSource.getMessage("default.500.exception",
                null,
                "INTERNAL_SERVER_ERROR",
                LocaleContextHolder.getLocale()));
        //  item.put("exception", exception);
        logger.error("INTERNAL_SERVER_ERROR: ", exception);
        return item;
    }

    /**
     * Tratamiento para org.springframework.orm.jpa.JpaSystemException
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(org.springframework.orm.jpa.JpaSystemException exception) {
        Map<String, Object> item = new HashMap<>();
        String message = messageSource.getMessage("default.500.exception",
                null,
                "INTERNAL_SERVER_ERROR",
                LocaleContextHolder.getLocale());
        
        message = exception.getMostSpecificCause().getMessage();
        item.put("message", message);
        logger.error("INTERNAL_SERVER_ERROR: ", exception);
        return item;
    }
    
    /**
     * Tratamiento para JsonProcessingException
     *
     * @param exception
     * @return
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleException(JsonProcessingException exception) {
        Map<String, Object> item = new HashMap<>();
        String message = messageSource.getMessage("default.500.exception",
                null,
                "INTERNAL_SERVER_ERROR",
                LocaleContextHolder.getLocale());
        message = exception.getMessage();
        item.put("message", message);
        logger.error("INTERNAL_SERVER_ERROR: JSON_PROCESSING EXCEPTION", exception);
        return item;
    }
    
    
    
    
}
