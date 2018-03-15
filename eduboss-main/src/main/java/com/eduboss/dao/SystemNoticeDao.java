package com.eduboss.dao;

import java.util.List;

import com.eduboss.domain.SystemNotice;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.DataPackage;

public interface SystemNoticeDao  extends GenericDAO<SystemNotice, String>{
	
	/**
	 * 系统公告管理
	 * */
	public DataPackage getSystemNoticeList(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	/**
	 * 系统公告管理
	 * */
	public DataPackage getSystemNoticeListByRoles(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	/**
	 * 修改公告
	 * */
	public void modifySystemNotice(SystemNotice systemNotice);
	
	/**
	 * 无忧
	 * 系统公告管理
	 * */
	public DataPackage getSystemNoticeListByRolesFuckCriteria(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	public DataPackage getSystemNoticeListByRolesFuckCriteriaWelCome(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	public List<SystemNoticeVo> getSystemNoticeByTypeTopNum(String recordId, String noticeType,String num);
	
	public List<SystemNoticeVo> getSystemNoticeOneAndMore(String recordId,String noticeType,String num);
	
}
