package com.eduboss.dao;

import java.util.List;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.StaffBonusMonth;

public interface StaffBonusMonthDao extends GenericDAO<StaffBonusMonth, String> {

	/** 
	 *  根据用户Id跟日期找到业绩数据
	* @param date
	* @param staffId
	* @author  author :Yao 
	* @date  2016年7月7日 下午4:56:51 
	* @version 1.0 
	*/
	public StaffBonusMonth findInfoByDateAndStaff(String staffId,int month,int year);
	

	/** 
	 * 更新排名
	* @param userId
	* @param Date
	* @author  author :Yao 
	* @date  2016年7月8日 下午4:56:24 
	* @version 1.0 
	*/
	public void updateRankByid(String userId,int year,int month);
	
	/** 
	 * 月排名数据
	* @param year
	* @param month
	* @param userId
	* @author  author :Yao 
	* @date  2016年7月12日 下午3:07:18 
	* @version 1.0 
	*/
	public List<StaffBonusMonth> findUserMonthRankList(int year,int month,String userId,String brenchId);


	/** 
	 * 月top10
	* @param year
	* @param month
	* @param branch
	* @param roleCode
	* @param userId
	* @author  author :Yao 
	* @date  2016年7月13日 下午3:50:25 
	* @version 1.0 
	*/
	public List<StaffBonusMonth> findUserMonthRankList(int year, int month, String branch,RoleCode roleCode, String userId);
	
	public List<StaffBonusMonth> findAllUserMonthRankList(int year, int month, String branch,RoleCode roleCode, String userId);
	
}
