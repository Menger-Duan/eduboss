package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.SystemNoticeUser;
import com.eduboss.domainVo.SystemNoticeUserVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.domainVo.WelcomeNoticeVo;
import com.eduboss.dto.DataPackage;

public interface SystemNoticeService {
	
	/**
	 * 系统公告管理列表
	 * */
	public DataPackage getSystemNoticeList(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	/**
	 * 系统公告管理列表
	 * */
	public DataPackage getSystemNoticeListByRoles(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	public DataPackage getSystemNoticeListByRolesWelCome(SystemNoticeVo systemNoticeVo, DataPackage dp);
	
	public SystemNoticeVo findSystemNoticeVoById(String id);
	
	/**
	 * 保存或修改公告
	 * */
	public void saveOrEditSystemNotice(SystemNotice systemNotice,MultipartFile myfile1, String isAllOrganization);
	
	/**
	 * 删除公告
	 * */
	public void deleteSystemNoticeById(SystemNotice systemNotice);
	
	/**
	 * 查询当前登录用户未读的必读公告
	 * */
	public List<WelcomeNoticeVo> getSystemNoticeUserList(SystemNoticeUserVo systemNoticeUserVo);
	
	/**
	 * 保存已读公告
	 * */
	public void saveHasReadedNotice(SystemNoticeUser systemNoticeUser);
	
	/**
	 * 查询已读公告用户
	 * */
	public List getHasReadedSystemNoticeUsers(SystemNoticeUserVo systemNoticeUserVo);
	
	/**
	 * 获取noticeType公告类型的前num条记录
	 * @param noticeType 公告类型
	 * @param num 前num条记录
	 * @return
	 */
	public List<SystemNoticeVo> getSystemNoticeByTypeTopNum(String recordId, String noticeType,String num);
	
	public List<SystemNoticeVo> getSystemNoticeOneAndMore(String recordId,String noticeType,String num);
	
	public void saveSystemNotice(SystemNotice systemNotice);

	public Map getTodayAndMonthTotal();
	
	  /**
     * 首页统计数据营收
     * @param miniClassId
     * @return
     */
    public List getTodayAndMonthTotalIncome();
    
    /**
     * 首页统计数据课时
     * @param miniClassId
     * @return
     */
    public List getTodayAndMonthTotalComsume();
    
    
    /**
     * 首页统计数据课时 课消 收款 根据不同的产品类型处理
     * @param miniClassId
     * @return
     */
    public List getTodayMonthConsumeByType(String productType);
    
    
    
    /**
     * 向手机端推送系统公告
     */
    public void pushSystemNoticeToMobile(SystemNotice systemNotice, String isAllOrganization);

}
