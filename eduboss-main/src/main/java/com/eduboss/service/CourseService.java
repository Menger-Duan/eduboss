package com.eduboss.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;




import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.AttendanceType;
import com.eduboss.common.CourseStatus;
import com.eduboss.common.RoleCode;
import com.eduboss.domain.Course;
import com.eduboss.domain.CourseSummary;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassStudentAttendent;
import com.eduboss.domain.StudnetAccMv;
import com.eduboss.domain.TwoTeacherClassCourse;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AuditAutoRecogVo;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CommonClassCourseVo;
import com.eduboss.domainVo.CourseAttendaceStatusCountVo;
import com.eduboss.domainVo.CourseAttendanceRecordVo;
import com.eduboss.domainVo.CourseSummarySearchResultVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.HasOneOnOneCourseVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MultiStudentProductSubjectVo;
import com.eduboss.domainVo.OneOnOneBatchAttendanceEditVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.SmallClassExcelVo;
import com.eduboss.domainVo.TextBookBossVo;
import com.eduboss.domainVo.TodayCourseBillVo;
import com.eduboss.domainVo.wechatVo.CourseForWechatVo;
import com.eduboss.dto.BenchmarkSubjectSearchVo;
import com.eduboss.dto.CourseEditVo;
import com.eduboss.dto.CourseRequirementEditVo;
import com.eduboss.dto.CourseRequirementSearchInputVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.CourseSummarySearchInputVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.GridRequest;
import com.eduboss.dto.MutilCourseVo;
import com.eduboss.dto.Response;
import com.eduboss.dto.SelectOptionResponse.NameValue;

public interface CourseService {

	/**
	 * 课程列表
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseList(CourseSearchInputVo courseSearchInputVo,
			DataPackage dp);
	
	public DataPackage getCourseList(CourseSearchInputVo courseSearchInputVo, DataPackage dp, String voMapperId);
	
	/**
	 * 获取校区课程表
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getSchoolZoneCourseList(
			CourseSearchInputVo courseSearchInputVo, DataPackage dp);
	
	/**
	 * 获取校区课程表2
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getSchoolZoneCourseList2(
			CourseSearchInputVo courseSearchInputVo, DataPackage dp);
	
	
	/**
	 * 找到相应的 一对一本周课表	 
	 * @param courseSearchInputVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findPageCourseForOneWeek(CourseSearchInputVo courseSearchInputVo, DataPackage  dataPackage);
	
	/**
	 * 获取一对一批量考勤列表
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneBatchAttendanceList(CourseVo inputCourseVo,
			DataPackage dp);
	
	/**
	 * 获取一对一批量考勤列表手机端
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneBatchAttendanceListForMobile(CourseVo inputCourseVo,
			DataPackage dp);

	/**
	 * 获取大课表
	 * @param courseOverviewSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseSummaryList(
			CourseSummarySearchInputVo courseOverviewSearchInputVo,
			DataPackage dp);

	/**
	 * 保存课程概要
	 * @param courseSummary
	 */
	public void saveCourseSunmmary(CourseSummary courseSummary);
	
	/**
	 * 学管排课需求
	 * @param courseRequirementSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseRequirementList(
			CourseRequirementSearchInputVo courseRequirementSearchInputVo,
			DataPackage dp);
			
	public void saveCourseList(Course[] courseList, CourseSummarySearchResultVo courseSummaryVo);
	
	public void saveMutilCourseList(MutilCourseVo[] mutilCourseVoList, CourseSummarySearchResultVo[] courseSummaryVoList, String transactionUuid);

	/**
	 * 编辑排课需求
	 * @param courseRequirementEditVo
	 */
	void saveOrUpdateCourseRequirement(CourseRequirementEditVo courseRequirementEditVo);

	/**
	 * 删除排课需求
	 * @param courseRequirementEditVo
	 */
	void deleteCourseRequirement(CourseRequirementEditVo courseRequirementEditVo);
	
	/**
	 * 查询单个排课需求
	 * @param courseRequirementId
	 * @return
	 */
	public CourseRequirementEditVo findCourseRequirementById(String courseRequirementId);
	
	/**
	 * 获取要批量更改课程的列表
	 * @param courseChangesSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseChangesList(
			CourseSearchInputVo courseSearchInputVo,
			DataPackage dp);
	

	/**
	 * 根据id查询课程详情
	 * @param courseId
	 * @return
	 */
	public CourseVo findCourseById(String courseId);
	
	/**
	 * 提交实际课时（考勤）
	 * @param courseId
	 * @param courseHours
	 * @param modifyUser
	 * @param roleCode
	 * @throws Exception 
	 */
	public void submitCourseAttendance(String courseId, String courseHours, String courseTime, User modifyUser, RoleCode roleCode) throws Exception;
	
	
	/**
	 * 根据条件查询考勤记录
	 * @param record
	 * @return
	 */
	public DataPackage getAttendanceRecordVos(CourseAttendanceRecordVo record, DataPackage dataPackage);
	
	/**
	 * 提交审核课时
	 * @param courseId
	 * @param auditCourseHours
	 * @param modifyUser
	 */
	public void submitCourseAudit(String courseId, BigDecimal auditCourseHours, User modifyUser);


    /**
     * 根据考勤编号获取学生课表
     * @param attendanceType
     * @param number
     * @param date
     * @return
     * @throws Exception
     */
	@Deprecated
    public DataPackage readCourseListByAttendanceType(AttendanceType attendanceType, String number, String date) throws Exception;

	/**
	 * 批量修改课程属性
	 * @param changesAttr
	 * @param changesData
	 * @param ids
	 * @param courseChangesSearchInputVo
	 * @throws Exception 
	 */
	void courseAttrChanges(String changesAttr, String changesData, String ids,
			CourseSearchInputVo courseSearchInputVo) throws Exception;
	
	/**
	 * 批量修改课程的老师
	 * @param teacherUserId
	 * @param ids
	 * @param courseSearchInputVo
	 */
	void batchChangeTeacher(String teacherUserId, String ids);
	
	/**
	 * 批量修改客户年级
	 * @param gradeId
	 * @param ids
	 */
	public void batchChangeCourseGrade (String gradeId, String ids);

	/**
	 * 删除某一课程
	 * @param courseEditVo
	 */
	void deleteCourse(CourseEditVo courseEditVo);

    /**
     * 删除考勤记录
     * @param ids
     */
    void deleteCourseAttendances(String ids);

	/**
	 * 添加或更新某一课程
	 * @param courseEditVo
	 */
	void saveOrUpdateCourse(CourseEditVo courseEditVo);
	
	/**
	 * 修改某一课程
	 * @param courseEditVo
	 */
	public void modifyCourse(CourseEditVo courseEditVo);
	
	/**
	 * 课程收费
	 * @param courseId
	 * @param modifyUser
	 */
	public void chargeCourse(String courseId, User modifyUser );

	/**
	 * 获取当前登录老师的课程表
	 * @param dp
	 * @return
	 */
	public DataPackage getTeacherCourseList(DataPackage dp);
	
	/**
	 * 保存单个单元格修改的数据
	 * @param courseEditVo
	 */
	public void courseCellEdit(CourseEditVo courseEditVo);

	/**
	 * 查询某一排课需求的所有已排课程
	 * @param courseRequirementId
	 * @param startDate
	 * @param endDate
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseRequirementArrengedCourseList(String courseRequirementId,
			String startDate, String endDate, DataPackage dp);

	/**
	 * 一对一批量考勤编辑
	 * @param oneOnOneBatchAttendanceEditVo
	 * @throws Exception 
	 */
	public void oneOnOneBatchAttendanceEdit(
			OneOnOneBatchAttendanceEditVo attendanceEditVo) throws Exception;

	/**
	 * 批量考勤提交
	 * @param vos
	 */
	public void mutilAttendaceSubmit(List<OneOnOneBatchAttendanceEditVo> vos) throws Exception;
	
	/**
	 * 一对一审核提交
	 */
	public void oneOnOneAuditSubmit(String courseId, String auditStatus);
	
	/**
	 * 获得老师课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception 
	 */
	DataPackage getTeacherCourseScheduleList(String teacherId, Date start,
			Date end, Boolean isAllCourseStatus) throws Exception;
	
	/**
	 * 获取某课程时间段的老师课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	DataPackage getTeacherCourseScheduleListByCourseTime(String teacherId, Date start, Date end, 
			String courseStartTime, String courseEndTime) throws Exception;
	
	/**
	 * 获得老师小班课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getTeacherMiniClassCourseScheduleList(String teacherId, Date start, Date end) throws Exception;
	
	/**
	 * 获取某课程时间段的老师小班课程表
	 * @param teacherId
	 * @param start
	 * @param end
	 * @param courseStartTime
	 * @param courseEndTime
	 * @return
	 */
	DataPackage getTeacherMiniClassCourseScheduleListByCourseTime(String teacherId, Date start, Date end, 
			String courseStartTime, String courseEndTime) throws Exception;
	
	/**
	 * 获取学生课程表
	 * @param studentId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public DataPackage getStudentCourseScheduleList(String studentId, Date start,
			Date end) throws Exception;

	/**
	 * 获取老师某天的学生课程表
	 * @param studentId
	 * @param courseDate
	 * @return
	 * @throws Exception
	 */
	public DataPackage getStudentCourseScheduleListByTeacher(String teacherId, String courseDate) throws Exception;
	
	/**
	 * 获取学生小班课程表
	 * @param studentId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public DataPackage getStudentMiniClassCourseScheduleList(String studentId, Date start, Date end) throws Exception ;
	
	/**
	 * 获取学生一对多课程表
	 * @param studentId
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public DataPackage getStudentOtmClassCourseScheduleList(String studentId, Date start, Date end) throws Exception ;


	/**
	 * 删除多条课程
	 * @param strCourseIds
	 */
	public void deleteMultiCourse(String strCourseIds);
	
	public void updateCourseStatus(String courseId, CourseStatus courseStatus);

    /**
     * 复制课程
     * @param origDate 源日期
     * @param destDate 目标日期
     */
	public void copyCourse(String origDate,String destDate, String transactionUuid);
	
	/**
	 * 复制多条课程
	 * @param strCourseIds
	 */
	public void copyMultiCourse(String strCourseIds, String destDate, String transactionUuid);

    /**
     * 删除一条课程概要
     * 删除课程概要将删除该概要下所有今天以后的课程
     * 课程概要删除为逻辑删除 delFlag=1
     * @param id
     */
    public void deleteCourseSummary(String id);

	public void saveMiniCourseList(MiniClassCourse[] miniClassCourseList, String arrangedHours);
	
    /**
     * 根据学生id 查询课程概要
     *@param teacherId
     *@return
     */
    public List<CourseSummarySearchResultVo> getCourseSummaryByTeacherId(String teacherId);

	/**
	 * 因为学生是不需要查询权限的， 只要求查到自己的课程，与 getSchoolZoneCourseList2 一致， 只是去除了角色的权限 ， ### 学生 app 的查询 ###
	 * @param searchInputVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getSchoolZoneCourseList2ForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dataPackage);
	/**
     * 根据查询条件查找小班课程
     * @param conditionHql
     * @return
     */
	public List<MiniClassCourse> getCourseByCondition(String conditionHql,Map<String, Object> params);
	
	/**
	 * 根据小班课程ID查找小班上课记录
	 * @param id
	 * @return
	 */
	public List<MiniClassStudentAttendent> getMiniClassStudentAttendentById(String id);
	
	/**
	 * 老师请假，取消请假
	 * @param courseId
	 * @param courseStatus
	 * @return
	 */
	public void teacherAskForLeave(String courseId, String courseStatus);
	
	
	
	/**
	 * 发送短信通知老师或者家长
	 * @param courseId
	 * @param Type
	 */
	public void sendMessageByCourse(String courseId, String Type);
	
	
	/**
	 * 找到相应的 一对一当天课表 
	 * @param courseSearchInputVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findPageCourseForToday(CourseSearchInputVo courseSearchInputVo, DataPackage  dataPackage);
	
	
		/**
	 * 找到相应的课程 # 主要是针对学生端， 没有携带了 session的role 信息 #
	 * @param searchInputVo
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findPageCourseForOneWeekForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dataPackage);
	
	
		/**
	 * 保存一对一考勤 签名图片
	 * @param courseId
	 * @param attendancePicFile
	 */
	public void saveCourseAttendancePic(String courseId,MultipartFile attendancePicFile,String servicePath) throws Exception;
	


	/**
	 * 判断 一个老师 在 某一天有无 一对一的课程  
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasOneOnOneCourseForDate(String teacherId, String date, List<CourseStatus> listOfStatus);

	/**
	 * 检查一段时间内的 老师 的一对一 有无课程， 分别是   无课，有课未考勤，有课全考勤
	 * @param teacherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDate(String teacherId,
			String startDate, String endDate);
	
	/**
	 * 一段时间内，根据考勤状态判断是否有课
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<HasOneOnOneCourseVo> checkHasOneOnOneCourseForPeriodDateByStatu(String status,
			String startDate, String endDate);

	/**
	 * 根据学生Id找到排课需求
	 * @param studentId
	 * @return
	 */
	public List<CourseRequirementEditVo> findCourseRequirementByStudentId(String studentId);
	
	/**
	 * 查找用户当天的全部课程
	 * @param dataPackage
	 * @return
	 */
	public DataPackage findCourseDay(String courseDate,DataPackage dataPackage);
	
	/**
	 * 一对一课程审批与查看汇总
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneAuditAnalyzeList(CourseVo inputCourseVo,
			DataPackage dp);
	
	
	/**
	 * 小班课时汇总
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param organizationIdFinder
	 * @return
	 */
	public DataPackage MiniClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String miniClassTypeId);
	
	
	/**
	 * 小班点击已排班次查看课程详情
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param courseVo
	 * @return
	 */	
	public DataPackage getSmallClassCourseInfo(DataPackage dp, String startDate, String campusId,
			String endDate, MiniClassCourseVo miniClassCourseVo);
	
	/**
	 * 双师点击已排班次查看课程详情
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param courseVo
	 * @return
	 */	
	public DataPackage getTwoTeacherClassCourseInfo(DataPackage dp, String startDate, String campusId,
			String endDate, MiniClassCourseVo miniClassCourseVo);
	
	/**
	 * 一对多课时汇总
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param organizationIdFinder
	 * @param otmClassTypeId
	 * @return
	 */
	public DataPackage getOtmClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String otmClassTypeId);
	
	
	public DataPackage getOtmCourseInfo(DataPackage dp, String campusId,
			OtmClassCourseVo otmClassCourseVo);
	
	public DataPackage excelOtmCourseInfo(DataPackage dp, String campusId,
			OtmClassCourseVo otmClassCourseVo);
	/**
	 * 小班课程审批汇总
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param organizationIdFinder
	 * @param teacherId
	 * @param AuditStatus
	 * @return
	 */
	public DataPackage getMiniClassCourseAuditAnalyze(DataPackage dataPackage,BasicOperationQueryVo vo,String AuditStatus,String anshazhesuan,String productQuarterSearch);
	
	/**
	 * 小班批量考勤列表
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	
	public DataPackage miniClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String anshazhesuan,String subject,String productQuarterSearch);
	
	/**
	 * 小班课程审批汇总（工资）
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param miniClassTypeId
	 * @return
	 */
	public DataPackage miniClaCourseAuditAnalyzeSalary(DataPackage dataPackage,BasicOperationQueryVo vo,String AuditStatus,String anshazhesuan,String productQuarterSearch);

	/**
	 * 获取不同课程的列表（目前包括小班和一对一）
	 * @param studentId
	 * @param startTime
	 * @param endTime
	 * @param dataPackage
	 * @throws Exception 
	 */
	public List<CommonClassCourseVo> getDifferentClassCourseList(String studentId, String startDate, String endDate) throws Exception;

	/**
	 * 
	 * @param courseId
	 * @param productType  (小班、精英版)
	 * @description 描述  课程详情,list大小大于1，则表示课程有冲突，并返回有冲突的课程
	 * @author wmy
	 * @date 2015年11月11日上午11:52:51
	 * @return List
	 */
	public List<CommonClassCourseVo> getCourseDetailsThatMaybeConfig(String courseId, String productType);


	/**
	 * 检查复制单日课程
	 * @param origDate
	 * @param destDate
	 * @return
	 */
    Map<String,String> checkCopyCourse(String origDate, String destDate);

	/**
	 * 检查复制多条课程
	 * @param courseIds
	 * @param destDate
	 * @return
	 */
	Map<String,String> checkCopyMultiCourse(String courseIds, String destDate);


	/**
	 * 检查已排课时是否超过剩余课时
	 * @param studentId
	 * @param courseHours
	 * @param courseSummaryId
	 * @param courseId
	 * @return
	 */
	boolean checkAheadOfOneOnOneRemainingHour(String studentId, BigDecimal courseHours, String courseSummaryId, String courseId);
	/**
	 * 一对一排课时检查是否超科目分配课时
	 * @param productId
	 * @param studentId
	 * @param subjectId
	 * @param courseHours
	 * @return
	 */
	boolean checkContractProductSubjectHours(String productId, String studentId, String subjectId, 
			BigDecimal courseHours, String courseSummaryId, String courseId);
	
	/**
	 * 一对一非规律排课时检查是否超科目分配课时
	 * @param multiStudentProductSubjectVo
	 * @return
	 */
	Response multiCheckContractProductSubjectHours(MultiStudentProductSubjectVo multiStudentProductSubjectVo);
	
	/**
	 * 根据条件查询课程信息
	 * @param courseSearchInputVo
	 * courseSearchaInputVo 主要封装如下参数:
	 * teacherId/studentId/courseId/startDate/endDate
	 */
	Map<String, Object> getCourseInfo(CourseSearchInputVo courseSearchInputVo);
	
	/**
	 * 查询课程内学生信息
	 * @param courseId
	 * @return
	 */
	Map<String, Object> getCourseStudentInfo(String courseId,String type);


	/**
	 * 查询当前登录者（老师学管师）的本日 本周 本月的考勤数
	 * @param userId
	 * @return
	 */
	Map<String, Integer> attendanceCount() throws Exception;

	public StudnetAccMv getStudentAccoutInfo(String studentId);

    List getMiniClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String auditStatus);
    
    /**
     * 获取课程考勤状态统计数据
     * @return
     */
    CourseAttendaceStatusCountVo getCourseAttendaceStatusCount();

    Map<String, Object> getClassCoursesInfo(String classId,String classIdTwo,String type);
    
	/**
	 * 根据课程id查询
	 * @param courseId
	 * @return
	 */
	Map<String, Object> findTextBookIdByCourseId(String courseId);
    
	/**
	 * 根据课程id查询  星火专用get  培优用find
	 * @param courseId
	 * @return
	 */
	Map<String, Object> getTextBookIdByCourseId(String courseId);
	
	DataPackage getTextBookBossList(TextBookBossVo textBookBossVo,DataPackage dataPackage);
	
	List<SmallClassExcelVo> getSmallClassToExcel(MiniClass miniClass,DataPackage dataPackage);
	
	List<List<Object>> getSmallClassToCSV(MiniClass miniClass,DataPackage dataPackage);
	
	List<Map<Object,Object>> getSmallClassToExcelSize(MiniClass miniClass,DataPackage dataPackage);

	/**
	 * 续读学生明细导出
	 * @param miniClass
	 * @param dataPackage
	 * @return
	 */
    List getSmallClassXuDuListToExcel(MiniClass miniClass, DataPackage dataPackage);
	CourseForWechatVo getCourseInfoForWechatById(String courseId);
	
	DataPackage getTodayCourseBillList(DataPackage dataPackage,TodayCourseBillVo todayCourseBillVo);
	
	TodayCourseBillVo getCourseBillInfoById(String courseId);
	
	Map<String, List<TodayCourseBillVo>> getCourseBillsPrint(String[] courseIds);
	
    Map<String, List<TodayCourseBillVo>> getCourseBillsPrintByDate(String date);
    Map<String, Object> getBillsPrintInfoByDate(String date);
    
    Response auditAutoRecog(AuditAutoRecogVo courseVo);
    
	/**
	 * 根据条件查询一对一课程信息 提供给教学平台
	 * @param courseSearchInputVo
	 * courseSearchaInputVo 主要封装如下参数:
	 * teacherId/studentId/courseId/startDate/endDate
	 */
	Map<String, Object> getOneOnOneCourseInfo(CourseSearchInputVo courseSearchInputVo);
	
	
	Course getCourseByCourseId(String courseId);
	
	void updateCourse(Course course);
	
	void updateCoursePicName(String courseId,String fileName);
	
	/**
	 * 根据学生成绩基准查询学生在读一对一科目
	 * @param benchmarkSubjectSearchVo
	 * @return
	 */
	NameValue getSubjectsByBenchmark(BenchmarkSubjectSearchVo benchmarkSubjectSearchVo);
	
	/**
	 * 判定类型符不符合有效
	 * @param studentId
	 * @param subejctId
	 * @param hours
	 * @param days
	 * @return
	 */
	Map<String, Object> checkCourseMeetConditions(String studentId, String subejctId, Integer hours, Integer days, String createDate);

	/**
	 * 获取自动审批课时单数量
	 * @param courseVo
	 * @return
	 */
    int getOneOnOneAutoRecogCount(CourseVo courseVo);

	/**
	 * 一对一课时单概要
	 * @param courseVo
	 * @return
	 */
	Map getOneOnOneAuditSummary(CourseVo courseVo);
}
