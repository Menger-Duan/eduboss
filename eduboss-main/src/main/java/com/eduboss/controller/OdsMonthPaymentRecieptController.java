package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eduboss.utils.PropertiesUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.BasicOperationQueryLevelType;
import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsPaymentReceiptModify;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainPrintVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptMainVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptPrintVo;
import com.eduboss.domainVo.OdsMonthPaymentRecieptVo;
import com.eduboss.domainVo.OdsPaymentRecieptModifyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.Response;
import com.eduboss.service.OdsMonthPaymentRecieptService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;

@Controller
@RequestMapping(value ="/OdsMonthPaymentRecieptController")
public class OdsMonthPaymentRecieptController {
	
	private final static Logger log = Logger.getLogger(OdsMonthPaymentRecieptController.class);
	
	@Autowired
	private OdsMonthPaymentRecieptService odsMonthPaymentRecieptService;
	
	
	@RequestMapping(value = "/findPaymentRecieptByMonth",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findPaymentRecieptByMonth(String yearMonth,String campusId,String evidenceAuditStatus){
		DataPackageForJqGrid dpf = new DataPackageForJqGrid();
		List<OdsMonthPaymentRecieptVo> list = odsMonthPaymentRecieptService.findPaymentRecieptByMonth(yearMonth,campusId);
		dpf.setRecords(list.size());
		dpf.setRows(list);
		dpf.setTotal(1);
		return dpf;
	}
	
	@RequestMapping(value = "/findPaymentRecieptMainByMonth",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findPaymentRecieptMainByMonth(BasicOperationQueryVo searchVo
			){
		DataPackage dp =new DataPackage();
		List list = odsMonthPaymentRecieptService.findPaymentRecieptMainByMonth(searchVo);
		dp.setPageSize(9999);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		DataPackageForJqGrid dpf = new DataPackageForJqGrid(dp);
		return dpf;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/exportPaymentRecieptExcel")
	public void exportPaymentRecieptExcel(String yearMonth,String campusId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<OdsMonthPaymentRecieptVo> list = odsMonthPaymentRecieptService.findPaymentRecieptByMonth(yearMonth,campusId);
		List<OdsMonthPaymentRecieptPrintVo> exportList = HibernateUtils.voListMapping(list, OdsMonthPaymentRecieptPrintVo.class);
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OdsMonthPaymentRecieptPrintVo> exporter = new ExportExcel<OdsMonthPaymentRecieptPrintVo>();
		String[] hearders = new String[] {"集团","分公司",  "校区", "学生", "学校", "年级", "新签/续费/退费", "合同编号", "合同创建日期", "合同状态",
				"收款状态", 
				"收款ID", "业绩产品类型", 
				"合同待分配资金", "本次收款/退费", "收款/退费时间", "收款/退费校区", "个人待分配业绩/责任", 
				"业绩金额", "业绩类型", "业绩人", "业绩人所属部门", "凭证月份", "凭证日期", "状态"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, exportList, out);
		}
	 }
	
	@ResponseBody
	@RequestMapping(value="/exportPaymentRecieptMainExcel")
	public void exportPaymentRecieptMainExcel(BasicOperationQueryVo searchVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		List list = odsMonthPaymentRecieptService.findPaymentRecieptMainByMonth(searchVo);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<Map> exporter = new ExportExcel<Map>();
		String[] hearders = null;
		String[] heardersId = null;

		if(PropertiesUtils.getStringValue("institution")!=null && "advance".equals(PropertiesUtils.getStringValue("institution"))){
			hearders = new String[] {"集团","分公司",  "校区","学生","本月新签收款", "本月续费收款", "本月新签直播产品收款","本月实际收款", "本月冲销金额", "本月收款小计", "正常退费金额", "特殊退费金额", "实际退费金额", "现金流合计", "调整金额", "调整后总计", "备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
			heardersId = new String[] {"groupName","branchName",  "campusName","studentName","newMoney", "reMoney", "liveNewMoney","allMoney", "washMoney", "totalMoney",
					"refundMoney", "specialRefundMoney", "totalRefundMoney", "totalFinace", "modifyMoney", "afterModifyMoney", "remark", "receiptMonth", "receiptDate", "receiptStatus"};//表头数组
		}else {
			if(BasicOperationQueryLevelType.CAMPUS.getValue().equals(searchVo.getConTypeOrProType())) {
				hearders = new String[] {"集团","分公司",  "校区","学生","本月新签收款", "本月续费收款","本月新签直播产品收款", "本月实际收款", "本月冲销金额", "本月收款小计",  "正常退费金额", "特殊退费金额", "实际退费金额",  "现金流合计",  "凭证月份", "凭证日期", "状态"};//表头数组
				heardersId =  new String[] {"groupName","branchName",  "campusName","studentName","newMoney", "reMoney","liveNewMoney", "allMoney", "washMoney", "totalMoney", 
						"refundMoney", "specialRefundMoney", "totalRefundMoney",  "totalFinace",  "receiptMonth", "receiptDate", "receiptStatus"};//表头数组
			}else {
				hearders = new String[] {"集团","分公司",  "校区","学生","本月新签收款", "本月续费收款","本月新签直播产品收款", "本月实际收款", "本月冲销金额", "本月收款小计",  "正常退费金额", "特殊退费金额", "实际退费金额",  "现金流合计", "调整金额", "调整后总计", "备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
				heardersId =  new String[] {"groupName","branchName",  "campusName","studentName","newMoney", "reMoney","liveNewMoney", "allMoney", "washMoney", "totalMoney", 
						"refundMoney", "specialRefundMoney", "totalRefundMoney",  "totalFinace",  "modifyMoney", "afterModifyMoney", "remark", "receiptMonth", "receiptDate", "receiptStatus"};//表头数组
			}

		}
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}
	 }
	
	public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {    
        if (map == null)  
            return null;  
        Object obj = beanClass.newInstance();  
  
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);  
  
        return obj;  
    }    
      
	
	@RequestMapping(value = "/findInfoByMainId",method = RequestMethod.GET)
	@ResponseBody
	public List<OdsPaymentRecieptModifyVo> findInfoByMainId(String mainId){
		return odsMonthPaymentRecieptService.findInfoByMainId(mainId);
	}
	
	@RequestMapping(value = "/saveModifyInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response saveModifyInfo(@ModelAttribute OdsPaymentReceiptModify OdsPaymentReceiptModify){
		return odsMonthPaymentRecieptService.saveModifyInfo(OdsPaymentReceiptModify);
	}
	
	@RequestMapping(value = "/deleteModifyInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response deleteModifyInfo(String id){
		return odsMonthPaymentRecieptService.deleteModifyInfo(id);
	}
	
	@RequestMapping(value = "/findMainInfoById",method = RequestMethod.GET)
	@ResponseBody
	public OdsMonthPaymentRecieptMainVo findMainInfoById(String id){
		return odsMonthPaymentRecieptService.findMainInfoById(id);
	}
	
	@RequestMapping(value="/auditPaymentReciept")
	@ResponseBody
	public Response auditPaymentReciept(@RequestParam String id,@RequestParam EvidenceAuditStatus status) throws Exception{
		odsMonthPaymentRecieptService.auditPaymentReciept(id,status);
		return new Response();
	}
	
	@RequestMapping(value="/rollbackPaymentReciept")
	@ResponseBody
	public Response rollbackPaymentReciept(@RequestParam String id) throws Exception{
		odsMonthPaymentRecieptService.rollbackPaymentReciept(id);
		return new Response();
	}
	
	@RequestMapping(value="/flushInfoById")
	@ResponseBody
	public Response flushInfoById(@RequestParam String id) throws Exception{
		odsMonthPaymentRecieptService.flushInfoById(id);
		return new Response();
	}
	
	@RequestMapping(value = "/updatePaymentMainInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response updatePaymentMainInfo(@RequestParam String id,@RequestParam String remark){
		return odsMonthPaymentRecieptService.updatePaymentMainInfo(id,remark);
	}
	
	@RequestMapping(value="/findOdsMonthPaymentRecieptMainPrintById")
	@ResponseBody
	public OdsMonthPaymentRecieptMainPrintVo findOdsMonthPaymentRecieptMainPrintById(@RequestParam String evidenceId) throws Exception {
		return odsMonthPaymentRecieptService.findOdsMonthPaymentRecieptMainPrintById(evidenceId);
	}
	
	@RequestMapping(value="/getCampusFundsAuditRate")
	@ResponseBody
	public List<Map<String,String>> getCampusFundsAuditRate(@RequestParam String campusId,String channel,@RequestParam String receiptMonth) throws Exception {
		return odsMonthPaymentRecieptService.getCampusFundsAuditRate(campusId,channel,receiptMonth);
	}

	@RequestMapping(value="/getPersonPaymentFinaceBonus")
	@ResponseBody
	public DataPackageForJqGrid getPersonPaymentFinaceBonus(BasicOperationQueryVo searchVo) throws Exception {
		DataPackage dp =new DataPackage();
		List list = odsMonthPaymentRecieptService.getPersonPaymentFinaceBonus(searchVo);
		dp.setPageSize(9999);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		DataPackageForJqGrid dpf = new DataPackageForJqGrid(dp);
		return dpf;
	}

	@ResponseBody
	@RequestMapping(value="/exportPaymentRecieptReportExcel")
	public void exportPaymentRecieptReportExcel(BasicOperationQueryVo searchVo,
											  HttpServletRequest request, HttpServletResponse response) throws IOException{
		List list = odsMonthPaymentRecieptService.getPaymentRecieptReportExcel(searchVo);

		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));

		ExportExcel<Map> exporter = new ExportExcel<Map>();
		String[] hearders = new String[] {"集团","分公司", "校区","业绩人员","学生","业绩来源校区",  "年级","业绩类型", "合同编号", "产品类型", "业绩金额", "收款/退款时间", "凭证月份"};//表头数组
		String[] heardersId = new String[] {"groupName","branchName","campusName","bonusStaffName","studentName","school_id","grade_id","contract_type","contract_id","product_type","bonus_amount","transaction_time","receipt_month"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcelFromMap(hearders, list, out,heardersId);
		}
	}
}


