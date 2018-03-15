package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domainVo.SentRecordVo;
import com.eduboss.domainVo.SystemMessageManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.Response;
import com.eduboss.service.SentRecordService;
import com.eduboss.service.SystemMessageManageService;

@Controller
@RequestMapping(value = "/SystemMessageManageAction")
public class SystemMessageManageController {
	
	@Autowired
	private SystemMessageManageService systemMessageMangeService;
	
	@Autowired
	private SentRecordService sentRecordService;

	/**
	 * 获取系统信息列表
	 * @return
	 */
	@RequestMapping(value ="/getSystemMessageManageList")
	@ResponseBody
	public DataPackageForJqGrid getSystemMessageManageList(@ModelAttribute SystemMessageManageVo systemMessageManageVo, @ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = systemMessageMangeService.getSystemMessageManageList(systemMessageManageVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 编辑系统信息
	 * @return
	 */
	@RequestMapping(value ="/editSystemMessageManage")
	@ResponseBody
	public Response editSystemMessageManage(@ModelAttribute GridRequest gridRequest, @ModelAttribute SystemMessageManageVo systemMessageManageVo) throws Exception{
		Response res = new Response();
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			systemMessageMangeService.deleteSystemMsg(systemMessageManageVo);
		} else if ("updateStatus".equalsIgnoreCase(gridRequest.getOper())) {
			systemMessageMangeService.updateSystemMsgStatus(systemMessageManageVo);
		} else {
			return systemMessageMangeService.saveOrUpdateSystemMsg(systemMessageManageVo); 
		}
		return res;
	}
	
	/**
	 * 根据id查找系统信息
	 * @return
	 */
	@RequestMapping(value ="/findSystemMessageManageById")
	@ResponseBody
	public SystemMessageManageVo findSystemMessageManageById(@RequestParam String id) {
		return systemMessageMangeService.findSystemMessageManageById(id);
	}
	
	/**
	 * 获取系统信息列表
	 * @return
	 */
	@RequestMapping(value ="/getSentRecordList")
	@ResponseBody
	public DataPackageForJqGrid getSentRecordList(@ModelAttribute SentRecordVo sentRecordVo, @ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = sentRecordService.getSentRecordList(sentRecordVo, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 重发系统信息
	 * @param recordId
	 * @return
	 */
	@RequestMapping(value ="/resentRecord")
	@ResponseBody
	public Response resentRecord(@RequestParam String recordId) {
		return sentRecordService.resentRecord(recordId);
	}
	
}
