package com.berge.ratenow.testapplication.server.requests;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.dto.comun.NullAllowed;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "Update Subscribers request data ")
public class UpdateSubscribersCommand {

	@Min(value = Long.MIN_VALUE, groups = NullAllowed.class)
	@NotNull
	protected Long idSubscription;

	@ApiModelProperty(value = "The list of persons Id")
    @NotNull
    private List<Long> personIds;
    
    
}

