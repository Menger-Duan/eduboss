package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.CourseStatus;
import com.eduboss.common.OrganizationType;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.OtmClassCourseDao;
import com.eduboss.domain.Organization;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.User;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.service.UserService;
import com.eduboss.utils.CalculateUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Repository("OtmClassCourseDao")
public class OtmClassCourseDaoImpl extends GenericDaoImpl<OtmClassCourse, String> implements OtmClassCourseDao {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrganizationDao organizationDao;
	


	@Override
	public void save(OtmClassCourse otmClassCourse) {
		String courseDate = otmClassCourse.getCourseDate();
		String courseTime = otmClassCourse.getCourseTime();
		BigDecimal courseMinutes = otmClassCourse.getCourseMinutes();
		Double courseHours = otmClassCourse.getCourseHours();
		CalculateUtil.calCourseTimeBetweenUnExpectTime(courseDate+" "+courseTime, courseMinutes, courseHours);
		super.save(otmClassCourse);
	}

	/**
	 * 根据一对多更新一对多课程
	 * @param otmClass
	 * @throws Exception
	 */
	@Override
	public void updateOtmClassCourse(OtmClass otmClass) throws Exception {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("otmClassName", otmClass.getName());
		params.put("subject", otmClass.getSubject().getId());
		hql.append(" update OtmClassCourse set otmClassName= :otmClassName ,SUBJECT= :subject  ");
		
		if(otmClass.getGrade()!=null){//现在逻辑允许为空
			hql.append(",GRADE= :grade ");
			params.put("grade", otmClass.getGrade().getId());
		}else{
			hql.append(",GRADE= :grade ");
			params.put("grade", null+" ");
		}
		if(otmClass.getTeacher() != null){
			hql.append(",teacher.userId= :teacherId ");
			params.put("teacherId", otmClass.getTeacher().getUserId());
		}
		params.put("otmClassId", otmClass.getOtmClassId());
		params.put("courseDate", DateTools.getCurrentDate());
		hql.append(" where otmClass.otmClassId = :otmClassId ");
		hql.append(" and courseDate > :courseDate ");
		this.excuteHql(hql.toString(),params);
	}
	
	/**
	 * 通过一对多ID查出所有一对多课程，按课程时间倒序
	 * @param otmClassId
	 * @return
	 */
	public List<OtmClassCourse> getOtmClassCourseListByOtmClassId(String otmClassId) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassCourse  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(otmClassId)) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassId);
		}
		
		hql.append(" order by courseDate asc ");

		List<OtmClassCourse> list=this.findAllByHQL(hql.toString(),params);
		return list;
	}
	
	/**
	 * 通过一对多ID查出所有一对多课程，按课程时间倒序，返回类型为DataPackage
	 * @param otmClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseListByOtmClassId(String otmClassId, String firstSchoolDate, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassCourse  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(otmClassId)) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassId);
		}
		if (StringUtils.isNotBlank(firstSchoolDate)) {
			hql.append(" and courseDate >= :firstSchoolDate ");
			params.put("firstSchoolDate", firstSchoolDate);
		}
		
		hql.append(" order by courseDate asc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	/**
	 * 一对多课程详情
	 * @param otmClassId
	 * @param dp
	 * @param modelVo
	 * @return
	 */
	public DataPackage getOtmClassCourseDetail(String otmClassId, DataPackage dp,ModelVo modelVo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from OtmClassCourse  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(otmClassId)) {
			hql.append(" and otmClass.otmClassId = :otmClassId ");
			params.put("otmClassId", otmClassId);
		}
		if (StringUtils.isNotBlank(modelVo.getStartDate())) {
			hql.append(" and courseDate >= :startDate ");
			params.put("startDate", modelVo.getStartDate());
		}
		if (StringUtils.isNotBlank(modelVo.getEndDate())) {
			hql.append(" and courseDate <= :endDate ");
			params.put("endDate", modelVo.getEndDate());
		}
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} 
		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = new HashMap<String, Object>();
		hql.append("select occ from OtmClassCourse occ ");
		if (StringUtils.isNotBlank(otmClassCourseVo.getStudentId())) {
			hql.append(" ,OtmClassStudentAttendent osa where osa.otmClassCourse.otmClassCourseId=occ.otmClassCourseId and osa.studentId = :studentId ");
			params.put("studentId", otmClassCourseVo.getStudentId());
		}else{
			hql.append(" where 1=1 ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getStartDate())) {
			hql.append(" and occ.courseDate >= :startDate ");
			params.put("startDate", otmClassCourseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getEndDate())) {
			hql.append(" and occ.courseDate <= :endDate ");
			params.put("endDate", otmClassCourseVo.getEndDate());
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getOtmClassName())) {
			hql.append(" and occ.otmClass.name like :otmClassName ");
			params.put("otmClassName", "%" + otmClassCourseVo.getOtmClassName() + "%");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getBlCampusName())) {
			hql.append(" and occ.otmClass.blCampus.name like :blCampusName ");
			params.put("blCampusName", "%" + otmClassCourseVo.getBlCampusName() + "%");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getTeacherName())) {
			hql.append(" and occ.teacher.name like :teacherName ");
			params.put("teacherName", "%" + otmClassCourseVo.getTeacherName() + "%");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getStudentId())) {
			hql.append(" and occ.otmClass.otmClassId in (select otmClass.otmClassId from OtmClassStudent where student.id = :studentId2)");			
			params.put("studentId2", otmClassCourseVo.getStudentId());
		}
        if (StringUtils.isNotBlank(otmClassCourseVo.getTeacherId())) {
            hql.append(" and occ.teacher.userId = :teacherId ");
            params.put("teacherId", otmClassCourseVo.getTeacherId());
        }
        
        if (StringUtils.isNotBlank(otmClassCourseVo.getCourseStatus())) {
            hql.append(" and occ.courseStatus = :courseStatus ");
            params.put("courseStatus", CourseStatus.valueOf(otmClassCourseVo.getCourseStatus()));
        }
        
        if(otmClassCourseVo.getAuditStatus() != null && StringUtils.isNotBlank(otmClassCourseVo.getAuditStatus().toString())){
        	hql.append(" AND occ.auditStatus = :auditStatus ");
        	params.put("auditStatus", otmClassCourseVo.getAuditStatus());
		}
        
        if(StringUtils.isNotBlank(otmClassCourseVo.getSearchParam())){
        	hql.append(" and (occ.teacher.name like :searchParam0 ");
        	hql.append(" or occ.otmClass.name like :searchParam1 ");
        	hql.append(" or occ.subject.name like :searchParam2 ");
        	hql.append(" or occ.grade.name like :searchParam3 ");
        	hql.append(")");
        	params.put("searchParam0", "%" + otmClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam1", "%" + otmClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam2", "%" + otmClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam3", "%" + otmClassCourseVo.getSearchParam() + "%");
        }
        //手机端按月查询 2015-07
        if (StringUtils.isNotBlank(otmClassCourseVo.getSearchMonth())) {
            hql.append(" and occ.courseDate like :searchMonth ");
            params.put("searchMonth", otmClassCourseVo.getSearchMonth() + "%");
        }
		
		//权限控制，老师只能看自己，学管只能看自己，如果是校区中的人，最多只能看到本校区的
		//Organization campus = userService.getBelongCampus();
		//hql.append(RoleCodeAuthoritySearchUtil.getMiniClassCourseRoleCodeAuthority(userService.getCurrentLoginUser(), campus));
		User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(otmClassCourseVo.getCurrentRoleId())){
			 hql.append(" and occ.teacher.userId='"+user.getUserId()+"'");
		} else if("classTeacherDeduction".equals(otmClassCourseVo.getCurrentRoleId())){
//				hql.append(" and studyHead.userId='"+user.getUserId()+"'");
			// 学管只能看到，课程学生的班主任为自己的课程
			 hql.append(" and '"+user.getUserId()+"' in (select studyManegerId from Student where id in "
			 		+ "(select studentId from OtmClassStudentAttendent where otmClassCourse.otmClassCourseId = occ.otmClassCourseId)) ");
		} else if ("mobileAttendance".equalsIgnoreCase(otmClassCourseVo.getCurrentRoleId())) {
			hql.append(" and (occ.teacher.userId='"+user.getUserId()+"' ");
			hql.append(" or '"+user.getUserId()+"' in (select studyManegerId from Student where id in "
				 		+ "(select studentId from OtmClassStudentAttendent where otmClassCourse.otmClassCourseId = occ.otmClassCourseId)) ");
		// 用于手机端的查询
		}  else if ("studentRole".equalsIgnoreCase(otmClassCourseVo.getCurrentRoleId())) {
			System.out.println("studentRole is current role! ");
		} else {	
            if(!"courseSchedule".equals(otmClassCourseVo.getCurrentRoleId())){
                hql.append(" and 1=2 ");
            }
		}
        
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by occ."+dp.getSidx()+" "+dp.getSord());
		} else{
			hql.append(" order by occ.createTime desc ");
		}

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 *//*
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp) {
		StringBuffer sql = new StringBuffer(" SELECT occ.*, occ.GRADE GRADE_ID, occ.SUBJECT SUBJECT_ID, occ.ATTENDANCE_PIC_NAME ATTENDACE_PIC_NAME ");
		StringBuffer sqlFrom = new StringBuffer(" FROM otm_class_course occ ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(otmClassCourseVo.getStudentId())) {
			sqlFrom.append(" , otm_class_student_attendent ocsa ");
			sqlWhere.append(" AND ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID AND ocsa.STUDENT_ID = '" + otmClassCourseVo.getStudentId() + "' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getStartDate())) {
			sqlWhere.append(" AND occ.COURSE_DATE >= '" + otmClassCourseVo.getStartDate() + "' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getEndDate())) {
			sqlWhere.append(" AND occ.COURSE_DATE <= '" + otmClassCourseVo.getEndDate() + "' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getOtmClassName())) {
			sqlWhere.append(" AND occ.OTM_CLASS_NAME LIKE '%" + otmClassCourseVo.getOtmClassName() + "%' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getBlCampusName())) {
			sql.append(" , campus.NAME BL_CAMPUS_NAME ");
			sqlFrom.append(" , otm_class oc, organization campus ");
			sqlWhere.append(" AND occ.OTM_CLASS_ID = oc.OTM_CLASS_ID AND oc.BL_CAMPUS_ID = campus.ID ");
			sqlWhere.append(" AND campus.NAME LIKE '%" + otmClassCourseVo.getBlCampusName() + "%' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getTeacherName())) {
			sql.append(" , teacher.NAME TEACHER_NAME ");
			sqlFrom.append(" , user teacher ");
			sqlWhere.append(" AND occ.TEACHER_ID = teacher.USER_ID ");
			sqlWhere.append(" AND teacher.NAME LIKE '%" + otmClassCourseVo.getTeacherName() + "%' ");
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getStudentId())) {
			sqlWhere.append(" AND occ.OTM_CLASS_ID IN (SELECT OTM_CLASS_ID FROM otm_class_student WHERE STUDENT_ID = '" + otmClassCourseVo.getStudentId() + "')");			
		}
        if (StringUtils.isNotBlank(otmClassCourseVo.getTeacherId())) {
        	sqlWhere.append(" AND occ.TEACHER_ID = '" + otmClassCourseVo.getTeacherId() + "' ");
        }
        
        if (StringUtils.isNotBlank(otmClassCourseVo.getCourseStatus())) {
        	sqlWhere.append(" AND occ.COURSE_STATUS = '" + otmClassCourseVo.getCourseStatus() + "' ");
        }
        
        if(otmClassCourseVo.getAuditStatus() != null ){
        	sqlWhere.append(" AND occ.AUDIT_STATUS = '" + otmClassCourseVo.getAuditStatus() + "' ");
        }
        
        if(StringUtils.isNotBlank(otmClassCourseVo.getSearchParam())){
        	if (sqlFrom.indexOf("user teacher") < 0) {
        		sql.append(" , teacher.NAME TEACHER_NAME ");
    			sqlFrom.append(" , user teacher ");
    			sqlWhere.append(" AND occ.TEACHER_ID = teacher.USER_ID ");
        	}
        	
        	sql.append(" , dd_g.NAME GRADE_NAME ");
			sqlFrom.append(" , data_dict dd_g ");
			sqlWhere.append(" AND occ.GRADE = dd_g.ID ");
			
			sql.append(" , dd_s.NAME SUBJECT_NAME ");
			sqlFrom.append(" , data_dict dd_s ");
			sqlWhere.append(" AND occ.SUBJECT = dd_s.ID ");
			
			sqlWhere.append(" AND (teacher.NAME LIKE '%" + otmClassCourseVo.getSearchParam() + "%' ");
			sqlWhere.append(" OR occ.OTM_CLASS_NAME LIKE '%" + otmClassCourseVo.getSearchParam() + "%' ");
			sqlWhere.append(" OR dd_s.NAME LIKE '%" + otmClassCourseVo.getSearchParam() + "%'");
			sqlWhere.append(" OR dd_s.NAME LIKE '%" + otmClassCourseVo.getSearchParam() + "%'");
//        	hql.append(" or studyHead.name like '%" + otmClassCourseVo.getSearchParam() + "%'");
			sqlWhere.append(")");
        }
        //手机端按月查询 2015-07
        if (StringUtils.isNotBlank(otmClassCourseVo.getSearchMonth())) {
        	sqlWhere.append(" AND occ.COURSE_DATE LIKE '" + otmClassCourseVo.getSearchMonth() + "%' ");
        }
		
		//权限控制，老师只能看自己，学管只能看自己，如果是校区中的人，最多只能看到本校区的
		//Organization campus = userService.getBelongCampus();
		//hql.append(RoleCodeAuthoritySearchUtil.getMiniClassCourseRoleCodeAuthority(userService.getCurrentLoginUser(), campus));
		User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(otmClassCourseVo.getCurrentRoleId())){
        	sqlWhere.append(" AND occ.TEACHER_ID='"+user.getUserId()+"'");
		} else if("classTeacherDeduction".equals(otmClassCourseVo.getCurrentRoleId())){
//				hql.append(" and studyHead.userId='"+user.getUserId()+"'");
			if (sqlFrom.indexOf("otm_class oc,") < 0) {
				sqlFrom.append(" , otm_class oc ");
				sqlWhere.append(" AND occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
			}
			// 学管只能看到，课程学生的班主任为自己的课程
			sqlWhere.append(" AND '"+user.getUserId()+"' IN (SELECT STUDY_MANAGER_ID FROM STUDENT_ORGANIZATION WHERE STUDENT_ID IN "
			 		+ "(SELECT STUDENT_ID FROM otm_class_student_attendent WHERE OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID) AND ORGANIZATION_ID = oc.BL_CAMPUS_ID) ");
		} else if ("mobileAttendance".equalsIgnoreCase(otmClassCourseVo.getCurrentRoleId())) {
			sqlWhere.append(" AND (occ.TEACHER_ID='"+user.getUserId()+"' ");
			sqlWhere.append(" OR '"+user.getUserId()+"' IN (SELECT STUDY_MANEGER_ID FROM student WHERE ID IN "
				 		+ "(SELECT STUDENT_ID FROM otm_class_student_attendent WHERE OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID)) ");
		// 用于手机端的查询
		}  else if ("studentRole".equalsIgnoreCase(otmClassCourseVo.getCurrentRoleId())) {
			System.out.println("studentRole is current role! ");
		} else {	
            if(!"courseSchedule".equals(otmClassCourseVo.getCurrentRoleId())){
            	sqlWhere.append(" and 1=2 ");
            }
		}
        
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if ("blCampusName".equals(dp.getSidx())) {
				if (sqlFrom.indexOf("otm_class oc") < 0) {
					sqlFrom.append(" , otm_class oc ");
					sqlWhere.append(" AND occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
				}
				sqlWhere.append(" ORDER BY oc.BL_CAMPUS_ID "+dp.getSord());
			} else {
				sqlWhere.append(" ORDER BY occ."+dp.getSidx()+" "+dp.getSord());
			}
		} else{
			sqlWhere.append(" ORDER BY occ.CREATE_TIME DESC ");
		}
		sql.append(sqlFrom).append(sqlWhere);
		return jdbcTemplateDao.queryPage(sql.toString(), OtmClassCourseVo.class, dp, true);
	}*/
	
	/**
	 * 一对多课程列表手机接口
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseListForMobile(OtmClassCourseVo otmClassCourseVo,
			DataPackage dp) {
		StringBuffer sql = new StringBuffer();

		StringBuffer sql2 = new StringBuffer();
		sql.append(" select DISTINCT occ.OTM_CLASS_COURSE_ID,occ.OTM_CLASS_ID,occ.OTM_CLASS_NAME,occ.COURSE_DATE,occ.COURSE_STATUS, ");
		sql.append(" occ.COURSE_TIME,occ.COURSE_MINUTES,occ.COURSE_HOURS,occ.COURSE_END_TIME,occ.TEACHER_ID,occ.SUBJECT,occ.GRADE,occ.ATTENDANCE_PIC_NAME, ");
		sql.append("  occ.TEACHING_ATTEND_TIME,occ.STUDY_MANAGER_CHARGE_TIME,occ.TEACHING_ATTEND_TIME,occ.STUDY_MANAGER_CHARGE_TIME,");
		sql.append(" occ.AUDIT_STATUS,occ.CREATE_TIME,occ.CREATE_USER_ID,occ.MODIFY_TIME,occ.MODIFY_USER_ID,occ.FIRST_ATTENDENT_TIME from otm_class_course occ ");
		sql.append(" left join otm_class_student_attendent ocsa on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
		sql.append(" left join  user teacher on occ.TEACHER_ID=teacher.USER_ID  ");
		sql.append(" left join  data_dict dd1 on occ.GRADE=dd1.ID  ");
		sql.append(" left join  data_dict dd2 on occ.SUBJECT=dd2.ID  ");
		sql.append(" left join user study on ocsa.STUDY_MANAGER_ID = study.USER_ID  ");
		sql.append("  where 1=1 ");
		
		sql2.append(" select DISTINCT occ.* from otm_class_student_attendent ocsa ");
		sql2.append(" inner join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
		sql2.append(" left join  user teacher on occ.TEACHER_ID=teacher.USER_ID  ");
		sql2.append(" left join  data_dict dd2 on occ.SUBJECT=dd2.ID  ");
		sql2.append(" left join user study on ocsa.STUDY_MANAGER_ID = study.USER_ID  ");
		sql2.append("  where 1=1 ");
		
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(otmClassCourseVo.getStartDate())) {
			sql.append("  and occ.COURSE_DATE>= :courseStartDate ");
			sql2.append("  and occ.COURSE_DATE>= :courseStartDate ");
			params.put("courseStartDate", otmClassCourseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(otmClassCourseVo.getEndDate())) {
			sql.append("  and occ.COURSE_DATE<= :courseEndDate ");
			sql2.append("  and occ.COURSE_DATE<= :courseEndDate ");
			params.put("courseEndDate", otmClassCourseVo.getEndDate());
		}
        
        if (StringUtils.isNotBlank(otmClassCourseVo.getCourseStatus())) {
            sql.append(" and occ.course_status = :courseStatus ");
            sql2.append(" and occ.course_status = :courseStatus ");
            params.put("courseStatus", otmClassCourseVo.getCourseStatus());
        }
        
        User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(otmClassCourseVo.getCurrentRoleId())){
			 sql.append(" and occ.teacher_id= :teacherId ");
			 sql2.append(" and occ.teacher_id= :teacherId ");
			 params.put("teacherId", user.getUserId());
		} else if("classTeacherDeduction".equals(otmClassCourseVo.getCurrentRoleId())){
			 sql.append(" and ocsa.STUDY_MANAGER_ID= :studyManagerId ");
			 sql2.append(" and ocsa.STUDY_MANAGER_ID= :studyManagerId ");
			 params.put("studyManagerId", user.getUserId());
		}
        
        if(StringUtils.isNotBlank(otmClassCourseVo.getSearchParam())){
        	sql.append(" and (teacher.name like :searchParam ");
        	sql.append(" or occ.OTM_CLASS_NAME like :searchParam ");
        	sql.append(" or dd2.name like :searchParam ");
        	sql.append(" or dd1.name like :searchParam ");
        	sql.append(" or study.name like :searchParam ");
        	sql.append(") union ");
        	sql.append(sql2);
        	sql.append(" and (teacher.name like :searchParam ");
        	sql.append(" or occ.OTM_CLASS_NAME like :searchParam ");
        	sql.append(" or dd2.name like :searchParam ");
        	sql.append(" or study.name like :searchParam ");
        	sql.append(")");
        	
        	params.put("searchParam", "%" + otmClassCourseVo.getSearchParam() + "%");
        	
        }
		
		
        
		sql.append(" order by occ.create_Time desc ");
			
			

		return super.findPageBySql(sql.toString(), dp,true,params);
	}

	@Override
	public List getOtmClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" IFNULL(sum(IF(gradeName = '幼儿园',stuNums,0)),0) as '幼儿园', ");
		sql.append(" IFNULL(sum(IF(gradeName = '一年级',stuNums,0)),0) as '一年级', ");
		sql.append(" IFNULL(sum(IF(gradeName = '二年级',stuNums,0)),0) as '二年级',");
		sql.append(" IFNULL(sum(IF(gradeName = '三年级',stuNums,0)),0) as '三年级',");
		sql.append(" IFNULL(sum(IF(gradeName = '四年级',stuNums,0)),0) as '四年级',");
		sql.append(" IFNULL(sum(IF(gradeName = '五年级',stuNums,0)),0) as '五年级',");
		sql.append(" IFNULL(sum(IF(gradeName = '六年级',stuNums,0)),0) as '六年级',");
		sql.append(" IFNULL(sum(IF(gradeName = '初一',stuNums,0)),0) as '初一',");
		sql.append(" IFNULL(sum(IF(gradeName = '初二',stuNums,0)),0) as '初二',");
		sql.append(" IFNULL(sum(IF(gradeName = '初三',stuNums,0)),0) as '初三',");
		sql.append(" IFNULL(sum(IF(gradeName = '高一',stuNums,0)),0) as '高一',");
		sql.append(" IFNULL(sum(IF(gradeName = '高二',stuNums,0)),0) as '高二',");
		sql.append(" IFNULL(sum(IF(gradeName = '高三',stuNums,0)),0) as '高三', ");
		sql.append(" IFNULL(sum(IF(gradeName = '毕业',stuNums,0)),0) as '毕业' ");

		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
		
		sql.append(" from ");
		sql.append(" (select  ");
		sql.append(" sum(case when occ.AUDIT_STATUS = 'VALIDATE' then occ.course_hours else 0 end) as amount, ");
		sql.append(" oc.OTM_CLASS_ID, g.NAME as gradeName, g.id  as gradeId , oc.BL_CAMPUS_ID, occ.OTM_CLASS_COURSE_ID, occ.TEACHER_ID, occ.GRADE, ");
		sql.append(" CASE WHEN tv.TEACHER_TYPE = 'TEN_CLASS_TEACHER' THEN sum(stu.charge) ELSE sum(stu.stuNums) END AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  otm_class_course occ ");
		sql.append(" LEFT JOIN teacher_version tv ON occ.TEACHER_ID = tv.TEACHER_ID AND tv.VERSION_DATE = (SELECT max(VERSION_DATE) FROM teacher_version WHERE VERSION_DATE <= occ.COURSE_DATE AND occ.TEACHER_ID = TEACHER_ID) ");
		sql.append(" left JOIN( ");
		sql.append(" select a.OTM_CLASS_COURSE_ID, a.conlete,a.late, case when (conlete+late-1)>0 then (conlete+late-1) else 0 end as 'stuNums', charge from (  ");
		sql.append(" select  ");
		sql.append(" occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ), 0) as 'conlete', ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ), 0) as 'late', ");
		sql.append(" IFNULL(sum(CASE WHEN ocsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS charge ");
		sql.append(" from otm_class_student_attendent ocsa ");
		sql.append(" LEFT JOIN otm_class_course occ on occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID where occ.course_status = 'CHARGED' and occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and occ.COURSE_DATE >= :startDate ");
		sql.append(" and occ.COURSE_DATE <= :endDate ");
		sql.append(" group by occ.OTM_CLASS_COURSE_ID ) a )stu on occ.OTM_CLASS_COURSE_ID=stu.OTM_CLASS_COURSE_ID ");
		sql.append(" left join otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on occ.GRADE = g.ID ");
		sql.append("  where 1=1 ");
//		sql.append(" and oc.BL_CAMPUS_ID = '"+vo.getBlCampusId()+"'");
		sql.append(" and occ.COURSE_DATE >= :startDate ");
		sql.append(" and occ.COURSE_DATE <= :endDate ");
		sql.append(" and occ.COURSE_STATUS = 'CHARGED'  ");

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
						params.put("subject"+i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append("  group by ");
		sql.append("  oc.BL_CAMPUS_ID, oc.OTM_CLASS_ID, occ.TEACHER_ID , occ.OTM_CLASS_COURSE_ID ) d ");
		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID, d.TEACHER_ID  ");

		List list = super.findMapBySql(sql.toString(),params);
		return list;
	}

	/**
	 * 一对多审批汇总(课时)
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	public DataPackage getOtmClassCourseAuditAnalyze(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder sql=new StringBuilder();
		sql.append("select ");
		sql.append("org.*, ah.teacher, ah.workType ,ah.teacherBlCampus, ah.teacherId, ah.otmClassName, ah.unAuditHours, ah.validateHours,  ah.unValidateHours, gh.*,ah.teacherLevel,ah.teacherType ");
		sql.append(" from ( select u.name as 'teacher',u.WORK_TYPE as 'workType' , org.name as 'teacherBlCampus', oc.name as 'otmClassName', oc.BL_CAMPUS_ID, occ.grade as 'grade', occ.TEACHER_ID as 'teacherId', occ.OTM_CLASS_ID as otmClassId, ");
		sql.append(" IFNULL(sum(case when (occ.AUDIT_STATUS is null or occ.audit_status = 'UNAUDIT') then occ.course_hours else 0 end), 0) as 'unAuditHours', ");		
		sql.append(" IFNULL(sum(case when occ.AUDIT_STATUS='VALIDATE' then occ.course_hours else 0 end ), 0) as 'validateHours', ");
		sql.append(" IFNULL(sum(case when occ.AUDIT_STATUS = 'UNVALIDATE' then occ.course_hours else 0 end ), 0) as 'unValidateHours' ");		

		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherType");
    	
		sql.append(" from otm_class_course occ ");
		sql.append(" LEFT JOIN otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append(" LEFT JOIN user u on occ.TEACHER_ID=u.USER_ID ");
		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end
		sql.append(" where 1=1");
		
		/*List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		if(userOrganizations != null && userOrganizations.size() > 0){
			Organization org = userOrganizations.get(0);
			sql.append("  and org.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
			for(int i = 1; i < userOrganizations.size(); i++){
				sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
			}
			sql.append(")");
		}*/
		
		//主职位
		sql.append(" and udj.isMajorRole = 0");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and oc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		sql.append(" and occ.course_date >= :startDate ");
		sql.append(" and occ.course_date <= :endDate ");
		sql.append(" and occ.course_status = 'CHARGED'");
		
		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
		if(StringUtils.isNotBlank(otmClassTypes)){
			if(StringUtils.isNotBlank(otmClassTypes)){			
				String[] otmClassTypeStrs=otmClassTypes.split(",");
				if(otmClassTypeStrs.length>0){
					sql.append(" AND (oc.OTM_TYPE = :otmType ");
                    params.put("otmType", otmClassTypeStrs[0]);
					for (int i = 1; i < otmClassTypeStrs.length; i++) {
						sql.append(" or oc.OTM_TYPE = :otmType"+i+" ");
						params.put("otmType"+i, otmClassTypeStrs[i]);
					}
					sql.append(" )");
				}
			}
		}

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
						params.put("subject"+i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append(" group by oc.BL_CAMPUS_ID, occ.OTM_CLASS_ID, occ.TEACHER_ID ) ah ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" select bl_campus_id, teacher_id, GRADE, OTM_CLASS_ID, ");
		sql.append(" IFNULL(max(IF(gradeName = '一年级',amount,0)),0) as 'gradeOneHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '二年级',amount,0)),0) as 'gradeTwoHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '三年级',amount,0)),0) as 'gradeThreeHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '四年级',amount,0)),0) as 'gradeFourHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '五年级',amount,0)),0) as 'gradeFiveHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '六年级',amount,0)),0) as 'gradeSixHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初一',amount,0)),0) as 'gradeSevenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初二',amount,0)),0) as 'gradeEightHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初三',amount,0)),0) as 'gradeNineHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高一',amount, 0)),0) as 'gradeTenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高二',amount,0)),0) as 'gradeElevenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高三',amount,0)),0) as 'gradeTwelveHours', ");
		sql.append(" IFNULL(max(IF(gradeName is null,amount,0)),0) as 'otherHours', ");
		sql.append(" IFNULL(sum(IF(gradeName = '一年级',d.stuNums,0)),0) as 'stuNums1', ");
		sql.append(" IFNULL(sum(IF(gradeName = '二年级',d.stuNums,0)),0) as 'stuNums2',");
		sql.append(" IFNULL(sum(IF(gradeName = '三年级',d.stuNums,0)),0) as 'stuNums3',");
		sql.append(" IFNULL(sum(IF(gradeName = '四年级',d.stuNums,0)),0) as 'stuNums4',");
		sql.append(" IFNULL(sum(IF(gradeName = '五年级',d.stuNums,0)),0) as 'stuNums5',");
		sql.append(" IFNULL(sum(IF(gradeName = '六年级',d.stuNums,0)),0) as 'stuNums6',");
		sql.append(" IFNULL(sum(IF(gradeName = '初一',d.stuNums,0)),0) as 'stuNums7',");
		sql.append(" IFNULL(sum(IF(gradeName = '初二',d.stuNums,0)),0) as 'stuNums8',");
		sql.append(" IFNULL(sum(IF(gradeName = '初三',d.stuNums,0)),0) as 'stuNums9',");
		sql.append(" IFNULL(sum(IF(gradeName = '高一',d.stuNums,0)),0) as 'stuNums10',");
		sql.append(" IFNULL(sum(IF(gradeName = '高二',d.stuNums,0)),0) as 'stuNums11',");
		sql.append(" IFNULL(sum(IF(gradeName = '高三',d.stuNums,0)),0) as 'stuNums12', ");
		sql.append(" IFNULL(max(IF(gradeName is null,d.stuNums,0)),0) as 'otherStuNums' ");
        
		sql.append(" from ");          
		sql.append(" (select  ");
		sql.append(" sum(case when occ.AUDIT_STATUS = 'VALIDATE' then occ.course_hours else 0 end) as amount, ");
		sql.append(" oc.OTM_CLASS_ID, g.NAME as gradeName, oc.BL_CAMPUS_ID, occ.OTM_CLASS_COURSE_ID, occ.TEACHER_ID, occ.GRADE, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = occ.TEACHER_ID  AND tv.VERSION_DATE <= occ.COURSE_DATE) AND TEACHER_ID = occ.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  otm_class_course occ ");
		sql.append(" left JOIN( ");
		sql.append(" select  ");
		sql.append(" occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ), 0) as 'conlete', ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ), 0) as 'late', ");
		sql.append(" IFNULL(sum(CASE WHEN ocsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS charge ");
		sql.append(" from otm_class_student_attendent ocsa ");
		sql.append(" LEFT JOIN otm_class_course occ on occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID where occ.course_status = 'CHARGED' and occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and occ.COURSE_DATE >= :startDate  ");
		sql.append(" and occ.COURSE_DATE <= :endDate  ");
		sql.append(" group by occ.OTM_CLASS_COURSE_ID ) a on occ.OTM_CLASS_COURSE_ID=a.OTM_CLASS_COURSE_ID ");
		sql.append(" left join otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on occ.GRADE = g.ID ");
		sql.append("  where 1=1 ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and oc.BL_CAMPUS_ID = :blCampusId2 ");
			params.put("blCampusId2", vo.getBlCampusId());
		}
		sql.append(" and occ.COURSE_DATE >= :startDate  ");
		sql.append(" and occ.COURSE_DATE <= :endDate ");
		sql.append(" and occ.COURSE_STATUS = 'CHARGED'  ");


		
		
		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
		}
		sql.append("  group by ");		
		sql.append("  oc.BL_CAMPUS_ID, oc.OTM_CLASS_ID, occ.TEACHER_ID  ) d "); //

		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID, d.TEACHER_ID, d.OTM_CLASS_ID ) gh  ");		 
		sql.append(" on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.teacherId = gh.TEACHER_ID and ah.grade=gh.GRADE and ah.otmClassId=gh.OTM_CLASS_ID ");
		sql.append(" inner join ");
		sql.append(" (select ");
		sql.append(" CONCAT(org_group.id,'') as groupId,org_group.name as groupName,");
		sql.append(" CONCAT(org_brench.id,'') as brenchId,org_brench.name as brenchName,");
		sql.append("  CONCAT(org_campus.id,'') as campusId,org_campus.name as campusName");
		sql.append("  from organization org_campus  ");
		sql.append("  LEFT JOIN organization org_brench on org_campus.parentID = org_brench.id  ");
		sql.append(" LEFT JOIN organization org_group on org_brench.parentID = org_group.id  ");
		sql.append(" where 1=1 ");
		sql.append(" and org_campus.orgType = 'CAMPUS'");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and org_campus.id = :blCampusId3 ");
			params.put("blCampusId3", vo.getBlCampusId());
		}
		sql.append("  ) org ");
		sql.append(" on ah.BL_CAMPUS_ID = org.campusId ");
		if (StringUtils.isNotBlank(AuditStatus)&&AuditStatus!=null) {
    		if ("HAS_UNAUDIT".equals(AuditStatus)) {
    			sql.append(" and ah.unAuditHours > 0 ");
    		} else {
    			sql.append(" and ah.unAuditHours <= 0 ");
    		}
    	}
		
		if(StringUtils.isNotBlank(vo.getTeacherType())){
			sql.append(" and ah.teacherType= :teacherType ");
			params.put("teacherType", vo.getTeacherType());
		}
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dp,params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	/**
	 * 一对多课程审批汇总(小时)
	 *
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @param otmClassTypes
	 * @param subject
	 * @return
	 */
	@Override
	public DataPackage getOtmClassCourseAuditAnalyzeXiaoShi(DataPackage dp, BasicOperationQueryVo vo, String auditStatus, String otmClassTypes) {
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
        if (StringUtil.isNotBlank(vo.getBlCampusId())) {
            params.put("blCampusId", vo.getBlCampusId());
        }
		
		StringBuilder sql=new StringBuilder();
		sql.append("select ");
		sql.append("org.*, ah.teacher, ah.workType ,ah.teacherBlCampus, ah.teacherId, ah.otmClassName, ah.unAuditHours, ah.validateHours,  ah.unValidateHours, gh.*,ah.teacherLevel,ah.teacherType ");
		sql.append(" from ( select u.name as 'teacher',u.WORK_TYPE as 'workType' , org.name as 'teacherBlCampus', oc.name as 'otmClassName', oc.BL_CAMPUS_ID, occ.grade as 'grade', occ.TEACHER_ID as 'teacherId', occ.OTM_CLASS_ID as otmClassId, ");
		sql.append(" ROUND(IFNULL(sum(case when (occ.AUDIT_STATUS is null or occ.audit_status = 'UNAUDIT') then occ.course_hours*occ.COURSE_MINUTES/60 else 0 end), 0),2) as 'unAuditHours', ");
		sql.append(" ROUND(IFNULL(sum(case when occ.AUDIT_STATUS='VALIDATE' then occ.course_hours*occ.COURSE_MINUTES/60 else 0 end ), 0),2) as 'validateHours', ");
		sql.append(" ROUND(IFNULL(sum(case when occ.AUDIT_STATUS = 'UNVALIDATE' then occ.course_hours*occ.COURSE_MINUTES/60 else 0 end ), 0),2) as 'unValidateHours' ");
		
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherType");
		
		sql.append(" from otm_class_course occ ");
		sql.append(" LEFT JOIN otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append(" LEFT JOIN user u on occ.TEACHER_ID=u.USER_ID ");
		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end
		sql.append(" where 1=1");

		/*List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
        if(userOrganizations != null && userOrganizations.size() > 0){
            Organization org = userOrganizations.get(0);
            sql.append("  and org.id in ( select id from organization where orgLevel like '").append(org.getOrgLevel()).append("%' ");
            for(int i = 1; i < userOrganizations.size(); i++){
                sql.append(" or orgLevel like '").append(userOrganizations.get(i).getOrgLevel()).append("%'");
            }
            sql.append(")");
        }*/

		//主职位
		sql.append(" and udj.isMajorRole = 0");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
		    sql.append(" and oc.BL_CAMPUS_ID = :blCampusId ");
		}
		sql.append(" and occ.course_date >= :startDate ");
		sql.append(" and occ.course_date <= :endDate ");
		sql.append(" and occ.course_status = 'CHARGED'");
		if(StringUtils.isNotBlank(otmClassTypes)){
			if(StringUtils.isNotBlank(otmClassTypes)){
				String[] otmClassTypeStrs=otmClassTypes.split(",");
				if(otmClassTypeStrs.length>0){					
					sql.append(" AND (oc.OTM_TYPE = :otmType ");
                    params.put("otmType", otmClassTypeStrs[0]);
					for (int i = 1; i < otmClassTypeStrs.length; i++) {
						sql.append(" or oc.OTM_TYPE = :otmType"+i+" ");
						params.put("otmType"+i, otmClassTypeStrs[i]);
					}
					sql.append(" )");
				}
			}
		}

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
						params.put("subject"+i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append(" group by oc.BL_CAMPUS_ID, occ.OTM_CLASS_ID, occ.TEACHER_ID ) ah ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" select bl_campus_id, teacher_id, GRADE, OTM_CLASS_ID, ");
		sql.append(" IFNULL(max(IF(gradeName = '一年级',amount,0)),0) as 'gradeOneHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '二年级',amount,0)),0) as 'gradeTwoHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '三年级',amount,0)),0) as 'gradeThreeHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '四年级',amount,0)),0) as 'gradeFourHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '五年级',amount,0)),0) as 'gradeFiveHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '六年级',amount,0)),0) as 'gradeSixHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初一',amount,0)),0) as 'gradeSevenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初二',amount,0)),0) as 'gradeEightHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '初三',amount,0)),0) as 'gradeNineHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高一',amount, 0)),0) as 'gradeTenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高二',amount,0)),0) as 'gradeElevenHours', ");
		sql.append(" IFNULL(max(IF(gradeName = '高三',amount,0)),0) as 'gradeTwelveHours', ");
		sql.append(" IFNULL(max(IF(gradeName is null, amount, 0)), 0) as 'otherHours', ");
		sql.append(" IFNULL(sum(IF(gradeName = '一年级',d.stuNums,0)),0) as 'stuNums1', ");
		sql.append(" IFNULL(sum(IF(gradeName = '二年级',d.stuNums,0)),0) as 'stuNums2',");
		sql.append(" IFNULL(sum(IF(gradeName = '三年级',d.stuNums,0)),0) as 'stuNums3',");
		sql.append(" IFNULL(sum(IF(gradeName = '四年级',d.stuNums,0)),0) as 'stuNums4',");
		sql.append(" IFNULL(sum(IF(gradeName = '五年级',d.stuNums,0)),0) as 'stuNums5',");
		sql.append(" IFNULL(sum(IF(gradeName = '六年级',d.stuNums,0)),0) as 'stuNums6',");
		sql.append(" IFNULL(sum(IF(gradeName = '初一',d.stuNums,0)),0) as 'stuNums7',");
		sql.append(" IFNULL(sum(IF(gradeName = '初二',d.stuNums,0)),0) as 'stuNums8',");
		sql.append(" IFNULL(sum(IF(gradeName = '初三',d.stuNums,0)),0) as 'stuNums9',");
		sql.append(" IFNULL(sum(IF(gradeName = '高一',d.stuNums,0)),0) as 'stuNums10',");
		sql.append(" IFNULL(sum(IF(gradeName = '高二',d.stuNums,0)),0) as 'stuNums11',");
		sql.append(" IFNULL(sum(IF(gradeName = '高三',d.stuNums,0)),0) as 'stuNums12', ");
		sql.append(" IFNULL(sum(IF(gradeName is null,d.stuNums,0)),0) as 'otherStuNums' ");

		sql.append(" from ");
		sql.append(" (select  ");
		sql.append(" ROUND(sum(case when occ.AUDIT_STATUS = 'VALIDATE' then occ.course_hours*occ.COURSE_MINUTES/60 else 0 end),2) as amount, ");
		sql.append(" oc.OTM_CLASS_ID, g.NAME as gradeName, oc.BL_CAMPUS_ID, occ.OTM_CLASS_COURSE_ID, occ.TEACHER_ID, occ.GRADE, ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = occ.TEACHER_ID  AND tv.VERSION_DATE <= occ.COURSE_DATE) AND TEACHER_ID = occ.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  otm_class_course occ ");
		sql.append(" LEFT JOIN teacher_version tv ON occ.TEACHER_ID = tv.TEACHER_ID AND tv.VERSION_DATE = (SELECT max(VERSION_DATE) FROM teacher_version WHERE VERSION_DATE <=  occ.COURSE_DATE AND occ.TEACHER_ID = TEACHER_ID) ");
		sql.append(" left JOIN( ");
        sql.append(" select  ");
        sql.append(" occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ), 0) as 'conlete', ");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ), 0) as 'late', ");
		sql.append(" IFNULL(sum(CASE WHEN ocsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS 'charge' ");
		sql.append(" from otm_class_student_attendent ocsa ");
		sql.append(" LEFT JOIN otm_class_course occ on occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID where occ.course_status = 'CHARGED' and occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and occ.COURSE_DATE >= :startDate  ");
		sql.append(" and occ.COURSE_DATE <= :endDate  ");
		sql.append(" group by occ.OTM_CLASS_COURSE_ID ) a on occ.OTM_CLASS_COURSE_ID=a.OTM_CLASS_COURSE_ID ");
		sql.append(" left join otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on occ.GRADE = g.ID ");
		sql.append("  where 1=1 ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
		    sql.append(" and oc.BL_CAMPUS_ID = :blCampusId ");
		}
		sql.append(" and occ.COURSE_DATE >= :startDate  ");
		sql.append(" and occ.COURSE_DATE <= :endDate ");
		sql.append(" and occ.COURSE_STATUS = 'CHARGED'  ");

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
		}
		sql.append("  group by ");
		sql.append("  oc.BL_CAMPUS_ID, oc.OTM_CLASS_ID, occ.TEACHER_ID ) d ");

		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID, d.TEACHER_ID, d.OTM_CLASS_ID ) gh  ");
		sql.append(" on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.teacherId = gh.TEACHER_ID and ah.grade=gh.GRADE and ah.otmClassId=gh.OTM_CLASS_ID ");
		sql.append(" inner join ");
		sql.append(" (select ");
		sql.append(" CONCAT(org_group.id,'') as groupId,org_group.name as groupName,");
		sql.append(" CONCAT(org_brench.id,'') as brenchId,org_brench.name as brenchName,");
		sql.append("  CONCAT(org_campus.id,'') as campusId,org_campus.name as campusName");
		sql.append("  from organization org_campus  ");
		sql.append("  LEFT JOIN organization org_brench on org_campus.parentID = org_brench.id  ");
		sql.append(" LEFT JOIN organization org_group on org_brench.parentID = org_group.id  ");
		sql.append(" where 1=1 ");
		sql.append(" and org_campus.orgType = 'CAMPUS'");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
		    sql.append(" and org_campus.id = :blCampusId ");
		}
		sql.append(" ) org ");
		sql.append(" on ah.BL_CAMPUS_ID = org.campusId ");
		if (StringUtils.isNotBlank(auditStatus)&&auditStatus!=null) {
			if ("HAS_UNAUDIT".equals(auditStatus)) {
				sql.append(" and ah.unAuditHours > 0 ");
			} else {
				sql.append(" and ah.unAuditHours <= 0 ");
			}
		}
		
		if(StringUtils.isNotBlank(vo.getTeacherType())){
			sql.append(" and ah.teacherType= :teacherType ");
			params.put("teacherType", vo.getTeacherType());
		}
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dp,params);

		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	/**
	 * 一对多课程审批汇总工资
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	public DataPackage otmClaCourseAuditAnalyzeSalary(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan) {
		
		Map<String, Object> params = Maps.newHashMap();
		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
		
		StringBuffer sql=new StringBuffer();
		sql.append(" select ");
		sql.append(" org.*, ah.teacher, ah.employeeNo, ah.workType , ah.teacherBlCampus, ah.teacherId, ah.otmClassName, ah.unAuditHours, ah.validateHours,ah.unValidateHours, gh.*,ah.teacherLevel,ah.teacherType,ah.monthLastStudents  ");
		sql.append("  from ");
		sql.append("   ( select ");
		sql.append(" u.name as 'teacher', u.employee_No as 'employeeNo', u.WORK_TYPE as 'workType' , org.name as 'teacherBlCampus', oc.name as 'otmClassName', oc.BL_CAMPUS_ID, oc.OTM_CLASS_ID, occ.grade as 'grade', occ.TEACHER_ID as 'teacherId', occ.OTM_CLASS_COURSE_ID as otmCourseId,");
		sql.append(" (select count(1) from otm_class_student_attendent where otm_class_course_id =(  select otm_class_course_id from otm_class_course where COURSE_DATE=(");
		sql.append(" select max(course_date) from otm_class_course where course_date like concat(substr( :endDate ,1,7),'%') and otm_class_id =oc.otm_class_id and COURSE_STATUS<>'CANCEL') and otm_class_id =oc.otm_class_id and COURSE_STATUS<>'CANCEL' limit 1)) monthLastStudents ,");
		if(anshazhesuan!=null && anshazhesuan.equals("hour")){
//			课程时长按照小时查询
			sql.append("  IFNULL(sum(case  when (occ.AUDIT_STATUS is null or occ.audit_status = 'UNAUDIT') then ROUND((occ.course_hours * IFNULL(occ.COURSE_MINUTES,0) / 60),2)else 0 end),0)  as 'unAuditHours', ");
			sql.append(" IFNULL(sum(case  when occ.AUDIT_STATUS='VALIDATE' then ROUND((occ.course_hours * IFNULL(occ.COURSE_MINUTES,0) / 60),2)else 0 end),0)  as 'validateHours', ");
			sql.append(" IFNULL(sum(case  when occ.AUDIT_STATUS = 'UNVALIDATE' then ROUND((occ.course_hours * IFNULL(occ.COURSE_MINUTES,0) / 60),2)else 0 end),0) as 'unValidateHours'   ");
			
		}else{
			sql.append(" IFNULL(sum(case when (occ.AUDIT_STATUS is null or occ.audit_status = 'UNAUDIT') then occ.course_hours else 0 end), 0) as 'unAuditHours', ");
			sql.append(" IFNULL(sum(case when occ.AUDIT_STATUS='VALIDATE' then occ.course_hours else 0 end ), 0) as 'validateHours', ");
			sql.append(" IFNULL(sum(case when occ.AUDIT_STATUS = 'UNVALIDATE' then occ.course_hours else 0 end ), 0) as 'unValidateHours' ");
		}
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =occ.teacher_id) and TEACHER_ID=occ.teacher_id) teacherType");
    	
		sql.append(" from otm_class_course occ ");
		sql.append(" LEFT JOIN otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append(" LEFT JOIN user u on occ.TEACHER_ID=u.USER_ID ");
		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end
		sql.append("  where occ.course_status = 'CHARGED' ");
		//主职位
		sql.append(" and udj.isMajorRole = 0");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and oc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		sql.append(" and occ.course_date >= :startDate ");
		sql.append(" and occ.course_date <= :endDate ");
		if(StringUtils.isNotBlank(otmClassTypes)){
			if(StringUtils.isNotBlank(otmClassTypes)){			
				String[] otmClassTypeStrs=otmClassTypes.split(",");
				if(otmClassTypeStrs.length>0){					
					sql.append(" AND (oc.OTM_TYPE = :otmType ");
                    params.put("otmType", otmClassTypeStrs[0]);
					for (int i = 1; i < otmClassTypeStrs.length; i++) {
						sql.append(" or oc.OTM_TYPE = :otmType"+i+" ");
						params.put("otmType"+i, otmClassTypeStrs[i]);
					}
					sql.append(" )");
				}
			}
		}

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=vo.getSubject().split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" ");
						params.put("subject"+i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append(" group by oc.BL_CAMPUS_ID, occ.OTM_CLASS_ID, occ.TEACHER_ID ) ah ");
		
		sql.append("  LEFT JOIN ");
		sql.append(" ( select bl_campus_id, teacher_id, GRADE, otm_class_id, ");
		if(anshazhesuan!=null && anshazhesuan.equals("hour")){
//			课程时长按照小时查询
			sql.append(" IFNULL(max(IF(gradeName = '一年级',hourAmount,0)),0) as 'gradeOneHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '二年级',hourAmount,0)),0) as 'gradeTwoHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '三年级',hourAmount,0)),0) as 'gradeThreeHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '四年级',hourAmount,0)),0) as 'gradeFourHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '五年级',hourAmount,0)),0) as 'gradeFiveHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '六年级',hourAmount,0)),0) as 'gradeSixHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初一',hourAmount,0)),0) as 'gradeSevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初二',hourAmount,0)),0) as 'gradeEightHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初三',hourAmount,0)),0) as 'gradeNineHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高一',hourAmount, 0)),0) as 'gradeTenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高二',hourAmount,0)),0) as 'gradeElevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高三',hourAmount,0)),0) as 'gradeTwelveHours', ");
			sql.append(" IFNULL(max(IF(gradeName is null, hourAmount, 0)), 0) as 'otherHours', ");
			
			sql.append(" IFNULL(sum(IF(gradeName = '一年级',salaryHours,0)),0) as 'salarys1', ");
			sql.append(" IFNULL(sum(IF(gradeName = '二年级',salaryHours,0)),0) as 'salarys2',");
			sql.append(" IFNULL(sum(IF(gradeName = '三年级',salaryHours,0)),0) as 'salarys3',");
			sql.append(" IFNULL(sum(IF(gradeName = '四年级',salaryHours,0)),0) as 'salarys4',");
			sql.append(" IFNULL(sum(IF(gradeName = '五年级',salaryHours,0)),0) as 'salarys5',");
			sql.append(" IFNULL(sum(IF(gradeName = '六年级',salaryHours,0)),0) as 'salarys6',");
			sql.append(" IFNULL(sum(IF(gradeName = '初一',salaryHours,0)),0) as 'salarys7',");
			sql.append(" IFNULL(sum(IF(gradeName = '初二',salaryHours,0)),0) as 'salarys8',");
			sql.append(" IFNULL(sum(IF(gradeName = '初三',salaryHours,0)),0) as 'salarys9',");
			sql.append(" IFNULL(sum(IF(gradeName = '高一',salaryHours,0)),0) as 'salarys10',");
			sql.append(" IFNULL(sum(IF(gradeName = '高二',salaryHours,0)),0) as 'salarys11',");
			sql.append(" IFNULL(sum(IF(gradeName = '高三',salaryHours,0)),0) as 'salarys12', ");
			sql.append(" IFNULL(sum(IF(gradeName is null, salaryHours, 0)), 0) as 'otherSalarys' ");
		}else{
			sql.append(" IFNULL(max(IF(gradeName = '一年级',amount,0)),0) as 'gradeOneHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '二年级',amount,0)),0) as 'gradeTwoHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '三年级',amount,0)),0) as 'gradeThreeHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '四年级',amount,0)),0) as 'gradeFourHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '五年级',amount,0)),0) as 'gradeFiveHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '六年级',amount,0)),0) as 'gradeSixHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初一',amount,0)),0) as 'gradeSevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初二',amount,0)),0) as 'gradeEightHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初三',amount,0)),0) as 'gradeNineHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高一',amount, 0)),0) as 'gradeTenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高二',amount,0)),0) as 'gradeElevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高三',amount,0)),0) as 'gradeTwelveHours', ");
			sql.append(" IFNULL(max(IF(gradeName is null, amount, 0)), 0) as 'otherHours', ");
			
			sql.append(" IFNULL(sum(IF(gradeName = '一年级',salarys,0)),0) as 'salarys1', ");
			sql.append(" IFNULL(sum(IF(gradeName = '二年级',salarys,0)),0) as 'salarys2',");
			sql.append(" IFNULL(sum(IF(gradeName = '三年级',salarys,0)),0) as 'salarys3',");
			sql.append(" IFNULL(sum(IF(gradeName = '四年级',salarys,0)),0) as 'salarys4',");
			sql.append(" IFNULL(sum(IF(gradeName = '五年级',salarys,0)),0) as 'salarys5',");
			sql.append(" IFNULL(sum(IF(gradeName = '六年级',salarys,0)),0) as 'salarys6',");
			sql.append(" IFNULL(sum(IF(gradeName = '初一',salarys,0)),0) as 'salarys7',");
			sql.append(" IFNULL(sum(IF(gradeName = '初二',salarys,0)),0) as 'salarys8',");
			sql.append(" IFNULL(sum(IF(gradeName = '初三',salarys,0)),0) as 'salarys9',");
			sql.append(" IFNULL(sum(IF(gradeName = '高一',salarys,0)),0) as 'salarys10',");
			sql.append(" IFNULL(sum(IF(gradeName = '高二',salarys,0)),0) as 'salarys11',");
			sql.append(" IFNULL(sum(IF(gradeName = '高三',salarys,0)),0) as 'salarys12', ");
			sql.append(" IFNULL(sum(IF(gradeName is null, salarys, 0)), 0) as 'otherSalarys' ");
		}		
		
		sql.append(" from ");
		sql.append("  ( select sum(d.salary) as salarys, sum(d.salaryHour) as salaryHours,oc.BL_CAMPUS_ID, sum(case when d.AUDIT_STATUS = 'VALIDATE' then d.course_hours else 0 end) as amount,");
		sql.append(" SUM(CASE WHEN d.AUDIT_STATUS = 'VALIDATE' THEN ROUND((d.COURSE_HOURS * IFNULL(d.COURSE_MINUTES,0) / 60),2) else 0 end) as hourAmount, d.*  ");
		sql.append(" from ");				
		sql.append(" (select occ.OTM_CLASS_ID, g.NAME as gradeName, g.ID as gradeId, occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" occ.AUDIT_STATUS, occ.TEACHER_ID, occ.GRADE, occ.course_hours,occ.course_minutes, ");

		sql.append(" ifnull(occ.course_hours *  (CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = occ.TEACHER_ID  AND tv.VERSION_DATE <= occ.COURSE_DATE) AND TEACHER_ID = occ.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS salary, ");

		sql.append(" ifnull(ROUND((occ.course_hours * IFNULL(occ.COURSE_MINUTES, 0) / 60),2) *  (CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = occ.TEACHER_ID  AND tv.VERSION_DATE <= occ.COURSE_DATE) AND TEACHER_ID = occ.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS salaryHour ");

		sql.append("  from otm_class_course occ ");
		sql.append("  INNER JOIN ");		
		sql.append(" (  select occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(CASE WHEN ocsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS charge ");
		sql.append(" from otm_class_student_attendent ocsa ");
		sql.append(" LEFT JOIN otm_class_course occ on occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID ");
		sql.append(" where occ.course_status = 'CHARGED' and occ.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and occ.COURSE_DATE >= :startDate ");
		sql.append(" and occ.COURSE_DATE <= :endDate  ");
		sql.append(" group by occ.OTM_CLASS_COURSE_ID ) a ");
		sql.append(" on occ.OTM_CLASS_COURSE_ID=a.OTM_CLASS_COURSE_ID ");
		sql.append(" left join otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append(" LEFT JOIN ( select ID, NAME from data_dict where CATEGORY = 'STUDENT_GRADE' ) g on occ.GRADE = g.ID ");
		sql.append(" where  occ.COURSE_STATUS = 'CHARGED' ");
		sql.append(" and occ.COURSE_DATE >= :startDate  ");
		sql.append(" and occ.COURSE_DATE <= :endDate  ");		
		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and occ.TEACHER_ID = :teacherId ");
		}		
		sql.append("  ) d  ");
				
		sql.append(" LEFT JOIN   ");
		sql.append(" otm_class oc on oc.OTM_CLASS_ID=d.OTM_CLASS_ID ");
		sql.append(" where d.AUDIT_STATUS = 'VALIDATE' ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and oc.BL_CAMPUS_ID = :blCampusId2 ");
			params.put("blCampusId2", vo.getBlCampusId());
		}
		sql.append("  group by d.OTM_CLASS_ID, oc.BL_CAMPUS_ID, d.TEACHER_ID ");
		sql.append("  ) bh  ");
		sql.append(" group by bh.otm_class_id, bh.teacher_id ) gh ");
		
		sql.append(" on ah.OTM_CLASS_ID = gh.OTM_CLASS_ID ");		
		sql.append(" and ah.teacherId = gh.TEACHER_ID  ");
		sql.append(" and ah.grade=gh.GRADE ");
//		sql.append(" and ah.miniCourseId=gh.MINI_CLASS_COURSE_ID  ");
		
		
		sql.append(" inner join ");
		sql.append("   ( select  CONCAT(org_group.id, '') as groupId,org_group.name as groupName, ");
		sql.append("   CONCAT(org_brench.id,'') as brenchId, org_brench.name as brenchName, ");
		sql.append(" CONCAT(org_campus.id,'') as campusId,org_campus.name as campusName    ");
		sql.append("  from organization org_campus  ");
		sql.append("   LEFT JOIN  organization org_brench  on org_campus.parentID = org_brench.id ");
		sql.append("   LEFT JOIN  organization org_group  on org_brench.parentID = org_group.id    ");
		sql.append("  where org_campus.orgType = 'CAMPUS' ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and org_campus.id = :blCampusId3 ");
			params.put("blCampusId3", vo.getBlCampusId());
		}
		if (StringUtil.isNotBlank(vo.getBrenchId())){
			sql.append(" and org_brench.id = :branchId ");
			params.put("branchId", vo.getBrenchId());
		}
		sql.append(" ) org "); 
		sql.append(" on ah.BL_CAMPUS_ID = org.campusId ");
				
		if (StringUtils.isNotBlank(AuditStatus)&&AuditStatus!=null) {
    		if ("HAS_UNAUDIT".equals(AuditStatus)) {
    			sql.append(" and ah.unAuditHours > 0 ");
    		} else {
    			sql.append(" and ah.unAuditHours <= 0 ");
    		}
    	}
		if(StringUtils.isNotBlank(vo.getTeacherType())){
			sql.append(" and ah.teacherType= :teacherType ");
			params.put("teacherType", vo.getTeacherType());
		}
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dp,params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}
	
	/**
	 * 一对多审批列表
	 * @param gridRequest
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @return
	 */
	public DataPackage otmClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String subject) {
		
		Map<String, Object> params = Maps.newHashMap();

		
		String orgLevel=null;
		if(campusId != null && !"".equals(campusId)){
			Organization org = organizationDao.findById(campusId);
			if(org.getOrgType()==OrganizationType.DEPARTMENT){
				Organization o=organizationDao.findById(org.getParentId());
				orgLevel = o.getOrgLevel();
			} else {
				orgLevel = org.getOrgLevel();
			}
		}
		StringBuilder sql=new StringBuilder();
		sql.append(" select occ.ATTENDANCE_PIC_NAME as attendacePicName,");
		sql.append(" campus.name as 'campusName', oc.name as 'className', d.name as 'grade', occ.grade as 'gradeValue', occ.otm_class_course_id as 'courseId', u_teacher.name as 'teacher', ");
		sql.append(" dd_subject.name as 'subjectName', occ.COURSE_MINUTES as 'courseMinutes', occ.course_date as 'courseDate', occ.course_time as 'startTime', occ.course_end_time as 'endTime', occ.course_hours as 'courseHours', occ.AUDIT_STATUS as 'auditStatus',");
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ),0) 'conplete',");  //上课人数
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'NEW' then 1 else 0 end ),0) 'news',");  //未上课人数
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ),0) 'late', ");   //迟到人数 迟到
		sql.append(" IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'ABSENT' then 1 else 0 end ),0) 'absent', ");   //缺勤人数
		sql.append(" IFNULL(sum(case when ocsa.CHARGE_STATUS = 'CHARGED' then 1 else 0 end ),0) 'charged', ");   //扣费人数
		sql.append(" IFNULL(sum(case when ocsa.CHARGE_STATUS = 'UNCHARGE' then 1 else 0 end ),0) 'uncharge', ");     //未扣费人数
		sql.append(" IFNULL(case when occ.AUDIT_STATUS='VALIDATE' then occ.course_hours else 0 end ,0)  as 'auditHours', ");   //有效课时
		sql.append(" IFNULL(case when occ.audit_status = 'UNVALIDATE' then occ.course_hours else 0 end ,0) as 'cancelHours',  ");  //无效课时
		sql.append(" IFNULL(case when (occ.audit_status is null or occ.audit_status = 'UNAUDIT') then occ.course_hours else 0 end ,0) as 'unauditHours'  ");  //未审批
		sql.append(" from otm_class_course occ  ");
		sql.append(" LEFT JOIN otm_class oc on occ.OTM_CLASS_ID=oc.OTM_CLASS_ID ");
		sql.append(" LEFT JOIN otm_class_student_attendent ocsa on occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN user u on u.USER_ID=occ.teacher_id ");
		sql.append(" LEFT JOIN ( select id,name from data_dict where CATEGORY = 'STUDENT_GRADE' ) d on occ.grade=d.id ");
		sql.append(" LEFT JOIN organization campus on campus.id=oc.BL_CAMPUS_ID");
		sql.append(" INNER JOIN organization brench on brench.id=campus.parentID");
		sql.append(" INNER JOIN organization grounp on grounp.id=brench.parentID");
		sql.append(" INNER JOIN user u_teacher on occ.teacher_id=u_teacher.USER_ID ");
		sql.append(" INNER JOIN data_dict dd_subject on occ.subject=dd_subject.ID ");
		sql.append(" where 1=1 ");
		sql.append(" and occ.course_status = 'CHARGED' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and occ.course_date >= :startDate ");
			params.put("startDate", startDate);

		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and occ.course_date <= :endDate ");
			params.put("endDate", endDate);
		}
		if(orgLevel!=null && !"".equals(orgLevel)){
			sql.append(" and campus.orgLevel like :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}
		if(auditStatus!=null && !"".equals(auditStatus)){
			if(auditStatus.equals("UNAUDIT")){
				//未审批
				sql.append(" and occ.audit_status is null");
			}else{
				sql.append(" and occ.audit_status= :auditStatus ");
				params.put("auditStatus", auditStatus);
			}
					
		}

		//科目筛选
		if (StringUtil.isNotBlank(subject)){
			String[] subjects=subject.split(",");
			if(subjects.length>0){
				sql.append(" and ( 1=0 ");
				for (int i = 0; i < subjects.length; i++) {
					if (StringUtil.isNotBlank(subjects[i])){
						sql.append(" or occ.SUBJECT = :subject"+i+" " );
						params.put("subject"+i, subjects[i]);
					}
				}
				sql.append(" )");
			}
		}

		if(teacherId!=null && !"".equals(teacherId)){
			sql.append(" and occ.teacher_id = :teacherId ");
			params.put("teacherId", teacherId);
		}		
		sql.append(" group by occ.otm_class_course_id,oc.BL_CAMPUS_ID ");
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
	}
	
}
