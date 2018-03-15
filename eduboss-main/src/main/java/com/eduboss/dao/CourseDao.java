package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.Course;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.CourseVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.dto.CourseChangesSearchInputVo;
import com.eduboss.dto.CourseSearchInputVo;
import com.eduboss.dto.DataPackage;

@Repository
public interface CourseDao extends GenericDAO<Course, String> {
	
	public void save(Course course);

	/**
	 * 课程列表
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getCourseList(CourseSearchInputVo courseSearchInputVo, DataPackage dp);
	
	/**
	 * 老师课程表 - 除指定某种状态外，查不含请假的所有状态
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getTeacherCourseScheduleList(CourseSearchInputVo searchInputVo,
			DataPackage dp);

	/**
	 * 老师课程表 - 所有状态
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getTeacherCourseScheduleListAllCourseStatus(CourseSearchInputVo searchInputVo,
													DataPackage dp);
	
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
			String courseStartTime, String courseEndTime, DataPackage dp);
	
	/**
	 * 校区课程列表
	 * @param courseSearchInputVo
	 * @param dp
	 * @return
	 */
	@Deprecated
	public DataPackage getSchoolZoneCourseList(CourseSearchInputVo courseSearchInputVo, DataPackage dp);

	/**
	 * 批量修改课程属性
	 * 
	 * @param changesAttr
	 * @param changesData
	 * @param updateAll
	 * @param ids
	 * @param courseChangesSearchInputVo
	 */
	public void courseAttrChanges(String changesAttr, String changesData,
			boolean updateAll, String[] ids,
			CourseSearchInputVo courseSearchInputVo) throws Exception;
	
	/**
	 * 批量修改课程的老师
	 * @param teacherUserId
	 * @param idsString
	 * @param courseSearchInputVo
	 */
	void batchChangeTeacher(String teacherUserId, String[] ids);
	
	/**
	 * 批量修改客户年级
	 * @param gradeId
	 * @param ids
	 */
	public void batchChangeCourseGrade (String gradeId, String ids);
	
	/**
	 * 根据条件查询课程
	 * @param updateAll 
	 * @param ids
	 * @param courseChangesSearchInputVo
	 * @return
	 */
	public List<Course> queryCourseAttrChanges( boolean updateAll, String[] ids, CourseChangesSearchInputVo courseChangesSearchInputVo);

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
	 * getCourseListByCourseSummaryId
	 * @param courseSummaryId
	 * @return
	 */
	public DataPackage getCourseListByCourseSummaryId(String courseSummaryId);

	/**
	 * 获取一对一批量考勤列表
	 * @param courseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneBatchAttendanceList(CourseVo courseVo, DataPackage dp);
	
	/**
	 * 获取一对一批量考勤列表手机端
	 * @param courseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneBatchAttendanceListForMobile(CourseVo courseVo, DataPackage dp);
	
	/**
	 * 检测冲突
	 * @param course
	 * @return
	 */
	public boolean checkCourseCrash(CourseVo course);

	public DataPackage getCourseChangesList(CourseSearchInputVo courseSearchInputVo, DataPackage dp);

	/**
	 * 学生课程表
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	public DataPackage getStudentCourseScheduleList(CourseSearchInputVo searchInputVo,
			DataPackage dp);
	
	 /**
     * 删除考勤记录
     *
     * @param id
     */
    public void deleteCourseAttendance(String id);
	
	/**
	 * 因为学生是不需要查询权限的， 只要求查到自己的课程，与 getSchoolZoneCourseList 一致， 只是去除了角色的权限，  ### 学生 app 的查询 ###
	 * @param searchInputVo
	 * @param dp
	 * @return
	 */
	@Deprecated
	public DataPackage getSchoolZoneCourseListForStudent(
			CourseSearchInputVo searchInputVo, DataPackage dp);
	
	/**
	 * 一对一课程审批与查看汇总(课时)
	 * @param inputCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOneOnOneAuditAnalyzeList(CourseVo inputCourseVo,
			DataPackage dp);


	/**
	 * 一对一课程审批与查看汇总(小时)
	 * @param inputCourseVo
	 * @param dp
     * @return
     */
	public DataPackage getOneOnOneAuditAnalyzeListXiaoShi(CourseVo inputCourseVo, DataPackage dp);
	/**
	 * 小班课时汇总
	 */
	public DataPackage MiniClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String miniClassTypeId);
	
	/**
	 * 一对多课时汇总
	 */
	public DataPackage getOtmClassCourseCollectList(DataPackage dataPackage,
			String startDate,String endDate,String organizationIdFinder,String otmClassTypeId);
	
	
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
	public DataPackage getMiniClassCourseAuditAnalyze(DataPackage dataPackage,BasicOperationQueryVo vo,String AuditStatus,String productQuarterSearch);
	
	/**
	 * 小班批量考勤列表(课时)
	 * @param miniCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage miniClassCourseAuditList(DataPackage dataPackage,String startDate,String endDate,
			String campusId,String teacherId,String auditStatus,String subject,String productQuarterSearch);

	/**
	 * 小班批量考勤列表（小时）
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
     * @param subject
     * @return
     */
	public DataPackage miniClassCourseAuditListXiaoshi(DataPackage dataPackage, String startDate, String endDate, String campusId, String teacherId, String auditStatus, String subject,String productQuarterSearch);

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
	 * 小班课程审批汇总(小时)
	 * @param dataPackage
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param auditStatus
	 * @param miniClassTypeId
     * @return
     */
	public DataPackage getMiniClassCourseAuditAnalyzeXiaoShi(DataPackage dataPackage, BasicOperationQueryVo vo, String auditStatus,String productQuarterSearch);

	/**
	 * 学生一对一已排的课时 ：未结算课时（未上课，老师已考勤，学管已确认）
	 * @param studentId
	 * @return
	 */
    BigDecimal countOneOnOneNotChargeCourse(String studentId);
    
    /**
     * 根据学生，产品组（或产品），科目计算一对一已排未上课课程课时数
     * @param studentId
     * @param productGroupId
     * @param productId
     * @param subjectId
     * @param summaryId
     * @param courseId
     * @return
     */
    BigDecimal sumCourseHoursByStudentProductSubject(String studentId, String productGroupId, String productId, String subjectId, String summaryId, String courseId);
    
    /**
     * 根据学生，产品组（或产品），科目计算一对一学管已确认课程课时数
     * @param studentId
     * @param productGroupId
     * @param subjectId
     * @return
     */
    BigDecimal sumConfrimCourseHoursByStudentProductSubject(String studentId, String productGroupId, String productId, String subjectId);

    List getMiniClassCourseAuditSalaryNums(BasicOperationQueryVo vo, String auditStatus);
}
