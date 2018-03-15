package com.eduboss.dao;

import java.util.List;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.StaffBonusWeek;

public interface StaffBonusWeekDao extends GenericDAO<StaffBonusWeek, String> {

	/** 
	 *  根据用户Id跟日期找到业绩数据
	* @param date
	* @param staffId
	* @author  author :Yao 
	* @date  2016年7月7日 下午4:56:51 
	* @version 1.0 
	*/
	public StaffBonusWeek findInfoByDateAndStaff(String staffId,int week,int year);
	

	/** 
	 * 更新排名
	* @param userId
	* @param Date
	* @author  author :Yao 
	* @date  2016年7月8日 下午4:56:24 
	* @version 1.0 
	*/
	public void updateRankByid(String userId,int year,int week);
	
	/** 
	 * 查询周TOP10
	* @param year
	* @param week
	* @param branch
	* @param roleCode
	* @return
	* @author  author :Yao 
	* @date  2016年7月13日 上午10:19:17 
	* @version 1.0 
	*/
	public List<StaffBonusWeek> findUserWeekRankList(int year,int week,String userId,String brenchId);


	/** 
	 * 查询周top10根据角色分公司
	* @param year
	* @param week
	* @param branch
	* @param roleCode
	* @return
	* @author  author :Yao 
	* @date  2016年7月13日 上午10:25:07 
	* @version 1.0 
	*/
	public List<StaffBonusWeek> findUserWeekRankList(int year, int week,String branch, RoleCode roleCode,String userId);
	

	public List<StaffBonusWeek> findAllUserWeekRankList(int year, int week,String branch, RoleCode roleCode,String userId);

}
