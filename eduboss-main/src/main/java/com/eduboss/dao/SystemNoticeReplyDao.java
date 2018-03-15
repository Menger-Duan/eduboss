package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domainVo.SystemNoticeReplyVo;
import com.eduboss.domainVo.SystemNoticeUserVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.DataPackage;

public interface SystemNoticeReplyDao extends GenericDAO<SystemNoticeReply,String>{
	
	public DataPackage getSystemNoticeReplyList(
			SystemNoticeReplyVo systemNoticeReplyVo, DataPackage dp);

	public void deleteSystemNoticeReply(SystemNoticeReply reply);
}
