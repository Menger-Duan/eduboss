package com.eduboss.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.eduboss.common.RoleCode;
import com.eduboss.domainVo.UserAmountRankInfo;
import com.eduboss.jedis.Message;


public interface StaffBonusDayService {
	public int saveOrUpdateStaffBonus(Message messge);

	public List<UserAmountRankInfo> getRankWithOrgAndTimeSpanOrgs(String startDate,String endDate, String branch,String role);

	public void updateTop10Rank(String type) throws SQLException ;

	public List<Map<Object,Object>> getTop10ForPC(String startDate,String endDate, Boolean isBranch, RoleCode roleCode);

}

