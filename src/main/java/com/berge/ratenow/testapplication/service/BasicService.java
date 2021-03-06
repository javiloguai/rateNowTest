package com.berge.ratenow.testapplication.service;


import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author jruizh
 * Useful if we want to add some standard validation
 *
 */
public class BasicService {

    private static final Logger LOG = LoggerFactory.getLogger(BasicService.class);

  
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * Checks if an Object have a at least minimum fields with value
     *
     * @param object The object to validate
     * @param minimumFields The minimum fields
     * @return the validation result
     */
    public boolean haveMinimumFields(final Object object, final Integer minimumFields) {
        final ObjectMapper oMapper = new ObjectMapper();
        final Map<String, Object> map = oMapper.convertValue(object, Map.class);
        map.values().removeIf(Objects::isNull);
        return map.size() >= minimumFields;
    }

    
}
