package com.berge.ratenow.testapplication.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
@Builder
//@NoArgsConstructor
public class Subscription implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long idPersonOwner;
	
	private Long sourceId;
	
	private Long objFilterId;
	
	private String personOwnerName;
	
	private Date registrationDate;
	
	private Boolean active;
	
	private List<Subscriber> personsWhoReceiveIt;


}
