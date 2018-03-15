package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.StaffBonusDayDao;
import com.eduboss.domain.StaffBonusDay;

@Repository("staffBonusDayDao")
public class StaffBonusDayDaoImpl extends GenericDaoImpl<StaffBonusDay, String> implements StaffBonusDayDao {

	@Override
	public StaffBonusDay findInfoByDateAndStaff(String date, String staffId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Staff_bonus_day where 1=1");
		if(StringUtils.isNotBlank(date)){
			sql.append(" and count_date = :countDate ");
			params.put("countDate", date);
		}
		if(StringUtils.isNotBlank(staffId)){
			sql.append(" and user_id = :staffId ");
			params.put("staffId", staffId);
		}
		List<StaffBonusDay> list=this.findBySql(sql.toString(), params);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<Map<Object, Object>> findUserDayRankList(String startDate,String endDate, String branch, RoleCode roleCode, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select sbw.user_Id userId,sbw.user_Name username,sum(sbw.amount) amount,sbw.org_id orgId, ");
		sql.append(" sbw.org_name orgName,(case when sbw.user_id = :userId then 1 else 0 end) isMe,sum(sbw.online_amount) onlineAmount,sum(sbw.line_Amount) lineAmount from Staff_bonus_day sbw ");
		params.put("userId", userId);
		sql.append(" where 1=1");

		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and sbw.count_date >= :startDate ");
			params.put("startDate", startDate);
		}

		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and sbw.count_date <= :endDate ");
			params.put("endDate", endDate);
		}

		if(StringUtils.isNotBlank(branch)){
			sql.append(" and sbw.brench_id = :branchId ");
			params.put("branchId", branch);
		}

		if(roleCode!=null){
			if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
				sql.append(" and sbw.user_id in(select user_Id from user_organization_role ur,role r where r.roleCode = :roleCode and ur.role_id=r.id ) ");
			}else{
				sql.append(" and sbw.user_id in(select userId from user_role ur,role r where r.roleCode = :roleCode and ur.roleid=r.id ) ");
			}
			params.put("roleCode", roleCode);
		}
		sql.append(" group by sbw.user_id");
		sql.append(" order by sum(sbw.amount) desc");
		return this.findMapBySql(sql.toString(), params);
	}


	@Override
	public List<StaffBonusDay> findUserDayRankList(String startDate,String branch, RoleCode roleCode, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select distinct  sbw.ID,sbw.COUNT_DATE,sbw.USER_ID,sbw.USER_NAME,sbw.ORG_ID,sbw.ORG_NAME,sbw.AMOUNT,sbw.RANK,sbw.BALANCE,"
				+ " sbw.UP_DOWN,sbw.BRENCH_RANK,sbw.BRENCH_BALANCE,sbw.BRENCH_UP_DOWN,sbw.BRENCH_ID,sbw.BRENCH_NAME,sbw.CREATE_TIME,sbw.MODIFY_TIME," +
				"sbw.online_amount,sbw.line_amount"
				+ "  from Staff_bonus_day sbw ");

		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			sql.append(" left join user_organization_role ur on ur.user_ID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.role_ID");
		}else{
			sql.append(" left join user_role ur on ur.userID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.roleID");
		}
		sql.append(" where 1=1");

		if(StringUtils.isNotBlank(startDate)){
			sql.append(" and sbw.count_date = :countDate ");
			params.put("countDate", startDate);
		}

		if(StringUtils.isNotBlank(branch)){
			sql.append(" and sbw.brench_id = :branchId ");
			params.put("branchId", branch);
		}

		if(roleCode!=null){
			sql.append(" and r.roleCode = :roleCode ");
			params.put("roleCode", roleCode);
		}
		sql.append(" order by sbw.amount desc");
		return this.findBySql(sql.toString(), params);
	}


}
