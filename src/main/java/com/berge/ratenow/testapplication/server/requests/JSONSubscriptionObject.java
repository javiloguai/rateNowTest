package com.berge.ratenow.testapplication.server.requests;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.berge.ratenow.testapplication.dto.comun.NullAllowed;

import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
public class JSONSubscriptionObject {

	@Min(value = Long.MIN_VALUE, groups = NullAllowed.class)
	@NotNull
	protected Long idSubscription;
	
	
	@Min(value = Long.MIN_VALUE, groups = NullAllowed.class)
	private Long sourceId;
	
	@Min(value = Long.MIN_VALUE, groups = NullAllowed.class)
	private Long objFilterId;
	
		

}
