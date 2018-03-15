package com.eduboss.service;

import com.eduboss.domain.StudentReturnFee;
import com.eduboss.domainVo.StudentReturnFeeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;


public interface StudentReturnService {
	public void saveStudentReturn(StudentReturnFee studentReturnFee) ;

	public DataPackage getStudentReturnList(StudentReturnFeeVo studentReturnVo,
			DataPackage dataPackage,ModelVo modelvo);
	

	public void saveStudentReturn(String studentReturnId,String returnType,String returnReason,String accountName,String account) ;
	
}
