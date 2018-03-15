package com.eduboss.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.common.EvidenceAuditStatus;
import com.eduboss.dao.OdsMonthCampusAchievementMainStudentDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.domain.OdsMonthCampusAchievementMainStudent;
import com.eduboss.domain.Organization;
import com.eduboss.service.UserService;
import com.google.common.collect.Maps;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class OdsMonthCampusAchievementMainStudentDaoImpl extends GenericDaoImpl<OdsMonthCampusAchievementMainStudent, String> implements OdsMonthCampusAchievementMainStudentDao {


	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private UserService userService;
	
	@Override
	public List<OdsMonthCampusAchievementMainStudent> findInfoByMonth(String yearMonth,String campusId) {
		StringBuffer hql=new StringBuffer();
		hql.append(" from OdsMonthCampusAchievementMainStudent where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if(StringUtils.isNotBlank(yearMonth)){
			hql.append(" and receiptMonth= :yearMonth ");
			params.put("yearMonth", yearMonth);
		}

		if(StringUtils.isNotBlank(campusId)){
			Organization org=organizationDao.findById(campusId);
			hql.append(" and (campus.id = :campusId ");
			params.put("campusId", campusId);
			if(org!=null){
				hql.append(" or bonusStaffOrg.id in (select id from Organization where orgLevel like :orgLevel )");
				params.put("orgLevel", org.getOrgLevel()+"%");
			}
			hql.append(" )");
		}
		return 	this.findAllByHQL(hql.toString(),params);
	}

	@Override
	public void updateRecieptMainStudentAuditStatus(String receiptMonth, String campusId,
			EvidenceAuditStatus nextStatus) {
		// TODO Auto-generated method stub
		Map<String, Object> params = Maps.newHashMap();
		params.put("auditStatus", nextStatus.getValue());
		params.put("campusId", campusId);
		params.put("countDate", receiptMonth);
		String sql = " UPDATE ods_month_campus_achievement_main_student SET receipt_status = :auditStatus WHERE campus_id = :campusId AND receipt_month = :countDate ";
		super.excuteSql(sql,params);
	}
}
