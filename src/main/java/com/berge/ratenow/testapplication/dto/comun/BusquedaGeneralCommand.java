package com.berge.ratenow.testapplication.dto.comun;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class BusquedaGeneralCommand {

    @Size(max = 255, groups = NullAllowed.class)
    private String criterio;
}
