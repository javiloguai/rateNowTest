package com.berge.ratenow.testapplication.service;

import com.berge.ratenow.testapplication.bo.ObjFilter;
import com.berge.ratenow.testapplication.bo.Report;

/**
 * @author jruizh
 * I will not implement this service, just a test I will assume it's implemented
 *
 */
public interface ObjFilterService {


	ObjFilter getObjectFilterByIdAndQn(Long objFilterReportId, Long qnId);


	
}
