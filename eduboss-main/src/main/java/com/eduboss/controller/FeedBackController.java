package com.eduboss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eduboss.domain.FeedBack;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;
import com.eduboss.dto.GridRequest;
import com.eduboss.service.FeedBackReplyService;
import com.eduboss.service.FeedBackService;

@Controller
@RequestMapping(value="/FeedBackController")
public class FeedBackController {
	@Autowired
	private FeedBackService feedBackservice;
	
	@Autowired
	private FeedBackReplyService feedBackReplyService;
	
	/**
	 * 获取反馈信息
	 * @param feedBackvo
	 * @param gridRequest
	 * @return
	 */
	@RequestMapping(value="/getFeedBackList")
	@ResponseBody
	public DataPackageForJqGrid getFeedBackList(@ModelAttribute FeedBackVo feedBackvo,
			@ModelAttribute GridRequest gridRequest,
			String startDate,
			String endDate){
		DataPackage dataPackage=new DataPackage(gridRequest);
		dataPackage=feedBackservice.getFeedBackList(dataPackage, feedBackvo,startDate,endDate);
		DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
		return dataPackageForJqGrid;
	}
	
	/**
	 * 删除，添加反馈信息
	 * @param gridRequest
	 * @param feedBack
	 */
	 @RequestMapping(value="/editFeedback")
	 @ResponseBody
	public void editFeedback(@ModelAttribute GridRequest gridRequest,@ModelAttribute FeedBackVo feedBackvo){
		if ("del".equalsIgnoreCase(gridRequest.getOper())) {
			//删除
			feedBackservice.deleteFeedBack(feedBackvo);
		}else{
			//添加
			feedBackservice.saveOrUpdateFeedBack(feedBackvo);
		}
	}
	 
	 /**
	  * 查看反馈详情
	  * 
	  */
	 
	 @RequestMapping(value="/getFeedbackById")
	 @ResponseBody
	 public FeedBackVo getFeedbackById(String id){
		 return feedBackservice.findFeedBackById(id);
	 }
	 
	 /**
	  * 查询一个公告下的回复信息
	  */
	 @RequestMapping(value="/getReplyByFid")
	 @ResponseBody
	 public DataPackageForJqGrid getReplyByFid(@ModelAttribute GridRequest gridRequest,String fid){		 
		 DataPackage dataPackage=new DataPackage(gridRequest);
			dataPackage=feedBackReplyService.getReplyByFid(dataPackage, fid);
			DataPackageForJqGrid dataPackageForJqGrid=new DataPackageForJqGrid(dataPackage);
			return dataPackageForJqGrid;
	 }
	 
	 /**
	  * 删除或添加回复
	  * 
	  */
	 
	 @RequestMapping(value="/editFeedbackReply")
	 @ResponseBody
	 public void editFeedbackReply(@ModelAttribute GridRequest gridRequest,@ModelAttribute FeedbackReplyVo feedbackreplyVo){
		 if ("del".equalsIgnoreCase(gridRequest.getOper())) {
				//删除
			 feedBackReplyService.deleteFeedbackReply(feedbackreplyVo);
			}else{
				//添加
				feedBackReplyService.addFeedbackReply(feedbackreplyVo);
			}
	 }
	 
	 

}
