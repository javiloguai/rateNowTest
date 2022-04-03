package com.berge.ratenow.testapplication.bo;

import java.io.Serializable;
import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jruizh
 *
 */
@Data
@Builder
//@NoArgsConstructor
public class Report implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long objFilterReportId;
	
	private Long qnId;
	
	

}
