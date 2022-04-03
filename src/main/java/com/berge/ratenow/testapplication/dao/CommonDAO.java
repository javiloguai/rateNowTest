package com.berge.ratenow.testapplication.dao;

import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.berge.ratenow.testapplication.dto.comun.BusquedaGeneralCommand;

public interface CommonDAO {

    Map<String, Object> findAll(BusquedaGeneralCommand command, Pageable pageable);

    
    
}
