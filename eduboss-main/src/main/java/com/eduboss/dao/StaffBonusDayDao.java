package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.common.RoleCode;
import com.eduboss.domain.StaffBonusDay;

public interface StaffBonusDayDao extends GenericDAO<StaffBonusDay, String> {

	/**
	 *  根据用户Id跟日期找到业绩数据
	 * @param date
	 * @param staffId
	 * @author  author :Yao
	 * @date  2016年7月7日 下午4:56:51
	 * @version 1.0
	 */
	public StaffBonusDay findInfoByDateAndStaff(String date,String staffId);


	/**
	 * 按日期查询top10
	 * @param startDate
	 * @param endDate
	 * @param branch
	 * @param roleCode
	 * @param userId
	 * @return
	 * @author  author :Yao
	 * @date  2016年7月13日 下午3:58:19
	 * @version 1.0
	 */
	public List<Map<Object,Object>> findUserDayRankList(String startDate,String endDate, String branch, RoleCode roleCode, String userId);

	/**
	 * 按日期查询top10
	 * @param startDate
	 * @param endDate
	 * @param branch
	 * @param roleCode
	 * @param userId
	 * @return
	 * @author  author :Yao
	 * @date  2016年7月13日 下午3:58:19
	 * @version 1.0
	 */
	public List<StaffBonusDay> findUserDayRankList(String startDate, String branch, RoleCode roleCode, String userId);

}
