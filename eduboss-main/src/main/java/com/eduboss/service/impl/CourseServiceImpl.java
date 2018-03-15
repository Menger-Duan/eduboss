package com.eduboss.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.eduboss.common.*;
import com.eduboss.dao.*;
import com.eduboss.domain.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.StaleObjectStateException;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.eduboss.domainJdbc.MiniClassCourseJdbc;
import com.eduboss.domainVo.AuditAutoRecogVo;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.ClassStudentInfoVo;
import com.eduboss.domainVo.CommonClassCourseVo;
import com.eduboss.domainVo.CourseAttendaceStatusCountVo;
import com.eduboss.domainVo.CourseAttendanceRecordVo;
import com.eduboss.domainVo.CourseChangesSearchResultVo;
import com.eduboss.domainVo.CourseInfoVo;
import com.eduboss.domainVo.CourseRequirementSearchResultVo;
import com.eduboss.domainVo.CourseSummarySearchResultVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.CourseWeekVo;
import com.eduboss.domainVo.HasOneOnOneCourseVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.domainVo.MultiStudentProductSubjectVo;
import com.eduboss.domainVo.OneOnOneBatchAttendanceEditVo;
import com.eduboss.domainVo.OneOnOneCourseInfoVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.ProductSubjectCourseHoursVo;
import com.eduboss.domainVo.ProductVo;
import com.eduboss.domainVo.SmallClassExcelVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.domainVo.StudnetAccMvVo;
import com.eduboss.domainVo.SystemConfigVo;
import com.eduboss.domainVo.TextBookBossVo;
import com.eduboss.domainVo.TodayCourseBillVo;
import com.eduboss.domainVo.TwoTeacherClassCourseVo;
import com.eduboss.domainVo.EduPlatform.ClassCoursesInfoVo;
import com.eduboss.domainVo.wechatVo.CourseForWechatVo;
import com.eduboss.dto.BenchmarkSubjectSearchVo;
import com.eduboss.dto.CourseEditVo;
import com.eduboss.dto.CourseRequirementEditVo;
import com.eduboss.dto.CourseRequirementSearchInputVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.CourseSummarySearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MutilCourseVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse;
import com.eduboss.dto.SelectOptionResponse.NameValue;
import com.eduboss.exception.ApplicationException;
import com.eduboss.exception.ErrorCode;
import com.eduboss.service.ChargeService;
import com.eduboss.service.ContractProductSubjectService;
import com.eduboss.service.ContractService;
import com.eduboss.service.CourseService;
import com.eduboss.service.MoneyWashRecordsService;
import com.eduboss.service.OdsMonthIncomeCampusService;
import com.eduboss.service.OtmClassService;
import com.eduboss.service.ProductService;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.service.SmallClassService;
import com.eduboss.service.StudentDynamicStatusService;
import com.eduboss.service.StudentService;
import com.eduboss.service.SystemConfigService;
import com.eduboss.service.UserService;
import com.eduboss.sms.AliyunSmsUtil;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.CalculateUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.HttpClientUtil;
import com.eduboss.utils.ImageSizer;
import com.eduboss.utils.PropertiesUtils;
import com.eduboss.utils.PushRedisMessageUtil;
import com.eduboss.utils.StringUtil;
import com.eduboss.utils.ValidationUtil;
import com.google.common.collect.Maps;


@Service("com.eduboss.service.CourseService")
public class CourseServiceImpl implements CourseService {

	/**
	 * 日志
	 */
	private final static Logger log = Logger.getLogger(CourseServiceImpl.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ChargeService chargeService;

	@Autowired
	private StudentDao studentDao;

	@Autowired
	private CourseAttendanceRecordDao courseAttendanceRecordDao;

	@Autowired
	private CourseDao courseDao;

	@Autowired
	private CourseSummaryDao courseSummaryDao;

	@Autowired
	private CourseRequirementDao courseRequirementDao;

	@Autowired
	private DataDictDao dataDictDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private StudnetAccMvDao studnetAccMvDao;

	@Autowired
	private CourseConflictDao courseConflictDao;

	@Autowired
	private MiniClassCourseDao miniClassCourseDao;

	@Autowired
	private StudentAttendanceRecordDao studentAttendanceRecordDao;

	@Autowired
	private StudentOrganizationDao studentOrganizationDao;

	@Autowired
	private ProductDao productDao;

	@Autowired
	private SmallClassService smallClassService;

	@Autowired
	private MiniClassStudentAttendentDao miniClassStudentAttendentDao;

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private RoleQLConfigService roleQLConfigService;

	@Autowired
	private ContractProductDao contractProductDao;

	@Autowired
	private ContractDao contractDao;

	@Autowired
	private ContractService contractService;

	@Autowired
	private StudentDynamicStatusService studentDynamicStatusService;

	@Autowired
	private OtmClassService otmClassService;

	@Autowired
	private OtmClassStudentAttendentDao otmClassStudentAttendentDao;

	@Autowired
	private TransactionRecordDao transactionRecordDao;

	@Autowired
	private MiniClassDao miniClassDao;

	@Autowired
	private OtmClassCourseDao otmClassCourseDao;

	@Autowired
	private OtmClassDao otmClassDao;

	@Autowired
	private AccountChargeRecordsDao accountChargeRecordsDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private StudentService studentService;

	@Autowired
	private MoneyWashRecordsService moneyWashRecordsService;

	@Autowired
	private ContractProductSubjectService contractProductSubjectService;

	@Autowired
	private ProductService productService;

	@Autowired
	private OdsMonthIncomeCampusService odsMonthIncomeCampusService;

	@Autowired
	private TwoTeacherClassStudentAttendentDao twoTeacherAttendentDao;

	@Autowired
	private TwoTeacherClassTwoDao twoTeacherClassTwoDao;

	@Autowired
	private TwoTeacherClassCourseDao twoTeacherClassCourseDao;

    @Autowired
    private OtmClassStudentDao otmClassStudentDao;

    @Autowired
	private TwoTeacherClassStudentDao twoTeacherClassStudentDao;

    @Autowired
    private UserOrganizationDao userOrganizationDao;

	/**
	 * 获取课程列表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getCourseList(CourseSearchInputVo courseSearchInputVo,
			DataPackage dp, String voMapperId) {
		dp = courseDao.getCourseList(courseSearchInputVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class, voMapperId);
		dp.setDatas(searchResultVoList);
		return dp;
	}

	@Override
	public DataPackage getCourseList(CourseSearchInputVo courseSearchInputVo,
			DataPackage dp) {
		return getCourseList(courseSearchInputVo, dp, "courseFullMapping");// 默认加载全部
	}

	/**
	 * 获取学生课程表
	 */
	@Override
	public DataPackage getStudentCourseScheduleList(String studentId,
			Date start, Date end) throws Exception {

		CourseSearchInputVo courseSearchVo = new CourseSearchInputVo();
		if (start != null) {
			String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
			courseSearchVo.setStartDate(startDate);
		}
		if (end != null) {
			String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");
			courseSearchVo.setEndDate(endDate);
		}
		courseSearchVo.setStudentId(studentId);

		DataPackage dp = new DataPackage(0, 999);
		dp = courseDao.getStudentCourseScheduleList(courseSearchVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class,
				"courseFullMapping");
		int crashInd = 0;
		for (CourseVo courseVo : searchResultVoList) {
			// 检测是否有冲突
			/*crashInd = courseConflictDao.countDistinctConflicts(
					courseVo.getCourseId(), courseVo.getStudentId(),
					courseVo.getTeacherId(), courseVo.getCourseDate(),
					courseVo.getCourseTime());*/
			crashInd = courseConflictDao.findCourseConflictCount(courseVo.getCourseId(), courseVo.getStudentId(),
					courseVo.getTeacherId());
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(courseVo.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
			if (count > 0) {
				courseVo.setIsWashed("TRUE");
			} else {
				courseVo.setIsWashed("FALSE");
			}
			courseVo.setCrashInd(Integer.toString(crashInd));
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}

	@Override
	public DataPackage getStudentCourseScheduleListByTeacher(String teacherId,
			String courseDate) throws Exception {
		CourseSearchInputVo courseSearchVo = new CourseSearchInputVo();
		courseSearchVo.setTeacherId(teacherId);
		courseSearchVo.setStartDate(courseDate);
		courseSearchVo.setEndDate(courseDate);
		DataPackage dp = new DataPackage(0, 999);
		dp = courseDao.getStudentCourseScheduleList(courseSearchVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class,
				"courseFullMapping");
		dp.setDatas(searchResultVoList);
		return dp;
	}

	/**
	 * 获取学生小班课程表
	 */
	@Override
	public DataPackage getStudentMiniClassCourseScheduleList(String studentId,
			Date start, Date end) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		MiniClassCourseVo vo = new MiniClassCourseVo();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		vo.setStudentId(studentId);
		vo.setCurrentRoleId("courseSchedule");

		DataPackage dp = new DataPackage(0, 999);
		dp = miniClassCourseDao.getMiniClassCourseList(vo, dp);
		@SuppressWarnings("unchecked")
		List<MiniClassCourseVo> searchResultVoList = HibernateUtils
				.voListMapping((List<MiniClassCourse>) dp.getDatas(),
						MiniClassCourseVo.class);
		int crashInd = 0;
		for (MiniClassCourseVo miniClassCourseVo : searchResultVoList) {
			if (StringUtil.isBlank(miniClassCourseVo.getGrade()) && StringUtil.isNotBlank(miniClassCourseVo.getGradeId())) {
				DataDict gradeDict = dataDictDao.findById(miniClassCourseVo.getGradeId());
				miniClassCourseVo.setGrade(gradeDict.getName());
				miniClassCourseVo.setGradeId(gradeDict.getValue());
			}
			if (StringUtil.isBlank(miniClassCourseVo.getSubject()) && StringUtil.isNotBlank(miniClassCourseVo.getSubjectId())) {
				DataDict subjectDict = dataDictDao.findById(miniClassCourseVo.getSubjectId());
				miniClassCourseVo.setSubject(subjectDict.getName());
				miniClassCourseVo.setGradeId(subjectDict.getValue());
			}

			if ((StringUtil.isBlank(miniClassCourseVo.getTeacherName()) || StringUtil.isBlank(miniClassCourseVo.getTeacherMobile())) && StringUtil.isNotBlank(miniClassCourseVo.getTeacherId())) {
				User teacher = userDao.findById(miniClassCourseVo.getTeacherId());
				miniClassCourseVo.setTeacherName(teacher.getName());
				miniClassCourseVo.setTeacherMobile(teacher.getContact());
			}else if (StringUtil.isBlank(miniClassCourseVo.getTeacherName()) ) {
				miniClassCourseVo.setTeacherName("无");
			}else if (StringUtil.isBlank(miniClassCourseVo.getTeacherMobile())){
				miniClassCourseVo.setTeacherMobile("无");
			}

			if ((StringUtil.isBlank(miniClassCourseVo.getStudyManegerName()) ||  StringUtil.isBlank(miniClassCourseVo.getStudyManegerMobile())) && StringUtil.isNotBlank(miniClassCourseVo.getStudyManegerId())) {
				User studyManager = userDao.findById(miniClassCourseVo.getStudyManegerId());
				miniClassCourseVo.setStudyManegerName(studyManager.getName());
				miniClassCourseVo.setStudyManegerMobile(studyManager.getContact());
			}

			// 检测是否有冲突
			/*crashInd = courseConflictDao.countMiniClassDistinctConflicts(
					miniClassCourseVo.getMiniClassCourseId(), miniClassCourseVo.getStudentId(),
					miniClassCourseVo.getTeacherId(), miniClassCourseVo.getCourseDate(),
					miniClassCourseVo.getCourseTime().toString() + " - " + miniClassCourseVo.getCourseEndTime().toString());*/
			crashInd = courseConflictDao.findCourseConflictCount(miniClassCourseVo.getMiniClassCourseId(), miniClassCourseVo.getStudentId(),
					miniClassCourseVo.getTeacherId());
			miniClassCourseVo.setCrashInd(Integer.toString(crashInd));
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(miniClassCourseVo.getMiniClassId(), ProductType.SMALL_CLASS);
			if (count > 0) {
				miniClassCourseVo.setIsWashed("TRUE");
			} else {
				miniClassCourseVo.setIsWashed("FALSE");
			}
		}
		dp.setDatas(searchResultVoList);
		return dp;
	}

	/**
	 * 获取学生一对多课程表
	 */
	@Override
	public DataPackage getStudentOtmClassCourseScheduleList(String studentId, Date start, Date end) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		OtmClassCourseVo vo = new OtmClassCourseVo();
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		vo.setStudentId(studentId);
		vo.setCurrentRoleId("courseSchedule");

		DataPackage dp = new DataPackage(0, 999);
		return otmClassService.getOtmClassCourseList(vo, dp);
	}

	/**
	 * 获取老师课程表
	 *//*
	@Override
	public DataPackage getTeacherCourseScheduleList(String teacherId,
			Date start, Date end, Boolean isAllCourseStatus) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		boolean searchTeacher = false;

		if (StringUtils.isBlank(teacherId)) {// 如果是没有搜索指定老师
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			if (user != null) {
				// 如果没有指定搜索老师，并且自己的角色是老师，才允许搜索，不然会返回所有老师的记录，老师课程表只返回一个老师的记录，不会有多个老师的情况
				if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
					teacherId = user.getUserId();
					searchTeacher = true;
				}
			} else {
				throw new ApplicationException("当前登录用户为空！");
			}
		} else {// 如果是搜索指定老师
			searchTeacher = true;
		}

		if (searchTeacher) {// 允许搜索
			CourseSearchInputVo courseSearchVo = new CourseSearchInputVo();
			courseSearchVo.setStartDate(startDate);
			courseSearchVo.setEndDate(endDate);
			courseSearchVo.setTeacherId(teacherId);

			DataPackage dp = new DataPackage(0, 999);
			if(isAllCourseStatus == null || isAllCourseStatus == false){
				dp = courseDao.getTeacherCourseScheduleList(courseSearchVo, dp);
			}else{
				dp = courseDao.getTeacherCourseScheduleListAllCourseStatus(courseSearchVo, dp);
			}
			List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
					(List<Course>) dp.getDatas(), CourseVo.class,
					"courseFullMapping");

			// 检测冲突

			int crashInd = 0;
			for (CourseVo courseVo : searchResultVoList) {
				// 检测是否有冲突
				crashInd = courseConflictDao.countDistinctConflicts(
						courseVo.getCourseId(), courseVo.getStudentId(),
						courseVo.getTeacherId(), courseVo.getCourseDate(),
						courseVo.getCourseTime());
				courseVo.setCrashInd(Integer.toString(crashInd));
			}
			dp.setDatas(searchResultVoList);
			return dp;
		} else {// 不符合搜索条件，返回空的结果集
			return new DataPackage(0, 1);
		}

	}*/

	/**
	 * 获取老师课程表
	 */
	@Override
	public DataPackage getTeacherCourseScheduleList(String teacherId,
			Date start, Date end, Boolean isAllCourseStatus) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		boolean searchTeacher = false;

		if (StringUtils.isBlank(teacherId)) {// 如果是没有搜索指定老师
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			if (user != null) {
				// 如果没有指定搜索老师，并且自己的角色是老师，才允许搜索，不然会返回所有老师的记录，老师课程表只返回一个老师的记录，不会有多个老师的情况
				if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
					teacherId = user.getUserId();
					searchTeacher = true;
				}
			} else {
				throw new ApplicationException("当前登录用户为空！");
			}
		} else {// 如果是搜索指定老师
			searchTeacher = true;
		}

		if (searchTeacher) {// 允许搜索
			CourseSearchInputVo courseSearchVo = new CourseSearchInputVo();
			courseSearchVo.setStartDate(startDate);
			courseSearchVo.setEndDate(endDate);
			courseSearchVo.setTeacherId(teacherId);

			DataPackage dp = new DataPackage(0, 999);
			if(isAllCourseStatus == null || isAllCourseStatus == false){
				dp = courseDao.getTeacherCourseScheduleList(courseSearchVo, dp);
			}else{
				dp = courseDao.getTeacherCourseScheduleListAllCourseStatus(courseSearchVo, dp);
			}
			List<CourseVo> searchResultVoList = HibernateUtils.voListMapping((List<Course>) dp.getDatas(), CourseVo.class);

			// 检测冲突

			int crashInd = 0;
			for (CourseVo courseVo : searchResultVoList) {
				// 检测是否有冲突
				/*crashInd = courseConflictDao.countDistinctConflicts(
						courseVo.getCourseId(), courseVo.getStudentId(),
						courseVo.getTeacherId(), courseVo.getCourseDate(),
						courseVo.getCourseTime());*/
				crashInd = courseConflictDao.findCourseConflictCount(courseVo.getCourseId(), courseVo.getStudentId(),
						courseVo.getTeacherId());
				courseVo.setCrashInd(Integer.toString(crashInd));
				int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(courseVo.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
				if (count > 0) {
					courseVo.setIsWashed("TRUE");
				} else {
					courseVo.setIsWashed("FALSE");
				}
			}
			dp.setDatas(searchResultVoList);
			return dp;
		} else {// 不符合搜索条件，返回空的结果集
			return new DataPackage(0, 1);
		}

	}

	/**
	 * 获取某课程时间段的老师课程表
	 *
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	@Override
	public DataPackage getTeacherCourseScheduleListByCourseTime(
			String teacherId, Date start, Date end, String courseStartTime,
			String courseEndTime) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		boolean searchTeacher = false;

		if (StringUtils.isBlank(teacherId)) {// 如果是没有搜索指定老师
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			if (user != null) {
				// 如果没有指定搜索老师，并且自己的角色是老师，才允许搜索，不然会返回所有老师的记录，老师课程表只返回一个老师的记录，不会有多个老师的情况
				if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
					teacherId = user.getUserId();
					searchTeacher = true;
				}
			} else {
				throw new ApplicationException("当前登录用户为空！");
			}
		} else {// 如果是搜索指定老师
			searchTeacher = true;
		}

		if (searchTeacher) {// 允许搜索
			DataPackage dp = new DataPackage(0, 999);
			dp = courseDao.getTeacherCourseScheduleListByCourseTime(teacherId,
					startDate, endDate, courseStartTime, courseEndTime, dp);
			List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
					(List<Course>) dp.getDatas(), CourseVo.class,
					"courseFullMapping");
			dp.setDatas(searchResultVoList);
			return dp;
		} else {// 不符合搜索条件，返回空的结果集
			return new DataPackage(0, 1);
		}
	}

	/**
	 * 获取老师小班课程表
	 */
	@Override
	public DataPackage getTeacherMiniClassCourseScheduleList(String teacherId,
			Date start, Date end) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		boolean searchTeacher = false;

		if (StringUtils.isBlank(teacherId)) {// 如果是没有搜索指定老师
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			if (user != null) {
				// 如果没有指定搜索老师，并且自己的角色是老师，才允许搜索，不然会返回所有老师的记录，老师课程表只返回一个老师的记录，不会有多个老师的情况
				if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
					teacherId = user.getUserId();
					searchTeacher = true;
				}
			} else {
				throw new ApplicationException("当前登录用户为空！");
			}
		} else {// 如果是搜索指定老师
			searchTeacher = true;
		}

		if (searchTeacher) {// 允许搜索
			MiniClassCourseVo vo = new MiniClassCourseVo();
			vo.setStartDate(startDate);
			vo.setEndDate(endDate);
			vo.setTeacherId(teacherId);
			vo.setCurrentRoleId("courseSchedule");

			DataPackage dp = new DataPackage(0, 999);
			dp = miniClassCourseDao.getMiniClassCourseList(vo, dp);
			@SuppressWarnings("unchecked")
			List<MiniClassCourseVo> searchResultVoList = HibernateUtils.voListMapping(
					(List<MiniClassCourseJdbc>) dp.getDatas(), MiniClassCourseVo.class);
			// 检测冲突

			int crashInd = 0;
			for (MiniClassCourseVo miniClassCourseVo : searchResultVoList) {
				// 检测是否有冲突
				String startTime = miniClassCourseVo.getCourseTime();
				String endTime = miniClassCourseVo.getCourseEndTime();
				if (startTime.length() < 5) {
					startTime = "0" + startTime;
				}
				if (endTime.length() < 5) {
					endTime = "0" + startTime;
				}
				if (StringUtil.isBlank(miniClassCourseVo.getGrade()) && StringUtil.isNotBlank(miniClassCourseVo.getGradeId())) {
					DataDict gradeDict = dataDictDao.findById(miniClassCourseVo.getGradeId());
					miniClassCourseVo.setGrade(gradeDict.getName());
					miniClassCourseVo.setGradeId(gradeDict.getValue());
				}
				if (StringUtil.isBlank(miniClassCourseVo.getSubject()) && StringUtil.isNotBlank(miniClassCourseVo.getSubjectId())) {
					DataDict subjectDict = dataDictDao.findById(miniClassCourseVo.getSubjectId());
					miniClassCourseVo.setSubject(subjectDict.getName());
					miniClassCourseVo.setGradeId(subjectDict.getValue());
				}

				if ((StringUtil.isBlank(miniClassCourseVo.getTeacherName()) || StringUtil.isBlank(miniClassCourseVo.getTeacherMobile())) && StringUtil.isNotBlank(miniClassCourseVo.getTeacherId())) {
					User teacher = userDao.findById(miniClassCourseVo.getTeacherId());
					miniClassCourseVo.setTeacherName(teacher.getName());
					miniClassCourseVo.setTeacherMobile(teacher.getContact());
				}

				if ((StringUtil.isBlank(miniClassCourseVo.getStudyManegerName()) ||  StringUtil.isBlank(miniClassCourseVo.getStudyManegerMobile())) && StringUtil.isNotBlank(miniClassCourseVo.getStudyManegerId())) {
					User studyManager = userDao.findById(miniClassCourseVo.getStudyManegerId());
					miniClassCourseVo.setStudyManegerName(studyManager.getName());
					miniClassCourseVo.setStudyManegerMobile(studyManager.getContact());
				}

				/*crashInd = courseConflictDao.countMiniClassDistinctConflicts(
						miniClassCourseVo.getMiniClassCourseId(), miniClassCourseVo.getStudentId(),
						miniClassCourseVo.getTeacherId(), miniClassCourseVo.getCourseDate(),
						startTime + " - " + endTime);*/
				crashInd = courseConflictDao.findCourseConflictCount(miniClassCourseVo.getMiniClassCourseId(), miniClassCourseVo.getStudentId(),
						miniClassCourseVo.getTeacherId());
				miniClassCourseVo.setCrashInd(Integer.toString(crashInd));
				int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(miniClassCourseVo.getMiniClassCourseId(), ProductType.SMALL_CLASS);
				if (count > 0) {
					miniClassCourseVo.setIsWashed("TRUE");
				} else {
					miniClassCourseVo.setIsWashed("FALSE");
				}
			}
			dp.setDatas(searchResultVoList);
			return dp;
		} else {// 不符合搜索条件，返回空的结果集
			return new DataPackage(0, 1);
		}

	}

	/**
	 * 获取某课程时间段的老师小班课程表
	 *
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	@Override
	public DataPackage getTeacherMiniClassCourseScheduleListByCourseTime(
			String teacherId, Date start, Date end, String courseStartTime,
			String courseEndTime) throws Exception {
		String startDate = DateTools.dateConversString(start, "yyyy-MM-dd");
		String endDate = DateTools.dateConversString(end, "yyyy-MM-dd");

		boolean searchTeacher = false;

		if (StringUtils.isBlank(teacherId)) {// 如果是没有搜索指定老师
			User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal()).getUser();
			if (user != null) {
				// 如果没有指定搜索老师，并且自己的角色是老师，才允许搜索，不然会返回所有老师的记录，老师课程表只返回一个老师的记录，不会有多个老师的情况
				if (userService.isCurrentUserRoleCode(RoleCode.TEATCHER)) {
					teacherId = user.getUserId();
					searchTeacher = true;
				}
			} else {
				throw new ApplicationException("当前登录用户为空！");
			}
		} else {// 如果是搜索指定老师
			searchTeacher = true;
		}

		if (searchTeacher) {// 允许搜索
			DataPackage dp = new DataPackage(0, 999);
			dp = miniClassCourseDao
					.getTeacherMiniClassCourseScheduleListByCourseTime(
							teacherId, startDate, endDate, courseStartTime,
							courseEndTime, dp);
			@SuppressWarnings("unchecked")
			List<MiniClassCourseVo> searchResultVoList = HibernateUtils
					.voListMapping((List<MiniClassCourse>) dp.getDatas(),
							MiniClassCourseVo.class);
			dp.setDatas(searchResultVoList);
			return dp;
		} else {// 不符合搜索条件，返回空的结果集
			return new DataPackage(0, 1);
		}
	}

	/**
	 * 获取校区课程表
	 *
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getSchoolZoneCourseList(
			CourseSearchInputVo courseSearchInputVo, DataPackage dp) {
		String voMapperId = "courseFullMapping";
		dp = courseDao.getCourseList(courseSearchInputVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class, voMapperId);

		for (CourseVo courseVo : searchResultVoList) {
			// 学管
			CourseAttendanceRecord staduyRecord = courseAttendanceRecordDao
					.getCourseAttendanceRecordByCourseIdAndRole(
							courseVo.getCourseId(), RoleCode.STUDY_MANAGER);
			if (staduyRecord != null) {
				courseVo.setStaduyManagerAuditHours(staduyRecord
						.getCourseHours());
			}
			// 教务专员
			CourseAttendanceRecord educatRecord = courseAttendanceRecordDao
					.getCourseAttendanceRecordByCourseIdAndRole(
							courseVo.getCourseId(), RoleCode.EDUCAT_SPEC);
			if (educatRecord != null) {
				courseVo.setTeachingManagerAuditHours(educatRecord
						.getCourseHours());
			}
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}

	/**
	 * 获取校区课程表2
	 *
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getSchoolZoneCourseList2(
			CourseSearchInputVo courseSearchInputVo, DataPackage dp) {
		dp = courseDao.getSchoolZoneCourseList(courseSearchInputVo, dp);
		return dp;
	}

	/**
	 * 找到相应的 一对一本周课表
	 *
	 * @param searchInputVo
	 * @return
	 */
	@Override
	public DataPackage findPageCourseForOneWeek(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		// if (StringUtils.isBlank(searchInputVo.getStudyManagerId())) {
		// searchInputVo.setStudyManagerId(userService.getCurrentLoginUser().getUserId());
		// }
		dp = findPageCourse(searchInputVo, dp);
//		dp = findPageCourseJdbc(searchInputVo, dp);
		List<CourseWeekVo> courseWeekVoList = new ArrayList<CourseWeekVo>();

		List<Course> courseList = (List<Course>) dp.getDatas();
		int crashInd = 0;
		for (Course course : courseList) {
			// 检测是否有冲突
			crashInd = courseConflictDao.countDistinctConflicts(course
					.getCourseId(), course.getStudent().getId(), course
					.getTeacher().getUserId(), course.getCourseDate(), course
					.getCourseTime());

			CourseWeekVo vo = HibernateUtils.voObjectMapping(course,
					CourseWeekVo.class);
			vo.setCrashInd(crashInd);
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(vo.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
			if (count > 0) {
				vo.setIsWashed("TRUE");
			} else {
				vo.setIsWashed("FALSE");
			}
			courseWeekVoList.add(vo);
		}

		dp.setDatas(courseWeekVoList);
		return dp;
	}


	/**
	 * 单纯的获取课表所有列信息， 与获取本周课表有区别
	 *
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	private DataPackage findPageCourse(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		StringBuffer hql = new StringBuffer();

		Map<String,Object> params = Maps.newHashMap();

		hql.append("from Course as course ");
		hql.append(" where 1 = 1 ");
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			hql.append(" and course.courseDate >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			hql.append(" and course.courseDate <= :endDate ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			hql.append(" and course.student.id = :studentId ");
			params.put("studentId", searchInputVo.getStudentId());
		}

		if (StringUtils.isNotBlank(searchInputVo.getStudentIds())){
			hql.append(" and course.student.id in( :studentIds ) ");
			params.put("studentIds", searchInputVo.getStudentIds().trim().split(","));
		}

		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			hql.append(" and course.student.name like :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			hql.append(" and course.teacher.userId = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			hql.append(" and course.teacher.name like '%"
					+ searchInputVo.getTeacherName() + "%' ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			hql.append(" and course.grade.value = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			hql.append(" and course.subject.value = :subject ");
			params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			hql.append(" and course.courseStatus = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus());

		}

		if (StringUtils.isNotBlank(searchInputVo.getExceptCharge())&&"exceptCharge".equals(searchInputVo.getExceptCharge())){
			hql.append(" and course.courseStatus in ('NEW','STUDENT_ATTENDANCE','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED') ");
		}

		if (StringUtils.isNotEmpty(searchInputVo.getCourseSummaryId())) {
			hql.append(" and course.courseSummary.courseSummaryId = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerId())) {
			hql.append(" and course.studyManager.userId = :studyManagerId ");
			params.put("studyManagerId", searchInputVo.getStudyManagerId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerName())) {
			hql.append(" and course.studyManager.name LIKE :studyManagerName ");
			params.put("studyManagerName", "%"+searchInputVo.getStudyManagerName()+"%");
		}
		if (searchInputVo.getDayOfWeek() != null) {
			hql.append(" and  DAYOFWEEK(course.courseDate) = :dayOfWeek ");
			params.put("dayOfWeek", searchInputVo.getDayOfWeek());
		}
		if (StringUtils.isNotBlank(searchInputVo.getProductName())) {
			hql.append(" and  course.product.name like :productName ");
			params.put("productName", "%"+searchInputVo.getProductName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getBlCampusId())) {
			hql.append(" and course.blCampusId.id = :blCampusId ");
			params.put("blCampusId", searchInputVo.getBlCampusId());
		}
		if (searchInputVo.getConflict() != null) {
			if (searchInputVo.getConflict() == 1) {
				hql.append("and course.courseStatus <> '"
						+ CourseStatus.STUDENT_LEAVE
						+ "' and course.courseStatus <> '"
						+ CourseStatus.TEACHER_LEAVE + "'");
			}

			hql.append("and( (SELECT  COUNT(DISTINCT con1.courseId) ");
			hql.append("FROM CourseConflict con1 WHERE con1.student.id = course.student.id ");
			hql.append("AND con1.courseId <> course.courseId ");
			hql.append("AND ((CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 1, 2), SUBSTR(course.courseTime, 4, 2)) BETWEEN con1.startTime AND con1.endTime) ");
			hql.append("AND CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 1, 2), SUBSTR(course.courseTime, 4, 2)) <> con1.endTime  ");
			hql.append("OR (CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 9, 2), SUBSTR(course.courseTime, 12, 2)) BETWEEN con1.startTime AND con1.endTime) ");
			hql.append("AND CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 9, 2), SUBSTR(course.courseTime, 12, 2)) <> con1.startTime)) ");
			if (searchInputVo.getConflict() == 1) {
				hql.append("  >0 or");
			} else {
				hql.append("  =0 and");
			}
			hql.append(" (SELECT  COUNT(DISTINCT con1.courseId) ");
			hql.append("FROM CourseConflict con1 WHERE con1.teacher.userId = course.teacher.userId ");
			hql.append("AND con1.courseId <> course.courseId ");
			hql.append("AND ((CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 1, 2), SUBSTR(course.courseTime, 4, 2)) BETWEEN con1.startTime AND con1.endTime) ");
			hql.append("AND CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 1, 2), SUBSTR(course.courseTime, 4, 2)) <> con1.endTime  ");
			hql.append("OR (CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 9, 2), SUBSTR(course.courseTime, 12, 2)) BETWEEN con1.startTime AND con1.endTime) ");
			hql.append("AND CONCAT(REPLACE(course.courseDate, '-', ''), SUBSTR(course.courseTime, 9, 2), SUBSTR(course.courseTime, 12, 2)) <> con1.startTime)) ");

			if (searchInputVo.getConflict() == 1) {
				hql.append("  >0 )");
			} else {
				hql.append("  =0 )");
			}
		}
		// 带上权限查询
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("本周课表","hql","course.blCampusId.id"));

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx() + " " + dp.getSord()+" ,course.courseDate desc ,course.courseTime desc");
			//params.put("orderBy", );
		} else {
			hql.append(" order by course.courseDate desc ,course.courseTime desc");
		}

		dp = courseDao.findPageByHQL(hql.toString(), dp,true,params);
		return dp;
	}

	/**
	 * 单纯的获取课表所有列信息， 与获取本周课表有区别
	 *
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	private DataPackage findPageCourseJdbc(CourseSearchInputVo searchInputVo,
			DataPackage dp) {
		Map<String, Object> params = Maps.newHashMap();

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.* ");
		StringBuffer sqlFrom = new StringBuffer(" FROM course a LEFT JOIN data_dict dd_g ON a.GRADE = dd_g.ID ");
		sqlFrom.append(" LEFT JOIN data_dict dd_s ON a.`SUBJECT` = dd_s.ID, organization org, ");
		sqlFrom.append(" student stu, `user` u_t, user s, product p ");
		StringBuffer sqlJoin = new StringBuffer(" WHERE 1=1 AND a.BL_CAMPUS_ID=org.id AND a.STUDENT_ID = stu.ID AND a.TEACHER_ID = u_t.USER_ID "
				+ "AND a.STUDY_MANAGER_ID = s.USER_ID AND a.PRODUCT_ID = p.ID ");
		StringBuffer sqlWhere = new StringBuffer();



		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			sqlWhere.append(" and a.COURSE_DATE >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			sqlWhere.append(" and a.COURSE_DATE <= :endDate  ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			sqlWhere.append(" and a.STUDENT_ID = :studentId ");
			params.put("studentId", searchInputVo.getStudentId());
		}

		if (StringUtils.isNotBlank(searchInputVo.getStudentIds())){
			sqlWhere.append(" and a.STUDENT_ID in( :studentIds ) ");
			params.put("studentIds", searchInputVo.getStudentIds().trim().split(","));
		}

		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			sqlWhere.append(" and stu.`NAME` like :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			sqlWhere.append(" and a.TEACHER_ID = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			sqlWhere.append(" and u_t.`NAME` like :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			sqlWhere.append(" and dd_g.`VALUE` = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			sqlWhere.append(" and dd_s.`VALUE` = :subject ");
			params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			sqlWhere.append(" and a.COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus().getValue());
		}

		if (StringUtils.isNotBlank(searchInputVo.getExceptCharge())&&"exceptCharge".equals(searchInputVo.getExceptCharge())){
			sqlWhere.append(" and a.COURSE_STATUS in ('NEW','STUDENT_ATTENDANCE','TEACHER_ATTENDANCE','STUDY_MANAGER_AUDITED') ");
		}

		if (StringUtils.isNotEmpty(searchInputVo.getCourseSummaryId())) {
			sqlWhere.append(" and a.COURSE_SUMMARY_ID = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerId())) {
			sqlWhere.append(" and a.STUDY_MANAGER_ID = :studyManagerId ");
			params.put("studyManagerId", searchInputVo.getStudyManagerId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerName())) {
			sqlWhere.append(" and s.`NAME` LIKE :studyManagerName ");
			params.put("studyManagerName", "%"+searchInputVo.getStudyManagerName()+"%");
		}
		if (searchInputVo.getDayOfWeek() != null) {
			sqlWhere.append(" and  DAYOFWEEK(a.COURSE_DATE) = :dayOfWeek ");
			params.put("dayOfWeek", searchInputVo.getDayOfWeek());
		}
		if (StringUtils.isNotBlank(searchInputVo.getProductName())) {
			sqlWhere.append(" and  p.`NAME` like :productName ");
			params.put("productName", "%"+searchInputVo.getProductName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getBlCampusId())) {
			sqlWhere.append(" and org.ID = :blCampusId ");
			params.put("blCampusId", searchInputVo.getBlCampusId());
		}
		if (searchInputVo.getConflict() != null) {
			if (searchInputVo.getConflict() == 1) {
				sqlWhere.append("and a.COURSE_STATUS <> '"
						+ CourseStatus.STUDENT_LEAVE
						+ "' and a.COURSE_STATUS <> '"
						+ CourseStatus.TEACHER_LEAVE + "'");
			}

			sqlWhere.append("and( (SELECT  COUNT(DISTINCT con1.COURSE_ID) ");
			sqlWhere.append("FROM COURSE_CONFLICT con1 WHERE con1.STUDENT_ID = a.STUDENT_ID ");
			sqlWhere.append("AND con1.COURSE_ID <> a.COURSE_ID ");
			sqlWhere.append("AND ((CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 1, 2), SUBSTR(a.COURSE_TIME, 4, 2)) BETWEEN con1.START_TIME AND con1.END_TIME) ");
			sqlWhere.append("AND CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 1, 2), SUBSTR(a.COURSE_TIME, 4, 2)) <> con1.END_TIME  ");
			sqlWhere.append("OR (CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 9, 2), SUBSTR(a.COURSE_TIME, 12, 2)) BETWEEN con1.START_TIME AND con1.END_TIME) ");
			sqlWhere.append("AND CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 9, 2), SUBSTR(a.COURSE_TIME, 12, 2)) <> con1.START_TIME)) ");
			if (searchInputVo.getConflict() == 1) {
				sqlWhere.append("  >0 or");
			} else {
				sqlWhere.append("  =0 and");
			}
			sqlWhere.append(" (SELECT  COUNT(DISTINCT con1.COURSE_ID) ");
			sqlWhere.append("FROM COURSE_CONFLICT con1 WHERE TEACHER_ID = a.TEACHER_ID ");
			sqlWhere.append("AND con1.COURSE_ID <> a.COURSE_ID ");
			sqlWhere.append("AND ((CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 1, 2), SUBSTR(a.COURSE_TIME, 4, 2)) BETWEEN con1.START_TIME AND con1.END_TIME) ");
			sqlWhere.append("AND CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 1, 2), SUBSTR(a.COURSE_TIME, 4, 2)) <> con1.END_TIME  ");
			sqlWhere.append("OR (CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 9, 2), SUBSTR(a.COURSE_TIME, 12, 2)) BETWEEN con1.START_TIME AND con1.END_TIME) ");
			sqlWhere.append("AND CONCAT(REPLACE(a.COURSE_DATE, '-', ''), SUBSTR(a.COURSE_TIME, 9, 2), SUBSTR(a.COURSE_TIME, 12, 2)) <> con1.START_TIME)) ");

			if (searchInputVo.getConflict() == 1) {
				sqlWhere.append("  >0 )");
			} else {
				sqlWhere.append("  =0 )");
			}
		}
		// 带上权限查询
		sql.append(roleQLConfigService.getAppendSqlByAllOrg("本周课表","sql","a.BL_CAMPUS_ID"));

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			sqlWhere.append(" order by "+dp.getSidx() + " " + dp.getSord()+" ,a.COURSE_DATE desc, a.COURSE_TIME desc");
			//params.put("orderBy", );
		} else {
			sqlWhere.append(" order by a.COURSE_DATE desc ,a.COURSE_TIME desc");
		}
		sql.append(sqlFrom).append(sqlJoin).append(sqlWhere);
		dp = courseDao.findPageBySql(sql.toString(),dp , true, params);
		return dp;
	}

	/**
	 * 获取一对一批量考勤列表
	 *
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 *//*
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getOneOnOneBatchAttendanceList(CourseVo inputCourseVo,
			DataPackage dp) {
		String voMapperId = "courseFullMapping";

		dp = courseDao.getOneOnOneBatchAttendanceList(inputCourseVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class, voMapperId);

		for (CourseVo courseVo : searchResultVoList) {
			// 学管确认课时
			// CourseAttendanceRecord staduyRecord = courseAttendanceRecordDao
			// .getCourseAttendanceRecordByCourseIdAndRole(
			// courseVo.getCourseId(), RoleCode.STUDY_MANAGER);
			// if (staduyRecord != null) {
			// courseVo.setStaduyManagerAuditHours(staduyRecord
			// .getCourseHours());
			// }
			courseVo.setStaduyManagerAuditHours(courseVo.getAuditHours());
			// 学生对应的学管ID
			// Student student = studentDao.findById(courseVo.getStudentId());
			// courseVo.setStudyManagerId(student.getStudyManegerId());
//			int courseTimeLong = 0;
//			if (courseVo.getRealHours() != null && courseVo.getRealHours().compareTo(BigDecimal.ZERO) > 0) {
//				courseTimeLong = CalculateUtil.calCourseTimeLong(
//						courseVo.getCourseTime(), courseVo.getRealHours());
//
//			} else {
//				courseTimeLong = CalculateUtil.calCourseTimeLong(
//						courseVo.getCourseTime(), courseVo.getPlanHours());
//			}
//			courseVo.setCourseTimeLong(courseTimeLong);

			// 查询所有有效合同产品
			List<Criterion> produceCriterionList = new ArrayList<Criterion>();
			produceCriterionList.add(Restrictions.eq("contract.student.id",
					courseVo.getStudentId()));
			produceCriterionList.add(Restrictions.eq("type",
					com.eduboss.common.ProductType.ONE_ON_ONE_COURSE));
			produceCriterionList.add(Restrictions.gt("price",BigDecimal.ZERO));
			// produceCriterionList.add(Expression.in("status", new
			// ContractProductStatus[]{ContractProductStatus.NORMAL,
			// ContractProductStatus.STARTED}));
			List<ContractProduct> allContractProduces = contractProductDao
					.findAllByCriteria(produceCriterionList);
			BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
//			if (allContractProduces.size() > 0) {
				for (ContractProduct cp : allContractProduces) {
//					if (com.eduboss.common.ProductType.ONE_ON_ONE_COURSE == cp
//							.getType()) {
						if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
							oneOnOneRemainingHour = oneOnOneRemainingHour
									.add(cp.getRemainingAmount().divide(
											cp.getPrice(), 2,
											RoundingMode.HALF_UP));
						}
//					}
//				}
			}
			courseVo.setOneOnOneRemainingHour(oneOnOneRemainingHour);
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}*/


	/**
	 * 获取自动审批课时单数量
	 *
	 * @param courseVo
	 * @return
	 */
	@Override
	public int getOneOnOneAutoRecogCount(CourseVo courseVo) {
		User user  = userService.getCurrentLoginUser();
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT count(c.COURSE_ID) ";
		sql += " from course c, `user` u_teacher, `user` u_study, student s, product p, organization org ";
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.STUDY_MANAGER_ID = u_study.USER_ID ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		sqlWhere.append(" AND c.PRODUCT_ID = P.ID ");
		sqlWhere.append(" AND c.BL_CAMPUS_ID = org.ID ");
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCourseDate())) {
			sqlWhere.append(" AND c.COURSE_DATE = :courseDate ");
			params.put("courseDate", courseVo.getCourseDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStartDate())) {
			sqlWhere.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", courseVo.getStartDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getEndDate())) {
			sqlWhere.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", courseVo.getEndDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStudentName())) {
			sqlWhere.append(" AND s.`NAME` LIKE :studentName ");
			params.put("studentName", "%"+courseVo.getStudentName()+"%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getTeacherName())) {
			sqlWhere.append("AND  u_teacher.`NAME` like :teacherName ");
			params.put("teacherName", "%"+courseVo.getTeacherName()+"%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getTeacherId())) {
			sqlWhere.append("AND  c.TEACHER_ID = :teacherId ");
			params.put("teacherId", courseVo.getTeacherId());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStudyManagerName())){
			sqlWhere.append("AND u_study.`NAME` like  :studyManagerName ");
			params.put("studyManagerName", "%"+courseVo.getStudyManagerName()+"%");
		}
		if (courseVo.getCourseStatus() != null) {
			sqlWhere.append("AND  c.COURSE_STATUS =  :courseStatus ");
			params.put("courseStatus", courseVo.getCourseStatus());
		}

		//自动识别
		sqlWhere.append(" and c.COURSE_ATTENCE_TYPE ='"+CourseAttenceType.AUTO_RECOG.getValue()+"' ");



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
		if(org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCampusId())){
			sqlWhere.append(" AND c.BL_CAMPUS_ID= :campusId ");
			params.put("campusId", courseVo.getCampusId());
		}

		if("teacherAttendance".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
			params.put("teacherId", user.getUserId());
		}else if("studyManegerVerify".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.STUDY_MANAGER_ID = :studyMangerId  ");
			params.put("studyMangerId", user.getUserId());
			Organization org=organizationDao.findById(user.getOrganizationId());
			if(org.getOrgType()==OrganizationType.DEPARTMENT){
				//当前登录人是部门，取到上一级组织id
				Organization o=organizationDao.findById(org.getParentId());
				sqlWhere.append(" AND ( c.BL_CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", o.getId());
				for(Organization orgs:user.getOrganization()){
					sqlWhere.append(" OR c.BL_CAMPUS_ID = :userBlCampusId ");
					params.put("userBlCampusId", orgs.getId());
				}
				sqlWhere.append(" ) ");
			}else{
				sqlWhere.append(" AND c.BL_CAMPUS_ID in (select ORGANIZATION_ID from USER_ORGANIZATION_ROLE WHERE USER_ID = :userId )");
				params.put("userId", userService.getCurrentLoginUser().getUserId());
			}
		}else if("classTeacherDeduction".equals(courseVo.getCurrentRoleId())
				&&  userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)) {
			Organization belongCampus = userService.getBelongCampus();
			sqlWhere.append(" AND c.BL_CAMPUS_ID = :belongCampusId ");
			params.put("belongCampusId", belongCampus.getId());
		} else if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCampusId())) {
				sqlWhere.append(" AND  c.BL_CAMPUS_ID = :courseCampusId ");
				params.put("courseCampusId", courseVo.getCampusId());
			}

		} else{
			sqlWhere.append(" and 1=2");
		}

		sql += sqlWhere.toString();


		if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
			sql += " ORDER BY c.GRADE ASC ";
		} else {
			sql += " ORDER BY u_study.ACCOUNT, u_teacher.ACCOUNT, s.`NAME` DESC, c.COURSE_DATE DESC, c.COURSE_TIME ";
		}


		int count = courseDao.findCountSql(sql, params);

		return count;
	}


	/**
	 * 一对一课时单概要
	 *
	 * @param courseVo
	 * @return
	 */
	@Override
	public Map getOneOnOneAuditSummary(CourseVo courseVo) {
		User user = userService.getCurrentLoginUser();
		Map<String, Object> params = Maps.newHashMap();
		String sql = " SELECT c.* ";
		sql += " from course c, `user` u_teacher, `user` u_study, student s, product p, organization org ";
		StringBuffer sqlWhere = new StringBuffer(" WHERE 1=1 ");
		sqlWhere.append(" AND c.STUDY_MANAGER_ID = u_study.USER_ID ");
		sqlWhere.append(" AND c.TEACHER_ID = u_teacher.USER_ID ");
		sqlWhere.append(" AND c.STUDENT_ID = s.ID ");
		sqlWhere.append(" AND c.PRODUCT_ID = P.ID ");
		sqlWhere.append(" AND c.BL_CAMPUS_ID = org.ID ");
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCourseDate())) {
			sqlWhere.append(" AND c.COURSE_DATE = :courseDate ");
			params.put("courseDate", courseVo.getCourseDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStartDate())) {
			sqlWhere.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", courseVo.getStartDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getEndDate())) {
			sqlWhere.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", courseVo.getEndDate());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStudentName())) {
			sqlWhere.append(" AND s.`NAME` LIKE :studentName ");
			params.put("studentName", "%"+courseVo.getStudentName()+"%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getTeacherName())) {
			sqlWhere.append("AND  u_teacher.`NAME` like :teacherName ");
			params.put("teacherName", "%"+courseVo.getTeacherName()+"%");
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getTeacherId())) {
			sqlWhere.append("AND  c.TEACHER_ID = :teacherId ");
			params.put("teacherId", courseVo.getTeacherId());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getStudyManagerName())){
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
		if(org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCampusId())){
			sqlWhere.append(" AND c.BL_CAMPUS_ID= :campusId ");
			params.put("campusId", courseVo.getCampusId());
		}

		if("teacherAttendance".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.TEACHER_ID = :teacherId ");
			params.put("teacherId", user.getUserId());
		}else if("studyManegerVerify".equals(courseVo.getCurrentRoleId())){
			sqlWhere.append(" AND c.STUDY_MANAGER_ID = :studyMangerId  ");
			params.put("studyMangerId", user.getUserId());
			Organization org=userService.getCurrentLoginUserOrganization();
			if(org.getOrgType()==OrganizationType.DEPARTMENT){
				//当前登录人是部门，取到上一级组织id
				Organization o=organizationDao.findById(org.getParentId());
				sqlWhere.append(" AND ( c.BL_CAMPUS_ID = :blCampusId ");
				params.put("blCampusId", o.getId());
				for(Organization orgs:user.getOrganization()){
					sqlWhere.append(" OR c.BL_CAMPUS_ID = :userBlCampusId ");
					params.put("userBlCampusId", orgs.getId());
				}
				sqlWhere.append(" ) ");
			}else{
				sqlWhere.append(" AND c.BL_CAMPUS_ID in (select ORGANIZATION_ID from USER_ORGANIZATION_ROLE WHERE USER_ID = :userId )");
				params.put("userId", user.getUserId());
			}
		}else if("classTeacherDeduction".equals(courseVo.getCurrentRoleId())
				&&  userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)) {
			Organization belongCampus = userService.getBelongCampus();
			sqlWhere.append(" AND c.BL_CAMPUS_ID = :belongCampusId ");
			params.put("belongCampusId", belongCampus.getId());
		} else if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(courseVo.getCampusId())) {
				sqlWhere.append(" AND  c.BL_CAMPUS_ID = :courseCampusId ");
				params.put("courseCampusId", courseVo.getCampusId());
			}

		} else{
			sqlWhere.append(" and 1=2");
		}

		sql += sqlWhere.toString();


		if ("financeAudit".equals(courseVo.getCurrentRoleId())) {
			sql += " ORDER BY c.GRADE ASC ";
		} else {
			sql += " ORDER BY u_study.ACCOUNT, u_teacher.ACCOUNT, s.`NAME` DESC, c.COURSE_DATE DESC, c.COURSE_TIME ";
		}
		List<Course> courseList = courseDao.findBySql(sql, params);

		Map map = getAuditSummaryMapFromCourseList(courseList, courseVo.getAnshazhesuan());

		return map;

	}

	/**
	 * 获取有效 无效 未审批 各个年级的数据
	 * @param courseList
	 * @param anshazhesuan
	 * @return
	 */
	private Map getAuditSummaryMapFromCourseList(List<Course> courseList, String anshazhesuan) {
		BigDecimal total = BigDecimal.ZERO;//总共
		BigDecimal valid =BigDecimal.ZERO;//有效
		BigDecimal invalid = BigDecimal.ZERO;//无效
		BigDecimal unaudit = BigDecimal.ZERO;//未审

		Map<String, BigDecimal> result = new HashMap<>();

		if ("keshi".equals(anshazhesuan)){
			for (Course c : courseList){
				total = total.add(c.getRealHours());
				if (c.getAuditStatus() == AuditStatus.VALIDATE){
					valid = valid.add(c.getRealHours());
					if (c.getGrade()!=null){
						if (result.containsKey(c.getGrade().getName())){
							BigDecimal grade = result.get(c.getGrade().getName());
							grade = grade.add(c.getRealHours());
							result.put(c.getGrade().getName(), grade);
						}else {
							result.put(c.getGrade().getName(), c.getRealHours());
						}
					}

				}else if (c.getAuditStatus()==AuditStatus.UNVALIDATE){
					invalid = invalid.add(c.getRealHours());
				}else {
					unaudit = unaudit.add(c.getRealHours());
				}
			}

		}else if ("xiaoshi".equals(anshazhesuan)){
			for (Course c : courseList){
				total = total.add(c.getRealHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
				if (c.getAuditStatus() == AuditStatus.VALIDATE){
					valid = valid.add(c.getAuditHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
					if (c.getGrade()!=null){
						if (result.containsKey(c.getGrade().getName())){
							BigDecimal grade = result.get(c.getGrade().getName());
							grade = grade.add(c.getRealHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
							result.put(c.getGrade().getName(), grade);
						}else {
							result.put(c.getGrade().getName(), c.getRealHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
						}
					}
				}else if (c.getAuditStatus() == AuditStatus.UNVALIDATE){
					invalid = invalid.add(c.getRealHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
				}else {
					unaudit = unaudit.add(c.getRealHours().multiply(c.getCourseMinutes()).divide(BigDecimal.valueOf(60), 3, BigDecimal.ROUND_HALF_UP));
				}
			}
		}

		result.put("total", total);
		result.put("valid", valid);
		result.put("invalid", invalid);
		result.put("unaudit", unaudit);



		for (Map.Entry<String, BigDecimal> entry :result.entrySet()){
			result.put(entry.getKey(), entry.getValue().divide(BigDecimal.ONE, 2, BigDecimal.ROUND_HALF_UP));
		}



		return result;
	}

	/**
	 * 获取一对一批量考勤列表
	 *
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getOneOnOneBatchAttendanceList(CourseVo inputCourseVo,
			DataPackage dp) {
		String voMapperId = "courseFullMapping";

		// 改用jdbc查询
		dp = courseDao.getOneOnOneBatchAttendanceList(inputCourseVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class, voMapperId);
		for (CourseVo courseVo : searchResultVoList) {
			// 学管确认课时
			// CourseAttendanceRecord staduyRecord = courseAttendanceRecordDao
			// .getCourseAttendanceRecordByCourseIdAndRole(
			// courseVo.getCourseId(), RoleCode.STUDY_MANAGER);
			// if (staduyRecord != null) {
			// courseVo.setStaduyManagerAuditHours(staduyRecord
			// .getCourseHours());
			// }
			courseVo.setStaduyManagerAuditHours(courseVo.getAuditHours());
			if(courseVo.getCourseAttenceType()!=null){
				courseVo.setCourseAttenceTypeName(courseVo.getCourseAttenceType().getName());
			}else{
				courseVo.setCourseAttenceTypeName("");
			}
			courseVo.setCourseMinutes(new BigDecimal(courseVo.getCourseTimeLong()));
			courseVo.setCourseVersion(transferVersion(courseVo.getcVersion()));

			// 学生对应的学管ID
			// Student student = studentDao.findById(courseVo.getStudentId());
			// courseVo.setStudyManagerId(student.getStudyManegerId());
//			int courseTimeLong = 0;
//			if (courseVo.getRealHours() != null && courseVo.getRealHours().compareTo(BigDecimal.ZERO) > 0) {
//				courseTimeLong = CalculateUtil.calCourseTimeLong(
//						courseVo.getCourseTime(), courseVo.getRealHours());
//
//			} else {
//				courseTimeLong = CalculateUtil.calCourseTimeLong(
//						courseVo.getCourseTime(), courseVo.getPlanHours());
//			}
//			courseVo.setCourseTimeLong(courseTimeLong);
			courseVo.setAuditStatusName(courseVo.getAuditStatus() == null ? null: courseVo.getAuditStatus().getName());
			CourseStatus s = courseVo.getCourseStatus();
			courseVo.setCourseStatusName(s.getName());

			// 查询所有有效合同产品
			List<ContractProduct> allContractProduces = contractProductDao
					.getContractProductByStudent(courseVo.getStudentId(), ProductType.ONE_ON_ONE_COURSE, BigDecimal.ZERO);
			BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
//			if (allContractProduces.size() > 0) {
				for (ContractProduct cp : allContractProduces) {
//					if (com.eduboss.common.ProductType.ONE_ON_ONE_COURSE == cp
//							.getType()) {
						if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
							oneOnOneRemainingHour = oneOnOneRemainingHour
									.add(cp.getRemainingAmount().divide(
											cp.getPrice(), 2,
											RoundingMode.HALF_UP));
						}
//					}
//				}
			}
			courseVo.setOneOnOneRemainingHour(oneOnOneRemainingHour);
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}

	@Override
	public DataPackage getOneOnOneBatchAttendanceListForMobile(
			CourseVo inputCourseVo, DataPackage dp) {
		String voMapperId = "courseFullMapping";
		dp = courseDao.getOneOnOneBatchAttendanceListForMobile(inputCourseVo, dp);
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dp.getDatas(), CourseVo.class, voMapperId);


		for (CourseVo courseVo : searchResultVoList) {
			courseVo.setStaduyManagerAuditHours(courseVo.getAuditHours());
			int courseTimeLong = 0;
			if (courseVo.getRealHours() != null) {
				courseTimeLong = CalculateUtil.calCourseTimeLong(
						courseVo.getCourseTime(), courseVo.getRealHours());
			} else {
				courseTimeLong = CalculateUtil.calCourseTimeLong(
						courseVo.getCourseTime(), courseVo.getPlanHours());
			}
			courseVo.setAuditStatusName(courseVo.getAuditStatus() == null ? null : courseVo.getAuditStatus().getName());
			courseVo.setCourseTimeLong(courseTimeLong);
			courseVo.setCourseStartTime(courseVo.getCourseTime().substring(0,courseVo.getCourseTime().indexOf(" - ")));
			//增加版本返回
			courseVo.setCourseVersion(transferVersion(courseVo.getcVersion()));
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}

/*	@Override
	public DataPackage getOneOnOneBatchAttendanceListForMobile(
			CourseVo inputCourseVo, DataPackage dp) {
		String voMapperId = "courseFullMapping";
		dp = courseDao.getOneOnOneBatchAttendanceListForMobile(inputCourseVo, dp);
		List<CourseVo> searchResultVoList = (List<CourseVo>) dp.getDatas();

		for (CourseVo courseVo : searchResultVoList) {
			if (StringUtil.isNotBlank(courseVo.getGrade())) {
				DataDict gradeDict = dataDictDao.findById(courseVo.getGrade());
				courseVo.setGrade(gradeDict.getName());
				courseVo.setGradeValue(gradeDict.getValue());
			}
			if (StringUtil.isNotBlank(courseVo.getSubject())) {
				DataDict subjectDict = dataDictDao.findById(courseVo.getSubject());
				courseVo.setSubject(subjectDict.getName());
				courseVo.setSubjectValue(subjectDict.getValue());
			}
			if (courseVo.getCourseStatus() != null) {
				courseVo.setCourseStatusName(courseVo.getCourseStatus().getName());
			}
			courseVo.setStaduyManagerAuditHours(courseVo.getAuditHours());
			int courseTimeLong = 0;
			if (courseVo.getRealHours() != null) {
				courseTimeLong = CalculateUtil.calCourseTimeLong(
						courseVo.getCourseTime(), courseVo.getRealHours());
			} else {
				courseTimeLong = CalculateUtil.calCourseTimeLong(
						courseVo.getCourseTime(), courseVo.getPlanHours());
			}

			if (courseVo.getCourseStatus() != CourseStatus.NEW) {
				MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByCourseId(courseVo.getCourseId());
				if (moneyWashRecords != null) {
					courseVo.setIsWashed("TRUE");
					courseVo.setWashRemark(moneyWashRecords.getDetailReason());
				} else {
					courseVo.setIsWashed("FALSE");
				}
			} else {
				courseVo.setIsWashed("FALSE");
			}

			courseVo.setAuditStatusName(courseVo.getAuditStatus() == null ? null : courseVo.getAuditStatus().getName());
			courseVo.setCourseTimeLong(courseTimeLong);
			courseVo.setCourseStartTime(courseVo.getCourseTime().substring(0,courseVo.getCourseTime().indexOf(" - ")));
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}*/

	/**
	 * 获取大课表
	 */
	@SuppressWarnings("unchecked")
	@Override
	public DataPackage getCourseSummaryList(
			CourseSummarySearchInputVo courseSummarySearchInputVo,
			DataPackage dp) {

		dp = courseSummaryDao.getCourseSummaryList(courseSummarySearchInputVo,
				dp);

		List<CourseSummarySearchResultVo> searchResultVoList = HibernateUtils
				.voListMapping((List<CourseSummary>) dp.getDatas(),
						CourseSummarySearchResultVo.class);

		// 计算已排未上课时，学生总剩余课时
		for (CourseSummarySearchResultVo courseSummarySearchResultVo : searchResultVoList) {// 循环所有课程概况

			// 计算已排未上课时
			String courseSummaryId = courseSummarySearchResultVo
					.getCourseSummaryId();
			DataPackage dpThisSummaryAllOfCourse = courseDao
					.getCourseListByCourseSummaryId(courseSummaryId);
			List<Course> courses = (List<Course>) dpThisSummaryAllOfCourse
					.getDatas();
			Double arrengedNotUsedHours = 0.0;
			for (Course course : courses) {// 循环所有具体课程
				if (CourseStatus.NEW == course.getCourseStatus()) {
					arrengedNotUsedHours += course.getPlanHours().doubleValue();
				}
			}
			courseSummarySearchResultVo
					.setArrengedNotUsedHours(arrengedNotUsedHours);

			// 获取学生总剩余课时
			String studentId = courseSummarySearchResultVo.getStudentId();
			StudnetAccMv studnetAccMv = studnetAccMvDao
					.getStudnetAccMvByStudentId(studentId);
			if (studnetAccMv != null) {
				courseSummarySearchResultVo.setRemainingHours(studnetAccMv
						.getOneOnOneRemainingHour()
						.add(studnetAccMv.getOneOnOneFreePaidHour())
						.subtract(studnetAccMv.getOneOnOneFreeCompletedHour())
						.doubleValue());
			}
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}

	/**
	 * 学管排课需求
	 */
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataPackage getCourseRequirementList(
			CourseRequirementSearchInputVo courseRequirementSearchInputVo,
			DataPackage dp) {

		dp = courseRequirementDao.getCourseRequirementList(
				courseRequirementSearchInputVo, dp, userService
						.getBelongCampus().getId(), userService
						.getCurrentLoginUser(), userService.getBelongCampus());
		List searchResultVoList = HibernateUtils.voListMapping(
				(List<CourseRequirement>) dp.getDatas(),
				CourseRequirementSearchResultVo.class);
		dp.setDatas(searchResultVoList);

		return dp;
	}

	/**
	 * 获取某一课程
	 */
	@Override
	public CourseVo findCourseById(String courseId) {
		Course cousre = courseDao.findById(courseId);

		if (cousre == null) {
			throw new ApplicationException("找不到课程。");
		}

		CourseVo courseVo = HibernateUtils.voObjectMapping(cousre,
				CourseVo.class, "courseFullMapping");

		// 学管
		CourseAttendanceRecord staduyRecord = courseAttendanceRecordDao
				.getCourseAttendanceRecordByCourseIdAndRole(
						courseVo.getCourseId(), RoleCode.STUDY_MANAGER);
		if (staduyRecord != null) {
			courseVo.setStaduyManagerAuditHours(staduyRecord.getCourseHours());
		}
		// 教务专员
		CourseAttendanceRecord educatRecord = courseAttendanceRecordDao
				.getCourseAttendanceRecordByCourseIdAndRole(
						courseVo.getCourseId(), RoleCode.EDUCAT_SPEC);
		if (educatRecord != null) {
			courseVo.setTeachingManagerAuditHours(educatRecord.getCourseHours());
		}

		List<Organization> userOrganizations= organizationDao.findOrganizationByUserId(userService.getCurrentLoginUser().getUserId());
		List<String> loginUserOrganizationsId=new ArrayList<String>();
		if(userOrganizations!=null && userOrganizations.size()>0){
			for(Organization org:userOrganizations){
				if(org.getOrgType()==OrganizationType.CAMPUS){
					loginUserOrganizationsId.add(org.getId());
				}else if(org.getOrgType()==OrganizationType.DEPARTMENT){
					loginUserOrganizationsId.add(org.getParentId());
				}
			}
		}
		if (courseVo.getCourseStatus() != CourseStatus.NEW) {
			MoneyWashRecords moneyWashRecords = moneyWashRecordsService.findMoneyWashRecordsByCourseId(courseVo.getCourseId());
			if (moneyWashRecords != null) {
				courseVo.setIsWashed("TRUE");
				courseVo.setWashRemark(moneyWashRecords.getDetailReason());
			} else {
				courseVo.setIsWashed("FALSE");
			}
		} else {
			courseVo.setIsWashed("FALSE");
		}
		//增加版本号
		courseVo.setCourseVersion(transferVersion(courseVo.getcVersion()));


		courseVo.setLoginUserOrganizationId(loginUserOrganizationsId);
		return courseVo;
	}

	/**
	 * 删除某一课程
	 */
	@Override
	public void deleteCourse(CourseEditVo courseEditVo) {

		// 删除课程考勤记录(如果有考勤记录了，课程不应该被删除，只能删除未上课的课程)
		// List<CourseAttendanceRecord> courseAttendanceRecords =
		// courseAttendanceRecordDao
		// .getCourseAttendanceRecordListByCourseId(courseEditVo.getId());
		// for (CourseAttendanceRecord courseAttendanceRecord :
		// courseAttendanceRecords) {
		// courseAttendanceRecordDao.delete(courseAttendanceRecord);
		// }

		// 删除课程
		Course course = courseDao.findById(courseEditVo.getId());
		if(course==null){
			throw new ApplicationException("找不到该节课程，请刷新页面重新操作。");
		}
		int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(course.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
		if (count > 0) {
			throw new ApplicationException("此课程发生过扣费或者扣费冲销，不允许删除，请取消课程或者修改课程信息重新考勤扣费。");
		}
		if (!CourseStatus.CHARGED.equals(course.getCourseStatus())) {
			courseDao.delete(course);
		} else {
			throw new ApplicationException(ErrorCode.COURSE_CAN_NOT_BE_DELETE);
		}
	}

	/**
	 * 修改某一课程
	 */
	@Override
	public void modifyCourse(CourseEditVo courseEditVo) {

		Course course = courseDao.findById(courseEditVo.getId());
		if(course==null){
			throw new ApplicationException("该课程已经被改动，请刷新后重试！");
		}
		String oldStudentId = course.getStudent().getId();
		String oldTeacherId = course.getTeacher().getUserId();
		String oldProductId = course.getProduct().getId();
		String oldSubjectId = course.getSubject().getId();
		String oldCourseTime= course.getCourseTime();
		BigDecimal oldCourseMintes = course.getCourseMinutes();
		BigDecimal oldPlanHours = course.getPlanHours();
		String oldCourseDate = course.getCourseDate();

		int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(course.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
		if (count > 0) {
			throw new ApplicationException("此课程发生过扣费冲销，不允许修改，请取消课程后新建课程重新考勤扣费。");
		}

		if (course == null) {
			throw new ApplicationException(ErrorCode.COURSE_NOT_FOUND);
		}
		 if (!course.getCourseStatus().equals(CourseStatus.NEW)) {
	        	throw new ApplicationException("只有“未上课”状态的课程才允许修改");
        }
		if (course.getCourseStatus().equals(CourseStatus.CHARGED)) {
			throw new ApplicationException("已结算的课程不允许修改");
		}

		//营收凭证如果已经审批完成就不能再排以前的课程了。
		try{
			if(DateTools.getDateSpace(DateTools.getFistDayofMonth(),courseEditVo.getCourseDate())<0){
				odsMonthIncomeCampusService.isFinishAudit(course.getBlCampusId().getId(),DateTools.getDateToString(DateTools.getLastDayOfMonth(courseEditVo.getCourseDate())));
			}
		}catch(ParseException e){
			e.printStackTrace();
		}

		if (StringUtils.isNotBlank(courseEditVo.getTeacherId())) {
			User teacher = userService.getUserById(courseEditVo.getTeacherId());
			if (teacher == null) {
				throw new ApplicationException(ErrorCode.TEACHER_NOT_FOUND);
			}
			course.setTeacher(teacher);
		}
		if (StringUtils.isNotBlank(courseEditVo.getStudentId())) {
			Student student = studentDao.findById(courseEditVo.getStudentId());
			if (student == null) {
				throw new ApplicationException("找不到该学生");
			}
			course.setStudent(student);
		}
		course.setProduct(productDao.findById(courseEditVo.getProductId()));
		course.setSubject(dataDictDao.findById(courseEditVo.getSubject()));
		course.setCourseDate(courseEditVo.getCourseDate());
		course.setCourseTime(courseEditVo.getCourseTime());
		course.setCourseMinutes(courseEditVo.getCourseMinutes());

		course.setPlanHours(courseEditVo.getPlanHours());

		course.setModifyTime(DateTools.getCurrentDateTime());
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();
		course.setModifyUser(user);
		if (course.getCourseId() == null) {
			course.setCreateTime(DateTools.getCurrentDateTime());
			course.setCreateUser(user);
		}

		if(course.getAuditStatus() == AuditStatus.UNVALIDATE){
			course.setAuditStatus(AuditStatus.UNAUDIT);
		}
		// 删除所有考勤记录
		courseDao.deleteCourseAttendance(course.getCourseId());

		// 设置冲突信息
		// course.setStudentCrashInfo(createCrashInfo(course.getStudent().getId(),
		// course.getCourseDate(), course.getCourseTime()));
		// course.setTeacherCrashInfo(createCrashInfo(course.getTeacher().getUserId(),
		// course.getCourseDate(), course.getCourseTime()));
		Boolean flag = false;
		synchronized(course) {
			if(StringUtil.isNotBlank(courseEditVo.getStudentId()) && !oldStudentId.equals(courseEditVo.getStudentId()) ){
				flag = true;
			}
			if(StringUtil.isNotBlank(courseEditVo.getTeacherId()) && !oldTeacherId.equals(courseEditVo.getTeacherId())){
				flag = true;
			}
            if(!oldProductId.equals(courseEditVo.getProductId())){
            	flag = true;
            }
            if(!oldSubjectId.equals(courseEditVo.getSubject())){
            	flag = true;
            }
            if(!oldCourseTime.equals(courseEditVo.getCourseTime())){
            	flag = true;
            }
            if(!oldCourseDate.equals(courseEditVo.getCourseDate())){
            	flag = true;
            }
            if(oldCourseMintes.compareTo(courseEditVo.getCourseMinutes())!=0){
            	flag = true;
            }
            if(oldPlanHours.compareTo(courseEditVo.getPlanHours())!=0){
            	flag = true;
            }
			if(flag){
				course.setcVersion(course.getcVersion()+1);
			}
		}

		courseDao.save(course);
	}

	/**
	 * 删除考勤记录
	 *
	 * @param ids
	 */
	@Override
	public void deleteCourseAttendances(String ids) {
		if (StringUtils.isNotBlank(ids)) {
			String[] idArray = ids.split(",");
			for (int i = 0; i < idArray.length; i++) {
				courseDao.deleteCourseAttendance(idArray[i]);
			}
		} else {
			throw new ApplicationException("传入的课程ID不允许为空");
		}
	}

	/**
	 * 添加或更新某一课程
	 */
	@Override
	public void saveOrUpdateCourse(CourseEditVo courseEditVo) {
		Course course = courseDao.findById(courseEditVo.getId());
		if(courseEditVo.getCancelCourse()!=null && courseEditVo.getCancelCourse().equals("cancel")){

			if(!course.getCourseStatus().equals(CourseStatus.NEW) && !course.getCourseStatus().equals(CourseStatus.TEACHER_ATTENDANCE)){
				throw new ApplicationException("只有“未上课”和“老师已考勤”状态的课程才允许取消!");
			}

			//取消课程，修改课程状态
			 this.updateCourseStatus(courseEditVo.getId(), CourseStatus.CANCEL);
			 return;
		}

		if(!course.getCourseStatus().equals(CourseStatus.NEW)){
			throw new ApplicationException("只有“未上课”的课程才允许修改!");
		}

		User teacher = new User();
		teacher.setUserId(courseEditVo.getTeacherId());


		course.setCourseId(courseEditVo.getId());
		course.setCourseTime(courseEditVo.getCourseTime());
		if (courseEditVo.getCourseMinutes()==null){
			throw new ApplicationException("课时时长为空，请联系系统管理员,谢谢");
		}else {
			course.setCourseMinutes(courseEditVo.getCourseMinutes());
		}
		course.setCourseMinutes(courseEditVo.getCourseMinutes());
		course.setTeacher(teacher);
		course.setPlanHours(courseEditVo.getPlanHours());
		course.setRealHours(courseEditVo.getRealHours());

		course.setModifyTime(DateTools.getCurrentDateTime());
		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();
		course.setModifyUser(user);
		if (course.getCourseId() == null) {
			course.setCreateTime(DateTools.getCurrentDateTime());
			course.setCreateUser(user);
		}

		Organization belongCampus = userService.getBelongCampus();
		if (!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())) {
			throw new ApplicationException("当前用户的归属组织架构只有校区下的才能提排课需求");
		}
		course.setBlCampusId(belongCampus);

		if (course.getStudent() != null) {
			// course.setStudyManager(new
			// User(course.getStudent().getStudyManegerId()));
			course.setStudyManager(studentOrganizationDao
					.findStudentOrganizationByStudentAndOrganization(course
							.getStudent().getId(), belongCampus.getId()));
		}

		String currDateTime = DateTools.getCurrentDateTime();

		// 学管师
		CourseAttendanceRecord studyRecord = courseAttendanceRecordDao
				.getCourseAttendanceRecordByCourseIdAndRole(
						course.getCourseId(), RoleCode.STUDY_MANAGER);
		if (null == studyRecord) {
			studyRecord = new CourseAttendanceRecord();
		}
		if (null == studyRecord.getCheckUserRole()) {// 角色
			List<Role> roles = roleDao.findRoleByCode(RoleCode.STUDY_MANAGER);
			if (roles.size() > 0) {
				Role role = roles.get(0);
				studyRecord.setCheckUserRole(role);
			}
		}
		studyRecord.setCourseHours(courseEditVo.getStaduyManagerAuditHours());
		studyRecord.setCourse(course);
		studyRecord.setCheckUser(user);
		studyRecord.setOprateTime(currDateTime);

		// 教务专员
		CourseAttendanceRecord educatRecord = courseAttendanceRecordDao
				.getCourseAttendanceRecordByCourseIdAndRole(
						course.getCourseId(), RoleCode.EDUCAT_SPEC);
		if (null == educatRecord) {
			educatRecord = new CourseAttendanceRecord();
		}
		if (null == educatRecord.getCheckUserRole()) {// 角色
			List<Role> roles = roleDao.findRoleByCode(RoleCode.EDUCAT_SPEC);
			if (roles.size() > 0) {
				Role role = roles.get(0);
				educatRecord.setCheckUserRole(role);
			}
		}
		educatRecord
				.setCourseHours(courseEditVo.getTeachingManagerAuditHours());
		educatRecord.setCourse(course);
		educatRecord.setCheckUser(user);
		educatRecord.setOprateTime(currDateTime);

		courseAttendanceRecordDao.save(studyRecord);
		courseAttendanceRecordDao.save(educatRecord);

		// 设置冲突信息
		course.setStudentCrashInfo(createCrashInfo(course.getStudent().getId(),
				course.getCourseDate(), course.getCourseTime()));
		course.setTeacherCrashInfo(createCrashInfo(course.getTeacher()
				.getUserId(), course.getCourseDate(), course.getCourseTime()));

		if (course.getStudyManager() == null) {
			throw new ApplicationException(ErrorCode.NOT_FIND_STUDY_MANAGER);
		}
		courseDao.save(course);
	}

	/**
	 * 创建冲突信息
	 *
	 * @param id
	 * @param date
	 * @param time
	 * @return
	 */
	private String createCrashInfo(String id, String date, String time) {
		return id + "#" + date + " " + time;
	}

	@Override
	public void courseCellEdit(CourseEditVo courseEditVo) {
		if (!RoleCode.STUDY_MANAGER.equals(userService.getCurrentLoginUser()
				.getRoleCode())) {
			throw new ApplicationException(ErrorCode.USER_AUTHROTY_FAIL);
		}
		Course course = courseDao.findById(courseEditVo.getId());
		if (course != null) {
			if (StringUtils.isNotBlank(courseEditVo.getCourseTime()))
				course.setCourseTime(courseEditVo.getCourseTime());
			if (StringUtils.isNotBlank(courseEditVo.getCourseDate()))
				course.setCourseDate(courseEditVo.getCourseDate());
			if (StringUtils.isNotBlank(courseEditVo.getTeacherId()))
				course.setTeacher(new User(courseEditVo.getTeacherId()));
			if (courseEditVo.getCourseStatus() != null)
				course.setCourseStatus(courseEditVo.getCourseStatus());
		}
	}

	/**
	 * 添加或更新排课需求
	 */
	@Override
	public void saveOrUpdateCourseRequirement(
			CourseRequirementEditVo courseRequirementEditVo) {
		CourseRequirement courseRequirement = null;
		if (StringUtils.isNotBlank(courseRequirementEditVo.getId())) {
			courseRequirement = courseRequirementDao
					.findById(courseRequirementEditVo.getId());
		} else {
			courseRequirement = new CourseRequirement();
		}

		if (StringUtils.isNotBlank(courseRequirementEditVo.getStudentId())) {// 学生ID
			Student student = studentDao.findById(courseRequirementEditVo
					.getStudentId());
			if (student != null) {
				courseRequirement.setStudent(student);
				// courseRequirement.setBlCampusId(student.getBlCampus());
			}
		}
		if (StringUtils.isNotBlank(courseRequirementEditVo
				.getCourseDateDesciption())) {// 排课说明
			if (courseRequirementEditVo.getCourseDateDesciption().length() < 256) {
				courseRequirement
						.setCourseDateDesciption(courseRequirementEditVo
								.getCourseDateDesciption());
			} else {
				throw new ApplicationException("输入字数超过限制，请重新输入。");
			}
		}
		if (StringUtils.isNotBlank(courseRequirementEditVo.getStartDate())) {// 开始日期
			courseRequirement.setStartDate(courseRequirementEditVo
					.getStartDate());
		}
		if (StringUtils.isNotBlank(courseRequirementEditVo.getEndDate())) {// 结束日期
			courseRequirement.setEndDate(courseRequirementEditVo.getEndDate());
		}
		if (StringUtils
				.isNotBlank(courseRequirementEditVo.getLastArrangeTime())) {// 最迟排课时间
			courseRequirement.setLastArrangeTime(courseRequirementEditVo
					.getLastArrangeTime());
		}
		if (courseRequirementEditVo.getRequirementCetegory() != null) {// 课程需求状态
			courseRequirement.setRequirementCetegory(courseRequirementEditVo
					.getRequirementCetegory());
		}

		courseRequirement.setModifyTime(DateTools.getCurrentDateTime());
		User user = userService.getCurrentLoginUser();
		courseRequirement.setModifyUser(user);
		if (courseRequirement.getId() == null) {
			// 如果没有排课状态：设置未排课的状态
			if (StringUtils.isBlank(courseRequirementEditVo
					.getRequirementStatus())) {
				courseRequirement
						.setRequirementStatus(CourseRequirementStatus.WAIT_FOR_ARRENGE);
			}
			if (courseRequirementEditVo.getRequirementCetegory().equals(
					CourseRequirementCetegory.EDUCATOR_SUBMIT.getValue())) {
				courseRequirement
						.setLastArrangeTime(DateTools.getCurrentDate());
				courseRequirement.setCourseDateDesciption("教务直接排课！");
			}

			courseRequirement.setCreateTime(DateTools.getCurrentDateTime());
			courseRequirement.setCreateUser(user);

		}
		if (StringUtils.isNotBlank(courseRequirementEditVo
				.getRequirementStatus())) {
			if (courseRequirementEditVo.getRequirementStatus().equals(
					CourseRequirementStatus.ARRENGED.getValue())) {
				courseRequirement
						.setRequirementStatus(CourseRequirementStatus.ARRENGED);
			} else {
				courseRequirement
						.setRequirementStatus(CourseRequirementStatus.WAIT_FOR_ARRENGE);
			}
		}

		courseRequirementDao.save(courseRequirement);
	}

	/**
	 * 删除排课需求
	 */
	@Override
	public void deleteCourseRequirement(
			CourseRequirementEditVo courseRequirementEditVo) {

		CourseRequirement courseRequirement = new CourseRequirement();
		courseRequirement.setId(courseRequirementEditVo.getId());
		courseRequirementDao.delete(courseRequirement);
	}

	/**
	 * 查询单个排课需求
	 */
	@Override
	public CourseRequirementEditVo findCourseRequirementById(
			String courseRequirementId) {
		CourseRequirement courseRequirement = courseRequirementDao
				.findById(courseRequirementId);
		CourseRequirementEditVo courseRequirementEditVo = HibernateUtils
				.voObjectMapping(courseRequirement,
						CourseRequirementEditVo.class);
		return courseRequirementEditVo;
	}

	/**
	 * 查询学生排课需求
	 */
	@Override
	public List<CourseRequirementEditVo> findCourseRequirementByStudentId(String studentId) {

		List<Criterion> requirementList = new ArrayList<Criterion>();
		requirementList.add(Restrictions.eq("student.id",
				studentId));
		List<CourseRequirement> allContractProduces = courseRequirementDao
				.findAllByCriteria(requirementList);

		List<CourseRequirementEditVo> courseRequirementEditVos = HibernateUtils
				.voListMapping(allContractProduces,
						CourseRequirementEditVo.class);
		return courseRequirementEditVos;
	}


	/**
	 * 获取要批量更改课程的列表
	 */
	@Override
	public DataPackage getCourseChangesList(
			CourseSearchInputVo courseSearchInputVo, DataPackage dp) {

		dp = courseDao.getCourseChangesList(courseSearchInputVo, dp);
		List<Course> courseList = (List<Course>) dp.getDatas();
		List searchResultVoList = HibernateUtils
				.voListMapping((List<Course>) dp.getDatas(),
						CourseChangesSearchResultVo.class);
		for (int i = 0; i < searchResultVoList.size(); i++) {
			Course c = courseList.get(i);
			CourseChangesSearchResultVo vo = (CourseChangesSearchResultVo) searchResultVoList
					.get(i);
			vo.setConflictCount(courseConflictDao.countDistinctConflicts(c
					.getCourseId(), c.getStudent().getId(), c.getTeacher()
					.getUserId(), c.getCourseDate(), c.getCourseTime()));
		}
		dp.setDatas(searchResultVoList);

		return dp;
	}

	/**
	 * 批量修改课程属性
	 *
	 * @throws Exception
	 */
	@Override
	public void courseAttrChanges(String changesAttr, String changesData,
			String idsString, CourseSearchInputVo courseSearchInputVo)
			throws Exception {

		boolean updateAll = false;
		String ids[] = idsString.split(",");
		if ("all".equals(idsString)) {
			updateAll = true;
		}

		if (changesAttr.startsWith("courseStatus:")) {
			changesData = changesAttr.substring(changesAttr.indexOf(":") + 1);
			changesAttr = "courseStatus";
		}

		// 已结算不能修改
		// if("courseStatus".equals(changesAttr) &&
		// "CHARGED".equals(changesData)){
		// List<Course> list=courseDao.queryCourseAttrChanges( updateAll,
		// ids,courseSearchInputVo);
		// User user = userService.getCurrentLoginUser();
		// for(Course course : list){
		// chargeService.chargeCourse(course.getCourseId(), user);
		// }
		// }

		if ("courseTime".equals(changesAttr)) {// 时间段属性特别处理
			if (StringUtils.isNotBlank(changesData)) {// 有值才从数据字典中找时间段
				// DataDict courseTime = dataDictDao.findById(changesData);
				// if (courseTime != null) {// 找到时间段才更新
				// courseDao.courseAttrChanges(changesAttr,
				// courseTime.getName(), updateAll, ids,
				// courseSearchInputVo);
				// }
				courseDao.courseAttrChanges(changesAttr, changesData,
						updateAll, ids, courseSearchInputVo);
			} else {// 否则设为空
				throw new ApplicationException("请选择时间");
			}
		} else if ("courseStatus".equals(changesAttr)) {// 如果是修改课程状态
			if (CourseStatus.TEACHER_LEAVE.getValue().equals(changesData)
					|| CourseStatus.STUDENT_LEAVE.getValue()
							.equals(changesData)
					|| CourseStatus.CANCEL.getValue().equals(changesData)
					|| CourseStatus.NEW.getValue().equals(changesData)) {
				courseDao.courseAttrChanges(changesAttr, changesData,
						updateAll, ids, courseSearchInputVo);
			} else {
				throw new ApplicationException(
						"只允许把课程状态改为\"未上课\"、\"老师请假\"、\"学生请假\"、\"取消课程\"这四种状态！");
			}

		} else {// 其他属性一般处理
			courseDao.courseAttrChanges(changesAttr, changesData, updateAll,
					ids, courseSearchInputVo);
		}
	}

	@Override
	public void batchChangeTeacher(String teacherUserId, String idsString) {
		String ids[] = idsString.split(",");
		courseDao.batchChangeTeacher(teacherUserId, ids);
	}

	/**
	 * 批量替换年级
	 */
	@Override
	public void batchChangeCourseGrade (String gradeId, String ids){
		courseDao.batchChangeCourseGrade(gradeId, ids);
	}

	/**
	 * 获取当前登录老师的课程表
	 */
	@Override
	public DataPackage getTeacherCourseList(DataPackage dp) {

		User user = ((UserDetailsImpl) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUser();

		CourseSearchInputVo courseSearchInputVo = new CourseSearchInputVo();
		// courseSearchInputVo.setTeacherId(user.getUserId());

		dp = courseDao.getCourseList(courseSearchInputVo, dp);

		dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),
				CourseVo.class, "courseFullMapping"));
		return dp;
	}

	/**
	 * 查询某一排课需求的所有已排课程
	 */
	@Override
	public DataPackage getCourseRequirementArrengedCourseList(
			String courseRequirementId, String startDate, String endDate,
			DataPackage dp) {

		dp = courseDao.getCourseRequirementArrengedCourseList(
				courseRequirementId, startDate, endDate, dp);
		dp.setDatas(HibernateUtils.voListMapping((List) dp.getDatas(),
				CourseVo.class, "courseFullMapping"));
		return dp;
	}

    private static final String WhiteOrgList = PropertiesUtils.getStringValue("WhiteOrgList");
    private static final String WhiteUserList = PropertiesUtils.getStringValue("WhiteUserList");
    private static final String IsOpenShaoYiShao = PropertiesUtils.getStringValue("IsOpenShaoYiShao");
    private Boolean checkCodeForShaoYiShao(Boolean isOpen,String userId){
        if(StringUtil.isNotBlank(userId)){
        	Boolean isOpenShaoYiShao = Boolean.valueOf(IsOpenShaoYiShao);
        	if(!isOpenShaoYiShao){
        		Organization blCampus = userService.getBelongCampusByUserId(userId);
                if(blCampus!=null){
                	if(StringUtil.isNotBlank(WhiteOrgList)){
                		String orgId = blCampus.getId();
                		String[] orgIds = WhiteOrgList.split(",");
                		for(String id:orgIds){
                			if(id.equals(orgId)){
                				isOpen = true;
                				break;
                			}
                		}
                	}
                }
	        	if(!isOpen){
		        	if(StringUtil.isNotBlank(WhiteUserList)){
		        		String[] userIds= WhiteUserList.split(",");
		        		//白名单里面的可以开通
		        		for(String id:userIds){
		        			if(id.equals(userId)){
		        				isOpen = true;
		        				break;
		        			}
		        		}
		        	}
	        	}

        	}else{
        		//开通了就不需要校验userList 和orgList
        		isOpen = true;
        	}
        }
        return isOpen;
	}
	/**
	 * 一对一批量考勤编辑
	 *
	 * @throws Exception
	 */
	@Override
	public void oneOnOneBatchAttendanceEdit(
			OneOnOneBatchAttendanceEditVo attendanceEditVo) throws Exception {

		List<String> studentIds=new ArrayList<String>();
		User user = userService.getCurrentLoginUser();
		Boolean isOpen = false;
		isOpen = checkCodeForShaoYiShao(isOpen, user.getUserId());

		log.info("attendanceEdit:hours:"+attendanceEditVo.getRealHours());
		log.info("attendanceEdit:operteType:"+attendanceEditVo.getOperteType());
		log.info("attendanceEdit:courseId:"+attendanceEditVo.getCourseId());

		CourseAttenceType courseAttenceType = null;
		if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
			courseAttenceType = CourseAttenceType.valueOf(attendanceEditVo.getCourseAttenceType());
		}
		String attendanceDetailsType = attendanceEditVo.getAttendanceDetail();

		if (!"confirmationFee".equals(attendanceEditVo.getOperteType())) {
			// 单条考勤
			if (StringUtil.isNotBlank(attendanceEditVo.getCourseId())) {
				Course course = courseDao.findById(attendanceEditVo
						.getCourseId());
				if (course == null) {
					throw new ApplicationException("课程编号" + attendanceEditVo.getCourseId() + "已被删除");
				}

				//校验课程版本号
				if(isOpen && StringUtil.isNotBlank(attendanceEditVo.getCourseVersion())){
					Course c = courseDao.findById(attendanceEditVo.getCourseId());
					String version = transferVersion(c.getcVersion());
					if(!attendanceEditVo.getCourseVersion().equals(version)){
						throw new ApplicationException("课程版本错误，请重新打印课时单");
					}
				}

				if(StringUtil.isNotBlank(attendanceDetailsType)){
					course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
				}

				if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
					course.setCourseAttenceType(courseAttenceType);
				}else{

					//注释代码
					if(course.getRealHours()!=null){
						//PC老师考勤
						if(attendanceEditVo.getRealHours()!=null && attendanceEditVo.getRealHours().compareTo(course.getRealHours().doubleValue())!=0){
							course.setCourseAttenceType(CourseAttenceType.MANUALLY_FILL_IN);
						}
						//学管考勤
						if(attendanceEditVo.getStaduyManagerAuditHours()!=null && attendanceEditVo.getStaduyManagerAuditHours().compareTo(course.getRealHours().doubleValue())!=0){
							course.setCourseAttenceType(CourseAttenceType.MANUALLY_FILL_IN);
						}
					}else{
						if(attendanceEditVo.getRealHours()!=null){
							course.setCourseAttenceType(CourseAttenceType.MANUALLY_FILL_IN);
						}
						if(attendanceEditVo.getStaduyManagerAuditHours()!=null ){
							course.setCourseAttenceType(CourseAttenceType.MANUALLY_FILL_IN);
						}
					}


				}

				if(course.getAuditStatus() == AuditStatus.UNVALIDATE){
					course.setAuditStatus(AuditStatus.UNAUDIT);
				}
				String courseTime = attendanceEditVo.getCourseTime();
				if (courseTime == null) {
					courseTime = course.getCourseTime();
				}
				if(course.getFirstAttendentTime() == null || StringUtils.isBlank(course.getFirstAttendentTime())){
					course.setFirstAttendentTime(DateTools.getCurrentDateTime());
				}
				int countDistinctConflicts = courseConflictDao
						.countDistinctConflicts(course.getCourseId(), course
								.getStudent().getId(), course.getTeacher()
								.getUserId(), course.getCourseDate(), courseTime);
				if (countDistinctConflicts > 0) {
					throw new ApplicationException("考勤失败：课程编号"
							+ attendanceEditVo.getCourseId()
							+ "课程冲突，请找排课人员调整修改后再考勤");
				}
				courseDao.save(course);
			} else {
				// 老师或学管批量考勤
				if (StringUtils.isNotBlank(attendanceEditVo.getIds())) {
					String[] idArray = attendanceEditVo.getIds().split(",");
					Course course = null;
					int countDistinctConflicts = 0;
					for (String id : idArray) {
						course = courseDao.findById(id);
						countDistinctConflicts = courseConflictDao
								.countDistinctConflicts(course.getCourseId(),
										course.getStudent().getId(), course
												.getTeacher().getUserId(),
										course.getCourseDate(), course
												.getCourseTime());
						if(course.getFirstAttendentTime() == null || StringUtils.isBlank(course.getFirstAttendentTime())){
							course.setFirstAttendentTime(DateTools.getCurrentDateTime());
						}
						if (countDistinctConflicts > 0) {
							throw new ApplicationException("考勤失败：课程编号"
									+ attendanceEditVo.getCourseId()
									+ "课程冲突，请找排课人员调整修改后再考勤");
						}
						if(course.getAuditStatus() == AuditStatus.UNVALIDATE){
							course.setAuditStatus(AuditStatus.UNAUDIT);
						}

						if(StringUtil.isNotBlank(attendanceDetailsType)){
							course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
						}

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
							course.setCourseAttenceType(courseAttenceType);
						}
						courseDao.save(course);
					}
				}else if(StringUtils.isNotBlank(attendanceEditVo.getTargetParams())){//mobileinterface用，课程管理，targetParams=课程id-考勤课时
					String[] courseParamArray = attendanceEditVo.getTargetParams().split(",");
					if(courseParamArray.length > 0){
						Course course =null;
						int countDistinctConflicts=0;
						for (String courseParam : courseParamArray) {
							String[] courseidNtime = courseParam.split("-",2);
							String id = courseidNtime[0];
							if(StringUtil.isNotBlank(id)){
								course = courseDao.findById(id);
                                if(course==null){
                                    throw new ApplicationException("课程"+id+"已经重新排课，请刷新后重试！");
                                }
								if(course.getFirstAttendentTime() == null || StringUtils.isBlank(course.getFirstAttendentTime())){
									course.setFirstAttendentTime(DateTools.getCurrentDateTime());
								}

								if(StringUtil.isNotBlank(attendanceDetailsType)){
									course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
								}

								if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
									course.setCourseAttenceType(courseAttenceType);
								}
								countDistinctConflicts=courseConflictDao.countDistinctConflicts(course.getCourseId(), course.getStudent().getId(), course.getTeacher().getUserId(), course.getCourseDate(), course.getCourseTime());
								if(countDistinctConflicts>0){
									throw new ApplicationException("考勤失败：课程编号"+attendanceEditVo.getCourseId()+"课程冲突，请找排课人员调整修改后再考勤");
								}
								if(course.getAuditStatus() == AuditStatus.UNVALIDATE){
									course.setAuditStatus(AuditStatus.UNAUDIT);
								}
								courseDao.save(course);
							}
						}
					}
				}

			}

		}
		if ("confirmationFee".equals(attendanceEditVo.getOperteType())) {

			//mobileinterface如果带课时的课程id不为空，则以此id集代替vo的课程id集（以活用旧逻辑）
			if(StringUtils.isNotBlank(attendanceEditVo.getTargetParams())){
				String[] courseParamArray = attendanceEditVo.getTargetParams().split(",");
				StringBuilder sbIds = new StringBuilder();
				for (String courseParam : courseParamArray) {
					String[] courseMessage = courseParam.split("-",2);
					sbIds.append(courseMessage[0]).append(",");
				}
				if(sbIds.length() > 0){
					sbIds.setLength(sbIds.length() - 1);
					attendanceEditVo.setIds(sbIds.toString());
				}
			}

			if (StringUtils.isNotBlank(attendanceEditVo.getIds())) {
				String[] idArray = attendanceEditVo.getIds().split(",");
				Map<String,BigDecimal> cosumeMap = new HashMap<>();
				for (String id : idArray) {//先校验是否可以扣费，防止营收先进redis 导致数据问题
					Course course = courseDao.findById(id);
					cosumeMap=checkCanChargeOneOnOne(course,cosumeMap);
				}

				for (String id : idArray) {

					Course course = courseDao.findById(id);

					if(StringUtil.isNotBlank(attendanceDetailsType)){
						course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
					}

					if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
						course.setCourseAttenceType(courseAttenceType);
					}
					int countDistinctConflicts = courseConflictDao
							.countDistinctConflicts(course.getCourseId(),
									course.getStudent().getId(), course
											.getTeacher().getUserId(), course
											.getCourseDate(), course
											.getCourseTime());
					if (countDistinctConflicts > 0) {
						throw new ApplicationException("考勤失败：课程编号" + id
								+ "课程冲突，请找排课人员调整修改后再扣费");
					}
					if (CourseStatus.STUDY_MANAGER_AUDITED == course
							.getCourseStatus()) {// 只有课程状态为学管已核对，才能扣费!
						BigDecimal courseAmout = chargeService.chargeOneOnOneCourse(id, user);

						courseDao.getHibernateTemplate().refresh(course);
						if (CourseStatus.CHARGED.equals(course.getCourseStatus())) {
							throw new ApplicationException("课程:" + course.getCourseId() + "，已经扣费!");
						}
						// 课程状态变为已扣费
						course.setCourseStatus(CourseStatus.CHARGED);
						course.setTeachingManagerAuditId(user.getUserId());
						course.setTeachingManagerAuditTime(DateTools
								.getCurrentDateTime());

						CourseAttendanceRecord studyRecord = new CourseAttendanceRecord();
						if (null == studyRecord.getCheckUserRole()) {// 角色
							List<Role> roles = roleDao
									.findRoleByCode(RoleCode.EDUCAT_SPEC);
							if (roles.size() > 0) {
								Role role = roles.get(0);
								studyRecord.setCheckUserRole(role);
							}
						}

						studyRecord.setCourseHours(course.getAuditHours());
						studyRecord.setCourse(course);
						studyRecord.setCheckUser(user);
						studyRecord.setOprateTime(DateTools
								.getCurrentDateTime());

						// 保存考勤记录
						courseAttendanceRecordDao.save(studyRecord);
						StudnetAccMv avv = getStudentAccoutInfo(course
								.getStudent().getId());
						studnetAccMvDao.save(avv);



						studentIds.add(course.getStudent().getId());
						// 通知老师学管学生已来扣费

//    关闭dwr   2016-12-17
//						messageService.sendMessage(MessageType.SYSTEM_MSG,
//								"学生课程扣费提醒", "学生"
//										+ course.getStudent().getName() + "的课程"
//										+ course.getSubject().getName()
//										+ "已扣费。", MessageDeliverType.SINGLE,
//								course.getStudyManager().getUserId());

						courseDao.save(course);
					} else {
						throw new ApplicationException("只有课程状态为学管已核对，才能扣费!");
					}
				}
			} else {
				// throw new ApplicationException("扣费的课程IDS不能为空!");
			}
			for (String studentId:studentIds){
				//学生状态处理
				PushRedisMessageUtil.pushStudentToMsg(studentId);
			}
		} else if ("teacherAudit".equals(attendanceEditVo.getOperteType())) {
			if (StringUtil.isNotBlank(attendanceEditVo.getCourseId())) {// 老师单条考勤
				if ((attendanceEditVo.getRealHours() != null) && StringUtils.isNotBlank(attendanceEditVo .getCourseId())) {
					if (attendanceEditVo.getRealHours() .equals(new Double("-1"))) {
						updateCourseStatus(attendanceEditVo.getCourseId(),CourseStatus.STUDENT_LEAVE);

						//校验课程版本号
						if(isOpen && StringUtil.isNotBlank(attendanceEditVo.getCourseVersion())){
							Course c = courseDao.findById(attendanceEditVo.getCourseId());
							String version = transferVersion(c.getcVersion());
							if(!attendanceEditVo.getCourseVersion().equals(version)){
								throw new ApplicationException("课程版本错误，请重新打印课时单");
							}
						}


						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					} else if (attendanceEditVo.getRealHours().equals(new Double("-2"))) {
						updateCourseStatus(attendanceEditVo.getCourseId(),CourseStatus.TEACHER_LEAVE);

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					} else if (new BigDecimal(attendanceEditVo.getRealHours()).compareTo(BigDecimal.ZERO) == 0) {
						throw new ApplicationException("实际上课课时必须大于0，如果有请假，请在pc端操作本课程老师请假或学生请假!");
					} else {
						submitCourseAttendance(attendanceEditVo.getCourseId(),
								attendanceEditVo.getRealHours().toString(),
								attendanceEditVo.getCourseTime(), user,
								RoleCode.TEATCHER);

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					}
				}
			} else {
				// 批量考勤
				// 老师批量考勤
				// 老师或学管批量考勤
				if (StringUtils.isNotBlank(attendanceEditVo.getIds())) {
					String[] idArray = attendanceEditVo.getIds().split(",");
					Course course = null;
					for (String id : idArray) {
						course = courseDao.findById(id);
						if (course != null) {// 老师考勤时默认拿计划课时
							submitCourseAttendance(id, course.getPlanHours()
									.toString(), null, user, RoleCode.TEATCHER);

							if(StringUtil.isNotBlank(attendanceDetailsType)){
								course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
							}

							if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
								course.setCourseAttenceType(courseAttenceType);
							}

						}
						courseDao.save(course);
					}
				}else if(StringUtils.isNotBlank(attendanceEditVo.getTargetParams())){//mobileinterface如果不是普通批量的话还需要判断是不是带考勤时间那个
					Course course =null;
					String[] courseParamArray = attendanceEditVo.getTargetParams().split(",");
					if(courseParamArray.length > 0){
						for (String courseParam : courseParamArray) {
							String[] courseArray = courseParam.split("-",2);
							String id = courseArray[0];
							String realHour = courseArray[1];
							course = courseDao.findById(id);
							if(course!=null){//老师考勤时默认拿计划课时
								submitCourseAttendance(id,realHour,null, user,	RoleCode.TEATCHER);

								if(StringUtil.isNotBlank(attendanceDetailsType)){
									course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
								}

								if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
									course.setCourseAttenceType(courseAttenceType);
								}

							}
							courseDao.save(course);
						}
					}
				}

			}

		} else if ("staduyManagerAudit".equals(attendanceEditVo.getOperteType())) {
			this.checkContractProductSubjectHoursByAttendanceEditVo(attendanceEditVo); // 学管确认判断对应科目分配的课时能否够考勤
			if (StringUtil.isNotBlank(attendanceEditVo.getCourseId())) {// 学管单条考勤
				if ((attendanceEditVo.getStaduyManagerAuditHours() != null)
						&& StringUtils.isNotBlank(attendanceEditVo
								.getCourseId())) {
					if (attendanceEditVo.getStaduyManagerAuditHours().equals(
							new Double("-1"))) {
						updateCourseStatus(attendanceEditVo.getCourseId(),
								CourseStatus.STUDENT_LEAVE);

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())
								|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					} else if (attendanceEditVo.getStaduyManagerAuditHours()
							.equals(new Double("-2"))) {
						updateCourseStatus(attendanceEditVo.getCourseId(),
								CourseStatus.TEACHER_LEAVE);

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())
								|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					} else if (new BigDecimal(
							attendanceEditVo.getStaduyManagerAuditHours())
							.compareTo(BigDecimal.ZERO) == 0) {
						throw new ApplicationException(
								"实际上课课时必须大于0，如果有请假，请在pc端操作本课程老师请假或学生请假!");
					} else {
						submitCourseAttendance(attendanceEditVo.getCourseId(),
								attendanceEditVo.getStaduyManagerAuditHours()
										.toString(),
								attendanceEditVo.getCourseTime(), user,
								RoleCode.STUDY_MANAGER);

						if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())
								|| StringUtil.isNotBlank(attendanceDetailsType)) {
							Course course = courseDao.findById(attendanceEditVo.getCourseId());
							if (course != null) {
								if(StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())){
									course.setCourseAttenceType(courseAttenceType);
								}
                                if(StringUtil.isNotBlank(attendanceDetailsType)){
                                	course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
                                }
							}
							courseDao.save(course);
						}

					}
				}
			} else {
				// 学管批量考勤
				if (StringUtils.isNotBlank(attendanceEditVo.getIds())) {
					String[] idArray = attendanceEditVo.getIds().split(",");
					Course course = null;
					Map<String, BigDecimal> studentCourseMap = new HashMap<String, BigDecimal>();
					for (String id : idArray) {
						course = courseDao.findById(id);
						if (course != null) {// 学管考勤时默认拿老师审核课时
							submitCourseAttendance(id, course.getRealHours()
									.toString(), null, user,
									RoleCode.STUDY_MANAGER);


							if(StringUtil.isNotBlank(attendanceDetailsType)){
								course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
							}

							if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
								course.setCourseAttenceType(courseAttenceType);
							}

						}
						courseDao.save(course);
					}
				}else if(StringUtils.isNotBlank(attendanceEditVo.getTargetParams())){//mobileinterface如果不是普通批量的话还需要判断是不是带考勤时间那个

					Course course =null;
					String[] courseParamArray = attendanceEditVo.getTargetParams().split(",");
					if(courseParamArray.length > 0){
						this.checkStudyManageAttendaceSubmit(courseParamArray);//手机端添加学管考勤时判断课时是否足够    add by yaoyuqi   16-03-28
						for (String courseParam : courseParamArray) {
							String[] courseArray = courseParam.split("-",2);
							String id = courseArray[0];
							String realHour = courseArray[1];
							course = courseDao.findById(id);
							if(course!=null){//老师考勤时默认拿计划课时
								submitCourseAttendance(id,realHour,null, user,	RoleCode.STUDY_MANAGER);

								if(StringUtil.isNotBlank(attendanceDetailsType)){
									course.setAttendanceDetail(AttendanceDetailsType.valueOf(attendanceDetailsType));
								}

								if (StringUtil.isNotBlank(attendanceEditVo.getCourseAttenceType())) {
									course.setCourseAttenceType(courseAttenceType);
								}

							}
							courseDao.save(course);
						}
					}
				}
			}

		} else {
			throw new ApplicationException("一对一批量考勤没有相关的匹配操作!");
		}

	}

	public Map<String,BigDecimal> checkCanChargeOneOnOne( Course course,Map<String,BigDecimal> cosumeMap) {
		BigDecimal courseAmout=BigDecimal.ZERO;
		// 按照时间顺序获取不同的对应课程科目合同产品
		List<ContractProduct> oneOnOneProductList = contractService.getOrderValidSubjectOneOnOneContractProducts(course);
		boolean allContractProductFrozen = true;
		List<ContractProduct> withOutFrozenList = new ArrayList<>();
		for (ContractProduct cp: oneOnOneProductList){
			if (cp.getIsFrozen() == 1){
				allContractProductFrozen = false;
				withOutFrozenList.add(cp);
			}
		}
		if (oneOnOneProductList.size()>0 && allContractProductFrozen){
			throw new ApplicationException("扣费所有合同产品已经冻结，不能进行扣费操作！");
		}
		BigDecimal chargeCourseHour = course.getRealHours();

		for(ContractProduct conProd :withOutFrozenList) {
			if(cosumeMap.get(conProd.getId())!=null){
				chargeCourseHour=chargeCourseHour.add(cosumeMap.get(conProd.getId()));
			}
			BigDecimal price =  conProd.getPrice();
			BigDecimal chargeHours = BigDecimal.ZERO;
			if (chargeCourseHour.compareTo(conProd.getSubjectRemainHours()) >= 0) {
				chargeHours = conProd.getSubjectRemainHours();
			} else {
				chargeHours = chargeCourseHour;
			}
			BigDecimal chargeMoney = price.multiply(chargeHours).setScale(2, BigDecimal.ROUND_DOWN);
			BigDecimal remainingMoney = conProd.getRemainingAmount();
			if(remainingMoney.compareTo(chargeMoney)>=0 && remainingMoney.compareTo(BigDecimal.ZERO)>0 && conProd.getSubjectRemainHours().compareTo(chargeCourseHour) >= 0) {
				checkCanChargeOneOnOneDetail(conProd.getContract(), conProd, chargeMoney, chargeCourseHour , course);
				chargeCourseHour = BigDecimal.ZERO;
				if(chargeMoney!=null){
					courseAmout=courseAmout.add(chargeMoney);
				}
				cosumeMap.put(conProd.getId(),chargeCourseHour);
				break;
			} else if(remainingMoney.compareTo(chargeMoney)>=0 && remainingMoney.compareTo(BigDecimal.ZERO)>0){
				chargeCourseHour = chargeCourseHour.subtract(conProd.getSubjectRemainHours());
				if(chargeMoney!=null){
					courseAmout=courseAmout.add(chargeMoney);
				}
				checkCanChargeOneOnOneDetail(conProd.getContract(), conProd, chargeMoney, conProd.getSubjectRemainHours() , course);
				cosumeMap.put(conProd.getId(),conProd.getSubjectRemainHours());
			}

		}
		if(oneOnOneProductList == null || oneOnOneProductList.size() == 0 || chargeCourseHour.compareTo(BigDecimal.ZERO) > 0 ) {
			throw new ApplicationException(course.getStudent().getName()+" - "+ErrorCode.ONE_ON_ONE_MONEY_NOT_ENOUGH.getErrorString());
		}

		if(course.getStudent().getStatus() != null && course.getStudent().getStatus() == StudentStatus.GRADUATION){
			String errMsg = "学生（"+course.getStudent().getName()+"）状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。";
			throw new ApplicationException(errMsg);
		}

		return cosumeMap;
	}

	public void checkCanChargeOneOnOneDetail(Contract contract, ContractProduct targetConPrd,
											 BigDecimal chargeMeney, BigDecimal auditedCourseHours, Course course) {
		BigDecimal remainingAmount = targetConPrd.getRemainingAmount();
		Organization belongCampus= userService.getBelongCampus();
		if(!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())  && !OrganizationType.BRENCH.equals(belongCampus.getOrgType())){
			throw new ApplicationException(ErrorCode.CAMPUS_ACCOUNT_ACHARGE_RECORDS);
		}
		// 只有 Normal 和 started 的值才可以 扣费
		if(remainingAmount.compareTo(chargeMeney)>=0 && (targetConPrd.getStatus() == ContractProductStatus.NORMAL ||targetConPrd.getStatus() == ContractProductStatus.STARTED )) {
		} else {
			throw new ApplicationException("本合同产品无法继续扣费");
		}
		ContractProductSubject cpSubject = contractProductSubjectService.findContractProductSubjectByCpIdAndSubjectId(targetConPrd.getId(), course.getSubject().getId());
		if (cpSubject != null) {
			if (cpSubject.getQuantity().subtract(cpSubject.getConsumeHours()).compareTo(auditedCourseHours) < 0) {
				throw new ApplicationException("本合同产品的" + course.getSubject().getName() + "科目对应的分配课时不足这次扣费");
			}
		} else {
			throw new ApplicationException("本合同产品的" + course.getSubject().getName() + "科目没有分配课时，无法扣费");
		}
	}

	private void checkContractProductSubjectHoursByAttendanceEditVo(OneOnOneBatchAttendanceEditVo attendanceEditVo) {
		Course course = null;
		Product product = null;
		String productGroupId = null;
		BigDecimal alreadyQuantities = BigDecimal.ZERO;
		BigDecimal confirmCourseHours = BigDecimal.ZERO;
		Map<String, BigDecimal> studentSubjectProductCourseMap = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> studentSubjectProductGroupCourseMap = new HashMap<String, BigDecimal>();
		if (StringUtil.isNotBlank(attendanceEditVo.getCourseId())) { // 学管单条考勤
			course = courseDao.findById(attendanceEditVo.getCourseId());
			if (course != null) {
				product = course.getProduct();
				productGroupId = product.getProductGroup() != null ? product.getProductGroup().getId() : null;
				alreadyQuantities = contractProductSubjectService
						.sumRemainHoursByStudentProductSubject(course.getStudent().getId(), productGroupId, product.getId(), course.getSubject().getId());
				confirmCourseHours = courseDao
						.sumConfrimCourseHoursByStudentProductSubject(course.getStudent().getId(), productGroupId, product.getId(), course.getSubject().getId());
				if (alreadyQuantities.subtract(
						(BigDecimal.valueOf(attendanceEditVo.getStaduyManagerAuditHours()))
						.add(confirmCourseHours)).compareTo(BigDecimal.ZERO) < 0) {
					throw new ApplicationException(
							"本合同产品的" + course.getSubject().getName() + "对应的分配课时不足这次考勤");
				}
			}
			return;

		}
		if (StringUtil.isNotBlank(attendanceEditVo.getIds())) { // 学管批量考勤
			String[] idArray = attendanceEditVo.getIds().split(",");
			for (String id : idArray) {
				course = courseDao.findById(id);
				if (course != null) {
					product = course.getProduct();
					productGroupId = product.getProductGroup() != null ? product.getProductGroup().getId() : null;
					if (productGroupId != null) {
						String student_subject_productGroup = course.getStudent().getId() + "_" + course.getSubject().getId() + "_" + productGroupId;
						if (!studentSubjectProductGroupCourseMap.containsKey(student_subject_productGroup)) {
							studentSubjectProductGroupCourseMap.put(student_subject_productGroup, course.getRealHours());
						} else {
							BigDecimal totalHours = studentSubjectProductGroupCourseMap.get(student_subject_productGroup);
							totalHours = totalHours.add(course.getRealHours());
							studentSubjectProductGroupCourseMap.put(student_subject_productGroup, totalHours);
						}
					} else {
						String student_subject_product = course.getStudent().getId() + "_" + course.getSubject().getId() + "_" + product.getId();
						if (!studentSubjectProductCourseMap.containsKey(student_subject_product)) {
							studentSubjectProductCourseMap.put(student_subject_product, course.getRealHours());
						} else {
							BigDecimal totalHours = studentSubjectProductCourseMap.get(student_subject_product);
							totalHours = totalHours.add(course.getRealHours());
							studentSubjectProductCourseMap.put(student_subject_product, totalHours);
						}
					}
				}
			}
			this.checkContractProductSubjectHoursByMaps(studentSubjectProductCourseMap, studentSubjectProductGroupCourseMap);
			return;
		}
		if (StringUtil.isNotBlank(attendanceEditVo.getTargetParams())) { //手机端
			String[] courseParamArray = attendanceEditVo.getTargetParams().split(",");
			if(courseParamArray.length > 0){
				for (String courseParam : courseParamArray) {
					String[] courseArray = courseParam.split("-",2);
					course = courseDao.findById(courseArray[0]);
					if (course != null) {
						product = course.getProduct();
						productGroupId = product.getProductGroup() != null ? product.getProductGroup().getId() : null;
						if (productGroupId != null) {
							String student_subject_productGroup = course.getStudent().getId() + "_" + course.getSubject().getId() + "_" + productGroupId;
							if (!studentSubjectProductGroupCourseMap.containsKey(student_subject_productGroup)) {
								studentSubjectProductGroupCourseMap.put(student_subject_productGroup, new BigDecimal(courseArray[1]));
							} else {
								BigDecimal totalHours = studentSubjectProductGroupCourseMap.get(student_subject_productGroup);
								totalHours = totalHours.add(new BigDecimal(courseArray[1]));
								studentSubjectProductGroupCourseMap.put(student_subject_productGroup, totalHours);
							}
						} else {
							String student_subject_product = course.getStudent().getId() + "_" + course.getSubject().getId() + "_" + product.getId();
							if (!studentSubjectProductCourseMap.containsKey(student_subject_product)) {
								studentSubjectProductCourseMap.put(student_subject_product, new BigDecimal(courseArray[1]));
							} else {
								BigDecimal totalHours = studentSubjectProductCourseMap.get(student_subject_product);
								totalHours = totalHours.add(new BigDecimal(courseArray[1]));
								studentSubjectProductCourseMap.put(student_subject_product, totalHours);
							}
						}
					}
				}
			}
			this.checkContractProductSubjectHoursByMaps(studentSubjectProductCourseMap, studentSubjectProductGroupCourseMap);
			return;
		}
	}

	private void checkContractProductSubjectHoursByMaps(Map<String, BigDecimal> studentSubjectProductCourseMap, Map<String, BigDecimal> studentSubjectProductGroupCourseMap) {
		String productGroupId = null;
		BigDecimal alreadyQuantities = BigDecimal.ZERO;
		BigDecimal confirmCourseHours = BigDecimal.ZERO;
		for (String student_subject_product : studentSubjectProductCourseMap.keySet()) {
			String[] tmpStrs = student_subject_product.split("_");
			String studentId = tmpStrs[0];
			String subjectId = tmpStrs[1];
			String productId = tmpStrs[2];
			BigDecimal selectedHours = studentSubjectProductCourseMap.get(student_subject_product);
			alreadyQuantities = contractProductSubjectService
					.sumRemainHoursByStudentProductSubject(studentId, null, productId, subjectId);
			confirmCourseHours = courseDao
					.sumConfrimCourseHoursByStudentProductSubject(studentId, null, productId, subjectId);
			if (alreadyQuantities.subtract((selectedHours).add(confirmCourseHours)).compareTo(BigDecimal.ZERO) < 0) {
				String subjectName = dataDictDao.findById(subjectId).getName();
				throw new ApplicationException(
						"本合同产品的" + subjectName + "对应的分配课时不足这次考勤");
			}
		}
		for (String student_subject_productGroup : studentSubjectProductGroupCourseMap.keySet()) {
			String[] tmpStrs = student_subject_productGroup.split("_");
			String studentId = tmpStrs[0];
			String subjectId = tmpStrs[1];
			productGroupId = tmpStrs[2];
			BigDecimal selectedHours = studentSubjectProductGroupCourseMap.get(student_subject_productGroup);
			alreadyQuantities = contractProductSubjectService
					.sumRemainHoursByStudentProductSubject(studentId, productGroupId, null, subjectId);
			confirmCourseHours = courseDao
					.sumConfrimCourseHoursByStudentProductSubject(studentId, productGroupId, null, subjectId);
			if (alreadyQuantities.subtract((selectedHours).add(confirmCourseHours)).compareTo(BigDecimal.ZERO) < 0) {
				String subjectName = dataDictDao.findById(subjectId).getName();
				throw new ApplicationException(
						"本合同产品的" + subjectName + "对应的分配课时不足这次考勤");
			}
		}
	}

	@Override
	public List getMiniClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String auditStatus) {
		List list =courseDao.getMiniClassCourseAuditSalaryNums(vo, auditStatus);
		return list;
	}

	/**
	 * 查询学生帐户情况, 除电子账户外，其他均实时计算
	 *
	 * @param studentId
	 * @return
	 */
	@Override
	public StudnetAccMv getStudentAccoutInfo(String studentId) {
		StudnetAccMv account = studnetAccMvDao
				.getStudnetAccMvByStudentId(studentId);
		if (account == null) {
			account = new StudnetAccMv();
		}
		// StudnetAccMvVo accVo = HibernateUtils.voObjectMapping(account,
		// StudnetAccMvVo.class);

		// 查询所有有效合同产品
		List<Criterion> produceCriterionList = new ArrayList<Criterion>();
		produceCriterionList.add(Restrictions.eq("contract.student.id",
				studentId));
		// produceCriterionList.add(Expression.in("status", new
		// ContractProductStatus[]{ContractProductStatus.NORMAL,
		// ContractProductStatus.STARTED}));
		List<ContractProduct> allContractProduces = contractProductDao
				.findAllByCriteria(produceCriterionList);

		BigDecimal totalAmount = BigDecimal.ZERO;
		BigDecimal paidAmount = BigDecimal.ZERO;
		BigDecimal promotinAmount = BigDecimal.ZERO;
		BigDecimal consumeAmount = BigDecimal.ZERO;
		BigDecimal remainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingAmount = BigDecimal.ZERO;
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		BigDecimal oneOnOnePromotinAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingAmount = BigDecimal.ZERO;
		BigDecimal miniRemainingHour = BigDecimal.ZERO;
		BigDecimal otherConsumeAmount = BigDecimal.ZERO;
//		BigDecimal ecsRemainingAmount = BigDecimal.ZERO;
		if (allContractProduces.size() > 0) {
			for (ContractProduct cp : allContractProduces) {
				if (cp.getPromotionAmount() == null)
					cp.setPromotionAmount(BigDecimal.ZERO);
				// paidAmount = paidAmount.add(cp.getPaidAmount());
				consumeAmount = consumeAmount.add(cp.getConsumeAmount());
				remainingAmount = remainingAmount.add(cp.getRemainingAmount());
				promotinAmount = promotinAmount.add(cp.getPromotionAmount());
				// totalAmount = totalAmount.add(cp.getPlanAmount());
				switch (cp.getType()) {
				case ONE_ON_ONE_COURSE:
					oneOnOneRemainingAmount = oneOnOneRemainingAmount.add(cp
							.getRemainingAmount());
					if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
						oneOnOneRemainingHour = oneOnOneRemainingHour.add(cp
								.getRemainingAmount().divide(cp.getPrice(), 2,
										RoundingMode.HALF_UP));
					}
					oneOnOnePromotinAmount = oneOnOnePromotinAmount.add(cp
							.getPromotionAmount());
					break;
				case SMALL_CLASS:
					miniRemainingAmount = miniRemainingAmount.add(cp
							.getRemainingAmount());
					if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
						miniRemainingHour = miniRemainingHour.add(cp
								.getRemainingAmount().divide(cp.getPrice(), 2,
										RoundingMode.HALF_UP));
					}
					break;
//				case ECS_CLASS:
//					ecsRemainingAmount = ecsRemainingAmount.add(cp
//							.getRemainingAmount());
//					break;
				case OTHERS:
					otherConsumeAmount = otherConsumeAmount.add(cp
							.getRemainingAmount());
					break;
				}
			}
			// totalAmount = totalAmount.add(paidAmount).add(promotinAmount);

			// accVo.setTotalAmount(totalAmount);
			// accVo.setPaidAmount(paidAmount);
			account.setRemainingAmount(remainingAmount);
			account.setOneOnOneRemainingAmount(oneOnOneRemainingAmount);
			account.setOneOnOneRemainingHour(oneOnOneRemainingHour);
			account.setMiniRemainingAmount(miniRemainingAmount);
			// account.setMiniRemainingHour(miniRemainingHour);
			account.setOtherConsumeAmount(otherConsumeAmount);
			// account.setEcsRemainingAmount(ecsRemainingAmount);
			// account.setPromotinAmount(promotinAmount);
		}else{
			account.setRemainingAmount(BigDecimal.ZERO);
			account.setOneOnOneRemainingAmount(BigDecimal.ZERO);
			account.setOneOnOneRemainingHour(BigDecimal.ZERO);
			account.setMiniRemainingAmount(BigDecimal.ZERO);
			account.setOtherConsumeAmount(BigDecimal.ZERO);
		}

		// 统计待付款
		List<Criterion> contractCriterionList = new ArrayList<Criterion>();
		contractCriterionList.add(Restrictions.eq("student.id", studentId));
		List<Contract> contarctList = contractDao
				.findAllByCriteria(contractCriterionList);
		BigDecimal totalPaddingAmount = BigDecimal.ZERO;
		BigDecimal availableAmount = BigDecimal.ZERO;
		for (Contract contract : contarctList) {
			contractService.calculateContractDomain(contract);
			if (contract.getPendingAmount().compareTo(BigDecimal.ZERO) > 0) {
				totalPaddingAmount = totalPaddingAmount.add(contract
						.getPendingAmount());
			}
			totalAmount = totalAmount.add(contract.getTotalAmount());
			paidAmount = paidAmount.add(contract.getPaidAmount());
		}
		account.setPaidAmount(paidAmount);
		account.setTotalAmount(totalAmount);
		// account.setAvailableAmount(availableAmount);
		// account.setArrangedAmount(totalPaddingAmount.subtract(availableAmount));
		// account.setPlanAmount(totalPaddingAmount);
		studnetAccMvDao.save(account);
		return account;
	}

	@Override
	public void updateCourseStatus(String courseId, CourseStatus courseStatus) {
		Course course = courseDao.findById(courseId);
		if (course != null) {
			if (CourseStatus.STUDENT_LEAVE.equals(courseStatus)) {
				course.setRealHours(null);
				// 删除冲突表当前冲突记录
				courseConflictDao.deleteCourseConflictByCourseIdAndStudentId(
						courseId, course.getStudent().getId());
			} else if (CourseStatus.TEACHER_LEAVE.equals(courseStatus)) {
				course.setRealHours(null);
				// 删除冲突表当前冲突记录
				courseConflictDao.deleteCourseConflictByCourseIdAndStudentId(
						courseId, course.getStudent().getId());
			}
			course.setCourseStatus(courseStatus);
			courseDao.save(course);
		}
	}

	/**
	 * 批量考勤提交
	 *
	 * @param vos
	 */
	@Override
	public void mutilAttendaceSubmit(List<OneOnOneBatchAttendanceEditVo> vos)
			throws Exception {
		if (vos != null && vos.size() > 0) {
			if ("staduyManagerAudit".equals(vos.get(0).getOperteType())) {
				this.checkStudyManageAttendaceSubmit(vos);
			}
			for (OneOnOneBatchAttendanceEditVo vo : vos) {
				oneOnOneBatchAttendanceEdit(vo);
			}
			try{
				courseDao.commit();
			}catch(StaleObjectStateException e){//捕获hibernate乐观锁异常
				throw new ApplicationException("有课程已经被修改，请刷新后再操作！");
			}

		} else {
			throw new ApplicationException("提交的考勤数据有误!");
		}
	}

	/**
	 * 一对一审核提交
	 */
	public void oneOnOneAuditSubmit(String courseId, String auditStatus) {
		Course course = courseDao.findById(courseId);
		course.setAuditStatus(AuditStatus.valueOf(auditStatus));
		courseDao.save(course);
	}

	private void checkStudyManageAttendaceSubmit(String[] courseParamArray) {
		List<OneOnOneBatchAttendanceEditVo> vos=new ArrayList<OneOnOneBatchAttendanceEditVo>();
		for (String courseParam : courseParamArray) {
			OneOnOneBatchAttendanceEditVo vo=new OneOnOneBatchAttendanceEditVo();
			String[] courseArray = courseParam.split("-",2);
			String id = courseArray[0];
			vo.setStaduyManagerAuditHours(new Double(courseArray[1]));
			vo.setCourseId(id);
			vos.add(vo);
		}
		this.checkStudyManageAttendaceSubmit(vos);

	}

	private void checkStudyManageAttendaceSubmit(
			List<OneOnOneBatchAttendanceEditVo> vos) {
		Map<String, BigDecimal> studentCourseMap = new HashMap<String, BigDecimal>();
		for (OneOnOneBatchAttendanceEditVo vo : vos) {
			Course course = courseDao.findById(vo.getCourseId());
			if(course==null){
				throw new ApplicationException("课程ID“"+vo.getCourseId()+"”找不到对应的课程信息，请刷新后重试！");
			}
			// BigDecimal realHours = course.getRealHours();
			BigDecimal realHours = BigDecimal.ZERO;
			if (vo.getStaduyManagerAuditHours() > 0) {
				realHours = new BigDecimal(vo.getStaduyManagerAuditHours());
			} else {
				realHours = course.getRealHours();
			}
			String studentId = course.getStudent().getId();
			if (!studentCourseMap.containsKey(studentId)) {
				studentCourseMap.put(studentId, realHours);
			} else {
				BigDecimal totalHours = studentCourseMap.get(studentId);
				totalHours = totalHours.add(realHours);
				studentCourseMap.put(studentId, totalHours);
			}
		}
		DataPackage dp = new DataPackage();
		dp.setPageSize(99);
		for (String studentId : studentCourseMap.keySet()) {
			BigDecimal selectedHours = studentCourseMap.get(studentId);
			BigDecimal totalHours = selectedHours;
			BigDecimal studyAuditHours = BigDecimal.ZERO;
			CourseSearchInputVo searchVo = new CourseSearchInputVo();
			searchVo.setStudentId(studentId);
			searchVo.setCourseStatus(CourseStatus.STUDY_MANAGER_AUDITED);
			dp = courseDao.getStudentCourseScheduleList(searchVo, dp);
			List<Course> courseList = (List<Course>) dp.getDatas();
			for (Course course : courseList) {
				studyAuditHours = studyAuditHours.add(course.getRealHours());
			}
			selectedHours = selectedHours.setScale(2, BigDecimal.ROUND_HALF_UP);
			totalHours = selectedHours.add(studyAuditHours);
			totalHours = totalHours.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal oneOnOneRemainingHour = this
					.getStudentOneOnOneRemainingHour(studentId);
			Student student = studentDao.findById(studentId);
			if (oneOnOneRemainingHour.compareTo(totalHours) < 0) {
				String studentName = student.getName();
				String errMsg = "学生" + studentName + "的剩余课时为"
						+ oneOnOneRemainingHour + "，已有学管已确认课时"
						+ studyAuditHours + "，本次需确认课时总数为" + selectedHours
						+ "，剩余课时不足以抵扣课消，请续费后再尝试确认。";
				throw new ApplicationException(errMsg);

			}
			if(student.getStatus() != null && student.getStatus()==StudentStatus.GRADUATION){
				String errMsg = "学生（"+student.getName()+"）状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。";
				throw new ApplicationException(errMsg);
			}

		}
	}

	private BigDecimal getStudentOneOnOneRemainingHour(String studentId) {
		// 查询所有有效合同产品
		List<Criterion> produceCriterionList = new ArrayList<Criterion>();
		produceCriterionList.add(Restrictions.eq("contract.student.id",
				studentId));
		List<ContractProduct> allContractProduces = contractProductDao
				.findAllByCriteria(produceCriterionList);
		BigDecimal oneOnOneRemainingHour = BigDecimal.ZERO;
		if (allContractProduces.size() > 0) {
			for (ContractProduct cp : allContractProduces) {
				if (com.eduboss.common.ProductType.ONE_ON_ONE_COURSE == cp
						.getType()) {
					if (BigDecimal.ZERO.compareTo(cp.getPrice()) != 0) {
						oneOnOneRemainingHour = oneOnOneRemainingHour.add(cp
								.getRemainingAmount().divide(cp.getPrice(), 2,
										RoundingMode.HALF_UP));
					}
				}
			}
		}
		return oneOnOneRemainingHour;
	}

	/**
	 * 提交课程考勤
	 *
	 * @throws Exception
	 */
	@Override
	public void submitCourseAttendance(String courseId, String courseHours,
			String courseTime, User modifyUser, RoleCode roleCode)
			throws Exception {
		Course course = courseDao.findById(courseId);
		if (course == null) {
			throw new ApplicationException(ErrorCode.COURSE_NOT_FOUND);
		}
		boolean isAttendanceSuccess = false;
		if (RoleCode.TEATCHER == roleCode) {// 老师
			if (course.getTeacher() == null
					|| !course.getTeacher().getUserId()
							.equalsIgnoreCase(modifyUser.getUserId())) {
				throw new ApplicationException("老师只能考勤自己的课程");
			}

			if (new Date().before(new SimpleDateFormat("yyyy-MM-dd")
					.parse(course.getCourseDate()))) { // 当前时间在上课日期之前
				throw new ApplicationException("上课日期之前不允许考勤");
			}

			if (course.getCourseStatus() != CourseStatus.STUDY_MANAGER_AUDITED
			// && course.getCourseStatus() !=
			// CourseStatus.TEACHING_MANAGER_AUDITED
					&& course.getCourseStatus() != CourseStatus.CHARGED) {// 如果状态为“学管已核对”或者“教务已核对”或者“已结算”，就不允许确认课时

				//当老师考勤时修改课程时间导致课程冲突了，要先更新冲突表，再检查冲突，如果冲突就不能考勤。
				if(course.getPlanHours().compareTo(new BigDecimal(courseHours))!=0 || !course.getCourseTime().equals(courseTime)){
					course.setRealHours(new BigDecimal(courseHours));
					if (null != courseTime) {
						course.setCourseTime(courseTime);
					}
					courseDao.save(course);
					courseDao.flush();
					int countDistinctConflicts = courseConflictDao
							.countDistinctConflicts(course.getCourseId(), course
									.getStudent().getId(), course.getTeacher()
									.getUserId(), course.getCourseDate(), course
									.getCourseTime());
					if (countDistinctConflicts > 0) {
						throw new ApplicationException("考勤失败：课程编号"
								+ courseId
								+ "修改课程时间导致课程冲突!");
					}
				}else{
					course.setRealHours(new BigDecimal(courseHours));
					if (null != courseTime) {
						course.setCourseTime(courseTime);
					}
				}



				course.setCourseStatus(CourseStatus.TEACHER_ATTENDANCE);// 老师已考勤
				course.setTeachingAttendTime(DateTools.getCurrentDateTime());




				isAttendanceSuccess = true;

				// 推送到手机端 add by Yao 2015-05-06
				// 系统应该要配置
				// String
				// value=systemConfigService.getSystemConfigValueByTag("attendanceIsPush");//得到系统是否需要推送信息
//				String value = "0"; 停用推送，2016-05-13   Yao
//				if (StringUtils.isNotEmpty(value) && "0".equals(value)) {
//					List<String> arrayOfUserId = new ArrayList<String>();
//					List<String> arrayOfStuId = new ArrayList<String>();
//					arrayOfUserId.add(course.getStudyManager().getUserId());
//					arrayOfUserId.add(course.getTeacher().getUserId());
//					arrayOfStuId.add(course.getStudent().getId());
//					mobilePushMsgService.pushRemindToUserAndStudentIds(
//							arrayOfUserId, arrayOfStuId,
//							modifyUser.getUserId(), "学生"
//									+ course.getStudent().getName() + "  "
//									+ course.getCourseDate() + " " + "的课程"
//									+ course.getSubject().getName() + "已考勤。");
//				}
				// 推送到手机端结束
				// 增加学生动态的记录项 开始
				studentDynamicStatusService.createCourseDynamicStatus(course,
						"TEACHER_ATTENDANCE");
				// 增加学生动态的记录项 结束

				// 老师考勤成功后，通知学管确认课时
				// if(course.getStudent().getStudyManeger() != null) {
				// messageService.sendMessage(MessageType.SYSTEM_MSG, "一对一考勤提醒",
				// "学生"+course.getStudent().getName()+"已上课并由老师"+course.getTeacher().getName()+"提交考勤，请及时确认。",
				// MessageDeliverType.SINGLE,
				// course.getStudent().getStudyManeger().getUserId());
				// }
			} else {
				throw new ApplicationException("当前课程的状态为："
						+ course.getCourseStatus().getName() + "，老师不能考勤！");
			}

		} else if (RoleCode.STUDY_MANAGER == roleCode) {// 学管师
			// 合同产品冻结 不能操作学管确认
			List<ContractProduct> oneOnOneProductList = contractService.getOrderValidOneOnOneContractProducts(course);
			//#5435 学生在走退费流程，1对1课程考勤对应的产品问题 （只要有一份没有冻结的合同产品就跳过）
			boolean allContractProductFrozen = true;
			for (ContractProduct cp : oneOnOneProductList) {
				if (cp.getIsFrozen() == 1) {
					allContractProductFrozen = false;
					break;
				}
			}
			if (oneOnOneProductList.size()>0 && allContractProductFrozen){
				throw new ApplicationException("考勤课程" + courseId + "的合同产品已经冻结，不能学管确认！");
			}

			if (CourseStatus.TEACHER_ATTENDANCE == course.getCourseStatus()
					|| CourseStatus.STUDY_MANAGER_AUDITED == course
							.getCourseStatus()) {// 课程状态为“老师已考勤”或“学管已确认”时才允许学管确认课时
				course.setAuditHours(new BigDecimal(courseHours));// 更新课程表中的核对课时
				if (null != courseTime) {
					course.setCourseTime(courseTime);
					course.setRealHours(new BigDecimal(courseHours));
				}
				course.setCourseStatus(CourseStatus.STUDY_MANAGER_AUDITED);// 学管已核对
				course.setStudyManagerAuditTime(DateTools.getCurrentDateTime());
				course.setStudyManagerAuditId(modifyUser.getUserId());
				isAttendanceSuccess = true;

				// 推送到手机端 add by Yao 2015-05-06
				// String
				// value=systemConfigService.getSystemConfigValueByTag("attendanceIsPush");//得到系统是否需要推送信息
//				String value = "0"; 停用推送，2016-05-13   Yao
//				if (StringUtils.isNotEmpty(value) && "0".equals(value)) {
//					List<String> arrayOfUserId = new ArrayList<String>();
//					List<String> arrayOfStuId = new ArrayList<String>();
//					arrayOfUserId.add(course.getStudyManager().getUserId());
//					arrayOfUserId.add(course.getTeacher().getUserId());
//					arrayOfStuId.add(course.getStudent().getId());
//					mobilePushMsgService.pushRemindToUserAndStudentIds(
//							arrayOfUserId, arrayOfStuId,
//							modifyUser.getUserId(), "学生"
//									+ course.getStudent().getName() + "  "
//									+ course.getCourseDate() + " " + "的课程"
//									+ course.getSubject().getName()
//									+ "提交考勤，请及时确认扣费。");
//				}
				// 推送到手机端结束

				// 增加学生动态的记录项 开始
				// studentDynamicStatusService.createCourseDynamicStatus(course,
				// "TEACHER_ATTENDANCE");
				// 增加学生动态的记录项 结束

				// 学管核对成功后，通知教务确认扣费
				// messageService.sendMessage(MessageType.SYSTEM_MSG, "一对一考勤提醒",
				// "学生"+course.getStudent().getName()+"上课并由学管"+course.getStudent().getStudyManeger().getName()+"提交考勤，请及时确认扣费。",
				// MessageDeliverType.SINGLE,
				// course.getCreateUser().getUserId());
			} else {
				throw new ApplicationException("课程状态为“老师已考勤时”才允许学管确认课时");
			}
		}

		if (isAttendanceSuccess) {
			course.setModifyTime(DateTools.getCurrentDateTime());
			course.setModifyUser(modifyUser);

			String currDateTime = DateTools.getCurrentDateTime();
			CourseAttendanceRecord studyRecord = new CourseAttendanceRecord();
			if (null == studyRecord.getCheckUserRole()) {// 角色
				List<Role> roles = roleDao.findRoleByCode(roleCode);
				if (roles.size() > 0) {
					Role role = roles.get(0);
					studyRecord.setCheckUserRole(role);
				}
			}

			studyRecord.setCourseHours(new BigDecimal(courseHours));
			studyRecord.setCourse(course);
			studyRecord.setCheckUser(modifyUser);
			studyRecord.setOprateTime(currDateTime);

			// 保存考勤记录
			courseAttendanceRecordDao.save(studyRecord);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public DataPackage getAttendanceRecordVos(CourseAttendanceRecordVo record,
			DataPackage dataPackage) {
		List<Criterion> criteriaList = new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(record.getCheckUserId())) {
			criteriaList.add(Restrictions.eq("checkUser.userId",
					record.getCheckUserId()));
		}
		dataPackage = courseAttendanceRecordDao.findPageByCriteria(dataPackage,
				HibernateUtils.prepareOrder(dataPackage, "oprateTime", "desc"),
				criteriaList);
		dataPackage.setDatas(HibernateUtils.voListMapping(
				(List) dataPackage.getDatas(), CourseAttendanceRecordVo.class));
		return dataPackage;
	}

	@Override
	public void submitCourseAudit(String courseId, BigDecimal auditCourseHours,
			User modifyUser) {

		Course course = courseDao.findById(courseId);
		ValidationUtil.checkObjectNullWithException(course,
				new ApplicationException(ErrorCode.COURSE_NOT_FOUND));

		course.setAuditHours(auditCourseHours);
		course.setModifyTime(DateTools.getCurrentDateTime());
		course.setModifyUser(modifyUser);

		// 保存考勤记录
		courseAttendanceRecordDao.save(new CourseAttendanceRecord(null,
				new Course(courseId), auditCourseHours, modifyUser, null,
				DateTools.getCurrentDateTime()));

		// 根据角色更新状态
		if (RoleCode.STUDY_MANAGER.equals(modifyUser.getRoleCode())) {
			course.setCourseStatus(CourseStatus.STUDY_MANAGER_AUDITED);
		}
		// else if (RoleCode.EDUCAT_SPEC.equals(modifyUser.getRoleCode())) {
		// course.setCourseStatus(CourseStatus.TEACHING_MANAGER_AUDITED);
		// }
	}

	@Override
	public void chargeCourse(String courseId, User modifyUser) {
		Course course = courseDao.findById(courseId);
		ValidationUtil.checkObjectNullWithException(course,
				new ApplicationException(ErrorCode.COURSE_NOT_FOUND));

		// if
		// (!CourseStatus.TEACHING_MANAGER_AUDITED.equals(course.getCourseStatus())
		// || course.getAuditHours() == null) {
		// throw new ApplicationException(ErrorCode.COURSE_NOT_AUDITED);
		// }

		// 修改课程状态
		course.setCourseStatus(CourseStatus.CHARGED);

		// 扣费
		chargeService.chargeOneOnOneCourse(courseId, modifyUser);
	}

	@Override
	public void saveCourseList(Course[] courseList,
			CourseSummarySearchResultVo courseSummaryVo) {
		User user = userService.getCurrentLoginUser();
		CourseSummary summary = HibernateUtils.voObjectMapping(courseSummaryVo,
				CourseSummary.class);
		if (courseSummaryVo != null && courseSummaryVo.getStudentId() != null
				&& courseSummaryVo.getTeacherId() != null) {
			List<Criterion> criterions = new ArrayList<Criterion>();
			criterions.add(Restrictions.eq("delFlag", 0));
			criterions.add(Restrictions.eq("subject", summary.getSubject()));
			criterions.add(Restrictions.eq("student.id", summary.getStudent()
					.getId()));
			criterions
					.add(Restrictions.eq("startDate", summary.getStartDate()));
			criterions.add(Restrictions.eq("endDate", summary.getEndDate()));
			criterions.add(Restrictions.eq("courseTime",
					summary.getCourseTime()));
			criterions.add(Restrictions.sqlRestriction("plan_hours = ?",
					summary.getPlanHours(), Hibernate.DOUBLE));
			criterions.add(Restrictions.eq("teacher.userId", summary
					.getTeacher().getUserId()));
			if (summary.getProduct() != null
					&& StringUtils.isNotBlank(summary.getProduct().getId())) {
				criterions.add(Restrictions.ne("product.id", summary
						.getProduct().getId()));
			}
			if (StringUtils.isNotBlank(summary.getCourseSummaryId())) {
				criterions.add(Restrictions.ne("courseSummaryId",
						summary.getCourseSummaryId()));
			}
			if (summary.getWeekInterval() == null) {
				criterions.add(Restrictions.isNull("weekInterval"));
			} else {
				criterions.add(Restrictions.eq("weekInterval",
						summary.getWeekInterval()));
			}
			if (summary.getWeekDay() == null) {
				criterions.add(Restrictions.isNull("weekDay"));
			} else {
				criterions
						.add(Restrictions.eq("weekDay", summary.getWeekDay()));
			}

			if (courseSummaryDao.findCountByCriteria(criterions) > 0) {
				throw new ApplicationException("请不要重复排课");
			}
		}
		// 创建课程概要
		boolean isModifyCourseSummary = StringUtils.isNotBlank(courseSummaryVo
				.getCourseSummaryId());
		if (courseSummaryVo.getCourseSummaryId() == null) {
			if (courseSummaryVo != null
					&& courseSummaryVo.getStudentId() != null
					&& courseSummaryVo.getTeacherId() != null) {

				// 校区大课表加入排课专员和学管师
				User teachingManager = userService.getCurrentLoginUser();
				summary.setTeachingManager(teachingManager);
				if (StringUtils.isNotBlank(summary.getStudent().getId())) {
					Student student = studentDao.findById(summary.getStudent()
							.getId());
					if (student != null && student.getStudyManeger() != null) {
						summary.setStudyManager(student.getStudyManeger());
					}
					if (student != null && student.getGradeDict() != null) {
						summary.setGrade(student.getGradeDict());
					}
				}
			}
		} else { // 修改课程概要
			summary = courseSummaryDao.findById(courseSummaryVo
					.getCourseSummaryId());
			CourseSummary summaryFormVo = HibernateUtils.voObjectMapping(
					courseSummaryVo, CourseSummary.class);
			summary.setProduct(summaryFormVo.getProduct());
			summary.setStudent(summaryFormVo.getStudent());
			summary.setSubject(summaryFormVo.getSubject());
			summary.setStartDate(summaryFormVo.getStartDate());
			summary.setEndDate(summaryFormVo.getEndDate());
			summary.setCourseTime(summaryFormVo.getCourseTime());
			summary.setCourseMinutes(summaryFormVo.getCourseMinutes());
			summary.setTeacher(summaryFormVo.getTeacher());
			summary.setPlanHours(summaryFormVo.getPlanHours());
			summary.setSpanHours(summaryFormVo.getSpanHours());
			summary.setWeekInterval(summaryFormVo.getWeekInterval());
			summary.setWeekDay(summaryFormVo.getWeekDay());
			// 修改概要需要删除该概要下所有未考勤的课程
			List<Course> courses = courseDao.findByCriteria(
					Restrictions.eq("courseSummary.id",
							courseSummaryVo.getCourseSummaryId()),
					Restrictions.eq("courseStatus", CourseStatus.NEW));

			courseDao.deleteAll(courses);
		}
		if (summary != null && summary.getStudent() != null
				&& summary.getTeacher() != null) {
			courseSummaryDao.save(summary);
		} else {
			summary = null;
		}

		// 保存课程
		if (courseList != null && courseList.length > 0) {
			Organization belongCampus = userService.getBelongCampus();
			if (!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())) {
				throw new ApplicationException("当前用户的归属组织架构只有校区下的才能提排课需求");
			}
			for (Course course : courseList) {
				// 修改大课表重新生成课程时跳过今天之前且状态为非“未上课”的课程
				if (isModifyCourseSummary
						&& DateTools.getDate(course.getCourseDate()).before(DateTools.getDate(DateTools.getCurrentDate()))
						&& !CourseStatus.NEW.equals(course.getCourseStatus())) {
					continue;
				} else if(isModifyCourseSummary && course.getCourseDate().equals(DateTools.getCurrentDate())){//如果是今天已经考勤过了的课程也不能修改，今天以后的暂时不考虑
					List<Course> courses = courseDao.findByCriteria(
							Restrictions.eq("courseSummary.id",courseSummaryVo.getCourseSummaryId()),
							Restrictions.eq("courseDate",DateTools.getCurrentDate()),
							Restrictions.ne("courseStatus", CourseStatus.NEW));
					if(courses.size()>0)continue;
				}
				//营收凭证如果已经审批完成就不能再排以前的课程了。
				try{
					if(DateTools.getDateSpace(DateTools.getFistDayofMonth(),course.getCourseDate())<0){
						odsMonthIncomeCampusService.isFinishAudit(belongCampus.getId(),DateTools.getDateToString(DateTools.getLastDayOfMonth(course.getCourseDate())));
					}
				}catch(ParseException e){
					e.printStackTrace();
				}


				if (course.getStudent() != null
						&& StringUtils.isNotBlank(course.getStudent().getId())) {
					Student student = studentDao.findById(course.getStudent()
							.getId());
					if (course.getGrade() == null && student != null && student.getGradeDict() != null) {
						course.setGrade(student.getGradeDict());
					}
					if (student != null && student.getId() != null) {
						// course.setStudyManager(student.getStudyManeger());
						course.setStudyManager(studentOrganizationDao
								.findStudentOrganizationByStudentAndOrganization(
										course.getStudent().getId(),
										belongCampus.getId()));
					}
				}
				course.setCreateTime(DateTools.getCurrentDateTime());
				course.setCreateUser(new User(user.getUserId()));
				course.setCourseStatus(CourseStatus.NEW);
				course.setAuditStatus(AuditStatus.UNAUDIT);
				course.setCourseSummary(summary);
				course.setBlCampusId(belongCampus);
				if (course.getStudyManager() == null) {
					throw new ApplicationException(
							ErrorCode.NOT_FIND_STUDY_MANAGER);
				}
				courseDao.save(course);
			}
		}

		// 更新排课需求状态
		if (StringUtils.isNotBlank(courseSummaryVo.getCourseRequeirmentId())) {
			CourseRequirement req = courseRequirementDao
					.findById(courseSummaryVo.getCourseRequeirmentId());
			if (req != null) {
				req.setRequirementStatus(CourseRequirementStatus.ARRENGED);
				req.setModifyTime(DateTools.getCurrentDateTime());
				req.setModifyUser(userService.getCurrentLoginUser());
			}
		}
	}

	@Override
	public void saveMutilCourseList(MutilCourseVo[] mutilCourseVoList,
			CourseSummarySearchResultVo[] courseSummaryVoList, String transactionUuid) {
		if (com.eduboss.utils.StringUtil.isNotBlank(transactionUuid)) {
			transactionRecordDao.saveTransactionRecord(transactionUuid);
		}
		Map<String, CourseSummarySearchResultVo> courseSummaryVoMap = new HashMap<String, CourseSummarySearchResultVo>();
		for (CourseSummarySearchResultVo courseSummaryVo : courseSummaryVoList) {
			courseSummaryVoMap.put(courseSummaryVo.getWeekDay(),
					courseSummaryVo);
		}
		for (MutilCourseVo mcv : mutilCourseVoList) {
			this.saveCourseList(mcv.getCourseArr(),
					courseSummaryVoMap.get(mcv.getWeekDay()));
		}
	}

	@Override
	public void saveMiniCourseList(MiniClassCourse[] miniClassCourseList, String arrangedHours) {
		User user = userService.getCurrentLoginUser();
		String miniclassId=null;
		for (MiniClassCourse miniClassCourse : miniClassCourseList) {
		try{
			if(DateTools.getDateSpace(DateTools.getFistDayofMonth(),miniClassCourse.getCourseDate())<0){
				MiniClass mc=miniClassDao.findById(miniClassCourse.getMiniClass().getMiniClassId());
				odsMonthIncomeCampusService.isFinishAudit(mc.getBlCampus().getId(), DateTools.getDateToString(DateTools.getLastDayOfMonth(miniClassCourse.getCourseDate())));
			}
		}catch(ParseException e){
			e.printStackTrace();
		}

			miniClassCourse.setCreateTime(DateTools.getCurrentDateTime());
			miniClassCourse.setCreateUserId(user.getUserId());
			miniClassCourse.setModifyTime(DateTools.getCurrentDateTime());
			miniClassCourse.setModifyUserId(user.getUserId());
			miniClassCourse.setCourseStatus(CourseStatus.NEW);
			if (null == miniClassCourse.getGrade()
					|| null == miniClassCourse.getGrade().getId()
					|| "".equals(miniClassCourse.getGrade().getId())) {
				miniClassCourse.setGrade(null);
			}
			if(miniclassId == null ){
				miniclassId=miniClassCourse.getMiniClass().getMiniClassId();
			}
			miniClassCourse.setAuditStatus(AuditStatus.UNAUDIT);
			if ("".equals(miniClassCourse.getTeacher().getUserId())){
				miniClassCourse.setTeacher(null);
			}

			miniClassCourseDao.save(miniClassCourse);
			log.info(miniClassCourse.getMiniClassCourseId()+",状态："+miniClassCourse.getCourseStatus());
			smallClassService.initMiniClassStudentAttendent(miniClassCourse);

		}
		MiniClass miniClass=miniClassDao.findById(miniclassId);
		miniClass.setArrangedHours(Double.valueOf(arrangedHours));
		if(miniClass.getIsModal()==0) {
			miniClass.setIsModal(1);
		}
		smallClassService.updateCourseNumByClassId(miniclassId);//更新排序
		miniClassCourseDao.flush();
		smallClassService.updateMiniClassMaxMinCourseDate(miniClass);

	}
	@Deprecated
	@Override
	public DataPackage readCourseListByAttendanceType(
			AttendanceType attendanceType, String number, String date)
			throws Exception {
		Student student = null;
		// 是否半日考勤halfDayAttendance 是否一对一考勤
		// 是否小班考勤isMiniClassCourse
		// 是否一对一考勤 isOneOnoneCourse
		// 是否指纹考勤 isFingerprint
		// 是否学生卡考勤 isStudentCard
		Organization organization = userService.getBelongGroup();
		SystemConfig systemConfig = new SystemConfig();
		systemConfig.setTag("classHour");
		systemConfig.setGroup(organization);
		List<SystemConfigVo> list = systemConfigService
				.getSystemConfigList(systemConfig);
		if (list.size() == 0) {
			throw new ApplicationException("请到系统配置页面设置考勤类型");
		}
		SystemConfigVo s = list.get(0);
		String courseAttandence = s.getValue();

		boolean halfDayflag = false;
		boolean isOneOnoneCourse = false;
		boolean isMiniClassCourse = false;
		boolean isFingerprint = false;
		boolean isStudentCard = false;
		if (courseAttandence.indexOf("4") != -1
				|| attendanceType.equals(AttendanceType.SYSTEM)) {
			// 全天考勤
			halfDayflag = false;
		} else {
			// 半天考勤
			halfDayflag = true;
		}

		if (courseAttandence.indexOf("0") != -1
				|| courseAttandence.indexOf("1") != -1) {
			// 学生卡考勤
			isStudentCard = true;
		}

		if (courseAttandence.indexOf("2") != -1
				|| courseAttandence.indexOf("3") != -1) {
			// 指纹考勤
			isFingerprint = true;
		}

		DataPackage result = new DataPackage();
		result.setDatas(new ArrayList<CourseVo>());
		if (AttendanceType.FINGERPRINT == attendanceType
				|| AttendanceType.SYSTEM == attendanceType) {
			if (isFingerprint) {
				if (number.startsWith("STU")) {// 如果是以stu开头的，说明是指纹采集机的指纹考勤，直接提交学生id
					student = studentDao.findById(number);
				} else {
					student = studentDao.findByAttendanceNo(number,
							userService.getBelongCampus());
				}
			}else{
				student = studentDao.findById(number);
			}
		} else if (AttendanceType.IC_CARD == attendanceType) {
			if (isStudentCard) {
				student = studentDao.findByIcCardNo(number);
			}
		} else {
			throw new ApplicationException("找不到匹配的考勤类型，请检查！");
		}

		if (student != null) {
			boolean attendentStatu=false;//考勤状态
			DataPackage dp = new DataPackage();
			// 一对一考勤isOneOnoneCourse
			if (courseAttandence.indexOf("0") != -1
					|| courseAttandence.indexOf("2") != -1) {
				isOneOnoneCourse = true;
			}
			if (isOneOnoneCourse) {
				dp = courseDao.findPageByCriteria(dp, HibernateUtils
								.prepareOrder(dp, "courseTime", "desc"), Restrictions
								.eq("courseDate", date), Restrictions.eq("student.id",
						student.getId()), Restrictions.ne("courseStatus",
						CourseStatus.STUDENT_LEAVE),// 学生请假
						Restrictions.ne("courseStatus",
								CourseStatus.TEACHER_LEAVE),// 老师请假
						Restrictions.ne("courseStatus", CourseStatus.CANCEL)// 课程取消
				);
				String notifyUsers = "";
				String courseIds = "";
				if (dp.getDatas() != null) {

					List<Course> courseList = null;
					if (halfDayflag) {// 如果是系统打印，则要打印全天的
						// 半日考勤
						courseList = halfDayCourse((List<Course>) dp.getDatas());
					} else {
						// 全日考勤
						courseList = (List<Course>) dp.getDatas();
					}
					for (Course course : courseList) {
						if (course.getCourseStatus().equals(CourseStatus.NEW))
							course.setCourseStatus(CourseStatus.STUDENT_ATTENDANCE);

						// 统计返回学生的课程
						courseIds += course.getCourseId() + ",";

						// 拼接要通知的人员
						if (course.getStudent().getStudyManeger() != null
								&& !notifyUsers.contains(course.getStudent()
								.getStudyManeger().getUserId())) {
							notifyUsers += course.getStudent()
									.getStudyManeger().getUserId()
									+ ",";
						}
						if (course.getTeacher() != null
								&& !notifyUsers.contains(course.getTeacher()
								.getUserId())) {
							notifyUsers += course.getTeacher().getUserId()
									+ ",";
						}
						attendentStatu=true;
					}
					if (courseAttandence.indexOf("6") != -1) {// 如果设置了打印一对一课时单则返回一对一的信息供打印
						result.setRoleQLConfigName(s.getName());// 课时单标题
						result.getDatas().addAll(
								HibernateUtils.voListMapping(courseList,
										CourseVo.class, "courseFullMapping"));
					}
				}
				// 通知老师学管学生已来学校     关闭dwr   2016-12-17
//				if (StringUtils.isNotBlank(notifyUsers)) {
//					messageService.sendMessage(MessageType.SYSTEM_MSG,
//							"学生到校提醒", "学生" + student.getName() + "已到校，请及时跟进。",
//							MessageDeliverType.SINGLE, notifyUsers);
//				}

				// 记录学生考勤
				StudentAttendanceRecord studentAttendanceRecord = new StudentAttendanceRecord();
				studentAttendanceRecord.setStudent(student);
				studentAttendanceRecord.setAttendanceType(attendanceType);
				studentAttendanceRecord.setAttendanceNo(number);
				studentAttendanceRecord.setAttendanceTime(DateTools
						.getCurrentDateTime());
				if (StringUtils.isNotBlank(courseIds)) {
					if (courseIds.length() > 0
							&& courseIds.substring(courseIds.length() - 1,
							courseIds.length()).equals(",")) {// 去逗号
						courseIds = courseIds.substring(0,
								courseIds.length() - 1);
					}
					studentAttendanceRecord.setCourseIds(courseIds);
				}
				studentAttendanceRecordDao.save(studentAttendanceRecord);
			}

			if (courseAttandence.indexOf("1") != -1
					|| courseAttandence.indexOf("3") != -1) {
				isMiniClassCourse = true;
			}
			// 小班考勤isMiniClassCourse
			if (isMiniClassCourse) {
				String currentDate = DateTools.getCurrentDate();
				Map<String, Object> params = Maps.newHashMap();
				params.put("date", date);
				params.put("studentId", student.getId());
				params.put("currentDate", currentDate);
				StringBuffer hql = new StringBuffer();
				hql.append(" from MiniClassCourse   where 1=1");
				hql.append(" and  courseDate = :date ");
				hql.append(" and  miniClass.miniClassId in(select miniClass.miniClassId from MiniClassStudent where student.id= :studentId and firstSchoolTime <= :currentDate )  ");
				// 半日考勤
				if (halfDayflag) {
					GregorianCalendar ca = new GregorianCalendar();
					if (ca.get(Calendar.AM_PM) == Calendar.PM) {
						hql.append(" and courseEndTime > '12:00' ");
					} else {
						hql.append(" and courseEndTime <= '12:00' ");
					}
				}

				List<MiniClassCourse> miniClassCourseList = miniClassCourseDao
						.findAllByHQL(hql.toString(),params);

				if (courseAttandence.indexOf("7") != -1) {// 如果设置了打印小班课时单则返回小班课时信息
					result.setRoleQLConfigName(s.getName());// 课时单标题
					// 将查到的小班转成CourseVo
					for (MiniClassCourse miniClassCourse : miniClassCourseList) {
						CourseVo courseVo = miniClassCourseToCourse(
								miniClassCourse, student);
						result.getDatas().add(courseVo);
					}
				}
				for (Iterator it = miniClassCourseList.iterator(); it.hasNext();) {
					MiniClassCourse miniClassCourse = (MiniClassCourse) it
							.next();
					MiniClassStudentAttendentVo miniClassStudentAttendentVo = new MiniClassStudentAttendentVo();
					miniClassStudentAttendentVo.setStudentId(student.getId());
					miniClassStudentAttendentVo
							.setMiniClassCourseId(miniClassCourse
									.getMiniClassCourseId());
					miniClassStudentAttendentVo
							.setMiniClassAttendanceStatus(MiniClassAttendanceStatus.CONPELETE);
					miniClassStudentAttendentVo.setAttendanceType("CONPELETE");
					smallClassService.updateMiniClassAttendanceRecord(
							miniClassStudentAttendentVo.getMiniClassCourseId(),
							miniClassStudentAttendentVo, "attendance");
					attendentStatu=true;
				}
			}
			if(!attendentStatu &&  !attendanceType.equals(AttendanceType.SYSTEM))//如果没有考勤，标记一下给前台桌面软件判断是否考勤
				result.setResultCode(1);

			return result;
		} else {
			throw new ApplicationException("根据考勤编号找不到学生，请检查！");
		}
	}



	/**
	 * 查询结束时间中午12以前一对一课程
	 *
	 * @param courseList
	 * @return
	 */
	private List<Course> halfDayCourse(List<Course> courseList) {
		GregorianCalendar ca = new GregorianCalendar();
		if (ca.get(Calendar.AM_PM) == Calendar.PM) {
			for (Iterator it = courseList.iterator(); it.hasNext();) {
				Course course = (Course) it.next();
				int endHour = Integer.parseInt(course.getCourseTime()
						.substring(8, 10));
				if (endHour <= 12) {
					it.remove();
				}
			}
		} else {
			for (Iterator it = courseList.iterator(); it.hasNext();) {
				Course course = (Course) it.next();
				int endHour = Integer.parseInt(course.getCourseTime()
						.substring(8, 10));
				if (endHour > 12) {
					it.remove();
				}
			}
		}
		return courseList;
	}

	/**
	 * 将小班课程转成courseVo
	 *
	 * @param miniClassCourse
	 * @param student
	 * @return
	 */
	private CourseVo miniClassCourseToCourse(MiniClassCourse miniClassCourse,
			Student student) {
		CourseVo courseVo = new CourseVo();
		courseVo.setCourseId(miniClassCourse.getMiniClassCourseId());
		courseVo.setStudentName(student.getName());
		courseVo.setStudentId(student.getId());
		courseVo.setGrade(miniClassCourse.getGrade().getName());
		courseVo.setGradeValue(miniClassCourse.getGrade().getValue());
		courseVo.setSubject(miniClassCourse.getSubject().getName());
		courseVo.setSubjectValue(miniClassCourse.getSubject().getValue());
		courseVo.setTeacherName(miniClassCourse.getTeacher().getName());
		courseVo.setTeacherId(miniClassCourse.getTeacher().getUserId());
		courseVo.setStudyManagerName(miniClassCourse.getStudyHead().getName());
		courseVo.setCourseTime(miniClassCourse.getCourseTime());
		courseVo.setCourseDate(miniClassCourse.getCourseDate());
		return courseVo;
	}

	@Override
	public void saveCourseSunmmary(CourseSummary courseSummary) {
		courseSummaryDao.save(courseSummary);
	}



	/**
	 * 删除多条课程
	 */
	@Override
	public void deleteMultiCourse(String strCourseIds) {
		String[] courseIds = strCourseIds.split(",");
		for (String id : courseIds) {
			CourseEditVo vo = new CourseEditVo();
			vo.setId(id);
			this.deleteCourse(vo);
		}
	}

	/**
	 * 复制课程
	 *
	 * @param origDate
	 *            源日期
	 * @param destDate
	 *            目标日期
	 */
	@Override
	public void copyCourse(String origDate, String destDate, String transactionUuid) {
		if (com.eduboss.utils.StringUtil.isNotBlank(transactionUuid)) {
			transactionRecordDao.saveTransactionRecord(transactionUuid);
		}
		Organization belongCampus = userService.getBelongCampus();
		if (!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())) {
			throw new ApplicationException("当前用户的归属组织架构只有校区下的才能提排课需求");
		}
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("courseDate", origDate));
		criterions.add(Restrictions.eq("student.blCampusId", userService
				.getBelongCampus().getId()));
		List<Course> list = courseDao.findAllByCriteria(criterions);
		for (int i = 0; i < list.size(); i++) {
			Course item = list.get(i);
			Course course = new Course();
			course.setCourseStatus(CourseStatus.NEW); // 新建课程
			course.setCourseDate(destDate); // 新课程建立到目标时间
			// 复制课程设置
			course.setProduct(item.getProduct());
			course.setTeacher(item.getTeacher());
			course.setStudent(item.getStudent());
			course.setGrade(item.getGrade());
			course.setSubject(item.getSubject());
			course.setCourseTime(item.getCourseTime());
			course.setCourseMinutes(item.getCourseMinutes());
			course.setPlanHours(item.getPlanHours());
			course.setAuditStatus(item.getAuditStatus());
			course.setCreateTime(DateTools.getCurrentDateTime());
			course.setCreateUser(userService.getCurrentLoginUser());
			course.setBlCampusId(belongCampus);
			if (course.getStudent() != null) {
				course.setStudyManager(studentOrganizationDao
						.findStudentOrganizationByStudentAndOrganization(course
								.getStudent().getId(), belongCampus.getId()));
			}
			if (course.getStudyManager() == null) {
				throw new ApplicationException(ErrorCode.NOT_FIND_STUDY_MANAGER);
			}
			courseDao.save(course);
		}
	}

	private Map<String, String> needToNotify(Map<String, BigDecimal> map) {
		Map<String, String> result = new HashMap();
		for (Map.Entry<String, BigDecimal> entry:map.entrySet()){
			System.out.println(entry.getKey());
			StudentVo studentVo = studentService.findStudentById(entry.getKey());
			StudnetAccMvVo studentAccoutInfo = studentService.getStudentAccoutInfo(entry.getKey());
			System.out.println("学生"+studentVo.getName()+" 的剩余课时是"+studentAccoutInfo.getOneOnOneRemainingHour());
			System.out.println("学生"+studentVo.getName()+" 的已排课时是"+studentVo.getOneOnOneAlreadyArrangeHour());
			BigDecimal total = studentVo.getOneOnOneAlreadyArrangeHour().add(entry.getValue());
			if (total.compareTo(studentAccoutInfo.getOneOnOneRemainingHour())>0){
				//已经超剩余课时
				result.put(studentVo.getName(), studentVo.getStudyManegerId());
			}else {
				//没有超剩余课时
			}

		}

		return result;
	}

	/**
	 * 检查复制单日课程
	 * @param origDate
	 * @param destDate
	 * @return
	 */
	@Override
	public Map<String, String> checkCopyCourse(String origDate, String destDate) {
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.eq("courseDate", origDate));
		criterions.add(Restrictions.eq("student.blCampusId", userService
				.getBelongCampus().getId()));
		List<Course> list = courseDao.findAllByCriteria(criterions);
		Map<String, BigDecimal> map = putCourseHourInMap(list);
		Map<String, String> result = needToNotify(map);
		return result;
	}




	/**
	 *
	 * @param list
	 */
	private Map<String, BigDecimal> putCourseHourInMap(List<Course> list) {
		Map<String, BigDecimal> map = new HashMap<>();

		for (Course course : list){
			String studentId = course.getStudent().getId();
			BigDecimal planHours = course.getPlanHours();
			BigDecimal p = map.get(studentId)==null?BigDecimal.ZERO:map.get(studentId);
			p=p.add(planHours);
			map.put(studentId, p);
		}
		return map;
	}

	/**
	 * 复制多条课程
	 *
	 * @param strCourseIds
	 */
	@Override
	public void copyMultiCourse(String strCourseIds, String destDate, String transactionUuid) {
		if (com.eduboss.utils.StringUtil.isNotBlank(transactionUuid)) {
			transactionRecordDao.saveTransactionRecord(transactionUuid);
		}
		String[] courseIds = strCourseIds.split(",");
		Organization belongCampus = userService.getBelongCampus();
		if (!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())) {
			throw new ApplicationException("当前用户的归属组织架构只有校区下的才能提排课需求");
		}
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.in("courseId", courseIds));
		List<Course> list = courseDao.findAllByCriteria(criterions);

		for (Course item : list) {
			Course course = new Course();
			course.setCourseStatus(CourseStatus.NEW); // 新建课程
			course.setCourseDate(destDate); // 新课程建立到目标时间
			// 复制课程设置
			course.setProduct(item.getProduct());
			course.setTeacher(item.getTeacher());
			course.setStudent(item.getStudent());
			course.setGrade(item.getGrade());
			course.setSubject(item.getSubject());
			course.setCourseTime(item.getCourseTime());
			course.setCourseMinutes(item.getCourseMinutes());
			course.setPlanHours(item.getPlanHours());
			course.setAuditStatus(item.getAuditStatus());
			course.setCreateTime(DateTools.getCurrentDateTime());
			course.setCreateUser(userService.getCurrentLoginUser());
			course.setBlCampusId(belongCampus);
			if (course.getStudent() != null) {
				course.setStudyManager(studentOrganizationDao
						.findStudentOrganizationByStudentAndOrganization(course
								.getStudent().getId(), belongCampus.getId()));
			}
			if (course.getStudyManager() == null) {
				throw new ApplicationException(ErrorCode.NOT_FIND_STUDY_MANAGER);
			}
			courseDao.save(course);
		}
	}


	/**
	 * 检查复制多条课程
	 * @param strCourseIds
	 * @param destDate
	 * @return
	 */
	@Override
	public Map<String, String> checkCopyMultiCourse(String strCourseIds, String destDate) {
		String[] courseIds = strCourseIds.split(",");
		Organization belongCampus = userService.getBelongCampus();
		if (!OrganizationType.CAMPUS.equals(belongCampus.getOrgType())) {
			throw new ApplicationException("当前用户的归属组织架构只有校区下的才能提排课需求");
		}
		List<Criterion> criterions = new ArrayList<Criterion>();
		criterions.add(Restrictions.in("courseId", courseIds));
		List<Course> list = courseDao.findAllByCriteria(criterions);
		Map<String, BigDecimal> map = putCourseHourInMap(list);
		Map<String, String> result = needToNotify(map);
		return result;
	}

	/**
	 * 删除一条课程概要 删除课程概要将删除该概要下所有今天以后的课程 课程概要删除为逻辑删除 delFlag=1
	 *
	 * @param id
	 */
	@Override
	public void deleteCourseSummary(String id) {
		// 修改概要需要删除该概要下所有今天以后未考勤的课程
		List<Course> courses = courseDao.findByCriteria(
				Restrictions.eq("courseSummary.id", id),
				Restrictions.ge("courseDate", DateTools.getCurrentDate()),
				Restrictions.eq("courseStatus", CourseStatus.NEW));
		courseDao.deleteAll(courses);
		CourseSummary courseSummary = courseSummaryDao.findById(id);
		courseSummary.setDelFlag(1);
	}

	/**
	 * 根据学生id 查询课程概要
	 *
	 * @param teacherId
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<CourseSummarySearchResultVo> getCourseSummaryByTeacherId(
			String teacherId) {
		List<Criterion> criterionList = new ArrayList<Criterion>();
		criterionList.add(Restrictions.eq("delFlag", 0)); // 只查询未逻辑删除的
		criterionList.add(Restrictions.eq("teacher.id", teacherId));
		criterionList.add(Restrictions.eq("type", CourseSummaryType.TEACHER));
		List<CourseSummary> couSumList = courseSummaryDao
				.findAllByCriteria(criterionList);
		for (CourseSummary summary : couSumList) {
			if (summary.getSpanHours() == null)
				summary.setSpanHours(courseDao.getCourseListByCourseSummaryId(
						summary.getCourseSummaryId()).getRowCount()
						* (summary.getPlanHours() == null ? summary
								.getSpanHours() : summary.getPlanHours()));
		}
		return HibernateUtils.voListMapping(couSumList,
				CourseSummarySearchResultVo.class);
	}

	@Override
	public DataPackage getSchoolZoneCourseList2ForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		dp = courseDao.getSchoolZoneCourseListForStudent(searchInputVo, dp);
		return dp;
	}

	/**
	 * 根据查询条件查找小班课程
	 *
	 * @param conditionHql
	 * @return
	 */
	@Override
	public List<MiniClassCourse> getCourseByCondition(String conditionHql,Map<String, Object> params) {

		return miniClassCourseDao.findAllByHQL(conditionHql,params);
	}

	/**
	 * 根据小班课程ID查找小班上课记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public List<MiniClassStudentAttendent> getMiniClassStudentAttendentById(
			String id) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", id);
		StringBuilder sb = new StringBuilder();
		sb.append(" from MiniClassStudentAttendent where 1=1 ");
		sb.append(" and miniClassCourse.id = :id ");

		return miniClassStudentAttendentDao.findAllByHQL(sb.toString(),params);
	}

	/**
	 * 老师请假，取消请假
	 *
	 * @param courseId
	 * @param courseStatus
	 * @return
	 */
	@Override
	public void teacherAskForLeave(String courseId, String courseStatus) {
		Course course = courseDao.findById(courseId);
		course.setCourseStatus(CourseStatus.valueOf(courseStatus));
		if (CourseStatus.NEW.getValue().equals(courseStatus)) {
			course.setRealHours(null);
			course.setAuditHours(null);
			// course.setStudyManagerAuditTime("");
			// course.setStudyManagerAuditId("");
			// course.setTeachingAttendTime("");
		}
		courseDao.save(course);
	}

	@Override
	public void sendMessageByCourse(String courseId, String Type) {
		Course course = courseDao.findById(courseId);
		if (course != null) {
			if ("teacher".equals(Type)) {
				if (StringUtil.isNotEmpty(course.getTeacher().getContact())) {
//						String ren = MessageUtil.readContentFromGet(
//								course.getTeacher().getContact(),
//								"你好，你于"
//										+ course.getCourseDate()
//										+ "_"
//										+ course.getCourseTime().replace(" ",
//												"") + "有一次科目为"
//										+ course.getGrade().getName() + "，"
//										+ course.getSubject().getName()
//										+ "的一对一课程，学生为"
//										+ course.getStudent().getName()
//										+ "，请提前10分钟到校，谢谢。");
//						if (Integer.valueOf(ren) < 0) {
//							throw new ApplicationException("发送失败");
//						}
					if (!AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_60105714", course.getTeacher().getContact(),
							"{\"courseDate\":\""+  course.getCourseDate() + "\",\"courseTime\":\"" + course.getCourseTime().replace(" ", "") + "\","
								+ "\"gradeSubject\":\"" + course.getGrade().getName() + course.getSubject().getName() + "\","
								+ "\"studentName\":\"" + course.getStudent().getName() + "\"}")) {
						throw new ApplicationException("发送失败");
					}
				} else {
					throw new ApplicationException("该老师没有填写手机号");
				}
			} else if ("customer".equals(Type)) {
				Set<Customer> cu = course.getStudent().getCustomers();
				for (Iterator iterator = cu.iterator(); iterator.hasNext();) {
					Customer customer = (Customer) iterator.next();
					if (StringUtil.isNotEmpty(customer.getContact())) {
//							String ren = MessageUtil.readContentFromGet(
//									customer.getContact(),
//									"你好，你的孩子"
//											+ course.getStudent().getName()
//											+ "于"
//											+ course.getCourseDate()
//											+ "_"
//											+ course.getCourseTime().replace(
//													" ", "") + "有一次科目为"
//											+ course.getGrade().getName() + "，"
//											+ course.getSubject().getName()
//											+ "的一对一课程，老师为"
//											+ course.getTeacher().getName()
//											+ "，请提前10分钟到校，谢谢。");
//							if (Integer.valueOf(ren) < 0) {
//								throw new ApplicationException("发送失败");
//							}
						if (!AliyunSmsUtil.sendSms(AliyunSmsUtil.SIGN_NAME, "SMS_59985719", customer.getContact(),
								"{\"studentName\":\""+  course.getStudent().getName() + "\",\"courseDate\":\"" + course.getCourseDate() + "\","
									+ "\"courseTime\":\"" + course.getCourseTime().replace(" ", "") + "\","
									+ "\"gradeSubject\":\"" + course.getGrade().getName() + course.getSubject().getName() + "\","
									+ "\"teacherName\":\"" + course.getTeacher().getName() + "\"}")) {
							throw new ApplicationException("发送失败");
						}
						break;
					} else {
						throw new ApplicationException("该客户没有填写手机号");
					}
				}
			} else {
				throw new ApplicationException(
						ErrorCode.PARAMETER_FORMAT_ERROR);
			}
		} else {
			throw new ApplicationException(ErrorCode.COURSE_NOT_FOUND);
		}

	}

	/**
	 * 主要是针对学生 ， 单纯的获取课表信息， 与获取本周课表有区别 因为学生不会为查询的时候加入过多的 session role 控制
	 *
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	private DataPackage findPageCourseForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		hql.append("from Course as course ");
		hql.append(" where 1 = 1 ");

		Map<String, Object> params = Maps.newHashMap();

		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			hql.append(" and courseDate >= :startDate ");
			params.put("startDate", searchInputVo.getStartDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getEndDate())) {
			hql.append(" and courseDate <= :endDate ");
			params.put("endDate", searchInputVo.getEndDate());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentId())) {
			hql.append(" and student.id = :studentId ");
			params.put("studentId", searchInputVo.getStudentId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getStudentName())) {
			hql.append(" and student.name like :studentName ");
			params.put("studentName", "%"+searchInputVo.getStudentName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherId())) {
			hql.append(" and teacher.userId = :teacherId ");
			params.put("teacherId", searchInputVo.getTeacherId());
		}
		if (StringUtils.isNotBlank(searchInputVo.getTeacherName())) {
			hql.append(" and teacher.name like :teacherName ");
			params.put("teacherName", "%"+searchInputVo.getTeacherName()+"%");
		}
		if (StringUtils.isNotBlank(searchInputVo.getGrade())) {
			hql.append(" and grade.value = :grade ");
			params.put("grade", searchInputVo.getGrade());
		}
		if (StringUtils.isNotBlank(searchInputVo.getSubject())) {
			hql.append(" and subject.value = :subject ");
            params.put("subject", searchInputVo.getSubject());
		}
		if (searchInputVo.getCourseStatus() != null) {
			hql.append(" and courseStatus = :courseStatus ");
			params.put("courseStatus", searchInputVo.getCourseStatus().getValue());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getCourseSummaryId())) {
			hql.append(" and courseSummary.courseSummaryId = :courseSummaryId ");
			params.put("courseSummaryId", searchInputVo.getCourseSummaryId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerId())) {
			hql.append(" and studyManager.userId = :studyManagerId ");
			params.put("studyManagerId", searchInputVo.getStudyManagerId());
		}
		if (StringUtils.isNotEmpty(searchInputVo.getStudyManagerName())) {
			hql.append(" and studyManager.name LIKE :studyManagerName ");
			params.put("studyManagerName", "%"+searchInputVo.getStudyManagerName()+"%");
		}
		if (searchInputVo.getDayOfWeek() != null) {
			hql.append(" and  DAYOFWEEK(courseDate) = :dayOfWeek ");
			params.put("dayOfWeek", searchInputVo.getDayOfWeek());
		}
		if (StringUtils.isNotBlank(searchInputVo.getProductName())) {
			hql.append(" and  product.name like :productName ");
			params.put("productName", "%"+searchInputVo.getProductName()+"%");
		}

		// 带上权限查询
		// hql.append(roleQLConfigService.getValueResult("本周课表", "hql"));

		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql.append(" order by "+dp.getSidx() + " " + dp.getSord()+" ,courseDate desc ,courseTime desc");
		} else {
			hql.append(" order by courseDate desc ,courseTime desc");
		}

		dp = courseDao.findPageByHQL(hql.toString(), dp,true,params);
		return dp;
	}

	@Override
	public DataPackage findPageCourseForOneWeekForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		// 主要只针对学生APP的 查询
		dp = findPageCourseForStudent(searchInputVo, dp);
		List<CourseWeekVo> courseWeekVoList = new ArrayList<CourseWeekVo>();

		List<Course> courseList = (List) dp.getDatas();
		int crashInd = 0;
		for (Course course : courseList) {
			// 检测是否有冲突
			// 可能暂时不需要冲突的检测
			// crashInd=courseConflictDao.countDistinctConflicts(course.getCourseId(),
			// course.getStudent().getId(),
			// course.getTeacher().getUserId(),course.getCourseDate(),
			// course.getCourseTime());

			CourseWeekVo vo = HibernateUtils.voObjectMapping(course,
					CourseWeekVo.class);
			// vo.setCrashInd(crashInd);
			courseWeekVoList.add(vo);
		}

		dp.setDatas(courseWeekVoList);
		return dp;
	}


	@Override
	public void saveCourseAttendancePic(String courseId,MultipartFile attendancePicFile,String servicePath) throws Exception {
		Course course = courseDao.findById(courseId);
		if(course !=null) {
			// 统一使用 文件名 来 标明签名文件
			//String fileName = UUID.randomUUID().toString();
			String fileName =  String.format("ONE_ON_ONE_ATTEND_PIC_%s.jpg", courseId);
			course.setAttendacePicName(fileName);

			String folder=servicePath+PropertiesUtils.getStringValue("save_file_path");//系统路径
			isNewFolder(folder);

			String bigFileName = "BIG_"+fileName;//阿里云上面的文件名 大   默认是JPG 
			String midFileName = "MID_"+fileName;//阿里云上面的文件名 中
			String smallFileName = "SMALL_"+fileName;//阿里云上面的文件名 小					 

			String relFileName=folder+"/realFile_"+courseId+UUID.randomUUID().toString()+".jpg";
			File realFile=new File(relFileName);
			File bigFile=new File(folder+"/"+bigFileName);
			File midFile=new File(folder+"/"+midFileName);
			File smallFile=new File(folder+"/"+smallFileName);

			try {
				attendancePicFile.transferTo(realFile);
				ImageSizer.compressImage(relFileName, smallFile.getAbsolutePath(), 60);//转换图片大小
				ImageSizer.compressImage(relFileName, midFile.getAbsolutePath(), 200);//转换图片大小
				ImageSizer.compressImage(relFileName, bigFile.getAbsolutePath(), 600);//转换图片大小
				AliyunOSSUtils.put(bigFileName, bigFile);//传到阿里云
				AliyunOSSUtils.put(midFileName, midFile);//传到阿里云
				AliyunOSSUtils.put(smallFileName, smallFile);//传到阿里云				
				AliyunOSSUtils.put(fileName, realFile);//传原图到阿里云				
			} catch (IllegalStateException e) {
				e.printStackTrace();
				throw new IllegalStateException();
			} catch (IOException e) {
				e.printStackTrace();
				throw new ApplicationException();
			}finally{
				bigFile.delete();
				midFile.delete();
				smallFile.delete();
				realFile.delete();
			}
			//mobilePushMsgService.saveFileToRemoteServer(attendancePicFile, fileName);
		}

	}

	/**
	 * 找到相应的 一对一当天
	 *
	 * @param searchInputVo
	 * @return
	 */
	@Override
	public DataPackage findPageCourseForToday(
			CourseSearchInputVo searchInputVo, DataPackage dp) {
		StringBuffer hql = new StringBuffer();
		hql.append("from Course as course ");
		hql.append(" where 1 = 1 ");
		Map<String, Object> params = Maps.newHashMap();
		if (StringUtils.isNotBlank(searchInputVo.getStartDate())) {
			hql.append(" and courseDate like :startDate ");
			params.put("startDate", searchInputVo.getStartDate()+"%");
		}
		if (StringUtils.isNotEmpty(searchInputVo.getBlCampusId())) {
			hql.append(" and blCampusId.id = :blCampusId ");
			params.put("blCampusId", searchInputVo.getBlCampusId());
		} else {
			hql.append(" and blCampusId.id = :blCampusId ");
			params.put("blCampusId", userService.getBelongCampus().getId());
		}
		// 带上权限查询
		hql.append(roleQLConfigService.getAppendSqlByAllOrg("本周课表","hql","blCampusId.id"));
		hql.append(" order by courseDate desc ,courseTime");
		dp = courseDao.findPageByHQL(hql.toString(), dp,true,params);

		List<CourseChangesSearchResultVo> searchResultVoList = new ArrayList<CourseChangesSearchResultVo>();
		List<Course> courseList = (List) dp.getDatas();
		int crashInd = 0;
		for (Course course : courseList) {
			// 检测是否有冲突
			crashInd = courseConflictDao.countDistinctConflicts(course
					.getCourseId(), course.getStudent().getId(), course
					.getTeacher().getUserId(), course.getCourseDate(), course
					.getCourseTime());
			CourseChangesSearchResultVo vo = HibernateUtils.voObjectMapping(
					course, CourseChangesSearchResultVo.class);
			vo.setConflictCount(crashInd);
			String isWashed = "FALSE";
			int count = accountChargeRecordsDao.countAccountChargeRecordsByCourseId(vo.getCourseId(), ProductType.ONE_ON_ONE_COURSE);
			if (count > 0) {
				isWashed = "TRUE";
			}
			vo.setIsWashed(isWashed);
			searchResultVoList.add(vo);
		}

		dp.setDatas(searchResultVoList);
		return dp;
	}

	@Override
	public boolean checkHasOneOnOneCourseForDate(String teacherId, String date,
			List<CourseStatus> listOfStatus) {
		StringBuffer countHql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("teacherId", teacherId);
		params.put("date", date);
		countHql.append("select count(*) from Course where 1 =1 ")
				.append(" and teacher.userId = :teacherId ").append(" and courseDate = :date ");
		if (listOfStatus.size() > 0) {
			countHql.append(" and (courseStatus in ( :listOfStatus ) or auditStatus='UNVALIDATE'");
			params.put("listOfStatus", listOfStatus);
//			for (CourseStatus status : listOfStatus) {
//				countHql.append("'").append(status).append("',");
//			}

//			countHql.deleteCharAt(countHql.length() - 1).append(") or auditStatus='UNVALIDATE')");
		}
		int count = this.courseDao.findCountHql(countHql.toString(),params);
		return count >= 1;
	}

	@Override
	public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDate(
			String teacherId, String startDate, String endDate) {
		List<String> dates = DateTools.getDates(startDate, endDate);
		boolean hasCourse = false;
		boolean hasUnCheckAttendanceCourse = false;
		List<HasOneOnOneCourseVo> listHasOneOnOneCourse = new ArrayList<HasOneOnOneCourseVo>();
		for (String strDate : dates) {
			HasOneOnOneCourseVo hasOneOnOneCourseVo=new HasOneOnOneCourseVo();
			hasOneOnOneCourseVo.setDateValue(strDate);//日期

			hasCourse = this.checkHasOneOnOneCourseForDate(teacherId, strDate,new ArrayList<CourseStatus>());
			if (hasCourse) { // NEW STUDENT_ATTENDANCE 检查 新， 学生已考勤
				List<CourseStatus> listOfStatus = new ArrayList<CourseStatus>();
				listOfStatus.add(CourseStatus.NEW);
				listOfStatus.add(CourseStatus.STUDENT_ATTENDANCE);
				hasUnCheckAttendanceCourse = this
						.checkHasOneOnOneCourseForDate(teacherId, strDate,
								listOfStatus);
				if (hasUnCheckAttendanceCourse) { // NEW STUDENT_ATTENDANCE 检查
													// 新， 学生已考勤
					hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());//课程状态					
				} else { // 有课 且 都是 没有未考勤的， 就是全部是 不用考勤的 ， // TEACHER_ATTENDANCE
							// STUDY_MANAGER_AUDITED CHARGED 老师已考勤， 学管已确认，
							// 教务已经扣费
					hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());//课程状态						
				}
			} else {
				hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.NO_COURSE.getValue());//课程状态	
			}
			listHasOneOnOneCourse.add(hasOneOnOneCourseVo);
		}
		return listHasOneOnOneCourse;
	}


	@Override
	public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDateByStatu(
			String status, String startDate, String endDate) {
		List<String> dates = DateTools.getDates(startDate, endDate);
		Organization belongCampus = userService.getBelongCampus();
		List<HasOneOnOneCourseVo> listHasOneOnOneCourse = new ArrayList<HasOneOnOneCourseVo>();
		String userId  =userService.getCurrentLoginUser().getUserId();
		Map<String, Object> params = Maps.newHashMap();
		for (String strDate : dates) {
			HasOneOnOneCourseVo hasOneOnOneCourseVo=new HasOneOnOneCourseVo();
			hasOneOnOneCourseVo.setDateValue(strDate);//日期
			StringBuffer whereHql1 = new StringBuffer();
			StringBuffer whereHql2 = new StringBuffer();
			StringBuffer whereHql3 = new StringBuffer();
			StringBuffer otherHql = new StringBuffer();
			StringBuffer otherHql2 = new StringBuffer();
			StringBuffer otherHql3 = new StringBuffer();

			    params.put("strDate", strDate);
				whereHql1.append("select count(*) from Course where 1 =1 ")
						.append(" and course_Date = :strDate ");

				whereHql1.append(" and  teacher_id = '" + userId + "' ");
	        	otherHql.append(" and (course_status in ('"+CourseStatus.NEW+"','"+CourseStatus.STUDENT_ATTENDANCE+"') or AUDIT_STATUS='UNVALIDATE')");



	        	whereHql2.append("select count(*) from Course where 1 =1 ")
						.append(" and course_Date = :strDate ");
				whereHql2.append("and  STUDY_MANAGER_ID = '" + userId + "' ");
//				whereHql2.append(" and blCampusId ='"+belongCampus.getId()+"'");
				otherHql2.append(" and course_status in ('"+CourseStatus.NEW+"','"+CourseStatus.STUDENT_ATTENDANCE+"','"+CourseStatus.TEACHER_ATTENDANCE+"') ");



				whereHql3.append("select count(*) from Course where 1 =1 ")
						.append(" and course_Date = :strDate ");
				whereHql3.append(" and bl_campus_id ='"+belongCampus.getId()+"'");
				otherHql3.append(" and course_status in ('"+CourseStatus.NEW+"','"+CourseStatus.STUDENT_ATTENDANCE+"','"+CourseStatus.TEACHER_ATTENDANCE+"','"+CourseStatus.STUDY_MANAGER_AUDITED+"') ");

				int count1 = courseDao.findCountSql(whereHql1.toString(),params);
				int count2 = courseDao.findCountSql(whereHql2.toString(),params);
				int count3 = courseDao.findCountSql(whereHql3.toString(),params);

			if(count1 >= 1){
				hasOneOnOneCourseVo.setTeacherCount(count1);
				whereHql1.append(otherHql);
				int count11 = courseDao.findCountSql(whereHql1.toString(),params);
				if(count11 >= 1){
					hasOneOnOneCourseVo.setTeacherCheckCount(count1-count11);
					hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}else{
					hasOneOnOneCourseVo.setTeacherCheckCount(0);
					hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}
			}else{
				hasOneOnOneCourseVo.setTeacherCount(0);
				hasOneOnOneCourseVo.setTeacherCheckCount(0);
				hasOneOnOneCourseVo.setHasValue(CourseSummaryStatus.NO_COURSE.getValue());//课程状态	
			}

			if(count2 >= 1){//学管确认
				hasOneOnOneCourseVo.setStudyCount(count2);
				whereHql2.append(otherHql2);
				int count11 = courseDao.findCountSql(whereHql2.toString(),params);
				if(count11 >= 1){
					hasOneOnOneCourseVo.setStudyCheckCount(count2-count11);
					hasOneOnOneCourseVo.setHasStudyManagerValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}else{
					hasOneOnOneCourseVo.setStudyCheckCount(0);
					hasOneOnOneCourseVo.setHasStudyManagerValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}
			}else{
				hasOneOnOneCourseVo.setStudyCount(0);
				hasOneOnOneCourseVo.setStudyCheckCount(0);
				hasOneOnOneCourseVo.setHasStudyManagerValue(CourseSummaryStatus.NO_COURSE.getValue());//课程状态	
			}

			if(count3 >= 1 && userService.isCurrentUserRoleCode(RoleCode.EDUCAT_SPEC)){//教务扣费
				hasOneOnOneCourseVo.setDeductionCount(count3);
				whereHql3.append(otherHql3);
				int count11 = courseDao.findCountSql(whereHql3.toString(),params);
				if(count11 >= 1){
					hasOneOnOneCourseVo.setDeductionCheckCount(count3-count11);
					hasOneOnOneCourseVo.setHasDeductionValue(CourseSummaryStatus.HAS_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}else{
					hasOneOnOneCourseVo.setDeductionCheckCount(0);
					hasOneOnOneCourseVo.setHasDeductionValue(CourseSummaryStatus.HAS_NO_UNCHECH_ATTENDANCE.getValue());//课程状态		
				}
			}else{
				hasOneOnOneCourseVo.setDeductionCount(0);
				hasOneOnOneCourseVo.setDeductionCheckCount(0);
				hasOneOnOneCourseVo.setHasDeductionValue(CourseSummaryStatus.NO_COURSE.getValue());//课程状态	
			}


			listHasOneOnOneCourse.add(hasOneOnOneCourseVo);
		}
		return listHasOneOnOneCourse;

	}
	/**
	 * 查找当前用户当天的全部课程
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findCourseDay(String courseDate,DataPackage dataPackage) {
		CourseSearchInputVo vo = new CourseSearchInputVo();
		vo.setStartDate(courseDate);
		vo.setEndDate(courseDate);
		dataPackage = findPageCourse(vo, dataPackage);

		String voMapperId = "courseFullMapping";
		List<CourseVo> searchResultVoList = HibernateUtils.voListMapping(
				(List<Course>) dataPackage.getDatas(), CourseVo.class, voMapperId);

		for (CourseVo courseVo : searchResultVoList) {
			// 班主任确认课时
			courseVo.setStaduyManagerAuditHours(courseVo.getAuditHours());
		}

		dataPackage.setDatas(searchResultVoList);

		return dataPackage;
	}

	public void isNewFolder(String folder){
		File f1=new File(folder);
		if(!f1.exists())
		{
			f1.mkdirs();
		}
	}

	/**
	 * 一对一课程审批与查看汇总
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	@Override
	public DataPackage getOneOnOneAuditAnalyzeList(CourseVo inputCourseVo,
			DataPackage dp) {
		if ("keshi".equals(inputCourseVo.getAnshazhesuan())){
			dp= courseDao.getOneOnOneAuditAnalyzeList(inputCourseVo, dp);
		} else {
			dp= courseDao.getOneOnOneAuditAnalyzeListXiaoShi(inputCourseVo,dp);
		}

		List<Map<String,String>> list= (List<Map<String, String>>) dp.getDatas();
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}

			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}


			newlist.add(map);
		}
		dp.setDatas(newlist);
		return dp;
	}


	/**
	 * 查询小班课时汇总
	 */
	@Override
	public DataPackage MiniClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String miniClassTypeId){
		return courseDao.MiniClassCourseCollectList(dataPackage, startDate, endDate, organizationIdFinder,miniClassTypeId);
	}

	/**
	 * 小班点击已排班次查看课程详情
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param courseVo
	 * @return
	 */
	@Override
	public DataPackage getSmallClassCourseInfo(DataPackage dp, String startDate, String endDate, String campusId,
			MiniClassCourseVo miniClassCourseVo){

		Map<String, Object> params = Maps.newHashMap();

		StringBuffer sql=new StringBuffer();
		sql.append("select mcc.* ");
		sql.append(" from mini_class_course mcc,mini_class mc,product p  ");
		sql.append(" where mcc.MINI_CLASS_ID=mc.MINI_CLASS_ID and p.ID = mc.PRODUCE_ID ");
		sql.append(" and mcc.course_status <> 'CANCEL' ");
		if(StringUtils.isNotBlank(campusId)){
    		sql.append(" and mc.BL_CAMPUS_ID= :campusId ");
    		params.put("campusId", campusId);
    	}
    	if(StringUtils.isNotBlank(startDate)){
    		sql.append(" and mcc.COURSE_DATE >= :startDate ");
    		params.put("startDate", startDate);
    	}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and  mcc.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseDate())){
			sql.append(" and mcc.COURSE_DATE = :courseDate ");
			params.put("courseDate", miniClassCourseVo.getCourseDate());
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseTime())){
			sql.append(" and mcc.COURSE_TIME>= :courseTime ");
			params.put("courseTime", miniClassCourseVo.getCourseTime());
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseEndTime())){
			sql.append(" and mcc.COURSE_END_TIME<= :courseEndTime ");
			params.put("courseEndTime", miniClassCourseVo.getCourseEndTime());
		}

		if(StringUtils.isNotBlank(miniClassCourseVo.getMiniClassType())){
			String[] miniClassTypes=miniClassCourseVo.getMiniClassType().split(",");
			if(miniClassTypes.length>0){
//				sql.append(" and (p.CLASS_TYPE_ID ='"+miniClassTypes[0]+"' ");
//				for (int i = 1; i < miniClassTypes.length; i++) {
//					sql.append(" or p.CLASS_TYPE_ID ='"+miniClassTypes[i]+"' ");
//				}
//				sql.append(" )");
				sql.append(" and (p.CLASS_TYPE_ID in ( :miniClassTypes ))");
				params.put("miniClassTypes", miniClassTypes);
			}
		}
		sql.append(" and mcc.COURSE_STATUS <> 'CANCEL' ");

		sql.append(" order by mcc.COURSE_DATE desc ");
		dp=miniClassCourseDao.findPageBySql(sql.toString(), dp, true,params);
    	List<MiniClassCourse> list=(List<MiniClassCourse>)dp.getDatas();
    	List<MiniClassCourseVo> voList=HibernateUtils.voListMapping(list, MiniClassCourseVo.class);
    	if(voList != null && voList.size()>0){
    		int i = 0;
    		for(MiniClassCourseVo vo:voList){
    			i = i+1 ;
    			Date courseData=DateTools.stringConversDate(vo.getCourseDate(), "yyyy-MM-dd");
    			vo.setWeekDay(DateTools.getWeekOfDate(courseData));
    			vo.setAppendCourseTime(vo.getCourseTime()+"-"+vo.getCourseEndTime());
    			if(campusId != null && StringUtils.isNotBlank(campusId)){
    				Organization campus=organizationDao.findById(campusId);
    				vo.setBlCampusName(campus.getName());
    			}
    			MiniClass miniClass=miniClassDao.findById(vo.getMiniClassId());
    			vo.setMiniClassName(miniClass.getName());
    			if(miniClass.getClassroom()!=null){
    				vo.setClassroom(miniClass.getClassroom().getClassroom());
    			}
    			String sqlCount=" select COUNT(1) from mini_class_student_attendent where MINI_CLASS_COURSE_ID= '" +  vo.getMiniClassCourseId() + "' ";
    			int stuNum=miniClassCourseDao.findCountSql(sqlCount, new HashMap<String, Object>());
    			vo.setStudentNums(stuNum);
    		}
    	}
    	dp.setDatas(voList);

		return dp;
	}

	/**
	 * 双师点击已排班次查看课程详情
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public DataPackage getTwoTeacherClassCourseInfo(DataPackage dp, String startDate, String endDate, String campusId,
			MiniClassCourseVo miniClassCourseVo){

		Map<String, Object> params = Maps.newHashMap();

		StringBuffer sql=new StringBuffer();
		sql.append("select course.COURSE_ID as courseId,course.COURSE_DATE as courseDate,course.COURSE_TIME as courseTime,course.COURSE_END_TIME as courseEndTime,");
		sql.append(" course.COURSE_HOURS as courseHours,two.`NAME` as courseName,o.name as blCampusName,");
		sql.append(" crm.CLASS_ROOM as classroom,d.`NAME` as grade,dd.`NAME` as subject,u.`NAME` as teacherName,uu.`NAME` as studyManegerName ");
		sql.append(" from two_teacher_class_student_attendent ttcsa ");
		sql.append(" left join two_teacher_class_two two on two.CLASS_TWO_ID= ttcsa.CLASS_TWO_ID");
		sql.append(" left join classroom_manage crm on two.CLASS_ROOM_ID = crm.ID ");
		sql.append(" left join organization o on o.id = two.BL_CAMPUS_ID ");
		sql.append(" left join two_teacher_class_course course on course.COURSE_ID= ttcsa.TWO_CLASS_COURSE_ID");
		sql.append(" left join two_teacher_class cla on cla.CLASS_ID= course.CLASS_ID");
		sql.append(" left join product p on p.ID= cla.PRODUCE_ID");
		sql.append(" left join data_dict d on d.ID = p.GRADE_ID ");
		sql.append(" left join data_dict dd on dd.ID = cla.`SUBJECT` ");
		sql.append(" left join `user` u on u.USER_ID = cla.TEACHER_ID ");
		sql.append(" left join `user` uu on uu.USER_ID = two.TEACHER_ID ");

	    sql.append(" where ttcsa.COURSE_STATUS<>'CANCEL' ");
		if(StringUtils.isNotBlank(campusId)){
    		sql.append(" and two.BL_CAMPUS_ID= :campusId ");
    		params.put("campusId", campusId);
    	}
    	if(StringUtils.isNotBlank(startDate)){
    		sql.append(" and course.COURSE_DATE >= :startDate ");
    		params.put("startDate", startDate);
    	}
		if(StringUtils.isNotBlank(endDate)){
			sql.append(" and course.COURSE_DATE <= :endDate ");
			params.put("endDate", endDate);
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseDate())){
			sql.append(" and course.COURSE_DATE = :courseDate ");
			params.put("courseDate", miniClassCourseVo.getCourseDate());
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseTime())){
			sql.append(" and course.COURSE_TIME>= :courseTime ");
			params.put("courseTime", miniClassCourseVo.getCourseTime());
		}
		if(StringUtils.isNotBlank(miniClassCourseVo.getCourseEndTime())){
			sql.append(" and course.COURSE_END_TIME<= :courseEndTime ");
			params.put("courseEndTime", miniClassCourseVo.getCourseEndTime());
		}

		if(StringUtils.isNotBlank(miniClassCourseVo.getMiniClassType())){
			String[] miniClassTypes=miniClassCourseVo.getMiniClassType().split(",");
			if(miniClassTypes.length>0){
				sql.append(" and (p.CLASS_TYPE_ID in ( :miniClassTypes ))");
				params.put("miniClassTypes", miniClassTypes);
			}
		}
		sql.append(" group by course.COURSE_ID,two.CLASS_TWO_ID ");
		sql.append(" order by course.COURSE_DATE desc ");

		dp = twoTeacherClassCourseDao.findMapPageBySQL(sql.toString(), dp, false, params);
		dp.setRowCount(twoTeacherClassCourseDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ", params));
    	List<Map<Object, Object>> list=(List<Map<Object, Object>>)dp.getDatas();
    	List<TwoTeacherClassCourseVo> voList=new ArrayList<>();
    	TwoTeacherClassCourseVo vo = null;
    	if(list != null && list.size()>0){
    		for(Map<Object, Object> map:list){
    			vo = new TwoTeacherClassCourseVo();
    			vo.setCourseDate(map.get("courseDate")!=null?map.get("courseDate").toString():"");
    			Date courseData=DateTools.stringConversDate(vo.getCourseDate(), "yyyy-MM-dd");
    			vo.setWeekDay(DateTools.getWeekOfDate(courseData));
    			vo.setAppendCourseTime(map.get("courseTime")+"-"+map.get("courseEndTime"));
    			vo.setCourseHours(map.get("courseHours")!=null?Double.valueOf(map.get("courseHours").toString()):0);
    			vo.setCourseName(map.get("courseName")!=null?map.get("courseName").toString():"");
    			vo.setBlCampusName(map.get("blCampusName")!=null?map.get("blCampusName").toString():"");
    			vo.setClassroom(map.get("classroom")!=null?map.get("classroom").toString():"");
    			vo.setGradeName(map.get("grade")!=null?map.get("grade").toString():"");
    			vo.setSubjectName(map.get("subject")!=null?map.get("subject").toString():"");
    			vo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
    			vo.setTeacherTwoName(map.get("studyManegerName")!=null?map.get("studyManegerName").toString():"");
    			String sqlCount=" select COUNT(1) from two_teacher_class_student_attendent ttcsa where ttcsa.TWO_CLASS_COURSE_ID = '" + map.get("courseId") + "' ";
    			int stuNum=twoTeacherAttendentDao.findCountSql(sqlCount, new HashMap<String, Object>());
    			vo.setStudentNums(stuNum);

    			voList.add(vo);
    		}
    	}
    	dp.setDatas(voList);

		return dp;
	}

	/**
	 * 一对多课时汇总
	 */
	@Override
	public DataPackage getOtmClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String otmClassTypeId) {
		return courseDao.getOtmClassCourseCollectList(dataPackage, startDate, endDate, organizationIdFinder,otmClassTypeId);
	}

	/**
	 * 一对多课程汇总点击已排班次查看课程详情
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param otmClassCourseVo
	 * @return
	 */
	@Override
	public DataPackage getOtmCourseInfo(DataPackage dp, String campusId,
			OtmClassCourseVo otmClassCourseVo){

		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql=new StringBuffer();
		sql.append("select occ.* ");
		sql.append(" from otm_class_course occ,otm_class oc ");
		sql.append(" where occ.OTM_CLASS_ID=oc.OTM_CLASS_ID  ");
		sql.append(" and occ.course_status <> 'CANCEL' ");
		if(StringUtils.isNotBlank(campusId)){
    		sql.append(" and oc.BL_CAMPUS_ID= :campusId ");
    		params.put("campusId", campusId);
    	}
    	if(StringUtils.isNotBlank(otmClassCourseVo.getStartDate())){
    		sql.append(" and occ.COURSE_DATE >= :startDate ");
    		params.put("startDate", otmClassCourseVo.getStartDate());
    	}
		if(StringUtils.isNotBlank(otmClassCourseVo.getEndDate())){
			sql.append(" and  occ.COURSE_DATE <= :endDate ");
			params.put("endDate", otmClassCourseVo.getEndDate());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseDate())){
			sql.append(" and occ.COURSE_DATE = :courseDate ");
			params.put("courseDate", otmClassCourseVo.getCourseDate());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseTime())){
			sql.append(" and occ.COURSE_TIME>= :courseTime ");
			params.put("courseTime", otmClassCourseVo.getCourseTime());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseEndTime())){
			sql.append(" and occ.COURSE_END_TIME<= :courseEndTime ");
			params.put("courseEndTime", otmClassCourseVo.getCourseEndTime());
		}


		if(StringUtils.isNotBlank(otmClassCourseVo.getOtmClassTypeId())){
			String[] otmClassTypes=otmClassCourseVo.getOtmClassTypeId().split(",");
			if(otmClassTypes.length>0){
//				sql.append(" and (oc.OTM_TYPE ='"+otmClassTypes[0]+"' ");
//				for (int i = 1; i < otmClassTypes.length; i++) {
//					sql.append(" or oc.OTM_TYPE ='"+otmClassTypes[i]+"' ");
//				}
//				sql.append(" )");
				sql.append(" and (oc.OTM_TYPE in ( :otmClassTypes ))");
				params.put("otmClassTypes", otmClassTypes);
			}
		}
		sql.append(" and occ.COURSE_STATUS <> 'CANCEL' ");

		sql.append(" order by occ.COURSE_DATE desc ");
		dp=otmClassCourseDao.findPageBySql(sql.toString(), dp, true,params);
    	List<OtmClassCourse> list=(List<OtmClassCourse>)dp.getDatas();
    	List<OtmClassCourseVo> voList=HibernateUtils.voListMapping(list, OtmClassCourseVo.class);
    	if(voList != null && voList.size()>0){
    		for(OtmClassCourseVo vo:voList){
    			Date courseData=DateTools.stringConversDate(vo.getCourseDate(), "yyyy-MM-dd");
    			vo.setWeekDay(DateTools.getWeekOfDate(courseData));
    			vo.setAppendCourseTime(vo.getCourseTime()+"-"+vo.getCourseEndTime());
    			if(campusId != null && StringUtils.isNotBlank(campusId)){
    				Organization campus=organizationDao.findById(campusId);
    				vo.setBlCampusName(campus.getName());
    			}
    			OtmClass otmClass=otmClassDao.findById(vo.getOtmClassId());
    			vo.setOtmClassName(otmClass.getName());
    			String sqlCount=" select * from otm_class_student_attendent where OTM_CLASS_COURSE_ID= '" + vo.getOtmClassCourseId() + "' ";
    			List<OtmClassStudentAttendent> stuAttList=otmClassStudentAttendentDao.findBySql(sqlCount, new HashMap<String, Object>());
    			vo.setStudentNums(stuAttList.size());
    			String studyManagerNam="";
    			if(stuAttList!=null && stuAttList.size()>0){
    				for(OtmClassStudentAttendent sa:stuAttList){
    					User user=sa.getStudyManager();
    					if(user!=null && !studyManagerNam.contains(user.getName())){
    						studyManagerNam += user.getName()+"，";
    					}

    				}

    			}
    			if(StringUtils.isNotBlank(studyManagerNam)){
    				vo.setStudyManagerName(studyManagerNam.substring(0, studyManagerNam.length()-1));
    			}

    		}
    	}
    	dp.setDatas(voList);

		return dp;
	}


	/**
	 * 一对多课程汇总点击已排班次查看课程详情Excel
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param otmClassCourseVo
	 * @return
	 */
	@Override
	public DataPackage excelOtmCourseInfo(DataPackage dp, String campusId,
			OtmClassCourseVo otmClassCourseVo){
		StringBuffer sql=new StringBuffer();
		sql.append("select occ.OTM_CLASS_COURSE_ID as courseId,occ.COURSE_DATE as courseDate, ");
		sql.append(" occ.COURSE_TIME as courseTime,occ.COURSE_END_TIME as courseEndTime,occ.COURSE_HOURS as courseHours, ");
		sql.append(" oc.`name` as otmClassName,oc.BL_CAMPUS_ID as blCampusId,occ.GRADE as grade,occ.`SUBJECT` as subjects, ");
		sql.append(" occ.TEACHER_ID as teacherId,ocsa.STUDY_MANAGER_ID as studyManagerId ");
		sql.append(" from otm_class_course occ,otm_class oc,otm_class_student_attendent ocsa ");
		sql.append(" where occ.OTM_CLASS_ID=oc.OTM_CLASS_ID  ");
		sql.append(" and occ.OTM_CLASS_COURSE_ID=ocsa.OTM_CLASS_COURSE_ID  ");

		Map<String, Object> params = Maps.newHashMap();

		if(StringUtils.isNotBlank(campusId)){
    		sql.append(" and oc.BL_CAMPUS_ID= :campusId ");
    		params.put("campusId", campusId);
    	}
    	if(StringUtils.isNotBlank(otmClassCourseVo.getStartDate())){
    		sql.append(" and occ.COURSE_DATE >= :startDate ");
    		params.put("startDate", otmClassCourseVo.getStartDate());
    	}
		if(StringUtils.isNotBlank(otmClassCourseVo.getEndDate())){
			sql.append(" and  occ.COURSE_DATE <= :endDate ");
			params.put("endDate", otmClassCourseVo.getEndDate());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseDate())){
			sql.append(" and occ.COURSE_DATE = :courseDate ");
			params.put("courseDate", otmClassCourseVo.getCourseDate());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseTime())){
			sql.append(" and occ.COURSE_TIME>= :courseTime ");
			params.put("courseTime", otmClassCourseVo.getCourseTime());
		}
		if(StringUtils.isNotBlank(otmClassCourseVo.getCourseEndTime())){
			sql.append(" and occ.COURSE_END_TIME<= :courseEndTime ");
			params.put("courseEndTime", otmClassCourseVo.getCourseEndTime());
		}

		if(StringUtils.isNotBlank(otmClassCourseVo.getOtmClassTypeId())){
			String[] otmClassTypes=otmClassCourseVo.getOtmClassTypeId().split(",");
			if(otmClassTypes.length>0){
//				sql.append(" and (oc.OTM_TYPE ='"+otmClassTypes[0]+"' ");
//				for (int i = 1; i < otmClassTypes.length; i++) {
//					sql.append(" or oc.OTM_TYPE ='"+otmClassTypes[i]+"' ");
//				}
//				sql.append(" )");
				sql.append(" and (oc.OTM_TYPE in ( :otmClassTypes ))");
				params.put("otmClassTypes", otmClassTypes);
			}
		}

		sql.append(" GROUP BY occ.OTM_CLASS_COURSE_ID,ocsa.STUDY_MANAGER_ID ");

		sql.append(" order by occ.COURSE_DATE desc ");

		List<Map<Object,Object>> list=otmClassCourseDao.findMapOfPageBySql(sql.toString(), dp,params);
		dp.setRowCount(otmClassCourseDao.findCountSql("select count(*) from ( " + sql.toString() + " ) countall ",params));
    	List<OtmClassCourseVo> voList=new ArrayList<OtmClassCourseVo>();
    	if(list != null && list.size()>0){
    		Map<String, Object> param = null;
    		int i = 0;
    		for(Map map:list){
    			param = Maps.newHashMap();
    			i=i+1;
    			OtmClassCourseVo vo=new OtmClassCourseVo();
    			Date courseData=DateTools.stringConversDate(map.get("courseDate").toString(), "yyyy-MM-dd");
    			vo.setWeekDay(DateTools.getWeekOfDate(courseData));
    			vo.setAppendCourseTime(map.get("courseTime").toString()+"-"+map.get("courseEndTime").toString());
    			if(map.get("blCampusId") != null){
    				Organization campus=organizationDao.findById(map.get("blCampusId").toString());
    				vo.setBlCampusName(campus.getName());
    			}
    			if(map.get("subjects") != null){
    				DataDict subject=dataDictDao.findById(map.get("subjects").toString());
    				vo.setSubjectName(subject.getName());
    			}
    			if(map.get("grade") != null){
    				DataDict grade=dataDictDao.findById(map.get("grade").toString());
    				vo.setGradeName(grade.getName());
    			}
    			if(map.get("teacherId") != null){
    				User teacher=userService.findUserById(map.get("teacherId").toString());
    				vo.setTeacherName(teacher.getName());
    			}
    			if(map.get("studyManagerId") != null ){
    				User studyManager=userService.findUserById(map.get("studyManagerId").toString());
    				vo.setStudyManagerName(studyManager.getName());
    			}

    			vo.setOtmClassName(map.get("otmClassName").toString());
    			vo.setCourseDate(map.get("courseDate").toString());
    			vo.setCourseHours(Double.valueOf(map.get("courseHours").toString()));
    			param.put("courseId"+i, map.get("courseId").toString());
    			param.put("studeyManagerId"+i, map.get("studyManagerId").toString());
    			String sqlCount=" select * from otm_class_student_attendent where OTM_CLASS_COURSE_ID= :courseId"+i+" and STUDY_MANAGER_ID= :studeyManagerId"+i+" ";
    			List<OtmClassStudentAttendent> stuAttList=otmClassStudentAttendentDao.findBySql(sqlCount,param);
    			vo.setStudentNums(stuAttList.size());
    			voList.add(vo);
    		}
    	}
    	dp.setDatas(voList);

		return dp;
	}


	/**
	 * 小班课程审批汇总
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param organizationIdFinder
	 * @param teacherId
	 * @param AuditStatus
	 * @return
	 */
	public DataPackage getMiniClassCourseAuditAnalyze(DataPackage dp,BasicOperationQueryVo vo,String AuditStatus,String anshazhesuan,String productQuarterSearch){
	    if (StringUtil.isBlank(vo.getBlCampusId())) {
	        return dp;
        }
		if ("keshi".equals(anshazhesuan)){
			dp= courseDao.getMiniClassCourseAuditAnalyze(dp, vo, AuditStatus,productQuarterSearch);
		}else {
			dp= courseDao.getMiniClassCourseAuditAnalyzeXiaoShi(dp, vo, AuditStatus,productQuarterSearch);
		}

		List<Map<String,String>> list= (List<Map<String, String>>) dp.getDatas();
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}

			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}


			newlist.add(map);
		}
		dp.setDatas(newlist);
		return dp;
	}

	/**
	 * 小班批量考勤列表
	 */
	public DataPackage miniClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String anshazhesuan,String subject,String productQuarterSearch){
		if ("keshi".equals(anshazhesuan)){
			return courseDao.miniClassCourseAuditList(dataPackage, startDate, endDate, campusId, teacherId, auditStatus,subject,productQuarterSearch);
		}else {
			return courseDao.miniClassCourseAuditListXiaoshi(dataPackage, startDate, endDate, campusId, teacherId, auditStatus,subject,productQuarterSearch);
		}
	}

	/**
	 * 小班课程审批汇总(工资)
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param miniClassTypeId
	 * @param anshazhesuan  按什么结算
     * @param subject
     * @return
     */
	public DataPackage miniClaCourseAuditAnalyzeSalary(DataPackage dp,BasicOperationQueryVo vo,String AuditStatus,String anshazhesuan,String productQuarterSearch){
	    if (StringUtil.isBlank(vo.getBrenchId())) {
	        return dp;
        }
		dp= courseDao.miniClaCourseAuditAnalyzeSalary(dp, vo, AuditStatus, anshazhesuan,productQuarterSearch);
		List<Map<String,String>> list= (List<Map<String, String>>) dp.getDatas();
		List<Map<String,String>> newlist=new ArrayList<Map<String,String>>();
		for (Map<String, String> map : list) {
			if(StringUtils.isNotBlank(map.get("teacherLevel"))){
				map.put("teacherLevelName", TeacherLevel.valueOf(map.get("teacherLevel")).getName());
			}

			if(StringUtils.isNotBlank(map.get("teacherType"))){
				map.put("teacherTypeName", TeacherType.valueOf(map.get("teacherType")).getName());
			}


			newlist.add(map);
		}
		dp.setDatas(newlist);
		return dp;
	}

	@Override
	public List<CommonClassCourseVo> getDifferentClassCourseList(String studentId, String start, String end) throws Exception {
		List<CommonClassCourseVo> classCourseList = new ArrayList<CommonClassCourseVo>();
		List<MiniClassCourseVo> miniClassCourseVoList = (List<MiniClassCourseVo>) getStudentMiniClassCourseScheduleList(studentId, DateTools.stringConversDate(start,  "yyyy-MM-dd"),  DateTools.stringConversDate(end,  "yyyy-MM-dd")).getDatas();
		Set<CommonClassCourseVo> miniClassSet = HibernateUtils.voCollectionMapping(miniClassCourseVoList, CommonClassCourseVo.class, "commonClassCourseVo_2");
		List<CourseVo> courseVoList = (List<CourseVo>) getStudentCourseScheduleList(studentId, DateTools.stringConversDate(start,  "yyyy-MM-dd"),  DateTools.stringConversDate(end,  "yyyy-MM-dd")).getDatas();
		Set<CommonClassCourseVo> oneOnOneSet = HibernateUtils.voCollectionMapping(courseVoList, CommonClassCourseVo.class, "commonClassCourseVo_1");
		List<OtmClassCourseVo> otmClassCourseVoList = (List<OtmClassCourseVo>) getStudentOtmClassCourseScheduleList(studentId, DateTools.stringConversDate(start,  "yyyy-MM-dd"),  DateTools.stringConversDate(end,  "yyyy-MM-dd")).getDatas();
		Set<CommonClassCourseVo> otmClassSet = HibernateUtils.voCollectionMapping(otmClassCourseVoList, CommonClassCourseVo.class, "commonClassCourseVo_3");

		//小班课程
		if(miniClassSet.size() > 0) {
			List<CommonClassCourseVo> miniClassList = new ArrayList<CommonClassCourseVo>(miniClassSet);
			for(CommonClassCourseVo classCourse : miniClassList) {
				//加入小班产品类型
				classCourse.setProductType(ProductType.SMALL_CLASS.getValue());
			}
			classCourseList.addAll(miniClassList);
		}
		//一对一课程
		if(oneOnOneSet.size() > 0) {
			List<CommonClassCourseVo> oneOnOneClassList = new ArrayList<CommonClassCourseVo>(oneOnOneSet);
			for(CommonClassCourseVo classCourse : oneOnOneClassList) {
				//加入产品类型
				//classCourse.setProductType(ProductType.ONE_ON_ONE_COURSE);
				//调整courseBeginTime,courseEndTime  原来如："15:00 - 17:00"
				String courseTime = classCourse.getCourseBeginTime();
				if(StringUtils.isNotBlank(courseTime)) {
					String[] courseTimeArray = courseTime.split("-");
					try{
						classCourse.setCourseBeginTime(StringUtils.trim(courseTimeArray[0]));
						classCourse.setCourseEndTime(StringUtils.trim(courseTimeArray[1]));
					}catch(Exception e) {
					}
				}
			}
			classCourseList.addAll(oneOnOneClassList);
		}
		//一对多课程
		if(otmClassSet.size() > 0) {
			List<CommonClassCourseVo> otmClassList = new ArrayList<CommonClassCourseVo>(otmClassSet);
			for(CommonClassCourseVo classCourse : otmClassList) {
				//加入小班产品类型
				classCourse.setProductType(ProductType.ONE_ON_MANY.getValue());
			}
			classCourseList.addAll(otmClassList);
		}
		return classCourseList;
	}

	@Override
	public List<CommonClassCourseVo> getCourseDetailsThatMaybeConfig(
			String courseId, String productType) {
		List<CommonClassCourseVo> courseDetailsList = new ArrayList<CommonClassCourseVo>();
		//小班课程
		if(ProductType.SMALL_CLASS.getValue().equals(productType)) {
			MiniClassCourse miniClassCourse = miniClassCourseDao.findById(courseId);
			MiniClassCourseVo miniClassCourseVo = HibernateUtils.voObjectMapping(miniClassCourse, MiniClassCourseVo.class);
			//查看冲突课程
			List<MiniClassStudentAttendent> msa = miniClassStudentAttendentDao.findByCriteria(Restrictions.eq("miniClassCourse.miniClassCourseId",miniClassCourseVo.getMiniClassCourseId()));
			String studentIds="";
			for (MiniClassStudentAttendent miniClassStudentAttendent : msa) {
				studentIds+="'"+miniClassStudentAttendent.getStudentId()+"',";
			}
			if(studentIds.length()>0){
				studentIds=studentIds.substring(0,studentIds.length()-1);
			}
			List<CourseConflict> courseConflictList = courseConflictDao.getMiniClassCourseConfictList(
					miniClassCourseVo.getMiniClassCourseId(),studentIds,
					miniClassCourseVo.getTeacherId(), miniClassCourseVo.getCourseDate(),
					miniClassCourseVo.getCourseTime().toString() + " - " + miniClassCourseVo.getCourseEndTime().toString());
			if(courseConflictList != null && courseConflictList.size() > 0){    //课程有冲突
				for(CourseConflict courseConflict : courseConflictList) {
					String conflictCourseId = courseConflict.getCourseId();
					if(courseId.equals(conflictCourseId)){  //自身记录
						miniClassCourseVo.setCrashInd(String.valueOf(courseConflictList.size() - 1));
						CommonClassCourseVo conflictCourseDetails = HibernateUtils.voObjectMapping(miniClassCourseVo, CommonClassCourseVo.class, "commonClassCourseVo_2");
						courseDetailsList.add(conflictCourseDetails);
					}else {
						CommonClassCourseVo conflictCourseDetails = getConflictCourseDetails(courseConflict);
						if(conflictCourseDetails != null) {
							conflictCourseDetails.setCrashInd(String.valueOf(courseConflictList.size() - 1));
							conflictCourseDetails.setProductType(productType);
							courseDetailsList.add(conflictCourseDetails);
						}
					}
				}
			}else {      //课程无冲突
				CommonClassCourseVo courseDetails = HibernateUtils.voObjectMapping(miniClassCourseVo, CommonClassCourseVo.class, "commonClassCourseVo_2");
				courseDetails.setCrashInd("0");
				courseDetails.setProductType(productType);
				courseDetailsList.add(courseDetails);
			}
		}
		//一对一课程
		else if(ProductType.ONE_ON_ONE_COURSE.getValue().equals(productType)
				|| ProductType.ONE_ON_ONE_COURSE_FREE.getValue().equals(productType)
				|| ProductType.ONE_ON_ONE_COURSE_NORMAL.getValue().equals(productType)){
			Course course= courseDao.findById(courseId);
			CourseVo courseVo = HibernateUtils.voObjectMapping(course, CourseVo.class, "courseFullMapping");
			courseVo.setCrashInd("0");
			List<CourseConflict> courseConflictList = courseConflictDao.getCourseConflictList(
					courseVo.getCourseId(), courseVo.getStudentId(),
					courseVo.getTeacherId(), courseVo.getCourseDate(),
					courseVo.getCourseTime());
			if(courseConflictList != null && courseConflictList.size() > 0){    //课程有冲突
				for(CourseConflict courseConflict : courseConflictList) {
					String conflictCourseId = courseConflict.getCourseId();
					if(courseId.equals(conflictCourseId)){  //自身记录
						CommonClassCourseVo conflictCourseDetails = HibernateUtils.voObjectMapping(courseVo, CommonClassCourseVo.class, "commonClassCourseVo_1");
						conflictCourseDetails.setCrashInd(String.valueOf(courseConflictList.size() - 1));
						conflictCourseDetails.setProductType(productType);
						courseDetailsList.add(conflictCourseDetails);
					}else {
						CommonClassCourseVo conflictCourseDetails = getConflictCourseDetails(courseConflict);
						if(conflictCourseDetails != null) {
							conflictCourseDetails.setCrashInd(String.valueOf(courseConflictList.size() - 1));
							conflictCourseDetails.setProductType(productType);
							courseDetailsList.add(conflictCourseDetails);
						}
					}
				}
			}else {      //课程无冲突
				CommonClassCourseVo courseDetails = HibernateUtils.voObjectMapping(courseVo, CommonClassCourseVo.class, "commonClassCourseVo_1");
				courseDetails.setCrashInd("0");
				courseDetails.setProductType(productType);
				courseDetailsList.add(courseDetails);
			}
		}
		// 一对多
		else if(ProductType.ONE_ON_MANY.getValue().equals(productType)) {
			OtmClassCourseVo queryVo = new OtmClassCourseVo();
			queryVo.setOtmClassCourseId(courseId);
			OtmClassCourseVo otmClassCourseVo = otmClassService.findOtmClassCourseById(queryVo);
			//查看冲突课程
			List<OtmClassStudentAttendent> ocsaList = otmClassStudentAttendentDao.findByCriteria(Restrictions.eq("otmClassCourse.otmClassCourseId",otmClassCourseVo.getOtmClassCourseId()));
			String studentIds="";
			for (OtmClassStudentAttendent otmClassStudentAttendent : ocsaList) {
				studentIds+="'"+otmClassStudentAttendent.getStudentId()+"',";
			}
			if(studentIds.length()>0){
				studentIds=studentIds.substring(0,studentIds.length()-1);
			}
			List<CourseConflict> courseConflictList = courseConflictDao.getMiniClassCourseConfictList(
					otmClassCourseVo.getOtmClassCourseId(),studentIds,
					otmClassCourseVo.getTeacherId(), otmClassCourseVo.getCourseDate(),
					otmClassCourseVo.getCourseTime().toString() + " - " + otmClassCourseVo.getCourseEndTime().toString());
			if(courseConflictList != null && courseConflictList.size() > 0){    //课程有冲突
				for(CourseConflict courseConflict : courseConflictList) {
					String conflictCourseId = courseConflict.getCourseId();
					if(courseId.equals(conflictCourseId)){  //自身记录
						otmClassCourseVo.setCrashInd(courseConflictList.size() - 1);
						CommonClassCourseVo conflictCourseDetails = HibernateUtils.voObjectMapping(otmClassCourseVo, CommonClassCourseVo.class, "commonClassCourseVo_3");
						courseDetailsList.add(conflictCourseDetails);
					}else {
						CommonClassCourseVo conflictCourseDetails = getConflictCourseDetails(courseConflict);
						if(conflictCourseDetails != null) {
							conflictCourseDetails.setCrashInd(String.valueOf(courseConflictList.size() - 1));
							conflictCourseDetails.setProductType(productType);
							courseDetailsList.add(conflictCourseDetails);
						}
					}
				}
			}else {      //课程无冲突
				CommonClassCourseVo courseDetails = HibernateUtils.voObjectMapping(otmClassCourseVo, CommonClassCourseVo.class, "commonClassCourseVo_3");
				courseDetails.setCrashInd("0");
				courseDetails.setProductType(productType);
				courseDetailsList.add(courseDetails);
			}
		}
		return courseDetailsList;
	}

	private CommonClassCourseVo getConflictCourseDetails(CourseConflict courseConflict) {
		MiniClass miniClass = courseConflict.getMiniClass();
		OtmClass otmClass = courseConflict.getOtmClass();
		String conflictCourseId = courseConflict.getCourseId();
		CommonClassCourseVo conflictCourseDetails = new CommonClassCourseVo();
		//和小班冲突
		if(miniClass != null) {
			MiniClassCourse miniClassCourse_conflict = miniClassCourseDao.findById(conflictCourseId);
			MiniClassCourseVo miniClassCourseVo_conflict = HibernateUtils.voObjectMapping(miniClassCourse_conflict, MiniClassCourseVo.class);
			conflictCourseDetails = HibernateUtils.voObjectMapping(miniClassCourseVo_conflict, CommonClassCourseVo.class, "commonClassCourseVo_2");
		} else if (otmClass != null) {
			OtmClassCourseVo queryVo = new OtmClassCourseVo();
			queryVo.setOtmClassCourseId(conflictCourseId);
			OtmClassCourseVo otmClassCourse_conflict = otmClassService.findOtmClassCourseById(queryVo);
			conflictCourseDetails = HibernateUtils.voObjectMapping(otmClassCourse_conflict, CommonClassCourseVo.class, "commonClassCourseVo_3");
		} else{  //和一对一冲突
			Course course_conflict = courseDao.findById(conflictCourseId);
			CourseVo courseVo_conflict = HibernateUtils.voObjectMapping(course_conflict, CourseVo.class, "courseFullMapping");
			conflictCourseDetails = HibernateUtils.voObjectMapping(courseVo_conflict, CommonClassCourseVo.class, "commonClassCourseVo_1");
		}
		return conflictCourseDetails;
	}

	/**
	 * 检查学生是否毕业，毕业就抛出异常
	* @param stu
	* @author  author :Yao
	* @date  2016年7月22日 下午6:47:16
	* @version 1.0
	*/
	public void checkStudentIsGraduation(Student stu){
		if(stu.getStatus()!=null && stu.getStatus().equals(StudentStatus.GRADUATION)){
			throw new ApplicationException("学生状态为“毕业”，相关课程操作已被禁止，请核实后再继续操作。");
		}

	}

	/**
	 * 检查已排课时是否超过剩余课时
	 * @param studentId
	 * @param courseHours
	 * @param courseSummaryId
	 * @param courseId
	 * @return
	 */
    @Override
    public boolean checkAheadOfOneOnOneRemainingHour(String studentId, BigDecimal courseHours, String courseSummaryId, String courseId) {
        BigDecimal oneOnoneNotChargeCourseHours = courseDao.countOneOnOneNotChargeCourse(studentId);
        if (StringUtil.isNotBlank(courseSummaryId)){
            CourseSummary summary = courseSummaryDao.findById(courseSummaryId);
            BigDecimal planHours = BigDecimal.valueOf(summary.getSpanHours());
            oneOnoneNotChargeCourseHours = oneOnoneNotChargeCourseHours.subtract(planHours);
        }
        if (StringUtil.isNotBlank(courseId)){
        	Course course = courseDao.findById(courseId);
        	if(course!=null) {
				BigDecimal planHours = course.getPlanHours();
				oneOnoneNotChargeCourseHours = oneOnoneNotChargeCourseHours.subtract(planHours);
			}
		}
        oneOnoneNotChargeCourseHours = oneOnoneNotChargeCourseHours.add(courseHours);
        StudnetAccMvVo studentAccoutInfo = studentService.getStudentAccoutInfo(studentId);
        BigDecimal oneOnOneRemainingHour = studentAccoutInfo.getOneOnOneRemainingHour();
        return oneOnoneNotChargeCourseHours.compareTo(oneOnOneRemainingHour)>0;
    }

    @Override
	public Map<String, Object> getCourseInfo(CourseSearchInputVo courseSearchInputVo) {
		// 备注 regionId regionName先不管 返回空
		// 满足时间区间(startDate,endDate)内的课程信息 coureseId teacherId studentId
		// 因为不同的班分在不同的表 需要根据不同的班来查询
		// 查询涉及到的合同产品都是正常或者冻结中的状态
		// Object 要将一个list转换为json格式
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuffer querySql = new StringBuffer();
		String startDate = courseSearchInputVo.getStartDate();
		String endDate = courseSearchInputVo.getEndDate();
		// 产品(班)：一对一course 一对多 otm_class 小班 mini_class 精英版 promise_class		

		List<CourseInfoVo> resultList = new ArrayList<CourseInfoVo>();
		ProductType type = courseSearchInputVo.getType();
		Map<String, Object> params = Maps.newHashMap();
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		if(type==ProductType.ONE_ON_ONE_COURSE){
			// 一对一
			querySql.append(" select p.`NAME` as courseName,c.COURSE_ID as courseId,c.STUDENT_ID as studentId,c.PRODUCT_ID as productId,concat(c.COURSE_DATE,'') as courseDate,c.COURSE_STATUS as status, ");
			querySql.append(" c.TEACHER_ID as teacherId,c.SUBJECT as subjectId,c.GRADE as gradeId,");
			querySql.append(" c.BL_CAMPUS_ID as organizationId,c.COURSE_TIME as courseTime,c.PLAN_HOURS as planHours ");
			querySql.append(" ,u.`NAME` as teacherName,d.`NAME` as subjectName,dd.`NAME` as gradeName,s.`NAME` as studentName,o.`name` as organizationName,p.CLASS_TIME_LENGTH as classTimeLength ");
			querySql.append(" ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
			querySql.append(" from course c");
			querySql.append(" left join `user` u on u.USER_ID = c.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = c.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = c.GRADE ");
			querySql.append(" left join student s on s.ID = c.STUDENT_ID ");
			querySql.append(" left join organization o on o.id = c.BL_CAMPUS_ID ");
			querySql.append(" left join organization og on og.id = o.parentID ");
			querySql.append(" left join product p on p.ID = c.PRODUCT_ID ");
			querySql.append(" left join region r on r.id = o.city_id ");
			querySql.append(" where 1=1 ");
			if (courseSearchInputVo.getCourseId() != null) {
				querySql.append(" and c.COURSE_ID = :courseId ");
				params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if (courseSearchInputVo.getStudentId() != null) {
				querySql.append(" and c.STUDENT_ID = :studentId ");
				params.put("studentId", courseSearchInputVo.getStudentId());
			}
			if (courseSearchInputVo.getTeacherId() != null) {
				querySql.append(" and c.TEACHER_ID = :teacherId ");
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and c.COURSE_DATE >= :startDate ");
			querySql.append(" and c.COURSE_DATE <= :endDate ");
			List<Map<Object, Object>> oneOnoneResult = courseDao.findMapBySql(querySql.toString(),params);
			StringBuffer query = new StringBuffer();




			for (Map<Object, Object> result : oneOnoneResult) {
				CourseInfoVo courseInfoVo  = new CourseInfoVo();
				courseInfoVo.setCourseId(String.valueOf(result.get("courseId")));
				courseInfoVo.setCourseName(result.get("courseName")!=null?result.get("courseName").toString():"");
				courseInfoVo.setClassNameTwo("");
				courseInfoVo.setType(ProductType.ONE_ON_ONE_COURSE.getValue());// 一对一
				courseInfoVo.setState(String.valueOf(result.get("status")));
				courseInfoVo.setTeacherId(String.valueOf(result.get("teacherId")));
				courseInfoVo.setTeacherName(String.valueOf(result.get("teacherName")));
				courseInfoVo.setSubjectId(String.valueOf(result.get("subjectId")));
				courseInfoVo.setSubjectName(String.valueOf(result.get("subjectName")));
				courseInfoVo.setGradeId(String.valueOf(result.get("gradeId")));
				courseInfoVo.setGradeName(String.valueOf(result.get("gradeName")));
			    courseInfoVo.setAudiencesId(String.valueOf(result.get("studentId")));// 一对一是写学生编号以及学生姓名
			    courseInfoVo.setAudiencesName(String.valueOf(result.get("studentName")));
				courseInfoVo.setOrganizationId(result.get("organizationId")!=null?result.get("organizationId").toString():"");
				courseInfoVo.setOrganizationName(result.get("organizationName")!=null?result.get("organizationName").toString():"");
				courseInfoVo.setRegionId(result.get("regionId")!=null?result.get("regionId").toString():"");
				courseInfoVo.setRegionName(result.get("regionName")!=null?result.get("regionName").toString():"");
				courseInfoVo.setCourseDate(String.valueOf(result.get("courseDate")));
				courseInfoVo.setBranchId(result.get("branchId")!=null?result.get("branchId").toString():"");
				courseInfoVo.setBranchName(result.get("branchName")!=null?result.get("branchName").toString():"");
				String courseTime = String.valueOf(result.get("courseTime"));
				if (courseTime != null) {
					courseInfoVo.setCourseTime(courseTime.split("-")[0]);
					courseInfoVo.setCourseEndTime(courseTime.split("-")[1].trim());
				}
				// planHours
				String planHours = String.valueOf(result.get("planHours"));// 用于计算period
				// courseMinutes
				String productId = String.valueOf(result.get("productId"));
				Integer courseMinutes = Integer.parseInt(result.get("classTimeLength")!=null?result.get("classTimeLength").toString():"0");
				// 如果courseMinutes==null 默认是45分钟
				if (courseMinutes==null||courseMinutes==0)
					courseMinutes = 45;
				courseInfoVo.setCourseMinutes(courseMinutes.toString());
				BigDecimal period = new BigDecimal(planHours);
				period = period.multiply(new BigDecimal(courseMinutes));// planHours * courseMinutes
				courseInfoVo.setPeriod(period.toString());

				// remainingTime
				// 一对一是计算该学生所有的合同产品的剩余
				query.append(
						" select cp.PRICE as price,case cp.PAID_STATUS WHEN 'PAID' THEN sum(cp.PAID_AMOUNT +cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT) ");
				query.append(" WHEN 'PAYING' THEN sum(cp.PAID_AMOUNT -cp.CONSUME_AMOUNT) END  as money ");
				query.append(" from contract_product cp ");
				query.append(" left join contract c on c.id=cp.contract_id ");
				query.append(" WHERE cp.TYPE='ONE_ON_ONE_COURSE' and (cp.`STATUS`  ='NORMAL' or cp.`STATUS` ='FROZEN') ");
				query.append(" and c.STUDENT_ID = '" + result.get("studentId") + "' and cp.PRODUCT_ID= :productId ");
				Map<String, Object> param = Maps.newHashMap();
				param.put("productId", productId);
				List<Map<Object, Object>> moneyResult = courseDao.findMapBySql(query.toString(),param);
				if (moneyResult != null && moneyResult.size() > 0) {
					BigDecimal money = new BigDecimal(moneyResult.get(0).get("money")!=null?moneyResult.get(0).get("money").toString():"0");
					BigDecimal price = new BigDecimal(moneyResult.get(0).get("price")!=null?moneyResult.get(0).get("price").toString():"0");
					if(moneyResult.get(0).get("money")!=null &&moneyResult.get(0).get("price")!=null){
						courseInfoVo.setRemainingTime(money.divide(price,2,BigDecimal.ROUND_HALF_UP).toString());
					}else{
						courseInfoVo.setRemainingTime("0");
					}
				}
				query.delete(0, query.length());

				courseInfoVo.setCourseNo(0);
				if (courseSearchInputVo.getCourseId() != null) {
					if(courseSearchInputVo.getCourseId().equals(courseInfoVo.getCourseId())){
						String sql = "select * from course where PRODUCT_ID ='"+productId+"' and`SUBJECT` ='"+result.get("subjectId")+"' and STUDENT_ID ='"+result.get("studentId")+"' and TEACHER_ID ='"+result.get("teacherId")+"' ORDER BY COURSE_DATE";
						List<Course> list = courseDao.findBySql(sql, new HashMap<>());
						if(list!=null && list.size()>0){
							int courseSize = list.size();
							for(int i= 0;i<courseSize;i++ ){
								if(courseInfoVo.getCourseId().equals(list.get(i).getCourseId())){
									courseInfoVo.setCourseNo(i+1);
									break;
								}
							}
						}
					}
				}
				resultList.add(courseInfoVo);
			} // end 一对一的查询
		}else if(type ==ProductType.ONE_ON_MANY){
			// 一对多
			querySql.append(" select occ.OTM_CLASS_NAME as courseName,occ.OTM_CLASS_COURSE_ID as courseId, occ.COURSE_DATE as courseDate,occ.COURSE_STATUS as status,occ.COURSE_MINUTES as courseMinutes, ");
			querySql.append(" occ.TEACHER_ID as teacherId,occ.SUBJECT as subjectId,occ.GRADE as gradeId,occ.OTM_CLASS_ID as audiencesId,occ.OTM_CLASS_NAME as audiencesName,");
			querySql.append(" oc.BL_CAMPUS_ID as organizationId,occ.COURSE_TIME as courseTime,occ.COURSE_END_TIME as courseEndTime,occ.COURSE_HOURS as courseHours,");
			querySql.append(" u.`NAME` as teacherName,d.`NAME` as subjectName,dd.`NAME` as gradeName,o.`name` as organizationName ");
			querySql.append(" ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
			querySql.append(" from otm_class_course occ LEFT JOIN otm_class_student ocs on occ.OTM_CLASS_ID = ocs.OTM_CLASS_ID ");
			querySql.append(" left join otm_class oc on occ.OTM_CLASS_ID = oc.OTM_CLASS_ID ");
			querySql.append(" left join `user` u on u.USER_ID = occ.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = occ.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = occ.GRADE ");
			querySql.append(" left join organization o on o.id = oc.BL_CAMPUS_ID ");
			querySql.append(" left join organization og on og.id = o.parentID ");
			querySql.append(" left join region r on r.id = o.city_id ");
			querySql.append(" where 1=1 ");
			if (courseSearchInputVo.getCourseId() != null) {
				querySql.append(" and occ.OTM_CLASS_COURSE_ID = :courseId ");
				params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if (courseSearchInputVo.getStudentId() != null) {
				querySql.append(" and ocs.STUDENT_ID = :studentId ");
				params.put("studentId", courseSearchInputVo.getStudentId());
			}
			if (courseSearchInputVo.getTeacherId() != null) {
				querySql.append(" and occ.TEACHER_ID = :teacherId ");
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and occ.COURSE_DATE >= :startDate ");
			querySql.append(" and occ.COURSE_DATE <= :endDate ");
			List<Map<Object, Object>> otmResult = courseDao.findMapBySql(querySql.toString(),params);
			for (Map<Object, Object> result : otmResult) {
				CourseInfoVo courseInfoVo = new CourseInfoVo();
				courseInfoVo.setCourseId(String.valueOf(result.get("courseId")));
				courseInfoVo.setCourseName(result.get("courseName")!=null?result.get("courseName").toString():"");
				courseInfoVo.setClassNameTwo("");
				courseInfoVo.setType(ProductType.ONE_ON_MANY.getValue());// 一对多
				courseInfoVo.setState(String.valueOf(result.get("status")));
				courseInfoVo.setTeacherId(String.valueOf(result.get("teacherId")));
				courseInfoVo.setTeacherName(String.valueOf(result.get("teacherName")));
				courseInfoVo.setSubjectId(String.valueOf(result.get("subjectId")));
				courseInfoVo.setSubjectName(String.valueOf(result.get("subjectName")));
				courseInfoVo.setGradeId(String.valueOf(result.get("gradeId")));
				courseInfoVo.setGradeName(String.valueOf(result.get("gradeName")));
				courseInfoVo.setAudiencesId(String.valueOf(result.get("audiencesId")));//
				courseInfoVo.setAudiencesName(String.valueOf(result.get("audiencesName")));
				courseInfoVo.setOrganizationId(String.valueOf(result.get("organizationId")));
				courseInfoVo.setOrganizationName(String.valueOf(result.get("organizationName")));
				courseInfoVo.setRegionId(result.get("regionId")!=null?result.get("regionId").toString():"");
				courseInfoVo.setRegionName(result.get("regionName")!=null?result.get("regionName").toString():"");
				courseInfoVo.setCourseDate(String.valueOf(result.get("courseDate")));
				courseInfoVo.setCourseTime(String.valueOf(result.get("courseTime")));
				courseInfoVo.setCourseEndTime(String.valueOf(result.get("courseEndTime")));
				courseInfoVo.setBranchId(result.get("branchId")!=null?result.get("branchId").toString():"");
				courseInfoVo.setBranchName(result.get("branchName")!=null?result.get("branchName").toString():"");
				String courseHours =String.valueOf(result.get("courseHours"));// 用于计算period
				String courseMinutes = String.valueOf(result.get("courseMinutes")); // courseMinutes
				if (courseMinutes.equals("null"))
					courseMinutes = "45";
				courseInfoVo.setCourseMinutes(courseMinutes);
				BigDecimal period = new BigDecimal(courseHours);
				period = period.multiply(new BigDecimal(courseMinutes));// courseHours * courseMinutes
				courseInfoVo.setPeriod(period.toString());
				courseInfoVo.setRemainingTime("");// 一对多的剩余课时是为空;
				resultList.add(courseInfoVo);
			} // end 一对多
		}else if(type ==ProductType.SMALL_CLASS || type == ProductType.ECS_CLASS){
			// 小班查询 目标班
			//精英版与小班的区分
			//select * from product where class_type_id in('DAT0000000240','DAT0000000372')----精英版只有这两种
			//通过mini_class  里面的 product_id 找到  product 的 class_type_id
			//根据入参学生的Id是否存在来区分是不是join表


			querySql.append(" select cm.ID as classroomId,cm.CLASS_ROOM as classroomName,mcc.MINI_CLASS_ID,mcc.COURSE_NUM courseNum,mcc.COURSE_NAME as courseName,MINI_CLASS_COURSE_ID as courseId, mc.PRODUCE_ID as productId, mcc.COURSE_DATE as courseDate,mcc.COURSE_STATUS as status,mcc.COURSE_MINUTES as courseMinutes, ");
			querySql.append(" mcc.TEACHER_ID as teacherId,mcc.SUBJECT as subjectId,mcc.GRADE as gradeId,mcc.MINI_CLASS_ID as audiencesId,mcc.MINI_CLASS_NAME as audiencesName,");
			querySql.append(" mc.BL_CAMPUS_ID as organizationId,mcc.COURSE_TIME as courseTime,mcc.COURSE_END_TIME as courseEndTime,mcc.COURSE_HOURS as courseHours,");
			querySql.append(" u.`NAME` as teacherName,d.`NAME` as subjectName,dd.`NAME` as gradeName,o.`name` as organizationName,p.CLASS_TYPE_ID as classTypeId ");
			querySql.append(" ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
			querySql.append(" ,mcc.COURSE_NUM as courseNo ");
			querySql.append(" from mini_class_course mcc ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getStudentId())) {
			     querySql.append(" left join mini_class_student mcs on mcc.MINI_CLASS_ID = mcs.MINI_CLASS_ID ");
			}
			querySql.append(" left join mini_class mc on mcc.MINI_CLASS_ID = mc.MINI_CLASS_ID ");
			querySql.append(" left join product p on p.ID = mc.PRODUCE_ID ");
			querySql.append(" left join `user` u on u.USER_ID = mcc.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = mcc.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = mcc.GRADE ");
			querySql.append(" left join organization o on o.id = mc.BL_CAMPUS_ID ");
			querySql.append(" left join organization og on og.id = o.parentID ");
			querySql.append(" left join region r on r.id = o.city_id ");
			querySql.append(" left join classroom_manage cm on cm.ID = mc.CLASSROOM ");
			querySql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getCourseId())){
			    querySql.append(" and mcc.MINI_CLASS_COURSE_ID = :courseId ");
			    params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getStudentId())) {
				querySql.append(" and mcs.STUDENT_ID = :studentId ");
				params.put("studentId", courseSearchInputVo.getStudentId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getTeacherId())) {
				querySql.append(" and mcc.TEACHER_ID = :teacherId ");
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and mcc.COURSE_DATE >= :startDate ");
			querySql.append(" and mcc.COURSE_DATE <= :endDate ");

			List<Map<Object, Object>> miniResult = courseDao.findMapBySql(querySql.toString(),params);

			String sql  = "select MAX(COURSE_NUM) as COURSE_NUM from mini_class_course where MINI_CLASS_ID = :classId ";

			Map<String, Object> param = new HashMap<>();
			Boolean getMaxNum = false;
			Integer maxNum = 0;


			for (Map<Object, Object> result : miniResult) {
				CourseInfoVo courseInfoVo = new CourseInfoVo();
				courseInfoVo.setClassroomId(result.get("classroomId")!=null?result.get("classroomId").toString():"");
				courseInfoVo.setClassroomName(result.get("classroomName")!=null?result.get("classroomName").toString():"");
				courseInfoVo.setCourseId(String.valueOf(result.get("courseId")));
				courseInfoVo.setCourseName(result.get("courseName")!=null?result.get("courseName").toString():"");
				courseInfoVo.setClassNameTwo("");
				if (result.get("classTypeId") != null) {
					String class_type_id = result.get("classTypeId").toString();
					if ("DAT0000000240".equals(class_type_id) || "DAT0000000372".equals(class_type_id)) {
						courseInfoVo.setType(ProductType.ECS_CLASS.getValue());// 目标班
					} else {
						courseInfoVo.setType(ProductType.SMALL_CLASS.getValue());// 小班
					}

				} else {
					courseInfoVo.setType(ProductType.SMALL_CLASS.getValue());// 小班
				}

				Object classId = result.get("MINI_CLASS_ID");

				if(classId!=null && !getMaxNum){
					param.put("classId", classId);
					List<Map<Object,Object>> maps = contractDao.findMapBySql(sql, param);
					if(maps!=null&&map.size()>0){
						Map<Object, Object> teMap  = maps.get(0);
						maxNum = (Integer)teMap.get("COURSE_NUM");
						getMaxNum = true;
						param.remove("classId");
					}
				}
				courseInfoVo.setRemainCourseCount(result.get("courseNum")!=null?(Integer)result.get("courseNum"):0);


				courseInfoVo.setState(String.valueOf(result.get("status")));
				courseInfoVo.setTeacherId(String.valueOf(result.get("teacherId")));

				courseInfoVo.setTeacherName(String.valueOf(result.get("teacherName")));
				courseInfoVo.setSubjectId(String.valueOf(result.get("subjectId")));
				courseInfoVo.setSubjectName(String.valueOf(result.get("subjectName")));
				courseInfoVo.setGradeId(String.valueOf(result.get("gradeId")));
				courseInfoVo.setGradeName(String.valueOf(result.get("gradeName")));
				courseInfoVo.setAudiencesId(String.valueOf(result.get("audiencesId")));//
				courseInfoVo.setAudiencesName(String.valueOf(result.get("audiencesName")));
				courseInfoVo.setOrganizationId(String.valueOf(result.get("organizationId")));
				courseInfoVo.setOrganizationName(String.valueOf(result.get("organizationName")));
				courseInfoVo.setRegionId(result.get("regionId")!=null?result.get("regionId").toString():"");
				courseInfoVo.setRegionName(result.get("regionName")!=null?result.get("regionName").toString():"");
				courseInfoVo.setCourseDate(String.valueOf(result.get("courseDate")));
				courseInfoVo.setCourseTime(String.valueOf(result.get("courseTime")));
				courseInfoVo.setCourseEndTime(String.valueOf(result.get("courseEndTime")));
				courseInfoVo.setBranchId(result.get("branchId")!=null?result.get("branchId").toString():"");
				courseInfoVo.setBranchName(result.get("branchName")!=null?result.get("branchName").toString():"");
				String courseHours = String.valueOf(result.get("courseHours"));// 用于计算period
				String courseMinutes = String.valueOf(result.get("courseMinutes")); // courseMinutes
				if (courseMinutes.equals("null"))
					courseMinutes = "45";
				courseInfoVo.setCourseMinutes(courseMinutes);
				BigDecimal period = new BigDecimal(courseHours);
				period = period.multiply(new BigDecimal(courseMinutes));// courseHours * courseMinutes
				courseInfoVo.setPeriod(period.toString());
				courseInfoVo.setRemainingTime("");//

				if(result.get("courseNo")!=null){
					courseInfoVo.setCourseNo((Integer)result.get("courseNo"));
				}


				resultList.add(courseInfoVo);
			} // end 小班目标班
			for(CourseInfoVo courseInfoVo:resultList){
				Integer integer = courseInfoVo.getRemainCourseCount();
				courseInfoVo.setRemainCourseCount(maxNum-integer);
			}
		}else if(type ==ProductType.TWO_TEACHER){
			//双师	 返回副班的id

			// 当类型是TWO_TEACHER双师时的逻辑如下。
			//当入参是courseId，不返回classIdTwo和teacherIdTwo，因为不能主班对副班是一对多的关系，不能唯一确定classIdTwo；
			//当入参是studentId，暂时无这样的使用场景，不需要支持； 当入参是teacherId，如果teacherId是主班老师也不返回classIdTwo和teacherIdTwo；
			//只有是副班老师才返回classIdTwo和teacherIdTwo。查询逻辑是 （拿老师先去查副班，存在就关联主班查课程 union 拿老师作为主班老师查课程）

			//先判断teacherId是否为空
			/*Boolean isClassTwoTeacher = false;
			if(StringUtil.isNotBlank(courseSearchInputVo.getTeacherId())){
				Map<String, Object> param = Maps.newHashMap();
				String sql = " select * from two_teacher_class_two where TEACHER_ID = :teacherId ";
				param.put("teacherId", courseSearchInputVo.getTeacherId());
				List<TwoTeacherClassTwo> list = twoTeacherClassTwoDao.findBySql(sql, param);
				if(list!=null && list.size()>0){
					isClassTwoTeacher = true;
				}
			}
					
			querySql.append(" select ttcc.COURSE_NAME as courseName,ttcc.CLASS_ID as classId,ttcc.COURSE_ID as courseId,ttcc.COURSE_STATUS as status,");
			querySql.append(" ttcc.TEACHER_ID as teacherId,course_u.`NAME` as teacherName,ttc.SUBJECT as subjectId,p.GRADE_ID as gradeId, ");
			querySql.append(" d.`NAME` as subjectName,dd.`NAME` as gradeName,ttc.CLASS_ID as audiencesId,ttc.NAME as audiencesName,");
			querySql.append(" ttcc.COURSE_TIME as courseTime,ttcc.COURSE_END_TIME as courseEndTime,ttcc.COURSE_HOURS as courseHours,");
			querySql.append(" ttcc.COURSE_DATE as courseDate,ttcc.COURSE_MINUTES as courseMinutes,ttcc.COURSE_NUM as courseNum ");
			querySql.append(" ,ttcc.COURSE_NUM as courseNo ");
			//querySql.append(" ,ttcc.TEACHER_ID as courseTeacherId, course_u.`NAME` as courseTeacherName");
			if(isClassTwoTeacher||StringUtils.isNotBlank(courseSearchInputVo.getStudentId())){
				querySql.append(" ,ttct.TEACHER_ID as teacherIdTwo,ttct.CLASS_TWO_ID as classIdTwo,ttct.BL_CAMPUS_ID as organizationId,");
				querySql.append(" uu.`NAME` as teacherNameTwo,o.`name` as organizationName ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
			    querySql.append(",ttct.`NAME` as classNameTwo,cm.ID as classroomId,cm.CLASS_ROOM as classroomName ");
			}

            querySql.append(" from two_teacher_class_course ttcc ");
			querySql.append(" left join two_teacher_class ttc on ttcc.CLASS_ID = ttc.CLASS_ID ");
			querySql.append(" left join product p on p.ID = ttc.PRODUCE_ID ");
			querySql.append(" left join `user` u on u.USER_ID = ttc.TEACHER_ID ");
			querySql.append(" left join `user` course_u on course_u.USER_ID = ttcc.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = ttc.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = p.GRADE_ID ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getStudentId())) {
				 querySql.append(" left join two_teacher_class_two ttct on ttct.CLASS_ID = ttcc.CLASS_ID ");
				 querySql.append(" left join two_teacher_class_student ttcs on ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID ");
				 querySql.append(" left join `user` uu on uu.USER_ID = ttct.TEACHER_ID ");
				 querySql.append(" left join organization o on o.id = ttct.BL_CAMPUS_ID ");
				 querySql.append(" left join organization og on og.id = o.parentID ");
				 querySql.append(" left join region r on r.id = o.city_id ");
				 querySql.append(" left join classroom_manage cm on cm.ID = ttct.CLASS_ROOM_ID ");
			}else if(isClassTwoTeacher){
				querySql.append(" left join two_teacher_class_two ttct on ttct.CLASS_ID = ttcc.CLASS_ID ");
				querySql.append(" left join `user` uu on uu.USER_ID = ttct.TEACHER_ID ");
				querySql.append(" left join organization o on o.id = ttct.BL_CAMPUS_ID ");
				querySql.append(" left join organization og on og.id = o.parentID ");
				querySql.append(" left join region r on r.id = o.city_id ");
				querySql.append(" left join classroom_manage cm on cm.ID = ttct.CLASS_ROOM_ID ");
			}
			
			

			querySql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getCourseId())){
			    querySql.append(" and ttcc.COURSE_ID = :courseId ");
			    params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getStudentId())) {
				querySql.append(" and ttcs.STUDENT_ID = :studentId ");
				params.put("studentId", courseSearchInputVo.getStudentId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getTeacherId())) {
				if(isClassTwoTeacher){
					querySql.append(" and (ttc.TEACHER_ID = :teacherId or ttct.TEACHER_ID = :teacherId or ttcc.TEACHER_ID = :teacherId)");
				}else{
					querySql.append(" and (ttc.TEACHER_ID = :teacherId or ttcc.TEACHER_ID = :teacherId) ");
				}
				
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and ttcc.COURSE_DATE >= :startDate ");
			querySql.append(" and ttcc.COURSE_DATE <= :endDate ");
					
			List<Map<Object, Object>> twoTeacherResult = courseDao.findMapBySql(querySql.toString(),params);*/

			querySql.append(" select ttcc.COURSE_NAME as courseName,ttcc.CLASS_ID as classId,ttcc.COURSE_ID as courseId,ttcc.COURSE_STATUS as status,");
			querySql.append(" ttcc.TEACHER_ID as teacherId,course_u.`NAME` as teacherName,ttc.SUBJECT as subjectId,p.GRADE_ID as gradeId, ");
			querySql.append(" d.`NAME` as subjectName,dd.`NAME` as gradeName,ttc.CLASS_ID as audiencesId,ttc.NAME as audiencesName,");
			querySql.append(" ttcc.COURSE_TIME as courseTime,ttcc.COURSE_END_TIME as courseEndTime,ttcc.COURSE_HOURS as courseHours,");
			querySql.append(" ttcc.COURSE_DATE as courseDate,ttcc.COURSE_MINUTES as courseMinutes,ttcc.COURSE_NUM as courseNum ");
			querySql.append(" ,ttcc.COURSE_NUM as courseNo,(select ifnull(MAX(COURSE_NUM),0) as COURSE_NUM from two_teacher_class_course where CLASS_ID = ttcc.CLASS_ID) as maxNum ");

			querySql.append(" ,'' as teacherIdTwo,'' as classIdTwo,'' as organizationId,");
			querySql.append(" '' as teacherNameTwo,'' as organizationName ,'' as regionName,'' regionId,'' as branchId,'' as branchName ");
		    querySql.append(",'' as classNameTwo,'' as classroomId,'' as classroomName ");

			querySql.append(" from two_teacher_class_course ttcc ");
			querySql.append(" left join two_teacher_class ttc on ttcc.CLASS_ID = ttc.CLASS_ID ");
			querySql.append(" left join product p on p.ID = ttc.PRODUCE_ID ");
			querySql.append(" left join `user` u on u.USER_ID = ttc.TEACHER_ID ");
			querySql.append(" left join `user` course_u on course_u.USER_ID = ttcc.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = ttc.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = p.GRADE_ID ");

			querySql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getCourseId())){
			    querySql.append(" and ttcc.COURSE_ID = :courseId ");
			    params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getTeacherId())){
				querySql.append(" and (ttc.TEACHER_ID = :teacherId or ttcc.TEACHER_ID = :teacherId) ");
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and ttcc.COURSE_DATE >= :startDate ");
			querySql.append(" and ttcc.COURSE_DATE <= :endDate ");

			querySql.append(" UNION ALL");

			querySql.append(" select ttcc.COURSE_NAME as courseName,ttcc.CLASS_ID as classId,ttcc.COURSE_ID as courseId,ttcc.COURSE_STATUS as status,");
			querySql.append(" ttcc.TEACHER_ID as teacherId,course_u.`NAME` as teacherName,ttc.SUBJECT as subjectId,p.GRADE_ID as gradeId, ");
			querySql.append(" d.`NAME` as subjectName,dd.`NAME` as gradeName,ttc.CLASS_ID as audiencesId,ttc.NAME as audiencesName,");
			querySql.append(" ttcc.COURSE_TIME as courseTime,ttcc.COURSE_END_TIME as courseEndTime,ttcc.COURSE_HOURS as courseHours,");
			querySql.append(" ttcc.COURSE_DATE as courseDate,ttcc.COURSE_MINUTES as courseMinutes,ttcc.COURSE_NUM as courseNum ");
			querySql.append(" ,ttcc.COURSE_NUM as courseNo,(select ifnull(MAX(COURSE_NUM),0) as COURSE_NUM from two_teacher_class_course where CLASS_ID = ttcc.CLASS_ID) as maxNum ");

			querySql.append(" ,ttct.TEACHER_ID as teacherIdTwo,ttct.CLASS_TWO_ID as classIdTwo,ttct.BL_CAMPUS_ID as organizationId,");
			querySql.append(" uu.`NAME` as teacherNameTwo,o.`name` as organizationName ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
		    querySql.append(",ttct.`NAME` as classNameTwo,cm.ID as classroomId,cm.CLASS_ROOM as classroomName ");

		    querySql.append(" from two_teacher_class_course ttcc ");
			querySql.append(" left join two_teacher_class ttc on ttcc.CLASS_ID = ttc.CLASS_ID ");
			querySql.append(" left join product p on p.ID = ttc.PRODUCE_ID ");
			querySql.append(" left join `user` u on u.USER_ID = ttc.TEACHER_ID ");
			querySql.append(" left join `user` course_u on course_u.USER_ID = ttcc.TEACHER_ID ");
			querySql.append(" left join data_dict d on d.ID = ttc.`SUBJECT` ");
			querySql.append(" left join data_dict dd on dd.ID = p.GRADE_ID ");

			querySql.append(" left join two_teacher_class_two ttct on ttct.CLASS_ID = ttcc.CLASS_ID ");
			querySql.append(" left join `user` uu on uu.USER_ID = ttct.TEACHER_ID ");
			querySql.append(" left join organization o on o.id = ttct.BL_CAMPUS_ID ");
			querySql.append(" left join organization og on og.id = o.parentID ");
			querySql.append(" left join region r on r.id = o.city_id ");
			querySql.append(" left join classroom_manage cm on cm.ID = ttct.CLASS_ROOM_ID ");
			querySql.append(" where 1=1 ");
			if(StringUtils.isNotBlank(courseSearchInputVo.getCourseId())){
			    querySql.append(" and ttcc.COURSE_ID = :courseId ");
			    params.put("courseId", courseSearchInputVo.getCourseId());
			}
			if(StringUtils.isNotBlank(courseSearchInputVo.getTeacherId())){
				querySql.append(" and (ttct.TEACHER_ID = :teacherId) ");
				params.put("teacherId", courseSearchInputVo.getTeacherId());
			}
			querySql.append(" and ttcc.COURSE_DATE >= :startDate ");
			querySql.append(" and ttcc.COURSE_DATE <= :endDate ");


			List<Map<Object, Object>> twoTeacherResult = courseDao.findMapBySql(querySql.toString(),params);
			twoTeacherResult = removeDuplicateMap(twoTeacherResult,"courseId");
			String sql  = "select MAX(COURSE_NUM) as COURSE_NUM from two_teacher_class_course where CLASS_ID = :classId";

			Map<String, Object> param = new HashMap<>();
			//Boolean getMaxNum = false; 
			//Integer maxNum = 0;
			for (Map<Object, Object> result :  twoTeacherResult) {
				CourseInfoVo courseInfoVo = new CourseInfoVo();
				courseInfoVo.setClassroomId(result.get("classroomId")!=null?result.get("classroomId").toString():"");
				courseInfoVo.setClassroomName(result.get("classroomName")!=null?result.get("classroomName").toString():"");
				courseInfoVo.setCourseId(String.valueOf(result.get("courseId")));
				courseInfoVo.setCourseName(result.get("courseName")!=null?result.get("courseName").toString():"");
				courseInfoVo.setClassNameTwo(result.get("classNameTwo")!=null?result.get("classNameTwo").toString():"");
				courseInfoVo.setType(ProductType.TWO_TEACHER.getValue());// 双师
				courseInfoVo.setState(String.valueOf(result.get("status")));
				Object classId = result.get("classId");
				courseInfoVo.setClassId(classId!=null?classId.toString():"");
		
				/*if(classId!=null && !getMaxNum){
					param.put("classId", classId);
					List<Map<Object,Object>> maps = contractDao.findMapBySql(sql, param);
					if(maps!=null&&maps.size()>0){
						Map<Object, Object> teMap  = maps.get(0);
						maxNum = (Integer)teMap.get("COURSE_NUM");
						getMaxNum = true;
						param.remove("classId");
					}
				}*/

				String courseNum = result.get("courseNum")!=null?result.get("courseNum").toString():"0";
				BigInteger maxNum = result.get("maxNum")!=null?(BigInteger)result.get("maxNum"):new BigInteger("0");
				courseInfoVo.setRemainCourseCount(maxNum.subtract(new BigInteger(courseNum)).intValue());
				courseInfoVo.setTeacherId(result.get("teacherId")!=null?result.get("teacherId").toString():"");
				courseInfoVo.setTeacherName(result.get("teacherName")!=null?result.get("teacherName").toString():"");
				//courseInfoVo.setCourseTeacherId(result.get("courseTeacherId")!=null?result.get("courseTeacherId").toString():"");
				//courseInfoVo.setCourseTeacherName(result.get("courseTeacherName")!=null?result.get("courseTeacherName").toString():"");
				courseInfoVo.setClassIdTwo(result.get("classIdTwo")!=null?result.get("classIdTwo").toString():"");
				courseInfoVo.setTeacherIdTwo(result.get("teacherIdTwo")!=null?result.get("teacherIdTwo").toString():"");
				courseInfoVo.setTeacherNameTwo(result.get("teacherNameTwo")!=null?result.get("teacherNameTwo").toString():"");
				courseInfoVo.setSubjectId(String.valueOf(result.get("subjectId")));
				courseInfoVo.setSubjectName(String.valueOf(result.get("subjectName")));
				courseInfoVo.setGradeId(String.valueOf(result.get("gradeId")));
				courseInfoVo.setGradeName(String.valueOf(result.get("gradeName")));
				courseInfoVo.setAudiencesId(String.valueOf(result.get("audiencesId")));//
				courseInfoVo.setAudiencesName(String.valueOf(result.get("audiencesName")));
				courseInfoVo.setOrganizationId(String.valueOf(result.get("organizationId")));
				courseInfoVo.setOrganizationName(String.valueOf(result.get("organizationName")));
				courseInfoVo.setRegionId(result.get("regionId")!=null?result.get("regionId").toString():"");
				courseInfoVo.setRegionName(result.get("regionName")!=null?result.get("regionName").toString():"");
				courseInfoVo.setCourseDate(String.valueOf(result.get("courseDate")));
				courseInfoVo.setCourseTime(String.valueOf(result.get("courseTime")));
				courseInfoVo.setCourseEndTime(String.valueOf(result.get("courseEndTime")));
				courseInfoVo.setBranchId(result.get("branchId")!=null?result.get("branchId").toString():"");
				courseInfoVo.setBranchName(result.get("branchName")!=null?result.get("branchName").toString():"");
				Object courseHours =result.get("courseHours");// 用于计算period
				Object courseMinutes = result.get("courseMinutes"); // courseMinutes
				if (courseMinutes==null)
					courseMinutes = "45";
				courseInfoVo.setCourseMinutes(courseMinutes.toString());
				BigDecimal period = new BigDecimal(courseHours.toString());
				period = period.multiply(new BigDecimal(courseMinutes.toString()));// courseHours * courseMinutes
				courseInfoVo.setPeriod(period.toString());

				if(result.get("status")!=null && result.get("status").equals(CourseStatus.NEW.getValue())){
					courseInfoVo.setRemainingTime(courseHours.toString());
				}else{
					courseInfoVo.setRemainingTime("0");
				}

				if(result.get("courseNo")!=null){
					courseInfoVo.setCourseNo((Integer)result.get("courseNo"));
				}

				resultList.add(courseInfoVo);
			} //end双师
			/*for(CourseInfoVo courseInfoVo:resultList){
				Integer integer = courseInfoVo.getRemainCourseCount();
				courseInfoVo.setRemainCourseCount(maxNum-integer);
			}*/
		}else{
			map.put("resultStatus", 400);
			map.put("resultMessage", "找不到对应课程类型的课程信息");
			map.put("result", null);
			return map;
		}
		map.put("resultStatus", 200);
		map.put("resultMessage", "课程信息");
		map.put("result", resultList);
		return map;
	}

    private List<Map<Object, Object>> removeDuplicateMap(List<Map<Object, Object>> list, String courseId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).get(courseId).equals(list.get(i).get(courseId))) {
					list.remove(j);
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, Integer> attendanceCount() throws Exception{
		//查询当前登录者(老师学管师的考勤数)
		//一对一  一对多  小班
		Map<String, Integer> map  = new HashMap<>();
		int currentDay = 0;
		int currentWeek = 0;
		int currentMonth = 0;
		User user = userService.getCurrentLoginUser();
		String userId  = user.getUserId();
		String roleCode = user.getRoleCode();
		StringBuilder query_oto  = new StringBuilder(126);
		StringBuilder query_otm  = new StringBuilder(126);
		StringBuilder query_mini  = new StringBuilder(126);
		String current_day_begin = DateTools.dateConversString(DateTools.getTimesmorning(), "yyyy-MM-dd");
		String current_day_end =  DateTools.dateConversString(DateTools.getTimesnight(),"yyyy-MM-dd");
		String current_week_begin = DateTools.dateConversString(DateTools.getTimesWeekmorning(), "yyyy-MM-dd");
		String current_week_end  = DateTools.dateConversString(DateTools.getTimesWeeknight(), "yyyy-MM-dd");
		String current_month_begin = DateTools.dateConversString(DateTools.getTimesMonthmorning(), "yyyy-MM-dd");
		String current_month_end = DateTools.dateConversString(DateTools.getTimesMonthnight(), "yyyy-MM-dd");
		if(roleCode!=null){

			Map<String, Object> params = Maps.newHashMap();
			params.put("userId", userId);
			params.put("current_day_begin", current_day_begin+" 00:00:00");
			params.put("current_day_end", current_day_end+" 00:00:00");
			params.put("current_week_begin", current_week_begin+" 00:00:00");
			params.put("current_week_end", current_week_end+" 00:00:00");
			params.put("current_month_begin", current_month_begin+" 00:00:00");
			params.put("current_month_end", current_month_end+" 00:00:00");


			if(roleCode.indexOf(RoleCode.TEATCHER.getValue())!=-1){
				//如果是老师
				//本日 一对一  一对多  小班
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHER_ID = :userId and c.TEACHING_ATTEND_TIME >= :current_day_begin and c.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.ATTENDENT_USER_ID = :userId and occ.TEACHING_ATTEND_TIME >= :current_day_begin and occ.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_mini.append(" select count(1) from mini_class_student_attendent mcsa WHERE mcsa.CHARGE_STATUS ='CHARGED' ");
				query_mini.append(" and mcsa.ATTENDENT_USER_ID = :userId and mcsa.CREATE_TIME >= :current_day_begin and mcsa.CREATE_TIME <= :current_day_end ");
			    currentDay =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params)+courseDao.findCountSql(query_mini.toString(),params);
				query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
			    query_mini.delete(0, query_mini.length());
				//本周 一对一  一对多  小班
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHER_ID = :userId and c.TEACHING_ATTEND_TIME >= :current_day_begin and c.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.ATTENDENT_USER_ID = :userId and occ.TEACHING_ATTEND_TIME >= :current_day_begin and occ.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_mini.append(" select count(1) from mini_class_student_attendent mcsa WHERE mcsa.CHARGE_STATUS ='CHARGED' ");
				query_mini.append(" and mcsa.ATTENDENT_USER_ID = :userId and mcsa.CREATE_TIME >= :current_week_begin and mcsa.CREATE_TIME <= :current_week_end ");
				currentWeek =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params)+courseDao.findCountSql(query_mini.toString(),params);
				query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
			    query_mini.delete(0, query_mini.length());
				//本月 一对一  一对多  小班
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHER_ID = :userId and c.TEACHING_ATTEND_TIME >= :current_day_begin and c.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.ATTENDENT_USER_ID = :userId and occ.TEACHING_ATTEND_TIME >= :current_day_begin and occ.TEACHING_ATTEND_TIME <= :current_day_end ");
				query_mini.append(" select count(1) from mini_class_student_attendent mcsa WHERE mcsa.CHARGE_STATUS ='CHARGED' ");
				query_mini.append(" and mcsa.ATTENDENT_USER_ID = :userId and mcsa.CREATE_TIME >= :current_day_begin and mcsa.CREATE_TIME <= :current_day_end ");
				currentMonth =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params)+courseDao.findCountSql(query_mini.toString(),params);
			    query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
			    query_mini.delete(0, query_mini.length());

			}else if (roleCode.indexOf(RoleCode.STUDY_MANAGER.getValue())!=-1){
				//如果是学管师 一对一 一对多
				//本日 一对一 一对多
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHING_MANAGER_AUDIT_ID = :userId and c.STADUY_MANAGER_AUDIT_TIME >= :current_day_begin and c.STADUY_MANAGER_AUDIT_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.STUDY_MANAGER_ID = :userId and occ.STUDY_MANAGER_CHARGE_TIME >= :current_day_begin and occ.STUDY_MANAGER_CHARGE_TIME <= :current_day_end ");
				currentDay =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params);
				query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
				//本周 一对一 一对多
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHING_MANAGER_AUDIT_ID = :userId and c.STADUY_MANAGER_AUDIT_TIME >= current_day_begin and c.STADUY_MANAGER_AUDIT_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.STUDY_MANAGER_ID = :userId and occ.STUDY_MANAGER_CHARGE_TIME >= :current_week_begin and occ.STUDY_MANAGER_CHARGE_TIME <= :current_week_end ");
				currentWeek =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params);
			    query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
				//本月 一对一 一对多
				query_oto.append(" select count(1) from course c where c.COURSE_STATUS ='CHARGED' and c.TEACHING_MANAGER_AUDIT_ID = :userId and c.STADUY_MANAGER_AUDIT_TIME >= :current_day_begin and c.STADUY_MANAGER_AUDIT_TIME <= :current_day_end ");
				query_otm.append(" select count(1) from otm_class_student_attendent ocsa left join otm_class_course occ on ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID ");
				query_otm.append(" WHERE ocsa.CHARGE_STATUS ='CHARGED' and ocsa.STUDY_MANAGER_ID = :userId and occ.STUDY_MANAGER_CHARGE_TIME >= :current_month_begin and occ.STUDY_MANAGER_CHARGE_TIME <= :current_month_end ");
				currentMonth =courseDao.findCountSql(query_oto.toString(),params)+courseDao.findCountSql(query_otm.toString(),params);
			    query_oto.delete(0, query_oto.length());
			    query_otm.delete(0, query_otm.length());
			}
			map.put("currentDay", currentDay);
			map.put("currentWeek", currentWeek);
			map.put("currentMonth", currentMonth);
			return map;
		}else{
			map.put("currentDay", currentDay);
			map.put("currentWeek", currentWeek);
			map.put("currentMonth", currentMonth);
			return map;
		}
	}

	@Override
	public Map<String, Object> getCourseStudentInfo(String courseId,String type) {
		Map<String, Object> map = new HashMap<String,Object>();
		try {
			ProductType productType =ProductType.valueOf(type);
			List<ClassStudentInfoVo> classStudentInfoVos = new ArrayList<ClassStudentInfoVo>();
			StringBuffer query = new StringBuffer();
			Map<String, Object> params = Maps.newHashMap();
			params.put("courseId", courseId);
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//查询一对一 课程的学生信息
				Course course =courseDao.findById(courseId);
				if(course!=null){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(course.getStudent().getId());
					classStudentInfoVo.setName(course.getStudent().getName());
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}else if(productType == ProductType.ONE_ON_MANY){
				//查询一对多
				query.append(" select * from otm_class_student_attendent where OTM_CLASS_COURSE_ID = :courseId ");
				List<OtmClassStudentAttendent> otmClassStudentAttendents= otmClassStudentAttendentDao.findBySql(query.toString(),params);
				for(OtmClassStudentAttendent otm:otmClassStudentAttendents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(otm.getStudentId());
					if(otm.getStudentId()!=null){
						Student student = studentDao.findById(otm.getStudentId());
						if(student!=null){
							classStudentInfoVo.setName(student.getName());
						}
					}else{
						classStudentInfoVo.setName("");
					}
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}else if(productType == ProductType.SMALL_CLASS){
				//查询小班
				query.append("SELECT * from mini_class_student_attendent  WHERE MINI_CLASS_COURSE_ID = :courseId ");
				List<MiniClassStudentAttendent> miniClassStudentAttendents = miniClassStudentAttendentDao.findBySql(query.toString(),params);
				for(MiniClassStudentAttendent mini:miniClassStudentAttendents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					classStudentInfoVo.setId(mini.getStudentId());
					if(mini.getStudentId()!=null){
						Student student = studentDao.findById(mini.getStudentId());
						if(student!=null){
							classStudentInfoVo.setName(student.getName());
						}
					}else{
						classStudentInfoVo.setName("");
					}
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}else if(productType == ProductType.TWO_TEACHER){
				//查询双师
				query.append(" select ttcs.* from two_teacher_class ttc join two_teacher_class_course ttcc on ttc.CLASS_ID = ttcc.CLASS_ID ");
				query.append(" join two_teacher_class_two ttct on ttc.CLASS_ID = ttct.CLASS_ID ");
				query.append(" join two_teacher_class_student ttcs on ttct.CLASS_TWO_ID = ttcs.CLASS_TWO_ID ");
				query.append(" where ttcc.COURSE_ID = :courseId group by ttcs.id");
				List<TwoTeacherClassStudent> twoTeacherClassStudents = twoTeacherClassStudentDao.findBySql(query.toString(),params);
				//query.append("SELECT * from two_teacher_class_student_attendent WHERE TWO_CLASS_COURSE_ID = :courseId ");
				//List<TwoTeacherClassStudentAttendent> twoTeacherClassStudentAttendents = twoTeacherAttendentDao.findBySql(query.toString(),params);
				for(TwoTeacherClassStudent twoTeacher:twoTeacherClassStudents){
					ClassStudentInfoVo classStudentInfoVo = new ClassStudentInfoVo();
					if(twoTeacher.getStudent()!=null){
						Student student = twoTeacher.getStudent();
						classStudentInfoVo.setId(student.getId());
						classStudentInfoVo.setName(student.getName());
					}else{
						classStudentInfoVo.setId("");
						classStudentInfoVo.setName("");
					}
					classStudentInfoVos.add(classStudentInfoVo);
				}
			}
			map.put("resultStatus", 200);
			map.put("resultMessage", "课程内学生信息");
			map.put("result", classStudentInfoVos);
			return map;
		} catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
			return map;
		}


	}

	/**
	 * 一对一排课时检查是否超科目分配课时
	 */
	@Override
	public boolean checkContractProductSubjectHours(String studentId, String productId, String subjectId,
			BigDecimal courseHours, String courseSummaryId, String courseId) {
		ProductVo pVo = productService.findProductById(productId);
		BigDecimal alreadyCourseHours = courseDao.sumCourseHoursByStudentProductSubject(studentId, pVo.getProductGroupId(), productId, subjectId, courseSummaryId, courseId);
		BigDecimal allCourseHours = alreadyCourseHours.add(courseHours);
		BigDecimal alreadyQuantities = contractProductSubjectService
				.sumRemainHoursByStudentProductSubject(studentId, pVo.getProductGroupId(), productId, subjectId);
		return allCourseHours.compareTo(alreadyQuantities) > 0;
	}

	/**
	 * 一对一非规律排课时检查是否超科目分配课时
	 */
	@Override
	public Response multiCheckContractProductSubjectHours(
			MultiStudentProductSubjectVo multiStudentProductSubjectVo) {
		String studentId = multiStudentProductSubjectVo.getStudentId();
		String summaryId = multiStudentProductSubjectVo.getCourseSummaryId();
		Set<ProductSubjectCourseHoursVo> set = multiStudentProductSubjectVo.getProductSubjectCourseHoursSet();
		String subjectIds = "";
		String returnSubjectName = "";
		if (set.size() > 0) {
			BigDecimal alreadyCourseHours = BigDecimal.ZERO;
			BigDecimal allCourseHours = BigDecimal.ZERO;
			BigDecimal alreadyQuantities = BigDecimal.ZERO;
			for (ProductSubjectCourseHoursVo vo : set) {
				ProductVo pVo = productService.findProductById(vo.getProductId());
				alreadyCourseHours = courseDao.sumCourseHoursByStudentProductSubject(studentId, pVo.getProductGroupId(), vo.getProductId(), vo.getSubjectId(), summaryId, null);
				allCourseHours = alreadyCourseHours.add(vo.getCourseHours());
				alreadyQuantities = contractProductSubjectService
						.sumRemainHoursByStudentProductSubject(studentId, pVo.getProductGroupId(),  vo.getProductId(), vo.getSubjectId());
				if (allCourseHours.compareTo(alreadyQuantities) > 0 && !subjectIds.contains(vo.getSubjectId())) {
					subjectIds += vo.getSubjectId() + ",";
					returnSubjectName += dataDictDao.findById(vo.getSubjectId()).getName() + ",";
				}
			}

			if (StringUtil.isNotBlank(returnSubjectName)) {
				returnSubjectName = returnSubjectName.substring(0, returnSubjectName.length() - 1);
			}
		}
		Response res = null;
		if (StringUtil.isNotBlank(returnSubjectName)) {
			res = new Response(-1, returnSubjectName);
		} else {
			res = new Response();
		}
		return res;
	}

	/**
	 * 获取课程考勤状态统计数据
	 */
	public CourseAttendaceStatusCountVo getCourseAttendaceStatusCount() {
		CourseAttendaceStatusCountVo vo = new CourseAttendaceStatusCountVo();
		List<Role> roles = userService.getCurrentLoginUserRoles();
		String roleCodes = "";
		for (Role role : roles) {
			if (role.getRoleCode() != null) {
				roleCodes += role.getRoleCode().getValue() + ",";
			}
		}
		String currentUserId = userService.getCurrentLoginUser().getUserId();

		// TODO: 2017/6/26 增加双师
		String currentDate = DateTools.getCurrentDate();
		if (roleCodes.indexOf(RoleCode.TEATCHER.getValue()) >= 0) {
			String oooNewSql = " SELECT count(1) FROM course WHERE TEACHER_ID = '" + currentUserId + "' "
					+ " AND (COURSE_STATUS = 'NEW' OR (COURSE_STATUS = 'TEACHER_ATTENDANCE' AND AUDIT_STATUS = 'UNVALIDATE')) AND COURSE_DATE <= '" + currentDate + "' ";
			int oooNewCount = courseDao.findCountSql(oooNewSql, new HashMap<String, Object>());

			String miniNewSql = " SELECT count(1) FROM mini_class_course WHERE TEACHER_ID = '" + currentUserId + "' "
					+ " AND (COURSE_STATUS = 'NEW' OR (COURSE_STATUS = 'TEACHER_ATTENDANCE' AND AUDIT_STATUS = 'UNVALIDATE')) AND COURSE_DATE <= '" + currentDate + "' ";
			int miniNewCount = courseDao.findCountSql(miniNewSql, new HashMap<String, Object>());

			String otmNewSql = " SELECT count(1) FROM otm_class_course WHERE TEACHER_ID = '" + currentUserId + "' "
					+ " AND (COURSE_STATUS = 'NEW' OR (COURSE_STATUS = 'TEACHER_ATTENDANCE' AND AUDIT_STATUS = 'UNVALIDATE')) AND COURSE_DATE <= '" + currentDate + "' ";
			int otmNewCount = courseDao.findCountSql(otmNewSql, new HashMap<String, Object>());

//			String twoNewSql = "  select count(1) from two_teacher_class_course cou  left join two_teacher_class_two two on two.class_id = cou.CLASS_ID" +
//					" left join two_teacher_class_two ttwo on ttwo.CLASS_ID=two.CLASS_ID where ttwo.teacher_id  = '" + currentUserId + "' "
//					+ " AND (cou.COURSE_STATUS = 'NEW' OR (cou.COURSE_STATUS = 'TEACHER_ATTENDANCE' AND cou.AUDIT_STATUS = 'UNVALIDATE')) AND cou.COURSE_DATE <= '" + currentDate + "' ";
//			int twoNewCount = courseDao.findCountSql(twoNewSql, new HashMap<String, Object>());

			String twoNewSql="  SELECT count(1) from ( SELECT ttcsa.id FROM two_teacher_class_student_attendent ttcsa LEFT JOIN two_teacher_class_two ttct ON ttcsa.CLASS_TWO_ID = ttct.CLASS_TWO_ID LEFT JOIN two_teacher_class_course ttcc ON ttcc.COURSE_ID = ttcsa.TWO_CLASS_COURSE_ID "+
					" WHERE ttct.TEACHER_ID= '"+currentUserId+"' "
					+" AND (ttcsa.COURSE_STATUS = 'NEW' OR (ttcsa.COURSE_STATUS = 'TEACHER_ATTENDANCE' AND ttcsa.AUDIT_STATUS='UNVALIDATE' ) ) AND ttcc.COURSE_DATE<='"+currentDate+"' GROUP BY ttcsa.TWO_CLASS_COURSE_ID, ttcsa.CLASS_TWO_ID) a ";
			int twoNewCount = courseDao.findCountSql(twoNewSql, new HashMap<String, Object>());

			vo.setNewCount(oooNewCount + miniNewCount + otmNewCount+twoNewCount);
		}
		if (roleCodes.indexOf(RoleCode.STUDY_MANAGER.getValue()) >= 0) {
			String oooAttSql = " SELECT count(1) FROM course WHERE STUDY_MANAGER_ID = '" + currentUserId + "' "
					+ " AND COURSE_STATUS = 'TEACHER_ATTENDANCE' AND AUDIT_STATUS = 'UNAUDIT' AND COURSE_DATE <= '" + currentDate + "' ";
			int oooAttCount = courseDao.findCountSql(oooAttSql, new HashMap<String, Object>());

			String otmAttSql = " SELECT COUNT(DISTINCT ocsa.OTM_CLASS_COURSE_ID) FROM otm_class_student_attendent ocsa "
					+ " LEFT JOIN otm_class_course occ ON ocsa.OTM_CLASS_COURSE_ID = occ.OTM_CLASS_COURSE_ID "
					+ " WHERE ocsa.STUDY_MANAGER_ID = '" + currentUserId + "' AND occ.COURSE_STATUS = 'TEACHER_ATTENDANCE' "
							+ " AND occ.AUDIT_STATUS = 'UNAUDIT' AND occ.COURSE_DATE <= '" + currentDate + "' ";
			int otmAttCount = courseDao.findCountSql(otmAttSql, new HashMap<String, Object>());
			vo.setTeacherAttendanceCount(oooAttCount + otmAttCount);
		}
		if (roleCodes.indexOf(RoleCode.EDUCAT_SPEC.getValue()) >= 0) {
			String belongCampusId = userService.getBelongCampus().getId();
			String oooStuSql = " SELECT count(1) FROM course WHERE BL_CAMPUS_ID = '" + belongCampusId + "' AND COURSE_STATUS = 'STUDY_MANAGER_AUDITED' ";
			int oooStuCount = courseDao.findCountSql(oooStuSql, new HashMap<String, Object>());
			vo.setStudyManagerAuditedCount(oooStuCount);
		}
		// TODO: 2017/6/26 增加双师
		return vo;
	}

	@Override
	public Map<String, Object> getClassCoursesInfo(String classId,String classIdTwo,String type) {
		Map<String, Object> map = new HashMap<String, Object>();

		try {
			ProductType productType = ProductType.valueOf(type);
			Boolean flag = false;
			if(productType == ProductType.ONE_ON_ONE_COURSE){
				//一对一
			}else if(productType == ProductType.ONE_ON_MANY){
				//一对多
			}else if(productType == ProductType.SMALL_CLASS || productType == ProductType.ECS_CLASS){
				//小班
			}else if(productType == ProductType.TWO_TEACHER){
				//双师
				flag = true;
				Map<String, Object> params = Maps.newHashMap();
				StringBuffer querySql = new StringBuffer();
				List<ClassCoursesInfoVo> result = new ArrayList<>();
				querySql.append(" select ttcc.COURSE_ID as courseId,ttcc.COURSE_NAME as courseName,ttcc.COURSE_STATUS as status,ttc.TEACHER_ID as teacherId,u.`NAME` as teacherName, ");
				querySql.append(" ttc.SUBJECT as subjectId,p.GRADE_ID as gradeId,d.`NAME` as subjectName,dd.`NAME` as gradeName,ttcc.COURSE_DATE as courseDate,ttcc.COURSE_MINUTES as courseMinutes,");
				querySql.append(" ttcc.COURSE_TIME as courseTime,ttcc.COURSE_END_TIME as courseEndTime,ttcc.COURSE_HOURS as courseHours,ttcc.COURSE_NUM as courseNum ");
				if(StringUtil.isNotBlank(classIdTwo)){
					querySql.append(" ,ttct.BL_CAMPUS_ID as organizationId,o.`name` as organizationName,r.`name` as regionName,r.id as regionId ");
				}
				querySql.append(" from two_teacher_class_course ttcc ");

				querySql.append(" left join two_teacher_class ttc on ttcc.CLASS_ID = ttc.CLASS_ID ");
				querySql.append(" left join product p on p.ID = ttc.PRODUCE_ID ");
				querySql.append(" left join `user` u on u.USER_ID = ttc.TEACHER_ID ");
				querySql.append(" left join data_dict d on d.ID = ttc.`SUBJECT` ");
				querySql.append(" left join data_dict dd on dd.ID = p.GRADE_ID ");
				if(StringUtil.isNotBlank(classIdTwo)){
					querySql.append(" left join two_teacher_class_two ttct on ttct.CLASS_ID = ttcc.CLASS_ID ");
					querySql.append(" left join organization o on o.id = ttct.BL_CAMPUS_ID ");
					querySql.append(" left join region r on r.id = o.city_id ");
				}
				querySql.append(" where 1=1 ");
				if(StringUtil.isNotBlank(classId)){
					querySql.append(" and ttc.CLASS_ID = :classId ");
					params.put("classId", classId);
				}
				if(StringUtil.isNotBlank(classIdTwo)){
					querySql.append(" and ttct.CLASS_TWO_ID = :classIdTwo ");
					params.put("classIdTwo", classIdTwo);
				}
				//remainingTime 剩余课时  dayOfWeek周几（如周五） period 该课程课时(换算成分钟)
				List<Map<Object, Object>> list = twoTeacherClassCourseDao.findMapBySql(querySql.toString(), params);
				ClassCoursesInfoVo classCoursesInfoVo = null;
				SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
				if(list!=null && list.size()>0){
					for(Map<Object, Object> tMap : list){
						classCoursesInfoVo = new ClassCoursesInfoVo();
						classCoursesInfoVo.setCourseId(tMap.get("courseId")!=null?tMap.get("courseId").toString():"");
						classCoursesInfoVo.setCourseName(tMap.get("courseName")!=null?tMap.get("courseName").toString():"");
						classCoursesInfoVo.setType(type);
						classCoursesInfoVo.setState(tMap.get("status")!=null?tMap.get("status").toString():"");
						classCoursesInfoVo.setTeacherId(tMap.get("teacherId")!=null?tMap.get("teacherId").toString():"");
						classCoursesInfoVo.setTeacherName(tMap.get("teacherName")!=null?tMap.get("teacherName").toString():"");
						classCoursesInfoVo.setSubjectId(tMap.get("subjectId")!=null?tMap.get("subjectId").toString():"");
						classCoursesInfoVo.setSubjectName(tMap.get("subjectName")!=null?tMap.get("subjectName").toString():"");
						classCoursesInfoVo.setGradeId(tMap.get("gradeId")!=null?tMap.get("gradeId").toString():"");
						classCoursesInfoVo.setGradeName(tMap.get("gradeName")!=null?tMap.get("gradeName").toString():"");
						if(StringUtil.isNotBlank(classIdTwo)){
							classCoursesInfoVo.setOrganizationId(tMap.get("organizationId")!=null?tMap.get("organizationId").toString():"");
							classCoursesInfoVo.setOrganizationName(tMap.get("organizationName")!=null?tMap.get("organizationName").toString():"");
							classCoursesInfoVo.setRegionId(tMap.get("regionId")!=null?tMap.get("regionId").toString():"");
							classCoursesInfoVo.setRegionName(tMap.get("regionName")!=null?tMap.get("regionName").toString():"");
						}
						classCoursesInfoVo.setCourseTime(tMap.get("courseTime")!=null?tMap.get("courseTime").toString():"");
						classCoursesInfoVo.setCourseEndTime(tMap.get("courseEndTime")!=null?tMap.get("courseEndTime").toString():"");
						Object courseDate = tMap.get("courseDate");
						classCoursesInfoVo.setCourseDate(courseDate!=null?courseDate.toString():"");
						Date date = null;
						if(courseDate!=null){
							try {
								date = sdf.parse(courseDate.toString());
							} catch (ParseException e) {
								date = new Date();
							}
						}else{
							date = new Date();
						}
						classCoursesInfoVo.setDayOfWeek(DateTools.getWeekOfDate(date));
						Object courseHours =tMap.get("courseHours");// 用于计算period
						Object courseMinutes = tMap.get("courseMinutes"); // courseMinutes
						if (courseMinutes==null)
							courseMinutes = "0";
						classCoursesInfoVo.setCourseMinutes(courseMinutes.toString());
						BigDecimal period = new BigDecimal(courseHours.toString());
						period = period.multiply(new BigDecimal(courseMinutes.toString()));// courseHours * courseMinutes
						classCoursesInfoVo.setPeriod(period.toString());

						if(tMap.get("status")!=null && tMap.get("status").equals(CourseStatus.NEW.getValue())){
							classCoursesInfoVo.setRemainingTime(courseHours.toString());
						}else{
							classCoursesInfoVo.setRemainingTime("0");
						}
						Object courseNum = tMap.get("courseNum");
						if(courseNum!=null){
							classCoursesInfoVo.setCourseNum(Integer.valueOf(courseNum.toString()));
						}else{
							classCoursesInfoVo.setCourseNum(0);
						}


						result.add(classCoursesInfoVo);
					}
				}
				map.put("resultStatus", 200);
				map.put("resultMessage", "班级内的讲次（课程）列表");
				map.put("result", result);
				return map;
			}

			if(!flag){
				map.put("resultStatus", 200);
				map.put("resultMessage", "目前暂时只能查询双师的情况");
				map.put("result", null);
			}
			return map;
		} catch (IllegalArgumentException e) {
			map.put("resultStatus", 400);
			map.put("resultMessage", "课程类型不对");
			map.put("result", null);
			return map;
		}
	}

    @Override
    public Map<String, Object> findTextBookIdByCourseId(String courseIds) {

        String[] ids = courseIds.split(",");
        Map<String, Object> params = Maps.newHashMap();
        params.put("ids", ids);
        Map<String, Object> map = new HashMap<String,Object>();
        StringBuffer query = new StringBuffer(128);
        //先去小班去找
        query.append(" SELECT c.MINI_CLASS_COURSE_ID as courseId,p.TEXTBOOK_ID as wareId from ");
        query.append(" (SELECT mcc.MINI_CLASS_COURSE_ID,mcc.MINI_CLASS_ID from mini_class_course mcc where mcc.MINI_CLASS_COURSE_ID in ( :ids ) ) c ");

        query.append(" LEFT JOIN mini_class mc on mc.MINI_CLASS_ID =  c.MINI_CLASS_ID ");
        query.append(" LEFT JOIN product p on p.ID = mc.PRODUCE_ID ");
        List<Map<Object, Object>> result = courseDao.findMapBySql(query.toString(),params);
        query.delete(0, query.length());
        if(result==null){
            //去一对一课程那里去查找
            query.append(" select c.COURSE_ID as courseId,p.TEXTBOOK_ID as wareId from (SELECT COURSE_ID,PRODUCT_ID from course WHERE COURSE_ID in ( :ids ) ) c ");
            query.append(" LEFT JOIN product p on p.ID = c.PRODUCT_ID ");
            //query.append(" SELECT * from product WHERE ID in ( SELECT PRODUCT_ID from course WHERE COURSE_ID in("+courseId.toString().substring(0, courseId.length()-1)+") ) ");
            result = courseDao.findMapBySql(query.toString(),params);
        }
        map.put("resultStatus", 200);
        map.put("resultMessage", "教材信息");
        map.put("result", result);
        return map;
    }

	@Override
	public Map<String, Object> getTextBookIdByCourseId(String courseIds) {
		String[] ids = courseIds.split(",");
		Map<String, Object> params = Maps.newHashMap();
		params.put("ids", ids);
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer query = new StringBuffer(128);
		//小班教材
		query.append(" SELECT c.MINI_CLASS_COURSE_ID as courseId,mc.TEXTBOOK_ID as wareId from ");
		query.append(" (SELECT mcc.MINI_CLASS_COURSE_ID,mcc.MINI_CLASS_ID from mini_class_course mcc where mcc.MINI_CLASS_COURSE_ID in ( :ids ) ) c ");
		query.append(" LEFT JOIN mini_class mc on mc.MINI_CLASS_ID =  c.MINI_CLASS_ID ");
		List<Map<Object, Object>> result = courseDao.findMapBySql(query.toString(), params);
		map.put("resultStatus", 200);
		map.put("resultMessage", "教材信息");
		map.put("result", result);
		return map;
	}

	@Override
	public DataPackage getTextBookBossList(TextBookBossVo textBookBossVo, DataPackage dataPackage) {
        // 使用 httpClient获取数据
        // 根据textBookVo封装参数
        Map<String, String> params = new HashMap<>();
        params.put("pageNum", String.valueOf(dataPackage.getPageNo()+1));
        params.put("pageSize", String.valueOf(dataPackage.getPageSize()));
        if (textBookBossVo != null) {
        	params.put("bossTag", "1");//所属集团(1-boss 0-培优) 写死必传
        	//教材名称
            if (StringUtils.isNotBlank(textBookBossVo.getKeyWord())) {
                params.put("keyWord", textBookBossVo.getKeyWord());
            }
            //年份
            if (StringUtils.isNotBlank(textBookBossVo.getYear())) {
                params.put("year", textBookBossVo.getYear());
            }
            //学段
            if (textBookBossVo.getSectionId()!=null) {
                params.put("sectionId", textBookBossVo.getSectionId().toString());
            }
            //科目
            if (textBookBossVo.getSubjectId()!=null) {
                params.put("subjectId", textBookBossVo.getSubjectId().toString());
            }
            //版本
            if (StringUtils.isNotBlank(textBookBossVo.getPublishVersion())) {
                params.put("publishVersion", textBookBossVo.getPublishVersion());
            }
            //书本信息
            if (StringUtils.isNotBlank(textBookBossVo.getBookVersion())) {
                params.put("bookVersion", textBookBossVo.getBookVersion());
            }
            //教材类型
            if (textBookBossVo.getWareType()!=null) {
                params.put("wareType", textBookBossVo.getWareType().toString());
            }
            //季度
            if (StringUtils.isNotBlank(textBookBossVo.getSeason())) {
                params.put("season", textBookBossVo.getSeason());
            }
            //适用班型
            if (StringUtils.isNotBlank(textBookBossVo.getClassTypeId())) {
                params.put("classTypeId", textBookBossVo.getClassTypeId());
            }
            //出版公司
            if (StringUtils.isNotBlank(textBookBossVo.getPressExtId())) {
                params.put("pressExtId", textBookBossVo.getPressExtId());
            }

        }
        String json = null;
        try {
        	final String listUrl = PropertiesUtils.getStringValue("bossTextBook");
        	log.info("getTextBookBossList:"+listUrl);
            long timeBegin = System.currentTimeMillis();
            json = HttpClientUtil.doGet(listUrl, params);
            long timeEnd = System.currentTimeMillis();
            log.info("getTextBookBossList-costtime:"+(timeEnd-timeBegin)+"ms");
            // 解析结果
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(json);
            if(object==null)return dataPackage;

            Integer code = (Integer) object.get("code");
            log.info("code:"+code);
            if (code!=200) {
                return dataPackage;
            }
            com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject) object.get("data");
            com.alibaba.fastjson.JSONArray jsonArray = data.getJSONArray("result");
            Integer totalCount = (Integer) data.get("totalCount");
            List<TextBookBossVo> result = null;
            if(jsonArray!=null){
            	result = JSON.parseArray(jsonArray.toJSONString(), TextBookBossVo.class);
            }
            dataPackage.setDatas(result);
            dataPackage.setRowCount(totalCount);
            log.info("result:"+result);
            return dataPackage;
        } catch (Exception e) {
            // 请求失败 则抛出返回 null
            e.printStackTrace();
            throw new ApplicationException("请求教材数据失败");
        }
    }

	@Override
	public List<SmallClassExcelVo> getSmallClassToExcel(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select mc.MINI_CLASS_ID as miniClassId,mc.name as className, d.`NAME` as productVersionName,dd.`NAME` as productQuarterName,");
		sql.append(" ddd.`NAME` as phaseName,d2.`NAME` as classTypeName,mc.PEOPLE_QUANTITY as peopleQuantity,");
		sql.append(" cm.CLASS_ROOM as classRoomName, branch.`name` branchName, o.`name` as campusName,d_grade.`NAME` as gradeName,");
		sql.append(" d_subject.`NAME` as subjectName,u.`NAME` as teacherName,u2.`NAME` as studyManagerName,");
		sql.append(" mc.CLASS_TIME as classTime,mc.EVERY_COURSE_CLASS_NUM as courseClassNum,mc.CLASS_TIME_LENGTH as classTimeLength,");
		sql.append(" mc.UNIT_PRICE as unitPrice,mc.`STATUS` as classStatus,mc.TOTAL_CLASS_HOURS as totalClassHours,mc.CONSUME as consumeClassHours,");
		sql.append(" mc.CREATE_TIME as createTime,mc.TEXTBOOK_NAME as textBookName ");
		sql.append(" from mini_class mc left join product p on 	mc.PRODUCE_ID = p.ID ");
		sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
		sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
		sql.append(" left join data_dict ddd on mc.PHASE = ddd.ID ");
		sql.append(" left join data_dict d2 on p.CLASS_TYPE_ID = d2.ID ");
		sql.append(" left join classroom_manage cm on mc.CLASSROOM = cm.ID ");
		sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
		sql.append(" left join organization branch on o.parentID = branch.id ");
		sql.append(" left join data_dict d_grade on mc.GRADE = d_grade.ID ");
		sql.append(" left join data_dict d_subject on mc.`SUBJECT` = d_subject.ID ");
		sql.append(" left join `user` u on mc.TEACHER_ID = u.USER_ID ");
		sql.append(" left join `user` u2 on mc.STUDY_MANEGER_ID = u2.USER_ID ");
		sql.append(" where 1=1 ");

		Map<String, Object> params = Maps.newHashMap();
		//筛选条件
		//开始时间		
		if(StringUtil.isNotBlank(miniClass.getStartDate())){
			sql.append(" and mc.START_DATE >= :startDate ");
			params.put("startDate", miniClass.getStartDate());
		}

		if(StringUtil.isNotBlank(miniClass.getEndDate())){
			sql.append(" and mc.START_DATE <= :endDate ");
			params.put("endDate", miniClass.getEndDate());
		}
		//小班名称
		if(StringUtil.isNotBlank(miniClass.getName())){
			sql.append(" and mc.`NAME` like :miniClassName ");
			params.put("miniClassName", "%" + miniClass.getName() + "%");
		}
		//班级类型
		if(miniClass.getMiniClassType()!=null && StringUtils.isNotEmpty(miniClass.getMiniClassType().getId())){
			sql.append(" and miniClassType.id = :miniClassTypeId ");
			params.put("miniClassTypeId", miniClass.getMiniClassType().getId());
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			sql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			sql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}
		//期
		if(miniClass.getPhase()!=null && StringUtils.isNotEmpty(miniClass.getPhase().getId())){
			sql.append(" and mc.PHASE = :phaseId ");
			params.put("phaseId", miniClass.getPhase().getId());
		}
		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			sql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

	    //学管师
		if(miniClass.getStudyManeger()!=null && StringUtils.isNotEmpty(miniClass.getStudyManeger().getUserId())){
			sql.append(" and mc.STUDY_MANEGER_ID = :studyManegerId ");
			params.put("studyManegerId", miniClass.getStudyManeger().getUserId());
		}
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization campus = userService.getBelongCampus();
			if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
				String orgLevel = campus.getOrgLevel();
				sql.append(" and o.orgLevel like '"+orgLevel+"%'" );
			}
		}


		//班级状态
		if(miniClass.getStatus() != null && StringUtils.isNotEmpty(miniClass.getStatus().getValue())) {
			sql.append(" and mc.`STATUS` = :status ");
			params.put("status", miniClass.getStatus().getValue());
		}

		//产品季度
		if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
			sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId", miniClass.getProduct().getProductQuarter().getId());
		}
		//产品年份
		if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
			sql.append(" and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		}
		//是否绑定教材 
		if(miniClass.getBindTextBook()!=null){
			if(miniClass.getBindTextBook()){
				sql.append(" and mc.TEXTBOOK_ID is not null ");
			}else {
				sql.append(" and mc.TEXTBOOK_ID is null ");
			}
		}
		//在线销售或者校区销售
		if(StringUtils.isNotBlank(miniClass.getSaleType())){
			if("1".equals(miniClass.getSaleType())){
				sql.append(" and mc.ONLINE_SALE =0 ");
			}else if("2".equals(miniClass.getSaleType())){
				sql.append(" and mc.CAMPUS_SALE =0 ");
			}
		}

		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by mc."+dataPackage.getSidx()+" "+dataPackage.getSord());
		}

		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		Map<String, String> temp = null;
		Map<String, BigDecimal> alreadyNum = null;
		Map<String, String> courseEndDate = null;
		//Map<String, String> courseTime = null;
		Map<String, BigInteger> peopleNum = null;
		Map<String, BigInteger> courseNum = null;
		Map<String, BigDecimal> unPayNum = null;
		Map<String, BigDecimal> payingNum = null;
		Map<String, BigDecimal> paidNum = null;
		Map<String, BigDecimal> closeNum = null;

        Map<String, String> mcStartDate= null;
        Map<String, String> mcStartTime = null;
        Map<String, String> mcEndTime = null;

		if(result!=null && result.size()>0){
			//开课日期
			StringBuffer query = new StringBuffer();
			query.append(" select mcc.MINI_CLASS_ID as miniClassId,min(mcc.COURSE_DATE) as courseDate,mcc.COURSE_TIME courseTime, ");
			query.append(" mcc.COURSE_END_TIME courseEndTime,max(mcc.COURSE_DATE) as courseEndDate ");
			query.append(" from mini_class_course mcc ");
			query.append(" where mcc.MINI_CLASS_ID in ( ");

			//已报名人数 peopleNum
			StringBuffer peopleQuery = new StringBuffer();
			peopleQuery.append(" select count(*) as peopleNum,mcs.MINI_CLASS_ID as miniClassId ");
			peopleQuery.append(" from  mini_class_student mcs ");
			peopleQuery.append(" where mcs.MINI_CLASS_ID in (");
			//未缴费人数 unPayNum 部分缴费人数payingNum 已缴费人数 paidNum
			StringBuffer payQuery = new StringBuffer();
			payQuery.append(" SELECT mcs.MINI_CLASS_ID as miniClassId, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='PAID' then 1 else 0 END ) as paidNum, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='UNPAY' then 1 else 0 END) as unPayNum, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='PAYING' then 1 else 0 END ) as payingNum ");
			payQuery.append(" from mini_class_student mcs,contract_product cp ");
			payQuery.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID ");
			payQuery.append(" and (cp.`STATUS` = 'ENDED' or cp.`STATUS` ='NORMAL') ");
			payQuery.append(" and mcs.MINI_CLASS_ID in ( ");
			//退费人数closeProductNum
			StringBuffer closeProductQuery = new StringBuffer();
			closeProductQuery.append(" SELECT mcs.MINI_CLASS_ID as miniClassId, ");
			closeProductQuery.append(" sum(CASE WHEN cp.`STATUS` = 'CLOSE_PRODUCT' then 1 else 0 END ) as closeNum ");
			closeProductQuery.append(" from mini_class_student mcs,contract_product cp ");
			closeProductQuery.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID ");
			closeProductQuery.append(" and mcs.MINI_CLASS_ID in ( ");

			//课次
			StringBuffer courseCount = new StringBuffer();
			courseCount.append(" select count(*) courseCount,mcc.MINI_CLASS_ID as miniClassId from mini_class_course mcc where mcc.MINI_CLASS_ID in ( ");

			//alreadyClassHours
			StringBuffer alreadyClassHours = new StringBuffer();
			alreadyClassHours.append(" SELECT mcc.MINI_CLASS_ID as miniClassId, sum(mcc.COURSE_HOURS) as alreayClassHours ");
			alreadyClassHours.append(" from mini_class_course mcc where mcc.COURSE_STATUS <> 'CANCEL' and mcc.MINI_CLASS_ID in ( ");

		    StringBuffer miniClassClassDate = new StringBuffer();
		    miniClassClassDate.append(" select mc.MINI_CLASS_ID as miniClassId,mc.START_DATE as startDate,mc.CLASS_TIME as classTime, ");
		    miniClassClassDate.append(" mc.EVERY_COURSE_CLASS_NUM as classNum, mc.CLASS_TIME_LENGTH as classTimeLength ");
		    miniClassClassDate.append(" from mini_class mc ");
		    miniClassClassDate.append(" where mc.MINI_CLASS_ID in ( ");


			StringBuffer queryId = new StringBuffer();
			for(Map<Object, Object> map: result){
				queryId.append("'"+map.get("miniClassId").toString()+"',");
			}
			query.append(queryId.substring(0, queryId.length()-1));
			query.append(" ) group by mcc.MINI_CLASS_ID ");
			courseCount.append(queryId.substring(0, queryId.length()-1));
			courseCount.append(" ) group by mcc.MINI_CLASS_ID ");
			peopleQuery.append(queryId.substring(0, queryId.length()-1));
			peopleQuery.append(" ) group by mcs.MINI_CLASS_ID ");
			payQuery.append(queryId.substring(0, queryId.length()-1));
			payQuery.append(" ) group by mcs.MINI_CLASS_ID ");
			closeProductQuery.append(queryId.substring(0, queryId.length()-1));
			closeProductQuery.append(" ) group by mcs.MINI_CLASS_ID ");

			alreadyClassHours.append(queryId.substring(0, queryId.length()-1));
			alreadyClassHours.append(" ) group by mcc.MINI_CLASS_ID ");

			miniClassClassDate.append(queryId.substring(0, queryId.length()-1));
			miniClassClassDate.append(" ) group by mc.MINI_CLASS_ID ");

			List<Map<Object, Object>> mcClassDate = miniClassDao.findMapBySql(miniClassClassDate.toString(), Maps.newHashMap());
			if(mcClassDate!=null && mcClassDate.size()>0){
            	//获取 开始日期 开始上课时间  计算上课结束时间
				mcStartDate = new HashMap<>();
				mcStartTime = new HashMap<>();
				mcEndTime = new HashMap<>();

				for(Map<Object,Object>  map : mcClassDate){
					String miniClassId = map.get("miniClassId").toString();
					mcStartDate.put(miniClassId, ""+map.get("startDate"));
					mcStartTime.put(miniClassId, ""+map.get("classTime"));
					if(map.get("classNum")!=null&&map.get("classTimeLength")!=null&& map.get("classTime")!=null){
						BigDecimal classNum=(BigDecimal)map.get("classNum");
						Integer classTimeLength = (Integer)map.get("classTimeLength");
						Integer delta = classNum.multiply(new BigDecimal(classTimeLength)).intValue();
						String endTime = null;
						try {
							endTime = DateTools.timeAddMinutes(map.get("classTime")+":00",delta);
						} catch (ParseException e) {
							endTime =map.get("classTime").toString();
						}
						mcEndTime.put(miniClassId, endTime);
					}

				}



            }





			List<Map<Object, Object>> subResult = miniClassCourseDao.findMapBySql(query.toString(),Maps.newHashMap());
			List<Map<Object, Object>> alreadyResult = miniClassCourseDao.findMapBySql(alreadyClassHours.toString(),Maps.newHashMap());
			List<Map<Object, Object>> courseResult = miniClassCourseDao.findMapBySql(courseCount.toString(),Maps.newHashMap());
			List<Map<Object, Object>> peopleResult = miniClassCourseDao.findMapBySql(peopleQuery.toString(),Maps.newHashMap());
			List<Map<Object, Object>> payResult = miniClassCourseDao.findMapBySql(payQuery.toString(),Maps.newHashMap());
			List<Map<Object, Object>> closeResult = miniClassCourseDao.findMapBySql(closeProductQuery.toString(),Maps.newHashMap());
			if(subResult!=null && subResult.size()>0){
				temp =  new HashMap<>();
				courseEndDate = new HashMap<>();
				//courseTime = new HashMap<>();
				for(Map<Object,Object>  map : subResult){
					temp.put(map.get("miniClassId").toString(), ""+map.get("courseDate"));
					courseEndDate.put(map.get("miniClassId").toString(), ""+map.get("courseEndDate"));
					//courseTime.put(map.get("miniClassId").toString(), ""+map.get("courseTime")+"-"+map.get("courseEndTime"));
				}
			}
			if(alreadyResult!=null&& alreadyResult.size()>0){
				alreadyNum = new HashMap<>();
				for(Map<Object, Object> map:courseResult){
					alreadyNum.put(map.get("miniClassId").toString(), (BigDecimal)map.get("alreayClassHours"));
				}
		    }


			if(courseResult!=null&& courseResult.size()>0){
				courseNum = new HashMap<>();
				for(Map<Object, Object> map:courseResult){
					courseNum.put(map.get("miniClassId").toString(), (BigInteger)map.get("courseCount"));
				}
		    }
			if(peopleResult!=null&& peopleResult.size()>0){
				peopleNum = new HashMap<>();
				for(Map<Object, Object> map:peopleResult){
					peopleNum.put(map.get("miniClassId").toString(), (BigInteger)map.get("peopleNum"));
				}
		    }
			if(payResult!=null && payResult.size()>0){
				payingNum =  new HashMap<>();
				unPayNum = new HashMap<>();
				paidNum = new HashMap<>();
				for(Map<Object,Object>  map : payResult){
					payingNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("payingNum") );
					unPayNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("unPayNum") );
					paidNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("paidNum") );
				}
			}
			if(closeResult!=null && closeResult.size()>0){
				closeNum =  new HashMap<>();
				for(Map<Object,Object>  map : closeResult){
					closeNum.put(map.get("miniClassId").toString(), (BigDecimal)map.get("closeNum"));
				}
			}
		}



		List<SmallClassExcelVo> list = new ArrayList<>();
		SmallClassExcelVo smallClassExcelVo = null;
		for(Map<Object, Object> map: result){
			smallClassExcelVo = new SmallClassExcelVo();
			String miniClassId = map.get("miniClassId").toString();

			smallClassExcelVo.setClassName(map.get("className")!=null?map.get("className").toString():"");
			smallClassExcelVo.setProductVersionName(map.get("productVersionName")!=null?map.get("productVersionName").toString():"");
			smallClassExcelVo.setProductQuarterName(map.get("productQuarterName")!=null?map.get("productQuarterName").toString():"");
			smallClassExcelVo.setPhaseName(map.get("phaseName")!=null?map.get("phaseName").toString():"");
			smallClassExcelVo.setClassTypeName(map.get("classTypeName")!=null?map.get("classTypeName").toString():"");
			smallClassExcelVo.setPeopleQuantity(map.get("peopleQuantity")!=null?new BigDecimal(map.get("peopleQuantity").toString()):new BigDecimal("0"));

			//已报名人数 peopleNum
			if(peopleNum!=null){
				if(peopleNum.get(miniClassId)!=null){
					smallClassExcelVo.setPeopleNum(new BigDecimal(peopleNum.get(miniClassId)));
				}else{
					smallClassExcelVo.setPeopleNum(new BigDecimal("0"));
				}

			}
			//部分缴费人数payingNum
			if(payingNum!=null){
				smallClassExcelVo.setPayingNum(payingNum.get(miniClassId)!=null?payingNum.get(miniClassId):new BigDecimal("0"));
			}
			//未缴费人数 unPayNum
			if(unPayNum!=null){
				smallClassExcelVo.setUnPayNum(unPayNum.get(miniClassId)!=null?unPayNum.get(miniClassId):new BigDecimal("0"));
			}
			//已缴费人数 paidNum
			if(paidNum!=null){
				smallClassExcelVo.setPaidNum(paidNum.get(miniClassId)!=null?paidNum.get(miniClassId):new BigDecimal("0"));
			}
			//退费人数closeProductNum
			if(closeNum!=null){
				smallClassExcelVo.setCloseProductNum(closeNum.get(miniClassId)!=null?closeNum.get(miniClassId):new BigDecimal("0"));
			}

			smallClassExcelVo.setBranchName(map.get("branchName")!=null?map.get("branchName").toString():"");
			smallClassExcelVo.setCampusName(map.get("campusName")!=null?map.get("campusName").toString():"");
			smallClassExcelVo.setClassRoomName(map.get("classRoomName")!=null?map.get("classRoomName").toString():"");
			smallClassExcelVo.setGradeName(map.get("gradeName")!=null?map.get("gradeName").toString():"");
			smallClassExcelVo.setSubjectName(map.get("subjectName")!=null?map.get("subjectName").toString():"");
			smallClassExcelVo.setTeacherName(map.get("teacherName")!=null?map.get("teacherName").toString():"");
			smallClassExcelVo.setStudyManagerName(map.get("studyManagerName")!=null?map.get("studyManagerName").toString():"");
			//上课日期 classDate
			if(temp!=null && mcStartDate!=null){
				if(temp.get(miniClassId)!=null){
					smallClassExcelVo.setClassDate(temp.get(miniClassId));
				}else if(mcStartDate.get(miniClassId)!=null){
					smallClassExcelVo.setClassDate(mcStartDate.get(miniClassId));
				}
			}
			if(courseEndDate!=null){
				smallClassExcelVo.setClassEndDate(courseEndDate.get(miniClassId));
			}
			if(mcStartTime!=null){
//				if(courseTime.get(miniClassId)!=null){
//					smallClassExcelVo.setClassTime(courseTime.get(miniClassId));
//				}else if(mcStartTime.get(miniClassId)!=null && mcEndTime.get(miniClassId)!=null){
//					smallClassExcelVo.setClassTime(mcStartTime.get(miniClassId)+"-"+mcEndTime.get(miniClassId));
//				}

				if(mcStartTime.get(miniClassId)!=null && mcEndTime.get(miniClassId)!=null){
					smallClassExcelVo.setClassTime(mcStartTime.get(miniClassId)+"-"+mcEndTime.get(miniClassId));
				}
			}


			smallClassExcelVo.setCourseClassNum(map.get("courseClassNum")!=null?new BigDecimal(map.get("courseClassNum").toString()):new BigDecimal("0"));
			smallClassExcelVo.setClassTimeLength(map.get("classTimeLength")!=null?new BigDecimal(map.get("classTimeLength").toString()):new BigDecimal("0"));
			smallClassExcelVo.setUnitPrice(map.get("unitPrice")!=null?new BigDecimal(map.get("unitPrice").toString()):new BigDecimal("0"));

			Object classStatus = map.get("classStatus");
			if(classStatus!=null){
				smallClassExcelVo.setClassStatus(MiniClassStatus.valueOf(classStatus.toString()).getName());
			}
			smallClassExcelVo.setTotalClassHours(map.get("totalClassHours")!=null?new BigDecimal(map.get("totalClassHours").toString()):new BigDecimal("0"));
			smallClassExcelVo.setConsumeClassHours(map.get("consumeClassHours")!=null?new BigDecimal(map.get("consumeClassHours").toString()):new BigDecimal("0"));
			smallClassExcelVo.setCreateTime(map.get("createTime")!=null?map.get("createTime").toString():"");
			smallClassExcelVo.setTextBookName(map.get("textBookName")!=null?map.get("textBookName").toString():"");


			//Double alreadyClassHours = miniClassCourseDao.findCountClassHours(miniClassId);
			if(alreadyNum!=null){
				smallClassExcelVo.setAlreadyClassHours(alreadyNum.get(miniClassId)!=null?alreadyNum.get(miniClassId):new BigDecimal("0"));
			}


			//每讲长度（小时）courseTimeLength =courseClassNum*classTimeLength/60
			BigDecimal courseTimeLenth = smallClassExcelVo.getCourseClassNum().multiply(smallClassExcelVo.getClassTimeLength())
					.divide(new BigDecimal("60"), 2, BigDecimal.ROUND_HALF_UP);
			smallClassExcelVo.setCourseTimeLength(courseTimeLenth);
			//每讲单价 coursePrice = courseClassNum*unitPrice
			smallClassExcelVo.setCoursePrice(smallClassExcelVo.getCourseClassNum().multiply(smallClassExcelVo.getUnitPrice()));
			//课次 courseCount
			if(courseNum!=null){
				if(courseNum.get(miniClassId)!=null){
					smallClassExcelVo.setCourseCount(new BigDecimal(courseNum.get(miniClassId)));
				}else{
					smallClassExcelVo.setCourseCount(new BigDecimal("0"));
				}

			}

			//金额（不含资料费） classAmount =totalClassHours*unitPrice
			smallClassExcelVo.setClassAmount(smallClassExcelVo.getTotalClassHours().multiply(smallClassExcelVo.getUnitPrice()));

			list.add(smallClassExcelVo);
		}

		return list;
	}

	@Override
	public List<List<Object>> getSmallClassToCSV(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select mc.MINI_CLASS_ID as miniClassId,mc.name as className, d.`NAME` as productVersionName,dd.`NAME` as productQuarterName,");
		sql.append(" ddd.`NAME` as phaseName,d2.`NAME` as classTypeName,mc.PEOPLE_QUANTITY as peopleQuantity,");
		sql.append(" cm.CLASS_ROOM as classRoomName,o.`name` as campusName,d_grade.`NAME` as gradeName,");
		sql.append(" d_subject.`NAME` as subjectName,u.`NAME` as teacherName,u2.`NAME` as studyManagerName,");
		sql.append(" mc.CLASS_TIME as classTime,mc.EVERY_COURSE_CLASS_NUM as courseClassNum,mc.CLASS_TIME_LENGTH as classTimeLength,");
		sql.append(" mc.UNIT_PRICE as unitPrice,mc.`STATUS` as classStatus,mc.TOTAL_CLASS_HOURS as totalClassHours,mc.CONSUME as consumeClassHours,");
		sql.append(" mc.CREATE_TIME as createTime,mc.TEXTBOOK_NAME as textBookName ");
		sql.append(" from mini_class mc left join product p on 	mc.PRODUCE_ID = p.ID ");
		sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
		sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
		sql.append(" left join data_dict ddd on mc.PHASE = ddd.ID ");
		sql.append(" left join data_dict d2 on p.CLASS_TYPE_ID = d2.ID ");
		sql.append(" left join classroom_manage cm on mc.CLASSROOM = cm.ID ");
		sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
		sql.append(" left join data_dict d_grade on mc.GRADE = d_grade.ID ");
		sql.append(" left join data_dict d_subject on mc.`SUBJECT` = d_subject.ID ");
		sql.append(" left join `user` u on mc.TEACHER_ID = u.USER_ID ");
		sql.append(" left join `user` u2 on mc.STUDY_MANEGER_ID = u2.USER_ID ");
		sql.append(" where 1=1 ");

		Map<String, Object> params = Maps.newHashMap();
		//筛选条件
		//开始时间		
		if(StringUtil.isNotBlank(miniClass.getStartDate())){
			sql.append(" and mc.START_DATE >= :startDate ");
			params.put("startDate", miniClass.getStartDate());
		}

		if(StringUtil.isNotBlank(miniClass.getEndDate())){
			sql.append(" and mc.START_DATE <= :endDate ");
			params.put("endDate", miniClass.getEndDate());
		}
		//小班名称
		if(StringUtil.isNotBlank(miniClass.getName())){
			sql.append(" and mc.`NAME` like :miniClassName ");
			params.put("miniClassName", "%" + miniClass.getName() + "%");
		}
		//班级类型
		if(miniClass.getMiniClassType()!=null && StringUtils.isNotEmpty(miniClass.getMiniClassType().getId())){
			sql.append(" and miniClassType.id = :miniClassTypeId ");
			params.put("miniClassTypeId", miniClass.getMiniClassType().getId());
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			sql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			sql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}
		//期
		if(miniClass.getPhase()!=null && StringUtils.isNotEmpty(miniClass.getPhase().getId())){
			sql.append(" and mc.PHASE = :phaseId ");
			params.put("phaseId", miniClass.getPhase().getId());
		}
		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			sql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

	    //学管师
		if(miniClass.getStudyManeger()!=null && StringUtils.isNotEmpty(miniClass.getStudyManeger().getUserId())){
			sql.append(" and mc.STUDY_MANEGER_ID = :studyManegerId ");
			params.put("studyManegerId", miniClass.getStudyManeger().getUserId());
		}
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization campus = userService.getBelongCampus();
			if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
				String orgLevel = campus.getOrgLevel();
				sql.append(" and o.orgLevel like '"+orgLevel+"%'" );
			}
		}


		//班级状态
		if(miniClass.getStatus() != null && StringUtils.isNotEmpty(miniClass.getStatus().getValue())) {
			sql.append(" and mc.`STATUS` = :status ");
			params.put("status", miniClass.getStatus().getValue());
		}

		//产品季度
		if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
			sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId", miniClass.getProduct().getProductQuarter().getId());
		}
		//产品年份
		if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
			sql.append(" and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		}
		//是否绑定教材 
		if(miniClass.getBindTextBook()!=null){
			if(miniClass.getBindTextBook()){
				sql.append(" and mc.TEXTBOOK_ID is not null ");
			}else {
				sql.append(" and mc.TEXTBOOK_ID is null ");
			}
		}
		//在线销售或者校区销售
		if(StringUtils.isNotBlank(miniClass.getSaleType())){
			if("1".equals(miniClass.getSaleType())){
				sql.append(" and mc.ONLINE_SALE =0 ");
			}else if("2".equals(miniClass.getSaleType())){
				sql.append(" and mc.CAMPUS_SALE =0 ");
			}
		}

		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by mc."+dataPackage.getSidx()+" "+dataPackage.getSord());
		}

		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		Map<String, String> temp = null;
		Map<String, BigDecimal> alreadyNum = null;
		Map<String, String> courseEndDate = null;
		Map<String, String> courseTime = null;
		Map<String, BigInteger> peopleNum = null;
		Map<String, BigInteger> courseNum = null;
		Map<String, BigDecimal> unPayNum = null;
		Map<String, BigDecimal> payingNum = null;
		Map<String, BigDecimal> paidNum = null;
		Map<String, BigDecimal> closeNum = null;

		if(result!=null && result.size()>0){
			//开课日期
			StringBuffer query = new StringBuffer();
			query.append(" select mcc.MINI_CLASS_ID as miniClassId,min(mcc.COURSE_DATE) as courseDate,mcc.COURSE_TIME courseTime, ");
			query.append(" mcc.COURSE_END_TIME courseEndTime,max(mcc.COURSE_DATE) as courseEndDate ");
			query.append(" from mini_class_course mcc ");
			query.append(" where mcc.MINI_CLASS_ID in ( ");

			//已报名人数 peopleNum
			StringBuffer peopleQuery = new StringBuffer();
			peopleQuery.append(" select count(*) as peopleNum,mcs.MINI_CLASS_ID as miniClassId ");
			peopleQuery.append(" from  mini_class_student mcs ");
			peopleQuery.append(" where mcs.MINI_CLASS_ID in (");
			//未缴费人数 unPayNum 部分缴费人数payingNum 已缴费人数 paidNum
			StringBuffer payQuery = new StringBuffer();
			payQuery.append(" SELECT mcs.MINI_CLASS_ID as miniClassId, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='PAID' then 1 else 0 END ) as paidNum, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='UNPAY' then 1 else 0 END) as unPayNum, ");
			payQuery.append(" sum(CASE WHEN cp.PAID_STATUS ='PAYING' then 1 else 0 END ) as payingNum ");
			payQuery.append(" from mini_class_student mcs,contract_product cp ");
			payQuery.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID ");
			payQuery.append(" and (cp.`STATUS` = 'ENDED' or cp.`STATUS` ='NORMAL') ");
			payQuery.append(" and mcs.MINI_CLASS_ID in ( ");
			//退费人数closeProductNum
			StringBuffer closeProductQuery = new StringBuffer();
			closeProductQuery.append(" SELECT mcs.MINI_CLASS_ID as miniClassId, ");
			closeProductQuery.append(" sum(CASE WHEN cp.`STATUS` = 'CLOSE_PRODUCT' then 1 else 0 END ) as closeNum ");
			closeProductQuery.append(" from mini_class_student mcs,contract_product cp ");
			closeProductQuery.append(" where mcs.CONTRACT_PRODUCT_ID = cp.ID ");
			closeProductQuery.append(" and mcs.MINI_CLASS_ID in ( ");

			//课次
			StringBuffer courseCount = new StringBuffer();
			courseCount.append(" select count(*) courseCount,mcc.MINI_CLASS_ID as miniClassId from mini_class_course mcc where mcc.MINI_CLASS_ID in ( ");

			//alreadyClassHours
			StringBuffer alreadyClassHours = new StringBuffer();
			alreadyClassHours.append(" SELECT mcc.MINI_CLASS_ID as miniClassId, sum(mcc.COURSE_HOURS) as alreayClassHours ");
			alreadyClassHours.append(" from mini_class_course mcc where mcc.COURSE_STATUS <> 'CANCEL' and mcc.MINI_CLASS_ID in ( ");




			StringBuffer queryId = new StringBuffer();
			for(Map<Object, Object> map: result){
				queryId.append("'"+map.get("miniClassId").toString()+"',");
			}
			query.append(queryId.substring(0, queryId.length()-1));
			query.append(" ) group by mcc.MINI_CLASS_ID ");
			courseCount.append(queryId.substring(0, queryId.length()-1));
			courseCount.append(" ) group by mcc.MINI_CLASS_ID ");
			peopleQuery.append(queryId.substring(0, queryId.length()-1));
			peopleQuery.append(" ) group by mcs.MINI_CLASS_ID ");
			payQuery.append(queryId.substring(0, queryId.length()-1));
			payQuery.append(" ) group by mcs.MINI_CLASS_ID ");
			closeProductQuery.append(queryId.substring(0, queryId.length()-1));
			closeProductQuery.append(" ) group by mcs.MINI_CLASS_ID ");

			alreadyClassHours.append(queryId.substring(0, queryId.length()-1));
			alreadyClassHours.append(" ) group by mcc.MINI_CLASS_ID ");

			List<Map<Object, Object>> subResult = miniClassCourseDao.findMapBySql(query.toString(),Maps.newHashMap());
			List<Map<Object, Object>> alreadyResult = miniClassCourseDao.findMapBySql(alreadyClassHours.toString(),Maps.newHashMap());
			List<Map<Object, Object>> courseResult = miniClassCourseDao.findMapBySql(courseCount.toString(),Maps.newHashMap());
			List<Map<Object, Object>> peopleResult = miniClassCourseDao.findMapBySql(peopleQuery.toString(),Maps.newHashMap());
			List<Map<Object, Object>> payResult = miniClassCourseDao.findMapBySql(payQuery.toString(),Maps.newHashMap());
			List<Map<Object, Object>> closeResult = miniClassCourseDao.findMapBySql(closeProductQuery.toString(),Maps.newHashMap());
			if(subResult!=null && subResult.size()>0){
				temp =  new HashMap<>();
				courseEndDate = new HashMap<>();
				courseTime = new HashMap<>();
				for(Map<Object,Object>  map : subResult){
					temp.put(map.get("miniClassId").toString(), ""+map.get("courseDate"));
					courseEndDate.put(map.get("miniClassId").toString(), ""+map.get("courseEndDate"));
					courseTime.put(map.get("miniClassId").toString(), ""+map.get("courseTime")+"-"+map.get("courseEndTime"));
				}
			}

			if(alreadyResult!=null&& alreadyResult.size()>0){
				alreadyNum = new HashMap<>();
				for(Map<Object, Object> map:courseResult){
					alreadyNum.put(map.get("miniClassId").toString(), (BigDecimal)map.get("alreayClassHours"));
				}
		    }

			if(courseResult!=null&& courseResult.size()>0){
				courseNum = new HashMap<>();
				for(Map<Object, Object> map:courseResult){
					courseNum.put(map.get("miniClassId").toString(), (BigInteger)map.get("courseCount"));
				}
		    }
			if(peopleResult!=null&& peopleResult.size()>0){
				peopleNum = new HashMap<>();
				for(Map<Object, Object> map:peopleResult){
					peopleNum.put(map.get("miniClassId").toString(), (BigInteger)map.get("peopleNum"));
				}
		    }
			if(payResult!=null && payResult.size()>0){
				payingNum =  new HashMap<>();
				unPayNum = new HashMap<>();
				paidNum = new HashMap<>();
				for(Map<Object,Object>  map : payResult){
					payingNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("payingNum") );
					unPayNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("unPayNum") );
					paidNum.put(map.get("miniClassId").toString(),(BigDecimal)map.get("paidNum") );
				}
			}
			if(closeResult!=null && closeResult.size()>0){
				closeNum =  new HashMap<>();
				for(Map<Object,Object>  map : closeResult){
					closeNum.put(map.get("miniClassId").toString(), (BigDecimal)map.get("closeNum"));
				}
			}
		}



		List<List<Object>> dataList = new ArrayList<List<Object>>();
		List<Object> rowList = null;
		for(Map<Object, Object> map: result){

			rowList = new ArrayList<>();

			//属性要有顺序put进去
			String miniClassId = map.get("miniClassId").toString();
			rowList.add(map.get("className")!=null?map.get("className"):"");
			rowList.add(map.get("productVersionName")!=null?map.get("productVersionName"):"");
			rowList.add(map.get("productQuarterName")!=null?map.get("productQuarterName"):"");
			rowList.add(map.get("phaseName")!=null?map.get("phaseName"):"");
			rowList.add(map.get("classTypeName")!=null?map.get("classTypeName"):"");
			rowList.add(map.get("peopleQuantity")!=null?new BigDecimal(map.get("peopleQuantity").toString()):new BigDecimal("0"));
			//已报名人数 peopleNum
			if(peopleNum!=null){
				if(peopleNum.get(miniClassId)!=null){
					rowList.add(peopleNum.get(miniClassId));
				}else{
					rowList.add("0");
				}
			}else{
				rowList.add("0");
			}
			//未缴费人数 unPayNum
			if(unPayNum!=null){
				rowList.add(unPayNum.get(miniClassId)!=null?unPayNum.get(miniClassId):new BigDecimal("0"));
			}else{
				rowList.add(new BigDecimal("0"));
			}
			//部分缴费人数payingNum
			if(payingNum!=null){
				rowList.add(payingNum.get(miniClassId)!=null?payingNum.get(miniClassId):new BigDecimal("0"));
			}else {
				rowList.add(new BigDecimal("0"));
			}
			//已缴费人数 paidNum
			if(paidNum!=null){
				rowList.add(paidNum.get(miniClassId)!=null?paidNum.get(miniClassId):new BigDecimal("0"));
			}else{
				rowList.add(new BigDecimal("0"));
			}


			//退费人数closeProductNum
			if(closeNum!=null){
				rowList.add(closeNum.get(miniClassId)!=null?closeNum.get(miniClassId):new BigDecimal("0"));
			}else{
				rowList.add(new BigDecimal("0"));
			}
			rowList.add(map.get("campusName")!=null?map.get("campusName"):"");
			rowList.add(map.get("classRoomName")!=null?map.get("classRoomName"):"");

			rowList.add(map.get("gradeName")!=null?map.get("gradeName").toString():"");
			rowList.add(map.get("subjectName")!=null?map.get("subjectName").toString():"");

			rowList.add(map.get("teacherName")!=null?map.get("teacherName"):"");
			rowList.add(map.get("studyManagerName")!=null?map.get("studyManagerName"):"");

			//上课日期 classDate
			if(temp!=null){
				rowList.add(temp.get(miniClassId)!=null?temp.get(miniClassId):"");
			}else{
				rowList.add("");
			}
			if(courseEndDate!=null){
				rowList.add(courseEndDate.get(miniClassId)!=null?courseEndDate.get(miniClassId):"");
			}else{
				rowList.add("");
			}

			if(courseTime!=null){
				rowList.add(courseTime.get(miniClassId)!=null?courseTime.get(miniClassId):"");
			}else{
				rowList.add("");
			}
			rowList.add(map.get("courseClassNum")!=null?new BigDecimal(map.get("courseClassNum").toString()):new BigDecimal("0"));
			rowList.add(map.get("classTimeLength")!=null?new BigDecimal(map.get("classTimeLength").toString()):new BigDecimal("0"));


			if(map.get("courseClassNum")!=null && map.get("classTimeLength")!=null){
				//每讲长度（小时）courseTimeLength =courseClassNum*classTimeLength/60
				BigDecimal courseTimeLenth = new BigDecimal(map.get("courseClassNum").toString()).multiply(new BigDecimal(map.get("classTimeLength").toString()))
						.divide(new BigDecimal("60"), 2, BigDecimal.ROUND_HALF_UP);
				rowList.add(courseTimeLenth);
			}else{
				rowList.add("0");
			}

			rowList.add(map.get("unitPrice")!=null?new BigDecimal(map.get("unitPrice").toString()):new BigDecimal("0"));
			//每讲单价 coursePrice = courseClassNum*unitPrice
			if(map.get("unitPrice")!=null && map.get("courseClassNum")!=null){
				rowList.add(new BigDecimal(map.get("courseClassNum").toString()).multiply(new BigDecimal(map.get("unitPrice").toString())));
			}else{
				rowList.add("0");
			}



			//课次 courseCount
			if(courseNum!=null){
				if(courseNum.get(miniClassId)!=null){
					rowList.add(new BigDecimal(courseNum.get(miniClassId)));
				}else{
					rowList.add(new BigDecimal("0"));
				}
			}else{
				rowList.add(new BigDecimal("0"));
			}



			//金额（不含资料费） classAmount =totalClassHours*unitPrice
			if(map.get("totalClassHours")!=null && map.get("unitPrice")!=null){
				rowList.add(new BigDecimal(map.get("unitPrice").toString()).multiply(new BigDecimal(map.get("totalClassHours").toString())));
			}else{
				rowList.add(new BigDecimal("0"));
			}

			Object classStatus = map.get("classStatus");
			if(classStatus!=null){
				rowList.add(MiniClassStatus.valueOf(classStatus.toString()).getName());
			}else{
				rowList.add("");
			}


			rowList.add(map.get("totalClassHours")!=null?new BigDecimal(map.get("totalClassHours").toString()):new BigDecimal("0"));
            rowList.add(map.get("consumeClassHours")!=null?new BigDecimal(map.get("consumeClassHours").toString()):new BigDecimal("0"));

			if(alreadyNum!=null){
				rowList.add(alreadyNum.get(miniClassId)!=null?alreadyNum.get(miniClassId):new BigDecimal("0"));
			}else{
				rowList.add(new BigDecimal("0"));
			}



			rowList.add(map.get("createTime")!=null?map.get("createTime").toString():"");
            rowList.add(map.get("textBookName")!=null?map.get("textBookName").toString():"");

            dataList.add(rowList);


		}
		return dataList;
	}

	/**
	 * 续读学生明细导出
	 *
	 * @param miniClass
	 * @param dataPackage
	 * @return
	 */
	@Override
	public List getSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage) {
		//产品季度
		if (miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())
				&& miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())){
			String quarterId = miniClass.getProduct().getProductQuarter().getId();
			if ("DAT0000000231".equals(quarterId)){
				//春季
				return getSpringSmallClassXuDuListToExcel(miniClass, dataPackage);
			}else if ("DAT0000000232".equals(quarterId)){
				//暑假
				return getSummerSmallClassXuDuListToExcel(miniClass, dataPackage);
			}else if ("DAT0000000233".equals(quarterId)){
				//秋季
				return getAutumnSmallClassXuDuListToExcel(miniClass, dataPackage);
			}else if ("DAT0000000230".equals(quarterId)){
				//寒假
				return getWinterSmallClassXuDuListToExcel(miniClass, dataPackage);
			}else {
				throw new ApplicationException("找不到的季度");
			}
		}else {
			throw new ApplicationException("请选择产品年份和季度再导出数据");
		}


	}


	private List getWinterSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		StringBuffer whereSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			whereSql.append("  and mc.BL_CAMPUS_ID = :blCampusId   ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization branch = userService.getBelongBranch();
			if (branch != null && OrganizationType.BRENCH == branch.getOrgType() && StringUtils.isNotEmpty( branch.getOrgLevel())) {
				String orgLevel = branch.getOrgLevel();
				whereSql.append("  and mc.BL_CAMPUS_ID in (SELECT id from organization where orgLevel like '"+orgLevel+"%')    ");
			}else {
				whereSql.append("  and 1=2       ");
			}
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			whereSql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}

		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			whereSql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			whereSql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}


	    sql.append("    	SELECT d.branchName branchName, d.blCampusName blCampusName ,d.mcName winterMCName, d.contractId winterContractId, d.contractTime  winterContractTime, d.winterTeacherName                                                                   ");
	    sql.append("    			,d.winterYearName, d.winterQuarterName                                                                                                   ");
	    sql.append("    			,d.phaseName                                                                                                                             ");
	    sql.append("    			,d.courseWeekday                                                                                                                         ");
	    sql.append("    			,d.winterGradeName                                                                                                                       ");
	    sql.append("    			,d.winterSubjectName                                                                                                                     ");
	    sql.append("    			,d.classTypeName                                                                                                                         ");
	    sql.append("    			,d.studentId                                                                                                                             ");
	    sql.append("    			,d.studentName                                                                                                                           ");
        sql.append("                ,case WHEN a.springTeacherId=d.winterTeacherId THEN 1 ELSE 0 END originSpringTeacher                                                     ");
        sql.append("                ,case WHEN IFNULL(a.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSpring                                                                          ");
        sql.append("                ,a.mcName springMCName, a.contractId springContractId, a.contractTime springContractTime                                                                                                                   ");
        sql.append("                ,a.classTypeName springClassType                                                                                                         ");
        sql.append("                ,a.springTeacherName                                                                                                                     ");
	    sql.append("    			,a.springTeacherId                                                                                                                       ");
	    sql.append("    			,a.springTimeSpace                                                                                                                       ");
	    sql.append("    	FROM                                                                                                                                             ");
	    sql.append("    			(                                                                                                                                        ");
	    sql.append("    					SELECT                                                                                                                           ");
	    sql.append("    					mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId                                                        ");
	    sql.append("    					, winterSubject.`NAME` winterSubjectName,winterYear.`NAME` winterYearName,winterQuarter.`NAME` winterQuarterName                 ");
	    sql.append("    					,winterGrade.`NAME` winterGradeName, winterGrade.ID winterGradeId                                                                ");
	    sql.append("    					,blCampus.name blCampusName, branch.name branchName                                                                                                      ");
	    sql.append("    					,winterTeacher.name winterTeacherName, winterTeacher.USER_ID winterTeacherId                                                     ");
	    sql.append("    					,phase.name phaseName                                                                                                            ");
	    sql.append("    					,classType.name classTypeName                                                                                                    ");
	    sql.append("    					,stu.name studentName                                                                                                            ");
	    sql.append("    					,mc.course_weekday courseWeekday                                                                                                 ");
	    sql.append("    					FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                       ");
	    sql.append("    					, data_dict winterSubject, data_dict winterGrade, data_dict winterYear , data_dict winterQuarter          ");
	    sql.append("    					, organization blCampus LEFT JOIN  organization branch on blCampus.parentID= branch.id                                                                                                        ");
	    sql.append("    					, `user` winterTeacher                                                                                                           ");
//	    sql.append("    					, data_dict phase                                                                                                                ");
//	    sql.append("    					, data_dict classType                                                                                                            ");
	    sql.append("    					,student stu                                                                                                                     ");
	    sql.append("                        , product winterProduct LEFT JOIN data_dict phase  ON phase.id=winterProduct.SMALL_CLASS_PHASE_ID  ");
	    sql.append("                        LEFT JOIN data_dict classType ON classType.id=winterProduct.CLASS_TYPE_ID   ");
	    sql.append("    					WHERE                                                                                                                            ");
	    sql.append("    					mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID                                                                                               ");
		sql.append("                        AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID  AND cp.TYPE='SMALL_CLASS'     ");//只要小班的
//	    sql.append("    					AND phase.id=winterProduct.SMALL_CLASS_PHASE_ID                                                                                  ");
//	    sql.append("    					AND classType.id=winterProduct.CLASS_TYPE_ID                                                                                     ");
	    sql.append("    					AND stu.id=mcs.STUDENT_ID                                                                                                        ");
	    sql.append("    					AND mc.TEACHER_ID=winterTeacher.USER_ID                                                                                          ");
	    sql.append("    					AND mc.BL_CAMPUS_ID=blCampus.id                                                                                                  ");
	    sql.append("    					AND winterSubject.ID=mc.`SUBJECT`                                                                                                ");
	    sql.append("    					AND winterGrade.ID=mc.GRADE                                                                                                      ");
	    sql.append("    					AND winterYear.ID=winterProduct.PRODUCT_VERSION_ID                                                                               ");
	    sql.append("    					AND mc.PRODUCE_ID=winterProduct.ID                                                                                               ");
	    sql.append("    					AND winterQuarter.ID=winterProduct.PRODUCT_QUARTER_ID                                                                            ");
		sql.append("                        AND winterProduct.PRODUCT_VERSION_ID = :productVersionId                                                                          "); //产品年份
		sql.append(whereSql);
	    sql.append("    					AND winterQuarter.`NAME`='寒假'                                                                                                    ");
        sql.append("                        ) d                                                                                                                              ");
	    sql.append("    	LEFT JOIN                                                                                                                                        ");
	    sql.append("    	(                                                                                                                                                ");
	    sql.append("    			SELECT mc.`NAME` mcName,con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, springSubject.`NAME` springSubjectName                                                ");
	    sql.append("    			,springYear.`NAME` springYearName                                                                                                        ");
	    sql.append("    			, springGrade.ID springGradeId                                                                                                           ");
	    sql.append("    			, springTeacher.USER_ID springTeacherId                                                                                                  ");
	    sql.append("    			, springTeacher.name springTeacherName                                                                                                   ");
	    sql.append("    			, classType.name classTypeName                                                                                                           ");
	    sql.append("    			, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) springTimeSpace                                               ");
	    sql.append("    	FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                                       ");
	    sql.append("    			, data_dict springSubject, data_dict springGrade, data_dict springYear , data_dict springQuarter                  ");
        sql.append("                ,`user` springTeacher                                                                                                                    ");
//	    sql.append("    			, data_dict classType                                                                                                                    ");
		sql.append("                , product springProduct LEFT JOIN  data_dict classType ON  classType.id=springProduct.CLASS_TYPE_ID     ");
	    sql.append("    			WHERE                                                                                                                                    ");
	    sql.append("    	mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID   AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                                                ");
//	    sql.append("    	AND classType.id=springProduct.CLASS_TYPE_ID                                                                                                     ");
	    sql.append("    	AND springSubject.ID=mc.`SUBJECT`                                                                                                                ");
	    sql.append("    	AND springTeacher.USER_ID=mc.TEACHER_ID                                                                                                          ");
	    sql.append("    	AND springGrade.ID=mc.GRADE                                                                                                                      ");
	    sql.append("    	AND springYear.ID=springProduct.PRODUCT_VERSION_ID                                                                                               ");
	    sql.append("    	AND mc.PRODUCE_ID=springProduct.ID                                                                                                               ");
	    sql.append("    	AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID                                                                                            ");
		sql.append("        AND springProduct.PRODUCT_VERSION_ID = :productVersionId                                                                          "); //产品年份
	    sql.append("    	AND springQuarter.`NAME`='春季'                                                                                                                    ");
        sql.append("        ) a                                                                                                                                              ");
	    sql.append("    			ON                                                                                                                                       ");
	    sql.append("    	d.studentId=a.studentId                                                                                                                          ");
	    sql.append("    	AND d.winterSubjectName=a.springSubjectName                                                                                                      ");
	    sql.append("    	AND d.winterYearName=a.springYearName                                                                                                            ");
	    sql.append("    	AND d.winterGradeId=a.springGradeId                                                                                                              ");


		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		return result;
	}

	/**
	 *
	 * @param miniClass
	 * @param dataPackage
	 * @return
	 */
	private List getAutumnSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		StringBuffer whereSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			whereSql.append("  and mc.BL_CAMPUS_ID = :blCampusId   ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization branch = userService.getBelongBranch();
			if (branch != null && OrganizationType.BRENCH == branch.getOrgType() && StringUtils.isNotEmpty( branch.getOrgLevel())) {
				String orgLevel = branch.getOrgLevel();
				whereSql.append("  and mc.BL_CAMPUS_ID in (SELECT id from organization where orgLevel like '"+orgLevel+"%')    ");
			}else {
				whereSql.append("  and 1=2       ");
			}
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			whereSql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}

		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			whereSql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			whereSql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}


		sql.append("    SELECT  c.branchName branchName, c.blCampusName blCampusName ,c.mcName autumnMCName, c.contractId springContractId, c.contractTime  autumnContractTime, c.autumnTeacherName                                                                ");
		sql.append("    		,c.autumnYearName, c.autumnQuarterName                                                                                                 ");
		sql.append("    		,c.phaseName                                                                                                                           ");
		sql.append("    		,c.courseWeekday                                                                                                                       ");
		sql.append("    		,c.autumnGradeName                                                                                                                     ");
		sql.append("    		,c.autumnSubjectName                                                                                                                   ");
		sql.append("    		,c.classTypeName                                                                                                                       ");
		sql.append("    		,c.studentId                                                                                                                           ");
		sql.append("    		,c.studentName                                                                                                                         ");
        sql.append("            ,case WHEN d.winterTeacherId=c.autumnTeacherId THEN 1 ELSE 0 END originAutumn1Teacher                                                 ");
        sql.append("            ,case WHEN IFNULL(d.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoWinter                                                                       ");
        sql.append("            ,d.mcName winterMCName, d.contractId winterContractId, d.contractTime winterContractTime                                                                                                                ");
        sql.append("            ,d.classTypeName winterClassType                                                                                                      ");
        sql.append("            ,d.winterTeacherName                                                                                                                  ");
		sql.append("    		,d.winterTeacherId                                                                                                                     ");
		sql.append("    		,d.winterTimeSpace                                                                                                                     ");
        sql.append("            ,case WHEN a.springTeacherId=c.autumnTeacherId THEN 1 ELSE 0 END originAutumn2Teacher                                                 ");
        sql.append("            ,case WHEN IFNULL(a.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSpring                                                                       ");
        sql.append("            ,a.mcName springMCName, a.contractId  springContractId, a.contractTime springContractTime                                                                                                                ");
        sql.append("            ,a.classTypeName springClassType                                                                                                      ");
        sql.append("            ,a.springTeacherName                                                                                                                  ");
		sql.append("    		,a.springTeacherId                                                                                                                     ");
		sql.append("    		,a.springTimeSpace                                                                                                                     ");
		sql.append("    FROM                                                                                                                                          ");
		sql.append("    		(                                                                                                                                      ");
		sql.append("    				SELECT                                                                                                                         ");
		sql.append("    				mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId                                                      ");
		sql.append("    				, autumnSubject.`NAME` autumnSubjectName,autumnYear.`NAME` autumnYearName,autumnQuarter.`NAME` autumnQuarterName               ");
		sql.append("    				,autumnGrade.`NAME` autumnGradeName, autumnGrade.ID autumnGradeId                                                              ");
		sql.append("    				,blCampus.name blCampusName, branch.name   branchName                                                                                              ");
		sql.append("    				,autumnTeacher.name autumnTeacherName, autumnTeacher.USER_ID autumnTeacherId                                                   ");
		sql.append("    				,phase.name phaseName                                                                                                          ");
		sql.append("    				,classType.name classTypeName                                                                                                  ");
		sql.append("    				,stu.name studentName                                                                                                          ");
		sql.append("    				,mc.course_weekday courseWeekday                                                                                               ");
		sql.append("    				FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                      ");
		sql.append("    				, data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear , data_dict autumnQuarter        ");
		sql.append("    				, organization blCampus  LEFT JOIN  organization branch on  blCampus.parentID=branch.id                                                                                                    ");
		sql.append("    				, `user` autumnTeacher                                                                                                         ");
//		sql.append("    				, data_dict phase                                                                                                              ");
//		sql.append("    				, data_dict classType                                                                                                          ");
		sql.append("    				,student stu                                                                                                                   ");
		sql.append("                    , product autumnProduct  LEFT JOIN  data_dict phase  ON  phase.id=autumnProduct.SMALL_CLASS_PHASE_ID                        ");
		sql.append("                    LEFT JOIN  data_dict classType ON  classType.id=autumnProduct.CLASS_TYPE_ID  ");
		sql.append("    				WHERE                                                                                                                          ");
		sql.append("    				mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID                                                                                             ");
		sql.append("                    AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID  AND cp.TYPE='SMALL_CLASS'     ");//只要小班的
//		sql.append("    				AND phase.id=autumnProduct.SMALL_CLASS_PHASE_ID                                                                                ");
//		sql.append("    				AND classType.id=autumnProduct.CLASS_TYPE_ID                                                                                   ");
		sql.append("    				AND stu.id=mcs.STUDENT_ID                                                                                                      ");
		sql.append("    				AND mc.TEACHER_ID=autumnTeacher.USER_ID                                                                                        ");
		sql.append("    				AND mc.BL_CAMPUS_ID=blCampus.id                                                                                                ");
		sql.append("    				AND autumnSubject.ID=mc.`SUBJECT`                                                                                              ");
		sql.append("    				AND autumnGrade.ID=mc.GRADE                                                                                                    ");
		sql.append("    				AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID                                                                             ");
		sql.append("    				AND mc.PRODUCE_ID=autumnProduct.ID                                                                                             ");
		sql.append("    				AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID                                                                          ");
		sql.append("                    AND autumnProduct.PRODUCT_VERSION_ID = :productVersionId                                                                   "); //产品年份
		sql.append(whereSql);
		sql.append("    				AND autumnQuarter.`NAME`='秋季') c                                                                                               ");
		sql.append("                                                                                                                                                  ");
		sql.append("    LEFT JOIN                                                                                                                                     ");
		sql.append("    (                                                                                                                                             ");
		sql.append("    		SELECT mc.`NAME` mcName,con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, winterSubject.`NAME` winterSubjectName                                              ");
		sql.append("    		,winterYear.`NAME` winterYearName                                                                                                      ");
		sql.append("    		, winterGrade.ID winterGradeId                                                                                                         ");
		sql.append("    		, winterTeacher.USER_ID winterTeacherId                                                                                                ");
		sql.append("    		, winterTeacher.name winterTeacherName                                                                                                 ");
		sql.append("    		, classType.name classTypeName                                                                                                         ");
		sql.append("    		, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) winterTimeSpace                                                                                                          ");
		sql.append("    FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                                    ");
		sql.append("    		, data_dict winterSubject, data_dict winterGrade, data_dict winterYear , data_dict winterQuarter                ");
        sql.append("            ,`user` winterTeacher                                                                                                                 ");
		sql.append("    		, product winterProduct LEFT JOIN  data_dict classType  ON classType.id=winterProduct.CLASS_TYPE_ID                                                                                                                ");
		sql.append("    		WHERE                                                                                                                                  ");
		sql.append("    mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                                                ");
//		sql.append("    AND classType.id=winterProduct.CLASS_TYPE_ID                                                                                                  ");
		sql.append("    AND winterSubject.ID=mc.`SUBJECT`                                                                                                             ");
		sql.append("    AND winterTeacher.USER_ID=mc.TEACHER_ID                                                                                                       ");
		sql.append("    AND winterGrade.ID=mc.GRADE                                                                                                                   ");
		sql.append("    AND winterYear.ID=winterProduct.PRODUCT_VERSION_ID                                                                                            ");
		sql.append("    AND mc.PRODUCE_ID=winterProduct.ID                                                                                                            ");
		sql.append("    AND winterQuarter.ID=winterProduct.PRODUCT_QUARTER_ID                                                                                         ");
//		sql.append("    AND winterProduct.PRODUCT_VERSION_ID = :productVersionId                                                                          "); //产品年份
		sql.append("    AND winterQuarter.`NAME`='寒假'                                                                                                                 ");
        sql.append("    ) d                                                                                                                                           ");
		sql.append("    ON  c.studentId=d.studentId                                                                                                                   ");
		sql.append("    AND c.autumnSubjectName=d.winterSubjectName                                                                                                   ");
		sql.append("    AND nextYear(c.autumnYearName)=d.winterYearName                                                                                               ");
		sql.append("    AND c.autumnGradeId=d.winterGradeId                                                                                                           ");
		sql.append("                                                                                                                                                  ");
		sql.append("    LEFT JOIN                                                                                                                                     ");
		sql.append("    (                                                                                                                                             ");
		sql.append("    		SELECT mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, springSubject.`NAME` springSubjectName                                              ");
		sql.append("    		,springYear.`NAME` springYearName                                                                                                      ");
		sql.append("    		, springGrade.ID springGradeId                                                                                                         ");
		sql.append("    		, springTeacher.USER_ID springTeacherId                                                                                                ");
		sql.append("    		, springTeacher.name springTeacherName                                                                                                 ");
		sql.append("    		, classType.name classTypeName                                                                                                         ");
		sql.append("    		, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) springTimeSpace                                                                                                       ");
		sql.append("    FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                                    ");
		sql.append("    		, data_dict springSubject, data_dict springGrade, data_dict springYear , data_dict springQuarter                ");
        sql.append("            ,`user` springTeacher                                                                                                                 ");
		sql.append("    		, product springProduct LEFT JOIN  data_dict classType  ON classType.id=springProduct.CLASS_TYPE_ID                                                                                                                ");
		sql.append("    		WHERE                                                                                                                                  ");
		sql.append("    mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID  AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                                           ");
		sql.append("    AND springSubject.ID=mc.`SUBJECT`                                                                                                             ");
//		sql.append("    AND classType.id=springProduct.CLASS_TYPE_ID                                                                                                  ");
		sql.append("    AND springTeacher.USER_ID=mc.TEACHER_ID                                                                                                       ");
		sql.append("    AND springGrade.ID=mc.GRADE                                                                                                                   ");
		sql.append("    AND springYear.ID=springProduct.PRODUCT_VERSION_ID                                                                                            ");
		sql.append("    AND mc.PRODUCE_ID=springProduct.ID                                                                                                            ");
		sql.append("    AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID                                                                                         ");
//		sql.append("    AND springProduct.PRODUCT_VERSION_ID = :productVersionId                                                                          "); //产品年份
		sql.append("    AND springQuarter.`NAME`='春季'                                                                                                                 ");
        sql.append("    ) a                                                                                                                                           ");
		sql.append("    ON c.studentId=a.studentId                                                                                                                    ");
		sql.append("    AND c.autumnSubjectName=a.springSubjectName                                                                                                   ");
		sql.append("    AND nextYear(c.autumnYearName)=a.springYearName                                                                                               ");
		sql.append("    AND c.autumnGradeId=a.springGradeId                                                                                                           ");


		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		return result;
	}

	/**
	 *
	 * @param miniClass
	 * @param dataPackage
	 * @return
	 */
	private List getSummerSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		StringBuffer whereSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			whereSql.append("  and mc.BL_CAMPUS_ID = :blCampusId   ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization branch = userService.getBelongBranch();
			if (branch != null && OrganizationType.BRENCH == branch.getOrgType() && StringUtils.isNotEmpty( branch.getOrgLevel())) {
				String orgLevel = branch.getOrgLevel();
				whereSql.append("  and mc.BL_CAMPUS_ID in (SELECT id from organization where orgLevel like '"+orgLevel+"%')    ");
			}else {
				whereSql.append("  and 1=2       ");
			}
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			whereSql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}

		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			whereSql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			whereSql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}

		sql.append("   SELECT b.branchName branchName, b.blCampusName blCampusName ,b.mcName summerMCName, b.contractId summerContractId, b.contractTime  summerContractTime, b.summerTeacherName                                                                  ");
		sql.append("   		,b.summerYearName, b.summerQuarterName                                                                                                  ");
		sql.append("   		,b.phaseName                                                                                                                            ");
		sql.append("   		,b.courseWeekday                                                                                                                        ");
		sql.append("   		,b.summerGradeName                                                                                                                      ");
		sql.append("   		,b.summerSubjectName                                                                                                                    ");
		sql.append("   		,b.classTypeName                                                                                                                        ");
		sql.append("   		,b.studentId                                                                                                                            ");
		sql.append("   		,b.studentName                                                                                                                          ");
        sql.append("           ,case WHEN c.autumnTeacherId=b.summerTeacherId THEN 1 ELSE 0 END originSummerTeacher                                                    ");
        sql.append("           ,case WHEN IFNULL(c.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoAutumn                                                                         ");
        sql.append("           ,c.mcName autumnMCName, c.contractId autumnContractId, c.contractTime autumnContractTime                                                                                                                  ");
        sql.append("           ,c.classTypeName autumnClassType                                                                                                        ");
        sql.append("           ,c.autumnTeacherName                                                                                                                    ");
		sql.append("   		,c.autumnTeacherId                                                                                                                      ");
		sql.append("   		,c.autumnTimeSpace                                                                                                                      ");
		sql.append("   FROM                                                                                                                                            ");
		sql.append("   		(                                                                                                                                       ");
		sql.append("   				SELECT                                                                                                                          ");
		sql.append("   				mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId                                                       ");
		sql.append("   				, summerSubject.`NAME` summerSubjectName,summerYear.`NAME` summerYearName,summerQuarter.`NAME` summerQuarterName                ");
		sql.append("   				,summerGrade.`NAME` summerGradeName, summerGrade.ID summerGradeId                                                               ");
		sql.append("   				,blCampus.name blCampusName, branch.name  branchName                                                                                                    ");
		sql.append("   				,summerTeacher.name summerTeacherName, summerTeacher.USER_ID summerTeacherId                                                    ");
		sql.append("   				,phase.name phaseName                                                                                                           ");
		sql.append("   				,classType.name classTypeName                                                                                                   ");
		sql.append("   				,stu.name studentName                                                                                                           ");
		sql.append("   				,mc.course_weekday courseWeekday                                                                                                ");
		sql.append("   				FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                      ");
		sql.append("   				, data_dict summerSubject, data_dict summerGrade, data_dict summerYear , data_dict summerQuarter         ");
		sql.append("   				, organization blCampus LEFT JOIN  organization branch on  blCampus.parentID= branch.id                                                                                           ");
		sql.append("   				, `user` summerTeacher                                                                                                          ");
//		sql.append("   				, data_dict phase                                                                                                               ");
//		sql.append("   				, data_dict classType                                                                                                           ");
		sql.append("   				,student stu                                                                                                                    ");
		sql.append("                , product summerProduct  LEFT JOIN data_dict phase on  phase.id=summerProduct.SMALL_CLASS_PHASE_ID                         ");
		sql.append("                LEFT JOIN data_dict classType on  classType.id=summerProduct.CLASS_TYPE_ID    ");
		sql.append("   				WHERE                                                                                                                           ");
		sql.append("   				mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID                                                                                              ");
		sql.append("                AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID  AND cp.TYPE='SMALL_CLASS'     ");//只要小班的
//		sql.append("   				AND phase.id=summerProduct.SMALL_CLASS_PHASE_ID                                                                                 ");
//		sql.append("   				AND classType.id=summerProduct.CLASS_TYPE_ID                                                                                    ");
		sql.append("   				AND stu.id=mcs.STUDENT_ID                                                                                                       ");
		sql.append("   				AND mc.TEACHER_ID=summerTeacher.USER_ID                                                                                         ");
		sql.append("   				AND mc.BL_CAMPUS_ID=blCampus.id                                                                                                 ");
		sql.append("   				AND summerSubject.ID=mc.`SUBJECT`                                                                                               ");
		sql.append("   				AND summerGrade.ID=mc.GRADE                                                                                                     ");
		sql.append("   				AND summerYear.ID=summerProduct.PRODUCT_VERSION_ID                                                                              ");
		sql.append("   				AND mc.PRODUCE_ID=summerProduct.ID                                                                                              ");
		sql.append("   				AND summerQuarter.ID=summerProduct.PRODUCT_QUARTER_ID                                                                           ");
		sql.append("                AND summerProduct.PRODUCT_VERSION_ID = :productVersionId                                                                   "); //产品年份
		sql.append(whereSql);
		sql.append("   				AND summerQuarter.`NAME`='暑假'                                                                                                   ");
        sql.append("            ) b                                                                                                                                    ");
		sql.append("   LEFT JOIN                                                                                                                                       ");
		sql.append("   (                                                                                                                                               ");
		sql.append("   		SELECT mc.`NAME` mcName,con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, autumnSubject.`NAME` autumnSubjectName                                               ");
		sql.append("   		,autumnYear.`NAME` autumnYearName                                                                                                       ");
		sql.append("   		, autumnGrade.ID autumnGradeId                                                                                                          ");
		sql.append("   		, autumnTeacher.USER_ID autumnTeacherId                                                                                                 ");
		sql.append("   		, autumnTeacher.name autumnTeacherName                                                                                                  ");
		sql.append("   		, classType.name classTypeName                                                                                                          ");
		sql.append("   		, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) autumnTimeSpace                                              ");
		sql.append("           FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                              ");
		sql.append("   		, data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear , data_dict autumnQuarter                 ");
        sql.append("           ,`user` autumnTeacher                                                                                                                   ");
		sql.append("   		, product autumnProduct LEFT JOIN  data_dict classType  on classType.id=autumnProduct.CLASS_TYPE_ID                                                                                                                 ");
		sql.append("   		WHERE                                                                                                                                   ");
		sql.append("           mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID   AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                                        ");
//		sql.append("           AND classType.id=autumnProduct.CLASS_TYPE_ID                                                                                            ");
		sql.append("           AND autumnSubject.ID=mc.`SUBJECT`                                                                                                       ");
		sql.append("           AND autumnTeacher.USER_ID=mc.TEACHER_ID                                                                                                 ");
		sql.append("           AND autumnGrade.ID=mc.GRADE                                                                                                             ");
		sql.append("           AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID                                                                                      ");
		sql.append("           AND mc.PRODUCE_ID=autumnProduct.ID                                                                                                      ");
		sql.append("           AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID                                                                                   ");
		sql.append("           AND autumnProduct.PRODUCT_VERSION_ID = :productVersionId                                                                   "); //产品年份
		sql.append("           AND autumnQuarter.`NAME`='秋季'                                                                                                           ");
		sql.append("   ) c                                                                                                                                             ");
		sql.append("   		ON                                                                                                                                      ");
		sql.append("           b.studentId=c.studentId                                                                                                                 ");
		sql.append("           AND b.summerSubjectName=c.autumnSubjectName                                                                                             ");
		sql.append("           AND b.summerYearName=c.autumnYearName                                                                                                   ");
		sql.append("           AND b.summerGradeId=c.autumnGradeId                                                                                                     ");


		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		return result;
	}

	/**
	 * 春季
	 * @param miniClass
	 * @param dataPackage
	 * @return
	 */
	private List getSpringSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage) {

		StringBuffer sql = new StringBuffer();
		StringBuffer whereSql = new StringBuffer();
		Map<String, Object> params = new HashMap<>();
		params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			whereSql.append("  and mc.BL_CAMPUS_ID = :blCampusId   ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization branch = userService.getBelongBranch();
			if (branch != null && OrganizationType.BRENCH == branch.getOrgType() && StringUtils.isNotEmpty( branch.getOrgLevel())) {
				String orgLevel = branch.getOrgLevel();
				whereSql.append("  and mc.BL_CAMPUS_ID in (SELECT id from organization where orgLevel like '"+orgLevel+"%')    ");
			}else {
				whereSql.append("  and 1=2       ");
			}
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			whereSql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}

		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			whereSql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			whereSql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}



		sql.append("  SELECT a.branchName branchName, a.blCampusName blCampusName ,a.mcName springMCName, a.contractId springContractId, a.contractTime  springContractTime, a.springTeacherName                                                            ");
		sql.append("  		,a.springYearName, a.springQuarterName                                                                                             ");
		sql.append("  		,a.phaseName                                                                                                                       ");
		sql.append("  		,a.courseWeekday                                                                                                                   ");
		sql.append("        ,a.springClassTimeSpace                                                                                                            ");
		sql.append("  		,a.springGradeName                                                                                                                 ");
		sql.append("  		,a.springSubjectName                                                                                                               ");
		sql.append("  		,a.classTypeName                                                                                                                   ");
		sql.append("  		,a.studentId                                                                                                                       ");
		sql.append("  		,a.studentName                                                                                                                     ");
        sql.append("          ,case WHEN b.summerTeacherId=a.springTeacherId THEN 1 ELSE 0 END originSpring1Teacher                                              ");
        sql.append("          ,case WHEN IFNULL(b.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoSummer                                                                    ");
        sql.append("          ,b.mcName summerMCName, b.contractId summerContractId, b.contractTime summerContractTime                                                                                                            ");
        sql.append("          ,b.classTypeName summerClassType                                                                                                   ");
        sql.append("          ,b.summerTeacherName                                                                                                               ");
        sql.append("          				,b.summerTeacherId                                                                                                 ");
        sql.append("          				,b.summerTimeSpace                                                                                                 ");
        sql.append("          ,case WHEN c.autumnTeacherId=a.springTeacherId THEN 1 ELSE 0 END originSpring2Teacher                                              ");
        sql.append("          ,case WHEN IFNULL(c.mcName, 0)=0 THEN 0  ELSE 1 END XuBaoAutumn                                                                    ");
        sql.append("          ,c.mcName autumnMCName, c.contractId  autumnContractId, c.contractTime autumnContractTime                                                                                                            ");
        sql.append("          ,c.classTypeName autumnClassType                                                                                                   ");
        sql.append("          ,c.autumnTeacherName                                                                                                               ");
		sql.append("  		,c.autumnTeacherId                                                                                                                 ");
		sql.append("  		,c.autumnTimeSpace                                                                                                                 ");
		sql.append("  FROM                                                                                                                                         ");
		sql.append("  		(                                                                                                                                  ");
		sql.append("  				SELECT                                                                                                                     ");
		sql.append("  				mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.MINI_CLASS_ID miniClassId, mcs.STUDENT_ID studentId                                                  ");
		sql.append("  				, springSubject.`NAME` springSubjectName,springYear.`NAME` springYearName,springQuarter.`NAME` springQuarterName           ");
		sql.append("                , timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) springClassTimeSpace                               ");
		sql.append("  				,springGrade.`NAME` springGradeName, springGrade.ID springGradeId                                                          ");
		sql.append("  				,blCampus.name blCampusName                                                                                                ");
		sql.append("  				,branch.name branchName                                                                                                ");
		sql.append("  				,springTeacher.name springTeacherName, springTeacher.USER_ID springTeacherId                                               ");
		sql.append("  				,phase.name phaseName                                                                                                      ");
		sql.append("  				,classType.name classTypeName                                                                                              ");
		sql.append("  				,stu.name studentName                                                                                                      ");
		sql.append("  				,mc.course_weekday courseWeekday                                                                                           ");
		sql.append("  				FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                    ");
		sql.append("  				, data_dict springSubject, data_dict springGrade, data_dict springYear , data_dict springQuarter    ");
		sql.append("  				, organization blCampus LEFT JOIN  organization branch  on  blCampus.parentID = branch.id                                                                                    ");
		sql.append("  				, `user` springTeacher                                                                                                     ");
//		sql.append("  				, data_dict phase                                                                                                          ");
//		sql.append("  				, data_dict classType                                                                                                      ");
		sql.append("  				,student stu                                                                                                               ");
		sql.append("                , product springProduct                                                            ");
		sql.append("                LEFT JOIN data_dict phase on phase.id = springProduct.SMALL_CLASS_PHASE_ID       ");
		sql.append("                LEFT JOIN data_dict classType on classType.id = springProduct.CLASS_TYPE_ID       ");
		sql.append("  				WHERE                                                                                                                      ");
		sql.append("  				mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID                                                                                         ");
		sql.append("                AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID  AND cp.TYPE='SMALL_CLASS'     ");//只要小班的
//		sql.append("  				AND phase.id=springProduct.SMALL_CLASS_PHASE_ID                                                                            ");
//		sql.append("  				AND classType.id=springProduct.CLASS_TYPE_ID                                                                               ");
		sql.append("  				AND stu.id=mcs.STUDENT_ID                                                                                                  ");
		sql.append("  				AND mc.TEACHER_ID=springTeacher.USER_ID                                                                                    ");
		sql.append("  				AND mc.BL_CAMPUS_ID=blCampus.id                                                                                            ");
		sql.append("  				AND springSubject.ID=mc.`SUBJECT`                                                                                          ");
		sql.append("  				AND springGrade.ID=mc.GRADE                                                                                                ");
		sql.append("  				AND springYear.ID=springProduct.PRODUCT_VERSION_ID                                                                         ");
		sql.append("  				AND mc.PRODUCE_ID=springProduct.ID                                                                                         ");
		sql.append("  				AND springQuarter.ID=springProduct.PRODUCT_QUARTER_ID                                                                      ");
		sql.append("                AND springProduct.PRODUCT_VERSION_ID = :productVersionId                                                                   "); //产品年份
		sql.append(whereSql);
		sql.append("  				AND springQuarter.`NAME`='春季') a                                                                                           ");
		sql.append("                                                                                                                                             ");
		sql.append("          LEFT JOIN                                                                                                                          ");
		sql.append("          (                                                                                                                                  ");
		sql.append("          		SELECT mc.`NAME` mcName,con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, summerSubject.`NAME` summerSubjectName                                  ");
		sql.append("          		,summerYear.`NAME` summerYearName                                                                                          ");
		sql.append("          		, summerGrade.ID summerGradeId                                                                                             ");
		sql.append("          		, summerTeacher.USER_ID summerTeacherId                                                                                    ");
		sql.append("          		, summerTeacher.name summerTeacherName                                                                                     ");
		sql.append("          		, classType.name classTypeName                                                                                             ");
		sql.append("          		, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) summerTimeSpace                                  ");
		sql.append("                  FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                 ");
		sql.append("          		, data_dict summerSubject, data_dict summerGrade, data_dict summerYear , data_dict summerQuarter    ");
        sql.append("                  ,`user` summerTeacher                                                                                                      ");
		sql.append("          		, product summerProduct LEFT JOIN data_dict classType on  classType.id=summerProduct.CLASS_TYPE_ID                                                                                                     ");
		sql.append("          		WHERE                                                                                                                      ");
		sql.append("                        mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID  AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                 ");
//		sql.append("                        AND classType.id=summerProduct.CLASS_TYPE_ID                                                                         ");
		sql.append("                        AND summerSubject.ID=mc.`SUBJECT`                                                                                    ");
		sql.append("                        AND summerTeacher.USER_ID=mc.TEACHER_ID                                                                              ");
		sql.append("                        AND summerGrade.ID=mc.GRADE                                                                                          ");
		sql.append("                        AND summerYear.ID=summerProduct.PRODUCT_VERSION_ID                                                                   ");
		sql.append("                        AND summerProduct.PRODUCT_VERSION_ID = :productVersionId                                                             ");
		sql.append("                        AND mc.PRODUCE_ID=summerProduct.ID                                                                                   ");
		sql.append("                        AND summerQuarter.ID=summerProduct.PRODUCT_QUARTER_ID                                                                ");
		sql.append("                        AND summerQuarter.`NAME`='暑假'                                                                                        ");
        sql.append("                   ) b                                                                                                                       ");
		sql.append("          ON a.studentId=b.studentId                                                                                                         ");
		sql.append("          AND a.springSubjectName=b.summerSubjectName                                                                                        ");
		sql.append("          AND a.springYearName=b.summerYearName                                                                                              ");
		sql.append("          AND nextGrade(a.springGradeId)=b.summerGradeId                                                                                     ");
		sql.append("                                                                                                                                             ");
		sql.append("          LEFT JOIN                                                                                                                          ");
		sql.append("          (                                                                                                                                  ");
		sql.append("          		SELECT mc.`NAME` mcName, con.ID contractId, con.CREATE_TIME contractTime, mcs.STUDENT_ID studentId, autumnSubject.`NAME` autumnSubjectName                                  ");
		sql.append("          		,autumnYear.`NAME` autumnYearName                                                                                          ");
		sql.append("          		, autumnGrade.ID autumnGradeId                                                                                             ");
		sql.append("          		, autumnTeacher.USER_ID autumnTeacherId                                                                                    ");
		sql.append("          		, autumnTeacher.name autumnTeacherName                                                                                     ");
		sql.append("          		, classType.name classTypeName                                                                                             ");
		sql.append("          		, timeADD(mc.CLASS_TIME, mc.CLASS_TIME_LENGTH, mc.EVERY_COURSE_CLASS_NUM) autumnTimeSpace                                                                                             ");
		sql.append("                  FROM mini_class_student mcs, mini_class mc, contract con, contract_product cp                                                                                 ");
		sql.append("          		, data_dict autumnSubject, data_dict autumnGrade, data_dict autumnYear , data_dict autumnQuarter    ");
        sql.append("                  ,`user` autumnTeacher                                                                                                      ");
		sql.append("          		, product autumnProduct LEFT JOIN  data_dict classType on   classType.id=autumnProduct.CLASS_TYPE_ID                                                                                                  ");
		sql.append("          		WHERE                                                                                                                      ");
		sql.append("                  mcs.MINI_CLASS_ID=mc.MINI_CLASS_ID AND mcs.CONTRACT_ID = con.ID AND mcs.CONTRACT_PRODUCT_ID=cp.ID AND cp.TYPE='SMALL_CLASS'                                                                                        ");
		sql.append("                  AND autumnSubject.ID=mc.`SUBJECT`                                                                                          ");
//		sql.append("                  AND  classType.id=autumnProduct.CLASS_TYPE_ID                                                                              ");
		sql.append("                  AND autumnTeacher.USER_ID=mc.TEACHER_ID                                                                                    ");
		sql.append("                  AND autumnGrade.ID=mc.GRADE                                                                                                ");
		sql.append("                  AND autumnYear.ID=autumnProduct.PRODUCT_VERSION_ID                                                                         ");
		sql.append("                  AND autumnProduct.PRODUCT_VERSION_ID = :productVersionId                                                                   ");
		sql.append("                  AND mc.PRODUCE_ID=autumnProduct.ID                                                                                         ");
		sql.append("                  AND autumnQuarter.ID=autumnProduct.PRODUCT_QUARTER_ID                                                                      ");
		sql.append("                  AND autumnQuarter.`NAME`='秋季'                                                                                              ");
        sql.append("          ) c                                                                                                                                ");
		sql.append("          ON a.studentId=c.studentId                                                                                                         ");
		sql.append("          AND a.springSubjectName=c.autumnSubjectName                                                                                        ");
		sql.append("          AND a.springYearName=c.autumnYearName                                                                                              ");
		sql.append("          AND nextGrade(a.springGradeId)=c.autumnGradeId                                                                                     ");

		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);

		return result;
	}

	@Override
	public List<Map<Object, Object>> getSmallClassToExcelSize(MiniClass miniClass, DataPackage dataPackage) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select mc.MINI_CLASS_ID as miniClassId,mc.name as className, d.`NAME` as productVersionName,dd.`NAME` as productQuarterName,");
		sql.append(" ddd.`NAME` as phaseName,d2.`NAME` as classTypeName,mc.PEOPLE_QUANTITY as peopleQuantity,");
		sql.append(" cm.CLASS_ROOM as classRoomName,o.`name` as campusName,d_grade.`NAME` as gradeName,");
		sql.append(" d_subject.`NAME` as subjectName,u.`NAME` as teacherName,u2.`NAME` as studyManagerName,");
		sql.append(" mc.CLASS_TIME as classTime,mc.EVERY_COURSE_CLASS_NUM as courseClassNum,mc.CLASS_TIME_LENGTH as classTimeLength,");
		sql.append(" mc.UNIT_PRICE as unitPrice,mc.`STATUS` as classStatus,mc.TOTAL_CLASS_HOURS as totalClassHours,mc.CONSUME as consumeClassHours,");
		sql.append(" mc.CREATE_TIME as createTime,mc.TEXTBOOK_NAME as textBookName ");
		sql.append(" from mini_class mc left join product p on 	mc.PRODUCE_ID = p.ID ");
		sql.append(" left join data_dict d on p.PRODUCT_VERSION_ID = d.ID ");
		sql.append(" left join data_dict dd on p.PRODUCT_QUARTER_ID = dd.ID ");
		sql.append(" left join data_dict ddd on mc.PHASE = ddd.ID ");
		sql.append(" left join data_dict d2 on p.CLASS_TYPE_ID = d2.ID ");
		sql.append(" left join classroom_manage cm on mc.CLASSROOM = cm.ID ");
		sql.append(" left join organization o on mc.BL_CAMPUS_ID = o.id ");
		sql.append(" left join data_dict d_grade on mc.GRADE = d_grade.ID ");
		sql.append(" left join data_dict d_subject on mc.`SUBJECT` = d_subject.ID ");
		sql.append(" left join `user` u on mc.TEACHER_ID = u.USER_ID ");
		sql.append(" left join `user` u2 on mc.STUDY_MANEGER_ID = u2.USER_ID ");
		sql.append(" where 1=1 ");

		Map<String, Object> params = Maps.newHashMap();
		//筛选条件
		//开始时间		
		if(StringUtil.isNotBlank(miniClass.getStartDate())){
			sql.append(" and mc.START_DATE >= :startDate ");
			params.put("startDate", miniClass.getStartDate());
		}

		if(StringUtil.isNotBlank(miniClass.getEndDate())){
			sql.append(" and mc.START_DATE <= :endDate ");
			params.put("endDate", miniClass.getEndDate());
		}
		//小班名称
		if(StringUtil.isNotBlank(miniClass.getName())){
			sql.append(" and mc.`NAME` like :miniClassName ");
			params.put("miniClassName", "%" + miniClass.getName() + "%");
		}
		//班级类型
		if(miniClass.getMiniClassType()!=null && StringUtils.isNotEmpty(miniClass.getMiniClassType().getId())){
			sql.append(" and miniClassType.id = :miniClassTypeId ");
			params.put("miniClassTypeId", miniClass.getMiniClassType().getId());
		}

		//年级
		if(miniClass.getGrade()!=null && StringUtils.isNotEmpty(miniClass.getGrade().getId())){
			sql.append(" and mc.GRADE = :gradeId ");
			params.put("gradeId", miniClass.getGrade().getId());
		}
		//科目
		if(miniClass.getSubject()!=null && StringUtils.isNotEmpty(miniClass.getSubject().getId())){
			sql.append(" and mc.`SUBJECT` = :subjectId ");
			params.put("subjectId", miniClass.getSubject().getId());
		}
		//期
		if(miniClass.getPhase()!=null && StringUtils.isNotEmpty(miniClass.getPhase().getId())){
			sql.append(" and mc.PHASE = :phaseId ");
			params.put("phaseId", miniClass.getPhase().getId());
		}
		//老师
		if(miniClass.getTeacher()!=null && StringUtils.isNotEmpty(miniClass.getTeacher().getUserId())){
			sql.append(" and mc.TEACHER_ID = :teacherId ");
			params.put("teacherId", miniClass.getTeacher().getUserId());
		}

	    //学管师
		if(miniClass.getStudyManeger()!=null && StringUtils.isNotEmpty(miniClass.getStudyManeger().getUserId())){
			sql.append(" and mc.STUDY_MANEGER_ID = :studyManegerId ");
			params.put("studyManegerId", miniClass.getStudyManeger().getUserId());
		}
		//校区
		if(miniClass.getBlCampus()!=null && StringUtils.isNotEmpty(miniClass.getBlCampus().getId())){
			sql.append(" and mc.BL_CAMPUS_ID = :blCampusId ");
			params.put("blCampusId", miniClass.getBlCampus().getId());
		}else{
			Organization campus = userService.getBelongCampus();
			if (campus != null && StringUtils.isNotEmpty( campus.getOrgLevel())) {
				String orgLevel = campus.getOrgLevel();
				sql.append(" and o.orgLevel like '"+orgLevel+"%'" );
			}
		}


		//班级状态
		if(miniClass.getStatus() != null && StringUtils.isNotEmpty(miniClass.getStatus().getValue())) {
			sql.append(" and mc.`STATUS` = :status ");
			params.put("status", miniClass.getStatus().getValue());
		}

		//产品季度
		if(miniClass.getProduct().getProductQuarter() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductQuarter().getId())) {
			sql.append(" and p.PRODUCT_QUARTER_ID = :productQuarterId ");
			params.put("productQuarterId", miniClass.getProduct().getProductQuarter().getId());
		}
		//产品年份
		if(miniClass.getProduct().getProductVersion() != null && StringUtils.isNotEmpty(miniClass.getProduct().getProductVersion().getId())) {
			sql.append(" and p.PRODUCT_VERSION_ID = :productVersionId ");
			params.put("productVersionId", miniClass.getProduct().getProductVersion().getId());
		}
		//是否绑定教材 
		if(miniClass.getBindTextBook()!=null){
			if(miniClass.getBindTextBook()){
				sql.append(" and mc.TEXTBOOK_ID is not null ");
			}else {
				sql.append(" and mc.TEXTBOOK_ID is null ");
			}
		}
		//在线销售或者校区销售
		if(StringUtils.isNotBlank(miniClass.getSaleType())){
			if("1".equals(miniClass.getSaleType())){
				sql.append(" and mc.ONLINE_SALE =0 ");
			}else if("2".equals(miniClass.getSaleType())){
				sql.append(" and mc.CAMPUS_SALE =0 ");
			}
		}

		if (StringUtils.isNotBlank(dataPackage.getSord())
				&& StringUtils.isNotBlank(dataPackage.getSidx())) {
			sql.append(" order by mc."+dataPackage.getSidx()+" "+dataPackage.getSord());
		}

		List<Map<Object,Object>> result = miniClassDao.findMapOfPageBySql(sql.toString(), dataPackage, params);
		return result;
	}

	@Override
	public CourseForWechatVo getCourseInfoForWechatById(String courseId) {
		CourseForWechatVo courseForWechatVo = new CourseForWechatVo();
		Course cousre = courseDao.findById(courseId);
		if (cousre != null) {
			//一对一
			CourseVo courseVo = HibernateUtils.voObjectMapping(cousre,
					CourseVo.class, "courseFullMapping");
			courseForWechatVo.setCourseId(courseId);

			courseForWechatVo.setProductName(courseVo.getGrade()+courseVo.getSubject());
			courseForWechatVo.setAuditHours(courseVo.getRealHours());
			courseForWechatVo.setPlanHours(courseVo.getPlanHours());
			courseForWechatVo.setCampusName(courseVo.getCampusName());
			courseForWechatVo.setCourseDate(courseVo.getCourseDate());
			courseForWechatVo.setCourseTime(courseVo.getCourseTime());
			courseForWechatVo.setCourseType(CourseWeChatType.OTO.getValue());
			courseForWechatVo.setCourseStatusName(courseVo.getCourseStatus().getName());
			courseForWechatVo.setTeacherName(courseVo.getTeacherName());
			courseForWechatVo.setStudyManagerName(courseVo.getStudyManagerName());
			courseForWechatVo.setCourseStatusName(courseVo.getCourseStatusName());
			courseForWechatVo.setAuditStatusName(cousre.getAuditStatus()!=null?cousre.getAuditStatus().getName():null);
			if(StringUtil.isNotBlank(courseVo.getAttendacePicName())){
				String ossUrl=PropertiesUtils.getStringValue("oss.access.url.prefix");
				courseForWechatVo.setAttendacePicName(ossUrl+courseVo.getAttendacePicName());
			}
			List<String> studentName = new ArrayList<>();
			studentName.add(courseVo.getStudentName());
			courseForWechatVo.setStudentName(studentName);

		}else{
			//一对多
			OtmClassCourse otmClassCourse= otmClassCourseDao.findById(courseId);
			if(otmClassCourse!=null){
				OtmClassCourseVo otmClassCourseVo = HibernateUtils.voObjectMapping(otmClassCourse, OtmClassCourseVo.class);
				OtmClass oc = otmClassDao.findById(otmClassCourseVo.getOtmClassId());
				courseForWechatVo.setCampusName(oc.getBlCampus().getName());

			//	User teacher = userDao.findById();
				courseForWechatVo.setTeacherName(otmClassCourseVo.getTeacherName());

				courseForWechatVo.setCourseId(courseId);
				courseForWechatVo.setProductName(oc.getName());
				courseForWechatVo.setAuditHours(new BigDecimal(otmClassCourseVo.getCourseHours()));
				courseForWechatVo.setPlanHours(new BigDecimal(otmClassCourseVo.getCourseHours()));

				courseForWechatVo.setCourseDate(otmClassCourseVo.getCourseDate());
				courseForWechatVo.setCourseTime(otmClassCourseVo.getCourseTime());
				courseForWechatVo.setCourseType(ProductType.ONE_ON_MANY.getValue());
				courseForWechatVo.setCourseStatusName(otmClassCourseVo.getCourseStatus() == null ? null : CourseStatus.valueOf(otmClassCourseVo.getCourseStatus()).getName());
				courseForWechatVo.setAuditStatusName(otmClassCourseVo.getAuditStatus()!=null?otmClassCourseVo.getAuditStatus().getName():null);

				List<OtmClassStudentAttendent> otmClassStudentAttendentList
		        =otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", courseId));

				if(otmClassStudentAttendentList!=null){
					String studyManagerName = "";
					for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
						if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName())) {
							studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
						}

					}
					if (studyManagerName.length() > 0) {
						studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
					}
					courseForWechatVo.setStudyManagerName(studyManagerName);
				}


				if(StringUtil.isNotBlank(otmClassCourseVo.getAttendacePicName())){
					String ossUrl=PropertiesUtils.getStringValue("oss.access.url.prefix");
					courseForWechatVo.setAttendacePicName(ossUrl+otmClassCourseVo.getAttendacePicName());
				}

				Map<String, Object> params = new HashMap<String, Object>();
				params.put("otmClassId", otmClassCourseVo.getOtmClassId());
				params.put("firstSchoolTime", otmClassCourseVo.getCourseDate());
				//报名的所有学生
				String sql ="select * from otm_class_student where OTM_CLASS_ID = :otmClassId and FIRST_SCHOOL_TIME <= :firstSchoolTime ";
				List<OtmClassStudent> list = otmClassStudentDao.findBySql(sql, params);
				List<String> studentName = new ArrayList<>();
				if(list!=null && !list.isEmpty()){
					for(OtmClassStudent student:list){
						studentName.add(student.getStudent().getName());
					}
				}
				courseForWechatVo.setStudentName(studentName);
			}

		}



		return courseForWechatVo;
	}

	@Override
	public DataPackage getTodayCourseBillList(DataPackage dataPackage, TodayCourseBillVo todayCourseBillVo) {

		// 今日课时单
		Map<String, Object> params = Maps.newHashMap();

		StringBuilder sql = new StringBuilder(512);
		sql.append(" select * from course where 1=1 ");

		if (StringUtil.isNotBlank(todayCourseBillVo.getStartDate())) {
			sql.append(" and COURSE_DATE = :startDate ");
			params.put("startDate", todayCourseBillVo.getStartDate());
		} else {
			sql.append(" and COURSE_DATE = :todayDate ");
			params.put("todayDate", DateTools.getCurrentDate());
		}
		if(StringUtil.isNotBlank(todayCourseBillVo.getCourseStatusName())){
			sql.append(" and COURSE_STATUS = :courseStatus ");
			params.put("courseStatus", todayCourseBillVo.getCourseStatusName());
		}

		if(StringUtil.isNotBlank(todayCourseBillVo.getCampusId())){
			sql.append(" and BL_CAMPUS_ID = :campusId ");
			params.put("campusId", todayCourseBillVo.getCampusId());
		}
		//#1760
//		if (StringUtil.isNotBlank(todayCourseBillVo.getStudyManagerName())) {
//			sql.append(" and STUDY_MANAGER_ID in(select USER_ID from `user` WHERE `NAME` LIKE :studyManagerName )");
//			params.put("studyManagerName", "%" + todayCourseBillVo.getStudyManagerName() + "%");
//		}
		if (StringUtil.isNotBlank(todayCourseBillVo.getStartTime())) {
			sql.append(" and TIME_TO_SEC(SUBSTRING(COURSE_TIME,1,5)) >=TIME_TO_SEC(:startTime)   ");
			params.put("startTime", todayCourseBillVo.getStartTime());
		}
		if (StringUtil.isNotBlank(todayCourseBillVo.getEndTime())) {
			sql.append(" and  TIME_TO_SEC(SUBSTRING(COURSE_TIME,9,13)) <=TIME_TO_SEC(:endTime)    ");
			params.put("endTime", todayCourseBillVo.getEndTime());
		}

		if (StringUtil.isNotBlank(todayCourseBillVo.getCourseAttenceType())) {
			sql.append(" and COURSE_ATTENCE_TYPE=:courseAttenceType ");
			params.put("courseAttenceType", todayCourseBillVo.getCourseAttenceType());
		}

		if (StringUtil.isNotBlank(todayCourseBillVo.getTeacherName())) {
			sql.append(" and TEACHER_ID in(select USER_ID from `user` WHERE `NAME` LIKE :teacherName ) ");
			params.put("teacherName", "%" + todayCourseBillVo.getTeacherName() + "%");
		}
		if (StringUtil.isNotBlank(todayCourseBillVo.getStudentName())) {
			sql.append(" and STUDENT_ID in (select ID from student WHERE `NAME` LIKE :studentName )");
			params.put("studentName", "%" + todayCourseBillVo.getStudentName() + "%");
		}

		if (StringUtil.isNotBlank(todayCourseBillVo.getSubjectId())) {
			sql.append(" and SUBJECT=:subject ");
			params.put("subject", todayCourseBillVo.getSubjectId());
		}

		//#1760
//		//添加权限
//		Organization organization = userService.getBelongCampus();
//		if(organization!=null){
//			sql.append(" and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' ) ");
//		}

		//#1760 20171128
		User user = userService.getCurrentLoginUser();
		String organizationId = user.getOrganizationId();
		Organization org = organizationDao.findById(organizationId);
		OrganizationType orgType = org.getOrgType();
		String orgLevel = "";
		if (OrganizationType.GROUNP == orgType || OrganizationType.BRENCH == orgType || OrganizationType.CAMPUS == orgType) {
			orgLevel = org.getOrgLevel();
		}else {
			Organization organization = userService.getBelongCampus();
			orgLevel = organization!=null?organization.getOrgLevel():org.getOrgLevel();
		}
		sql.append(" and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+orgLevel+"%' ) ");
		sql.append(" order by TEACHER_ID ,COURSE_ID desc ");

		dataPackage = courseDao.findPageBySql(sql.toString(), dataPackage, true, params);
		List<Course> list = (List<Course>) dataPackage.getDatas();
		List<CourseVo> courseVos = HibernateUtils.voListMapping(list, CourseVo.class, "courseFullMapping");
		if (courseVos != null) {
			List<TodayCourseBillVo> todayCourseBillVos = new ArrayList<>();
			TodayCourseBillVo vo = null;
			for (CourseVo courseVo : courseVos) {
				vo = new TodayCourseBillVo();
				vo.setCourseId(courseVo.getCourseId());
				vo.setCourseName(courseVo.getGrade() + courseVo.getSubject());
				vo.setCourseType(ProductType.ONE_ON_ONE_COURSE.getName());
				vo.setCourseDate(courseVo.getCourseDate());
				vo.setCampusName(courseVo.getCampusName());
				vo.setStudyManagerName(courseVo.getStudyManagerName());
				vo.setTeacherName(courseVo.getTeacherName());
				vo.setStudentName(courseVo.getStudentName());
				vo.setGradeName(courseVo.getGrade());
				vo.setSubjectName(courseVo.getSubject());
				vo.setCourseTime(courseVo.getCourseTime());
				vo.setPlanHours(courseVo.getPlanHours());
				vo.setRealHours(courseVo.getRealHours());
				vo.setPicName(courseVo.getAttendacePicName());
				vo.setCourseVersion(transferVersion(courseVo.getcVersion()));
		        vo.setCourseStatusName(courseVo.getCourseStatus()!=null?courseVo.getCourseStatus().getName():"");
		        vo.setCourseAttenceType(courseVo.getCourseAttenceType()!=null?courseVo.getCourseAttenceType().getName():"");
				if(StringUtil.isNotBlank(todayCourseBillVo.getCourseName())){
                    if(vo.getCourseName().indexOf(todayCourseBillVo.getCourseName())!=-1){
                    	todayCourseBillVos.add(vo);
                    }
				}else{
					todayCourseBillVos.add(vo);
				}

			}
			dataPackage.setDatas(todayCourseBillVos);
		}
		return dataPackage;
	}

	@Override
	public TodayCourseBillVo getCourseBillInfoById(String courseId) {
		TodayCourseBillVo todayCourseBillVo = new TodayCourseBillVo();
		Course cousre = courseDao.findById(courseId);
		if (cousre != null) {
			//一对一
			CourseVo courseVo = HibernateUtils.voObjectMapping(cousre,
					CourseVo.class, "courseFullMapping");
			todayCourseBillVo.setCourseId(courseId);
			todayCourseBillVo.setCourseName(courseVo.getGrade()+courseVo.getSubject());
			todayCourseBillVo.setPlanHours(courseVo.getPlanHours());
			todayCourseBillVo.setCampusName(courseVo.getCampusName());
			todayCourseBillVo.setCourseDate(courseVo.getCourseDate());
			todayCourseBillVo.setCourseTime(courseVo.getCourseTime());
			todayCourseBillVo.setCourseType(CourseWeChatType.OTO.getValue());
			todayCourseBillVo.setTeacherName(courseVo.getTeacherName());
			todayCourseBillVo.setStudyManagerName(courseVo.getStudyManagerName());
			todayCourseBillVo.setStudentName(courseVo.getStudentName());
			todayCourseBillVo.setSubjectName(courseVo.getSubject());
			todayCourseBillVo.setGradeName(courseVo.getGrade());

			todayCourseBillVo.setCourseVersion(transferVersion(courseVo.getcVersion()));



		}else{
			//一对多 以后可能做一对多  
//			OtmClassCourse otmClassCourse= otmClassCourseDao.findById(courseId);
//			if(otmClassCourse!=null){
//				OtmClassCourseVo otmClassCourseVo = HibernateUtils.voObjectMapping(otmClassCourse, OtmClassCourseVo.class);
//				OtmClass oc = otmClassDao.findById(otmClassCourseVo.getOtmClassId());
//				todayCourseBillVo.setCampusName(oc.getBlCampus().getName());
//				todayCourseBillVo.setTeacherName(otmClassCourseVo.getTeacherName());
//				todayCourseBillVo.setSubjectName(otmClassCourseVo.getSubjectName());
//				todayCourseBillVo.setGradeName(otmClassCourseVo.getGradeName());
//				todayCourseBillVo.setCourseId(courseId);
//				todayCourseBillVo.setCourseName(otmClassCourseVo.getGradeName()+otmClassCourseVo.getSubjectName());
//				todayCourseBillVo.setPlanHours(new BigDecimal(otmClassCourseVo.getCourseHours()));
//				
//				todayCourseBillVo.setCourseDate(otmClassCourseVo.getCourseDate());
//				todayCourseBillVo.setCourseTime(otmClassCourseVo.getCourseTime());
//				todayCourseBillVo.setCourseType(ProductType.ONE_ON_MANY.getValue());	
//
//				List<OtmClassStudentAttendent> otmClassStudentAttendentList 
//		        =otmClassStudentAttendentDao.findByCriteria(Expression.eq("otmClassCourse.otmClassCourseId", courseId));
//				
//				if(otmClassStudentAttendentList!=null){
//					String studyManagerName = "";
//					for(OtmClassStudentAttendent otmClassStudentAttendent : otmClassStudentAttendentList){
//						if (null != otmClassStudentAttendent.getStudyManager() && !studyManagerName.contains(otmClassStudentAttendent.getStudyManager().getName())) {
//							studyManagerName += otmClassStudentAttendent.getStudyManager().getName() + ",";
//						}
//
//					}
//					if (studyManagerName.length() > 0) {
//						studyManagerName = studyManagerName.substring(0, studyManagerName.length() - 1);
//					}
//					todayCourseBillVo.setStudyManagerName(studyManagerName);
//				}
//				
//				
//
//				
//				Map<String, Object> params = new HashMap<String, Object>();
//				params.put("otmClassId", otmClassCourseVo.getOtmClassId());
//				params.put("firstSchoolTime", otmClassCourseVo.getCourseDate());
//				//报名的所有学生
//				String sql ="select * from otm_class_student where OTM_CLASS_ID = :otmClassId and FIRST_SCHOOL_TIME <= :firstSchoolTime ";
//				List<OtmClassStudent> list = otmClassStudentDao.findBySql(sql, params);
//				List<String> studentName = new ArrayList<>();
//				if(list!=null && !list.isEmpty()){
//					for(OtmClassStudent student:list){
//						studentName.add(student.getStudent().getName());
//					}
//				}
//			}



		}



		return todayCourseBillVo;
	}

	@Override
	public Map<String, List<TodayCourseBillVo>> getCourseBillsPrint(String[] courseIds) {
		//目前只是获取一对一的课程
		//按老师ID分组

		//今日课时单
		Map<String, Object> params = Maps.newHashMap();

		String querySql ="select TEACHER_ID from course WHERE COURSE_ID in (:courseIds) GROUP BY TEACHER_ID ";
		params.put("courseIds", courseIds);
		List<Map<Object, Object>> teacherList = courseDao.findMapBySql(querySql, params);
		List<String> teachers = new  ArrayList<>();
		if(teacherList!=null && !teacherList.isEmpty()){
			for(Map<Object, Object> map:teacherList){
				if(map.get("TEACHER_ID")!=null){
					teachers.add((String)map.get("TEACHER_ID"));
				}
			}
		}else{
			return null;
		}
		Map<String, List<TodayCourseBillVo>> result = new HashMap<>();
		for(int i=0;i<teachers.size();i++){
			result.put(teachers.get(i), new ArrayList<TodayCourseBillVo>());
		}

		String sql ="select * from course where COURSE_ID in (:courseIds) order by TEACHER_ID ,COURSE_ID desc  ";

		List<Course> list=courseDao.findBySql(sql, params);
		List<CourseVo> courseVos= HibernateUtils.voListMapping(list, CourseVo.class,"courseFullMapping");
		if (courseVos != null) {
             result = transferResult(result, courseVos);
		}

		return result;
	}

	private Map<String, List<TodayCourseBillVo>> transferResult(Map<String, List<TodayCourseBillVo>> map,List<CourseVo> courseVos){
	    TodayCourseBillVo vo = null;
		for (CourseVo courseVo : courseVos) {
			String teacherId = courseVo.getTeacherId();
			vo = new TodayCourseBillVo();
			vo.setCourseId(courseVo.getCourseId());
			vo.setCourseName(courseVo.getGrade() + courseVo.getSubject());
			vo.setCourseType(CourseWeChatType.OTO.getValue());
			vo.setCourseDate(courseVo.getCourseDate());
			vo.setCampusName(courseVo.getCampusName());
			vo.setStudyManagerName(courseVo.getStudyManagerName());
			vo.setTeacherName(courseVo.getTeacherName());
			vo.setStudentName(courseVo.getStudentName());
			vo.setGradeName(courseVo.getGrade());
			vo.setSubjectName(courseVo.getSubject());
			vo.setCourseTime(courseVo.getCourseTime());
			vo.setPlanHours(courseVo.getPlanHours());
		    vo.setCourseVersion(transferVersion(courseVo.getcVersion()));
			map.get(teacherId).add(vo);
		}
		return map;
	}

	private String transferVersion(int version){
		if (version >= 1000) {
			return "" + (version+1);
		} else if (version >= 100) {
			return "0" + (version + 1);
		} else if (version >= 10) {
			return "00" + (version + 1);
		} else {
			return "000" + (version + 1);
		}
	}


	@Override
	public Map<String, List<TodayCourseBillVo>> getCourseBillsPrintByDate(String date) {
		//目前只是获取一对一的课程
		//按老师ID分组
		//今日课时单
		Organization organization = userService.getBelongCampus();

		Map<String, Object> params = Maps.newHashMap();
		String querySql = null;
		if(organization!=null){
			querySql ="select TEACHER_ID from course WHERE COURSE_DATE = :date and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' ) GROUP BY TEACHER_ID ";
		}else{
			querySql ="select TEACHER_ID from course WHERE COURSE_DATE = :date GROUP BY TEACHER_ID ";
		}

		params.put("date", date);
		List<Map<Object, Object>> teacherList = courseDao.findMapBySql(querySql, params);
		List<String> teachers = new  ArrayList<>();
		if(teacherList!=null && !teacherList.isEmpty()){
			for(Map<Object, Object> map:teacherList){
				if(map.get("TEACHER_ID")!=null){
					teachers.add((String)map.get("TEACHER_ID"));
				}
			}
		}else{
			return null;
		}
		Map<String, List<TodayCourseBillVo>> result = new HashMap<>();
		for(int i=0;i<teachers.size();i++){
			result.put(teachers.get(i), new ArrayList<TodayCourseBillVo>());
		}


		String sql = null;
		if(organization!=null){
			sql ="select * from course WHERE COURSE_DATE = :date and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' ) order by TEACHER_ID ,COURSE_ID desc  ";
		}else{
			sql ="select * from course WHERE COURSE_DATE = :date order by TEACHER_ID ,COURSE_ID desc  ";
		}
		List<Course> list=courseDao.findBySql(sql, params);
		List<CourseVo> courseVos= HibernateUtils.voListMapping(list, CourseVo.class,"courseFullMapping");
		if (courseVos != null) {
             result = transferResult(result, courseVos);
		}

		return result;
	}

	@Override
	public Map<String, Object> getBillsPrintInfoByDate(String date) {
		Map<String, Object> result = new HashMap<>();
		result.put("date", date);
		String week= DateTools.getWeekOfDate(DateTools.getDate(date));
		result.put("week", week);
		Map<String, Object> params = Maps.newHashMap();
		params.put("date", date);

		Organization organization = userService.getBelongCampus();
		String sql = null;
		if(organization!=null){
			sql ="select count(1) from course WHERE COURSE_DATE = :date and BL_CAMPUS_ID in (select id from organization where orgLevel LIKE '"+organization.getOrgLevel()+"%' ) order by TEACHER_ID ,COURSE_ID desc  ";
		}else{
			sql ="select count(1) from course WHERE COURSE_DATE = :date order by TEACHER_ID ,COURSE_ID desc  ";
		}



		int count=courseDao.findCountSql(sql, params);
		result.put("count", count);
		return result;
	}

	@Override
	public Response auditAutoRecog(AuditAutoRecogVo courseVo) {
		Response response = new Response();

		Map<String, Object> params = Maps.newHashMap();
		StringBuffer sql = new StringBuffer(" select * from course c where 1=1 ");

		if (StringUtils.isNotBlank(courseVo.getStartDate())) {
			sql.append(" AND c.COURSE_DATE >= :startDate ");
			params.put("startDate", courseVo.getStartDate());
		}
		if (StringUtils.isNotBlank(courseVo.getEndDate())) {
			sql.append(" AND c.COURSE_DATE <= :endDate ");
			params.put("endDate", courseVo.getEndDate());
		}
//		if (courseVo.getCourseStatus() != null) {
//			sqlWhere.append("AND  c.COURSE_STATUS =  :courseStatus ");
//			params.put("courseStatus", courseVo.getCourseStatus());
//		}
		//增加考勤类型的条件查询
		sql.append(" and c.COURSE_ATTENCE_TYPE ='"+CourseAttenceType.AUTO_RECOG.getValue()+"' ");
		 //查询未审批的
		sql.append(" AND (c.AUDIT_STATUS is null or c.AUDIT_STATUS='UNAUDIT') ");


		if (StringUtils.isNotBlank(courseVo.getSubject())) {

			String[] subjects=StringUtil.replaceSpace(courseVo.getSubject()).split(",");
			if(subjects.length>0){
				sql.append(" and c.SUBJECT in (:subjects) ");
				params.put("subjects", subjects);

			}
		}
		if(StringUtils.isNotBlank(courseVo.getCampusId())){
			sql.append(" AND c.BL_CAMPUS_ID= :campusId ");
			params.put("campusId", courseVo.getCampusId());
		}

//        if("financeAudit".equals(courseVo.getCurrentRoleId())) {
//			if (StringUtils.isNotBlank(courseVo.getCampusId())) {
//				sqlWhere.append(" AND  c.BL_CAMPUS_ID = :courseCampusId ");
//				params.put("courseCampusId", courseVo.getCampusId());
//		}
//			
//		} else{
//			sqlWhere.append(" and 1=2");
//		}

		sql.append(" ORDER BY c.GRADE ASC ");
		List<Course> list = courseDao.findBySql(sql.toString(), params);
		if(list!=null && !list.isEmpty()){
		    for (int i = 0; i < list.size(); i++) {
				Course course = list.get(i);
				course.setAuditStatus(AuditStatus.VALIDATE);
				if(i % 20 == 0){//每20条刷新一下数据库
					courseDao.flush();
				}
			}
		}
		return response;
	}

	@Override
	public Map<String, Object> getOneOnOneCourseInfo(CourseSearchInputVo courseSearchInputVo) {
		Map<String, Object> map = new HashMap<String, Object>();

		StringBuffer querySql = new StringBuffer();

		//查询一对一course

		Map<String, Object> params = Maps.newHashMap();
			// 一对一
		querySql.append(" select p.`NAME` as courseName,c.COURSE_ID as courseId,c.STUDENT_ID as studentId,c.PRODUCT_ID as productId,concat(c.COURSE_DATE,'') as courseDate,c.COURSE_STATUS as status, ");
		querySql.append(" c.TEACHER_ID as teacherId,c.SUBJECT as subjectId,c.GRADE as gradeId,");
		querySql.append(" c.BL_CAMPUS_ID as organizationId,c.COURSE_TIME as courseTime,c.PLAN_HOURS as planHours ");
		querySql.append(" ,u.`NAME` as teacherName,d.`NAME` as subjectName,dd.`NAME` as gradeName,s.`NAME` as studentName,o.`name` as organizationName,p.CLASS_TIME_LENGTH as classTimeLength ");
		querySql.append(" ,r.`name` as regionName,r.id as regionId,concat(og.id,'') as branchId,og.`name` as branchName ");
		querySql.append(" from course c");
		querySql.append(" left join `user` u on u.USER_ID = c.TEACHER_ID ");
		querySql.append(" left join data_dict d on d.ID = c.`SUBJECT` ");
		querySql.append(" left join data_dict dd on dd.ID = c.GRADE ");
		querySql.append(" left join student s on s.ID = c.STUDENT_ID ");
		querySql.append(" left join organization o on o.id = c.BL_CAMPUS_ID ");
		querySql.append(" left join organization og on og.id = o.parentID ");
		querySql.append(" left join product p on p.ID = c.PRODUCT_ID ");
		querySql.append(" left join region r on r.id = o.city_id ");
		querySql.append(" where 1=1 ");
		if (courseSearchInputVo.getCourseId() != null) {
				querySql.append(" and c.COURSE_ID = :courseId ");
				params.put("courseId", courseSearchInputVo.getCourseId());
		}
		querySql.append(" limit 1 ");

		List<Map<Object, Object>> oneOnoneResult = courseDao.findMapBySql(querySql.toString(),params);
		StringBuffer query = new StringBuffer();
		OneOnOneCourseInfoVo courseInfoVo = null;
		if(oneOnoneResult==null || (oneOnoneResult!=null &&oneOnoneResult.size()==0)){
			map.put("resultStatus", 200);
			map.put("resultMessage", "课程信息");
			map.put("result", courseInfoVo);
			return map;
		}

		for (Map<Object, Object> result : oneOnoneResult) {
			courseInfoVo = new OneOnOneCourseInfoVo();
			courseInfoVo.setCourseId(String.valueOf(result.get("courseId")));
			courseInfoVo.setCourseName(result.get("courseName")!=null?result.get("courseName").toString():"");
			courseInfoVo.setClassNameTwo("");
			courseInfoVo.setType(ProductType.ONE_ON_ONE_COURSE.getValue());// 一对一
			courseInfoVo.setState(String.valueOf(result.get("status")));
			courseInfoVo.setTeacherId(String.valueOf(result.get("teacherId")));
			courseInfoVo.setTeacherName(String.valueOf(result.get("teacherName")));
			courseInfoVo.setSubjectId(String.valueOf(result.get("subjectId")));
			courseInfoVo.setSubjectName(String.valueOf(result.get("subjectName")));
			courseInfoVo.setGradeId(String.valueOf(result.get("gradeId")));
			courseInfoVo.setGradeName(String.valueOf(result.get("gradeName")));
			courseInfoVo.setAudiencesId(String.valueOf(result.get("studentId")));// 一对一是写学生编号以及学生姓名
			courseInfoVo.setAudiencesName(String.valueOf(result.get("studentName")));
			courseInfoVo.setOrganizationId(result.get("organizationId")!=null?result.get("organizationId").toString():"");
			courseInfoVo.setOrganizationName(result.get("organizationName")!=null?result.get("organizationName").toString():"");
			courseInfoVo.setRegionId(result.get("regionId")!=null?result.get("regionId").toString():"");
			courseInfoVo.setRegionName(result.get("regionName")!=null?result.get("regionName").toString():"");
			courseInfoVo.setCourseDate(String.valueOf(result.get("courseDate")));
			courseInfoVo.setBranchId(result.get("branchId")!=null?result.get("branchId").toString():"");
			courseInfoVo.setBranchName(result.get("branchName")!=null?result.get("branchName").toString():"");
			String courseTime = String.valueOf(result.get("courseTime"));
			if (courseTime != null) {
				courseInfoVo.setCourseTime(courseTime.split("-")[0]);
				courseInfoVo.setCourseEndTime(courseTime.split("-")[1].trim());
			}
			// planHours
			String planHours = String.valueOf(result.get("planHours"));// 用于计算period
			// courseMinutes
			String productId = String.valueOf(result.get("productId"));
			Integer courseMinutes = Integer.parseInt(result.get("classTimeLength")!=null?result.get("classTimeLength").toString():"0");
			// 如果courseMinutes==null 默认是45分钟
			if (courseMinutes==null||courseMinutes==0)
				courseMinutes = 45;
			courseInfoVo.setCourseMinutes(courseMinutes.toString());
			BigDecimal period = new BigDecimal(planHours);
			period = period.multiply(new BigDecimal(courseMinutes));// planHours * courseMinutes
			courseInfoVo.setPeriod(period.toString());

			// remainingTime
			// 一对一是计算该学生所有的合同产品的剩余
			query.append(
						" select cp.PRICE as price,case cp.PAID_STATUS WHEN 'PAID' THEN sum(cp.PAID_AMOUNT +cp.PROMOTION_AMOUNT-cp.CONSUME_AMOUNT) ");
			query.append(" WHEN 'PAYING' THEN sum(cp.PAID_AMOUNT -cp.CONSUME_AMOUNT) END  as money ");
			query.append(" from contract_product cp ");
			query.append(" left join contract c on c.id=cp.contract_id ");
			query.append(" WHERE cp.TYPE='ONE_ON_ONE_COURSE' and (cp.`STATUS`  ='NORMAL' or cp.`STATUS` ='FROZEN') ");
			query.append(" and c.STUDENT_ID = '" + result.get("studentId") + "' and cp.PRODUCT_ID= :productId ");
			Map<String, Object> param = Maps.newHashMap();
			param.put("productId", productId);
			List<Map<Object, Object>> moneyResult = courseDao.findMapBySql(query.toString(),param);
			if (moneyResult != null && moneyResult.size() > 0) {
				BigDecimal money = new BigDecimal(moneyResult.get(0).get("money")!=null?moneyResult.get(0).get("money").toString():"0");
				BigDecimal price = new BigDecimal(moneyResult.get(0).get("price")!=null?moneyResult.get(0).get("price").toString():"0");
				if(moneyResult.get(0).get("money")!=null &&moneyResult.get(0).get("price")!=null){
					courseInfoVo.setRemainingTime(money.divide(price,2,BigDecimal.ROUND_HALF_UP).toString());
				}else{
					courseInfoVo.setRemainingTime("0");
				}
			}
			query.delete(0, query.length());

			//课程顺序
			courseInfoVo.setCourseNo(0);
			//学生是不是新学生
			courseInfoVo.setNewStudentFlag(false);
			if (courseSearchInputVo.getCourseId() != null) {
				if(courseSearchInputVo.getCourseId().equals(courseInfoVo.getCourseId())){
					String sql = "select * from course where STUDENT_ID ='"+result.get("studentId")+"' and `SUBJECT` ='"+result.get("subjectId")+"' ORDER BY COURSE_DATE";
					String sql2 = "select * from course where STUDENT_ID ='"+result.get("studentId")+"' and `SUBJECT` ='"+result.get("subjectId")+"' and COURSE_DATE ='"+CourseStatus.NEW.getValue()+"' ORDER BY COURSE_DATE";
					List<Course> list = courseDao.findBySql(sql, new HashMap<>());
					List<Course> list2 = courseDao.findBySql(sql, new HashMap<>());

					if(list==null ||(list!=null && list.isEmpty())){
						courseInfoVo.setNewStudentFlag(true);
					}else{
						if(list2!=null && (list.size()== list2.size())){
							courseInfoVo.setNewStudentFlag(true);
						}
					}

					if(list!=null && list.size()>0){
						int courseSize = list.size();
						for(int i= 0;i<courseSize;i++ ){
							if(courseInfoVo.getCourseId().equals(list.get(i).getCourseId())){
								courseInfoVo.setCourseNo(i+1);
								break;
							}
						}
					}
				}
			}
		} // end 一对一的查询

		map.put("resultStatus", 200);
		map.put("resultMessage", "课程信息");
		map.put("result", courseInfoVo);
		return map;
	}

	@Override
	public Course getCourseByCourseId(String courseId) {
		Course course = courseDao.findById(courseId);
		return course;
	}

	@Override
	public void updateCourse(Course course) {
		courseDao.save(course);
	}

	@Override
	public void updateCoursePicName(String courseId, String fileName) {
		String sql = "update course set ATTENDANCE_PIC_NAME = :picName where COURSE_ID = :courseId ";
        Map<String,Object> params= new HashMap<>();
        params.put("picName",fileName);
        params.put("courseId",courseId);
		courseDao.excuteSql(sql, params);
	}

	/**
	 * 根据学生成绩基准查询学生在读一对一科目
	 */
    @Override
    public NameValue getSubjectsByBenchmark(
            BenchmarkSubjectSearchVo benchmarkSubjectSearchVo) {
        NameValue result = null;
        DataDict schoolYear = dataDictDao.findById(benchmarkSubjectSearchVo.getSchoolYearId());
        if (schoolYear == null) {
            throw new ApplicationException("学年查不到");
        }
        String startYear = schoolYear.getName().substring(0, 4);
        String startDate = "";
        String endDate = "";
        if (benchmarkSubjectSearchVo.getSemester() == Semester.LAST_SEMESTER) {
            String endYear = schoolYear.getName().substring(5, 9);
            startDate += startYear + "-09-02";
            endDate += endYear + "-03-01";
        } else {
            startDate += startYear + "-03-02";
            endDate += startYear + "-09-01";
        }
        String sql = " select DISTINCT `SUBJECT` subject_id, (select name from data_dict where id = `SUBJECT`) subject_name "
                + " from course where course_date >= :startDate and course_date <= :endDate and STUDENT_ID = :studentId ";
        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("studentId", benchmarkSubjectSearchVo.getStudentId());
        List<Map<Object, Object>> list = courseDao.findMapBySql(sql, params);
        if (list != null && list.size() > 0) {
            String name = "";
            String value = "";
            for (Map<Object, Object> map : list) {
                name += map.get("subject_name") + "，";
                value += map.get("subject_id") + ",";
            }
            name = name.substring(0, name.length() - 1);
            value = value.substring(0, value.length() - 1);
            result = SelectOptionResponse.buildNameValue(name, value);
        }
        return result;
    }

    @Override
    public Map<String, Object> checkCourseMeetConditions(String studentId,
            String subejctId, Integer hours, Integer days, String createDate) {
        Map<String, Object> result = Maps.newHashMap();
        Map<String, Object> params = Maps.newHashMap();
        params.put("studentId", studentId);
        params.put("subejctId", subejctId);
        String isOneOnOneSql = " select count(*) from contract_product_subject cps left join contract_product cp on cps.CONTRACT_PRODUCT_ID = cp.id left join contract c on cp.CONTRACT_ID = c.ID "
                + " where cps.SUBJECT_ID = :subejctId and STUDENT_ID = :studentId ";
        int isOneOnOneCount = courseDao.findCountSql(isOneOnOneSql, params);
        if (isOneOnOneCount <= 0) {
            result.put("status", false);
            result.put("readOnlyDecs", "本科目不是一对一科目");
            return result;
        }
        String hoursSql = " select sum(REAL_HOURS) from course where COURSE_STATUS = 'CHARGED' and STUDENT_ID = :studentId and SUBJECT = :subejctId ";
        double courseHours = courseDao.findSumSql(hoursSql, params);
        if (courseHours < hours.intValue()) {
            result.put("status", false);
            result.put("readOnlyDecs", "本科目一对一课消不足" + hours.intValue() + "课时");
            return result;
        }
        createDate = createDate == null ? DateTools.getCurrentDate() : createDate;
        params.put("courseDate", DateTools.addDateToString(createDate, -days));
        String daysSql = " select count(*) from account_charge_records acr, course c  where acr.COURSE_ID = c.COURSE_ID AND acr.CHARGE_TYPE = 'NORMAL' and acr.CHARGE_PAY_TYPE = 'CHARGE' AND acr.IS_WASHED = 'FALSE' "
                + " and c.STUDENT_ID = :studentId and c.SUBJECT = :subejctId and acr.PAY_TIME >= :courseDate ";
        int daysHours = courseDao.findCountSql(daysSql, params);
        if (daysHours <= 0) {
            result.put("status", false);
            result.put("readOnlyDecs", "对比成绩录入前" + days.intValue() + "天内本科目一对一课消为0");
            return result;
        }
        result.put("status", true);
        return result;
    }

}
