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

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.domain.OdsCampusAchievementModify;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OdsMonthAchievementStudentPrintVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainPrintVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainStudentVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementMainVo;
import com.eduboss.domainVo.OdsMonthCampusAchievementModifyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.Response;
import com.eduboss.service.OdsMonthCampusAchievementService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.PropertiesUtils;

@Controller
@RequestMapping(value ="/OdsMonthCampusAchievementController")
public class OdsMonthCampusAchievementController {
	
	private final static Logger log = Logger.getLogger(OdsMonthCampusAchievementController.class);
	
	@Autowired
	private OdsMonthCampusAchievementService odsMonthCampusAchievementService;
	
	
	@RequestMapping(value = "/findAchievementMainStudentByMonth",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findAchievementMainStudentByMonth(String yearMonth,String campusId,String evidenceAuditStatus){
		DataPackageForJqGrid dpf = new DataPackageForJqGrid();
		List<OdsMonthCampusAchievementMainStudentVo> list = odsMonthCampusAchievementService.findAchievementMainStudentByMonth(yearMonth,campusId);
		dpf.setRecords(list.size());
		dpf.setRows(list);
		dpf.setTotal(1);
		return dpf;
	}
	
	@RequestMapping(value = "/findCampusAchievementMainByMonth",method = RequestMethod.GET)
	@ResponseBody
	public DataPackageForJqGrid findCampusAchievementMainByMonth(BasicOperationQueryVo searchVo
			){
		DataPackage dp =new DataPackage();
		List list = odsMonthCampusAchievementService.findCampusAchievementMainByMonth(searchVo);
		dp.setPageSize(9999);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		DataPackageForJqGrid dpf = new DataPackageForJqGrid(dp);
		return dpf;
	}
	
	
	@ResponseBody
	@RequestMapping(value="/exportCampusAchievementMainStudentExcel")
	public void exportCampusAchievementMainStudentExcel(String yearMonth,String campusId,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		List<OdsMonthCampusAchievementMainStudentVo> list = odsMonthCampusAchievementService.findAchievementMainStudentByMonth(yearMonth,campusId);
		List<OdsMonthAchievementStudentPrintVo> exportList = HibernateUtils.voListMapping(list, OdsMonthAchievementStudentPrintVo.class);
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<OdsMonthAchievementStudentPrintVo> exporter = new ExportExcel<OdsMonthAchievementStudentPrintVo>();
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
	@RequestMapping(value="/exportCampusAchievementMainExcel")
	public void exportCampusAchievementMainExcel(BasicOperationQueryVo searchVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		List list = odsMonthCampusAchievementService.findCampusAchievementMainByMonth(searchVo);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<Map> exporter = new ExportExcel<Map>();
		String[] hearders = null;
		String[] heardersId = null;

		if(PropertiesUtils.getStringValue("institution")!=null && "advance".equals(PropertiesUtils.getStringValue("institution"))){
			hearders = new String[] {"集团","分公司",  "校区","学生","新签合同业绩", "续费合同业绩","收款业绩", "退费责任业绩", "总业绩", "直播新签业绩", "直播续费业绩", "直播新签退费责任业绩", "直播续费退费责任业绩", "直播合作总业绩", "总收款业绩", "总退费责任", "总业绩", "调整金额", "调整后总计", "备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
			heardersId =  new String[] {"groupName","branchName",  "campusName","studentName","campusAmountNew", "campusAmountRe","allMoney", "refundAmount", "totalMoney", "liveIncomeNew", "liveIncomeRenew",
						"liveRefundNew", "liveRefundRenew", "liveTotalMoney", "totalIncomeMoney", "totalRefundMoney", "totalBonus", "modifyMoney", "afterModifyMoney", "remark", "receiptMonth", "receiptDate", "receiptStatus"};//表头数组
		}else {
			hearders = new String[] {"集团","分公司",  "校区","学生","新签合同业绩", "续费合同业绩","收款业绩", "退费责任业绩", "总业绩", "直播新签业绩", "直播续费业绩", "直播新签退费责任业绩", "直播续费退费责任业绩", "直播合作总业绩", "总收款业绩", "总退费责任", "总业绩", "调整金额", "调整后总计", "备注信息", "凭证月份", "凭证日期", "状态"};//表头数组
			heardersId =  new String[] {"groupName","branchName",  "campusName","studentName","campusAmountNew", "campusAmountRe","allMoney", "refundAmount", "totalMoney", "liveIncomeNew", "liveIncomeRenew",
						"liveRefundNew", "liveRefundRenew", "liveTotalMoney", "totalIncomeMoney", "totalRefundMoney", "totalBonus", "modifyMoney", "afterModifyMoney", "remark", "receiptMonth", "receiptDate", "receiptStatus"};//表头数组
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
      
	
	@RequestMapping(value = "/findModifyInfoByMainId",method = RequestMethod.GET)
	@ResponseBody
	public List<OdsMonthCampusAchievementModifyVo> findModifyInfoByMainId(String mainId){
		return odsMonthCampusAchievementService.findModifyInfoByMainId(mainId);
	}
	
	@RequestMapping(value = "/saveAchievementModifyInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response saveAchievementModifyInfo(@ModelAttribute OdsCampusAchievementModify odsCampusAchievementModify){
		return odsMonthCampusAchievementService.saveAchievementModifyInfo(odsCampusAchievementModify);
	}
	
	@RequestMapping(value = "/deleteAchievementModifyInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response deleteAchievementModifyInfo(String id){
		return odsMonthCampusAchievementService.deleteAchievementModifyInfo(id);
	}
	
	@RequestMapping(value = "/findAchievementMainInfoById",method = RequestMethod.GET)
	@ResponseBody
	public OdsMonthCampusAchievementMainVo findAchievementMainInfoById(String id){
		return odsMonthCampusAchievementService.findAchievementMainInfoById(id);
	}
	
	@RequestMapping(value="/auditCampusAchievement")
	@ResponseBody
	public Response auditCampusAchievement(@RequestParam String id,@RequestParam EvidenceAuditStatus status) throws Exception{
		odsMonthCampusAchievementService.auditCampusAchievement(id,status);
		return new Response();
	}
	
	@RequestMapping(value="/rollbackCampusAchievement")
	@ResponseBody
	public Response rollbackCampusAchievement(@RequestParam String id) throws Exception{
		odsMonthCampusAchievementService.rollbackPaymentReciept(id);
		return new Response();
	}
	
	@RequestMapping(value="/flushCampusAchievementById")
	@ResponseBody
	public Response flushCampusAchievementById(@RequestParam String id) throws Exception{
		odsMonthCampusAchievementService.flushCampusAchievementById(id);
		return new Response();
	}
	
	@RequestMapping(value = "/updateAchievementMainInfo",method = RequestMethod.POST)
	@ResponseBody
	public Response updateAchievementMainInfo(@RequestParam String id,@RequestParam String remark){
		return odsMonthCampusAchievementService.updateAchievementMainInfo(id,remark);
	}
	
	@RequestMapping(value="/findOdsMonthCampusAchievementMainPrintById")
	@ResponseBody
	public OdsMonthCampusAchievementMainPrintVo findOdsMonthCampusAchievementMainPrintById(@RequestParam String evidenceId) throws Exception {
		return odsMonthCampusAchievementService.findOdsMonthCampusAchievementMainPrintById(evidenceId);
	}
	
	@RequestMapping(value="/loadCampusFundsAuditRate")
	@ResponseBody
	public List<Map<String,String>> loadCampusFundsAuditRate(@RequestParam String campusId,String channel,@RequestParam String receiptMonth) throws Exception {
		return odsMonthCampusAchievementService.getCampusFundsAuditRate(campusId,channel,receiptMonth);
	}
	
}


