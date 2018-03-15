package com.eduboss.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.MessageDeliverRecord;
import com.eduboss.domain.MessageDeliverRecordId;
import com.eduboss.domain.MessageRecord;
import com.eduboss.domain.UserDetailsImpl;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.MessageVo;
import com.eduboss.dto.Response;
import com.eduboss.service.MessageRecordService;
import com.eduboss.service.UserService;
import com.eduboss.service.dwr.DwrMessageManager;

/**
 * 消息
 * 
 * @author ndd 2014-8-6
 */
@Controller
@RequestMapping(value = "/MessageRecordController")
public class MessageRecordController {

	@Autowired
	MessageRecordService messageRecordService;
	
	@Autowired
	DwrMessageManager dwrMessageManager;
	
	@Autowired
	UserService userService;

	@RequestMapping(value = "/getMessageRecordList")
	@ResponseBody
	public DataPackageForJqGrid getMessageRecordList(@ModelAttribute MessageRecord messageRecord, MessageVo messageVo,
			@ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = messageRecordService.getMessageRecordList(messageRecord, dataPackage, messageVo);
		return new DataPackageForJqGrid(dataPackage);
	}

	@RequestMapping(value = "/editMessageRecord")
	@ResponseBody
	public Response editMessageRecord(@ModelAttribute GridRequest gridRequest, @ModelAttribute MessageRecord messageRecord) {
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			messageRecordService.deleteMessageRecord(messageRecord);
		} else {
			messageRecordService.saveOrUpdateMessageRecord(messageRecord);
		}
		return new Response();
	}

	@ResponseBody
	@RequestMapping(value = "/findMessageRecordById")
	public MessageRecord findMessageRecordById(@RequestParam String id) {
		return messageRecordService.findMessageRecordById(id);
	}

	@RequestMapping(value = "/getMessageDeliverRecordList")
	@ResponseBody
	public DataPackageForJqGrid getMessageDeliverRecordList(@ModelAttribute MessageDeliverRecord messageDeliverRecord, MessageVo messageVo,
			@ModelAttribute GridRequest gridRequest) {
		DataPackage dataPackage = new DataPackage(gridRequest);
		dataPackage = messageRecordService.getMessageDeliverRecordList(messageDeliverRecord, dataPackage,messageVo);
		return new DataPackageForJqGrid(dataPackage);
	}
	
	/**
	 * 获取我的消息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getMyMessage")
	public List<MessageDeliverRecord> getMyMessage(){
		String userId=userService.getCurrentLoginUser().getUserId();
		return messageRecordService.getMessageDeliverrecordByUserId(userId);
	}
	
//	@RequestMapping(value="/sendMesTest")
//	public void sendMesTest(HttpServletResponse res){
//		List<MessageDeliverRecord> list= messageRecordService.getMessageDeliverrecordByUserId("112233");
//		System.out.println("-----"+list.size());
//		dwrMessageManager.sendMessageToSingle("112233", "fdfg");
//		PrintWriter out;
//		try {
//			out = res.getWriter();
//			out.print("aaa");
//			out.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
}
