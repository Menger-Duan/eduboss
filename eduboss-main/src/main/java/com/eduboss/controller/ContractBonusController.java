package com.eduboss.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.eduboss.domainVo.RefundIncomeDistributeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.BonusType;
import com.eduboss.domainVo.ContractBonusVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.ContractBonusService;
import com.eduboss.utils.StringUtil;

@Controller
@RequestMapping(value = "/ContractBonusController")
public class ContractBonusController {
	
	@Autowired
	private ContractBonusService contractBonusService;
	
	
	
	@RequestMapping(value = "/getContractBonusList")
	@ResponseBody
	public DataPackageForJqGrid getContractBonusList(HttpServletRequest request,@ModelAttribute GridRequest gridRequest,ContractBonusVo contractBonusVo){
		DataPackage dataPackage = new DataPackage(gridRequest);
		
		String startDate = "",endDate="";
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(request.getParameter("startDate"))){
			startDate =request.getParameter("startDate");
		}
		if(StringUtil.isNotBlank(request.getParameter("endDate"))){
			endDate = request.getParameter("endDate");
		}
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		dataPackage = contractBonusService.getContractBonusVoList(dataPackage, contractBonusVo,params);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid ;
	}

	/**
	 * 取回合同的提成信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/getContractBonusByStudentReturnId", method =  RequestMethod.GET)
	@ResponseBody
	public RefundIncomeDistributeVo getContractBonusByStudentReturnId(@RequestParam String StudentReturnId){
		RefundIncomeDistributeVo refundWorkflowVo = contractBonusService.getContractBonusByStudentReturnId(StudentReturnId);
		return refundWorkflowVo;
	}
	
	
	/**
	 * 保存退费信息和责任信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "/saveBonusAndReturnInfo")
	@ResponseBody
	public Response saveBonusAndReturnInfo(@RequestParam String fundChargeId, RefundIncomeDistributeVo refundIncomeDistributeVo,String studentReturnId,String returnType,String returnReason,String accountName,String account,String contractProductId,
			 @RequestParam(required=false)MultipartFile certificateImageFile){
		contractBonusService.saveContractBonus(fundChargeId, BonusType.FEEDBACK_REFUND.toString(),studentReturnId,returnType,returnReason,certificateImageFile,accountName,account,contractProductId, refundIncomeDistributeVo);
		return new Response();
	}
	
	/**
	 * 退费详情删除图片
	 * @param fundsChangeId
	 */
	@RequestMapping(value = "/returnAmountDeleteImage", method =  RequestMethod.GET)
	@ResponseBody
	public Response deleteImage (@RequestParam String fundsChangeId){
		contractBonusService.deleteImage(fundsChangeId);
		return new Response();
	}
	
}
