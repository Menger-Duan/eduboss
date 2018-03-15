package com.eduboss.service;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.eduboss.dto.*;
import com.eduboss.exception.ApplicationException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.CourseStatus;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.CourseModal;
import com.eduboss.domain.MiniClass;
import com.eduboss.domain.MiniClassCourse;
import com.eduboss.domain.MiniClassRelation;
import com.eduboss.domain.Student;
import com.eduboss.domain.User;
import com.eduboss.domainVo.AttentanceInfoVo;
import com.eduboss.domainVo.ContractProductVo;
import com.eduboss.domainVo.HasClassCourseVo;
import com.eduboss.domainVo.MiniClassCourseStudentRosterVo;
import com.eduboss.domainVo.MiniClassCourseVo;
import com.eduboss.domainVo.MiniClassMaxMinDateVo;
import com.eduboss.domainVo.MiniClassProductVo;
import com.eduboss.domainVo.MiniClassStudentAttendentVo;
import com.eduboss.domainVo.MiniClassStudentVo;
import com.eduboss.domainVo.MiniClassVo;
import com.eduboss.domainVo.ProductChooseVo;
import com.eduboss.domainVo.StudentVo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public interface SmallClassService {


	/**
	 * 更新小班报班状态
	 * @param miniClass
	 */
	void updateMiniClassRecruitStatus(MiniClass miniClass);

	/**
	 * 返回 小班课程信息的表格
	 * 
	 * @return
	 */
	public DataPackage getMiniClassList(MiniClass miniClass, DataPackage dataPackage, String miniClassTypeId, String branchLevel, String studentId);

    int getMiniClassRealMember(String miniClassId);

    /**
	 * 根据小班id获得小班
	 * 
	 * @return
	 */
	public MiniClassVo findMiniClassById(String id);

	/**
	 * 删除小班
	 * @throws Exception 
	 */
	public void deleteMiniClass(MiniClass miniClass) throws Exception;

	/**
	 * 保存，更新小班
	 * @throws Exception 
	 */
	public void saveOrUpdateMiniClass(MiniClass miniClass) throws Exception;
	//保存小班
	public void saveMiniClass(MiniClass miniClass) throws Exception;

	/**
	 * 小班报读 查找学生
	 */
	public List<StudentVo> getStudentForEnrollMiniClasss(String idOrName);

	/**
	 * 根据小班id查看小班已报读学生
	 * @param smallClassId
	 * @return
	 */
	public List<StudentVo> getStudentAlreadyEnrollMiniClasss(String smallClassId);
	
	/**
	 * 根据小班id查看合同拟报读学生id列表
	 * @param smallClassId
	 * @return
	 */
	public List<StudentVo> getStudentWantListBySmallClassId(String smallClassId);
	
	/**
	 * 新逻辑，小班关联多产品
	 * 根据小班id查看合同拟报读学生列表
	 * @param productId 产品id
	 * @return
	 */
	public List<StudentVo> getStudentWantListByMiniClassId(String miniClassId);
	
	/**
	 * 批量添加小班学生
	 */
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,Boolean isAllowAddStudent) throws Exception;
	
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,String firstSchoolTime,Boolean isAllowAddStudent);

	void AddPromiseStudentForMiniClasss(Student student, MiniClass miniClass,ContractProduct cp,BigDecimal hours);

	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,String firstSchoolTime,Boolean isAllowAddStudent, boolean updateInventory);

	/**
	 *  判断能否添加学生到小班中去
	 * @param studentIds
	 * @param smallClassId
	 * @return
	 */
	public Response checkAllowAddStudent4MiniClass(String studentIds, String smallClassId);
	
	/**
	 * 增加小班续班扩科记录
	 * @param studentId
	 * @param smallClassId
	 * @param continueMiniClassId
	 * @param extendMiniClassId
	 */
	public void addMiniClassRelation(String studentId, String smallClassId, String continueMiniClassId, String extendMiniClassId);
	
	/**
	 * 根据学生ID和新的小班ID获取续班扩科记录
	 * @param studentId
	 * @param smallClassId
	 */
	public List<MiniClassRelation> getMiniClassRelatonsByStudentNewMiniClass(String studentId, String smallClassId);
	
	/**
	 * 130 批量添加小班学生
	 * @param studentIds  学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return 
	 * @throws Exception 
	 */
	public void AddStudentForMiniClasss(String studentIds, String smallClassId,String contractId,String contractProductId,Boolean isAllowAddStudent) throws Exception;

	/**
	 * 131批量删除小班学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	public void deleteStudentInMiniClasss(String studentIds, String smallClassId);
	
	/**
	 * 根据合同移除学生报名记录
	 * @param contract
	 */
	void deleteStudentByContract(Contract contract);

	/**
	 * 获取有考勤记录的学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	public List<Student> getMiniClassAttendedStudent(String studentIds, String smallClassId) throws Exception;

	/**
	 * 检测学生id是否存在，是否已报读该小班
	 * @param studentId
	 * @param smallClassId
	 * @return
	 */
	public Response checkMiniClassStudent(String studentId, String smallClassId);

	public List<MiniClass> getMiniClassByIdOrName(String idOrName);

	public void adjustMiniClassStudent(String oldSmallClassId,
			String newSmallClassId, String studentIds);

	/**
	 * 获取小班课程列表
	 * @param miniClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseList(MiniClassCourseVo miniClassCourseVo,
			DataPackage dp);

	/**
	 * 对该小班进行排课
	 * @param miniClassId
	 * @throws Exception
	 */
	public void arrangementMiniClassCourse(String miniClassId) throws Exception;
	
	/**
	 * 初始化小班考勤记录
	 * @param miniClassCourse
	 */
	public void initMiniClassStudentAttendent(MiniClassCourse miniClassCourse);

	/**
	 * 根据ID获取小班课程
	 * @param miniClassCourseId
	 * @return
	 */
	public MiniClassCourseVo findMiniClassCourseById(String miniClassCourseId);

	/**
	 * 新增或更新小班课程
	 * @param miniClassCourse
	 * @throws Exception
	 */
	public void saveOrUpdateMiniClassCourse(MiniClassCourseVo miniClassCourseVo)
			throws Exception;

	/**
	 * 获取小班学生考勤列表
	 * @param miniClassStudentAttendentVo
	 * @param dp
	 * @return
	 * @throws Exception 
	 */
	public DataPackage getMiniClassStudentAttendentList(
			MiniClassStudentVo miniClassStudentVo,
			DataPackage dp) throws Exception;

	/**
	 * 更新小班学生考勤信息
	 * @param miniClassCourseId
	 * @param attendanceData
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws Exception 
	 */
	public Response modifyMiniClassCourseStudentAttendance(String miniClassCourseId,
			String attendanceData, String oprationCode) throws JsonParseException, JsonMappingException, IOException, Exception;
	/**
	* 修改小班学生考勤信息
	* @param miniClassCourseId
	* @param attendanceData
	* @throws IOException
	* @throws JsonMappingException
	* @throws JsonParseException
	* @throws Exception
	*/
	public Response changeMccStudentAttendance(String miniClassCourseId,
											   String attendanceData) throws JsonParseException, JsonMappingException, IOException, Exception;


	/**
	 * 更新小班学生补课时间
	 * @param miniClassCourseId
	 * @param studentId
	 * @param supplementDate
	 */
	public void modifyMiniClassCourseStudentSupplement(String miniClassCourseId, String studentId, String supplementDate);
	
	/**
	 * 更新小班学生缺勤备注
	 * @param miniClassCourseId
	 * @param studentId
	 * @param absentRemark
	 * @return
	 */
	public void modifyMiniClassCourseAbsentRemark(String miniClassCourseId, String studentId, String absentRemark);

	/**
	 * 小班学生扣费
	 * @param miniClassId
	 * @param studentId
	 * @throws Exception 
	 */
	public void miniClassStudentPreCharge(String miniClassId, String studentId) throws Exception;

	/**
	 * 操作小班记录
	 * @param oper
	 * @param miniClass
	 * @throws Exception
	 */
	public void operationMiniClassRecord(String oper, MiniClass miniClass, String productIdArray)
			throws Exception;
	
	/**
	 * 操作小班管理产品记录
	 * @param productIdArray
	 * @parem miniClassId
	 * @throws Exception
	 */
	public void operationMiniClassProduct(String productIdArray, String miniClassId)
			throws Exception;
	
	/**
	 * 增加单条小班管理产品记录
	 * @param productIdArray
	 * @parem productId
	 * @throws Exception
	 */
	public Response addOneMiniClassProduct(String productId, String miniClassId)
			throws Exception;
	
	/**
	 * 获取小班管理产品记录
	 * @param miniClassId
	 * @return
	 * @throws Exception
	 */
	public List<MiniClassProductVo> getMiniClassProductList(String miniClassId)
			throws Exception;
	
	/**
	 * 获取符合产品，小班的报名学生
	 * @param miniClassId
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public List<MiniClassStudentVo> getStudentsByMiniClassProduct(String miniClassId, String productId)
			throws Exception;
	
	/**
	 * 删除小班产品关联记录
	 * @param miniClassId
	 * @param productId
	 * @return
	 * @throws Exception
	 */
	public void deleteMiniClassProduct(String miniClassId, String productId)
			throws Exception;

	/**
	 * 过滤未报名课程的学生
	 * @param currMiniClassCourse
	 * @param searchResultVoList
	 * @return
	 * @throws Exception
	 */
	public List<MiniClassStudentVo> filterNoSignStudent(
			MiniClassCourse currMiniClassCourse,
			List<MiniClassStudentVo> searchResultVoList) throws Exception;

	/**
	 * 小班是否已经有预扣费记录
	 * @param miniClassId
	 * @return
	 */
	boolean miniClassHasPreChargeRecord(String miniClassId);

	/**
	 * 小班扣费
	 * @param miniClassCourseId
	 * @param miniClassStudentAttendentId
	 * @throws Exception
	 */
	public boolean miniClassCourseCharge(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo) throws Exception;
	public boolean miniClassCourseCharge_Edu(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo,User user) throws Exception;
	/**
	 * 更新小班状态
	 * @param miniClassCourseId
	 */
	public void updateMiniClassStatus(String miniClassCourseId);
	
	/**
	 * 更新小班消耗课时
	 * @param miniClassCourseId
	 */
	public void updateMiniClassConsume(String miniClassCourseId);

	/**
	 * 是否已经有该学生的考勤记录
	 * @param miniClassStudentAttendentId
	 * @return
	 */
	public boolean miniClassCourseHasStuAttendentRecord(
			String miniClassStudentAttendentId);

	/**
	 * 小班现有人数
	 * @param miniClassId
	 * @return
	 */
	public int getMiniClassExistingPeopleNumber(String miniClassId);

	/**
	 * 小班最大人数
	 * @param miniClassId
	 * @return
	 */
	public int getMiniClassMaxPeopleNumber(String miniClassId);

	/**
	 * 是否允许新增小班学生
	 * @param smallClassId
	 * @param newStudentNumber
	 * @param updateInventory 更新库存
	 * @return
	 */
	public boolean allowAddStudent4MiniClass(String smallClassId, int newStudentNumber, boolean updateInventory);

	
	public MiniClass getMiniClassByName(String name,String blCampus);
	
	/**
	 * 修改小班学生首次上课时间
	 * @param id
	 * @param firstSchoolTime
	 * @return
	 */
	public void updateMiniClassStudentfirstSchoolTime(String studentId,String smallClassId,String firstSchoolTime);

	public MiniClassVo getMiniClassByContractProductId(String contractProductId);

	/**
	 * 获取小班课程时间段内占用教室列表
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public String getMiniClassCourseUseClassroomListString(String blCampusId,String startDate, String endDate, String classroomName, String miniClassName);

	/**
	 * 获取小班课程
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public List getClassroomMiniClassCourse(String startDate, String endDate, String classroomName, String miniClassName,String campusId,String miniClassTypeId);

	/**
	 * 查询哪个小班课程安排教室
	 * @param startDate
	 * @param endDate
	 * @param classroomName
	 * @param miniClassName
	 * @return
	 */
	public List getNotArrangementClassroomMiniClassCourse(String startDate,
			String endDate, String classroomName, String miniClassName);

	/**
	 * 小班课程详情
	 * @param miniClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseDetail(String miniClassId, DataPackage dp,ModelVo modelVo);
	
	/**
	 * 小班课时信息
	 * @param miniClassId
	 * @return
	 */
	public Map getMiniClassCourseTimeAnalyze(String miniClassId);

	/**
	 * 小班详情-在读学生信息列表
	 * @param miniClassId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassDetailStudentList(String miniClassId, DataPackage dp);

	/**
	 * 小班详情-学生详情
	 * @param miniClassId
	 * @param studentId
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassDetailStudentDetail(String miniClassId,
			String studentId, DataPackage dp);

	/**
	 * 小班详情-小班考勤扣费详情
	 * @param miniClassStudentVo
	 * @param dp
	 * @return
	 * @throws Exception
	 */
	public DataPackage getMiniClassAttendanceChargedDetail(
			MiniClassStudentVo miniClassStudentVo, DataPackage dp)
			throws Exception;

	/**
	 * 所有小班列表，用作Selection
	 * @return
	 */
	public List getMiniClassList4Selection(String productId);

	/**
	 * 小班详情-主界面信息
	 * @param miniClassId
	 * @return
	 */
	public MiniClassVo getMiniClassDetailMainPageInfo(String miniClassId);

	/**
	 * 小班详情-小班考勤扣费详情页面信息
	 * @param miniClassCourseId
	 * @return
	 */
	public MiniClassCourseVo getMiniClassDetailAttChaPageInfo(String miniClassCourseId);

	/**
	 * 小班课程上课人数
	 * @param miniClassId
	 * @return
	 */
	public int getMiniClassCourseStudentNum(String miniClassCourseId);

	/**
	 * 小班学生对应的合同产品
	 * @param miniClassId
	 * @param studentId
	 * @return
	 */
	public ContractProductVo getMiniClassStudentContractProduct(String miniClassId,
			String studentId);

	/**
	 * 小班学生转班 + 设置第一次上课日期
	 * @param oldSmallClassId
	 * @param newSmallClassId
	 * @param studentIds
	 * @param fistClassDate
	 */
	public void stuChangeMiniClassAndSetFistClassDate(String oldSmallClassId,
			String newSmallClassId, String studentIds, String fistClassDate);
	
	
	/**
	 * 根据合同产品 ， 删除小班 学生
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	public void deleteSingleStudentInMiniClasss(String studentId, String smallClassId);
	
	/**
	 * 根据合同产品 ， 查询到 某小班
	 * @param studentIds 学生id （多条用，隔开）
	 * @param smallClassId 小班id
	 * @return
	 * @throws Exception 
	 */
	public MiniClass getMiniClassByContractProduct(ContractProduct conProduct) ;


	
	/**
	 * 查询某个 学生 的某个下班 有无考勤记录
	 * @param miniClassId
	 * @param Studnetid
	 * @return
	 */
	public boolean hasAttendenceRecordByMiniClassAndStudent(String miniClassId,
			String Studnetid);

	public void deleteMiniClassCourse(String miniClassCourseId);

    public void updateMiniClassAttendanceRecord(String miniClassCourseId, MiniClassStudentAttendentVo eachInputStuAttendentVo, String oprationCode);
    
    public int countByProductId(String productId);
    
	/**
	 * 获取小班的 课消情况
	 * @param miniClassCourseVo
	 * @param dp
	 * @return
	 */
	public DataPackage getMiniClassCourseConsumeList(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp);
	
	/**
	 * 根据学生获取已经结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
	public DataPackage findOldMiniClassByStudent(DataPackage dp, MiniClassRelationSearchVo miniClassRelationSearchVo);
	
	
	/**
	 * 根据学生获取还没结束的小班
	 * @param dp
	 * @param miniClassRelationSearchVo
	 * @return
	 */
	public DataPackage findUnconpeletedMiniClass(DataPackage dp, MiniClassRelationSearchVo miniClassRelationSearchVo);
	
	/**
	 * 获取学生管理已结束小班的续班扩课记录
	 * @param studentId
	 * @param oldMiniClassId
	 * @return
	 */
	public List<MiniClassRelation> findMiniClassRelation(String studentId, String oldMiniClassId);
	
	/**
	 * 删除小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
	public void deleteOldMiniClass(MiniClassRelationSearchVo miniClassRelationSearchVo);
	
	/**
	 * 保存小班续班扩科关联记录
	 * @param miniClassRelationSearchVo
	 */
	public void saveMiniClassRelation(MiniClassRelationSearchVo miniClassRelationSearchVo);

	/**
	 * 判断 某一天 有无老师的小班 课程, 根据不同的状态
	 * @param teacherId
	 * @param date
	 * @param listOfStatus
	 * @return
	 */
	public boolean checkHasMiniClassForDate(String teacherId, String date,  List<CourseStatus> listOfStatus);

	/**
	 * 判断 一段时间内 有无老师的小班 课程
	 * @param teacherId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<HasClassCourseVo> checkHasMiniClassForPeriodDate(String teacherId,
			String startDate, String endDate);
	
	
	public boolean checkHasDeductionMiniClassForDate(String teacherId, String date, List<CourseStatus> listOfStatus);

	/**
 * 保存小班考勤 签名图片
 * @param miniClassCourseId
 * @param attendancePicFile
 */
  public void saveMiniClassAttendancePic(String miniClassCourseId,MultipartFile attendancePicFile,String servicePath);
  
  /**
   * 删除报名时间在courseDate之前的未扣费的未上课的考勤记录
   * @param miniClassCourseId
   */
  public void deleteMiniClassStudentAttendentByMiniClassCourse(MiniClassCourse miniClassCourse);
  
  /**
   * 获取未报班合同产品和人次
   * @param campusId
   * @param productId
   * @param studentId
   * @return
   */
  public List<Map<Object, Object>> getMiniClassProductWithCount(MiniClassProductSearchVo miniClassProductSearchVo);
  
  /**
  * 获取未报读（或报读有小班ID）当前合同产品关联小班的学生列表
  * @param campusId
  * @param productId
  * @param miniClassId
  * @return
  */
  public List<Map<Object, Object>> getStudentWithRemainingHours(String campusId, String productId, String miniClassId);
  
  /**
   * 获取所选小班产品该校区关联的所有具体小班信息
   * @param campusId
   * @param productId
   * @return
   */
   public List<MiniClassVo> getApplyMiniClassList(String campusId, String productId);
   
   /**
	  * 获取未报读小班产品和剩余课时信息
	  * @param studentId
	  * @param dp
	  * @return
	  */
    public DataPackage getProductWithRemainingHours(String studentId, DataPackage dp);
    
    
    /**
	  * 获取已报读小班产品、剩余课时和小班信息
	  * @param studentId
	  * @param dp
	  * @return
	  */
   public DataPackage getProductWithMiniClass(String studentId, DataPackage dp);
   
   /**
	  * 刷选小班
	  * @param productChooseVo
	  * @param gridRequest
	  * @return
	  */
   public DataPackage findMiniClassForChoose(DataPackage dp, ProductChooseVo productChooseVo);
   

	/**
	 * 小班课程审批
	 */
	
	public void miniClassCourseAudit(String courseId, String auditStatus);
	
	/**
	 * 小班考勤前判断
	 */
	public void miniClassCourseBeforAttendance(String miniClassCourseId) throws Exception;

	public DataPackage getMiniClassCourseListForMobile(
			MiniClassCourseVo miniClassCourseVo, DataPackage dp);

	List<MiniClass> getAllMiniClassByContractProduct(ContractProduct conProduct);

	/**
	 * 删除考勤学生的考勤记录
	 * @param miniClassId
	 * @param id
	 */
	public void deleteAttendenceRecordByMiniClassAndStudent(String miniClassId,
			String id);
    

	public BigDecimal findMiniClassWillBuyHour(String id, String startDate);
	
	/**
	 * 小班学生花名册
	 * @param miniClassId
	 * @return
	 */
	MiniClassCourseStudentRosterVo getMiniClaCourseStudentRoster(String miniClassId);
	

	/**
	 * 开放接口给教育平台 同步点名信息
	 *
	 */
	public Map<String,Object> chargeAttentanceInfo(List<AttentanceInfoVo> attentanceInfoVos,String type,String chargeUserId) throws Exception;

	/**
	 * 开放接口给教育平台 同步点名信息
	 * 
	 */
	public int updateAttentanceInfo(List<AttentanceInfoVo> attentanceInfoVos) throws Exception;

	void updateCourseNumByClassId(String miniClassId);
	
	/**
	 * 根据小班id查询非取消课程
	 * @param classId
	 * @return
	 */
	List<MiniClassCourse> findMiniClassCourseByClassId(String classId);
	
	/**
	 * 根据小班ids获取小班列表
	 * @param miniClassIds
	 * @return
	 */
	List<MiniClassVo> getMiniClassListByIds(String miniClassIds);

	DataPackage findMiniClassByPromiseSubjectId(DataPackage dp, Map<String, Object> paramMap);

	List<Map<Object,Object>> findMiniClassTeacherForSelect(String name);

    Response changeMiniClassSaleType(String miniClassIds, int campusSale, int onlineSale,String remark) throws Exception;
    
    void updateMiniClassSaleType(String miniClassIds, int onlineSale);
    
    Map<Object, Object> findCourseStatusNumVoByOrgId(String orgId);

    Response checkOnlineSaleBrench(String[] campusId);
    
    /**
     * 根据小班id获取排课的最大最小值
     * @param miniClassId
     * @return
     */
    MiniClassMaxMinDateVo getMaxMinCourseDateByMiniClass(String miniClassId);
    
    /**
     * 更新小班的开课结课日期
     * @param miniClass
     */
    void updateMiniClassMaxMinCourseDate(MiniClass miniClass);

    List<MiniClassModalVo> getSmallClassListOnModal(MiniClassModalVo vo);

    Response saveEditClassModal(MiniClassEditModalVo vo);

	Response modifyClassModal(CourseModal modal,Boolean isChangeTechNum);

    void clearAllClassCourse(String miniClassId);

    void deleteMiniStudentAttendent(String contractProductId);

    List<MiniClass> findMiniClassByModalId(int modalId);

    List<MiniClassModalVo> getSmallClassListByTeacher(MiniClassModalVo vo);

	public Response batchSaveMiniClass(MiniClassVo[] miniClassVo) throws Exception;

	public Integer getBatchMiniClassNum(String productId,String coursePhase,String campusId,String subjectId)throws ApplicationException;

    List getStudentMiniClassAttendent(String miniClassId, String studentId);

	List getMiniClassCourseDateInfo(String miniClassId);

    Response getMiniClassCanChangeProduct(String miniClassId);

    MiniClassCourse getMiniClassCourseById(String miniClassCourseId);

    void updateMiniClassCourse(MiniClassCourse miniClassCourse);

    List<MiniClassVo> changeMiniClassSelectList(String productId);

	List<MiniClassCourseVo> findAllEnableMiniClassCourse(String miniClassId);
}
