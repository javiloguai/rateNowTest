package com.berge.ratenow.testapplication.server.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.berge.ratenow.testapplication.enums.SourceType;

@Data
@NoArgsConstructor
@ApiModel(description = "Subscription request data ")
public class SubscriptionCommand {

    @NotNull
    @ApiModelProperty(value = "filter", required = true, example = "MyWifeIsBald")
    @Size(min = 1, max = 10)
    private String filter;

    @NotNull
    @ApiModelProperty(value = "The source Id", required = false, example = "12")
    private Long sourceId;
    
    
    @NotNull
    @ApiModelProperty(value = "The source Type", allowableValues = "REPORT, QUESTIONNAIRE", example = "QUESTIONNAIRE")
    private SourceType sourceType;

}

