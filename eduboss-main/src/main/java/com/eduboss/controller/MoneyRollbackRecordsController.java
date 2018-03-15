package com.eduboss.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.MoneyRollbackRecords;
import com.eduboss.domainVo.MoneyRollbackRecordsVo;
import com.eduboss.domainVo.RollbackBackupRecordsVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.MoneyRollbackRecordsService;

@Controller
@RequestMapping(value = "/MoneyRollbackAction")
public class MoneyRollbackRecordsController {

	@Autowired
	private MoneyRollbackRecordsService moneyRollbackRecordsService;
	
	/**
	 * 资金回滚记录列表
	 * @param moneyRollbackRecordsVo
	 * @return
	 */
	@RequestMapping(value = "/getMoneyRollbackRecordsList", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getMoneyRollbackRecordsList(@ModelAttribute GridRequest gridRequest, @ModelAttribute MoneyRollbackRecordsVo moneyRollbackRecordsVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = moneyRollbackRecordsService.getMoneyRollbackRecordsList(dataPackage, moneyRollbackRecordsVo);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 根据id查询资金回滚记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/findMoneyRollbackRecordsById", method =  RequestMethod.GET)
	@ResponseBody
	public MoneyRollbackRecordsVo findMoneyRollbackRecordsById(@RequestParam("id") String id){
		return moneyRollbackRecordsService.findMoneyRollbackRecordsById(id);
	}
	
	/**
	 * 根据id查询资金回滚记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/getRollbackBackupRecords", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getRollbackBackupRecordsByTransactionId(@RequestParam("transactionId") String transactionId, @ModelAttribute GridRequest gridRequest){
		DataPackage dp = new DataPackage(gridRequest);
		dp = moneyRollbackRecordsService.getRollbackBackupRecordsByTransactionId(transactionId, dp);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dp);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 修改回滚详情
	 * @param moneyRollbackRecordsVo
	 * @return
	 */
	@RequestMapping(value = "/editMoneyRollbackRecords", method =  RequestMethod.GET)
	@ResponseBody
	public Response editMoneyRollbackRecords(@ModelAttribute MoneyRollbackRecords moneyRollbackRecords){
		moneyRollbackRecordsService.editMoneyRollbackRecords(moneyRollbackRecords);
		return new Response();
	}
	
}
