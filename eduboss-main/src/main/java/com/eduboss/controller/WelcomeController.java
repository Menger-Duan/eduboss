package com.eduboss.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.service.SystemNoticeService;

@Controller
@RequestMapping(value = "/WelcomeController")
public class WelcomeController {
	
	@Autowired
	private SystemNoticeService systemNoticeService;
	
	
	@RequestMapping(value = "/getSystemNoticeList")
	@ResponseBody
	public DataPackageForJqGrid getSystemNoticeList(SystemNoticeVo systemNoticeVo,GridRequest gridRequest){
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = systemNoticeService.getSystemNoticeListByRolesWelCome(systemNoticeVo, dataPackage);
//		List<WelcomeNoticeVo> welcomeVoList = new ArrayList<WelcomeNoticeVo>();
//		List<SystemNoticeVo> systemNoticeVoList = (List<SystemNoticeVo>) dataPackage.getDatas();
//		for (SystemNoticeVo noticeVo: systemNoticeVoList) {
//			WelcomeNoticeVo welcomeNotice = HibernateUtils.voObjectMapping(noticeVo, WelcomeNoticeVo.class);
//			welcomeVoList.add(welcomeNotice);
//		}
//		dataPackage.setDatas(welcomeVoList);
		DataPackageForJqGrid dpfjg = new DataPackageForJqGrid(dataPackage);
		dpfjg.setTotal(dataPackage.getRowCount());
		return dpfjg;
	}
	
	@RequestMapping(value = "/findSystemNoticeVoById")
	@ResponseBody
	public SystemNoticeVo findSystemNoticeVoById(@RequestParam("id") String id) {
		return systemNoticeService.findSystemNoticeVoById(id);
	}
	
	
	/**
	 * 得到本月收入
	 * @return
	 */
	@RequestMapping(value = "/getTodayAndMonthTotal")
	@ResponseBody
	public Map getTodayAndMonthTotal() {
		 return systemNoticeService.getTodayAndMonthTotal();
	}
	
	
	/**
	 * 得到本月收入
	 * @return
	 */
	@RequestMapping(value = "/getTodayAndMonthTotalIncome")
	@ResponseBody
	public List getTodayAndMonthTotalIncome() {
		 return systemNoticeService.getTodayAndMonthTotalIncome();
	}
	
	
	/**
	 * 得到本月收入
	 * @return
	 */
	@RequestMapping(value = "/getTodayAndMonthTotalComsume")
	@ResponseBody
	public List getTodayAndMonthTotalComsume() {
		 return systemNoticeService.getTodayAndMonthTotalComsume();
	}
	
	
	/**
	 * 欢迎页 营业指标概览  课消 20170731 xiaojinwang
	 * @return
	 */
	@RequestMapping(value = "/getTodayMonthConsumeByType")
	@ResponseBody
	public List getTodayMonthConsumeByType(String productType) {
		 return systemNoticeService.getTodayMonthConsumeByType(productType);
	}
	
	

}
