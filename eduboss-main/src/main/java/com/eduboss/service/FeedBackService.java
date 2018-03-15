package com.eduboss.service;


import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.DataPackageForJqGrid;



public interface FeedBackService {
	/**
	 * 查询登录用户可以看到的反馈信息
	 */
	public DataPackage getFeedBackList(DataPackage dataPackage,FeedBackVo feedBackvo,String startDate,String endDate);
	
	/**
	 * 添加，反馈信息
	 */
	public void saveOrUpdateFeedBack(FeedBackVo feedBackvo);
	
	/**
	 * ，删除 反馈信息
	 */
	public void deleteFeedBack(FeedBackVo feedBackvo);
	
	/**
	 * 查看详情
	 */
	
	public FeedBackVo findFeedBackById(String id);
	
	
}
