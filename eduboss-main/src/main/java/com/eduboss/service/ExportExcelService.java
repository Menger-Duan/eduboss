package com.eduboss.service;

import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

public interface ExportExcelService {

	public String exportContractListExcel(DataPackage dataPackage, ContractVo contractVo , TimeVo timeVo) ;

}
