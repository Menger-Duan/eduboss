package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.common.Constants;
import com.eduboss.utils.PropertiesUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.eduboss.common.RoleCode;
import com.eduboss.dao.StaffBonusMonthDao;
import com.eduboss.domain.StaffBonusMonth;

@Repository("staffBonusMonthDao")
public class StaffBonusMonthDaoImpl extends GenericDaoImpl<StaffBonusMonth, String> implements StaffBonusMonthDao {

	@Override
	public StaffBonusMonth findInfoByDateAndStaff(String staffId,int month,int year) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from Staff_bonus_month where 1=1");
		if(year>0){
			sql.append(" and year = :year ");
			params.put("year", year);
		}
		if(month>0){
			sql.append(" and month = :month ");
			params.put("month", month);
		}
		if(StringUtils.isNotBlank(staffId)){
			sql.append(" and user_id = :staffId ");
			params.put("staffId", staffId);
		}
		List<StaffBonusMonth> list=this.findBySql(sql.toString(), params);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}

	@Override
	public void updateRankByid(String userId,int year,int month) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" update staff_bonus_day re left join ");
		sql.append(" (SELECT id, amount, @rownum := @rownum +1 AS ranking");
		sql.append(" FROM staff_bonus_day t, (SELECT@rownum :=0) r");
		sql.append(" where t.USER_ID = :userId and t.year = :tYear and t.month = :tMonth order by amount desc) base on re.id=base.id ");
		sql.append(" set re.rank=base.ranking where re.month = :reMonth and re.year = :reYear ");
		params.put("userId", userId);
		params.put("tYear", year);
		params.put("tMonth", month);
		params.put("reMonth", month);
		params.put("reYear", year);
		this.excuteSql(sql.toString(), params);
	}

	@Override
	public List<StaffBonusMonth> findUserMonthRankList(int year, int month, String userId,String brenchId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select * from Staff_bonus_month where 1=1");
		sql.append(" and year = :year ");
		sql.append(" and month = :month ");
		params.put("year", year);
		params.put("month", month);
		if(StringUtils.isNotBlank(userId)){
			sql.append(" and user_id = :userId ");
			params.put("userId", userId);
		}
		if(StringUtils.isNotBlank(brenchId)){
			sql.append(" and brench_id = :brenchId ");
			params.put("brenchId", brenchId);
		}
		sql.append(" order by amount desc ");
		return this.findBySql(sql.toString(), params);
		
	}

	@Override
	public List<StaffBonusMonth> findUserMonthRankList(int year, int month, String branch,
			RoleCode roleCode, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select distinct  sbw.ID,sbw.YEAR,sbw.MONTH,sbw.USER_ID,sbw.USER_NAME,sbw.ORG_ID,sbw.ORG_NAME,sbw.AMOUNT,sbw.RANK,sbw.BALANCE,"
				+ " sbw.UP_DOWN,sbw.BRENCH_RANK,sbw.BRENCH_BALANCE,sbw.BRENCH_UP_DOWN,sbw.BRENCH_ID,sbw.BRENCH_NAME,sbw.CREATE_TIME,sbw.MODIFY_TIME"
				+ "  from Staff_bonus_month sbw ");
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			sql.append(" left join user_organization_role ur on ur.user_ID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.role_ID");
		}else{
			sql.append(" left join user_role ur on ur.userID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.roleID");
		}
		sql.append(" where 1=1");
		sql.append(" and sbw.year = :year ");
		sql.append(" and sbw.month = :month ");
		params.put("year", year);
		params.put("month", month);
		
		
		if(StringUtils.isNotBlank(branch)){
			sql.append(" and (sbw.brench_rank<11 or sbw.user_id = :userId)");//前10  或者是自己的排名
			sql.append(" and sbw.brench_id = :brenchId ");
			params.put("userId", userId);
			params.put("brenchId", branch);
		}else{
			sql.append(" and (sbw.rank<11 or sbw.user_id = :userId)");//前10  或者是自己的排名
			params.put("userId", userId);
		}
		if(roleCode!=null){
			sql.append(" and r.roleCode = :roleCode");
			params.put("roleCode", roleCode);
		}
		sql.append(" order by sbw.rank,sbw.brench_rank  ");
		return this.findBySql(sql.toString(), params);
	}
	
	
	@Override
	public List<StaffBonusMonth> findAllUserMonthRankList(int year, int month,
			String branch, RoleCode roleCode, String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sql=new StringBuffer();
		sql.append(" select distinct  sbw.ID,sbw.YEAR,sbw.MONTH,sbw.USER_ID,sbw.USER_NAME,sbw.ORG_ID,sbw.ORG_NAME,sbw.AMOUNT,sbw.RANK,sbw.BALANCE,"
				+ " sbw.UP_DOWN,sbw.BRENCH_RANK,sbw.BRENCH_BALANCE,sbw.BRENCH_UP_DOWN,sbw.BRENCH_ID,sbw.BRENCH_NAME,sbw.CREATE_TIME,sbw.MODIFY_TIME," +
				"sbw.online_amount,sbw.line_amount"
				+ "  from Staff_bonus_month sbw ");
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			sql.append(" left join user_organization_role ur on ur.user_ID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.role_ID");
		}else{
			sql.append(" left join user_role ur on ur.userID=sbw.user_id");
			sql.append(" left join role r on r.id=ur.roleID");
		}
		sql.append(" where 1=1");
		sql.append(" and sbw.year = :year ");
		sql.append(" and sbw.month = :month ");
		params.put("year", year);
		params.put("month", month);
		
		if(StringUtils.isNotBlank(branch)){
			sql.append(" and sbw.brench_id = :brenchId ");
			params.put("brenchId", branch);
		}
		if(roleCode!=null){
			sql.append(" and r.roleCode = :roleCode");
			params.put("roleCode", roleCode);
		}
		sql.append(" order by sbw.rank,sbw.brench_rank  ");
		return this.findBySql(sql.toString(), params);
	}
	
}
