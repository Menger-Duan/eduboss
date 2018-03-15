package com.eduboss.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.components.Else;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ReportDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.Organization;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.jedis.RedisDataSource;
import com.eduboss.service.ReportFormsService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.google.common.collect.Maps;



@Service
public class ReportFormsServiceImpl implements ReportFormsService {
	public static int top = 10;

	// 缓存组织架构信息
	private static Map<String, Organization> organizationMap = null;
	
	private static final int CACHE_EXPIRE_TIME = PropertiesUtils.getIntValue("CACHE_EXPIRE_TIME");
	private static final String isCahche = PropertiesUtils.getStringValue("isCahche");

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userService;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Autowired
	private ReportDao reportDao;
	
	
	@Autowired
	private RedisDataSource redisDataSource;

	public Map<String, Organization> getOrganizationMap() {
		if (organizationMap == null) {
			organizationMap = new HashMap<String, Organization>();
			List<Organization> organizationList = organizationDao.findAll();
			for (Organization organization : organizationList) {
				organizationMap.put(organization.getId(), organization);
			}
		}
		return organizationMap;
	}

	/**
	 * 获取所有top15的数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15Data(String startDate,
			String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getUserPerformance(startDate,
				endDate, blBranch, null);
		List<Map<String, Object>> list2 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.CONSULTOR);
		List<Map<String, Object>> list3 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.STUDY_MANAGER);
		// List list2=getConsultorPerformance(startDate, endDate, blBranch);
		// List list3=getStudyManagerPerformance(startDate, endDate, blBranch);
		List<Map<String, Object>> list4 = getTeacherHours(startDate, endDate,
				blBranch);
		List<Map<String, Object>> list5 = getStudyManagerHours(startDate,
				endDate, blBranch);
		list.add(list1);
		list.add(list2);
		list.add(list3);
		list.add(list4);
		list.add(list5);
		return list;
	}

	/**
	 * 获取第一个表的top15数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15UserPerformance(
			String startDate, String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getUserPerformance(startDate,
				endDate, blBranch, null);
		list.add(list1);
		return list;
	}

	/**
	 * 获取第二个表的top15数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15ConsultorPerformance(
			String startDate, String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.CONSULTOR);
		list.add(list1);
		return list;
	}

	/**
	 * 获取第三个表的top15数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15StudyManagerPerformance(
			String startDate, String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.STUDY_MANAGER);
		list.add(list1);
		return list;
	}

	/**
	 * 获取第四个表的top15数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15TeacherHours(
			String startDate, String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getTeacherHours(startDate, endDate,
				blBranch);
		list.add(list1);
		return list;
	}

	/**
	 * 获取第五个表的top15数据
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15StudyManagerHours(
			String startDate, String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getStudyManagerHours(startDate,
				endDate, blBranch);
		list.add(list1);
		return list;
	}

	/**
	 * 前三后二地获取top15的数据 - 之前三
	 */
	@Override
	public List<List<Map<String, Object>>> getTop15DataFirst3(String startDate,
			String endDate, Boolean isQueryAll) {
		List<List<Map<String, Object>>> list = new ArrayList<List<Map<String, Object>>>();
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		List<Map<String, Object>> list1 = getUserPerformance(startDate,
				endDate, blBranch, null);
		List<Map<String, Object>> list2 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.CONSULTOR);
		List<Map<String, Object>> list3 = getUserPerformance(startDate,
				endDate, blBranch, RoleCode.STUDY_MANAGER);
		list.add(list1);
		list.add(list2);
		list.add(list3);
		return list;
	}

	@Override
	public List<Map<String, Object>> getRankWithOrgAndTimeSpan(String startDate,String endDate, Organization blBranch,RoleCode roleCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		list = getUserPerformance1(startDate,endDate, blBranch, roleCode);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getRankWithOrgAndTimeSpanOrgs(String startDate,String endDate, List<Organization> blBranchs,RoleCode roleCode) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		list = getUserPerformanceByOrgs(startDate,endDate, blBranchs, roleCode);
		return list;
	}
	
	/**
	 * 前三后二地获取top15的数据 - 之后二
	 */
	@Override
	public List<Map<String, Object>> getTop15DataLast2(String startDate,
			String endDate, Boolean isQueryAll,String type) {
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
		if (StringUtils.isNotEmpty(endDate)) {
			endDate = DateTools.addDateToString(endDate, 1);
		}
		if(type==null){
			return null;
		}
		
		if(type.equals("teacher")){
			return getTeacherHours(startDate, endDate,blBranch);
		}else if (type.equals("studyManager")){
			return getStudyManagerHours(startDate,endDate, blBranch);
		}
		return null;
	}

	/**
	 * 员工、咨询师、学管师Top10查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @param roleCode
	 * @return
	 */
	@Deprecated
	public List<Map<String, Object>> getUserPerformance(String startDate,
			String endDate, Organization blBranch, RoleCode roleCode) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and TRANSACTION_TIME>='" + startDate + "'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and TRANSACTION_TIME<'" + endDate + "'";
			}
		}
		if (blBranch != null) {
			sqlWhere += " and u.organizationID IN (SELECT id FROM organization WHERE orgLevel like '"
					+ blBranch.getOrgLevel() + "%' )";
		}
		// if (roleCode != null) {
		// sqlWhere +=
		// " and u.USER_ID IN (SELECT userID FROM user_role WHERE roleID IN (SELECT id FROM role WHERE roleCode='"
		// + roleCode + "') )";
		// }
		// FIXME: 2017/2/17 不要用 CONTRACT_BONUS
		String sql = " SELECT u.NAME username,sum(CASE WHEN cb.BONUS_TYPE <> 'FEEDBACK_REFUND' THEN cb.BONUS_AMOUNT else (-cb.BONUS_AMOUNT) end) amount, u.organizationID orgid, o.name orgName FROM funds_change_history fch "
				+ " INNER JOIN CONTRACT_BONUS cb ON fch.ID=cb.FUNDS_CHANGE_ID AND BONUS_STAFF_ID IS NOT NULL"
				+ " INNER JOIN user u ON cb.BONUS_STAFF_ID =u.USER_ID "
				+ " INNER JOIN organization o on o.id=u.organizationID ";
		if (roleCode != null) {
			sql += "INNER JOIN user_role ur ON ur.userID = u.USER_ID INNER JOIN role r ON r.id = ur.roleID and roleCode='";
			sql += roleCode;
			sql += "' ";
		}
		sql += " WHERE fch.CHANNEL IN ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY') ";
		sql += " AND fch.CONTRACT_ID IS NOT NULL ";
		sql += sqlWhere;
		sql += " GROUP BY cb.BONUS_STAFF_ID ";
		sql += " ORDER BY amount desc LIMIT 0," + top;
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	/**
	 * 员工、咨询师、学管师Top10查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @param roleCode
	 * @return
	 */
	@Deprecated
	private List<Map<String, Object>> getUserPerformance1(String startDate,
			String endDate, Organization blBranch, RoleCode roleCode) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and TRANSACTION_TIME>='" + startDate + "'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and TRANSACTION_TIME<='" + endDate + "'";
			}
		}
		if (blBranch != null) {
			sqlWhere += " and u.organizationID IN (SELECT id FROM organization WHERE orgLevel like '"
					+ blBranch.getOrgLevel() + "%' )";
		}
		// FIXME: 2017/2/17 不要用CONTRACT_BONUS
		String sql = " SELECT u.user_id user_id,u.NAME username,sum(cb.BONUS_AMOUNT) amount, u.organizationID orgid FROM funds_change_history fch "
				+ " INNER JOIN CONTRACT_BONUS cb ON fch.ID=cb.FUNDS_CHANGE_ID AND BONUS_STAFF_ID IS NOT NULL"
				+ " INNER JOIN user u ON cb.BONUS_STAFF_ID =u.USER_ID ";
		if (roleCode != null) {
			sql += "INNER JOIN user_role ur ON ur.userID = u.USER_ID INNER JOIN role r ON r.id = ur.roleID and roleCode='";
			sql += roleCode;
			sql += "' ";
		}
		sql += " WHERE fch.CHANNEL IN ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER','REFUND_MONEY') ";
		sql += " AND fch.CONTRACT_ID IS NOT NULL ";
		sql += sqlWhere;
		sql += " GROUP BY cb.BONUS_STAFF_ID ";
		sql += " ORDER BY amount desc 0,"+top;
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Deprecated
	private List<Map<String, Object>> getUserPerformanceByOrgs(String startDate,
			String endDate, List<Organization> blBranchs, RoleCode roleCode) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and TRANSACTION_TIME>='" + startDate + "'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and TRANSACTION_TIME<='" + endDate + "'";
			}
		}
		if (blBranchs != null && blBranchs.size()>0) {
			String sqlTemp=" orgLevel like '"+blBranchs.get(0).getOrgLevel()+"%'";
			for(int i=1;i<blBranchs.size();i++){
				sqlTemp+=" or orgLevel like '"+blBranchs.get(i).getOrgLevel()+"%'";
			}
			sqlWhere += " and u.organizationID IN (SELECT id FROM organization WHERE " +sqlTemp+ ")";
		}
		// FIXME: 2017/2/17 不要用CONTRACT_BONUS
		String sql = " SELECT u.user_id user_id,u.NAME username,sum(CASE WHEN cb.BONUS_TYPE <> 'FEEDBACK_REFUND' THEN cb.BONUS_AMOUNT else (-cb.BONUS_AMOUNT) end) amount, u.organizationID orgid,"
				+ " (case when length(o.orgLevel)>8 then concat((select name from organization where orgLevel=substring(o.orgLevel, 1, 8 )),' - ',o.name)  "
				+ " when length(o.orgLevel)>4 then concat((select name from organization where orgLevel=substring(o.orgLevel, 1, 4 )),' - ',o.name) "
				+ " else o.name end) orgName"
				+ " FROM funds_change_history fch "
				+ " INNER JOIN CONTRACT_BONUS cb ON fch.ID=cb.FUNDS_CHANGE_ID AND BONUS_STAFF_ID IS NOT NULL"
				+ " INNER JOIN user u ON cb.BONUS_STAFF_ID =u.USER_ID ";
		if (roleCode != null) {
			sql += "INNER JOIN user_role ur ON ur.userID = u.USER_ID INNER JOIN role r ON r.id = ur.roleID and roleCode='";
			sql += roleCode;
			sql += "' ";
		}
		sql+=" left join organization o on o.id=u.organizationID ";
		sql += " WHERE fch.CHANNEL IN ('POS', 'CASH','WEB_CHART_PAY','ALI_PAY','BANK_TRANSFER', 'REFUND_MONEY') ";
		sql += " AND fch.CONTRACT_ID IS NOT NULL ";
		sql += sqlWhere;
		sql += " GROUP BY cb.BONUS_STAFF_ID ";
		sql += " ORDER BY amount desc";
		return fireSQLAndReturnListInBranchCampus(sql);
	}
	
	/**
	 * 教师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getTeacherHours(String startDate,
			String endDate, Organization blBranch) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (blBranch != null) {
			sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
					+ blBranch.getOrgLevel() + "%' )";
		}
		String sql = " SELECT b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o, organization o2 "
				+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ " and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE'  "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result), CACHE_EXPIRE_TIME);
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}

	}

	/**
	 * 教师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getTeacherHoursByProductType(String startDate,
			String endDate, Organization blBranch,String productType) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (blBranch != null) {
			sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
					+ blBranch.getOrgLevel() + "%' )";
		}
		
		String sql = null;
		if("TOTAL".equals(productType)){
			//如果是total 分为三个数据返回					
//			sql = " SELECT b.USER_ID,b.NAME username,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName, "
//					+"SUM(a.QUANTITY) amount,"
//					+"SUM(case when a.PRODUCT_TYPE ='ONE_ON_ONE_COURSE' then a.QUANTITY else 0 end) amount_one,"
//					+"SUM(case when a.PRODUCT_TYPE ='SMALL_CLASS' then a.QUANTITY else 0 end) amount_small,"
//					+"SUM(case when (a.PRODUCT_TYPE !='ONE_ON_ONE_COURSE' and a.PRODUCT_TYPE !='SMALL_CLASS') then a.QUANTITY else 0 end) amount_other"
//					+ " FROM account_charge_records a, user b, organization o, organization o2 "
//					+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
//					+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
//					//+ " and (a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' or a.PRODUCT_TYPE='SMALL_CLASS' or a.PRODUCT_TYPE='ONE_ON_ONE_COURSE'  ) "
//					+ sqlWhere
//					+ " GROUP BY b.USER_ID "
//					+ " ORDER BY amount desc LIMIT 0," + top;

			String sqlWhereOne= "";
			String sqlWhereSmall="";
			String sqlWhereOtm = "";
			String sqlWhereTwo = "";


			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhereOne += " and c.COURSE_DATE>='" + startDate+"'";
				sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate+"'";
				sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
				sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate+"'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhereOne += " and c.COURSE_DATE<='" + endDate + "'";
				sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate + "'";
				sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
				sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate + "'";
			}


			if (blBranch != null) {
				sqlWhereOne += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlWhereSmall += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlWhereOtm += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlWhereTwo += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}

			sql =" select a.userId USER_ID,a.userName username,a.orgId orgid,a.branchId branchid,a.orgName,sum(a.amount) as amount,"
					+"sum(case when a.atype=1 then a.amount else 0 end) amount_one,"
					+"sum(case when a.atype=2 then a.amount else 0 end) amount_small,"
					+"sum(case when a.atype=3 then a.amount else 0 end) amount_other "
					+ " from ( "
					+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(c.REAL_HOURS) as amount "
					+" ,1 as atype"
					+" from course c,`user` u,organization o,organization oo "
					+" where c.TEACHER_ID = u.USER_ID "
					+" and c.BL_CAMPUS_ID = oo.id "
					+" and u.organizationID = o.id "
					+" and c.COURSE_STATUS ='CHARGED' "
					+sqlWhereOne
					+" group by c.TEACHER_ID "
					+" UNION ALL "
					+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
					+" ,2 as atype"
					+" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "
					+" where mcc.TEACHER_ID = u.USER_ID "
					+" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
					+" and mc.BL_CAMPUS_ID = oo.id "
					+" and u.organizationID = o.id "
					+" and mcc.COURSE_STATUS ='CHARGED' "
					+sqlWhereSmall
					+" group by mcc.TEACHER_ID "
					+" UNION ALL "
			        + " select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount "
			        +" ,3 as atype"
			        + " from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
			        + " where occ.TEACHER_ID = u.USER_ID "
		           	+ " and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
			        + " and oc.BL_CAMPUS_ID = oo.id "
			        + " and u.organizationID = o.id "
			        + " and occ.COURSE_STATUS ='CHARGED' "
			        + sqlWhereOtm
			        + "group by occ.TEACHER_ID "
			        + " UNION ALL "
			        + "select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
			        +" ,3 as atype"
			        + " from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb "
                    +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                    +" and ttc.CLASS_ID = ttb.CLASS_ID "
                    +" and ttc.TEACHER_ID = u.USER_ID "
                    +" and u.organizationID = o.id "
                    +" and ttcc.COURSE_STATUS ='CHARGED' "
                    +sqlWhereTwo
                    +" group by ttc.TEACHER_ID "
                    +" UNION ALL "
                    +" select  userId, userName, orgId, branchId, orgName,sum(b.amount) as amount "
    		        +" ,3 as atype"
                    +" from("
                    +" select DISTINCT u.USER_ID as userId,u.`NAME` as userName,oo.id,ttct.CLASS_TWO_ID,ttcc.COURSE_ID "
                    +" as orgId,oo.parentID as branchId,o.`name` as orgName,ttcc.COURSE_HOURS as amount "
                    +" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo ,two_teacher_class_student_attendent ttcsa"
                    +" "
                    +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                    +" and ttcsa.TWO_CLASS_COURSE_ID=ttcc.COURSE_ID and ttcsa.CLASS_TWO_ID=ttct.CLASS_TWO_ID"
                    +" and ttct.CLASS_ID = ttc.CLASS_ID "
                    +" and ttct.TEACHER_ID = u.USER_ID "
                    +" and ttct.BL_CAMPUS_ID = oo.id "
                    +" and u.organizationID = o.id "
                    +" and ttcsa.CHARGE_STATUS ='CHARGED' "
                    +sqlWhereTwo
                    +" group by ttct.CLASS_TWO_ID,ttcc.COURSE_ID) b group by b.userId "
                    +" ) as a "
                    +" group by a.userId "
                    +" order by amount desc LIMIT 0," + top;
		}else if("OTHERS".equals(productType)){
			sql = " SELECT b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
					+ " FROM account_charge_records a, user b, organization o, organization o2 "
					+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
					+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
					+ " and a.PRODUCT_TYPE <>'ONE_ON_ONE_COURSE' and  a.PRODUCT_TYPE <>'SMALL_CLASS' "
					+ sqlWhere
					+ " GROUP BY b.USER_ID "
					+ " ORDER BY amount desc LIMIT 0," + top;
			
			String sqlWhereOtm = "";
			String sqlWhereTwo = "";
			
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
				sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate+"'";			
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate+"'";
				sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate+"'";
			}
			if (blBranch != null) {
				sqlWhereOtm += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlWhereTwo += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}
			
			
		    sql = "select a.userId as USER_ID ,a.userName as username,a.orgId as orgid,a.branchId as branchid,a.orgName,sum(a.amount) as amount from ("
					+ " select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount "
					+ " from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
					+ " where occ.TEACHER_ID = u.USER_ID "
					+ " and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
					+ " and oc.BL_CAMPUS_ID = oo.id "
					+ " and u.organizationID = o.id "
					+ " and occ.COURSE_STATUS ='CHARGED' "
					+ sqlWhereOtm
					+ "group by occ.TEACHER_ID "
					+ " UNION ALL "
					+ "select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
					+ " from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb "
	                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
	                +" and ttc.CLASS_ID = ttb.CLASS_ID "
	                +" and ttc.TEACHER_ID = u.USER_ID "
	                +" and u.organizationID = o.id "
	                +" and ttcc.COURSE_STATUS ='CHARGED' "
	                +sqlWhereTwo
	                +" group by ttc.TEACHER_ID "
	                +" UNION ALL "
	                +" select  userId, userName, orgId, branchId, orgName,sum(b.amount) as amount from("
	                +" select DISTINCT u.USER_ID as userId,u.`NAME` as userName,oo.id,ttct.CLASS_TWO_ID,ttcc.COURSE_ID "
	                +" as orgId,oo.parentID as branchId,o.`name` as orgName,ttcc.COURSE_HOURS as amount "
	                +" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo ,two_teacher_class_student_attendent ttcsa"
	                +" "
	                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
	                +" and ttcsa.TWO_CLASS_COURSE_ID=ttcc.COURSE_ID and ttcsa.CLASS_TWO_ID=ttct.CLASS_TWO_ID"
	                +" and ttct.CLASS_ID = ttc.CLASS_ID "
	                +" and ttct.TEACHER_ID = u.USER_ID "
	                +" and ttct.BL_CAMPUS_ID = oo.id "
	                +" and u.organizationID = o.id "
	                +" and ttcsa.CHARGE_STATUS ='CHARGED' "
	                +sqlWhereTwo
	                +" group by ttct.CLASS_TWO_ID,ttcc.COURSE_ID) b group by b.userId "  
	                +" ) as a "
	                +" group by a.userId "
	                +" order by amount desc LIMIT 0," + top;
			
			
			
			
		}else{
			sql = " SELECT b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
					+ " FROM account_charge_records a, user b, organization o, organization o2 "
					+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
					+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
					+ " and a.PRODUCT_TYPE ='"+productType+"' "
					+ sqlWhere
					+ " GROUP BY b.USER_ID "
					+ " ORDER BY amount desc LIMIT 0," + top;		
		}

		List<Map<String, Object>> result =null;
		
		Boolean cacheFlag = Boolean.valueOf(isCahche);
		if(!cacheFlag){
			result = fireSQLAndReturnListInBranchCampus(sql);
			return result;
		}else{
			//加入缓存  先对sql进行hash
			String key =MD5.getMD5("sql"+sql);
		
			try {
				byte[] keybyte =ObjectUtil.objectToBytes(key);
				if(JedisUtil.exists(keybyte)){			
				    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
				    result = (List<Map<String, Object>>) object;
				    return result;
				}else{
					result = fireSQLAndReturnListInBranchCampus(sql);
					JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result), CACHE_EXPIRE_TIME);
					return result;
				}
			} catch (Exception e) {
				System.out.println("ObjectUtil.objectToBytes错了");
				e.printStackTrace();
				throw new ApplicationException(e.getMessage());
			}			
		}
		


	}
	/**
	 * 学管师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getStudyManagerHours(String startDate,
			String endDate, Organization blBranch) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (blBranch != null) {
			sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
					+ blBranch.getOrgLevel() + "%' )";
		}
		String sql = "select b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
				+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
				+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
		


	}

	/**
	 * 学管师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getStudyManagerHoursByProductType(String startDate,
			String endDate, Organization blBranch,String productType) {
		String sqlWhere = "";
		
    	String sql = null;
		if("TOTAL".equals(productType)){
//			sql = "select b.USER_ID,b.NAME username,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName, "
//					+"SUM(a.QUANTITY) amount,"
//					+"SUM(case when a.PRODUCT_TYPE ='ONE_ON_ONE_COURSE' then a.QUANTITY else 0 end) amount_one,"
//					+"SUM(case when a.PRODUCT_TYPE ='SMALL_CLASS' then a.QUANTITY else 0 end) amount_small,"
//					+"SUM(case when (a.PRODUCT_TYPE !='ONE_ON_ONE_COURSE' and a.PRODUCT_TYPE !='SMALL_CLASS') then a.QUANTITY else 0 end) amount_other"
//					+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
//					+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
//					//+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
//					+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
//					+ sqlWhere
//					+ " GROUP BY b.USER_ID "
//					+ " ORDER BY amount desc LIMIT 0," + top;
			
			String sqlWhereOne= "";
			String sqlWhereSmall="";
			String sqlWhereOtm = "";
			String sqlwhereOtm ="";
			

			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhereOne += " and c.COURSE_DATE>='" + startDate+"'";
				sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate+"'";
				sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhereOne += " and c.COURSE_DATE<='" + endDate+"'";
				sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate+"'";
				sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate+"'";
			}
			if (blBranch != null) {
				sqlWhereOne += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlWhereSmall += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
				sqlwhereOtm += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}

			sql ="select a.USER_ID,a.username,a.orgid,a.branchid,a.orgName,sum(a.amount) as amount,"
					+"sum(case when a.atype=1 then a.amount else 0 end) amount_one,"
					+"sum(case when a.atype=2 then a.amount else 0 end) amount_small,"
					+"sum(case when a.atype=3 then a.amount else 0 end) amount_other "
					+ " from ( "
	                +" select u.USER_ID,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(c.REAL_HOURS) as amount, " 
	                +" 1 as atype "
	                +" from course c,`user` u,organization o,organization oo "  
	                +" where c.STUDY_MANAGER_ID = u.USER_ID "
	                +" and c.BL_CAMPUS_ID = oo.id "
	                +" and u.organizationID = o.id "
	                +" and c.COURSE_STATUS ='CHARGED' " 
	                +sqlWhereOne
	                +" group by c.STUDY_MANAGER_ID "
	                +" UNION ALL "
	                +" select u.USER_ID,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount, " 
	                +" 2 as atype "
	                +" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "  
	                +" where mcc.STUDY_MANEGER_ID = u.USER_ID "
	                +" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
	                +" and mc.BL_CAMPUS_ID = oo.id "
	                +" and u.organizationID = o.id "
	                +" and mcc.COURSE_STATUS ='CHARGED' " 
	                +sqlWhereSmall
	                +" group by mcc.STUDY_MANEGER_ID "
	                +" UNION ALL "
                    +"select u.USER_ID ,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(aa.COURSE_HOURS) as amount, " 
					+" 3 as atype from "
					+" (select occ.OTM_CLASS_ID,ocsa.STUDY_MANAGER_ID,occ.COURSE_HOURS from otm_class_student_attendent ocsa,otm_class_course occ"
			        +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
					+" and ocsa.CHARGE_STATUS ='CHARGED' "
                    +sqlWhereOtm
                    +" GROUP BY ocsa.OTM_CLASS_COURSE_ID) as aa, "
			        +" otm_class oc,`user` u ,organization o,organization oo "
			        +" where 1=1 "
			        +" and aa.STUDY_MANAGER_ID = u.USER_ID "
			        +" and aa.OTM_CLASS_ID = oc.OTM_CLASS_ID "
			        +" and oc.BL_CAMPUS_ID = oo.id "
			        +" and u.organizationID = o.id "
			        + sqlwhereOtm
			        +" group by aa.STUDY_MANAGER_ID "                
	                +" ) as a "
	                +" group by a.USER_ID " 
	                +" order by amount desc LIMIT 0," + top;		
						
		}else if("OTHERS".equals(productType)){
			//其他
            //只有一对多
			String sqlwhere = "";
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and occ.COURSE_DATE>='" + startDate+"'";;		
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and occ.COURSE_DATE<='" + endDate+"'";;
			}
			if (blBranch != null) {
				sqlwhere += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}

			sql ="select u.USER_ID ,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(a.COURSE_HOURS) as amount " 
					   +" from "
					   +" (select occ.OTM_CLASS_ID,ocsa.STUDY_MANAGER_ID,occ.COURSE_HOURS from otm_class_student_attendent ocsa,otm_class_course occ"
			           +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
					   +" and ocsa.CHARGE_STATUS ='CHARGED' "
                       +sqlWhere
                       +" GROUP BY ocsa.OTM_CLASS_COURSE_ID) as a, "
			           +"otm_class oc,`user` u ,organization o,organization oo "
			           +" where 1=1 "
			           +" and a.STUDY_MANAGER_ID = u.USER_ID "
			           +" and a.OTM_CLASS_ID = oc.OTM_CLASS_ID "
			           +" and oc.BL_CAMPUS_ID = oo.id "
			           +" and u.organizationID = o.id "
			           + sqlwhere
			           +" group by a.STUDY_MANAGER_ID "
			           +" order by amount desc LIMIT 0," + top;			
			
		}else if("SMALL_CLASS".equals(productType)){
			//小班		
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and mcc.COURSE_DATE>='" + startDate+"'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and mcc.COURSE_DATE<='" + endDate+"'";
			}
			if (blBranch != null) {
				sqlWhere += " and u.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}
			
			sql = "select u.USER_ID ,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
					+ " from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "
					+ " where mcc.STUDY_MANEGER_ID = u.USER_ID "
					+ " and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
					+ " and mc.BL_CAMPUS_ID = oo.id "
					+ " and u.organizationID = o.id "
					+ " and mcc.COURSE_STATUS ='CHARGED' "
					+ sqlWhere
					+ " group by mcc.STUDY_MANEGER_ID "
					+ " order by amount desc LIMIT 0," + top;
			
			
		}else if ("ONE_ON_ONE_COURSE".equals(productType)) {			
			//一对一
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
			}
			if (blBranch != null) {
				sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
						+ blBranch.getOrgLevel() + "%' )";
			}
			
			sql = "select b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
					+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
					+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
					+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
					+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
					+ sqlWhere
					+ " GROUP BY b.USER_ID "
					+ " ORDER BY amount desc LIMIT 0," + top;
			
		}
		
		Boolean cacheFlag = Boolean.valueOf(isCahche);
		List<Map<String, Object>> result =null;
		if(!cacheFlag){
			result = fireSQLAndReturnListInBranchCampus(sql);
			return result;			
		}else{
			//加入缓存  先对sql进行hash
			String key =MD5.getMD5("sql"+sql);			
			try {
				byte[] keybyte =ObjectUtil.objectToBytes(key);
				if(JedisUtil.exists(keybyte)){			
				    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
				    result = (List<Map<String, Object>>) object;
				    return result;
				}else{
					result = fireSQLAndReturnListInBranchCampus(sql);
					JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result), CACHE_EXPIRE_TIME);
					return result;
				}
			} catch (Exception e) {
				System.out.println("ObjectUtil.objectToBytes错了");
				e.printStackTrace();
				throw new ApplicationException(e.getMessage());
			}			
		}
		
		

		


	}

	/**
	 * 教师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getTeacherHoursOrgs(String startDate,
			String endDate, List<Organization> blBranchs) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (blBranchs != null && blBranchs.size()>0) {
			sqlWhere += " and b.organizationID in(select id from organization where"+"  orgLevel like '"
					+ blBranchs.get(0).getOrgLevel() + "%' ";
			if(blBranchs.size()>1){
				for(int index=1;index<blBranchs.size();index++){
					sqlWhere+=" or orgLevel like '"+blBranchs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = " SELECT b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o, organization o2 "
				+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
				+ " and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE'  "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc " ;
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	/**
	 * 学管师课消Top10
	 * 
	 * @param startDate
	 * @param endDate
	 * @param blBranch
	 * @return
	 */
	public List<Map<String, Object>> getStudyManagerHoursOrgs(String startDate,
			String endDate, List<Organization> blBranchs) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (blBranchs != null && blBranchs.size()>0) {
			sqlWhere += " and b.organizationID in(select id from organization where"+"  orgLevel like '"
					+ blBranchs.get(0).getOrgLevel() + "%' ";
			if(blBranchs.size()>1){
				for(int index=1;index<blBranchs.size();index++){
					sqlWhere+=" or orgLevel like '"+blBranchs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = "select b.USER_ID,b.NAME username,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgid,o2.parentID branchid,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
				+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
				+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc" ;
		return fireSQLAndReturnListInBranchCampus(sql);
	}
	
	/**
	 * 辅助函数，按传入的queryString进行查询并且返回结果，按分公司
	 * 
	 * @param sqlQueryString
	 * @return
	 */
	public List<Map<String, String>> fireSQLAndReturnListInBranch(
			String sqlQueryString) {
		Map<String, Object> params = Maps.newHashMap();
		List<Map<Object,Object>> list = userDao.findMapBySql(sqlQueryString,params);
		String orgid = null;
		String orgName = null;
		List<Map<String, String>> result = new ArrayList<>();
		for (Map<Object,Object> tmap : list) {
			Map<String, String> map=(Map)tmap;
			orgid = "";
			orgName = "空值";
			if (map.get("orgid") != null) {
				orgid = map.get("orgid");
				Organization organization = userService
						.getBelongBranchByCampusId(orgid);
				if (organization != null)
					orgName = organization.getName();
				else
					orgName = userService.getBelongGrounpByOrgId(orgid)
							.getName();
			}

			map.put("orgName", orgName);
			result.add(map);
		}
		return result;
	}

	/**
	 * 辅助函数，按传入的queryString进行查询并且返回结果，按 分公司 - 校区
	 * 
	 * @param sqlQueryString
	 * @return
	 */
	public List<Map<String, Object>> fireSQLAndReturnListInBranchCampus(
			String sqlQueryString) {
//		List<Map<String, String>> list = userDao.findMapBySql(sqlQueryString);
		Map<String, Object> params = Maps.newHashMap();
		List<Map<Object,Object>> list = userDao.findMapBySql(sqlQueryString,params);
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<Object,Object> tmap : list) {
			Map<String, Object> map=(Map)tmap;
			if(map.get("user_Id")!=null) {
				map.put("userIdHeadImg", PropertiesUtils.getStringValue("oss.access.url.prefix") + "MOBILE_HEADER_BIG_" + map.get("user_Id") + ".jpg");
			}else if(map.get("USER_ID")!=null){
				map.put("userIdHeadImg", PropertiesUtils.getStringValue("oss.access.url.prefix") + "MOBILE_HEADER_BIG_" + map.get("USER_ID") + ".jpg");
			}
		    result.add(map);
		}
//		String orgId = null;
//		String orgName = null;
//		for (Map<String, String> map : list) {
//			orgId = "";
//			orgName = "未知";
//			if (map.get("orgid") != null) {
//				
//				orgId = map.get("orgid");
//				Organization organization = organizationDao.findById(orgId);
//				if (organization == null) {
//					orgName = "未知 - 未知";
//				} else {
//					//如果当前组织的parent不是null/集团或者分公司，进行升级操作。
//					String parentId = organization.getParentId();
//					Organization parentOrganization = organizationDao
//							.findById(parentId);
//					while (true) {
//						if (parentOrganization == null
//								|| parentOrganization.getOrgType() == OrganizationType.GROUNP
//								|| parentOrganization.getOrgType() == OrganizationType.BRENCH) {
//							if (parentOrganization == null) {
//								orgName = "未知 - " + organization.getName();
//							} else {
//								orgName = parentOrganization.getName() + " - "
//										+ organization.getName();
//							}
//							break;
//						} else {
//							organization = parentOrganization;
//							parentId = organization.getParentId();
//							parentOrganization = organizationDao
//									.findById(parentId);
//						}
//					}
//				}
//			}
//			
//			map.put("orgName", orgName);
//		}
		return result;
	}

	/**
	 * 辅助函数，按传入的queryString进行查询并且返回结果，按校区
	 * 
	 * @param sqlQueryString
	 * @return
	 */
	public List<Map<String, String>> fireSQLAndReturnListInCampus(
			String sqlQueryString) {
		Map<String, Object> params = Maps.newHashMap();
		List<Map<Object,Object>> list = userDao.findMapBySql(sqlQueryString,params);
		String orgid = null;
		String orgName = null;
		List<Map<String, String>> result = new ArrayList<>();
		for (Map<Object, Object> tmap : list) {
			Map<String, String> map=(Map)tmap;
			orgid = "";
			orgName = "空值";
			if (map.get("orgid") != null) {
				orgid = map.get("orgid");

				/**
				 * 校区：人员所在主组织架构在该分公司下，如果是校区下的部门，取校区，如果是分公司的部门或者分公司，取分公司。
				 */
				Organization organization = organizationDao.findById(orgid);
				String parentId = organization.getParentId();// ||(organization.getOrgType()==OrganizationType.OTHER
																// &&
																// parentOrganization.getOrgType()==OrganizationType.CAMPUS)
				Organization parentOrganization = organizationDao
						.findById(parentId);
				if (organization != null) {
					if (organization.getOrgType() == OrganizationType.OTHER
							|| organization.getOrgType() == OrganizationType.DEPARTMENT) {
						if (parentOrganization != null) {
							if (parentOrganization.getOrgType() == OrganizationType.CAMPUS) {
								orgName = parentOrganization.getName();
							} else if (parentOrganization.getOrgType() == OrganizationType.BRENCH) {
								orgName = parentOrganization.getName();
							}
						}
					} else if (organization.getOrgType() == OrganizationType.CAMPUS) {
						orgName = organization.getName();
					} else if (organization.getOrgType() == OrganizationType.BRENCH) {
						orgName = organization.getName();
					} else if (organization.getOrgType() == OrganizationType.GROUNP) {
						orgName = organization.getName();
					}
				}

				// Organization
				// organization=userService.getBelongBrenchByCampusId(orgid);
				// if(organization!=null)
				// orgname=organization.getName();
				// else
				// orgname=userService.getBelongGrounpByOrgId(orgid).getName();
			}

			map.put("orgName", orgName);
			result.add(map);
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public List<Map> getIncomeRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and funds_change_history.TRANSACTION_TIME>= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and funds_change_history.TRANSACTION_TIME<= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and funds_change_history.TRANSACTION_TIME<= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT sum(amount) as value, time FROM ")
				.append("(SELECT SUBSTRING(funds_change_history.TRANSACTION_TIME , 1, 10) as time, funds_change_history.TRANSACTION_AMOUNT as amount  ")
				.append("FROM funds_change_history inner JOIN contract ON funds_change_history.CONTRACT_ID = contract.ID ")
				.append("INNER JOIN student ON contract.STUDENT_ID = student.ID where 1=1")
				.append(sqlWhere).append(") as report GROUP BY time");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		List<Map> returnList = addEmptyDayValue(list, startDate, t_endDate);
		return returnList;
	}

	/**
	 * 按照时间排序， 如果没有值的时候需要插入空值 Map 的 取值， time / value
	 * 
	 * @param list
	 * @param t_endDate
	 *            开始日期
	 * @param startDate
	 *            结束日期
	 */
	private List<Map> addEmptyDayValue(List<Map<Object,Object>> list, String startDate,
			String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		try {
			start.setTime(format.parse(startDate));
			end.setTime(format.parse(endDate));
			end.add(Calendar.DAY_OF_MONTH, 1); // 增加多一天
		} catch (ParseException e) {
			// TODOAuto-generatedcatchblock
			e.printStackTrace();
		}
		List<Map> returnList = new ArrayList<Map>();
		while (start.before(end)) {
			String dateString = format.format(start.getTime());
			HashMap dateMap = new HashMap<String, Integer>();
			dateMap.put("time", dateString);
			dateMap.put("value", 0);
			start.add(Calendar.DAY_OF_MONTH, 1);
			for (Map map : list) {
				if (map.get("time").equals(dateString)) {
					dateMap.put("value", map.get("value"));
					break;
				}
			}
			returnList.add(dateMap);
		}
		return returnList;
	}

	@Override
	public List<Map> getOutcomeRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME>= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME<= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME<= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select time,  sum(amount) as value from ( select substring(PAY_TIME,1,10) as time, product_type, amount  from ACCOUNT_CHARGE_RECORDS, student")
				.append(" where 1=1 and ACCOUNT_CHARGE_RECORDS.STUDENT_ID = student.ID ")
				.append(sqlWhere).append(") as reports group by time");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		List<Map> returnList = addEmptyDayValue(list, startDate, t_endDate);
		return returnList;
	}

	@Override
	public List<List<Map>> getOutcomeIncomeRpData(String startDate,
			String endDate, String orgId) {
		List<Map> income = this.getIncomeRpData(startDate, endDate, orgId);
		List<Map> outcome = this.getOutcomeRpData(startDate, endDate, orgId);
		List<Map> outcomeForOne = this.getOutcomeForOneRpData(startDate,
				endDate, orgId);
		List<Map> outcomeForSmall = this.getOutcomeForSmallRpData(startDate,
				endDate, orgId);
		List<Map> outcomeForOther = this.getOutcomeForOtherRpData(startDate,
				endDate, orgId);

		List<List<Map>> returnList = new ArrayList<List<Map>>();
		returnList.add(income);
		returnList.add(outcome);
		returnList.add(outcomeForOne);
		returnList.add(outcomeForSmall);
		returnList.add(outcomeForOther);
		return returnList;
	}

	@Override
	public List<Map> getOutcomeForSmallRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME>= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME<= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME<= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select time, product_type as type, sum(amount) as value from ( select substring(PAY_TIME,1,10) as time, product_type, amount  from ACCOUNT_CHARGE_RECORDS, student")
				.append(" where 1=1 and ACCOUNT_CHARGE_RECORDS.STUDENT_ID = student.ID and product_type = 'SMALL_CLASS' ")
				.append(sqlWhere).append(") as reports group by time");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		List<Map> returnList = addEmptyDayValue(list, startDate, t_endDate);
		return returnList;
	}

	@Override
	public List<Map> getOutcomeForOtherRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME >='"
						+ startDate + "'";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME <= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME <= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select time, product_type as type, sum(amount) as value from ( select substring(PAY_TIME,1,10) as time, product_type, amount  from ACCOUNT_CHARGE_RECORDS, student")
				.append(" where 1=1 and ACCOUNT_CHARGE_RECORDS.STUDENT_ID = student.ID and product_type = 'OTHERS' ")
				.append(sqlWhere).append(") as reports group by time");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		List<Map> returnList = addEmptyDayValue(list, startDate, t_endDate);
		return returnList;
	}

	@Override
	public List<Map> getOutcomeForOneRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME >= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and ACCOUNT_CHARGE_RECORDS.PAY_TIME <= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and  ACCOUNT_CHARGE_RECORDS.PAY_TIME <= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select time, product_type as type, sum(amount) as value from ( select substring(PAY_TIME,1,10) as time, product_type, amount  from ACCOUNT_CHARGE_RECORDS, student")
				.append(" where 1=1 and ACCOUNT_CHARGE_RECORDS.STUDENT_ID = student.ID and product_type = 'ONE_ON_ONE_COURSE_NORMAL' ")
				.append(sqlWhere).append(") as reports group by time");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		List<Map> returnList = addEmptyDayValue(list, startDate, t_endDate);
		return returnList;
	}

	@Override
	public List getStudentBySubjectRpData(String startDate, String endDate,
			String orgId) {
		List oneStuList = this.getOneStuBySubjectRpData(startDate, endDate,
				orgId);
		List smallStuList = this.getSmallStuBySubjectRpData(startDate, endDate,
				orgId);

		List returnList = new ArrayList();
		returnList.add(oneStuList);
		returnList.add(smallStuList);
		return returnList;

	}

	// 获取小班 科目分布
	private List getSmallStuBySubjectRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and mini_class_course.COURSE_DATE >= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and mini_class_course.COURSE_DATE <= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and  mini_class_course.COURSE_DATE <= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and mini_class.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select count(1) as stuNumber, name as subjectName, SUBJECT as subjectId from ( SELECT DISTINCT mini_class_student.STUDENT_ID,")
				.append(" mini_class.`SUBJECT`,  data_dict.NAME FROM mini_class_student LEFT JOIN mini_class ON mini_class_student.MINI_CLASS_ID = mini_class.MINI_CLASS_ID")
				.append(" LEFT JOIN data_dict ON data_dict.ID = mini_class.`SUBJECT` LEFT JOIN student ON mini_class_student.STUDENT_ID = student.ID")
				.append(" LEFT JOIN mini_class_course ON mini_class_student.MINI_CLASS_ID = mini_class_course.MINI_CLASS_ID WHERE data_dict.CATEGORY = 'SUBJECT' ")
				.append(sqlWhere).append(" ) as reports group by NAME");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		return list;
	}

	// 获取一对一的科目分布
	private List getOneStuBySubjectRpData(String startDate, String endDate,
			String orgId) {
		String sqlWhere = "";
		String t_endDate = "";
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotEmpty(startDate)
				|| StringUtils.isNotEmpty(endDate)
				|| StringUtils.isNotEmpty(endDate)) {
			if (StringUtils.isNotEmpty(startDate)) {
				sqlWhere += " and course.COURSE_DATE >= :startDate ";
				params.put("startDate", startDate);
			} else {
				throw new ApplicationException(ErrorCode.EMPTY_START_DATE);
			}
			if (StringUtils.isNotEmpty(endDate)) {
				sqlWhere += " and course.COURSE_DATE <= :endDate ";
				params.put("endDate", endDate+" 23:59:59");
				t_endDate = endDate;
			} else {
				sqlWhere += " and  course.COURSE_DATE <= :endDate ";
				params.put("endDate", DateTools.getCurrentDate()+" 23:59:59");
				t_endDate = DateTools.getCurrentDate();
			}
			if (StringUtils.isNotEmpty(orgId)) {
				sqlWhere += " and student.BL_CAMPUS_ID = :orgId ";
				params.put("orgId", orgId);
			}
		}
		StringBuffer sql = new StringBuffer();
		sql.append(
				"select count(DISTINCT student_id) as stuNumber, subject as subjectId, data_dict.name as subjectName from course, data_dict, organization, student")
				.append(" where  subject = data_dict.id and COURSE_STATUS != 'NEW' ")
				.append(" and student.BL_CAMPUS_ID = organization.id and course.student_id =  student.id ")
				.append(sqlWhere).append(" group by subject ");
		List<Map<Object,Object>> list = userDao.findMapBySql(sql.toString(),params);
		return list;
	}

	/**
	 * 首页营收
	 * 
	 * @param dp
	 * @return
	 */
	public DataPackage getContractRevenue(DataPackage dp) {
		// BasicOperationQueryVo basicOperationQueryVo =new
		// BasicOperationQueryVo();
		// basicOperationQueryVo.setStartDate(DateTools.getCurrentDate());
		// basicOperationQueryVo.setEndDate(DateTools.addDateToString(DateTools.getCurrentDate(),
		// 1));
		// List<Map> currentDayMap=
		// reportDao.getContractRevenue(basicOperationQueryVo);
		// basicOperationQueryVo.setStartDate(DateTools.getCurrentMonthFirstDate());
		// basicOperationQueryVo.setEndDate(DateTools.getNextMonthFirstDate());
		// List<Map> currentMonthMap=
		// reportDao.getContractRevenue(basicOperationQueryVo);

		return dp;

	}

	@Override
	public List<Map<String, Object>> getSmallClassTeacherHours(String startDate, String endDate, Organization org) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and mcc.COURSE_DATE>='" + startDate + "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and mcc.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhere += " and o.orgLevel like '"
					+ org.getOrgLevel() + "%' ";
		}
		String sql = "select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
				+ " from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo  "
				+ " where mcc.TEACHER_ID = u.USER_ID "
				+ " and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+ " and mc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and mcc.COURSE_STATUS ='CHARGED' "
				+ sqlWhere
				+ " group by mcc.TEACHER_ID "
				+ " order by amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result), CACHE_EXPIRE_TIME);
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
		
	}

	@Override
	public List<Map<String, Object>> getSmallClassTeacherHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {		
		//orgs各个分公司的org
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and mcc.COURSE_DATE>='" + startDate+"'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and mcc.COURSE_DATE<='" + endDate+"'";
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlWhere += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhere+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = "select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
				+ " from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo  "
				+ " where mcc.TEACHER_ID = u.USER_ID "
				+ " and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+ " and mc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and mcc.COURSE_STATUS ='CHARGED' "
				+ sqlWhere
				+ " group by mcc.TEACHER_ID "
				+ " order by amount desc ";
		
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getSmallClassStudyManagerHours(String startDate, String endDate,
			Organization org) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and mcc.COURSE_DATE>='" + startDate + "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and mcc.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhere += " and o.orgLevel like '"
					+ org.getOrgLevel() + "%' ";
		}
		String sql = "select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
				+ " from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "
				+ " where mcc.STUDY_MANEGER_ID = u.USER_ID "
				+ " and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+ " and mc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and mcc.COURSE_STATUS ='CHARGED' "
				+ sqlWhere
				+ " group by mcc.STUDY_MANEGER_ID "
				+ " order by amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result), CACHE_EXPIRE_TIME);
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
		
	}

	@Override
	public List<Map<String, Object>> getSmallClassStudyManagerHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		//orgs各个分公司的org
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and mcc.COURSE_DATE>='" + startDate+"'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and mcc.COURSE_DATE<='" + endDate+"'";
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlWhere += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhere+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = "select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount "
				+ " from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "
				+ " where mcc.STUDY_MANEGER_ID = u.USER_ID "
				+ " and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+ " and mc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and mcc.COURSE_STATUS ='CHARGED' "
				+ sqlWhere
				+ " group by mcc.STUDY_MANEGER_ID "
				+ " order by amount desc ";
		
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getOtherTeacherHours(String startDate, String endDate, Organization org) {
		
		//查询一对多和双师两者的课时加和的 Top10
		String sqlWhereOtm = "";
		String sqlWhereTwo = "";
		
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate + "'";
			sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate + "'";			
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
			sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhereOtm += " and o.orgLevel like '"
					+ org.getOrgLevel() + "%' ";
			sqlWhereTwo += " and o.orgLevel like '"
					+ org.getOrgLevel() + "%' ";
		}
		
		String sql = "select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ("
				+ " select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount "
				+ " from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
				+ " where occ.TEACHER_ID = u.USER_ID "
				+ " and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
				+ " and oc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and occ.COURSE_STATUS ='CHARGED' "
				+ sqlWhereOtm
				+ "group by occ.TEACHER_ID "
				+ " UNION ALL "
				+ "select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
				+ " from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttc.CLASS_ID = ttb.CLASS_ID "
                +" and ttc.TEACHER_ID = u.USER_ID "
                +" and u.organizationID = o.id "
                +" and ttcc.COURSE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttc.TEACHER_ID "
                +" UNION ALL "
                +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
                +" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttct.CLASS_ID = ttc.CLASS_ID "
                +" and ttct.TEACHER_ID = u.USER_ID "
                +" and ttct.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                +" and ttcc.COURSE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttct.TEACHER_ID "
                +" ) as a "
                +" group by a.userId "
                +" order by amount desc LIMIT 0," + top;
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> getOtherTeacherHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		
		String sqlWhereOtm = "";
		String sqlWhereTwo = "";
		
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
			sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate+"'";			
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate+"'";
			sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate+"'";
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlWhereOtm += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlWhereTwo += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhereOtm+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlWhereTwo+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhereOtm+=")";
			sqlWhereTwo+=")";
		}
		String sql = "select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ("
				+ " select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount "
				+ " from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
				+ " where occ.TEACHER_ID = u.USER_ID "
				+ " and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
				+ " and oc.BL_CAMPUS_ID = oo.id "
				+ " and u.organizationID = o.id "
				+ " and occ.COURSE_STATUS ='CHARGED' "
				+ sqlWhereOtm
				+ "group by occ.TEACHER_ID "
				+ " UNION ALL "
				+ "select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
				+ " from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttc.CLASS_ID = ttb.CLASS_ID "
                +" and ttc.TEACHER_ID = u.USER_ID "
                +" and u.organizationID = o.id "
                +" and ttcc.COURSE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttc.TEACHER_ID "
                +" UNION ALL "              
                +" select  userId, userName, orgId, branchId, orgName,sum(b.amount) as amount from("
                +" select DISTINCT u.USER_ID as userId,u.`NAME` as userName,oo.id,ttct.CLASS_TWO_ID,ttcc.COURSE_ID "
                +" as orgId,oo.parentID as branchId,o.`name` as orgName,ttcc.COURSE_HOURS as amount "
                +" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo ,two_teacher_class_student_attendent ttcsa"
                +" "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttcsa.TWO_CLASS_COURSE_ID=ttcc.COURSE_ID and ttcsa.CLASS_TWO_ID=ttct.CLASS_TWO_ID"
                +" and ttct.CLASS_ID = ttc.CLASS_ID "
                +" and ttct.TEACHER_ID = u.USER_ID "
                +" and ttct.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                +" and ttcsa.CHARGE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttct.CLASS_TWO_ID,ttcc.COURSE_ID) b group by b.userId "         
                +" ) as a "
                +" group by a.userId "
                +" order by amount desc ";
		
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getOtherStudyManagerHours(String startDate, String endDate, Organization org) {
		//只有一对多有学管师 双师没有		
		String sqlWhereOtm = "";
		
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate + "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhereOtm += " and o.orgLevel like '"
					+ org.getOrgLevel() + "%' ";			
		}
		
		String sql ="select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount " 
		           +" from otm_class_student_attendent ocsa,otm_class_course occ,otm_class oc,`user` u ,organization o,organization oo "
		           +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
		           +" and ocsa.STUDY_MANAGER_ID = u.USER_ID "
		           +" and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
		           +" and oc.BL_CAMPUS_ID = oo.id "
		           +" and u.organizationID = o.id "
		           +" and ocsa.CHARGE_STATUS ='CHARGED' "
		           + sqlWhereOtm
		           +" group by ocsa.STUDY_MANAGER_ID "
		           +" order by amount desc LIMIT 0," + top;
	
		// 加入缓存 先对sql进行hash
		String key = MD5.getMD5("sql" + sql);
		List<Map<String, Object>> result = null;
		try {
			byte[] keybyte = ObjectUtil.objectToBytes(key);
			if (JedisUtil.exists(keybyte)) {
				Object object = ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
				result = (List<Map<String, Object>>) object;
				return result;
			} else {
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}

	}

	@Override
	public List<Map<String, Object>> getOtherStudyManagerHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		//只有一对多有学管师 双师没有
		String sqlWhereOtm = "";
		String sqlwhere ="";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";;		
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate+"'";;
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlwhere += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlwhere+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlwhere+=")";
		}
//		String sql ="select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount " 
//		           +" from otm_class_student_attendent ocsa,otm_class_course occ,otm_class oc,`user` u ,organization o,organization oo "
//		           +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
//		           +" and ocsa.STUDY_MANAGER_ID = u.USER_ID "
//		           +" and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
//		           +" and oc.BL_CAMPUS_ID = oo.id "
//		           +" and u.organizationID = o.id "
//		           +" and ocsa.CHARGE_STATUS ='CHARGED' "
//		           + sqlWhereOtm
//		           +" group by ocsa.STUDY_MANAGER_ID "
//		           +" order by amount desc ";
		
		 

		 String sql ="select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(a.COURSE_HOURS) as amount "
				   +" from "
				   +" (select occ.OTM_CLASS_ID,ocsa.STUDY_MANAGER_ID,occ.COURSE_HOURS from otm_class_student_attendent ocsa,otm_class_course occ"
		           +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
				   +" and ocsa.CHARGE_STATUS ='CHARGED' "
                   +sqlWhereOtm
                   +" GROUP BY ocsa.OTM_CLASS_COURSE_ID) as a, "
		           +"otm_class oc,`user` u ,organization o,organization oo "
		           +" where 1=1 "
		           +" and a.STUDY_MANAGER_ID = u.USER_ID "
		           +" and a.OTM_CLASS_ID = oc.OTM_CLASS_ID "
		           +" and oc.BL_CAMPUS_ID = oo.id "
		           +" and u.organizationID = o.id "
		           + sqlwhere
		           +" group by a.STUDY_MANAGER_ID "
		           +" order by amount desc ";
		
		
		
		
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getTotalTeacherHours(String startDate, String endDate, Organization org) {
		//查询一对多和双师两者的老师课时加和的 Top10
		String sqlWhereOne= "";
		String sqlWhereSmall="";
		String sqlWhereOtm = "";
		String sqlWhereTwo = "";
		

		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOne += " and c.COURSE_DATE>='" + startDate + "'";
			sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate + "'";
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate + "'";
			sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate + "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOne += " and c.COURSE_DATE<='" + endDate + "'";
			sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate + "'";
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
			sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhereOne += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
			sqlWhereSmall += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
			sqlWhereOtm += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
			sqlWhereTwo += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
		}
		
		
		String sql =" select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ( "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(c.REAL_HOURS) as amount " 
				+" from course c,`user` u,organization o,organization oo "  
				+" where c.TEACHER_ID = u.USER_ID "
				+" and c.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and c.COURSE_STATUS ='CHARGED' "
				+sqlWhereOne
				+" group by c.TEACHER_ID "
				+" UNION ALL "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount " 
				+" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo " 
				+" where mcc.TEACHER_ID = u.USER_ID "
				+" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+" and mc.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and mcc.COURSE_STATUS ='CHARGED' " 
				+sqlWhereSmall
				+" group by mcc.TEACHER_ID "
				+" UNION ALL "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount " 
				+" from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
				+" where occ.TEACHER_ID = u.USER_ID "
				+" and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
				+" and oc.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and occ.COURSE_STATUS ='CHARGED' "
				+sqlWhereOtm
				+" group by occ.TEACHER_ID "
				+" UNION ALL "
				+" select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount " 
				+" from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb " 
				+" where ttcc.CLASS_ID = ttc.CLASS_ID "
				+" and ttc.CLASS_ID = ttb.CLASS_ID "
				+" and ttc.TEACHER_ID = u.USER_ID "
				+" and u.organizationID = o.id "
				+" and ttcc.COURSE_STATUS ='CHARGED' " 
				+sqlWhereTwo
				+" group by ttc.TEACHER_ID "
				+" UNION ALL "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount " 
				+" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo "
				+" where ttcc.CLASS_ID = ttc.CLASS_ID "
				+" and ttct.CLASS_ID = ttc.CLASS_ID "
				+" and ttct.TEACHER_ID = u.USER_ID "
				+" and ttct.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and ttcc.COURSE_STATUS ='CHARGED' "
				+sqlWhereTwo
				+" group by ttct.TEACHER_ID "
				+" ) as a "
				+" group by a.userId " 
				+" order by amount desc limit 0, "+top; 
		
		// 加入缓存 先对sql进行hash
		String key = MD5.getMD5("sql" + sql);
		List<Map<String, Object>> result = null;
		try {
			byte[] keybyte = ObjectUtil.objectToBytes(key);
			if (JedisUtil.exists(keybyte)) {
				Object object = ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
				result = (List<Map<String, Object>>) object;
				return result;
			} else {
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> getTotalTeacherHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		//查询一对多和双师两者的老师课时加和的 Top10
		String sqlWhereOne= "";
		String sqlWhereSmall="";
		String sqlWhereOtm = "";
		String sqlWhereTwo = "";
		

		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOne += " and c.COURSE_DATE>='" + startDate+"'";
			sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate+"'";
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
			sqlWhereTwo += " and ttcc.COURSE_DATE>='" + startDate+"'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOne += " and c.COURSE_DATE<='" + endDate + "'";
			sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate + "'";
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
			sqlWhereTwo += " and ttcc.COURSE_DATE<='" + endDate + "'";
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlWhereOne += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlWhereSmall += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlWhereOtm += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlWhereTwo += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhereOne+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlWhereSmall+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlWhereOtm+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlWhereTwo+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhereOne+=")";
			sqlWhereSmall+=")";
			sqlWhereOtm+=")";
			sqlWhereTwo+=")";
		}
			
		String sql =" select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ( "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(c.REAL_HOURS) as amount " 
				+" from course c,`user` u,organization o,organization oo "  
				+" where c.TEACHER_ID = u.USER_ID "
				+" and c.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and c.COURSE_STATUS ='CHARGED' "
				+sqlWhereOne
				+" group by c.TEACHER_ID "
				+" UNION ALL "
				+" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount " 
				+" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo " 
				+" where mcc.TEACHER_ID = u.USER_ID "
				+" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
				+" and mc.BL_CAMPUS_ID = oo.id "
				+" and u.organizationID = o.id "
				+" and mcc.COURSE_STATUS ='CHARGED' " 
				+sqlWhereSmall
				+" group by mcc.TEACHER_ID "
				+" UNION ALL "
                + " select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount "
	         	+ " from otm_class_course occ,otm_class oc,`user` u,organization o,organization oo "
	         	+ " where occ.TEACHER_ID = u.USER_ID "
	         	+ " and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
	        	+ " and oc.BL_CAMPUS_ID = oo.id "
		        + " and u.organizationID = o.id "
		        + " and occ.COURSE_STATUS ='CHARGED' "
		        + sqlWhereOtm
		        + "group by occ.TEACHER_ID "
		        + " UNION ALL "
		        + "select u.USER_ID as userId,u.`NAME` as userName,o.id as orgId,ttb.BRENCH_ID as branchId,o.`name` as orgName,sum(ttcc.COURSE_HOURS) as amount "
		        + " from two_teacher_class_course ttcc,two_teacher_class ttc,`user` u ,organization o,two_teacher_brench ttb "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttc.CLASS_ID = ttb.CLASS_ID "
                +" and ttc.TEACHER_ID = u.USER_ID "
                +" and u.organizationID = o.id "
                +" and ttcc.COURSE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttc.TEACHER_ID "
                +" UNION ALL "
                +" select  userId, userName, orgId, branchId, orgName,sum(b.amount) as amount from("
                +" select DISTINCT u.USER_ID as userId,u.`NAME` as userName,oo.id,ttct.CLASS_TWO_ID,ttcc.COURSE_ID "
                +" as orgId,oo.parentID as branchId,o.`name` as orgName,ttcc.COURSE_HOURS as amount "
                +" from two_teacher_class_course ttcc,two_teacher_class ttc,two_teacher_class_two ttct,`user` u ,organization o,organization oo ,two_teacher_class_student_attendent ttcsa"
                +" "
                +" where ttcc.CLASS_ID = ttc.CLASS_ID "
                +" and ttcsa.TWO_CLASS_COURSE_ID=ttcc.COURSE_ID and ttcsa.CLASS_TWO_ID=ttct.CLASS_TWO_ID"
                +" and ttct.CLASS_ID = ttc.CLASS_ID "
                +" and ttct.TEACHER_ID = u.USER_ID "
                +" and ttct.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                +" and ttcsa.CHARGE_STATUS ='CHARGED' "
                +sqlWhereTwo
                +" group by ttct.CLASS_TWO_ID,ttcc.COURSE_ID) b group by b.userId "
                +" ) as a "
                +" group by a.userId "
                +" order by amount desc ";


		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getTotalStudyManagerHours(String startDate, String endDate, Organization org) {
		//查询一对多和双师两者的学管师课时加和的 Top10
		String sqlWhereOne= "";
		String sqlWhereSmall="";
		String sqlWhereOtm = "";
		

		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOne += " and c.COURSE_DATE>='" + startDate + "'";
			sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate + "'";
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate + "'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOne += " and c.COURSE_DATE<='" + endDate + "'";
			sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate + "'";
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate + "'";
		}
		if (org != null) {
			sqlWhereOne += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
			sqlWhereSmall += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
			sqlWhereOtm += " and o.orgLevel like '" + org.getOrgLevel() + "%' ";
		}
		
		
		String sql ="select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ( "
                  +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(c.REAL_HOURS) as amount " 
                  +" from course c,`user` u,organization o,organization oo "  
                  +" where c.STUDY_MANAGER_ID = u.USER_ID "
                  +" and c.BL_CAMPUS_ID = oo.id "
                  +" and u.organizationID = o.id "
                  +" and c.COURSE_STATUS ='CHARGED' " 
                  +sqlWhereOne
                  +" group by c.STUDY_MANAGER_ID "
                  +" UNION ALL "
                  +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount " 
                  +" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "  
                  +" where mcc.STUDY_MANEGER_ID = u.USER_ID "
                  +" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
                  +" and mc.BL_CAMPUS_ID = oo.id "
                  +" and u.organizationID = o.id "
                  +" and mcc.COURSE_STATUS ='CHARGED' " 
                  +sqlWhereSmall
                  +" group by mcc.STUDY_MANEGER_ID "
                  +" UNION ALL "
                  +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(occ.COURSE_HOURS) as amount " 
                  +" from otm_class_student_attendent ocsa,otm_class_course occ,otm_class oc,`user` u ,organization o,organization oo " 
                  +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
                  +" and ocsa.STUDY_MANAGER_ID = u.USER_ID " 
                  +" and occ.OTM_CLASS_ID = oc.OTM_CLASS_ID "
                  +" and oc.BL_CAMPUS_ID = oo.id "
                  +" and u.organizationID = o.id "
                  +" and ocsa.CHARGE_STATUS ='CHARGED' " 
                  +sqlWhereOtm
                  +" group by ocsa.STUDY_MANAGER_ID " 
                  +" ) as a "
                  +" group by a.userId " 
                  +" order by amount desc limit 0,"+top;
		
		// 加入缓存 先对sql进行hash
		String key = MD5.getMD5("sql" + sql);
		List<Map<String, Object>> result = null;
		try {
			byte[] keybyte = ObjectUtil.objectToBytes(key);
			if (JedisUtil.exists(keybyte)) {
				Object object = ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
				result = (List<Map<String, Object>>) object;
				return result;
			} else {
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
	}

	@Override
	public List<Map<String, Object>> getTotalStudyManagerHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		//查询一对多和双师两者的老师课时加和的 Top10
		String sqlWhereOne= "";
		String sqlWhereSmall="";
		String sqlWhereOtm = "";
		String sqlwhereOtm ="";

		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhereOne += " and c.COURSE_DATE>='" + startDate+"'";
			sqlWhereSmall += " and mcc.COURSE_DATE>='" + startDate+"'";
			sqlWhereOtm += " and occ.COURSE_DATE>='" + startDate+"'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhereOne += " and c.COURSE_DATE<='" + endDate+"'";
			sqlWhereSmall += " and mcc.COURSE_DATE<='" + endDate+"'";
			sqlWhereOtm += " and occ.COURSE_DATE<='" + endDate+"'";
		}
		
		if (orgs != null && orgs.size()>0) {
			sqlWhereOne += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlWhereSmall += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			sqlwhereOtm += " and ( o.orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhereOne+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlWhereSmall+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
					sqlwhereOtm+=" or o.orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhereOne+=")";
			sqlWhereSmall+=")";
			sqlwhereOtm+=")";
		}
		String sql ="select a.userId,a.userName,a.orgId,a.branchId,a.orgName,sum(a.amount) as amount from ( "
                +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(c.REAL_HOURS) as amount " 
                +" from course c,`user` u,organization o,organization oo "  
                +" where c.STUDY_MANAGER_ID = u.USER_ID "
                +" and c.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                +" and c.COURSE_STATUS ='CHARGED' " 
                +sqlWhereOne
                +" group by c.STUDY_MANAGER_ID "
                +" UNION ALL "
                +" select u.USER_ID as userId,u.`NAME` as userName,oo.id as orgId,oo.parentID as branchId,o.`name` as orgName,sum(mcc.COURSE_HOURS) as amount " 
                +" from mini_class_course mcc,mini_class mc,`user` u,organization o,organization oo "  
                +" where mcc.STUDY_MANEGER_ID = u.USER_ID "
                +" and mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID "
                +" and mc.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                +" and mcc.COURSE_STATUS ='CHARGED' " 
                +sqlWhereSmall
                +" group by mcc.STUDY_MANEGER_ID "
                +" UNION ALL "	
                +" select u.USER_ID as userId,u.`NAME` as username,oo.id as orgid,oo.parentID as branchid,o.`name` as orgName,sum(aa.COURSE_HOURS) as amount "
		        +" from "
		        +" (select occ.OTM_CLASS_ID,ocsa.STUDY_MANAGER_ID,occ.COURSE_HOURS from otm_class_student_attendent ocsa,otm_class_course occ"
                +" where ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
		        +" and ocsa.CHARGE_STATUS ='CHARGED' "
                +sqlWhereOtm
                +" GROUP BY ocsa.OTM_CLASS_COURSE_ID) as aa, "
                +" otm_class oc,`user` u ,organization o,organization oo "
                +" where 1=1 "
                +" and aa.STUDY_MANAGER_ID = u.USER_ID "
                +" and aa.OTM_CLASS_ID = oc.OTM_CLASS_ID "
                +" and oc.BL_CAMPUS_ID = oo.id "
                +" and u.organizationID = o.id "
                + sqlwhereOtm
                +" group by aa.STUDY_MANAGER_ID "                
                +" ) as a "
                +" group by a.userId "
                +" order by amount desc ";
		
		
		return fireSQLAndReturnListInBranchCampus(sql);
		
	}

	@Override
	public List<Map<String, Object>> getTop10CourseHoursByType(String startDate, String endDate, Boolean isQueryAll,
			String productType, String type) {
		Organization blBranch = null;
		if (isQueryAll != null && !isQueryAll) {
			blBranch = userService.getBelongBranch();
		}
//		if (StringUtils.isNotEmpty(endDate)) {
//			endDate = DateTools.addDateToString(endDate, 1);
//		}
		if(type==null){
			return null;
		}
		
		if(type.equals("teacher")){
			return getTeacherHoursByProductType(startDate, endDate, blBranch, productType);
		}else if (type.equals("studyManager")){
			return getStudyManagerHoursByProductType(startDate, endDate, blBranch, productType);
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getOneTeacherHours(String startDate, String endDate, Organization org) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (org != null) {
			sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
					+ org.getOrgLevel() + "%' )";
		}
		String sql = " SELECT b.USER_ID as userId,b.NAME as userName,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgId,o2.parentID branchId,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o, organization o2 "
				+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ " and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE'  "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME );
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}

	}

	@Override
	public List<Map<String, Object>> getOneStudyManagerHours(String startDate, String endDate, Organization org) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (org!= null) {
			sqlWhere += " and b.organizationID in(select id from organization where  orgLevel like '"
					+ org.getOrgLevel() + "%' )";
		}
		String sql = "select b.USER_ID as userId,b.NAME userName,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgId,o2.parentID branchId,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
				+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
				+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc LIMIT 0," + top;
		
		//加入缓存  先对sql进行hash
		String key =MD5.getMD5("sql"+sql);
		List<Map<String, Object>> result =null;
		try {
			byte[] keybyte =ObjectUtil.objectToBytes(key);
			if(JedisUtil.exists(keybyte)){			
			    Object object=	ObjectUtil.bytesToObject(JedisUtil.get(keybyte));
			    result = (List<Map<String, Object>>) object;
			    return result;
			}else{
				result = fireSQLAndReturnListInBranchCampus(sql);
				JedisUtil.set(keybyte, ObjectUtil.objectToBytes(result),CACHE_EXPIRE_TIME);
				return result;
			}
		} catch (Exception e) {
			System.out.println("ObjectUtil.objectToBytes错了");
			e.printStackTrace();
			throw new ApplicationException(e.getMessage());
		}
		


	}

	@Override
	public List<Map<String, Object>> getOneTeacherHoursOrgs(String startDate, String endDate, List<Organization> orgs) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (orgs != null && orgs.size()>0) {
			sqlWhere += " and b.organizationID in(select id from organization where"+"  orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhere+=" or orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = " SELECT b.USER_ID as userId,b.NAME userName,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgId,o2.parentID branchId,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o, organization o2 "
				+ " WHERE a.TEACHER_ID = b.USER_ID  and b.organizationID=o.id and o2.id=a.BL_CAMPUS_ID and a.CHARGE_TYPE='NORMAL' "
				+ " and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE'  "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc " ;
		return fireSQLAndReturnListInBranchCampus(sql);
	}

	@Override
	public List<Map<String, Object>> getOneStudyManagerHoursOrgs(String startDate, String endDate,
			List<Organization> orgs) {
		String sqlWhere = "";
		if (StringUtils.isNotEmpty(startDate)) {
			sqlWhere += " and a.TRANSACTION_TIME>='" + startDate + "  00:00:00'";
		}
		if (StringUtils.isNotEmpty(endDate)) {
			sqlWhere += " and a.TRANSACTION_TIME<'" + endDate + "  23:59:59'";
		}
		if (orgs != null && orgs.size()>0) {
			sqlWhere += " and b.organizationID in(select id from organization where"+"  orgLevel like '"
					+ orgs.get(0).getOrgLevel() + "%' ";
			if(orgs.size()>1){
				for(int index=1;index<orgs.size();index++){
					sqlWhere+=" or orgLevel like '"+orgs.get(index).getOrgLevel()+"%' ";
				}	
			}
			sqlWhere+=")";
		}
		String sql = "select b.USER_ID as userId,b.NAME userName,SUM(a.QUANTITY) amount,a.BL_CAMPUS_ID orgId,o2.parentID branchId,o.name AS orgName "
				+ " FROM account_charge_records a, user b, organization o,organization o2,course c"
				+ " WHERE c.STUDY_MANAGER_ID = b.USER_ID and b.organizationID=o.id and a.COURSE_ID=c.COURSE_ID and o2.id=a.BL_CAMPUS_ID"
				+ " and a.CHARGE_TYPE='NORMAL' and a.PRODUCT_TYPE='ONE_ON_ONE_COURSE' "
				+ " AND a.IS_WASHED = 'FALSE' AND a.CHARGE_PAY_TYPE='CHARGE' "
				+ sqlWhere
				+ " GROUP BY b.USER_ID "
				+ " ORDER BY amount desc" ;
		return fireSQLAndReturnListInBranchCampus(sql);
	}

}
