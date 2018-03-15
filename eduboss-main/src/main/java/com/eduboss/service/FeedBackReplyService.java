package com.eduboss.service;

import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.dto.DataPackage;

public interface FeedBackReplyService {
	/**
	 * 查看反馈回复
	 */
	
	public DataPackage getReplyByFid(DataPackage dataPackage,String fid);
	
	/**
	 * 删除反馈回复
	 */
	public void deleteFeedbackReply(FeedbackReplyVo feedbacReplyVo);
	/**
	 * 添加回复
	 */
	
	public void addFeedbackReply (FeedbackReplyVo feedbacReplyVo);
	
}
