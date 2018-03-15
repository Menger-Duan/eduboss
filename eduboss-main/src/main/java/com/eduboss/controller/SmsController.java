package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.SmsRecord;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.Response;
import com.eduboss.service.SmsService;

/**
 * 
 * @author qin.jingkai
 *  2014-10-17
 */
@RequestMapping(value = "/SmsController")
@Controller
public class SmsController {
	
	
	@Autowired
	private SmsService smsService;

	@RequestMapping(value="/getStudentSmsRecords")
	@ResponseBody
	public DataPackageForJqGrid getStudentSmsRecords(String mobileNumber, @RequestParam int pageSize, @RequestParam int pageNumber) throws Exception {
		DataPackage dataPackage = new DataPackage(pageNumber, pageSize);
		dataPackage = smsService.getStudentSmsRecords(mobileNumber, dataPackage);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	
	@RequestMapping(value = "/sendSms")
	@ResponseBody
	public Response sendSms(@ModelAttribute SmsRecord message) {
		smsService.sendSms(message);
		return new Response();
	}

	/**
	 * 由外部回调，没有session
	 */
	@RequestMapping(value = "/onReceiveSms")
	@ResponseBody
	public Response onReceiveSms(String clientid, String msg, String mobile, String receivetime, String destmobile) {
		smsService.receiveSms(mobile, msg, receivetime);
		return new Response();
	}

	@RequestMapping(value = "/notifyTeacherAndStudyManager")
	@ResponseBody
	public Response notifyTeacherAndStudyManager(String teacherId, String studyManagerId, String content){
		smsService.notifyTeacherAndStudyManager(teacherId, studyManagerId, content);
		return new Response();
	}

}
