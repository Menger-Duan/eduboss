package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.eduboss.common.AuditStatus;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.OrganizationType;
import com.eduboss.common.ProductType;
import com.eduboss.common.RoleCode;
import com.eduboss.dao.CourseBusinessDao;
import com.eduboss.dao.CourseConflictDao;
import com.eduboss.dao.CourseDao;
import com.eduboss.dao.DataDictDao;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.ProductDao;
import com.eduboss.dao.TeacherSubjectDao;
import com.eduboss.dao.UserDao;
import com.eduboss.dao.UserOrganizationDao;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseBusiness;
import com.eduboss.domain.DataDict;
import com.eduboss.domain.Organization;
import com.eduboss.domain.Product;
import com.eduboss.domain.TeacherSubject;
import com.eduboss.domain.User;
import com.eduboss.domain.UserOrganization;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.dto.CourseChangesSearchInputVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.UserService;
import com.eduboss.utils.CalculateUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Repository("CourseDao")
public class CourseDaoImpl extends GenericDaoImpl<Course, String> implements
		CourseDao {
	
	@Autowired
	private HibernateTemplate hibernateTemplate;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DataDictDao dataDictDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private TeacherSubjectDao teacherSubjectDao;
	
	@Autowired
	private CourseConflictDao courseConflictDao;

    @Autowired
    private RoleQLConfigService roleQLConfigService;
    
    @Autowired
    private OrganizationDao organizationDao;
    
    @Autowired
    private UserOrganizationDao userOrganizationDao;
    
    @Autowired
    private CourseBusinessDao courseBusinessDao;
    
    @Autowired
    private JdbcTemplateDao jdbcTemplateDao;
    
    @Override
    public void save(Course course) {
		String courseDate = course.getCourseDate();
		String courseTime = course.getCourseTime().substring(0, 5);
		BigDecimal courseMinutes = course.getCourseMinutes();
		BigDecimal planHours = course.getPlanHours();
		CalculateUtil.calCourseTimeBetweenUnExpectTime(courseDate+" "+courseTime, courseMinutes, planHours.doubleValue());
    	if (StringUtil.isBlank(course.getCourseId())) {
			CourseBusiness business = new CourseBusiness();
			courseBusinessDao.save(business);
			String businessId = "";
			if (business.getId() > 0) {
				businessId += "COU" + StringUtil.numberPadding0(business.getId(), 10);
			}
			course.setCourseId(businessId);
		}
		super.save(course);
    }
	
	/**
	 * 课程列表
	 */
	@Override
	public DataPackage getCourseList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {

		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			criterionList.add(Restrictions.eq("courseSummary.courseSummaryId",
					searchInputVo.getCourseSummaryId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			criterionList.add(Restrictions.ge("courseDate",
					searchInputVo.getStartDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			criterionList.add(Restrictions.le("courseDate",
					searchInputVo.getEndDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			criterionList.add(Restrictions.eq("student.id",
					searchInputVo.getStudentId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			criterionList.add(Restrictions.like("student.name",
					searchInputVo.getStudentName(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			criterionList.add(Restrictions.eq("teacher.userId",
					searchInputVo.getTeacherId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			criterionList.add(Restrictions.like("teacher.name",
					searchInputVo.getTeacherName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			criterionList.add(Restrictions.eq("grade.id",
					searchInputVo.getGrade()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			criterionList.add(Restrictions.eq("subject.id",
					searchInputVo.getSubject()));
		}
		if (searchInputVo.getCourseStatus() != null) {
			criterionList.add(Restrictions.eq("courseStatus",
					searchInputVo.getCourseStatus()));
		}
		
		//权限控制，老师只能看自己，如果是校区中的人，最多只能看到本校区的
		Organization campus = userService.getBelongCampus();
		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
			criterionList.add(Restrictions.eq("student.blCampusId", campus.getId()));
		}
//		if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
//			criterionList.add(Restrictions.eq("teacher.userId", userService.getCurrentLoginUser().getUserId()));
//		}
		
		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "createTime", "desc"),
				criterionList);
	}

	/**
	 * 学生课程表 - 老师学生请假不做查询，除非是传入了课程状态的查询
	 * 以上作废
	 * 功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentCourseScheduleList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		
		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			criterionList.add(Restrictions.eq("courseSummary.courseSummaryId",
					searchInputVo.getCourseSummaryId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			criterionList.add(Restrictions.ge("courseDate",
					searchInputVo.getStartDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			criterionList.add(Restrictions.le("courseDate",
					searchInputVo.getEndDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			criterionList.add(Restrictions.eq("student.id",
					searchInputVo.getStudentId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			criterionList.add(Restrictions.like("student.name",
					searchInputVo.getStudentName(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			criterionList.add(Restrictions.eq("teacher.userId",
					searchInputVo.getTeacherId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			criterionList.add(Restrictions.like("teacher.name",
					searchInputVo.getTeacherName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			criterionList.add(Restrictions.eq("grade.id",
					searchInputVo.getGrade()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			criterionList.add(Restrictions.eq("subject.id",
					searchInputVo.getSubject()));
		}
		if (searchInputVo.getCourseStatus() != null) {
			criterionList.add(Restrictions.eq("courseStatus",
					searchInputVo.getCourseStatus()));
		}
		//功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
//		else{//老师学生请假不做查询，除非是传入了课程状态的查询
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.STUDENT_LEAVE));
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.TEACHER_LEAVE));
//		}
		
		//权限控制，老师只能看自己，如果是校区中的人，最多只能看到本校区的
//		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			criterionList.add(Restrictions.eq("blCampusId.id", campus.getId()));
//		}
//
//		// 只能看到自己带的学生的数据，除了学管主管与校区主任的组织权限较大，所以可以看到全校区的
//		boolean onlyOwnStudent = false;
//		if (userService.isCurrentUserRoleCode(RoleCode.CAMPUS_DIRECTOR)
//				|| userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER_HEAD)
//				|| userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)) {
//			onlyOwnStudent = false;
//		} else if (userService.isCurrentUserRoleCode(RoleCode.STUDY_MANAGER)
//				|| userService.isCurrentUserRoleCode(RoleCode.CONSULTOR)) {// 只有这三种角色才允许带学生, 他们分别只能查到自己带的学生课程表
//			onlyOwnStudent = true;
//		}
//		if (onlyOwnStudent) {
//			criterionList.add(Restrictions.eq("student.studyManeger.userId",
//					userService.getCurrentLoginUser().getUserId()));
//		}
//		dp.setRoleQLConfigName("一对一学生课程列表");
		
		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "createTime", "desc"),
				criterionList);
	}

	/**
	 * 老师课程表 - 老师学生请假不做查询，除非是传入了课程状态的查询
	 * 以上作废
	 * 功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
	 * @param searchInputVo
	 * @param dp
	 * @return
	 *//*
	public DataPackage getTeacherCourseScheduleList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {

		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			criterionList.add(Restrictions.eq("courseSummary.courseSummaryId",
					searchInputVo.getCourseSummaryId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			criterionList.add(Restrictions.ge("courseDate",
					searchInputVo.getStartDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			criterionList.add(Restrictions.le("courseDate",
					searchInputVo.getEndDate()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			criterionList.add(Restrictions.eq("student.id",
					searchInputVo.getStudentId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			criterionList.add(Restrictions.like("student.name",
					searchInputVo.getStudentName(), MatchMode.ANYWHERE));
		}
		
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			criterionList.add(Restrictions.eq("teacher.userId",
					searchInputVo.getTeacherId()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			criterionList.add(Restrictions.like("teacher.name",
					searchInputVo.getTeacherName(), MatchMode.ANYWHERE));
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			criterionList.add(Restrictions.eq("grade.id",
					searchInputVo.getGrade()));
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			criterionList.add(Restrictions.eq("subject.id",
					searchInputVo.getSubject()));
		}
		if (searchInputVo.getCourseStatus() != null) {
			criterionList.add(Restrictions.eq("courseStatus",
					searchInputVo.getCourseStatus()));
		}
		////功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
//		else{//老师学生请假不做查询，除非是传入了课程状态的查询
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.STUDENT_LEAVE));
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.TEACHER_LEAVE));
//		}
		
		//权限控制，老师只能看自己，如果是校区中的人，最多只能看到本校区的
//		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			//criterionList.add(Restrictions.eq("student.blCampusId", campus.getId()));
//			criterionList.add(Restrictions.or(Restrictions.eq("student.blCampusId", campus.getId()), Restrictions.eq("teacher.userId", searchInputVo.getTeacherId())));
//		}
//		dp.setRoleQLConfigName("一对一课程列表"); // 自动附加权限控制配置
		
		// 注意，老师课程表里不能加老师只能看自己的条件，
		// 如果用户角色即是学管，又是老师。那么用户又想搜索其他老师的记录，那么就出错了
		
		return super.findPageByCriteria(dp,
				HibernateUtils.prepareOrder(dp, "createTime", "desc"),
				criterionList);
	}*/
	
	/**
	 * 老师课程表 - 老师学生请假不做查询，除非是传入了课程状态的查询
	 * 以上作废
	 * 功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getTeacherCourseScheduleList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql =  new StringBuffer(" SELECT c.* ");
		StringBuffer sqlFrom = new StringBuffer(" FROM course c, `user` u_teacher, student s  ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			sqlWhere.append(" AND c.COURSE_SUMMARY_ID = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			sqlWhere.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			sqlWhere.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			sqlWhere.append(" AND c.STUDENT_ID = :studentId  ");
			params.put("studentId", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			sqlWhere.append(" AND s.NAME LIKE :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			sqlWhere.append(" AND u_teacher.NAME = :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			sqlWhere.append(" AND c.GRADE = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			sqlWhere.append(" AND c.SUBJECT = :subject ");
			params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			sqlWhere.append(" AND c.COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus());
		}
		////功能优化 #4039 学生课程表查询不到学生请假或者老师请假的课程
//		else{//老师学生请假不做查询，除非是传入了课程状态的查询
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.STUDENT_LEAVE));
//			criterionList.add(Restrictions.ne("courseStatus",
//					CourseStatus.TEACHER_LEAVE));
//		}
		
		//权限控制，老师只能看自己，如果是校区中的人，最多只能看到本校区的
//		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			//criterionList.add(Restrictions.eq("student.blCampusId", campus.getId()));
//			criterionList.add(Restrictions.or(Restrictions.eq("student.blCampusId", campus.getId()), Restrictions.eq("teacher.userId", searchInputVo.getTeacherId())));
//		}
//		dp.setRoleQLConfigName("一对一课程列表"); // 自动附加权限控制配置
		
		// 注意，老师课程表里不能加老师只能看自己的条件，
		// 如果用户角色即是学管，又是老师。那么用户又想搜索其他老师的记录，那么就出错了
		sqlWhere.append(" ORDER BY c.CREATE_TIME DESC ");
		sql.append(sqlFrom).append(sqlWhere);
		return this.findPageBySql(sql.toString(), dp, true, params);
	}


	
	/**
	 * 老师课程表 - 所有课程状态查询，包括老师学生请假
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getTeacherCourseScheduleListAllCourseStatus(CourseSearchInputVo searchInputVo,
													DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql =  new StringBuffer(" SELECT c.*, c.ATTENDANCE_PIC_NAME ATTENDACE_PIC_NAME, ");
		sql.append(" s.`NAME` STUDENT_NAME, u_teacher.NAME TEACHER_NAME ");
		StringBuffer sqlFrom = new StringBuffer(" FROM course c, `user` u_teacher, student s  ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			sqlWhere.append(" AND c.COURSE_SUMMARY_ID = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			sqlWhere.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			sqlWhere.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			sqlWhere.append(" AND c.STUDENT_ID = :studentId ");
			params.put("studentId", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			sqlWhere.append(" AND s.NAME LIKE :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			sqlWhere.append(" AND u_teacher.NAME = :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			sqlWhere.append(" AND c.GRADE = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			sqlWhere.append(" AND c.SUBJECT = :subject ");
			params.put("subject", searchInputVo.getSubject());
		}
		//权限控制，老师只能看自己，如果是校区中的人，最多只能看到本校区的
//		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			//criterionList.add(Restrictions.eq("student.blCampusId", campus.getId()));
//			criterionList.add(Restrictions.or(Restrictions.eq("student.blCampusId", campus.getId()), Restrictions.eq("teacher.userId", searchInputVo.getTeacherId())));
//		}
//		dp.setRoleQLConfigName("一对一课程列表"); // 自动附加权限控制配置

		// 注意，老师课程表里不能加老师只能看自己的条件，
		// 如果用户角色即是学管，又是老师。那么用户又想搜索其他老师的记录，那么就出错了
		sqlWhere.append(" ORDER BY c.CREATE_TIME DESC ");
		sql.append(sqlFrom).append(sqlWhere);
		return this.findPageBySql(sql.toString(), dp, true, params);
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
	public DataPackage getTeacherCourseScheduleListByCourseTime(String teacherId, String start, String end, 
			String courseStartTime, String courseEndTime, DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = "select * from course ";
		String whereSql = " where 1=1 ";
		if (StringUtils.isNotBlank(teacherId)) {
			whereSql += "and TEACHER_ID = '" + teacherId + "' ";
		}
		if (StringUtils.isNotBlank(start)) {
			whereSql += "and COURSE_DATE >= '" + start + "' ";
		}
		if (StringUtils.isNotBlank(end)) {
			whereSql += "and COURSE_DATE <= '" + end + "' ";
		}
		if (StringUtils.isNotBlank(courseStartTime) && StringUtils.isNotBlank(courseEndTime)) {
			whereSql += "and UNIX_TIMESTAMP(CONCAT('" + start + " ',substr(COURSE_TIME,1,5))) >= "
					+ "UNIX_TIMESTAMP('" + start + " " + courseStartTime + "') AND UNIX_TIMESTAMP(CONCAT('" + start + " ',substr(COURSE_TIME,1,5))) "
					+ "< UNIX_TIMESTAMP('" + start + " " + courseEndTime + "') ";
		}
		sql += whereSql + "ORDER BY COURSE_DATE ";
		return this.findPageBySql(sql, dp, true, params);
	}
	

	
	/**
	 * 获取一对一批量考勤列表
	 */
	@Override
	public DataPackage getOneOnOneBatchAttendanceList(CourseVo courseVo,
			DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT c.* ";
		sql += " from course c, `user` u_teacher, `user` u_study, student s, product p, organization org ";
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.STUDY_MANAGER_ID = u_study.USER_ID ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		sqlWhere.append(" AND c.PRODUCT_ID = P.ID ");
		sqlWhere.append(" AND c.BL_CAMPUS_ID = org.ID ");
		if (StringUtils.isNotBlank(courseVo.getCourseDate())) {
			sqlWhere.append(" AND c.COURSE_DATE = :courseDate ");
			params.put("courseDate", courseVo.getCourseDate());
		}
		if (StringUtils.isNotBlank(courseVo.getStartDate())) {
			sqlWhere.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", courseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseVo.getEndDate())) {
			sqlWhere.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", courseVo.getEndDate());
		}
		if (StringUtils.isNotBlank(courseVo.getStudentName())) {
			sqlWhere.append(" AND s.`NAME` LIKE :studentName ");
			params.put("studentName", "%"+courseVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(courseVo.getTeacherName())) {
			sqlWhere.append("AND  u_teacher.`NAME` like :teacherName ");
			params.put("teacherName", "%"+courseVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(courseVo.getTeacherId())) {
			sqlWhere.append("AND  c.TEACHER_ID = :teacherId ");
			params.put("teacherId", courseVo.getTeacherId());
		}
        if (StringUtils.isNotBlank(courseVo.getStudyManagerName())){
        	sqlWhere.append("AND u_study.`NAME` like  :studyManagerName ");
        	params.put("studyManagerName", "%"+courseVo.getStudyManagerName()+"%");
        }
		if (courseVo.getCourseStatus() != null) {
			sqlWhere.append("AND  c.COURSE_STATUS =  :courseStatus ");
			params.put("courseStatus", courseVo.getCourseStatus());
		}
		//增加考勤类型的条件查询
		if(courseVo.getCourseAttenceType()!=null){
			sqlWhere.append(" and c.COURSE_ATTENCE_TYPE ='"+courseVo.getCourseAttenceType().getValue()+"' ");
		}
		
		
		if (courseVo.getAuditStatus() != null) {
			if (courseVo.getAuditStatus() == AuditStatus.UNAUDIT) {
				sqlWhere.append(" AND (c.AUDIT_STATUS is null or c.AUDIT_STATUS='UNAUDIT') ");
			} else {
				sqlWhere.append(" AND c.AUDIT_STATUS = :auditStatus ");
				params.put("auditStatus", courseVo.getAuditStatus());
			}
		}
		
		if (StringUtils.isNotBlank(courseVo.getSubject())) {

			String[] subjects=StringUtil.replaceSpace(courseVo.getSubject()).split(",");
			if(subjects.length>0){

//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sqlWhere.append(" OR c.SUBJECT ='"+subjects[i]+"' ");
//					}
//				}
				sqlWhere.append(" and c.SUBJECT in (:subjects) ");
				params.put("subjects", subjects);

			}
		}
		if(StringUtils.isNotBlank(courseVo.getCampusId())){
			sqlWhere.append(" AND c.BL_CAMPUS_ID= :campusId ");
			params.put("campusId", courseVo.getCampusId());
		}
		
        if("teacherAttendance".equals(courseVo.getCurrentRoleId())){
        	sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
        	params.put("teacherId", userService.getCurrentLoginUser().getUserId());
		}else if("studyManegerVerify".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.STUDY_MANAGER_ID = :studyMangerId  ");
			params.put("studyMangerId", userService.getCurrentLoginUser().getUserId());
			Organization org=userService.getCurrentLoginUserOrganization();
			if(org.getOrgType()==OrganizationType.DEPARTMENT){
				//当前登录人是部门，取到上一级组织id
				Organization o=organizationDao.findById(org.getParentId());
				sqlWhere.append(" AND ( c.BL_CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", o.getId());
				List<Organization> uolist=userService.getCurrentLoginUser().getOrganization();
				for(Organization orgs:uolist){
					sqlWhere.append(" OR c.BL_CAMPUS_ID = :userBlCampusId ");
					params.put("userBlCampusId", orgs.getId());
				}
				sqlWhere.append(" ) ");
			}else{
				sqlWhere.append(" AND c.BL_CAMPUS_ID in (select ORGANIZATION_ID from USER_ORGANIZATION_role WHERE USER_ID = :userId )");
				params.put("userId", userService.getCurrentLoginUser().getUserId());
			}			
		}else if("classTeacherDeduction".equals(courseVo.getCurrentRoleId())
				&&  userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)) {
			Organization belongCampus = userService.getBelongCampus();
			sqlWhere.append(" AND c.BL_CAMPUS_ID = :belongCampusId ");
			params.put("belongCampusId", belongCampus.getId());
		} else if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
			if (StringUtils.isNotBlank(courseVo.getCampusId())) {
				sqlWhere.append(" AND  c.BL_CAMPUS_ID = :courseCampusId ");
				params.put("courseCampusId", courseVo.getCampusId());
			}
			
		} else{
			sqlWhere.append(" and 1=2");
		}
		
		sql += sqlWhere.toString();
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {//老师帐号是按字母新建的，所以按老师名字排序可以用帐号来排
			sql+=" ORDER BY c."+dp.getSidx()+" "+dp.getSord() +",u_study.ACCOUNT, u_teacher.ACCOUNT, s.`NAME` DESC ";
		} else {
			if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
				sql += " ORDER BY c.GRADE ASC ";
			} else {
				sql += " ORDER BY u_study.ACCOUNT, u_teacher.ACCOUNT, s.`NAME` DESC, c.COURSE_DATE DESC, c.COURSE_TIME ";
			}
		}
		dp=this.findPageBySql(sql, dp, true, params);
		
		for(Course course:(List<Course>)dp.getDatas()){
			System.out.println(course.getCourseMinutes());
		}
		dp.getDatas();

		return dp;
	}
	
	@Override
	public DataPackage getOneOnOneBatchAttendanceListForMobile(
			CourseVo courseVo, DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql =  new StringBuffer(" SELECT c.* ");
		StringBuffer sqlFrom = new StringBuffer(" FROM course c ");
		StringBuffer sqlFromMore = new StringBuffer(", `user` u_teacher, `user` u_study, student s, product p, organization org ");
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.STUDY_MANAGER_ID = u_study.USER_ID ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		sqlWhere.append(" AND c.PRODUCT_ID = P.ID ");
		sqlWhere.append(" AND c.BL_CAMPUS_ID = org.ID ");

		if (StringUtils.isNotBlank(courseVo.getCourseDate())) {
			sqlWhere.append(" AND c.COURSE_DATE = :courseDate ");
			params.put("courseDate", courseVo.getCourseDate());
		}
		if (courseVo.getCourseStatus() != null) {
			sqlWhere.append(" AND  c.COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", courseVo.getCourseStatus());
		}
		
		if(StringUtils.isNotBlank(courseVo.getSearchParam())){//手机端的搜索参数，年级，科目，老师，学生
			sqlWhere.append(" AND (u_teacher.NAME LIKE '%"+courseVo.getSearchParam()+"%'");//老师名字
			sqlWhere.append(" OR s.NAME LIKE :searchParam ");//学生名字
			sqlFrom.append(" LEFT JOIN data_dict dd_s ON c.SUBJECT = dd_s.ID LEFT JOIN data_dict dd_g ON c.GRADE = dd_g.ID ");
			sqlWhere.append(" OR dd_s.NAME LIKE :searchParam ");//科目名字
			sqlWhere.append(" OR dd_g.NAME LIKE :searchParam )");//年级名字
			params.put("searchParam", "%"+courseVo.getSearchParam()+"%");
//			hqlWhere.append(" )");
		}
		
        if("teacherAttendance".equals(courseVo.getCurrentRoleId())){
        	sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
        	params.put("teacherId", userService.getCurrentLoginUser().getUserId());
		}else if("studyManegerVerify".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.STUDY_MANAGER_ID = :studyManagerId  ");
			params.put("studyManagerId", userService.getCurrentLoginUser().getUserId());
		}else if("classTeacherDeduction".equals(courseVo.getCurrentRoleId())
				&&  userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)) {
			Organization belongCampus = userService.getBelongCampus();
			sqlWhere.append(" AND c.BL_CAMPUS_ID = :belongCampusId ");
			params.put("belongCampusId", belongCampus.getId());
		}else{
			sqlWhere.append(" AND 1=2 ");
		}
        sql.append(sqlFrom).append(sqlFromMore).append(sqlWhere);
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {//老师帐号是按字母新建的，所以按老师名字排序可以用帐号来排
			sql.append(" ORDER BY c."+dp.getSidx()+" "+dp.getSord() +", u_study.ACCOUNT,u_teacher.ACCOUNT,s.NAME DESC");
		} else {
			sql.append(" ORDER BY c.COURSE_TIME, u_study.ACCOUNT desc ");
		}
		dp = this.findPageBySql(sql.toString(), dp, true, params);
		return dp;
	}
	

	
	/**
	 * 校区课程列表
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getSchoolZoneCourseList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" SELECT ");
		sql.append(" 	a.COURSE_ID COURSE_ID,p.name PRODUCT_NAME, g.`NAME` SUBJECT, h.`NAME` GRADE, f.`NAME` TEACHER_NAME, s.`NAME` STUDY_MANAGER_NAME ");
		sql.append(" 	, a.COURSE_DATE, a.COURSE_TIME, e.`NAME` STUDENT_NAME,  a.PLAN_HOURS, a.REAL_HOURS ");
		sql.append(" 	, b.STADUY_MANAGER_AUDIT_HOURS, c.TEACHING_MANAGER_AUDIT_HOURS, a.COURSE_STATUS ");
		sql.append(" 	, a.STUDENT_ID STUDENT_ID,a.TEACHER_ID  TEACHER_ID ");
//		sql.append(" 	, (                               ");
//		sql.append("           select count(distinct course_id) from course_conflict where  ");
//		sql.append("                 (                                                            ");
//		sql.append("                       student_id = e.`ID` and course_id <> a.COURSE_ID and  ");
//		sql.append("           			  (                        																");
//		sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
//		sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
//		sql.append("          		      )                   																    ");
//		sql.append("          		 )                																			");
//		sql.append("                 or                             															");
//		sql.append("                 (                                                            ");
//		sql.append("                       teacher_id = f.`USER_ID`  and course_id <> a.COURSE_ID and  ");
//		sql.append("           			  (                        																");
//		sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
//		sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
//		sql.append("          		      )                   																    ");
//		sql.append("          		 )                																			");
//		sql.append(" 	  ) as CRASH_IND       ");
		sql.append(" FROM ");
		sql.append(" 	course a ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		d.course_id d_course_id, ");
		sql.append(" 		d.course_hours STADUY_MANAGER_AUDIT_HOURS ");
		sql.append(" 	FROM ");
		sql.append(" 		course_attendance_record d ");
		sql.append("			, role b1 ");
		sql.append(" 	WHERE ");
		sql.append(" 		d.CHECK_USER_ROLE_ID = b1.ID and b1.roleCode = '"+ RoleCode.STUDY_MANAGER +"'  ORDER BY OPRATE_TIME DESC limit 0,1 ");
		sql.append(" ) b ON a.course_id = b.d_course_id ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		e.course_id e_course_id, ");
		sql.append(" 		e.course_hours TEACHING_MANAGER_AUDIT_HOURS ");
		sql.append(" 	FROM ");
		sql.append(" 		course_attendance_record e ");
		sql.append("			, role b2 ");
		sql.append(" 	WHERE ");
		sql.append(" 		e.CHECK_USER_ROLE_ID = b2.ID and b2.roleCode = '"+ RoleCode.EDUCAT_SPEC +"' ");
		sql.append(" ) c ON a.course_id = c.e_course_id  ");
		sql.append(" LEFT JOIN course_summary d ON a.COURSE_SUMMARY_ID = d.COURSE_SUMMARY_ID ");
		sql.append(" LEFT JOIN student e ON a.STUDENT_ID = e.ID ");
		sql.append(" LEFT JOIN user f ON a.TEACHER_ID = f.USER_ID ");
		sql.append(" LEFT JOIN user s ON a.STUDY_MANAGER_ID = s.USER_ID ");
		sql.append(" LEFT JOIN data_dict g ON (g.VALUE) = a.SUBJECT ");
		sql.append(" LEFT JOIN data_dict h ON (h.VALUE) = a.grade ");
		sql.append(" LEFT JOIN product p ON a.PRODUCT_ID = p.id ");

		sql.append(" WHERE 1=1 ");

        if(searchInputVo.getConflict() != null){
            sql.append("   and (                               ");
            sql.append("           select count(distinct course_id) from course_conflict where  ");
            sql.append("                 (                                                            ");
            sql.append("                       student_id = e.`ID` and course_id <> a.COURSE_ID and  ");
            sql.append("           			  (                        																");
            sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
            sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
            sql.append("          		      )                   																    ");
            sql.append("          		 )                																			");
            sql.append("                 or                             															");
            sql.append("                 (                                                            ");
            sql.append("                       teacher_id = f.`USER_ID`  and course_id <> a.COURSE_ID and  ");
            sql.append("           			  (                        																");
            sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
            sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
            sql.append("          		      )                   																    ");
            sql.append("          		 )                																			");
            sql.append(" 	   )       ");
            if(searchInputVo.getConflict() == 1){
                sql.append(" 	  > 0       ");
            }else{
                sql.append(" 	  = 0       ");
            }
        }

		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate  ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate  ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			sql.append(" AND a.STUDENT_ID = :studentId  ");
			params.put("studentId", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			sql.append(" AND e.NAME LIKE :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId  ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			sql.append(" AND f.NAME LIKE :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			sql.append(" AND h.VALUE = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			sql.append(" AND g.VALUE = :subject  ");
			params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			sql.append(" AND a.COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getCourseSummaryId())) {
			sql.append(" AND a.COURSE_SUMMARY_ID = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
        if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerId())) {
            sql.append(" AND s.USER_ID = :studyManagerId ");
            params.put("studyManagerId", searchInputVo.getStudyManagerId());
        }
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerName())) {
			sql.append(" AND s.NAME LIKE :studyManagerName");
			params.put("studyManagerName", "%"+searchInputVo.getStudyManagerName()+"%");
		}
		if (searchInputVo.getDayOfWeek()!=null) {
			sql.append(" AND  DAYOFWEEK(a.COURSE_DATE) = :dayOfWeek ");
			params.put("dayOfWeek", searchInputVo.getDayOfWeek());
		}
        if (StringUtils.isNotBlank(searchInputVo.getProductName())) {
            sql.append(" AND  p.name like :productName ");
            params.put("productName", "%"+searchInputVo.getProductName()+"%");
        }
		//权限控制，只能看到本校区的，老师只能看自己的
		Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			sql.append(" and e.BL_CAMPUS_ID = '" + campus.getId() + "'");
//		}
//		if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
//			sql.append(" and a.TEACHER_ID = '" + userService.getCurrentLoginUser().getUserId() + "'");
//		}
		//权限控制
//		sql.append(RoleCodeAuthoritySearchUtil.getCourseSQLRoleCodeAuthoritySQL(userService.getCurrentLoginUser(), campus));
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("本周课表","sql","a.BL_CAMPUS_ID"));

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} else {
			sql.append(" order by COURSE_DATE desc,COURSE_TIME desc");
		}
		dp = this.findPageBySql(sql.toString(), dp, true, params);
		
//		Query q = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
//				.addScalar("COURSE_ID")
//				.addScalar("PRODUCT_NAME")
//				.addScalar("COURSE_DATE")
//				.addScalar("STUDY_MANAGER_NAME")
//				.addScalar("STUDENT_NAME")
//                .addScalar("GRADE")
//                .addScalar("SUBJECT")
//                .addScalar("COURSE_DATE")
//                .addScalar("COURSE_TIME")
//                .addScalar("PLAN_HOURS")
//                .addScalar("TEACHER_NAME")
////				.addScalar("REAL_HOURS")
////				.addScalar("STADUY_MANAGER_AUDIT_HOURS")
////				.addScalar("TEACHING_MANAGER_AUDIT_HOURS")
//				.addScalar("COURSE_STATUS")
//				.addScalar("STUDENT_ID")
//				.addScalar("TEACHER_ID")
////				.addScalar("CRASH_IND")
//				;
//		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
//		q.setMaxResults(dp.getPageSize());
//		List<Object[]> datas=q.list();
		List<Object[]> datas=(List<Object[]>)dp.getDatas();// FIXME: 2017/3/10
		List<Object[]> newDatas=new ArrayList<Object[]>();
		for(Object[] obj :datas){
			Object[] newObj=new Object[obj.length-1];
			for(int i=0;i<obj.length-2;i++){
				newObj[i]=obj[i];
			}
			newObj[obj.length-2]=courseConflictDao.countDistinctConflicts(obj[0].toString(), obj[12].toString(), obj[13].toString(), obj[7].toString(), obj[8].toString());
			newDatas.add(newObj);
		}
		
		dp.setDatas(newDatas);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		
		
		return dp;
	}
	
	/**
	 * 批量修改课程属性
	 * @throws Exception 
	 */
	public void courseAttrChanges(String changesAttr, String changesData, boolean updateAll, String[] ids, CourseSearchInputVo courseSearchInputVo) throws Exception {
		if(StringUtils.isEmpty(changesData)){
			throw new ApplicationException("修改值不能为空");
		}
		checkCourseAttrChanges(changesAttr, changesData, updateAll, ids, courseSearchInputVo);
//		if ("studyManager".equals(changesAttr)) {
//			checkUpdateStudentManage(updateAll, ids, courseSearchInputVo);
//		}
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		String setColumn="";
		if("courseStatus".equals(changesAttr) && "NEW".equals(changesData)){
			setColumn=",realHours=NULL,auditHours=NULL,teachingAttendTime=NULL ,teachingManagerAuditId=NULL,teachingManagerAuditTime=NULL" +
					",studyManagerAuditId=NULL,studyManagerAuditTime=NULL ";
		}
		if (changesAttr.equals("studyManager") || changesAttr.equals("teacher")) {
			changesAttr = changesAttr + ".userId";
		}
		hql.append(" update Course set "+changesAttr+"= :changesData "+setColumn+" where  courseStatus!='"+CourseStatus.CHARGED+"' ");
		params.put("changesData", changesData);
		String delCourseConflictHql="delete CourseConflict where courseId in(select courseId from Course where 1=1 "+getCourseChangesHqlWhere(courseSearchInputVo)+") ";
//		if (!updateAll) {// 更新CheckBox选中记录
			hql.append(" and courseId in( :ids ");
//			String delIds="";
//			for (int n=0; n<ids.length; n++) {
//				if (n!=0) {
//					hql.append(",");
//					delIds+=",";
//				}
//				hql.append("'" + ids[n] + "'");
//				delIds+="'" + ids[n] + "'";
//			}
			hql.append(" ) ");
			params.put("ids", ids);
			delCourseConflictHql="delete CourseConflict where courseId in (:ids) ";
		Map<String, Object> params1 = Maps.newHashMap();
		params1.put("ids", ids);
			//
//		} else {// 更新所有分页记录
//			hql.append(getCourseChangesHqlWhere(courseSearchInputVo));
//			
//		}
		super.excuteHql(hql.toString(), params);
		if(changesAttr.equals("studyManager")){
            for (String id : ids) {
            	deleteCourseAttendance(id);
            }
		}
		if("STUDENT_LEAVE".equals(changesData) || "TEACHER_LEAVE".equals(changesData) || "CANCEL".equals(changesData)){
			super.excuteHql(delCourseConflictHql, params1);
		}
	}
	
	@Override
	public void batchChangeTeacher(String teacherUserId, String[] ids) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" update Course set teacher.userId= :teacherUserId ");
		params.put("teacherUserId", teacherUserId);
		hql.append("where courseId in( :ids");
//		for (int n=0; n<ids.length; n++) {
//			if (n!=0) {
//				hql.append(",");
//			}
//			hql.append("'" + ids[n] + "'");
//		}
		hql.append(" ) ");
		params.put("ids", ids);
		super.excuteHql(hql.toString(), params);
	}
	
	/**
	 * 批量修改客户年级
	 * @param gradeId
	 * @param ids
	 */
	public void batchChangeCourseGrade (String gradeId, String ids){
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		String[] idArray = StringUtil.replaceSpace(ids).split(",");
		hql.append(" update Course set grade.id = :gradeId ");
		params.put("gradeId", gradeId);
		hql.append("where courseId in( :idArray ");
//		for (int n=0; n<idArray.length; n++) {
//			if (n!=0) {
//				hql.append(",");
//			}
//			hql.append("'" + idArray[n] + "'");
//		}
		hql.append(" ) ");
		params.put("idArray", idArray);
		super.excuteHql(hql.toString(), params);
	}
	
	/**
	 * 批量修改验证-已结算的数据不允许修改
	 * 批量修改科目，也要这批人员所在校区又该年级的该科目产品才可以，老师也是只有该校区有该科目的老师才可以选择
	 * @param changesAttr
	 * @param changesData
	 * @param updateAll
	 * @param ids
	 * @param courseSearchInputVo
	 * @return
	 * @throws Exception
	 */
	private boolean checkCourseAttrChanges(String changesAttr, String changesData, boolean updateAll, String[] ids, CourseSearchInputVo courseSearchInputVo) throws Exception{
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("select count(*) from Course  where courseStatus='"+CourseStatus.CHARGED+"' ");
		if (!updateAll) {// 更新CheckBox选中记录
			hql.append(" and courseId in( :ids");
//			for (int n=0; n<ids.length; n++) {
//				if (n!=0) {
//					hql.append(",");
//				}
//				hql.append("'" + ids[n] + "'");
//			}
			hql.append(" ) ");
			params.put("ids", ids);
			
		} else {// 更新所有分页记录
//			hql.append(getCourseChangesHqlWhere(courseSearchInputVo));
			hql.append(getCourseChangesHqlWhere(courseSearchInputVo, params));
		}
		int count=super.findCountHql(hql.toString(), params);
		if(count>0){
			throw new ApplicationException("已结算的数据不允许修改");
		}
		if("subject".equals(changesAttr) || "teacher".equals(changesAttr)){
			hql.setLength(0);
			hql.append(" from Course  where 1=1 ");
			if (!updateAll) {// 更新CheckBox选中记录
				hql.append(" and courseId in( :ids ");
//				for (int n=0; n<ids.length; n++) {
//					if (n!=0) {
//						hql.append(",");
//					}
//					hql.append("'" + ids[n] + "'");
//				}
				hql.append(" ) ");
				params.put("ids", ids);
				
			} else {// 更新所有分页记录
//				hql.append(getCourseChangesHqlWhere(courseSearchInputVo));
				hql.append(getCourseChangesHqlWhere(courseSearchInputVo, params));
			}
			List<Course> list=super.findAllByHQL(hql.toString(), params);
			if(list!=null && list.size()>0){
				if("subject".equals(changesAttr)){
					DataDict sub=dataDictDao.findById(changesData);
					if(sub==null){
						throw new ApplicationException("修改科目不存在");
					}
					Set<String> set=new HashSet<String>();
					for(Course course : list){
						if(course.getGrade()!=null)
							set.add(course.getGrade().getId());
					}
					Organization org= userService.getBelongBranch();//归属校区
					List<Product> proList = productDao.findByCriteria(Expression.eq("category", ProductType.ONE_ON_ONE_COURSE),Expression.eq("name", sub.getName()),Expression.in("gradeDict.id", set),Expression.eq("organization.id", org.getId()));
					if(proList.size() !=set.size()){
						for(Product pro : proList){
							set.remove(pro.getGradeDict().getId());
						}
						Iterator<String> iter=set.iterator();
						String gradeIds="";
						while(iter.hasNext()){
							gradeIds+=","+iter.next();
						}
						gradeIds=gradeIds.substring(1);
						Set<String> gradeNames=new HashSet<String>();
						for(Course course : list){
							if(course.getGrade()!=null){
								for(String str : StringUtil.replaceSpace(gradeIds).split(",")){
									if(str.equals(course.getGrade().getId())){
										gradeNames.add(course.getGrade().getName());
									}
								}
							}	
						}
						throw new ApplicationException("产品中年级："+gradeNames.toString() +"没有科目"+sub.getName());
					}
				}else{//判断老师是否可教科目年级
					User teach=userDao.findById(changesData);
					if(teach==null){
						throw new ApplicationException("修改老师不存在");
					}
					List<TeacherSubject> teacherSubjectList =teacherSubjectDao.findByCriteria(
							Expression.eq("id", changesData));
					if(teacherSubjectList==null || teacherSubjectList.size()==0){
						throw new ApplicationException(teach.getName()+"没有可教科目");
					}
					Map<String,String> map =new HashMap<String, String>();
					boolean bool=false;
					for(Course course : list){
						DataDict grade=course.getGrade();
						DataDict subject=course.getSubject();
						if(grade!=null && subject!=null){
							bool=false;
							for(TeacherSubject ts : teacherSubjectList){
								if(ts.getSubject().getId().equals(subject.getId())
										&& ts.getGrade().getId().equals(grade.getId())){
									bool=true;
									continue;
								}
							}
							if(!bool){
								//grade.getId()+"_"+
								String key=grade.getName();
								//subject.getId()+"_"+
								String value=subject.getName();
								if(map.containsKey(key)){
									if(map.get(key).indexOf(value)==-1)
										map.put(key, map.get(key)+","+value);
								}else{
									map.put(key, value);
								}
							}
						}
					}
					if(!map.isEmpty()){
						throw new ApplicationException(teach.getName()+"不可教"+map.toString().replaceAll("=", " ： "));
					}
				}
			}
			
		}
		return true;
	}
	
	/**
	 * 检查能否修改选中课程的学管
	 * @param updateAll
	 * @param ids
	 * @param courseSearchInputVo
	 * @return
	 * @throws Exception
	 */
	private boolean checkUpdateStudentManage(boolean updateAll, String[] ids, CourseSearchInputVo courseSearchInputVo) throws Exception {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append("from Course");
		String whereHql = " where 1=1";
		whereHql += " and courseStatus in ('" +CourseStatus.NEW.getValue() + "', '" + CourseStatus.STUDENT_ATTENDANCE.getValue() + "', '" + CourseStatus.TEACHER_ATTENDANCE.getValue() + "', '" + CourseStatus.STUDY_MANAGER_AUDITED.getValue() + "')";
//		whereHql += " and courseStatus!='"+CourseStatus.CHARGED+"'";
		whereHql += " and courseDate<= :currentDate ";
		params.put("currentDate", DateTools.getCurrentDate());
		hql.append(whereHql);
		if (!updateAll) {// 更新CheckBox选中记录
			hql.append(" and courseId in( :ids");
//			for (int n=0; n<ids.length; n++) {
//				if (n!=0) {
//					hql.append(",");
//				}
//				hql.append("'" + ids[n] + "'");
//			}
			hql.append(" ) ");
			params.put("ids", ids);
			
		} else {// 更新所有分页记录
//			hql.append(getCourseChangesHqlWhere(courseSearchInputVo));
			hql.append(getCourseChangesHqlWhere(courseSearchInputVo, params));
		}
		List<Course> list = super.findAllByHQL(hql.toString(), params);
		if (list.size() > 0) {
			throw new ApplicationException("要先考勤完今天和以前的课程才能换学管。");
		}
		return true;
	}
	
	/**
	 * 根据条件查询课程
	 * @param updateAll 
	 * @param ids
	 * @param courseChangesSearchInputVo
	 * @return
	 */
	public List<Course> queryCourseAttrChanges( boolean updateAll, String[] ids, CourseChangesSearchInputVo courseChangesSearchInputVo) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from Course  where 1=1 ");
		
		if (!updateAll) {// 更新CheckBox选中记录
			hql.append(" and courseId in( :ids");
//			for (int n=0; n<ids.length; n++) {
//				if (n!=0) {
//					hql.append(",");
//				}
//				hql.append("'" + ids[n] + "'");
//			}
			hql.append(" ) ");
			params.put("ids", ids);
			
		} else {// 更新所有分页记录
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getStartDate())) {
				hql.append(" and startDate >= :startDate ");
				params.put("startDate", courseChangesSearchInputVo.getStartDate());
			}
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getEndDate())) {
				hql.append(" and endDate <= :endDate ");
				params.put("endDate", courseChangesSearchInputVo.getEndDate());
			}
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getStudentName())) {
				hql.append(" and student.name like :studentName ");
				params.put("studentName", "%"+courseChangesSearchInputVo.getStudentName()+"%");
			}
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getTeacherName())) {
				hql.append(" and teacher.name like :teacherName ");
				params.put("teacherName", "%"+courseChangesSearchInputVo.getTeacherName()+"%");
			}
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getGrade())) {
				hql.append(" and grade.value = :grade ");
				params.put("grade", courseChangesSearchInputVo.getGrade());
			}
			if (StringUtils.isNotBlank(courseChangesSearchInputVo.getSubject())) {
				hql.append(" and subject.value = :subject ");
				params.put("subject", courseChangesSearchInputVo.getSubject());
			}
			if (courseChangesSearchInputVo.getCourseStatus() != null) {
				hql.append(" and courseStatus = :courseStatus ");
				params.put("courseStatus", courseChangesSearchInputVo.getCourseStatus());
			}
		}
		return super.findAllByHQL(hql.toString(), params);
	}
	
	/**
	 * 查询某一排课需求的所有已排课程
	 * @param courseRequirementId
	 * @param startDate
	 * @param endDate
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseRequirementArrengedCourseList(String courseRequirementId, String startDate, String endDate, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from Course  where 1=1 ");
		
		if (StringUtils.isNotBlank(courseRequirementId)) {
			hql.append(" and courseSummary.courseRequirement.id = :courseRequirementId ");
			params.put("courseRequirementId", courseRequirementId);
		}
		if (StringUtils.isNotBlank(startDate)) {
			hql.append(" and courseDate >= :startDate ");
			params.put("startDate", startDate);
		}
		if (StringUtils.isNotBlank(endDate)) {
			hql.append(" and courseDate <= :endDate ");
			params.put("endDate", endDate);
		}

		return super.findPageByHQL(hql.toString(), dp, true, params);
	}
	
	/**
	 * 根据课程概况ID查找所有课程
	 */
	@Override
	public DataPackage getCourseListByCourseSummaryId(String courseSummaryId) {

		List<Criterion> criterionList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(courseSummaryId)) {
			criterionList.add(Restrictions.eq("courseSummary.courseSummaryId", courseSummaryId));
		}

		DataPackage dp = new DataPackage(0, 9999);
		return super.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "createTime", "desc"), criterionList);
	}

	@Override
	public boolean checkCourseCrash(CourseVo course) {
		Map<String, Object> params = Maps.newHashMap();
		String hql = "select count(*) from course where studentCrashInfo= :studentCrashInfo  or teacherCrashInfo = :teacherCrashInfo ";
		params.put("studentCrashInfo", course.getStudentCrashInfo());
		params.put("teacherCrashInfo", course.getTeacherCrashInfo());
		return super.findCountHql(hql, params) > 0;
	}
	
	/**
	 * 批量更改课程的列表
	 */
	@Override
	public DataPackage getCourseChangesList(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		StringBuffer hql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" from Course  where 1=1 ");
//		hql.append(getCourseChangesHqlWhere(searchInputVo));
		hql.append(getCourseChangesHqlWhere(searchInputVo, params));
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} 
		return super.findPageByHQL(hql.toString(), dp, true, params);
	}

	/**
	 * 批量修改课程查询条件
	 * @param searchInputVo
	 * @param params
	 * @return
	 */
	private String getCourseChangesHqlWhere(CourseSearchInputVo searchInputVo, Map<String, Object> params){
		StringBuffer hql=new StringBuffer();
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			hql.append(" and courseSummary.courseSummaryId= :courseSummaryIdP ");
			params.put("courseSummaryIdP", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			hql.append(" and courseDate>= :startDateP  ");
			params.put("startDateP", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			hql.append(" and courseDate<= :endDateP  ");
			params.put("endDateP", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			hql.append(" and student.id= :studentIdP ");
			params.put("studentIdP", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			hql.append(" and student.name like :studentNameP ");
			params.put("studentNameP", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			hql.append(" and teacher.userId= :teacherIdP ");
			params.put("teacherIdP", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			hql.append(" and teacher.name like :teacherNameP ");
			params.put("teacherNameP", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudyManagerId())) {
			hql.append(" and studyManager.userId= :studyManagerIdP ");
			params.put("studyManagerIdP", searchInputVo.getStudyManagerId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			hql.append(" and grade.id = :gradeP ");
			params.put("gradeP", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			hql.append(" and subject.id = :subjectP ");
			params.put("subjectP", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			hql.append(" and courseStatus = :courseStatusP ");
			params.put("courseStatusP", searchInputVo.getCourseStatus());
		}
		//权限
//		hql.append(RoleCodeAuthoritySearchUtil.getCourseRoleCodeAuthority(userService.getCurrentLoginUser(),  userService.getBelongCampus()));
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("批量修改课程列表","hql","blCampusId.id"));
		return hql.toString();
	}
	
	/**
	 * 批量修改课程查询条件
	 * @param searchInputVo
	 * @return
	 */
	private String getCourseChangesHqlWhere(CourseSearchInputVo searchInputVo){
		StringBuffer hql=new StringBuffer();
		if (StringUtils.isNotBlank(searchInputVo.getCourseSummaryId())) {
			hql.append(" and courseSummary.courseSummaryId='"+searchInputVo.getCourseSummaryId()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			hql.append(" and courseDate>='"+searchInputVo.getStartDate()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			hql.append(" and courseDate<='"+searchInputVo.getEndDate()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			hql.append(" and student.id='"+searchInputVo.getStudentId()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			hql.append(" and student.name like '%"+searchInputVo.getStudentName()+"%' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			hql.append(" and teacher.userId='"+searchInputVo.getTeacherId()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			hql.append(" and teacher.name like '%"+searchInputVo.getTeacherName()+"%' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudyManagerId())) {
			hql.append(" and studyManager.userId= '"+searchInputVo.getStudyManagerId()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			hql.append(" and grade.id = '"+searchInputVo.getGrade()+"' ");
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			hql.append(" and subject.id = '"+searchInputVo.getSubject()+"' ");
		}
		if (searchInputVo.getCourseStatus() != null) {
			hql.append(" and courseStatus = '"+searchInputVo.getCourseStatus()+"' ");
		}
		//权限
//		hql.append(RoleCodeAuthoritySearchUtil.getCourseRoleCodeAuthority(userService.getCurrentLoginUser(),  userService.getBelongCampus()));
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("批量修改课程列表","hql","blCampusId.id"));
		return hql.toString();
	}

	@Override
	public DataPackage getSchoolZoneCourseListForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		
		sql.append(" SELECT ");
		sql.append(" 	a.COURSE_ID COURSE_ID,p.name PRODUCT_NAME, g.`NAME` SUBJECT, h.`NAME` GRADE, f.`NAME` TEACHER_NAME, s.`NAME` STUDY_MANAGER_NAME ");
		sql.append(" 	, a.COURSE_DATE, a.COURSE_TIME, e.`NAME` STUDENT_NAME,  a.PLAN_HOURS, a.REAL_HOURS ");
		sql.append(" 	, b.STADUY_MANAGER_AUDIT_HOURS, c.TEACHING_MANAGER_AUDIT_HOURS, a.COURSE_STATUS ");
		sql.append(" 	, a.STUDENT_ID STUDENT_ID,a.TEACHER_ID  TEACHER_ID ");
//		sql.append(" 	, (                               ");
//		sql.append("           select count(distinct course_id) from course_conflict where  ");
//		sql.append("                 (                                                            ");
//		sql.append("                       student_id = e.`ID` and course_id <> a.COURSE_ID and  ");
//		sql.append("           			  (                        																");
//		sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
//		sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
//		sql.append("          		      )                   																    ");
//		sql.append("          		 )                																			");
//		sql.append("                 or                             															");
//		sql.append("                 (                                                            ");
//		sql.append("                       teacher_id = f.`USER_ID`  and course_id <> a.COURSE_ID and  ");
//		sql.append("           			  (                        																");
//		sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
//		sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
//		sql.append("          		      )                   																    ");
//		sql.append("          		 )                																			");
//		sql.append(" 	  ) as CRASH_IND       ");
		sql.append(" FROM ");
		sql.append(" 	course a ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		d.course_id d_course_id, ");
		sql.append(" 		d.course_hours STADUY_MANAGER_AUDIT_HOURS ");
		sql.append(" 	FROM ");
		sql.append(" 		course_attendance_record d ");
		sql.append("			, role b1 ");
		sql.append(" 	WHERE ");
		sql.append(" 		d.CHECK_USER_ROLE_ID = b1.ID and b1.roleCode = '"+ RoleCode.STUDY_MANAGER +"'  ORDER BY OPRATE_TIME DESC limit 0,1 ");
		sql.append(" ) b ON a.course_id = b.d_course_id ");
		sql.append(" LEFT JOIN ( ");
		sql.append(" 	SELECT ");
		sql.append(" 		e.course_id e_course_id, ");
		sql.append(" 		e.course_hours TEACHING_MANAGER_AUDIT_HOURS ");
		sql.append(" 	FROM ");
		sql.append(" 		course_attendance_record e ");
		sql.append("			, role b2 ");
		sql.append(" 	WHERE ");
		sql.append(" 		e.CHECK_USER_ROLE_ID = b2.ID and b2.roleCode = '"+ RoleCode.EDUCAT_SPEC +"' ");
		sql.append(" ) c ON a.course_id = c.e_course_id  ");
		sql.append(" LEFT JOIN course_summary d ON a.COURSE_SUMMARY_ID = d.COURSE_SUMMARY_ID ");
		sql.append(" LEFT JOIN student e ON a.STUDENT_ID = e.ID ");
		sql.append(" LEFT JOIN user f ON a.TEACHER_ID = f.USER_ID ");
		sql.append(" LEFT JOIN user s ON a.STUDY_MANAGER_ID = s.USER_ID ");
		sql.append(" LEFT JOIN data_dict g ON (g.VALUE) = a.SUBJECT ");
		sql.append(" LEFT JOIN data_dict h ON (h.VALUE) = a.grade ");
		sql.append(" LEFT JOIN product p ON a.PRODUCT_ID = p.id ");

		sql.append(" WHERE 1=1 ");

        if(searchInputVo.getConflict() != null){
            sql.append("   and (                               ");
            sql.append("           select count(distinct course_id) from course_conflict where  ");
            sql.append("                 (                                                            ");
            sql.append("                       student_id = e.`ID` and course_id <> a.COURSE_ID and  ");
            sql.append("           			  (                        																");
            sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
            sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
            sql.append("          		      )                   																    ");
            sql.append("          		 )                																			");
            sql.append("                 or                             															");
            sql.append("                 (                                                            ");
            sql.append("                       teacher_id = f.`USER_ID`  and course_id <> a.COURSE_ID and  ");
            sql.append("           			  (                        																");
            sql.append("                              concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,1,2),substr(a.COURSE_TIME,4,2)) <> end_time");
            sql.append("          		         or concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) between start_time and end_time  and concat(replace(a.COURSE_DATE,'-',''),substr(a.COURSE_TIME,9,2),substr(a.COURSE_TIME,12,2)) <> start_time ");
            sql.append("          		      )                   																    ");
            sql.append("          		 )                																			");
            sql.append(" 	   )       ");
            if(searchInputVo.getConflict() == 1){
                sql.append(" 	  > 0       ");
            }else{
                sql.append(" 	  = 0       ");
            }
        }

		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			sql.append(" AND a.COURSE_DATE >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			sql.append(" AND a.COURSE_DATE <= :endDate ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			sql.append(" AND a.STUDENT_ID = :studentId ");
			params.put("studentId", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			sql.append(" AND e.NAME LIKE :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			sql.append(" AND a.TEACHER_ID = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			sql.append(" AND f.NAME LIKE :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			sql.append(" AND h.VALUE = :grade ");
			params.put("grade",searchInputVo.getGrade() );
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			sql.append(" AND g.VALUE = :subject ");
			params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			sql.append(" AND a.COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getCourseSummaryId())) {
			sql.append(" AND a.COURSE_SUMMARY_ID = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
        if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerId())) {
            sql.append(" AND s.USER_ID = :studyManagerId ");
            params.put("studyManagerId", searchInputVo.getStudyManagerId());
        }
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerName())) {
			sql.append(" AND s.NAME LIKE :studyManagerName ");
			params.put("studyManagerName", "%"+searchInputVo.getStudyManagerName()+"%");
		}
		if (searchInputVo.getDayOfWeek()!=null) {
			sql.append(" AND  DAYOFWEEK(a.COURSE_DATE) = :dayOfWeek ");
			params.put("dayOfWeek", searchInputVo.getDayOfWeek());
		}
        if (StringUtils.isNotBlank(searchInputVo.getProductName())) {
            sql.append(" AND  p.name like :productName ");
            params.put("productName", "%"+searchInputVo.getProductName()+"%");
        }
		//权限控制，只能看到本校区的，老师只能看自己的
		//Organization campus = userService.getBelongCampus();
//		if (campus != null && OrganizationType.CAMPUS.equals(campus.getOrgType())) {
//			sql.append(" and e.BL_CAMPUS_ID = '" + campus.getId() + "'");
//		}
//		if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
//			sql.append(" and a.TEACHER_ID = '" + userService.getCurrentLoginUser().getUserId() + "'");
//		}
		//权限控制
//		sql.append(RoleCodeAuthoritySearchUtil.getCourseSQLRoleCodeAuthoritySQL(userService.getCurrentLoginUser(), campus));
		//sql.append(roleQLConfigService.getValueResult("本周课表","sql"));

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sql.append(" order by "+dp.getSidx()+" "+dp.getSord());
		} else {
			sql.append(" order by COURSE_DATE desc,COURSE_TIME desc");
		}
		
//		Query q = hibernateTemplate.getSessionFactory().getCurrentSession().createSQLQuery(sql.toString())
//				.addScalar("COURSE_ID")
//				.addScalar("PRODUCT_NAME")
//				.addScalar("COURSE_DATE")
//				.addScalar("STUDY_MANAGER_NAME")
//				.addScalar("STUDENT_NAME")
//                .addScalar("GRADE")
//                .addScalar("SUBJECT")
//                .addScalar("COURSE_DATE")
//                .addScalar("COURSE_TIME")
//                .addScalar("PLAN_HOURS")
//                .addScalar("TEACHER_NAME")
////				.addScalar("REAL_HOURS")
////				.addScalar("STADUY_MANAGER_AUDIT_HOURS")
////				.addScalar("TEACHING_MANAGER_AUDIT_HOURS")
//				.addScalar("COURSE_STATUS")
//				.addScalar("STUDENT_ID")
//				.addScalar("TEACHER_ID")
////				.addScalar("CRASH_IND")
//				;
//		q.setFirstResult(dp.getPageNo()*dp.getPageSize());
//		q.setMaxResults(dp.getPageSize());
		dp = this.findPageBySql(sql.toString(), dp, true, params);
//		List<Object[]> datas=q.list();
		List<Object[]> datas=(List<Object[]>)dp.getDatas();
		List<Object[]> newDatas=new ArrayList<Object[]>();
		for(Object[] obj :datas){
			Object[] newObj=new Object[obj.length-1];
			for(int i=0;i<obj.length-2;i++){
				newObj[i]=obj[i];
			}
			newObj[obj.length-2]=courseConflictDao.countDistinctConflicts(obj[0].toString(), obj[11].toString(), obj[12].toString(), obj[6].toString(), obj[7].toString());
			newDatas.add(newObj);
		}
		
		dp.setDatas(newDatas);
		dp.setRowCount(findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		
		
		return dp;
	}
	
	 /**
     * 删除考勤记录
     *
     * @param id
     */
	public void deleteCourseAttendance(String id) {
        Course course = findById(id);
        if (course == null) {
            throw new ApplicationException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (course.getCourseStatus() == CourseStatus.CHARGED){
            throw new ApplicationException("已结算的课程不允许修改");
        }
        course.setCourseStatus(CourseStatus.NEW);
//        deleteAll(course.getCourseAttendanceRecords());
        course.setTeachingAttendTime(null);
        course.setTeachingManagerAuditId(null);
        course.setTeachingManagerAuditTime(null);
        course.setStudyManagerAuditId(null);
        course.setStudyManagerAuditTime(null);
        course.setCourseAttendanceRecords(null);
        course.setAuditHours(null);
        course.setRealHours(null);
        save(course);
    }
	
	/**
	 * 一对一课程审批与查看汇总(课时)
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneAuditAnalyzeList(CourseVo inputCourseVo,
			DataPackage dp) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select org.*, ah.teacherName,ah.workType , ah.TEACHER_ID as teacherId, ah.teacherBlcampusName, ah.unAuditHours, ah.validateHours, ah.unValidateHours, gh.*,ah.teacherLevel,ah.teacherType  ");
    	sql.append(" from (select c.BL_CAMPUS_ID, c.TEACHER_ID, u.`NAME` as teacherName, u.WORK_TYPE as 'workType' , org.`name` as teacherBlcampusName, ");
    	sql.append(" ifnull(sum(case when (c.AUDIT_STATUS is null or c.AUDIT_STATUS = 'UNAUDIT') then c.REAL_HOURS else 0 end ), 0) as unAuditHours, ");
    	sql.append(" ifnull(sum(case when c.AUDIT_STATUS='VALIDATE' then c.REAL_HOURS else 0 end ), 0) as validateHours, ");
    	sql.append(" ifnull(sum(case when c.AUDIT_STATUS='UNVALIDATE' then c.REAL_HOURS else 0 end ), 0) as unValidateHours ");
    	
    	sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherType");

		params.put("endDate", inputCourseVo.getEndDate());
    	
    	sql.append(" from course c left join user u on u.USER_ID = c.TEACHER_ID ");
		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end
    	sql.append(" where c.BL_CAMPUS_ID = :campusId ");
    	params.put("campusId", inputCourseVo.getCampusId());
		//主职位
		sql.append(" and udj.isMajorRole = 0");
    	sql.append(" and c.COURSE_DATE >= :startDate and c.COURSE_DATE <= :endDate ");
    	params.put("startDate", inputCourseVo.getStartDate());
    	sql.append(" and c.COURSE_STATUS = 'CHARGED' ");

		//科目筛选
		if (StringUtil.isNotBlank(inputCourseVo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(inputCourseVo.getSubject()).split(",");
			params.put("subjects", subjects);
			if(subjects.length>0){
				sql.append(" and  c.subject in (:subjects) ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or c.subject ='"+subjects[i]+"' ");
//					}
//				}

			}
		}

    	if (StringUtils.isNotBlank(inputCourseVo.getTeacherId())) {
    		sql.append(" and c.TEACHER_ID = :teacherId ");
    		params.put("teacherId", inputCourseVo.getTeacherId());
    	}
    	sql.append(" group by c.BL_CAMPUS_ID, c.TEACHER_ID) ah ");
    	sql.append(" left join (select BL_CAMPUS_ID, TEACHER_ID, max(IF(gradeName = '一年级',amount,0)) as 'gradeOneHours', ");
    	sql.append(" max(IF(gradeName = '二年级',amount,0)) as 'gradeTwoHours', max(IF(gradeName = '三年级',amount,0)) as 'gradeThreeHours', ");
    	sql.append(" max(IF(gradeName = '四年级',amount,0)) as 'gradeFourHours', max(IF(gradeName = '五年级',amount,0)) as 'gradeFiveHours',  ");
    	sql.append(" max(IF(gradeName = '六年级',amount,0)) as 'gradeSixHours', max(IF(gradeName = '初一',amount,0)) as 'gradeSevenHours', ");
    	sql.append(" max(IF(gradeName = '初二',amount,0)) as 'gradeEightHours',  max(IF(gradeName = '初三',amount,0)) as 'gradeNineHours', ");
    	sql.append(" max(IF(gradeName = '高一',amount,0)) as 'gradeTenHours',  max(IF(gradeName = '高二',amount,0)) as 'gradeElevenHours', ");
    	sql.append(" max(IF(gradeName = '高三',amount,0)) as 'gradeTwelveHours' ");
    	sql.append(" from (select sum(c.REAL_HOURS) as amount, g.NAME as gradeName, c.BL_CAMPUS_ID, c.TEACHER_ID, c.GRADE  ");
    	sql.append(" from course c inner join (select ID, NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on c.GRADE = g.ID ");
    	sql.append(" where c.BL_CAMPUS_ID = :campusId and c.AUDIT_STATUS = 'VALIDATE' ");
    	sql.append(" and c.COURSE_DATE >= :startDate and c.COURSE_DATE <= :endDate ");
    	sql.append(" and c.COURSE_STATUS = 'CHARGED' ");

    	params.put("startDate", inputCourseVo.getStartDate());


		//科目筛选
		if (StringUtil.isNotBlank(inputCourseVo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(inputCourseVo.getSubject()).split(",");
			if(subjects.length>0){
//				sql.append(" and ( 1=0 ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or c.subject ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");
				sql.append(" and c.subject in (:subjects) ");
				params.put("subjects", subjects);
			}
		}

    	if (StringUtils.isNotBlank(inputCourseVo.getTeacherId())) {
    		sql.append(" and c.TEACHER_ID = :teacherId  ");
    		params.put("teacherId", inputCourseVo.getTeacherId());
    	}
    	sql.append(" group by c.BL_CAMPUS_ID, c.TEACHER_ID, c.GRADE) d group by d.BL_CAMPUS_ID, d.TEACHER_ID) gh on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.TEACHER_ID = gh.TEACHER_ID ");
    	sql.append(" inner join (select CONCAT(org_group.id, '') as groupId, org_group.name as groupName, CONCAT(org_brench.id, '') as brenchId, ");
    	sql.append(" org_brench.name as brenchName, CONCAT(org_campus.id, '') as campusId, org_campus.name as campusName ");
    	sql.append(" from organization org_campus ");
    	sql.append(" LEFT JOIN organization org_brench on org_campus.parentID = org_brench.id ");
    	sql.append(" LEFT JOIN organization org_group on org_brench.parentID = org_group.id ");
    	sql.append(" where org_campus.orgType = 'CAMPUS' and org_campus.id = :campusId ) org on ah.BL_CAMPUS_ID = org.campusId ");
    	params.put("campusId", inputCourseVo.getCampusId());
    	if (StringUtils.isNotBlank(inputCourseVo.getHasUnauditStatus())) {
    		if ("HAS_UNAUDIT".equals(inputCourseVo.getHasUnauditStatus())) {
    			sql.append(" and ah.unAuditHours > 0 ");
    		} else {
    			sql.append(" and ah.unAuditHours <= 0 ");
    		}
    	}
    	

		if(StringUtils.isNotBlank(inputCourseVo.getTeacherType())){
			sql.append(" and ah.teacherType= :teacherType ");
			params.put("teacherType", inputCourseVo.getTeacherType());
		}
    	
    	List<Map<Object, Object>> list = super.findMapOfPageBySql(sql.toString(), dp, params);
    	dp.setDatas(list);
    	dp.setRowCount(list.size());
		return dp;
	}


	/**
	 * 一对一课程审批与查看汇总(小时)
	 *
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOneOnOneAuditAnalyzeListXiaoShi(CourseVo inputCourseVo, DataPackage dp) {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select org.*, ah.teacherName,ah.workType , ah.TEACHER_ID as teacherId, ah.teacherBlcampusName, ah.unAuditHours, ah.validateHours, ah.unValidateHours, gh.* ,ah.teacherLevel,ah.teacherType  ");
		sql.append(" from (select c.BL_CAMPUS_ID, c.TEACHER_ID, u.`NAME` as teacherName, u.WORK_TYPE as 'workType' , org.`name` as teacherBlcampusName, ");
		sql.append(" ROUND(ifnull(sum(case when (c.AUDIT_STATUS is null or c.AUDIT_STATUS = 'UNAUDIT') then c.REAL_HOURS*c.COURSE_MINUTES/60    else 0 end ), 0),2) as unAuditHours, ");
		sql.append(" ROUND(ifnull(sum(case when c.AUDIT_STATUS='VALIDATE' then c.REAL_HOURS*c.COURSE_MINUTES/60 else 0 end ), 0),2) as validateHours, ");
		sql.append(" ROUND(ifnull(sum(case when c.AUDIT_STATUS='UNVALIDATE' then c.REAL_HOURS*c.COURSE_MINUTES/60 else 0 end),0),2) as unValidateHours ");
		
    	sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =c.teacher_id) and TEACHER_ID=c.teacher_id) teacherType");

		params.put("endDate", inputCourseVo.getEndDate());

		sql.append(" from course c left join user u on u.USER_ID = c.TEACHER_ID ");
		// 取人事部门的组织架构 start
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		//取人事部门的组织架构 end
		sql.append(" where c.BL_CAMPUS_ID = :campusId ");
		params.put("campusId", inputCourseVo.getCampusId());
		//主职位
		sql.append(" and udj.isMajorRole = 0");
		sql.append(" and c.COURSE_DATE >= :startDate  and c.COURSE_DATE <= :endDate ");
		params.put("startDate", inputCourseVo.getStartDate());
		sql.append(" and c.COURSE_STATUS = 'CHARGED' ");

		//科目筛选
		if (StringUtil.isNotBlank(inputCourseVo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(inputCourseVo.getSubject()).split(",");
			if(subjects.length>0){
//				sql.append(" and ( 1=0 ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or c.subject ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");
				sql.append(" and c.subject in (:subjects)");
				params.put("subjects", subjects);
			}
		}

		if (StringUtils.isNotBlank(inputCourseVo.getTeacherId())) {
			sql.append(" and c.TEACHER_ID = :teacherId ");
			params.put("teacherId", inputCourseVo.getTeacherId());
		}
		sql.append(" group by c.BL_CAMPUS_ID, c.TEACHER_ID) ah ");
		sql.append(" left join (select BL_CAMPUS_ID, TEACHER_ID, max(IF(gradeName = '一年级',amount,0)) as 'gradeOneHours', ");
		sql.append(" max(IF(gradeName = '二年级',amount,0)) as 'gradeTwoHours', max(IF(gradeName = '三年级',amount,0)) as 'gradeThreeHours', ");
		sql.append(" max(IF(gradeName = '四年级',amount,0)) as 'gradeFourHours', max(IF(gradeName = '五年级',amount,0)) as 'gradeFiveHours',  ");
		sql.append(" max(IF(gradeName = '六年级',amount,0)) as 'gradeSixHours', max(IF(gradeName = '初一',amount,0)) as 'gradeSevenHours', ");
		sql.append(" max(IF(gradeName = '初二',amount,0)) as 'gradeEightHours',  max(IF(gradeName = '初三',amount,0)) as 'gradeNineHours', ");
		sql.append(" max(IF(gradeName = '高一',amount,0)) as 'gradeTenHours',  max(IF(gradeName = '高二',amount,0)) as 'gradeElevenHours', ");
		sql.append(" max(IF(gradeName = '高三',amount,0)) as 'gradeTwelveHours' ");
		sql.append(" from (select ROUND(sum(c.REAL_HOURS*c.COURSE_MINUTES/60),2) as amount, g.NAME as gradeName, c.BL_CAMPUS_ID, c.TEACHER_ID, c.GRADE  ");
		sql.append(" from course c inner join (select ID, NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on c.GRADE = g.ID ");
		sql.append(" where c.BL_CAMPUS_ID = :campusId and c.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and c.COURSE_DATE >= :startDate and c.COURSE_DATE <= :endDate ");
		sql.append(" and c.COURSE_STATUS = 'CHARGED' ");

		//科目筛选
		if (StringUtil.isNotBlank(inputCourseVo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(inputCourseVo.getSubject()).split(",");
			if(subjects.length>0){
				sql.append(" and  c.subject in (:subjects) ");
				params.put("subjects", subjects);
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or c.subject ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");
			}
		}

		if (StringUtils.isNotBlank(inputCourseVo.getTeacherId())) {
			sql.append(" and c.TEACHER_ID = :teacherId ");
			params.put("teacherId", inputCourseVo.getTeacherId());
		}


		sql.append(" group by c.BL_CAMPUS_ID, c.TEACHER_ID, c.GRADE) d group by d.BL_CAMPUS_ID, d.TEACHER_ID) gh on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.TEACHER_ID = gh.TEACHER_ID ");
		sql.append(" inner join (select CONCAT(org_group.id, '') as groupId, org_group.name as groupName, CONCAT(org_brench.id, '') as brenchId, ");
		sql.append(" org_brench.name as brenchName, CONCAT(org_campus.id, '') as campusId, org_campus.name as campusName ");
		sql.append(" from organization org_campus ");
		sql.append(" LEFT JOIN organization org_brench on org_campus.parentID = org_brench.id ");
		sql.append(" LEFT JOIN organization org_group on org_brench.parentID = org_group.id ");
		sql.append(" where org_campus.orgType = 'CAMPUS' and org_campus.id = :campusId ) org on ah.BL_CAMPUS_ID = org.campusId ");
		params.put("campusId", inputCourseVo.getCampusId());
		if (StringUtils.isNotBlank(inputCourseVo.getHasUnauditStatus())) {
			if ("HAS_UNAUDIT".equals(inputCourseVo.getHasUnauditStatus())) {
				sql.append(" and ah.unAuditHours > 0 ");
			} else {
				sql.append(" and ah.unAuditHours <= 0 ");
			}
		}
		
		if(StringUtils.isNotBlank(inputCourseVo.getTeacherType())){
			sql.append(" and ah.teacherType='"+inputCourseVo.getTeacherType()+"'");
		}

		List<Map<Object, Object>> list = super.findMapOfPageBySql(sql.toString(), dp, params);
		dp.setDatas(list);
		dp.setRowCount(list.size());
		return dp;
	}

	/**
	 * 小班课时汇总
	 */
	public DataPackage MiniClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String miniClassTypeId){
		String orgLevel=null;
		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select ");
		sql.append(" concat(grounp.name) 'GROUNP', ");
		sql.append(" concat(brench.name) 'BRENCH', ");
		sql.append(" concat(campus.id,\"_\",campus.name) 'CAMPUS', ");
		sql.append(" count(1) 'allcourse', ");		//已排课次
		sql.append(" IFNULL(sum(case when mcc.COURSE_STATUS = 'NEW' then 1 else 0 end ),0) 'newcourse' , ");  //未上课
		sql.append(" IFNULL(sum(case when mcc.COURSE_STATUS = 'TEACHER_ATTENDANCE' then 1 else 0 end ),0) 'teacherAttendance', ");//老师已考勤
		sql.append(" IFNULL(sum(case when mcc.COURSE_STATUS = 'CHARGED' then 1 else 0 end ),0) 'charged', ");  //班主任已结算
		sql.append(" IFNULL(sum(mcsa.newstudent), 0) 'newstudent', ");   //未上课学生数
		sql.append(" IFNULL(sum(mcsa.conpelete), 0) 'conpelete', ");    //上课学生数
		sql.append(" IFNULL(sum(mcsa.late), 0) 'late', ");   //迟到学生数
		sql.append(" IFNULL(sum(mcsa.absent), 0) 'absent', ");   //缺勤学生数
		sql.append(" IFNULL(sum(mcsa.chargedstu), 0) 'chargedstu', ");  //已结算学生数
		sql.append(" IFNULL(sum(mcsa.unchargestu), 0) 'unchargestu'    ,"); //未结算学生数
		sql.append(" sum(case when mcc.COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end )  'unaudit' ,");  //未审课程
		sql.append(" sum(case when mcc.COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and mcc.AUDIT_STATUS = 'VALIDATE' then 1 else 0 end )  'audit',");  //已审课程
		sql.append(" sum(case when mcc.COURSE_STATUS = 'CHARGED' and (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS  <> 'VALIDATE') then 1 else 0 end )  'financeUnaudit' ,");  //未审课程
		sql.append(" sum(case when mcc.COURSE_STATUS = 'CHARGED' and mcc.AUDIT_STATUS = 'VALIDATE' then 1 else 0 end )  'financeAudit'");  //已审课程
		sql.append(" from mini_class_course mcc ");
		sql.append(" INNER JOIN mini_class mc ON mcc.MINI_CLASS_ID =mc.MINI_CLASS_ID ");
		sql.append(" INNER JOIN product p ON p.ID =mc.PRODUCE_ID ");
		sql.append(" INNER JOIN organization campus on campus.id=mc.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN organization brench on campus.parentID=brench.id ");
		sql.append(" INNER JOIN organization grounp on brench.parentID=grounp.id ");

		sql.append(" LEFT JOIN (select mcc.MINI_CLASS_COURSE_ID, IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'NEW' then 1 else 0 end ), 0) 'newstudent', ");
		sql.append(" 	IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ), 0) 'conpelete', ");
		sql.append(" 	IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ), 0) 'late', ");
		sql.append(" 	IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'ABSENT' then 1 else 0 end ), 0) 'absent', ");
		sql.append(" 	IFNULL(sum(case when mcsa.CHARGE_STATUS = 'CHARGED' then 1 else 0 end ), 0) 'chargedstu', ");
		sql.append(" 	IFNULL(sum(case when mcsa.CHARGE_STATUS = 'UNCHARGE' then 1 else 0 end ), 0) 'unchargestu'");
		sql.append(" 	from mini_class_student_attendent mcsa  ");
		sql.append(" 	INNER JOIN mini_class_course mcc on mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID  ");
		sql.append(" 	INNER JOIN mini_class mc on mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
		sql.append(" 	where 1=1 ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.COURSE_DATE >= :startDate ");
			params.put("startDate",startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.COURSE_DATE <= :endDate  ");
			params.put("endDate", endDate);
		}
		sql.append(" 	group by mcc.MINI_CLASS_COURSE_ID) mcsa on mcsa.MINI_CLASS_COURSE_ID = mcc.MINI_CLASS_COURSE_ID ");


		sql.append(" where 1=1 ");
		sql.append(" and mcc.COURSE_STATUS <> 'CANCEL' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}
		sql.append(" and mcc.COURSE_STATUS <> 'CANCEL' ");

		if(StringUtils.isNotBlank(miniClassTypeId)){
			String[] miniClassTypes=StringUtil.replaceSpace(miniClassTypeId).split(",");
			if(miniClassTypes.length>0){
//				sql.append(" and (p.CLASS_TYPE_ID ='"+miniClassTypes[0]+"' ");
//				for (int i = 1; i < miniClassTypes.length; i++) {
//					sql.append(" or p.CLASS_TYPE_ID ='"+miniClassTypes[i]+"' ");
//				}
//				sql.append(" )");
				sql.append(" and p.CLASS_TYPE_ID in (:miniClassTypes) ");
				params.put("miniClassTypes", miniClassTypes);
			}
		}

		sql.append(" group by mc.BL_CAMPUS_ID");

		dataPackage = this.findMapPageBySQL(sql.toString(), dataPackage, true, params);
		List<Map<String,Object>> list = (List<Map<String,Object>>)dataPackage.getDatas();

        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double charged1 = Double.valueOf(o1.get("charged").toString());
                Double total1 = Double.valueOf(o1.get("allcourse").toString());
                Double charged2 = Double.valueOf(o2.get("charged").toString());
                Double total2 = Double.valueOf(o2.get("allcourse").toString());
                Double chargedPercent1 = charged1 / total1;
                Double chargedPercent2 = charged2 / total2;

                if(chargedPercent1.compareTo(chargedPercent2)>0){
					return -1;
				}else if(chargedPercent1.compareTo(chargedPercent2)==0){
					return 0;
				}else{
					return 1;
				}
            }
        });

		return dataPackage;
	}
	
	/**
	 * 一对多课时汇总
	 */
	@Override
	public DataPackage getOtmClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String otmClassTypeId) {
		String orgLevel=null;
		if(organizationIdFinder != null && !"".equals(organizationIdFinder)){
			orgLevel = organizationDao.findById(organizationIdFinder).getOrgLevel();
		}
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select concat(grounp.name) 'GROUNP', concat(brench.name) 'BRENCH', concat(campus.id,\"_\",campus.name) 'CAMPUS', ");
		sql.append(" count(1) 'allcourse', ");		//已排课次
		sql.append(" IFNULL(sum(case when occ.COURSE_STATUS = 'NEW' then 1 else 0 end ), 0) 'newcourse', ");  //未上课
		sql.append(" IFNULL(sum(case when occ.COURSE_STATUS = 'TEACHER_ATTENDANCE' then 1 else 0 end ), 0) 'teacherAttendance', ");//老师已考勤
		sql.append(" IFNULL(sum(case when occ.COURSE_STATUS = 'CHARGED' then 1 else 0 end ), 0) 'charged', ");  //班主任已结算
		sql.append(" IFNULL(sum(ocsa.newstudent), 0) 'newstudent', ");   //未上课学生数
		sql.append(" IFNULL(sum(ocsa.conpelete), 0) 'conpelete', ");    //上课学生数
		sql.append(" IFNULL(sum(ocsa.late), 0) 'late', ");   //迟到学生数
		sql.append(" IFNULL(sum(ocsa.absent), 0) 'absent', ");   //缺勤学生数
		sql.append(" IFNULL(sum(ocsa.chargedstu), 0) 'chargedstu', ");  //已结算学生数
		sql.append(" IFNULL(sum(ocsa.unchargestu), 0) 'unchargestu' , "); //未结算学生数
		sql.append(" sum(case when occ.COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and (occ.AUDIT_STATUS is null  or occ.AUDIT_STATUS <> 'VALIDATE') then 1 else 0 end )  'unaudit' ,");  //未审课程
		sql.append(" sum(case when occ.COURSE_STATUS in ('NEW','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED','CHARGED') and occ.AUDIT_STATUS = 'VALIDATE' then 1 else 0 end )  'audit', ");  //已审课程
		sql.append(" sum(case when occ.COURSE_STATUS = 'CHARGED' and (occ.AUDIT_STATUS is null  or occ.AUDIT_STATUS <> 'VALIDATE') then 1 else 0 end )  'financeUnaudit' ,");  //未审课程
		sql.append(" sum(case when occ.COURSE_STATUS = 'CHARGED' and occ.AUDIT_STATUS = 'VALIDATE' then 1 else 0 end )  'financeAudit'");  //已审课程
		sql.append(" FROM otm_class_course occ ");
		sql.append(" INNER JOIN otm_class oc ON occ.OTM_CLASS_ID =oc.OTM_CLASS_ID ");
		sql.append(" INNER JOIN organization campus ON campus.id=oc.BL_CAMPUS_ID ");
		sql.append(" INNER JOIN organization brench ON campus.parentID=brench.id ");
		sql.append(" INNER JOIN organization grounp ON brench.parentID=grounp.id ");
		
		sql.append(" LEFT JOIN ( select occ.OTM_CLASS_COURSE_ID, ");
		sql.append(" 	IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'NEW' then 1 else 0 end ), 0) 'newstudent', ");
		sql.append(" 	IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ), 0) 'conpelete', ");
		sql.append(" 	IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ), 0) 'late', ");
		sql.append(" 	IFNULL(sum(case when ocsa.ATTENDENT_STATUS = 'ABSENT' then 1 else 0 end ), 0) 'absent', ");
		sql.append(" 	IFNULL(sum(case when ocsa.CHARGE_STATUS = 'CHARGED' then 1 else 0 end ), 0) 'chargedstu', ");
		sql.append(" 	IFNULL(sum(case when ocsa.CHARGE_STATUS = 'UNCHARGE' then 1 else 0 end ), 0) 'unchargestu'");
		sql.append(" 	FROM otm_class_student_attendent ocsa ");
		sql.append(" 	INNER JOIN otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = Occ.OTM_CLASS_COURSE_ID ");
		sql.append(" 	where 1=1 ");
		
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and occ.COURSE_DATE >= :startDate ");
			params.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and occ.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		sql.append(" 	group by occ.OTM_CLASS_COURSE_ID) ocsa on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
		
		sql.append(" where 1=1 ");
		sql.append(" and occ.COURSE_STATUS <> 'CANCEL' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and occ.COURSE_DATE >=  :startDate ");
			params.put("startDate", startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and occ.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if(orgLevel!=null && !orgLevel.equals("")){
			sql.append(" and campus.orgLevel LIKE :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}
		sql.append(" and occ.COURSE_STATUS <> 'CANCEL' ");

		if(StringUtils.isNotBlank(otmClassTypeId)){
			String[] otmClassTypes=StringUtil.replaceSpace(otmClassTypeId).split(",");
			if(otmClassTypes.length>0){
//				sql.append(" and (oc.OTM_TYPE ='"+otmClassTypes[0]+"' ");
//				for (int i = 1; i < otmClassTypes.length; i++) {
//					sql.append(" or oc.OTM_TYPE ='"+otmClassTypes[i]+"' ");
//				}
//				sql.append(" )");
				sql.append(" and oc.OTM_TYPE in(:otmClassTypes) ");
				params.put("otmClassTypes", otmClassTypes);
			}
		}


		
		sql.append(" group by oc.BL_CAMPUS_ID");
		
//		List<Map<String,Object>> list=super.findMapOfPageBySql(sql.toString(), dataPackage);
//		List<Map<String,Object>> list=jdbcTemplateDao.queryPage(sql.toString(), dataPackage);
		List<Map<String,Object>> list=(List<Map<String,Object>>)this.findMapPageBySQL(sql.toString(), dataPackage, true, params).getDatas();
		dataPackage.setDatas(list);
		dataPackage.setRowCount(this.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
		
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double charged1 = Double.valueOf(o1.get("charged").toString());
                Double total1 = Double.valueOf(o1.get("allcourse").toString());
                Double charged2 = Double.valueOf(o2.get("charged").toString());
                Double total2 = Double.valueOf(o2.get("allcourse").toString());
                Double chargedPercent1 = charged1 / total1;
                Double chargedPercent2 = charged2 / total2;
              
                if(chargedPercent1.compareTo(chargedPercent2)>0){
					return -1;
				}else if(chargedPercent1.compareTo(chargedPercent2)==0){
					return 0;
				}else{
					return 1;
				}
            }
        });
        					
		return dataPackage;
	}
	
	/**
	 * 小班课程审批汇总
	 * 
	 */
	
	@Override
	public DataPackage getMiniClassCourseAuditAnalyze(DataPackage dataPackage, BasicOperationQueryVo vo, String AuditStatus, String productQuarterSearch){
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select ");
		sql.append("org.*, ah.teacher, ah.workType ,ah.teacherBlCampus, ah.teacherId, ah.miniClassName,ah.validateHours, gh.*,ah.teacherLevel,ah.teacherType");
		sql.append(" from ( select  u.name as 'teacher', u.WORK_TYPE as 'workType' , org.name as 'teacherBlCampus',mc.name as 'miniClassName',mc.BL_CAMPUS_ID,mcc.grade as 'grade', mcc.TEACHER_ID as 'teacherId',mcc.MINI_CLASS_COURSE_ID as miniCourseId,");
//		sql.append(" IFNULL(sum(case when (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS = 'UNAUDIT') then mcc.course_hours else 0 end),0) as 'unAuditHours', ");		
		sql.append(" IFNULL(sum(mcc.course_hours),0) as 'validateHours' ");
//		sql.append(" IFNULL(sum(case when mcc.AUDIT_STATUS = 'UNVALIDATE' then mcc.course_hours else 0 end ),0) as 'unValidateHours' ");	
		
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherLevel,");
    	params.put("endDate", vo.getEndDate());
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherType");
		
		sql.append(" from mini_class_course mcc ");
		sql.append(" LEFT JOIN mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN product p on p.id=mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN user u on mcc.TEACHER_ID=u.USER_ID ");
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		sql.append(" where 1=1");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", vo.getBlCampusId());
		}
		sql.append(" and mcc.course_date >= :startDate ");
		sql.append(" and mcc.course_date <= :endDate ");
		sql.append(" and mcc.course_status = 'CHARGED'");
		sql.append(" and udj.isMajorRole = 0");
		params.put("startDate",vo.getStartDate());
		if(productQuarterSearch!=null && StringUtils.isNotBlank(productQuarterSearch)){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id= :productQuarterSearch ) ");
			params.put("productQuarterSearch",productQuarterSearch);
		}
		if(StringUtils.isNotBlank(vo.getMiniClassTypeId())){
			if(StringUtils.isNotBlank(vo.getMiniClassTypeId())){			
				String[] miniClassTypes=StringUtil.replaceSpace(vo.getMiniClassTypeId()).split(",");
				if(miniClassTypes.length>0){
					sql.append(" AND p.CLASS_TYPE_ID in (:miniClassTypes) ");
					params.put("miniClassTypes", miniClassTypes);
				}
			}
		}

			//科目筛选
			if (StringUtil.isNotBlank(vo.getSubject())){
				String[] subjects=StringUtil.replaceSpace(vo.getSubject()).split(",");
				if(subjects.length>0){
					sql.append(" and mcc.SUBJECT in (:subjects) ");
					params.put("subjects", subjects);
				}
			}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId",vo.getTeacherId());
		}
		sql.append(" group by mc.BL_CAMPUS_ID ,mcc.MINI_CLASS_ID ,mcc.TEACHER_ID ) ah");
		sql.append(" LEFT JOIN ( ");
		sql.append(" select bl_campus_id,teacher_id, GRADE,MINI_CLASS_COURSE_ID,");
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
		sql.append(" IFNULL(sum(IF(gradeName = '一年级',stuNums,0)),0) as 'stuNums1', ");
        sql.append(" IFNULL(sum(IF(gradeName = '二年级',stuNums,0)),0) as 'stuNums2',");
        sql.append(" IFNULL(sum(IF(gradeName = '三年级',stuNums,0)),0) as 'stuNums3',");
        sql.append(" IFNULL(sum(IF(gradeName = '四年级',stuNums,0)),0) as 'stuNums4',");
        sql.append(" IFNULL(sum(IF(gradeName = '五年级',stuNums,0)),0) as 'stuNums5',");
        sql.append(" IFNULL(sum(IF(gradeName = '六年级',stuNums,0)),0) as 'stuNums6',");
        sql.append(" IFNULL(sum(IF(gradeName = '初一',stuNums,0)),0) as 'stuNums7',");
        sql.append(" IFNULL(sum(IF(gradeName = '初二',stuNums,0)),0) as 'stuNums8',");
        sql.append(" IFNULL(sum(IF(gradeName = '初三',stuNums,0)),0) as 'stuNums9',");
        sql.append(" IFNULL(sum(IF(gradeName = '高一',stuNums,0)),0) as 'stuNums10',");
        sql.append(" IFNULL(sum(IF(gradeName = '高二',stuNums,0)),0) as 'stuNums11',");
        sql.append(" IFNULL(sum(IF(gradeName = '高三',stuNums,0)),0) as 'stuNums12', ");
        sql.append(" IFNULL(max(IF(gradeName is null,stuNums,0)),0) as 'otherStuNums' ");
        
		sql.append(" from ");          
		sql.append(" (select  ");
		sql.append(" sum(mcc.course_hours) as amount, ");
		sql.append(" mc.MINI_CLASS_ID,  g.NAME as gradeName,  mc.BL_CAMPUS_ID,mcc.MINI_CLASS_COURSE_ID, mcc.TEACHER_ID, mcc.GRADE, sum(charge) charge,  ");
		sql.append(" ifnull(sum(CASE WHEN "
	                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = mcc.TEACHER_ID  AND tv.VERSION_DATE <= mcc.COURSE_DATE) AND TEACHER_ID = mcc.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
	                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  mini_class_course mcc ");
		sql.append(" left JOIN( ");
		sql.append(" select  ");
		sql.append(" mcc.MINI_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS 'charge' ");
		sql.append(" from mini_class_student_attendent mcsa ");
		sql.append(" LEFT JOIN mini_class_course mcc on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID where mcc.course_status = 'CHARGED' ");
		sql.append(" and mcc.COURSE_DATE >= '"+vo.getStartDate()+"'  ");
		sql.append(" and mcc.COURSE_DATE <= '"+vo.getEndDate()+"'  ");
		sql.append(" group by mcc.mini_class_course_id ) a on mcc.MINI_CLASS_COURSE_ID=a.MINI_CLASS_COURSE_ID ");
		sql.append(" left join mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on mcc.GRADE = g.ID ");
		sql.append("  where 1=1 ");
		sql.append(" and mc.BL_CAMPUS_ID = '"+vo.getBlCampusId()+"'");
		sql.append(" and mcc.COURSE_DATE >= '"+vo.getStartDate()+"'  ");
		sql.append(" and mcc.COURSE_DATE <= '"+vo.getEndDate()+"'  ");
		sql.append(" and mcc.COURSE_STATUS = 'CHARGED'  ");
		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = '"+vo.getTeacherId()+"' ");
		}
		sql.append("  group by ");		
		sql.append("  mc.BL_CAMPUS_ID,mc.MINI_CLASS_ID, mcc.TEACHER_ID  ) d  ");//, mcc.MINI_CLASS_COURSE_ID

		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID,d.TEACHER_ID,d.MINI_CLASS_ID) gh  ");		 
		sql.append(" on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.teacherId = gh.TEACHER_ID and ah.grade=gh.GRADE and ah.miniCourseId=gh.MINI_CLASS_COURSE_ID");
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
		sql.append(" and org_campus.id = '"+vo.getBlCampusId()+"'  ) org ");
		sql.append(" on ah.BL_CAMPUS_ID = org.campusId ");
		
		if(StringUtils.isNotBlank(vo.getTeacherType())){
			sql.append(" and ah.teacherType=  :teacherType ");
			params.put("teacherType",vo.getTeacherType());
		}
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage, params);
		
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
		
	}

	/**
	 * 小班课程审批汇总(小时)
	 *
	 * @param dataPackage
	 * @param auditStatus
	 * @return
	 */
	@Override
	public DataPackage getMiniClassCourseAuditAnalyzeXiaoShi(DataPackage dataPackage, BasicOperationQueryVo vo, String auditStatus, String productQuarterSearch) {
		StringBuilder sql=new StringBuilder();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select ");
		sql.append("org.*, ah.teacher, ah.workType ,ah.teacherBlCampus, ah.teacherId, ah.miniClassName,ah.validateHours,gh.*,ah.teacherLevel,ah.teacherType");
		sql.append(" from ( select  u.name as 'teacher', u.WORK_TYPE as 'workType' , org.name as 'teacherBlCampus',mc.name as 'miniClassName',mc.BL_CAMPUS_ID,mcc.grade as 'grade', mcc.TEACHER_ID as 'teacherId',mcc.MINI_CLASS_COURSE_ID as miniCourseId,");
//		sql.append(" ROUND(IFNULL(sum(case when (mcc.AUDIT_STATUS is null or mcc.AUDIT_STATUS = 'UNAUDIT') then mcc.course_hours*mcc.COURSE_MINUTES/60 else 0 end),0),2) as 'unAuditHours', ");
		sql.append(" ROUND(IFNULL(sum(mcc.course_hours*mcc.COURSE_MINUTES/60),0),2) as 'validateHours' ");
//		sql.append(" ROUND(IFNULL(sum(case when mcc.AUDIT_STATUS = 'UNVALIDATE' then mcc.course_hours*mcc.COURSE_MINUTES/60 else 0 end ),0),2) as 'unValidateHours' ");
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherType");
		
		sql.append(" from mini_class_course mcc ");
		sql.append(" LEFT JOIN mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN product p on p.id=mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN user u on mcc.TEACHER_ID=u.USER_ID ");
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		sql.append(" where 1=1");
		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
		sql.append(" and mcc.course_date >= :startDate ");
		sql.append(" and mcc.course_date <= :endDate ");
		sql.append(" and mcc.course_status = 'CHARGED'");
		sql.append(" and udj.isMajorRole = 0");
		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
		params.put("blCampusId", vo.getBlCampusId());

		if(productQuarterSearch!=null && StringUtils.isNotBlank(productQuarterSearch)){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id= :productQuarterSearch ) ");
			params.put("productQuarterSearch",productQuarterSearch);
		}
		if(StringUtils.isNotBlank(vo.getMiniClassTypeId())){
			if(StringUtils.isNotBlank(vo.getMiniClassTypeId())){
				String[] miniClassTypes=StringUtil.replaceSpace(vo.getMiniClassTypeId()).split(",");
				if(miniClassTypes.length>0){
					sql.append(" AND p.CLASS_TYPE_ID in (:miniClassTypes) ");
					params.put("miniClassTypes",miniClassTypes);
				}
			}
		}

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(vo.getSubject()).split(",");
			if(subjects.length>0){
				sql.append(" and mcc.SUBJECT in (:subjects) ");
				params.put("subjects", subjects);
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId",vo.getTeacherId());
		}
		sql.append(" group by mc.BL_CAMPUS_ID ,mcc.MINI_CLASS_ID ,mcc.TEACHER_ID ) ah");
		sql.append(" LEFT JOIN ( ");
		sql.append(" select bl_campus_id,teacher_id, GRADE,MINI_CLASS_COURSE_ID,");
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
		sql.append(" IFNULL(max(IF(gradeName is null ,amount,0)),0) as 'otherHours', ");
		sql.append(" IFNULL(sum(IF(gradeName = '一年级',stuNums,0)),0) as 'stuNums1', ");
		sql.append(" IFNULL(sum(IF(gradeName = '二年级',stuNums,0)),0) as 'stuNums2',");
		sql.append(" IFNULL(sum(IF(gradeName = '三年级',stuNums,0)),0) as 'stuNums3',");
		sql.append(" IFNULL(sum(IF(gradeName = '四年级',stuNums,0)),0) as 'stuNums4',");
		sql.append(" IFNULL(sum(IF(gradeName = '五年级',stuNums,0)),0) as 'stuNums5',");
		sql.append(" IFNULL(sum(IF(gradeName = '六年级',stuNums,0)),0) as 'stuNums6',");
		sql.append(" IFNULL(sum(IF(gradeName = '初一',stuNums,0)),0) as 'stuNums7',");
		sql.append(" IFNULL(sum(IF(gradeName = '初二',stuNums,0)),0) as 'stuNums8',");
		sql.append(" IFNULL(sum(IF(gradeName = '初三',stuNums,0)),0) as 'stuNums9',");
		sql.append(" IFNULL(sum(IF(gradeName = '高一',stuNums,0)),0) as 'stuNums10',");
		sql.append(" IFNULL(sum(IF(gradeName = '高二',stuNums,0)),0) as 'stuNums11',");
		sql.append(" IFNULL(sum(IF(gradeName = '高三',stuNums,0)),0) as 'stuNums12', ");
		sql.append(" IFNULL(sum(IF(gradeName is null,stuNums,0)),0) as 'otherStuNums' ");

		sql.append(" from ");
		sql.append(" (select  ");
		sql.append(" ROUND(sum(mcc.course_hours*mcc.COURSE_MINUTES/60),2) as amount, ");
		sql.append(" mc.MINI_CLASS_ID,  g.NAME as gradeName,  mc.BL_CAMPUS_ID,mcc.MINI_CLASS_COURSE_ID, mcc.TEACHER_ID, mcc.GRADE, sum(charge) charge,  ");
		sql.append(" ifnull(sum(CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = mcc.TEACHER_ID  AND tv.VERSION_DATE <= mcc.COURSE_DATE) AND TEACHER_ID = mcc.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  mini_class_course mcc ");
		sql.append(" left JOIN( ");
		sql.append(" select  ");
		sql.append(" mcc.MINI_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS 'charge' ");
		sql.append(" from mini_class_student_attendent mcsa ");
		sql.append(" LEFT JOIN mini_class_course mcc on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID where mcc.course_status = 'CHARGED' ");
		sql.append(" and mcc.COURSE_DATE >= :startDate  ");
		sql.append(" and mcc.COURSE_DATE <= :endDate  ");
		sql.append(" group by mcc.mini_class_course_id ) a on mcc.MINI_CLASS_COURSE_ID=a.MINI_CLASS_COURSE_ID ");
		sql.append(" left join mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on mcc.GRADE = g.ID ");
		sql.append("  where 1=1 ");
		sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
		sql.append(" and mcc.COURSE_DATE >= :startDate  ");
		sql.append(" and mcc.COURSE_DATE <= :endDate  ");
		sql.append(" and mcc.COURSE_STATUS = 'CHARGED'  ");
		params.put("startDate", vo.getStartDate());
		params.put("endDate", vo.getEndDate());
		params.put("blCampusId", vo.getBlCampusId());
		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId",vo.getTeacherId());
		}
		sql.append("  group by ");
		sql.append("  mc.BL_CAMPUS_ID,mc.MINI_CLASS_ID, mcc.TEACHER_ID ) d "); //, mcc.MINI_CLASS_COURSE_ID
		
		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID,d.TEACHER_ID,d.MINI_CLASS_ID) gh  ");
		sql.append(" on ah.BL_CAMPUS_ID = gh.BL_CAMPUS_ID and ah.teacherId = gh.TEACHER_ID and ah.grade=gh.GRADE and ah.miniCourseId=gh.MINI_CLASS_COURSE_ID");
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
		sql.append(" and org_campus.id = :blCampusId  ) org ");
		params.put("blCampusId",vo.getBlCampusId());
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
			params.put("teacherType",vo.getTeacherType());
		}
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage, params);

		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
	}

	/**
	 * 学生一对一已排的课时 ：未结算课时（未上课，老师已考勤，学管已核对）
	 * @param studentId
	 * @return
	 */
    @Override
    public BigDecimal countOneOnOneNotChargeCourse(String studentId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		hql.append(" select sum(c.planHours) from Course c where 1=1 ");
		hql.append(" and c.student.id = :studentId  ");
		params.put("studentId",studentId);
		hql.append(" and c.courseStatus in ('NEW', 'STUDENT_ATTENDANCE', 'TEACHER_ATTENDANCE', 'STUDY_MANAGER_AUDITED')");
		double hours = super.findSumHql(hql.toString(), params);
		return BigDecimal.valueOf(hours);
    }

    /**
	 * 小班批量考勤列表（小时）
	 *
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @param subject
	 * @return
	 */
	@Override
	public DataPackage miniClassCourseAuditListXiaoshi(DataPackage dataPackage, String startDate, String endDate, String campusId, String teacherId, String auditStatus, String subject,
			String productQuarterSearch) {
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
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select mcc.ATTENDANCE_PIC_NAME as attendacePicName, ");
		sql.append(" campus.name as 'campusName', mc.name as 'className',d.name as 'grade',mcc.grade as 'gradeValue',mcc.mini_class_course_id as 'courseId',u_teacher.name as 'teacher', u_study.name as 'studyManeger',dd_subject.name as 'subjectName', ");
		sql.append("  mcc.course_date as 'courseDate',mcc.course_time as 'startTime',mcc.course_end_time as 'endTime',mcc.course_hours as 'courseHours',mcc.COURSE_MINUTES as 'courseTimeLong', mcc.AUDIT_STATUS as 'auditStatus',");
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ),0) 'conplete',");  //上课人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'NEW' then 1 else 0 end ),0) 'news',");  //未上课人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ),0) 'late', ");   //迟到人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'ABSENT' then 1 else 0 end ),0) 'absent', ");   //缺勤人数
		sql.append(" IFNULL(sum(case when mcsa.CHARGE_STATUS = 'CHARGED' then 1 else 0 end ),0) 'charged', ");   //扣费人数
		sql.append(" IFNULL(sum(case when mcsa.CHARGE_STATUS = 'UNCHARGE' then 1 else 0 end ),0) 'uncharge', ");     //未扣费人数
		sql.append(" IFNULL(sum(case when (mcsa.ATTENDENT_STATUS in ( 'LEAVE','ABSENT' ) and mcsa.SUPPLEMENT_DATE is not null) then 1 else 0 end ),0) as 'notleave', "); //补课人数
		sql.append(" ROUND(IFNULL(case when mcc.AUDIT_STATUS='VALIDATE' then mcc.course_hours else 0 end ,0),2)  as 'auditHours', ");   //有效课时
		sql.append(" ROUND(IFNULL(case when mcc.audit_status = 'UNVALIDATE' then mcc.course_hours else 0 end ,0),2) as 'cancelHours',  ");  //无效课时
		sql.append(" ROUND(IFNULL(case when (mcc.audit_status is null or mcc.audit_status = 'UNAUDIT') then mcc.course_hours else 0 end ,0),2) as 'unauditHours'  ");  //未审批
		sql.append(" from mini_class_course mcc  ");
		sql.append(" LEFT JOIN mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN mini_class_student_attendent mcsa on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN user u on u.USER_ID=mcc.teacher_id");
		sql.append(" LEFT JOIN ( select id,name from data_dict where CATEGORY = 'STUDENT_GRADE' ) d on mcc.grade=d.id ");
		sql.append(" LEFT JOIN organization campus on campus.id=mc.BL_CAMPUS_ID");
		sql.append(" INNER JOIN organization brench on brench.id=campus.parentID");
		sql.append(" INNER JOIN organization grounp on grounp.id=brench.parentID");
		sql.append(" INNER JOIN user u_teacher on mcc.teacher_id=u_teacher.USER_ID ");
		sql.append(" INNER JOIN user u_study on mcc.study_maneger_id = u_study.USER_ID ");
		sql.append(" INNER JOIN data_dict dd_subject on mcc.subject=dd_subject.ID ");
		sql.append(" where 1=1 ");
		sql.append(" and mcc.course_status = 'CHARGED' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.course_date >= :startDate ");
			params.put("startDate",startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.course_date <= :endDate ");
			params.put("endDate",endDate);
		}
		if(orgLevel!=null && !"".equals(orgLevel)){
			sql.append(" and campus.orgLevel like :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}
		if(auditStatus!=null && !"".equals(auditStatus)){
			if(auditStatus.equals("UNAUDIT")){
				//未审批
				sql.append(" and (mcc.audit_status = 'UNAUDIT' or mcc.audit_status is null )");
			}else{
				sql.append(" and mcc.audit_status= :auditStatus ");
				params.put("auditStatus", auditStatus);
			}

		}

		//科目筛选
		if (StringUtil.isNotBlank(subject)){
			String[] subjects=StringUtil.replaceSpace(subject).split(",");
			if(subjects.length>0){
//				sql.append(" and ( 1=0 ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or mcc.SUBJECT ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");
				sql.append(" and mcc.SUBJECT in (:subjects) ");
				params.put("subjects",subjects);
			}
		}

		if(teacherId!=null && !"".equals(teacherId)){
			sql.append(" and mcc.teacher_id = :teacherId ");
			params.put("teacherId",teacherId);
		}
		sql.append(" group by mcc.mini_class_course_id,mc.BL_CAMPUS_ID ");

		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
	}

	/**
	 * 小班批量考勤列表(课时)
	 */
	@Override
	public DataPackage miniClassCourseAuditList(DataPackage dataPackage, String startDate, String endDate,
												String campusId, String teacherId, String auditStatus, String subject, String productQuarterSearch){
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
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select mcc.ATTENDANCE_PIC_NAME as attendacePicName, ");
		sql.append(" campus.name as 'campusName', mc.name as 'className',d.name as 'grade',mcc.grade as 'gradeValue',mcc.mini_class_course_id as 'courseId',u_teacher.name as 'teacher', u_study.name as 'studyManeger',dd_subject.name as 'subjectName', ");
		sql.append("  mcc.course_date as 'courseDate',mcc.course_time as 'startTime',mcc.course_end_time as 'endTime',mcc.course_hours as 'courseHours',mcc.COURSE_MINUTES as 'courseTimeLong', mcc.AUDIT_STATUS as 'auditStatus',");
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ),0) 'conplete',");  //上课人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'NEW' then 1 else 0 end ),0) 'news',");  //未上课人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ),0) 'late', ");   //迟到人数
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'ABSENT' then 1 else 0 end ),0) 'absent', ");   //缺勤人数
		sql.append(" IFNULL(sum(case when mcsa.CHARGE_STATUS = 'CHARGED' then 1 else 0 end ),0) 'charged', ");   //扣费人数
		sql.append(" IFNULL(sum(case when mcsa.CHARGE_STATUS = 'UNCHARGE' then 1 else 0 end ),0) 'uncharge', ");     //未扣费人数
		sql.append(" IFNULL(sum(case when (mcsa.ATTENDENT_STATUS in ( 'LEAVE','ABSENT' ) and mcsa.SUPPLEMENT_DATE is not null) then 1 else 0 end ),0) as 'notleave', "); //补课人数
		sql.append(" IFNULL(case when mcc.AUDIT_STATUS='VALIDATE' then mcc.course_hours else 0 end ,0)  as 'auditHours', ");   //有效课时
		sql.append(" IFNULL(case when mcc.audit_status = 'UNVALIDATE' then mcc.course_hours else 0 end ,0) as 'cancelHours',  ");  //无效课时
		sql.append(" IFNULL(case when (mcc.audit_status is null or mcc.audit_status = 'UNAUDIT') then mcc.course_hours else 0 end ,0) as 'unauditHours'  ");  //未审批
		sql.append(" from mini_class_course mcc  ");
		sql.append(" LEFT JOIN mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append(" LEFT JOIN mini_class_student_attendent mcsa on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID ");
		sql.append(" LEFT JOIN user u on u.USER_ID=mcc.teacher_id");
		sql.append(" LEFT JOIN ( select id,name from data_dict where CATEGORY = 'STUDENT_GRADE' ) d on mcc.grade=d.id ");
		sql.append(" LEFT JOIN organization campus on campus.id=mc.BL_CAMPUS_ID");
		sql.append(" INNER JOIN organization brench on brench.id=campus.parentID");
		sql.append(" INNER JOIN organization grounp on grounp.id=brench.parentID");
		sql.append(" INNER JOIN user u_teacher on mcc.teacher_id=u_teacher.USER_ID ");
		sql.append(" INNER JOIN user u_study on mcc.study_maneger_id = u_study.USER_ID ");
		sql.append(" INNER JOIN data_dict dd_subject on mcc.subject=dd_subject.ID ");
		sql.append(" where 1=1 ");
		sql.append(" and mcc.course_status = 'CHARGED' ");
		if(startDate!=null && StringUtil.isNotBlank(startDate)){
			sql.append(" and mcc.course_date >= :startDate ");
			params.put("startDate",startDate);
		}
		if(endDate!=null && StringUtil.isNotBlank(endDate)){
			sql.append(" and mcc.course_date <= :endDate ");
			params.put("endDate",endDate);
		}
		if(orgLevel!=null && !"".equals(orgLevel)){
			sql.append(" and campus.orgLevel like :orgLevel ");
			params.put("orgLevel", orgLevel+"%");
		}
		if(auditStatus!=null && !"".equals(auditStatus)){
			if(auditStatus.equals("UNAUDIT")){
				//未审批
				sql.append(" and (mcc.audit_status = 'UNAUDIT' or mcc.audit_status is null )");
			}else{
				sql.append(" and mcc.audit_status= :auditStatus ");
				params.put("auditStatus",auditStatus);
			}
					
		}		
		if(productQuarterSearch!=null && StringUtils.isNotBlank(productQuarterSearch)){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id= :productQuarterSearch ) ");
			params.put("productQuarterSearch",productQuarterSearch);
		}

		//科目筛选
		if (StringUtil.isNotBlank(subject)){
			String[] subjects=StringUtil.replaceSpace(subject).split(",");
			if(subjects.length>0){
//				sql.append(" and ( 1=0 ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or mcc.SUBJECT ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");

				sql.append(" and mcc.SUBJECT in (:subjects)");
				params.put("subjects",subjects);
			}
		}

		if(teacherId!=null && !"".equals(teacherId)){
			sql.append(" and mcc.teacher_id = '"+teacherId+"' ");
		}		
		sql.append(" group by mcc.mini_class_course_id,mc.BL_CAMPUS_ID ");
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
	}

	/**
	 * 小班课程审批汇总(工资)
	 *                		
	 */
	
	
	@Override
	public DataPackage miniClaCourseAuditAnalyzeSalary(DataPackage dataPackage,
			BasicOperationQueryVo vo, String AuditStatus, String anshazhesuan,String productQuarterSearch) {
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append(" select ");
		sql.append(" org.*,ah.teacher,ah.employeeNo,ah.workType, ah.teacherBlCampus, ah.teacherId,ah.miniClassName, ah.validateHours, gh.*,ah.teacherLevel,ah.teacherType,ah.monthLastStudents  ");
		sql.append("  from ");
		sql.append("   ( select ");
		sql.append(" u.name as 'teacher',u.employee_No as 'employeeNo',u.WORK_TYPE as 'workType', org.name as 'teacherBlCampus', mc.name as 'miniClassName', mc.BL_CAMPUS_ID, mc.MINI_CLASS_ID,");
		sql.append("  mcc.grade as 'grade', mcc.TEACHER_ID as 'teacherId',mcc.MINI_CLASS_COURSE_ID as miniCourseId, ");
		
		sql.append(" (select count(1) from mini_class_student_attendent where mini_class_course_id =( select mini_class_course_id from mini_class_course where COURSE_DATE=( ");
		sql.append(" select max(course_date) from mini_class_course where course_date like concat(substr( :endDate ,1,7),'%') and mini_class_id =mc.MINI_CLASS_ID and COURSE_STATUS<>'CANCEL') and COURSE_STATUS<>'CANCEL' and mini_class_id =mc.MINI_CLASS_ID limit 1)) as monthLastStudents, ");

		params.put("endDate",vo.getEndDate());
		
		if(anshazhesuan!=null && anshazhesuan.equals("hour")){
//			课程时长按照小时查询
			sql.append(" IFNULL(sum(ROUND((mcc.COURSE_HOURS * IFNULL(mcc.COURSE_MINUTES,0) / 60),2)),0)  as 'validateHours' ");
		}else{
			sql.append(" IFNULL(sum(mcc.course_hours),0) as 'validateHours' ");
		}
		
		sql.append("  ,(select TEACHER_LEVEL from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherLevel,");
    	sql.append("  (select TEACHER_TYPE from teacher_version where version_date=(select max(version_date) ");
    	sql.append("  from teacher_version where version_date<= :endDate and teacher_id =mcc.teacher_id) and TEACHER_ID=mcc.teacher_id) teacherType");
    	
		sql.append(" from  mini_class_course mcc   ");
		sql.append(" LEFT JOIN mini_class mc  on mcc.mini_class_id=mc.MINI_CLASS_ID   ");
		sql.append(" LEFT JOIN product p on p.id=mc.PRODUCE_ID ");
		sql.append(" LEFT JOIN user u  on mcc.TEACHER_ID=u.USER_ID  ");
		sql.append(" LEFT JOIN user_dept_job udj on udj.user_id=u.user_id ");
		sql.append(" LEFT JOIN organization org1 on udj.DEPT_ID=org1.id ");
		sql.append(" LEFT JOIN organization org on org1.belong=org.id ");
		sql.append("  where  mcc.course_status = 'CHARGED'  ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId",vo.getBlCampusId());
		}
		sql.append(" and mcc.course_date >= :startDate ");
		sql.append(" and mcc.course_date <= :endDate ");
		params.put("startDate",vo.getStartDate());
		sql.append(" and udj.isMajorRole = 0");
		if(productQuarterSearch!=null && StringUtils.isNotBlank(productQuarterSearch)){
			//产品季度
			sql.append(" and mc.MINI_CLASS_ID in (select MINI_CLASS_ID from mini_class mc INNER JOIN product p on mc.produce_id=p.id where p.product_quarter_id= :productQuarterSearch) ");
			params.put("productQuarterSearch",productQuarterSearch);
		}
		if(StringUtils.isNotBlank(vo.getMiniClassTypeId())){			
			String[] miniClassTypes=StringUtil.replaceSpace(vo.getMiniClassTypeId()).split(",");
			if(miniClassTypes.length>0){
//				sql.append(" AND (p.CLASS_TYPE_ID ='"+miniClassTypes[0]+"' ");
//				for (int i = 1; i < miniClassTypes.length; i++) {
//					sql.append(" or p.CLASS_TYPE_ID ='"+miniClassTypes[i]+"' ");
//				}
//				sql.append(" )");

				sql.append(" AND p.CLASS_TYPE_ID in (:miniClassTypes)");
				params.put("miniClassTypes", miniClassTypes);
			}
		}

		//科目筛选
		if (StringUtil.isNotBlank(vo.getSubject())){
			String[] subjects=StringUtil.replaceSpace(vo.getSubject()).split(",");
			if(subjects.length>0){
//				sql.append(" and ( 1=0 ");
//				for (int i = 0; i < subjects.length; i++) {
//					if (StringUtil.isNotBlank(subjects[i])){
//						sql.append(" or mcc.SUBJECT ='"+subjects[i]+"' ");
//					}
//				}
//				sql.append(" )");

				sql.append(" and mcc.SUBJECT in (:subjects) ");
				params.put("subjects",subjects);
			}
		}

		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append(" group by mc.BL_CAMPUS_ID , mcc.MINI_CLASS_ID , mcc.TEACHER_ID ) ah  ");
		
		sql.append("  LEFT JOIN ");
		sql.append("  (  select bl_campus_id,teacher_id,GRADE,mini_class_id, ");
		if(anshazhesuan!=null && anshazhesuan.equals("hour")){
//			课程时长按照小时查询
			sql.append(" IFNULL(max(IF(gradeName = '一年级',hourAmount, 0)),0) as 'gradeOneHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '二年级',hourAmount, 0)),0) as 'gradeTwoHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '三年级',hourAmount, 0)),0) as 'gradeThreeHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '四年级',hourAmount, 0)),0) as 'gradeFourHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '五年级',hourAmount, 0)),0) as 'gradeFiveHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '六年级',hourAmount, 0)),0) as 'gradeSixHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初一',hourAmount, 0)),0) as 'gradeSevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初二',hourAmount, 0)),0) as 'gradeEightHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '初三',hourAmount, 0)),0) as 'gradeNineHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高一',hourAmount, 0)),0) as 'gradeTenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高二',hourAmount, 0)),0) as 'gradeElevenHours', ");
			sql.append(" IFNULL(max(IF(gradeName = '高三',hourAmount, 0)),0) as 'gradeTwelveHours', ");
			sql.append(" IFNULL(max(IF(gradeName is null,hourAmount, 0)),0) as 'otherHours', ");
			
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
		sql.append("  ( select sum(d.salary) as salarys,sum(d.salaryHour) as salaryHours,mc.BL_CAMPUS_ID,sum(d.course_hours) as amount,");
		sql.append(" SUM(ROUND((d.COURSE_HOURS * IFNULL(d.COURSE_MINUTES,0) / 60),2)) as hourAmount, d.*  ");
		sql.append(" from ");				
		sql.append(" (select mcc.MINI_CLASS_ID,g.NAME as gradeName,g.ID as gradeId,mcc.MINI_CLASS_COURSE_ID, ");
		sql.append(" mcc.AUDIT_STATUS,mcc.TEACHER_ID, mcc.GRADE,mcc.course_hours,mcc.COURSE_MINUTES, ");
		
		sql.append(" ifnull(mcc.course_hours *  (CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = mcc.TEACHER_ID  AND tv.VERSION_DATE <= mcc.COURSE_DATE) AND TEACHER_ID = mcc.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS salary, ");
        
        sql.append(" ifnull(ROUND((mcc.course_hours * IFNULL(mcc.COURSE_MINUTES, 0) / 60),2) *  (CASE WHEN "
                + " (SELECT TEACHER_TYPE FROM teacher_version where version_date = (SELECT max(version_date) FROM teacher_version tv WHERE tv.TEACHER_ID = mcc.TEACHER_ID  AND tv.VERSION_DATE <= mcc.COURSE_DATE) AND TEACHER_ID = mcc.TEACHER_ID) = 'TEN_CLASS_TEACHER' "
                + " THEN a.charge ELSE IF(a.charge -1 >0, a.charge -1, 0) END), 0) AS salaryHour ");
		
		sql.append("  from  mini_class_course mcc   ");
		sql.append("  INNER JOIN ");		
		sql.append("  (  select mcc.MINI_CLASS_COURSE_ID, ");		
		sql.append(" IFNULL(sum(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS charge ");
		sql.append("   from  mini_class_student_attendent mcsa    ");
		sql.append("   LEFT JOIN  mini_class_course mcc  on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID   ");
		sql.append("  where  mcc.course_status = 'CHARGED'    ");
		sql.append(" and mcc.COURSE_DATE >= :startDate  ");
		sql.append(" and mcc.COURSE_DATE <= :endDate  ");
		sql.append("  group by mcc.mini_class_course_id ) a ");
		sql.append(" on mcc.MINI_CLASS_COURSE_ID=a.MINI_CLASS_COURSE_ID   ");
		sql.append("  left join mini_class mc   on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append("  LEFT JOIN (  select ID, NAME   from  data_dict  where  CATEGORY = 'STUDENT_GRADE' ) g  on mcc.GRADE = g.ID  ");
		sql.append("  where  mcc.COURSE_STATUS = 'CHARGED' ");
		sql.append(" and mcc.COURSE_DATE >= :startDate  ");
		sql.append(" and mcc.COURSE_DATE <= :endDate  ");
		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId",vo.getTeacherId());
		}		
		sql.append("  ) d  ");
		
		sql.append(" LEFT JOIN   ");
		sql.append(" mini_class mc on mc.MINI_CLASS_ID=d.mini_class_id ");
		sql.append(" where 1=1 ");
		if (StringUtil.isNotBlank(vo.getBlCampusId())) {
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId2 ");
			params.put("blCampusId2",vo.getBlCampusId());
		}
		sql.append("  group by d.mini_class_id,mc.BL_CAMPUS_ID,d.TEACHER_ID ");
		sql.append("  ) bh  ");
		sql.append(" group by bh.mini_class_id,bh.teacher_id ) gh ");
		
		sql.append(" on ah.MINI_CLASS_ID = gh.MINI_CLASS_ID    ");		
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
			params.put("blCampusId3",vo.getBlCampusId());
		}
		if (StringUtil.isNotBlank(vo.getBrenchId())){
			sql.append(" and  org_brench.id =:branchId  ");
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
			sql.append(" and ah.teacherType='"+vo.getTeacherType()+"'");
		}
		
		List<Map<Object, Object>> list=super.findMapOfPageBySql(sql.toString(),dataPackage,params);
		dataPackage.setDatas(list);
		dataPackage.setRowCount(list.size());
		return dataPackage;
	}

	/**
	 *  根据学生，产品组（或产品），科目计算一对一已排未上课课程课时数
	 */
	@Override
	public BigDecimal sumCourseHoursByStudentProductSubject(String studentId,
			String productGroupId, String productId, String subjectId, String summaryId, String courseId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT sum(c.PLAN_HOURS) realHours FROM course c "
				+ " left join product p ON c.PRODUCT_ID = p.ID WHERE 1=1 ";
		sql += " AND c.COURSE_STATUS = 'NEW' ";
		if (StringUtils.isNotBlank(studentId)) {
			sql += " AND c.STUDENT_ID = :studentId ";
			params.put("studentId",studentId);
		}
		if (StringUtils.isNotBlank(productGroupId)) {
			sql += " AND p.PRODUCT_GROUP_ID = :productGroupId ";
			params.put("productGroupId",productGroupId);
		} else {
			if (StringUtils.isNotBlank(productId)) {
				sql += " AND p.ID = :productId ";
				params.put("productId",productId);
			}
		}
		if (StringUtils.isNotBlank(subjectId)) {
			sql += " AND c.SUBJECT = :subjectId ";
			params.put("subjectId",subjectId);
		}
		if (StringUtils.isNotBlank(summaryId)) {
			sql += " AND c.COURSE_SUMMARY_ID != :summaryId ";
			params.put("summaryId",summaryId);
		}
		if (StringUtils.isNotBlank(courseId)) {
			sql += " AND c.COURSE_ID != :courseId ";
			params.put("courseId",courseId);
		}
		List<Map<Object, Object>> list = super.findMapBySql(sql,params);
		if (list.size() > 0 && list.get(0).get("realHours") != null) {
			return (BigDecimal) list.get(0).get("realHours");
		}
		return BigDecimal.ZERO;
	}

	@Override
	public List getMiniClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String auditStatus) {
		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer();
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
		sql.append(" ROUND(sum(case when mcc.AUDIT_STATUS = 'VALIDATE' then mcc.course_hours*mcc.COURSE_MINUTES/60 else 0 end),2) as amount, ");
		sql.append(" mc.MINI_CLASS_ID,  g.NAME as gradeName,  mc.BL_CAMPUS_ID,mcc.MINI_CLASS_COURSE_ID, mcc.TEACHER_ID, mcc.GRADE, ");
		sql.append(" CASE WHEN tv.TEACHER_TYPE = 'TEN_CLASS_TEACHER' THEN sum(stu.charge) ELSE sum(stu.stuNums) END AS 'stuNums' ");
		sql.append(" from ");
		sql.append("  mini_class_course mcc ");
		sql.append(" LEFT JOIN teacher_version tv ON mcc.TEACHER_ID = tv.TEACHER_ID AND tv.VERSION_DATE = (SELECT max(VERSION_DATE) FROM teacher_version WHERE VERSION_DATE <= mcc.COURSE_DATE AND mcc.TEACHER_ID = TEACHER_ID) ");
		sql.append(" left JOIN( ");
		sql.append(" select a.MINI_CLASS_COURSE_ID mcid,a.conlete,a.late,a.notleave,case when (conlete+notleave+late-1)>0 then (conlete+notleave+late-1) else 0 end as 'stuNums', charge from ( ");
		sql.append(" select  ");
		sql.append(" mcc.MINI_CLASS_COURSE_ID, ");
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'CONPELETE' then 1 else 0 end ),0) as 'conlete', ");
		sql.append(" IFNULL(sum(case when mcsa.ATTENDENT_STATUS = 'LATE' then 1 else 0 end ),0) as 'late', ");
		sql.append(" IFNULL(sum(case when (mcsa.ATTENDENT_STATUS in ( 'LEAVE','ABSENT' ) and mcsa.SUPPLEMENT_DATE is not null) then 1 else 0 end ),0) as 'notleave', ");
		sql.append(" IFNULL(sum(CASE WHEN mcsa.CHARGE_STATUS = 'CHARGED' THEN 1 ELSE 0 END), 0) AS 'charge' ");
		sql.append(" from mini_class_student_attendent mcsa ");
		sql.append(" LEFT JOIN mini_class_course mcc on mcc.mini_class_course_id=mcsa.MINI_CLASS_COURSE_ID where mcc.course_status = 'CHARGED' and mcc.AUDIT_STATUS = 'VALIDATE' ");
		sql.append(" and mcc.COURSE_DATE >= :startDate  ");
		sql.append(" and mcc.COURSE_DATE <= :endDate ");
		sql.append(" group by mcc.mini_class_course_id ) a )stu on mcc.MINI_CLASS_COURSE_ID=stu.mcid ");
		sql.append(" left join mini_class mc on mcc.mini_class_id=mc.MINI_CLASS_ID ");
		sql.append("  inner join ");
		sql.append(" (select ID,NAME from data_dict where CATEGORY = 'STUDENT_GRADE') g on mcc.GRADE = g.ID ");
		sql.append("  where 1=1 ");
//		sql.append(" and mc.BL_CAMPUS_ID = '"+vo.getBlCampusId()+"'");
		sql.append(" and mcc.COURSE_DATE >= :startDate ");
		sql.append(" and mcc.COURSE_DATE <= :endDate ");
		sql.append(" and mcc.COURSE_STATUS = 'CHARGED'  ");
		if(StringUtil.isNotBlank(vo.getTeacherId())){
			sql.append(" and mcc.TEACHER_ID = :teacherId ");
			params.put("teacherId", vo.getTeacherId());
		}
		sql.append("  group by ");
		sql.append("  mc.BL_CAMPUS_ID,mc.MINI_CLASS_ID, mcc.TEACHER_ID, mcc.MINI_CLASS_COURSE_ID ) d ");
		sql.append("  group by ");
		sql.append("  d.BL_CAMPUS_ID,d.TEACHER_ID   ");
		List mapBySql = super.findMapBySql(sql.toString(),params);
		return mapBySql;
	}

	/**
	 * 根据学生，产品组（或产品），科目计算一对一学管已确认课程课时数
	 */
	@Override
	public BigDecimal sumConfrimCourseHoursByStudentProductSubject(
			String studentId, String productGroupId, String productId,
			String subjectId) {
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT sum(c.REAL_HOURS) realHours FROM course c "
				+ " left join product p ON c.PRODUCT_ID = p.ID WHERE 1=1 ";
		sql += " AND c.COURSE_STATUS = 'STUDY_MANAGER_AUDITED' ";
		if (StringUtils.isNotBlank(studentId)) {
			sql += " AND c.STUDENT_ID = :studentId ";
			params.put("studentId",studentId);
		}
		if (StringUtils.isNotBlank(productGroupId)) {
			sql += " AND p.PRODUCT_GROUP_ID = :productGroupId ";
			params.put("productGroupId", productGroupId);
		} else {
			if (StringUtils.isNotBlank(productId)) {
				sql += " AND p.ID = :productId ";
				params.put("productId",productId);
			}
		}
		if (StringUtils.isNotBlank(subjectId)) {
			sql += " AND c.SUBJECT = :subjectId ";
			params.put("subjectId",subjectId);
		}
		List<Map<Object, Object>> list = super.findMapBySql(sql,params);
		if (list.size() > 0 && list.get(0).get("realHours") != null) {
			return (BigDecimal) list.get(0).get("realHours");
		}
		return BigDecimal.ZERO;
	};


	
	
}
