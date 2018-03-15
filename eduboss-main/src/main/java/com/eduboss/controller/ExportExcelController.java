package com.eduboss.controller;

import com.eduboss.domainVo.ContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="/excelExport")
public class ExportExcelController {


	@Autowired
	ExportExcelService exportExcelService;

	@RequestMapping(value="/exportContractListExcel")
	@ResponseBody
//	@Async("excelExecutor")
	public String exportContractListExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute ContractVo contractVo, @ModelAttribute TimeVo timeVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		return exportExcelService.exportContractListExcel(dataPackage,contractVo,timeVo);
	}
	
}
