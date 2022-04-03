package com.berge.ratenow.testapplication.bo;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

/**
 * @author jruizh
 *
 */
@Data
@Builder
public class ObjFilter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	

}
