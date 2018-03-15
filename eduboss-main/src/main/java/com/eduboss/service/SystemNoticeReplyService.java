package com.eduboss.service;

import java.util.List;

import com.eduboss.domain.SystemNoticeReply;
import com.eduboss.domainVo.SystemNoticeReplyVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.DataPackage;

public interface SystemNoticeReplyService {
	/**
	 * 保存或修改公告
	 * */
	public void saveOrEditSystemNoticeReply(SystemNoticeReply systemNotice);
	
	/**
	 * 获取系统公告回复
	 */
	public DataPackage getSystemNoticeReplyList(
			SystemNoticeReplyVo systemNotiplyVo, DataPackage dp);
	
	/**
	 * 删除系统公告回复
	 */
	public void deleteSystemNoticeReply(SystemNoticeReply reply);
}
