package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.SystemNoticeUser;
import com.eduboss.domainVo.SystemNoticeUserVo;
import com.eduboss.domainVo.SystemNoticeVo;

public interface SystemNoticeUserDao extends GenericDAO<SystemNoticeUser,String>{
	
	public List<SystemNoticeVo> getSystemNoticeUserList(SystemNoticeUserVo systemNoticeUserVo);

}
