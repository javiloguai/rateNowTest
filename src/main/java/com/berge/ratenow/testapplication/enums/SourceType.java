package com.berge.ratenow.testapplication.enums;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.StringUtils;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author jruizh
 *
 */
public enum SourceType {
	REPORT, QUESTIONNAIRE;

	public static SourceType getFromString(final String source) {
		if (StringUtils.hasText(source) && EnumUtils.isValidEnum(SourceType.class,source)) {
			return SourceType.valueOf(source);
		}
		return null;
	}
}
