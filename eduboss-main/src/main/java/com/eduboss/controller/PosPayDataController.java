package com.eduboss.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.PosPayDataToExcelVo;
import com.eduboss.domainVo.PosPayDataVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.PosPayDataService;
import com.eduboss.utils.ExportExcel;
import com.eduboss.utils.HibernateUtils;

/**
 * 
 * @author lixuejun
 *
 */
@Controller
@RequestMapping(value="/PosPayDataController")
public class PosPayDataController {

	@Autowired
	private PosPayDataService posPayDataService;
	
	@ResponseBody
	@RequestMapping(value="/getPosPayDataList")
	public DataPackageForJqGrid getPosPayDataList(@ModelAttribute GridRequest gridRequest, @ModelAttribute PosPayDataVo posPayDataVo) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = posPayDataService.findPagePosPayData(dataPackage, posPayDataVo);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	@ResponseBody
	@RequestMapping(value="/artificialMatch")
	public Response artificialMatch(@RequestParam String id, @RequestParam String remark) {
		posPayDataService.artificialMatch(id, remark);
		return new Response();
	}
	
	@ResponseBody
	@RequestMapping(value="/getPosPayDataListToExcel")
	public void getPosPayDataListToExcel(@ModelAttribute GridRequest gridRequest, @ModelAttribute PosPayDataVo posPayDataVo,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage.setPageSize(gridRequest.getNumOfRecordsLimitation());
		dataPackage = posPayDataService.findPagePosPayData(dataPackage, posPayDataVo);
		List<PosPayDataVo> voList = (List<PosPayDataVo>) dataPackage.getDatas();
		List<PosPayDataToExcelVo> list = HibernateUtils.voListMapping(voList, PosPayDataToExcelVo.class);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String filename = timeFormat.format(new Date())+".xls";
		response.setContentType("application/ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename=".concat(String.valueOf(URLEncoder.encode(filename, "UTF-8"))));
		
		ExportExcel<PosPayDataToExcelVo> exporter = new ExportExcel<PosPayDataToExcelVo>();
		String[] hearders = new String[] {"所属分公司", "校区", "终端号","商户名称", "支付参考号", "支付金额（元）", "支付日期",  "导入时间", "导入人", "匹配状态", "备注"};//表头数组
		try(OutputStream out = response.getOutputStream();){
			exporter.exportExcel(hearders, list, out);
		}
	 }
	
}
