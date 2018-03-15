package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import com.eduboss.exception.ApplicationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.CourseStatus;
import com.eduboss.common.MonitorSubject;
import com.eduboss.common.MonitorSubjectOfTeacherNotAttCourseHour;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.CountUserOperationDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.CountUserOperation;
import com.eduboss.domain.Organization;
import com.eduboss.domainVo.CountUserOperationVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.google.common.collect.Maps;



@Repository
public class CountUserOperationDaoImpl extends GenericDaoImpl<CountUserOperation, String> implements
		CountUserOperationDao {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	/**
	 * 获取前台录入资源统计数据
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @throws Exception 
	 */
	@Override
	public DataPackage getCountUserOperation(CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception {
		
		if (null == countUserOperationVo.getMonitorSubject()) {
			throw new ApplicationException("查询函数没有设置统计类型，请检查！");
		}
		
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select a.ID, a.USER_ID, b.NAME USER_NAME, '"+countUserOperationVo.getMonitorSubject().getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" , sum(a.COUNT_QUANTITY) COUNT_QUANTITY ");
		sql.append(" from `COUNT_USER_OPERATION` a ");
		sql.append(" inner join user b on a.USER_ID = b.USER_ID ");
		sql.append(" where 1=1 ");
		sql.append(" and MONITOR_SUBJECT = '"+countUserOperationVo.getMonitorSubject()+"' ");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
			sql.append(" and b.organizationID = :blCampusId ") ;
			params.put("blCampusId", countUserOperationVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getTeacherId())) {
			sql.append(" and a.USER_ID = :teacherId ") ;
			params.put("teacherId", countUserOperationVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getStartDate())) {
			sql.append(" and COUNT_DATE >= :startDate ") ;
			params.put("startDate", countUserOperationVo.getStartDate());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getEndDate())) {
			sql.append(" and COUNT_DATE <= :endDate ") ;
			params.put("endDate", countUserOperationVo.getEndDate());
		}
		Organization campus = userService.getBelongCampus();
		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
			sql.append(" and b.organizationID = :organizationID ");
			params.put("organizationID", campus.getId());
		}
		
		sql.append(" GROUP BY a.USER_ID ");
		sql.append(" ORDER BY a.COUNT_QUANTITY DESC ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取学管所带学生、绑卡、绑指纹数量	(实时数据)
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getRealTimeStudyManagerWithCardAndFingerprintAndStudent(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" SELECT a.USER_ID ID, a.USER_ID, a. NAME USER_NAME, '" + MonitorSubject.STUDY_MANAGER_WITH_CARD_AND_FINGERPRINT_AND_STUDENT.getName() + "' MONITOR_SUBJECT ");
		sql.append(" ,(SELECT COUNT(*) FROM student WHERE STUDY_MANEGER_ID = a.USER_ID ) COUNT_STUDENT ");
		sql.append(" ,(SELECT COUNT(*) FROM student WHERE STUDY_MANEGER_ID = a.USER_ID AND IC_CARD_NO IS NOT NULL AND IC_CARD_NO != '' ) COUNT_IC_CARD_NO ");
		sql.append(" ,(SELECT COUNT(*) FROM student WHERE STUDY_MANEGER_ID = a.USER_ID AND ATTANCE_NO IS NOT NULL AND ATTANCE_NO != '' ) COUNT_ATTANCE_NO ");
		sql.append(" FROM `user` a ");
		sql.append(" WHERE 1=1 ");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
			sql.append(" and a.organizationID = :organizationID ") ;
			params.put("organizationID", countUserOperationVo.getBlCampusId());
		}
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取学管所带学生、绑卡、绑指纹数量（取昨晚跑存储过程的数据）
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getProcStudyManagerWithCardAndFingerprintAndStudent(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" SELECT ");
		sql.append(" 	c.NAME CAMPUS_NAME,  ");
		sql.append(" 	a.USER_ID,  ");
		sql.append(" 	b.NAME USER_NAME,  ");
		sql.append(" 	a.COUNT_DATE, ");
		sql.append(" 	a.MANAGE_STUDENT_QUANTITY, ");
		sql.append(" 	a.IC_CARD_STUDENT_QUANTITY, ");
		sql.append(" 	a.FINGERPRINT_STUDENT_QUANTITY, ");
		sql.append(" 	a.MANAGE_STUDENT_QUANTITY - a.IC_CARD_STUDENT_QUANTITY UNBOUND_IC_CARD_QUANTITY, ");
		sql.append(" 	a.MANAGE_STUDENT_QUANTITY - a.FINGERPRINT_STUDENT_QUANTITY UNBOUND_FINGERPRINT_QUANTITY, ");
		sql.append(" 	a.MANAGE_STUDENT_QUANTITY * 2 - a.IC_CARD_STUDENT_QUANTITY - a.FINGERPRINT_STUDENT_QUANTITY TOTAL_UNBOUND_QUANTITY ");
		sql.append(" FROM ");
		sql.append(" 	COUNT_UNBOUND_ATTENDANCE_NUMBER a ");
		sql.append(" INNER JOIN `user` b ON b.USER_ID = a.USER_ID ");
		sql.append(" INNER JOIN `organization` c ON b.organizationID = c.ID ");
		sql.append(" INNER JOIN `user_role` d ON d.userID = a.USER_ID ");
		sql.append(" INNER JOIN `role` e ON e.ID = d.roleID ");
		sql.append(" WHERE 1=1 ");
		sql.append(" 	AND (e.roleCode = '"+RoleCode.STUDY_MANAGER+"' OR e.roleCode = '"+RoleCode.STUDY_MANAGER_HEAD+"') ");
		sql.append(" 	AND a.COUNT_DATE = date_sub(curdate(), INTERVAL 1 DAY) ");
		
		//如果当前登录账号是学管师，则只能看到自己的信息，不能看其他人的
		String userRoleCode=userService.getCurrentLoginUser().getRoleCode();
		String studyManager = RoleCode.STUDY_MANAGER.toString();
		if(userRoleCode.equals(studyManager)){
			sql.append(" AND b.USER_ID = :userId ");
			params.put("userId", userService.getCurrentLoginUser().getUserId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
//			sql.append(" AND b.organizationID = '"+countUserOperationVo.getBlCampusId()+"' ") ;
			Organization org=organizationDao.findById(countUserOperationVo.getBlCampusId());
			sql.append(" AND b.organizationID in (select id from organization where orgLevel like :orgLevel ) ") ;
			params.put("orgLevel", org.getOrgLevel()+"%");
			
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getUserName())) {
			sql.append(" AND b.NAME like :userName ") ;
			params.put("userName", "%"+countUserOperationVo.getUserName()+"%");
		}
		
		//加权限
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("未绑定考勤号的学生数量","sql","b.organizationID"));
		
		sql.append(" GROUP BY a.USER_ID ");
		sql.append(" ORDER BY TOTAL_UNBOUND_QUANTITY DESC ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取未上课学生数量
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getNewCourseStudentQuantity(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" SELECT a.ID CAMPUS_ID, a. NAME CAMPUS_NAME, count(DISTINCT(b.ID)) NEW_COURSE_STUDENT_QUANTITY, '"+countUserOperationVo.getMonitorSubject().getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" FROM `organization` a ");
		sql.append(" INNER JOIN student b ON a.ID = b.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN course c ON c.STUDENT_ID = b.ID ");
		sql.append(" WHERE orgType = '"+OrganizationType.CAMPUS+"' ");
		sql.append(" 	AND c.COURSE_STATUS = '"+CourseStatus.NEW+"' ");
		sql.append(" 	AND c.COURSE_DATE >= :startDate ");
		sql.append(" 	AND c.COURSE_DATE <= :endDate ");
		sql.append(" 	GROUP BY a.ID, a. NAME ");

		params.put("startDate", countUserOperationVo .getStartDate());
		params.put("endDate", countUserOperationVo.getEndDate());
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取考勤学生数量
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getAttendanceStudentQuantity(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" SELECT a.ID CAMPUS_ID, a. NAME CAMPUS_NAME, count(a.ID) ATTENDANCE_QUANTITY, '"+countUserOperationVo.getMonitorSubject().getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" FROM `organization` a ");
		sql.append(" INNER JOIN student b ON a.ID = b.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		* ");
		sql.append(" 	FROM ");
		sql.append(" 		( ");
		sql.append(" 			SELECT	 ");
		sql.append(" 				ID, STUDENT_ID, substring(ATTENDANCE_TIME, 1, 10) ATTENDANCE_DATE ");
		sql.append(" 			FROM  ");
		sql.append(" 				`STUDENT_ATTENDANCE_RECORD` ");
		sql.append(" 		) d ");
		sql.append(" 	WHERE ");
		sql.append(" 		d.ATTENDANCE_DATE >= :startDate ");
		sql.append(" 	AND d.ATTENDANCE_DATE <= :endDate ");
		sql.append(" 	GROUP BY ");
		sql.append(" 		d.STUDENT_ID, ");
		sql.append(" 		d.ATTENDANCE_DATE ");
		sql.append(" ) c ON c.STUDENT_ID = b.ID ");
		sql.append(" WHERE ");
		sql.append(" 	orgType = '"+OrganizationType.CAMPUS+"' ");
		sql.append(" GROUP BY ");
		sql.append(" 	a.ID ");

		params.put("startDate", countUserOperationVo .getStartDate());
		params.put("endDate", countUserOperationVo.getEndDate());
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取老师未考勤课时数（总未考勤课时数，一对一未考勤课时数 + 小班未考勤课时数）, 不是实时数据，从统计表中查询数据
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @throws Exception 
	 */
	@Override
	public DataPackage getCountTeacherNotAttendanceCourseHourList(CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select a.ID, a.USER_ID, b.NAME USER_NAME, '"+MonitorSubject.TEACHER_NOT_ATTENDANCE_COURSE_HOUR.getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" , sum(a.COUNT_QUANTITY) TOTAL_QUANTITY ");
		sql.append(" ,(  ");
		sql.append(" 	select sum(c.COUNT_QUANTITY) ONE_ON_ONE_QUANTITY from `COUNT_USER_OPERATION` c where 1=1 and MONITOR_SUBJECT = '"+MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_ONE_ON_ONE_COURSE_HOUR+"'  ");
		sql.append(" 		and a.USER_ID = c.USER_ID	GROUP BY c.USER_ID ");
		sql.append("  ) ONE_ON_ONE_QUANTITY ");
		sql.append(" ,(  ");
		sql.append(" 	select sum(e.COUNT_QUANTITY) MINI_CLASS_QUANTITY from `COUNT_USER_OPERATION` e where 1=1 and MONITOR_SUBJECT = '"+MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_MINI_CLASS_COURSE_HOUR+"'  ");
		sql.append(" 		and a.USER_ID = e.USER_ID	GROUP BY e.USER_ID ");
		sql.append("  ) MINI_CLASS_QUANTITY ");
		sql.append(" , c.name CAMPUS_NAME ");
		sql.append(" from `COUNT_USER_OPERATION` a ");
		sql.append(" inner join `user` b on a.USER_ID = b.USER_ID ");
		sql.append(" inner join `organization` c on b.organizationID = c.id ");
		sql.append(" where 1=1 ");
		sql.append(" and (MONITOR_SUBJECT = '"+MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_ONE_ON_ONE_COURSE_HOUR+"' OR MONITOR_SUBJECT = '" + MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_MINI_CLASS_COURSE_HOUR+ "')");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
			sql.append(" and b.organizationID = :blCampusId ") ;
			params.put("blCampusId", countUserOperationVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getTeacherName())) {
			sql.append(" and b.NAME like teacherName ") ;
			params.put("teacherName", "%"+countUserOperationVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getStartDate())) {
			sql.append(" and COUNT_DATE >= :startDate ") ;
			params.put("startDate", countUserOperationVo.getStartDate());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getEndDate())) {
			sql.append(" and COUNT_DATE <= :endDate ") ;
			params.put("endDate", countUserOperationVo.getEndDate());
		}
		Organization campus = userService.getBelongCampus();
		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
			sql.append(" and b.organizationID = :campusId ");
			params.put("campusId", campus.getId());
		}
		
		sql.append(" GROUP BY a.USER_ID ");
		sql.append(" ORDER BY ONE_ON_ONE_QUANTITY DESC ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取老师未考勤课时数（实时查询，实时数据）
	 * @param startDate
	 * @param endDate
	 * @param orgId
	 * @throws Exception 
	 */
	@Override
	public DataPackage getRealTimeTeacherNotAttendanceCourseHourData(CountUserOperationVo countUserOperationVo, DataPackage dp) throws Exception {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" 	SELECT b.USER_ID, b.NAME USER_NAME, '"+MonitorSubject.TEACHER_NOT_ATTENDANCE_COURSE_HOUR.getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" 	, sum(a.PLAN_HOURS) ONE_ON_ONE_QUANTITY,  '' TOTAL_QUANTITY, '' MINI_CLASS_QUANTITY ");
		sql.append(" 	, c.name CAMPUS_NAME ");
		sql.append(" 	FROM `course` a ");
		sql.append(" 	INNER JOIN `user` b ON b.USER_ID = a.TEACHER_ID ");
		sql.append(" 	INNER JOIN `organization` c ON c.id = a.BL_CAMPUS_ID ");
		sql.append(" 	WHERE ");
		sql.append(" 		1 = 1 ");
		sql.append(" 	AND a.COURSE_DATE < curdate() ");
		sql.append(" 	AND ( ");
		sql.append(" 		COURSE_STATUS = 'NEW' ");
		sql.append(" 		OR ");
		sql.append(" 		COURSE_STATUS = 'STUDENT_ATTENDANCE' ");
		sql.append(" 	) ");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
			sql.append(" AND a.BL_CAMPUS_ID = :blCampusId ") ;
			params.put("blCampusId", countUserOperationVo.getBlCampusId());
		}
		//荣辱榜加分公司查询条件
		if (StringUtils.isNotBlank(countUserOperationVo.getOrganizationId())) {
			Organization org = organizationDao.findById(countUserOperationVo.getOrganizationId());
			sql.append(" AND a.BL_CAMPUS_ID in (SELECT ID FROM organization WHERE orgLevel like  :orgLevel )") ;
			params.put("orgLevel", org.getOrgLevel()+"%");
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getTeacherName())) {
			sql.append(" AND b.NAME like :teacherName ") ;
			params.put("teacherName", "%"+countUserOperationVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ") ;
			params.put("startDate", countUserOperationVo.getStartDate());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ") ;
			params.put("endDate", countUserOperationVo.getEndDate());
		}
//		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			sql.append(" AND b.organizationID = '" + campus.getId() + "'");
//		}
		//加权限
		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","c.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("老师未考勤课时数","nsql","sql");
		sql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

//		sql.append(" 	GROUP BY ");
//		sql.append(" 	TEACHER_ID ");
		
		sql.append(" GROUP BY a.BL_CAMPUS_ID,a.TEACHER_ID ");
		sql.append(" ORDER BY ONE_ON_ONE_QUANTITY DESC ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}

	
	/**
	 * 获取老师未考勤课时数
	 * @param countUserOperationVo
	 * @return
	 */
	@Override
	public Double getTeacherNotAttendanceCourseHour(CountUserOperationVo countUserOperationVo) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select sum(COUNT_QUANTITY) COUNT_QUANTITY from COUNT_USER_OPERATION  where 1=1 ");
		sql.append(" and (MONITOR_SUBJECT = '" + MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_ONE_ON_ONE_COURSE_HOUR + "' OR MONITOR_SUBJECT = '"+MonitorSubjectOfTeacherNotAttCourseHour.TEACHER_NOT_ATTENDANCE_MINI_CLASS_COURSE_HOUR+"' ) ");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getUserId())) {
			sql.append(" and USER_ID = :userId ");
			params.put("userId", countUserOperationVo.getUserId());
		}
		
		DataPackage dp = new DataPackage(0, 999);
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		if(list!=null && list.size()>0){
			Object snid = list.get(0).get("COUNT_QUANTITY");
			if (null == snid) {
				return 0.0;
			} else {
				return new Double(snid.toString());
			}
		}
		return 0.0;
	}
	
	@Override
	public DataPackage getCourseHourConsume(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select a.ID, a.USER_ID, b.NAME USER_NAME, '"+countUserOperationVo.getMonitorSubject().getName()+"' MONITOR_SUBJECT, '"+countUserOperationVo.getStartDate()+"' START_DATE, '"+countUserOperationVo.getEndDate()+"' END_DATE ");
		sql.append(" , sum(a.COUNT_QUANTITY) COUNT_QUANTITY ");
		sql.append(" from `COUNT_USER_OPERATION` a ");
		sql.append(" inner join user b on a.USER_ID = b.USER_ID ");
		sql.append(" where 1=1 ");
		sql.append(" and MONITOR_SUBJECT = '"+MonitorSubject.COURSE_HOUR_CONSUME_OF_TEACHER+"' ");
		
		if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
			sql.append(" and b.organizationID = :blCampusId ") ;
			params.put("blCampusId", countUserOperationVo.getBlCampusId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getTeacherId())) {
			sql.append(" and a.USER_ID = :teacherId ") ;
			params.put("teacherId", countUserOperationVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getStartDate())) {
			sql.append(" and COUNT_DATE >= :startDate ") ;
			params.put("startDate", countUserOperationVo.getStartDate());
		}
		if (StringUtils.isNotBlank(countUserOperationVo.getEndDate())) {
			sql.append(" and COUNT_DATE <= :endDate ") ;
			params.put("endDate", countUserOperationVo.getEndDate());
		}
		Organization campus = userService.getBelongCampus();
		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
			sql.append(" and b.organizationID = :campusId ");
			params.put("campusId", campus.getId());
		}
		
		sql.append(" GROUP BY a.USER_ID ");
		sql.append(" ORDER BY a.COUNT_QUANTITY DESC ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
	
	/**
	 * 获取校区数据监控列表
	 * @param countUserOperationVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getCampusMonitorDataSummary(CountUserOperationVo countUserOperationVo, DataPackage dp) {
		StringBuffer sql=  new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		String [] subjectArray = {MonitorSubject.RECEPTIONIST_INPUT_RES.getValue()
				, MonitorSubject.CONSULTOR_PRINT_RECEIPT.getValue()
				, MonitorSubject.CONSULTOR_PRINT_CONTRACT.getValue()
				, MonitorSubject.CONSULTOR_COURSE_REQUIREMENT.getValue()
				, MonitorSubject.STUDY_MANAGER_COURSE_REQUIREMENT.getValue()
				, MonitorSubject.STUDY_MANAGER_PRINT_RECEIPT.getValue()
				, MonitorSubject.STUDY_MANAGER_PRINT_CONTRACT.getValue()};
		String [][] unboundAttNumArray = {{"MANAGE_STUDENT_QUANTITY - FINGERPRINT_STUDENT_QUANTITY", "UNBOUND_FINGERPRINT_QUANTITY"}
																,  {"MANAGE_STUDENT_QUANTITY - IC_CARD_STUDENT_QUANTITY", "UNBOUND_IC_CARD_QUANTITY"}};
		
		if (countUserOperationVo.getOrganizationType().equals(OrganizationType.GROUNP) 
				|| countUserOperationVo.getOrganizationType().equals(OrganizationType.BRENCH)) {// 集团或分公司
			sql.append(" SELECT ( ");
			sql.append(" 	SELECT CONCAT(ID,'_',name) FROM `organization` WHERE orgLevel = SUBSTRING(o.orgLevel, 1, 4) ");
			sql.append(" ) GROUNP, ( ");
			sql.append(" 	SELECT CONCAT(ID,'_',name) FROM `organization` WHERE orgLevel = SUBSTRING(o.orgLevel, 1, 8) ");
			sql.append(" ) BRENCH ");
			
			// 是否显示校区列表数据
			if (countUserOperationVo.getOrganizationType().equals(OrganizationType.GROUNP)) {// 集团
				sql.append(" , '' CAMPUS ");
			} else if (countUserOperationVo.getOrganizationType().equals(OrganizationType.BRENCH)) {// 分公司
				sql.append(" , CONCAT(o.ID,'_',o.name) CAMPUS ");
			}
			
			sql.append(" , '' USER_NAME ");

			// 取数据：每个统计项目数据
			for (String subject:subjectArray) {
				sql.append(" , ( ");
				sql.append(" SELECT SUM(COUNT_QUANTITY) FROM COUNT_USER_OPERATION a ");
				sql.append(" INNER JOIN `user` b ON b.USER_ID = a.USER_ID ");
				sql.append(" INNER JOIN `organization` c ON c.ID = b.organizationID ");
				sql.append(" WHERE MONITOR_SUBJECT = '"+subject+"' and COUNT_DATE >= :startDate AND COUNT_DATE <= :endDate ");
				// TODO: 2017/3/8 改好测试
				sql.append(" 	AND c.ID in (SELECT ID FROM `organization` WHERE orgLevel like CONCAT('', o.orgLevel, '%')) ");
				sql.append(" ) " + subject);
			}
			params.put("startDate", countUserOperationVo.getStartDate());
			params.put("endDate", countUserOperationVo.getEndDate());

			// 取数据：未绑定考勤编码
			for (String []subject:unboundAttNumArray) {
				sql.append(" , ( ");
				sql.append(" 	SELECT ");
				sql.append("			SUM( "+subject[0]+") ");
				sql.append(" 	FROM ");
				sql.append(" 		`COUNT_UNBOUND_ATTENDANCE_NUMBER` a ");
				sql.append(" 	INNER JOIN `user` b ON b.USER_ID = a.USER_ID ");
				sql.append(" 	INNER JOIN `organization` c ON c.ID = b.organizationID ");
				sql.append(" 	WHERE 1=1 ");
				sql.append(" 	AND a.COUNT_DATE = date_sub( curdate(), interval 1 day) ");
				sql.append(" 	AND a.USER_ID = b.USER_ID ");
				sql.append(" 	AND c.ID IN ( ");
				sql.append(" 		SELECT ID FROM `organization` WHERE orgLevel LIKE CONCAT('', o.orgLevel, '%') ");
				sql.append(" 	) ");
				sql.append(" ) " + subject[1]);
			}
			
			sql.append(" FROM `organization` o WHERE 1=1 ");
			
			// 显示分公司还是校区数据
			if (countUserOperationVo.getOrganizationType().equals(OrganizationType.GROUNP)) {// 集团
				sql.append(" AND orgType = '"+OrganizationType.BRENCH+"' ");
			} else if (countUserOperationVo.getOrganizationType().equals(OrganizationType.BRENCH)) {// 分公司
				sql.append(" AND orgType = '"+OrganizationType.CAMPUS+"' ");
			}
			
			if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
				Organization organization =organizationDao.findById(countUserOperationVo.getBlCampusId());
				if (organization != null) {
					sql.append(" 	AND o.orgLevel like :orgLevel ");
					params.put("orgLevel", organization.getOrgLevel()+"%");
				}
			} else {
				if (countUserOperationVo.getOrganizationType().equals(OrganizationType.GROUNP)) {// 集团
					sql.append(" 	AND o.orgLevel like :nextOrgLevel ");
					params.put("nextOrgLevel", organizationDao.getNextOrgLevel("-1", "-1")+"%");
				}
			}
			//加权限
			sql.append(roleQLConfigService.getAppendSqlByAllOrg("校区数据监控","sql","o.id"));
			
		} else if (countUserOperationVo.getOrganizationType().equals(OrganizationType.CAMPUS)) {// 校区
			sql.append(" SELECT ( ");
			sql.append(" 	SELECT CONCAT(ID,'_',name) FROM `organization` WHERE orgLevel = SUBSTRING(c.orgLevel, 1, 4) ");
			sql.append(" ) GROUNP, ( ");
			sql.append(" 	SELECT CONCAT(ID,'_',name) FROM `organization` WHERE orgLevel = SUBSTRING(c.orgLevel, 1, 8) ");
			sql.append(" ) BRENCH, CONCAT(c.ID,'_',c.name) CAMPUS, b.NAME USER_NAME ");
			
			// 取数据：每个统计项目数据
			for (String subject:subjectArray) {
				sql.append(" , ( ");
				sql.append(" SELECT SUM(COUNT_QUANTITY) FROM COUNT_USER_OPERATION a ");
				sql.append(" WHERE MONITOR_SUBJECT = '"+subject+"' and a.COUNT_DATE >= :startDate AND a.COUNT_DATE <= :endDate ");
				sql.append(" 	AND a.USER_ID = b.USER_ID ");
				sql.append(" ) " + subject);
			}
			params.put("startDate", countUserOperationVo.getStartDate());
			params.put("endDate", countUserOperationVo.getEndDate());
			
			// 取数据：未绑定考勤编码
			for (String []subject:unboundAttNumArray) {
				sql.append(" , ( ");
				sql.append(" 	SELECT ");
				sql.append(			subject[0]);
				sql.append(" 	FROM ");
				sql.append(" 		`COUNT_UNBOUND_ATTENDANCE_NUMBER` a ");
				sql.append(" 	WHERE	1=1 ");
				sql.append(" 	AND a.COUNT_DATE = date_sub( curdate(), interval 1 day) ");
				sql.append(" 	AND a.USER_ID = b.USER_ID ");
				sql.append(" ) "+subject[1]);
			}
			
			sql.append(" FROM `user` b ");
			sql.append(" 	INNER JOIN `organization` c ON c.ID = b.organizationID ");
			sql.append(" 	WHERE 1=1 ");
			//加权限
			sql.append(roleQLConfigService.getAppendSqlByAllOrg("校区数据监控","sql","b.organizationId"));
			if (StringUtils.isNotBlank(countUserOperationVo.getBlCampusId())) {
				sql.append(" 		AND b.organizationID = :blCampusId ");
				params.put("blCampusId", countUserOperationVo.getBlCampusId());
				Organization organization =organizationDao.findById(countUserOperationVo.getBlCampusId());
				if (organization != null) {
					sql.append(" 		AND c.orgLevel like :orgLevel ");
					params.put("orgLevel", organization.getOrgLevel()+"%");
				}
			}
			sql.append(" GROUP BY b.USER_ID ");
		}
		
		
		sql.append(" HAVING ( ");
		sql.append(" 	RECEPTIONIST_INPUT_RES > 0 OR CONSULTOR_PRINT_RECEIPT > 0 OR CONSULTOR_PRINT_CONTRACT > 0 ");
		sql.append(" 	OR CONSULTOR_COURSE_REQUIREMENT > 0 OR STUDY_MANAGER_COURSE_REQUIREMENT > 0  ");
		sql.append(" 	OR STUDY_MANAGER_PRINT_RECEIPT > 0 OR STUDY_MANAGER_PRINT_CONTRACT > 0  ");
		sql.append(" 	OR UNBOUND_FINGERPRINT_QUANTITY > 0 OR UNBOUND_IC_CARD_QUANTITY > 0  ");
		sql.append(" ) ");
		
		List<Map<Object, Object>> list= super.findMapOfPageBySql(sql.toString(), dp, params);
		
		dp.setDatas(list);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		return dp;
	}
}
