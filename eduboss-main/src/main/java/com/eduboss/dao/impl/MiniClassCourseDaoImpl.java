package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.CourseStatus;
import com.eduboss.dao.MiniClassCourseDao;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.User;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CalculateUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Repository("MiniClassCourse")
public class MiniClassCourseDaoImpl extends GenericDaoImpl<MiniClassCourse, String> implements MiniClassCourseDao {

	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Override
	public void save(MiniClassCourse miniClassCourse) {
		String courseDate = miniClassCourse.getCourseDate();
		String courseTime = miniClassCourse.getCourseTime();
		BigDecimal courseMinutes = miniClassCourse.getCourseMinutes();
		Double courseHours = miniClassCourse.getCourseHours();
		CalculateUtil.calCourseTimeBetweenUnExpectTime(courseDate+" "+courseTime, courseMinutes, courseHours);
		super.save(miniClassCourse);
	}


	/**
	 * 小班课程列表
	 */
	@Override
	public DataPackage getMiniClassCourseList(MiniClassCourseVo miniClassCourseVo,
			DataPackage dp) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select mc from MiniClassCourse mc ");
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudentId())) {
			hql.append(" ,MiniClassStudentAttendent msa where msa.miniClassCourse.miniClassCourseId=mc.miniClassCourseId and msa.studentId = :studentId ");
			params.put("studentId", miniClassCourseVo.getStudentId());
		}else{
			hql.append(" where 1=1 ");
		}
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getStartDate())) {
			hql.append(" and mc.courseDate >= :startDate ");
			params.put("startDate", miniClassCourseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getEndDate())) {
			hql.append(" and mc.courseDate <= :endDate ");
			params.put("endDate", miniClassCourseVo.getEndDate());
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getMiniClassName())) {
			hql.append(" and mc.miniClass.name like :miniClassName ");
			params.put("miniClassName", "%" + miniClassCourseVo.getMiniClassName() + "%");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getBlCampusName())) {
			hql.append(" and mc.miniClass.blCampus.name like :blCampusName ");
			params.put("blCampusName", "%" + miniClassCourseVo.getBlCampusName() + "%");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getTeacherName())) {
			hql.append(" and mc.teacher.name like :teacherName ");
			params.put("teacherName", "%" + miniClassCourseVo.getTeacherName() + "%");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudyManegerName())) {
			hql.append(" and mc.miniClass.studyManeger.name like :studyManegerName ");
			params.put("studyManegerName", "%" + miniClassCourseVo.getStudyManegerName() + "%");
		}
		if(miniClassCourseVo.getAuditStatus() != null && StringUtils.isNotBlank(miniClassCourseVo.getAuditStatus().toString())){
			hql.append(" AND mc.auditStatus = :auditStatus ");
			params.put("auditStatus", miniClassCourseVo.getAuditStatus());
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudentId())) {
			hql.append(" and mc.miniClass.miniClassId in (select miniClass.miniClassId from MiniClassStudent where student.id = :studentId2)");
			params.put("studentId2", miniClassCourseVo.getStudentId());
		}
        if (StringUtils.isNotBlank(miniClassCourseVo.getTeacherId())) {
            hql.append(" and mc.teacher.userId = :teacherId ");
            params.put("teacherId", miniClassCourseVo.getTeacherId());
        }
        
        if (StringUtils.isNotBlank(miniClassCourseVo.getCourseStatus())) {
            hql.append(" and mc.courseStatus = :courseStatus ");
            params.put("courseStatus", CourseStatus.valueOf(miniClassCourseVo.getCourseStatus()));
        }
        
        if(StringUtils.isNotBlank(miniClassCourseVo.getSearchParam())){
        	hql.append(" and (mc.teacher.name like :searchParam0 ");
        	hql.append(" or mc.miniClass.name like :searchParam1 ");
        	hql.append(" or mc.subject.name like :searchParam2 ");
        	hql.append(" or mc.grade.name like :searchParam3 ");
        	hql.append(" or mc.studyHead.name like :searchParam4 ");
        	hql.append(")");
        	params.put("searchParam0", "%" + miniClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam1", "%" + miniClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam2", "%" + miniClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam3", "%" + miniClassCourseVo.getSearchParam() + "%");
        	params.put("searchParam4", "%" + miniClassCourseVo.getSearchParam() + "%");
        }
        //手机端按月查询 2015-07
        if (StringUtils.isNotBlank(miniClassCourseVo.getSearchMonth())) {
            hql.append(" and mc.courseDate like :searchMonth ");
            params.put("searchMonth", miniClassCourseVo.getSearchMonth() + "%");
        }
		
		//权限控制，老师只能看自己，学管只能看自己，如果是校区中的人，最多只能看到本校区的
		//Organization campus = userService.getBelongCampus();
		//hql.append(RoleCodeAuthoritySearchUtil.getMiniClassCourseRoleCodeAuthority(userService.getCurrentLoginUser(), campus));
		User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(miniClassCourseVo.getCurrentRoleId())){
			 hql.append(" and mc.teacher.userId='"+user.getUserId()+"'");
		} else if("classTeacherDeduction".equals(miniClassCourseVo.getCurrentRoleId())){
				hql.append(" and mc.studyHead.userId='"+user.getUserId()+"'");
//			 hql.append(" and miniClass.studyManeger.userId='"+user.getUserId()+"'");
		} else if ("mobileAttendance".equalsIgnoreCase(miniClassCourseVo.getCurrentRoleId())) {
			hql.append(" and (mc.miniClass.teacher.userId='"+user.getUserId()+"' or miniClass.studyManeger.userId='"+user.getUserId()+"' ) ");
		// 用于手机端的查询
		}  else if ("studentRole".equalsIgnoreCase(miniClassCourseVo.getCurrentRoleId())) {
			System.out.println("studentRole is current role! ");
		} else {	
            if(!"courseSchedule".equals(miniClassCourseVo.getCurrentRoleId())){
                hql.append(" and 1=2 ");
            }
		}
        
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by mc."+dp.getSidx()+" "+dp.getSord());
		} else{
			hql.append(" order by mc.createTime desc ");
		}

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 小班课程列表
	 *//*
	@Override
	public DataPackage getMiniClassCourseList(MiniClassCourseVo miniClassCourseVo,
			DataPackage dp) {
		StringBuffer sql = new StringBuffer(" SELECT mcc.*, mcc.GRADE GRADE_ID, mcc.SUBJECT SUBJECT_ID, mcc.ATTENDANCE_PIC_NAME ATTENDACE_PIC_NAME ");
		StringBuffer sqlFrom = new StringBuffer(" FROM mini_class_course mcc ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudentId())) {
			sqlFrom.append(" ,MINI_CLASS_STUDENT_ATTENDENT msa");
			sqlWhere.append(" AND msa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID and msa.STUDENT_ID = '"+miniClassCourseVo.getStudentId()+"' ");
		}
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getStartDate())) {
			sqlWhere.append(" AND mcc.COURSE_DATE >= '" + miniClassCourseVo.getStartDate() + "' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getEndDate())) {
			sqlWhere.append(" AND mcc.COURSE_DATE <= '" + miniClassCourseVo.getEndDate() + "' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getMiniClassName())) {
			sqlWhere.append(" AND mcc.MINI_CLASS_NAME LIKE '%" + miniClassCourseVo.getMiniClassName() + "%' ");
		}
		if(miniClassCourseVo.getAuditStatus() != null && StringUtils.isNotBlank(miniClassCourseVo.getAuditStatus().toString())){
			sqlWhere.append(" AND mcc.AUDIT_STATUS = '" + miniClassCourseVo.getAuditStatus() + "' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getBlCampusName())) {
			if (sqlFrom.indexOf("mini_class mc") < 0) {
				sql.append(" , org.`NAME` BL_CAMPUS_NAME ");
				sqlFrom.append(" , mini_class mc, organization org ");
				sqlWhere.append(" AND mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID AND mc.BL_CAMPUS_ID = org.ID ");
			} else {
				sql.append(" , org.`NAME` BL_CAMPUS_NAME ");
				sqlFrom.append(" , organization org ");
				sqlWhere.append(" AND mc.BL_CAMPUS_ID = org.ID ");
			}
			sqlWhere.append("  AND org.`NAME` LIKE '%" + miniClassCourseVo.getBlCampusName() + "%' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getTeacherName())) {
			sql.append(" , u_teacher.`NAME` TEACHER_NAME, u_teacher.CONTACT TEACHER_MOBILE ");
			sqlFrom.append(" , user u_teacher ");
			sqlWhere.append(" AND mcc.TEACHER_ID = u_teacher.USER_ID ");
			sqlWhere.append(" AND u_teacher.`NAME` LIKE '%" + miniClassCourseVo.getTeacherName() + "%' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudyManegerName())) {
			if (sqlFrom.indexOf("mini_class mc") < 0) {
				sqlFrom.append(" , mini_class mc, user u_study_mc ");
				sqlWhere.append(" AND mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID AND mc.STUDY_MANEGER_ID = u_study_mc.USER_ID ");
			} else {
				sqlFrom.append(" , user u_study_mc ");
				sqlWhere.append(" AND mc.STUDY_MANEGER_ID = u_study_mc.USER_ID ");
			}
			sqlWhere.append(" AND u_study_mc.`NAME` LIKE '%" + miniClassCourseVo.getStudyManegerName() + "%' ");
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getStudentId())) {
			sqlWhere.append(" and mcc.MINI_CLASS_ID in (SELECT MINI_CLASS_ID FROM mini_class_student WHERE STUDENT_ID = '" + miniClassCourseVo.getStudentId() + "')");
		}
        if (StringUtils.isNotBlank(miniClassCourseVo.getTeacherId())) {
        	sqlWhere.append(" AND mcc.TEACHER_ID = '" + miniClassCourseVo.getTeacherId() + "' ");
        }
        
        if (StringUtils.isNotBlank(miniClassCourseVo.getCourseStatus())) {
        	sqlWhere.append(" AND mcc.COURSE_STATUS = '" + miniClassCourseVo.getCourseStatus() + "' ");
        }
        
        if(StringUtils.isNotBlank(miniClassCourseVo.getSearchParam())){
        	if (sqlFrom.indexOf("u_teacher") < 0) {
        		sql.append(" , u_teacher.`NAME` TEACHER_NAME, u_teacher.CONTACT TEACHER_MOBILE ");
    			sqlFrom.append(" , user u_teacher ");
    			sqlWhere.append(" AND mcc.TEACHER_ID = u_teacher.USER_ID ");
        	}
    		sql.append(" , u_study.`NAME` STUDY_MANEGER_NAME, u_study.CONTACT STUDY_MANEGER_MOBILE, dd_g.`NAME` GRADE_NAME, dd_s.`NAME` SUBJECT_NAME ");
			sqlFrom.append(" , user u_study, data_dict dd_g, data_dict dd_s ");
			sqlWhere.append(" AND mcc.STUDY_MANEGER_ID = u_study.USER_ID AND dd_g.ID = mcc.GRADE  AND dd_s.ID = mcc.SUBJECT ");
			
        	sqlWhere.append(" AND (u_teacher.`NAME` LIKE '%" + miniClassCourseVo.getSearchParam() + "%' ");
        	sqlWhere.append(" OR mcc.MINI_CLASS_NAME LIKE '%" + miniClassCourseVo.getSearchParam() + "%' ");
        	sqlWhere.append(" OR dd_s.`NAME` LIKE '%" + miniClassCourseVo.getSearchParam() + "%'");
        	sqlWhere.append(" OR dd_g.`NAME` LIKE '%" + miniClassCourseVo.getSearchParam() + "%'");
        	sqlWhere.append(" OR u_study.`NAME` LIKE '%" + miniClassCourseVo.getSearchParam() + "%'");
        	sqlWhere.append(")");
        }
        //手机端按月查询 2015-07
        if (StringUtils.isNotBlank(miniClassCourseVo.getSearchMonth())) {
        	sqlWhere.append(" AND mcc.COURSE_DATE LIKE '" + miniClassCourseVo.getSearchMonth() + "%' ");
        }
		
		//权限控制，老师只能看自己，学管只能看自己，如果是校区中的人，最多只能看到本校区的
		User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(miniClassCourseVo.getCurrentRoleId())){
        	sqlWhere.append(" and mcc.TEACHER_ID = '"+user.getUserId()+"'");
		} else if("classTeacherDeduction".equals(miniClassCourseVo.getCurrentRoleId())){
			sqlWhere.append(" and mcc.STUDY_MANEGER_ID = '"+user.getUserId()+"'");
//			 hql.append(" and miniClass.studyManeger.userId='"+user.getUserId()+"'");
		} else if ("mobileAttendance".equalsIgnoreCase(miniClassCourseVo.getCurrentRoleId())) {
			sqlWhere.append(" and mcc.TEACHER_ID = '"+user.getUserId()+"' OR mcc.STUDY_MANEGER_ID = '"+user.getUserId()+"'");
//			sqlWhere.append(" and (mc.miniClass.teacher.userId='"+user.getUserId()+"' or miniClass.studyManeger.userId='"+user.getUserId()+"' ) ");
		// 用于手机端的查询
		}  else if ("studentRole".equalsIgnoreCase(miniClassCourseVo.getCurrentRoleId())) {
			System.out.println("studentRole is current role! ");
		} else {	
            if(!"courseSchedule".equals(miniClassCourseVo.getCurrentRoleId())){
            	sqlWhere.append(" and 1=2 ");
            }
		}
        
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			if ("blCampusName".equals(dp.getSidx())) {
				if (sqlFrom.indexOf("mini_class mc") < 0) {
					sqlFrom.append(" , mini_class mc ");
					sqlWhere.append(" AND mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
				}
				sqlWhere.append(" ORDER BY mc.BL_CAMPUS_ID "+dp.getSord());
			} else {
				sqlWhere.append(" ORDER BY mcc."+dp.getSidx()+" "+dp.getSord());
			}
		} else{
			sqlWhere.append(" ORDER BY mcc.CREATE_TIME DESC ");
		}

		sql.append(sqlFrom).append(sqlWhere);
		return jdbcTemplateDao.queryPage(sql.toString(), MiniClassCourseJdbc.class, dp, true);
	}*/
	
	@Override
	public DataPackage getMiniClassCourseListForMobile(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
	

		StringBuffer hql2 = new StringBuffer();
		hql.append(" select  mc.* from mini_class_course mc ");
		hql.append(" left join  user teacher on mc.TEACHER_ID=teacher.USER_ID  ");
		hql.append(" left join  data_dict dd1 on mc.GRADE=dd1.ID  ");
		hql.append(" left join  data_dict dd2 on mc.SUBJECT=dd2.ID  ");
		hql.append(" left join user study  on mc.STUDY_MANEGER_ID=study.USER_ID  ");
		hql.append("  where 1=1 ");
		
		hql2.append(" select  mc.* from mini_class_course mc ");
		hql2.append(" left join  user teacher on mc.TEACHER_ID=teacher.USER_ID  ");
		hql2.append(" left join  data_dict dd2 on mc.SUBJECT=dd2.ID  ");
		hql2.append(" left join user study  on mc.STUDY_MANEGER_ID=study.USER_ID  ");
		hql2.append("  where 1=1 ");
		
		
		if (StringUtils.isNotBlank(miniClassCourseVo.getStartDate())) {
			hql.append("  and mc.COURSE_DATE>= :startDate ");
			hql2.append("  and mc.COURSE_DATE>= :startDate ");
			params.put("startDate",miniClassCourseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(miniClassCourseVo.getEndDate())) {
			hql.append("  and mc.COURSE_DATE<= :endDate ");
			hql2.append("  and mc.COURSE_DATE<= :endDate ");
			params.put("endDate",miniClassCourseVo.getEndDate());
		}
        
        if (StringUtils.isNotBlank(miniClassCourseVo.getCourseStatus())) {
            hql.append(" and mc.course_status = :courseStatus ");
            hql2.append(" and mc.course_status = :courseStatus ");
            params.put("courseStatus",miniClassCourseVo.getCourseStatus());
        }
        
        User user=userService.getCurrentLoginUser();
        if ("teacherAttendance".equals(miniClassCourseVo.getCurrentRoleId())){
			 hql.append(" and mc.teacher_id= :teacherId ");
			 hql2.append(" and mc.teacher_id= :teacherId ");
			 params.put("teacherId",user.getUserId());
		} else if("classTeacherDeduction".equals(miniClassCourseVo.getCurrentRoleId())){
			 hql.append(" and mc.STUDY_MANEGER_ID= :studyManagerId ");
			 hql2.append(" and mc.STUDY_MANEGER_ID= :studyManagerId ");
			 params.put("studyManagerId",user.getUserId());
		}
        
        if(StringUtils.isNotBlank(miniClassCourseVo.getSearchParam())){
        	hql.append(" and (teacher.name like :teacherName1 ");
        	hql.append(" or mc.MINI_CLASS_NAME like :miniClassName1 ");
        	hql.append(" or dd2.name like :dd2Name1 ");
        	hql.append(" or dd1.name like :dd1Name ");
        	hql.append(" or study.name like :studyName1 ");
        	hql.append(") union ");
        	hql.append(hql2);
        	hql.append(" and (teacher.name like :teacherName2 ");
        	hql.append(" or mc.MINI_CLASS_NAME like :miniClassName2 ");
        	hql.append(" or dd2.name like :dd2Name2 ");
        	hql.append(" or study.name like :studyName2 ");
        	hql.append(")");
        	params.put("teacherName1","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("miniClassName1","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("dd2Name1","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("dd1Name","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("studyName1","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("teacherName2","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("miniClassName2","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("dd2Name2","%"+miniClassCourseVo.getSearchParam()+"%");
        	params.put("studyName2","%"+miniClassCourseVo.getSearchParam()+"%");
        }
		
		
        
		hql.append(" order by create_Time desc ");
			
			

		return super.findPageBySql(hql.toString(), dp,true,params);
	}
	
	/**
	 * 获取某课程时间段的老师课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	public DataPackage getTeacherMiniClassCourseScheduleListByCourseTime(String teacherId, String start, String end, 
			String courseStartTime, String courseEndTime, DataPackage dp) {
		String sql = "select * from mini_class_course ";
		String whereSql = " where 1=1 ";
		Map<String, Object> params = Maps.newHashMap();
		
		if (StringUtils.isNotBlank(teacherId)) {
			whereSql += "and TEACHER_ID = :teacherId ";
			params.put("teacherId", teacherId);
		}
		if (StringUtils.isNotBlank(start)) {
			whereSql += "and COURSE_DATE >= :courseDateStart ";
			params.put("courseDateStart", start);
		}
		if (StringUtils.isNotBlank(end)) {
			whereSql += "and COURSE_DATE <= :courseDateEnd ";
			params.put("courseDateEnd", end);
		}
		if (StringUtils.isNotBlank(courseStartTime) && StringUtils.isNotBlank(courseEndTime)) {
			whereSql += "and UNIX_TIMESTAMP(CONCAT(:start1, COURSE_TIME)) >= "
					+ "UNIX_TIMESTAMP( :courseStartTime1 ) AND UNIX_TIMESTAMP(CONCAT(:start2 , COURSE_TIME)) "
					+ "< UNIX_TIMESTAMP( :courseDateEnd1 ) ";
			params.put("start1", start);
			params.put("start2", start);
			params.put("courseStartTime1", start+" "+courseStartTime);
			params.put("courseDateEnd1", start+" "+courseEndTime);
			
		}
		sql += whereSql + "ORDER BY COURSE_DATE";
		
		return this.findPageBySql(sql, dp,true,params);
	}
	
	/**
	 * 删除小班课程
	 */
	@Override
	public void deleteMiniClassCourse(String miniClassId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		hql.append(" delete MiniClassCourse  where 1=1 ");
		hql.append(" and  miniClass.miniClassId = :miniClassId ");
		
		StringBuffer mcsaSql = new StringBuffer();
		
		mcsaSql.append(" delete mcsa FROM MINI_CLASS_STUDENT_ATTENDENT mcsa ");
		mcsaSql.append(" inner join mini_class_course mcs on mcsa.MINI_CLASS_COURSE_ID=mcs.MINI_CLASS_COURSE_ID where mcs.MINI_CLASS_ID = :miniClassId ");
		mcsaSql.append(" and mcsa.CHARGE_STATUS = 'UNCHARGE' ");
		excuteSql(mcsaSql.toString(),params);
		
		excuteHql(hql.toString(),params);
	}
	
	/**
	 * 通过小班ID查出所有小班课程，按课程时间倒序
	 */
	public List<MiniClassCourse> getMiniClassCourseListByMiniClassId(String miniClassId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		
		hql.append(" from MiniClassCourse  where 1=1 ");
		
		if (StringUtils.isNotBlank(miniClassId)) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		
		hql.append(" order by courseDate asc ");

		List<MiniClassCourse> list=this.findAllByHQL(hql.toString(),params);
		return list;
	}
	
	/**
	 * 通过小班ID查出所有小班课程，按课程时间倒序，返回类型为DataPackage
	 */
	public DataPackage getMiniClassCourseListByMiniClassId(String miniClassId, String firstSchoolDate, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		
		Map<String, Object> params = Maps.newHashMap();
		
		hql.append(" from MiniClassCourse  where 1=1 ");
		
		if (StringUtils.isNotBlank(miniClassId)) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassId);
		}
		if (StringUtils.isNotBlank(firstSchoolDate)) {
			hql.append(" and courseDate >= :firstSchoolDate ");
			params.put("firstSchoolDate", firstSchoolDate);
		}
		
		hql.append(" order by courseDate asc ");

		return super.findPageByHQL(hql.toString(), dp,true,params);
	}
	
	public String getTop1MiniClassCourseByDate(String courseDate,String miniClassId){
		DataPackage dataPackage = new DataPackage(0,1);
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Expression.eq("miniClass.miniClassId", miniClassId));
		criterionList.add(Expression.gt("courseDate", courseDate));
		List<Order> scoreOrderList = new ArrayList<Order>();
		scoreOrderList.add(Order.asc("courseDate")) ;
		dataPackage= super.findPageByCriteria(dataPackage, scoreOrderList, criterionList);
		if(dataPackage.getDatas()!=null && dataPackage.getDatas().size()>0){
			List<MiniClassCourse> list=(List<MiniClassCourse>) dataPackage.getDatas();
			return list.get(0).getCourseDate();
		}
		return null; 
	}
	
	
	/**
	 * 获取小班课程时间段内占用教室列表
	 */
	@Override
	public List getMiniClassCourseUseClassroomList(String blCampusId,String startDate, String endDate, String classroomName, String miniClassName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.CLASSROOM_ID classroom from mini_class_course a ");
		sql.append(" inner join mini_class b on a.MINI_CLASS_ID = b.MINI_CLASS_ID ");
		sql.append(" where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(blCampusId)) {
			sql.append(" and b.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId",blCampusId );
		}
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" and a.COURSE_Date >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" and a.COURSE_Date <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(classroomName)) {
			sql.append(" and a.CLASSROOM_ID in (select id from CLASSROOM_MANAGE where CLASS_ROOM like :classroomName ) ");
			params.put("classroomName", "%"+classroomName+"%");
		}
		if (StringUtils.isNotBlank(miniClassName)) {
			sql.append(" and b.NAME like :miniClassName ");
			params.put("miniClassName","%"+miniClassName+"%" );
		}

		// 权限控制
		sql.append(roleQLConfigService.getValueResult("小班课程时间段内占用教室列表","sql"));
		
		sql.append(" group by a.CLASSROOM_ID ");
		
		List<Map<Object,Object>> list = findMapBySql(sql.toString(),params);
		return list;
	}
	
	/**
	 * 获取小班教室的小班课程
	 */
	@Override
	public List getClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName,String campusId,String miniClassTypeId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.*, b.*, c.NAME TEACHER_NAME,cm.CLASS_ROOM classroomCm from mini_class_course a ");
		sql.append(" left join mini_class b on a.MINI_CLASS_ID = b.MINI_CLASS_ID ");
		sql.append(" left join product p on p.ID = b.PRODUCE_ID ");
		sql.append(" left join `user` c on c.USER_ID = a.TEACHER_ID ");
		sql.append(" left join CLASSROOM_MANAGE cm on cm.ID = a.CLASSROOM_ID ");
		sql.append(" where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" and a.COURSE_Date >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" and a.COURSE_Date <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(classroomName)) {
			sql.append(" and cm.CLASS_ROOM like :classroomName ");
			params.put("classroomName", "%"+classroomName+"%");
		}
		if (StringUtils.isNotBlank(miniClassName)) {
			sql.append(" and b.NAME like :miniClassName ");
			params.put("miniClassName", "%"+miniClassName+"%");
		}
		if(StringUtils.isNotBlank(campusId)){
			sql.append(" and b.bl_campus_id = :campusId ");
			params.put("campusId", campusId);
		}

		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=StringUtil.replaceSpace(miniClassTypeId).split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID = :miniClassTypes0 ");
				params.put("miniClassTypes0", miniClassTypes[0]);
				for (int i = 1; i < miniClassTypes.length; i++) {
					sql.append(" or p.CLASS_TYPE_ID = :miniClassTypes"+i+" " );
					params.put("miniClassTypes"+i, miniClassTypes[i]);
				}
				sql.append(" )");
			}
		}
		
		//权限控制
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班教室的小班课程","sql","b.BL_CAMPUS_ID"));
		
		List<Map<Object,Object>> list = findMapBySql(sql.toString(),params);
		return list;
	}
	public List<MiniClassCourse> getClassroomMiniClassCoursePlus(String startDate, String endDate, String classroomName, String miniClassName) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder sbHql = new StringBuilder();
		sbHql.append(" from MiniClassCourse where miniClass.classroom is not null");
		if(StringUtils.isNotBlank(startDate)){
			sbHql.append(" and courseDate >= :startDate ");
			params.put("startDate", startDate);
		}
		if(StringUtils.isNotBlank(endDate)){
			sbHql.append(" and courseDate <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(classroomName)) {
			sbHql.append(" and miniClass.classroom.classroom like :classroomName ");
			params.put("classroomName", "%"+classroomName+"%");
		}
		if (StringUtils.isNotBlank(miniClassName)) {
			sbHql.append(" and miniClass.name like :miniClassName ");
			params.put("miniClassName", "%"+miniClassName+"%");
		}
		List<MiniClassCourse> list = this.findAllByHQL(sbHql.toString(),params);				
		
		return list;
	}	
	/**
	 * 查询哪个小班课程未安排教室
	 */
	@Override
	public List getNotArrangementClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from  mini_class b  ");
		sql.append(" left JOIN mini_class_course a on b.MINI_CLASS_ID = a.MINI_CLASS_ID ");
		sql.append(" left join `user` c on c.USER_ID = b.TEACHER_ID ");
		sql.append(" where 1=1 ");
		sql.append(" and (b.CLASSROOM is null or b.CLASSROOM = '') ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(startDate)) {
			sql.append(" and a.COURSE_Date >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			sql.append(" and a.COURSE_Date <= :endDate ");
			params.put("endDate", endDate);
		}
		if (StringUtils.isNotBlank(classroomName)) {
			sql.append(" and b.CLASSROOM like :classroomName ");
			params.put("classroomName", "%"+classroomName+"%");
		}
		if (StringUtils.isNotBlank(miniClassName)) {
			sql.append(" and b.NAME like :miniClassName ");
			params.put("miniClassName", "%"+miniClassName+"%");
		}
		
		// 权限控制
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("小班教室的小班课程","sql","b.BL_CAMPUS_ID"));
		
		sql.append(" GROUP BY b.MINI_CLASS_ID ");
		
		List<Map<Object,Object>> list = findMapBySql(sql.toString(),params);
		return list;
	}
	
	
	/**
	 * 小班课程详情
	 */
	public DataPackage getMiniClassCourseDetail(String miniClassId, DataPackage dp,ModelVo modelVo) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from MiniClassCourse  where 1=1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(miniClassId)) {
			hql.append(" and miniClass.miniClassId = :miniClassId ");
			params.put("miniClassId", miniClassId);
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
	
	@Override
	public void updateMiniClassCourse(MiniClass miniClass) throws Exception{
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer hql = new StringBuffer();
		String courseTime="";
		String courseEndTime="";
		if(miniClass.getClassTime()!=null && miniClass.getEveryCourseClassNum()!=null && miniClass.getClassTimeLength()!=null){
			courseTime=miniClass.getClassTime();
			Double dou= miniClass.getEveryCourseClassNum()*miniClass.getClassTimeLength().doubleValue();
			courseEndTime=DateTools.dateConversString(DateTools.add(DateTools.getDateTime("2012-01-01 "+miniClass.getClassTime()), Calendar.MINUTE,dou.intValue()), "HH:mm");
		}
		hql.append(" update MiniClassCourse set miniClassName= :miniClassName ,courseTime= :courseTime ,courseEndTime= :courseEndTime " +
				",courseHours= :courseHours ,SUBJECT= :subject  ");
		params.put("miniClassName", miniClass.getName());
		params.put("courseTime", courseTime);
		params.put("courseEndTime", courseEndTime);
		params.put("courseHours", miniClass.getEveryCourseClassNum());
		params.put("subject", miniClass.getSubject().getId());
		if(miniClass.getGrade()!=null){//现在逻辑允许为空
			hql.append(",GRADE= :grade ");
			params.put("grade", miniClass.getGrade().getId());
		}else{
			hql.append(",GRADE= :grade ");
			params.put("grade", null+" ");
		}
		if(miniClass.getTeacher() != null){
			hql.append(",teacher.userId= :teacherId ");
		    params.put("teacherId", miniClass.getTeacher().getUserId());
		}
		if(miniClass.getStudyManeger() != null){
			hql.append(",studyHead.userId= :studyManagerId ");
		    params.put("studyManagerId", miniClass.getStudyManeger().getUserId());
		}
		hql.append(" where miniClass.miniClassId = :miniClassId ");
		hql.append(" and courseDate > :courseDate ");
		params.put("miniClassId", miniClass.getMiniClassId());
		params.put("courseDate", DateTools.getCurrentDate());
		this.excuteHql(hql.toString(),params);
	}

	@Override
	public Double findCountClassHours(String miniClassId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("miniClassId", miniClassId);
		String hql=" select sum(courseHours) from MiniClassCourse where miniClass.miniClassId= :miniClassId and courseStatus <> 'CANCEL' " ;
		return this.findSumHql(hql,params);
	}

	
	@Override
	public List<MiniClassCourse> getMiniClassCourse4Class(String sbHql) {
		Map<String, Object> params = Maps.newHashMap();
		return this.findAllByHQL(sbHql,params);
	}

	/**
	 * 保存小班信息时，自动修改所有跟此班关联的课室为此次选定的课室
	 * @param miniClass
	 */
	@Override
	public void saveClassroom4itsClass(MiniClass miniClass) {
		Map<String, Object> params = Maps.newHashMap();
		String day = DateTools.getCurrentDateTime();
		String classroomId = null;
		if(miniClass.getClassroom() != null){
			classroomId = miniClass.getClassroom().getId();
		}
		StringBuilder sbSql = new StringBuilder();
		if(classroomId != null){
//			sbSql.append(" UPDATE mini_class_course set CLASSROOM_ID=NULL ");
//		}else{
			sbSql.append(" UPDATE mini_class_course set CLASSROOM_ID= :classroomId ");
			params.put("classroomId", classroomId);
			sbSql.append(" WHERE mini_class_id= :miniclassId ").append(" and COURSE_DATE > :day ");
			params.put("miniclassId", miniClass.getMiniClassId());
			params.put("day", day);
			this.excuteSql(sbSql.toString(),params);
		}
	}

	/**
	 * 根据小班id获取排课的最大最小值
	 */
    @Override
    public Map<Object, Object> getMaxMinCourseDateByMiniClass(String miniClassId) {
        List<Map<Object, Object>> list = null;
        Map<Object, Object> result = null;
        Map<String, Object> params = Maps.newHashMap();
        StringBuffer sql = new StringBuffer("select max(CONCAT(COURSE_DATE, ' ', COURSE_TIME, ':00')) maxCourseDate, min(CONCAT(COURSE_DATE, ' ', COURSE_TIME, ':00')) minCourseDate from mini_class_course ");
        sql.append(" where COURSE_STATUS != 'CANCEL' and MINI_CLASS_ID = :miniClassId ");
        params.put("miniClassId", miniClassId);
        list = this.findMapBySql(sql.toString(), params);
        if (list != null && list.size() > 0) {
            result = list.get(0);
        }
        return result;
    }

    @Override
	public List<MiniClassCourse> findAllUnChargeMiniClassCourse(String miniClassId, CourseStatus status) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MiniClassCourse where courseStatus<>'CANCEL' and courseStatus<>'CHARGED'");
		hql.append( " and miniClass.miniClassId=:miniClassId");
		params.put("miniClassId",miniClassId);
		if(status!=null){
			hql.append(" and courseStatus = :status");
			params.put("status",status);
		}
		return this.findAllByHQL(hql.toString(),params);
	}

    @Override
    public List getMiniClassCourseDateInfo(String miniClassId) {
    	String sql = " select course_num courseNum,course_date courseDate,course_status courseStatus,COURSE_HOURS courseHours from mini_class_course where mini_class_id = :miniClassId and course_status<>'CANCEL' order by course_num";
    	Map<String,Object> params= Maps.newHashMap();

    	params.put("miniClassId",miniClassId);

        return this.findMapBySql(sql,params);
    }


	@Override
	public List<MiniClassCourse> findAllEnableMiniClassCourse(String miniClassId) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuilder hql = new StringBuilder();
		hql.append(" from MiniClassCourse where courseStatus<>'CANCEL'");
		hql.append( " and miniClass.miniClassId=:miniClassId");
		params.put("miniClassId",miniClassId);
		hql.append(" order by courseDate ,courseTime ");
		return this.findAllByHQL(hql.toString(),params);
	}

}
