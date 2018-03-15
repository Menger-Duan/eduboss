package com.eduboss.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.RefundContract;
import com.eduboss.domainVo.AccountChargeRecordsExcelVo;
import com.eduboss.domainVo.ContractVo;
import com.eduboss.domainVo.RefundContractExcelVo;
import com.eduboss.domainVo.RefundContractVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;
import com.eduboss.service.CommonService;
import com.eduboss.service.RefundService;
import com.eduboss.service.UserService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;

/**
 * @classname	CommonAction.java 
 * @Description
 * @author	ChenGuiBan
 * @Date	2013-3-16 01:09:47
 * @LastUpdate	ChenGuiBan
 * @Version	1.0
 */

@Controller
@RequestMapping(value = "/RefundController")
public class RefundController {
	
	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(RefundController.class);
	
	/**
	 * common service
	 */
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RefundService refundService;
	
	private final static ObjectMapper objectMapper = new ObjectMapper();
	
	@RequestMapping(value = "/getPendingRefundContractsByStuId", method =  RequestMethod.GET)
	@ResponseBody
	public List<ContractVo> getPendingRefundContractsByStuId(@RequestParam String studentId) {
		return refundService.getPendingRefundContractsByStuId(studentId);
	}
	
	@RequestMapping(value = "/saveNewRefundContract", method =  RequestMethod.POST)
	@ResponseBody
	public Response saveNewRefundContract(@RequestBody RefundContract refundContract) {
		refundService.saveNewRefundContract(refundContract);
		return new Response();
	}
	
	@RequestMapping(value = "/getPageRefundContracts", method =  RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid getPageRefundContracts(GridRequest gridRequest, RefundContractVo refundContractVo, TimeVo timeVo) {
        DataPackage dp = new DataPackage(gridRequest);
		return new DataPackageForJqGrid(refundService.getPageRefundContracts(dp, refundContractVo, timeVo));
	}
	
	@RequestMapping(value = "/getRedundContractById", method =  RequestMethod.GET)
	@ResponseBody
	public RefundContractVo getRedundContractById(String refundContractId) {
		return refundService.getRedundContractById(refundContractId);
	}
	
	@RequestMapping(value = "/approveRefundContract", method =  RequestMethod.GET)
	@ResponseBody
	public Response approveRefundContract(@ModelAttribute RefundContractVo refundContractVo) {
		refundService.approveRefundContract(refundContractVo);
		return new Response();
	}
	
	
	@RequestMapping(value = "/getRefundExcel", method =  RequestMethod.GET)
	@ResponseBody
	public void getRefundExcel(GridRequest gridRequest, RefundContractVo refundContractVo, TimeVo timeVo,HttpServletRequest request,HttpServletResponse response) throws Exception {
        DataPackage dataPackage = new DataPackage(gridRequest);
        dataPackage.setPageSize(1000);
        
		dataPackage=refundService.getPageRefundContracts(dataPackage, refundContractVo, timeVo);
		
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		ExportExcel<RefundContractExcelVo> ex = new ExportExcel<RefundContractExcelVo>();
		String[] hearders = new String[] {"退费合同ID", "合同总额","退费金额", "退费学生","学生学管","申请人","申请时间","当前流程"};//表头数组
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		OutputStream out = response.getOutputStream();
		Set<RefundContractExcelVo> datas = HibernateUtils.voCollectionMapping(dataPackage.getDatas(), RefundContractExcelVo.class);
		ex.exportExcel(hearders, datas, out);
        out.close();
	}
}
