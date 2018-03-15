package com.eduboss.dao;

import com.eduboss.domain.FeedbackReply;
import com.eduboss.dto.DataPackage;

public interface FeedbackReplyDao {
	
	/**
	 * 查看反馈回复
	 */
	
	public DataPackage getReplyByFid(DataPackage dataPackage,String fid);
	
	/**
	 * 删除反馈回复
	 */
	public void deleteFeedbackReply(FeedbackReply feedbacReply);
	/**
	 * 添加回复
	 */
	
	public void addFeedbackReply (FeedbackReply feedbacReply);

}
