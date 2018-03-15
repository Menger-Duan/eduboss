package com.eduboss.dao;

import com.eduboss.domain.FeedBack;

import com.eduboss.domain.FeedbackReply;
import com.eduboss.domainVo.FeedBackVo;
import com.eduboss.dto.DataPackage;


public interface FeedBackDao extends GenericDAO<FeedBack, String> {
	/**
	 * 查询登录用户可以看到的反馈信息
	 */
	public DataPackage getFeedBackList(DataPackage dataPackage,FeedBackVo feedBackvo,String startDate,
			String endDate);
	
	/**
	 * 添加 反馈信息
	 */
	public void saveOrUpdateFeedBack(FeedBack feedBack);
	
	/**
	 * ，删除 反馈信息
	 */
	public void deleteFeedBack(FeedBack feedBack);
	
	/**
	 * 查看详情
	 */
	
	public FeedBackVo findFeedBackById(String id);
	

}
