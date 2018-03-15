package com.eduboss.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.CourseStatus;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.OtmClass;
import com.eduboss.domain.OtmClassCourse;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.BasicOperationQueryVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassCourseVo;
import com.eduboss.domainVo.OtmClassStudentAttendentVo;
import com.eduboss.domainVo.OtmClassStudentVo;
import com.eduboss.domainVo.OtmClassVo;
import com.eduboss.domainVo.StudentVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.ModelVo;
import com.eduboss.dto.Response;

@Service
public interface OtmClassService {

	/**
	 * 获取一对多班级列表
	 * @param otmClassVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassList(OtmClassVo otmClassVo, DataPackage dp);
	
	/**
	 * 删除和编辑一对多班级信息
	 * @param oper
	 * @param otmClassVo
	 */
	public void operationOtmClassRecord(String oper, OtmClassVo otmClassVo) throws Exception;
	
	/**
	 * 删除一对多班级
	 * @param otmClass
	 */
	public void deleteOtmClass(OtmClass otmClass) throws Exception;
	
	/**
	 * 新增或修改一对多班级
	 * @param otmClass
	 */
	public void saveOrUpdateOtmClass(OtmClass otmClass) throws Exception;
	
	/**
	 * 根据id查找一对多
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OtmClassVo findOtmClassById(String id) throws Exception;
	
	/**
	 * 根据一对多id查看合同拟报读学生id列表
	 * @param otmClasId
	 * @return
	 */
	public List<StudentVo> getStudentWantListByOtmClassId(String otmClassId);
	
	/**
	 * 根据一对多id查看一对多已报读学生
	 * @param otmClasId
	 * @return
	 */
	public List<StudentVo> getStudentAlreadyEnrollOtmClasss(String otmClassId);
	
	/**
	 * 批量添加一对多学生
	 * @param studentIds  学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @param firstSchoolTime 第一次上课时间
	 * @param isAllowAddStudent 是否
	 * @throws Exception
	 */
	public void AddStudentForOtmClasss(String studentIds, String otmClassId, String firstSchoolTime, Boolean isAllowAddStudent) throws Exception;
	
	/**
	 * 是否允许新增小班学生
	 * @param otmClassId
	 * @param newStudentNumber
	 * @return
	 */
	public boolean allowAddStudent4OtmClass(String otmClassId, int newStudentNumber);
	
	/**
	 * 一对多现有人数
	 * @param otmClassId
	 * @return
	 */
	public int getOtmClassExistingPeopleNumber(String otmClassId);
	
	/**
	 * 修改一对多学生首次上课时间
	 * @param studentId
	 * @param otmClassId
	 * @param firstSchoolTime
	 * @return
	 */
	public void updateOtmClassStudentfirstSchoolTime(String studentId,String otmClassId,String firstSchoolTime);
	
	/**
	 * 获取有考勤记录的一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多班级id
	 * @return
	 * @throws Exception 
	 */
	public List<Student> getOtmClassAttendedStudent(String studentIds, String otmClassId) throws Exception;
	
	/**
	 * 批量删除一对多学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param otmClassId 一对多id
	 * @return
	 * @throws Exception 
	 */
	public void deleteStudentInOtmClasss(String studentIds, String otmClassId) throws Exception;
	
	/**
	 * 一对多课程详情
	 * @param otmClassId
	 * @param gridRequest
	 * @param modelVo
	 * @return
	 */
	public DataPackage getOtmClassCourseDetail(String otmClassId, DataPackage dp,ModelVo modelVo);
	
	/**
	 * 一对多详情-在读学生信息列表
	 * @param otmClassId
	 * @param gridRequest
	 * @return
	 */
	public DataPackage getOtmClassDetailStudentList(String otmClassId, DataPackage dp);
	
	/**
	 * 一对多详情-一对多学生剩余资金&剩余课时
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	public OtmClassStudentVo findOtmClassStuRemainFinAndHour(String otmClassId, String studentId);
	
	/**
	 * 所有otmType一对多班级列表，用作Selection 
	 * @param otmType
	 * @return
	 */
	public List<OtmClass> getOtmClassList4Selection(Integer otmType);
	
	/**
	 * 一对多学生转班 + 设置第一次上课日期
	 * @param oldOtmClassId
	 * @param newOtmClassId
	 * @param studentIds
	 * @param firstClassDate
	 * @return
	 */
	public void stuChgOtmClassAndSetFistClassDate(String oldOtmClassId, String newOtmClassId, String studentIds, String firstClassDate);
	
	/**
	 * 一对多详情-学生详情
	 * @param otmClassId
	 * @param studentId
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassDetailStudentDetail(String otmClassId, String studentId, DataPackage dp);
	
	/**
	 * 一对多课时信息 
	 * @param otmClassId
	 * @return
	 */
	public Map getOtmClassCourseTimeAnalyze(String otmClassId);
	
	/**
	 * 保存一对多课程列表
	 * @param courseList
	 */
	public void saveOtmCourseList(OtmClassCourse[] otmClassCourseList);
	
	/**
	 * 初始化一对多考勤记录
	 * @param otmClassCourse
	 */
	public void initOtmClassStudentAttendent(OtmClassCourse otmClassCourse);
	
	/**
	 * 一对多课程列表
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseList(OtmClassCourseVo otmClassCourseVo, DataPackage dp);
	
	/**
	 * 一对多考勤前判断
	 * @param otmClassCourseId
	 */
	public void otmClassCourseBeforAttendance(String otmClassCourseId);
	
	/**
	 * 获取一对多学生考勤列表
	 * @param otmClassStudentVo
	 * @param oprationCode
	 * @param dp
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getOtmClassStudentAttendentList(OtmClassStudentAttendentVo otmClassStudentAttendentVo, String oprationCode, DataPackage dp) throws Exception;
	
	/**
	 * 根据id获取一对多课程
	 * @param vo
	 * @return
	 */
	public OtmClassCourseVo findOtmClassCourseById(OtmClassCourseVo vo);
	
	/**
	 * 更新一对多学生考勤信息
	 * @param otmClassCourseId
	 * @param attendanceData
	 * @param oprationCode
	 * @return
	 * @throws Exception 
	 */
	public Response modifyOtmClassCourseStudentAttendance(String otmClassCourseId, String attendanceData, String oprationCode) throws Exception;
	
	/**
	 * 一对多详情-主界面信息
	 * @param otmClassId
	 * @return
	 */
	public OtmClassCourseVo getOtmClassDetailAttChaPageInfo(String otmClassCourseId);
	
	/**
	 * 更新考勤记录
	 * @param miniClassCourseId
	 * @param eachInputStuAttendentVo
	 * @param oprationCode
	 */
	public void updateOtmClassAttendanceRecord(String otmClassCourseId, OtmClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode, String studyManagerId);
	
	/**
	 * 一对多扣费
	 * @param otmClassCourseId
	 * @param eachInputStuAttendentVo
	 * @throws Exception
	 */
	public boolean otmClassCourseCharge(String otmClassCourseId, OtmClassStudentAttendentVo eachInputStuAttendentVo, String studyManagerId) throws Exception;
	
	/**
	 * 是否已经有该学生的考勤记录
	 * @param otmClassStudentAttendentId
	 * @return
	 */
	public boolean otmClassCourseHasStuAttendentRecord(
			String miniClassStudentAttendentId);
	
	/**
	 * 更新一对多班级状态
	 * @param otmClassCourseId
	 */
	public void updateOtmClassStatus(String otmClassCourseId);
	
	/**
	 * 一对多课程上课人数
	 * @param otmClassCourseId
	 * @return
	 */
	public int getOtmClassCourseStudentNum(String otmClassCourseId);
	
	/**
	 * 一对多详情-一对多考勤扣费详情
	 * @param otmClassStudentVo
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getOtmClassAttChargedDetail(OtmClassStudentAttendentVo otmClassStudentAttendentVo, DataPackage dp);

	/**
	 * 修改一对多课程信息
	 * @param otmClassCourseVo
	 * @throws Exception
	 */
	public void saveOrUpdateOtmClassCourse(OtmClassCourseVo otmClassCourseVo) throws Exception;
	
	/**
     * 删除报名时间在courseDate之前的未扣费的未上课的考勤记录
     * @param otmClassCourse
     */
	public void deleteOtmClassStudentAttendentByOtmClassCourse(OtmClassCourse otmClassCourse);
	
	/**
	 * 删除一对多课程
	 * @param otmClassCourseId
	 */
	public void deleteOtmClassCourse(String otmClassCourseId);
	
	/**
	 * 更新一对多课程考勤中的学管师，只更新未扣费的
	 * @param studyManagerId
	 */
	public void updateOtmClassStudentAttendentStudyManager(String studyManagerId, String studentId, String blCampusId) throws Exception;
	
	/**
	 * 获取一对多课程学管师们的名称
	 * @param otmClassCourseId
	 * @return
	 */
	public String getStudyManagerName(String otmClassCourseId);
	
	/**
	 * 判断 一段时间内 有无老师的一对多课程
	 * @param teacherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<HasClassCourseVo> checkHasOtmClassForPeriodDate(String teacherId,
			String startDate, String endDate);
	
	/**
	 * 判断 某一天 有无老师的一对多 课程, 根据不同的状态
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasOtmClassForDate(String teacherId, String date,  List<CourseStatus> listOfStatus);
	
	/**
	 * 判断 某一天 有无学管师的一对多  课程, 根据不同状态
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasDeductionOtmClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus);
	
	/**
	 * 判断 某一天 有无学管师的一对多  课程, 根据不同状态  新逻辑，兼容多学管。
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasDectionOtmClassForDateNew(String teacherId, String date, List<CourseStatus> listOfStatus);
	
	/**
	 * 一对多课程列表 手机端
	 * @param otmClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getOtmClassCourseListForMobile(
			OtmClassCourseVo otmClassCourseVo, DataPackage dp);
	
	/**
	 * 保存小班考勤 签名图片
	 * @param miniClassCourseId
	 * @param attendancePicFile
	 */
	public void saveOtmClassAttendancePic(String otmClassCourseId,MultipartFile attendancePicFile,String servicePath);
	
	/**
	 * 一对多课程审批汇总
	 * @param dp
	 * @param startDate
	 * @param endDate
	 * @param campusId
	 * @param teacherId
	 * @param AuditStatus
	 * @param otmTypes
	 * @return
	 */
	public DataPackage getOtmClassCourseAuditAnalyze(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan);
	
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
	public DataPackage otmClaCourseAuditAnalyzeSalary(DataPackage dp, BasicOperationQueryVo vo, String AuditStatus, String otmClassTypes,String anshazhesuan);
	
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
			String campusId,String teacherId,String auditStatus,String subject);
	
	/**
	 * 一对多课程审批
	 */
	public void otmClassCourseAudit(String courseId, String auditStatus);


    List getOtmClassCourseAuditSalaryNums( BasicOperationQueryVo vo, String auditStatus, String otmClassTypes);
    
    OtmClassCourse getOtmClassCourseById(String otmClassCourseId);
    
    void updateOtmClassCourse(OtmClassCourse otmClassCourse);
}
