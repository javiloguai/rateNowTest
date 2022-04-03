package com.berge.ratenow.testapplication.dto.comun;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class PaginationCommand {

    @Min(value = 0, groups = NullAllowed.class)
    private Integer page;
    @Min(value = 0, groups = NullAllowed.class)
    private Integer size;
    @Size(max = 255, groups = NullAllowed.class)
    private String sortDir;
    @Size(max = 255, groups = NullAllowed.class)
    private String sort;
}
