package com.eduboss.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.FeedbackReplyDao;
import com.eduboss.domain.FeedbackReply;
import com.eduboss.domainVo.FeedbackReplyVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.FeedBackReplyService;
import com.eduboss.utils.HibernateUtils;


@Service("FeedBackReplyService")
public class FeedBackReplyServiceImpl implements FeedBackReplyService {
	@Autowired
	private FeedbackReplyDao feedbackReplyDao;
	
	/**
	 * 查看反馈回复
	 */
	@Override
	public DataPackage getReplyByFid(DataPackage dataPackage,String fid){
		
		
		return feedbackReplyDao.getReplyByFid(dataPackage, fid);
	}
	
	/**
	 * 删除反馈回复
	 */
	@Override
	public void deleteFeedbackReply(FeedbackReplyVo feedbackReplyVo){
		FeedbackReply feedbackreply = HibernateUtils.voObjectMapping(feedbackReplyVo, FeedbackReply.class);
		if(feedbackReplyVo!=null){			
			feedbackReplyDao.deleteFeedbackReply(feedbackreply);
		}
		
	}
	/**
	 * 添加回复
	 */
	@Override
	public void addFeedbackReply (FeedbackReplyVo feedbacReplyVo){
		FeedbackReply feedbackreply = HibernateUtils.voObjectMapping(feedbacReplyVo, FeedbackReply.class);
		if(feedbackreply!=null){
			feedbackReplyDao.addFeedbackReply(feedbackreply);
		}
	}

}
