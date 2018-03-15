package com.eduboss.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;









import javax.servlet.http.HttpServletResponse;

import com.eduboss.domain.*;
import com.eduboss.domain.Student;
import com.eduboss.domainVo.*;
import com.eduboss.domainVo.StudentMap.ParamVo;
import com.eduboss.domainVo.StudentMap.StudentMap;
import com.eduboss.dto.*;
import com.eduboss.jedis.StudentStatusMsg;

import org.springframework.web.multipart.MultipartFile;

import com.eduboss.domainVo.StudentVo;

public interface StudentService {

	public void saveStudent(Student student);
	
	public Student getStduentById(String id);

	public DataPackage getStudentList(Student student,boolean includingAccountInfo, DataPackage dp,ModelVo modelVo,
			String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGradeSchool);
	
	public DataPackage getStudentList(Student student, DataPackage dp,ModelVo modelVo, boolean includingAccountInfo,
			String rcourseHour ,String rcourseHourEnd,String brenchId,String stuType,String stuNameGradeSchool);

	public void deleteStudent(Student student);

	public void saveOrUpdateStudent(Student student);

	public StudentVo findStudentById(String id);

	public void updateStudentManeger(String studentIds, String oldStudyManagerId, String newStudyManagerId);
	
	public List<StudentVo> findStudentsByNameOrId(String term);

	public List<StudentVo> findStudentByCourseRequirement(String term,String orgId);
	
	/**
	 * 根据学生id查询未排课排课需求
	 * @param studentId
	 * @return
	 */
	public List<Map<String,String>> getCourseRequirementByStudentIdAndWaitArrenge(String studentId);

	/**
	 * 根据学生id 查询课程概要
	 *@param studentId
	 *@return
	 */
	public List<CourseSummarySearchResultVo> getCourseSummaryByStudentId(String studentId,String endDate);

	/**
	 * 保存 学生信息
	 * @param stuName	学生姓名
	 * @param gradeId	学生年纪ID
	 * @param customerId 客户ID
	 * @return stuID	学生的ID 
	 */
	public String saveNewStudent(String stuName, String gradeId, String customerId);
	
	/**
	 * 保存学生信息
	 * @param stuName
	 * @param gradeId
	 * @param customerId
	 * @param studyManagerId
	 * @param schoolId
	 * @return
	 */
	public String saveNewStudent(String stuName, String gradeId, String customerId, String studyManagerId, String schoolId);

	public String saveNewStudent(String stuName, String gradeId, String customerId, String studyManagerId, String schoolId,String blCampusId);



	/**
	 * 查找学生列表（自动搜索）
	 * @param input
	 * @return
	 */
	public List<Map<Object,Object>> getStudentAutoComplate(String input);
	
	/**
	 * 查询学生帐户情况
	 * @param studentId
	 * @return
	 */
	public StudnetAccMvVo getStudentAccoutInfo(String studentId);

	public DataPackage getCustomerStudentList(String studentId,
			DataPackage dataPackage);
	
	public DataPackage getDeletedCustomerStudentList(String studentId,
			DataPackage dataPackage);
	
	/**
	 * 添加学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	public Response addCustomerStudent(CustomerStudent customerStudent);
	
	/**
	 * 修改学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	public Response updateCustomerStudent(CustomerStudent customerStudent);
	
	public Response restoreCustomerStudent(CustomerStudent customerStudent);
	
	/**
	 * 删除学生关联客户关系
	 * @param customerStudent
	 * @return
	 */
	public Response deleteCustomerStudent(CustomerStudent customerStudent);
	
	/**
	 * 修改学管师
	 * @param studentId
	 * @param studyManegerId
	 * @return
	 */
	public Response updateStudentStudyManager(String studentId, String studyManegerId) throws Exception;

	public DataPackage getStudentSchoolList(StudentSchool studentSchool, DataPackage dataPackage);

	public DataPackage getStudentScoreList(StudentScore studentScore,DataPackage dataPackage,BigDecimal min,BigDecimal max);//

	public void deleteStudentSchool(StudentSchool studentSchool);
	
	public void deleteStudentScore(StudentScore studentscoremanage);//

	public void saveOrUpdateStudentSchool(StudentSchool studentSchool);
	
	public void saveOrUpdateStudentScore(StudentScore studentscoremanage,String[] subject,BigDecimal[] score,String[] classRange,String[] gradeRange);//

	public StudentSchoolVo findStudentSchoolById(String id);

	public List<StudentSchoolVo> getStudentSchoolForAutoCompelate(String term);
	
	public StuInfoForFundsVo getCampusInfoByCampusId(String blCampusId);

	public StudentScoreVo findStudentScoreById(String id);

	
	/**
	 * 学生登录
	 * @param studentId
	 * @param appPassword
	 * @return
	 */
	public AppStudentLoginResponse appLogin(String studentId, String appPassword);
	
	/**
	 * 获取学生在校区的联系人
	 * @param studentId
	 * @return
	 */
	public List<UserVo> getStudentReferenceCampusContact(String studentId);
	
	/**
	 * 获取学生学管
	 * @param studentId
	 * @return
	 */
	public User getStudentStudyManager(String studentId);

	public Response editAttanceNo(String studentId,String attanceNo, String icCardNo);

    /**
     * 查找所有学生with课程数量
     * @return
     */
    public List<StudentVo> findStudentWithCourseCount(CourseVo courseVo);
    
    /**
     * 查询地图显示学生，包括经纬度
     * @param studentVo
     * @return
     */
    public List<Map<String, String>> findStudentForMapView(StudentVo studentvo, ModelVo modelVo);
    
    /**
     * 维护学生位置信息
     * @param studentVo
     * @return
     */
    public Response editStudentMapInfo(String studentId, String address, String log, String lat);

    /**
     * 修改学生指纹
     * @param studentId
     * @param fingerInfo
     */
	public String uploadStudentFingerInfo(String studentFingerNo,String studentId, String fingerInfo);

	public List<Object[]> getAllStudentFingerInfo();

	public List<StudentVo> getStudentListByName(String studentName,String brenchType,String conCampusId);
	
	/**
	 * 提取电子账户余额资金
	 * @param studentId
	 * @param withDrawAmount
	 * @return
	 */
	public BigDecimal withDrawElectronicAmount(String studentId, BigDecimal withDrawAmount);
	
	/**
	 * 从电子账户划归收入
	 * @param studentId
	 * @param withDrawAmount
	 * @param reason
	 * @return
	 */
	public BigDecimal putMoneyIncome(String studentId, BigDecimal withDrawAmount, String reason);
	
	
	/**
	 * 提取电子账户余额资金
	 * @param studentId
	 * @param withDrawAmount
	 * @return
	 */
//	public BigDecimal withDrawElectronicAmount(String studentId, BigDecimal withDrawAmount,String returnReson,String returnType,String cbsIdA, String userIdA, String bonusA, String bonusAA,
//			String cbsIdB, String userIdB, String bonusB, String bonusBB,
//			String cbsIdC, String userIdC, String bonusC, String bonusCC,
//			String schoolA, String schoolB, String schoolC,String contractProductId);
	
	/**
	 * 结课退费（不经电子账户）
     * @param studentId
     * @param returnNormalAmount
     * @param returnPromotionAmount
     * @param withDrawAmount
     * @param returnReason
     * @param returnType
     * @param contractProductId
     * @param certificateImageFile
     * @param accountName
     * @param account_from
     * @param returnCampusId
     * @param returnUserId
     * @param list
     * @param liveContractProductRefundVo
     */
	public void closeContractProduct(String studentId,
                                     BigDecimal returnNormalAmount, BigDecimal returnSpecialAmount, BigDecimal returnPromotionAmount,
                                     BigDecimal withDrawAmount, String returnReason, String returnType,
                                     String contractProductId,
                                     MultipartFile certificateImageFile, String accountName, String account_from, String returnCampusId, String returnUserId, List<IncomeDistributionVo> list, LiveContractProductRefundVo liveContractProductRefundVo);

	/**
	 * 提取电子账户余额资金
	 * @param studentId
	 * @param withDrawAmount
	 * @param refundIncomeDistributeVo
	 * @return
	 */
	public BigDecimal withDrawElectronicAmount(String studentId, BigDecimal returnNormalAmount, BigDecimal returnSpecialAmount, BigDecimal returnPromotionAmount, BigDecimal withDrawAmount, String returnReson, String returnType,
											   String contractProductId, MultipartFile certificateImageFile, String accountName, String account, String returnCampusId, String returnUserId, RefundIncomeDistributeVo refundIncomeDistributeVo);

	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName);
	
	/**
     * 电子账户转账
     * @param origStuId
     * @param destStuId
     * @param transferAmount
     */
    public void transferElectronicAmount(String origStuId,String destStuId,BigDecimal transferAmount);

	public void updateStudentStatus(Student student) throws Exception;
	
	
	/**
	 * 获取学生资金变更记录
	 * */
	public DataPackage getStudentMoneyChanges(StudentMoneyChanges studentMoneyChanges,Map map, DataPackage dataPackage);
	
	/**
	 * 学生回访记录
	 * */
	public DataPackage getStudentFollowUp(StudentFollowUpVo studentFollowUpVo, DataPackage dataPackage);
	
	/**
	 * 保存学生回访记录
	 * */
	public void saveStudentFollowUp(StudentFollowUp studentFollowUp);

	/**
	 * 手机接口需要的
	 * @param studentFollowUp
	 * @param documentfile
	 */
	public void saveStudentFollowUp(StudentFollowUp studentFollowUp,MultipartFile documentfile);
	
	/**
	 * 根据学生ID查询电子账户记录
	 * */
	public List<ElectronicAccountChangeLogVo> getElectronicAccountChangeLogByStudentId(ElectronicAccountChangeLogVo electronicAccountChangeLogVo);
	
	/**
	 * 保存学生转校区记录
	 * */
	public void saveStudentTransactionCampusRecord(TransactionCampusRecord transactionCampusRecord);

	public void updateStudentTrunCampus(TransactionCampusRecord transactionRecord);

    /**
     * 根据老师和科目查询可排课学生
     * @param teacherId
     * @param subjectId
     * @return
     */
    public List<StudentVo> getStudentByTeacherAndSubject(String teacherId,String subjectId);

    /**
     * 根据老师、科目和产品获取可消费的学生
     * @param teacherId
     * @param subjectId
     * @param productId
     * @return
     */
    public List<StudentVo> getCanConsumeStudents(String teacherId,String subjectId,String productId);

	public void saveStudentOrganization(StudentOrganizationForm studentOrganizationForm);
	
	public void deleteStudentOrganization(StudentOrganizationVo studentOrganizationVo);
	
	public void saveOrUpdateStudentOrganization(StudentOrganizationVo studentOrganizationVo) throws Exception;

	public List<StudentOrganizationVo> getStudentOrganization(String studentId);

	/**
	 * 根据不同的staffId 获取到相应的学生信息
	 * @param staffId
	 * @return
	 */
	public List<MobileUserVo> getMobileContactsForStaff(String staffId);
	
	
	/**
	 * @param 根据校区找学生
	 * @return
	 */
	public List<StudentVo> findStudentByCampus();
	
	/**
	 * 查找所有学生
	 * @param term
	 * @return
	 */
	public List<AutoCompleteOptionVo> getStudentAutoComplateAll(String term);
	
	
	/**
	 * 查找某校区的学生
	 * @param campusId
	 * @return
	 */
	public List<StudentVo> findStudentsByCampusId(String campusId);
	
		/**
	 * 通过学生Id得到学生评价
	 * @param studentId
	 * @param dataPackage
	 * @return
	 */
	public DataPackage getStudentComment(String studentId, DataPackage dataPackage);

	/**
	 * 保存学生评价
	 * @param studentComment
	 */
	public void saveStudentComment(StudentComment studentComment) ;
	
	/**
	 * 获取所有学生校区*/
	public List<Object> getAllStudentOrganization();
	
	/**
	 * 查询该校区所有为上课中的学生ID*/
	public List<Map<Object, Object>> getStudentIdByOrganizationId(String organizationId);
	
	
	/**
	 * 查询所有为上课中的学生ID*/
	public List<Map<Object, Object>> getAllStudentId();
	
	/**
	 *通过学生ID查询学生指纹*/
	public StudentFingerInfo getStudentFingerInfoByStudentId(String studentId);
	
	/**
	 * 获取所有上课中的学生指纹*/
	public List<Object[]> getAllStudentFingerInfoByStatus();
	
	/**
	 * 获取所有今天有课的学生指纹*/
	public List<Object[]> getTodayStudentFingerInfo();
	
	/**
	 * 保存学生档案
	 * @param myfile
	 * @param studentFileVo
	 * @param servicePath
	 */
	public void saveStudentFile(MultipartFile myfile,StudentFileVo studentFileVo,String servicePath);
	
	/**
	 * 获取学生档案	 
	 * @param studentFileVo
	 * @param dataPackage
	 */
	public DataPackage getStudentFile(StudentFileVo studentFileVo,DataPackage dataPackage);
	
	
	/**
	 * 获取学生档案PC端	 
	 * @param studentFileVo
	 * @param dataPackage
	 */
	public DataPackage getStudentFileByPC(StudentFileVo studentFileVo,DataPackage dataPackage,TimeVo timeVo);
	
	/**
	 * 更新 同学生的信息， 单独的对某个字段进行更新
	 * @param studentVo
	 */
	public void updateStudentForSimple(StudentVo studentVo);

	public Map<Object, Object> getCusAndStuByStudentId(String studentId);
	
	/**
	 * 
	 * 批量升级学生年级
	 * 
	 */
	
	public void upGrade(String cla,String course,String orgnizationId, String gradeNames);
	
	/**
	 * 小班学生列表
	 */
	
	public DataPackage getMiniClassStudents(StudentVo studentvo,MiniClassVo miniclassvo,DataPackage dp,String brenchId, String miniClassGradeId,
			ModelVo modelvo,String stuNameGradeSchool);
	

	
	/**
	 * 目标班学生列表
	 */
	public DataPackage getPromiseStudents(StudentVo studentvo,PromiseClassVo promiseClassvo,DataPackage dp,String brenchId,
			ModelVo modelvo,String productQuarterId,String stuNameGradeSchool);
	

	
	/**
	 * 更换用户组织架构前判断
	 * @param studyManegerId
	 * @throws Exception
	 */
	public void befroChangeStudyManegerOrg(String orgIds,String userId) throws Exception;
	
	
	/**
	 * 一对多学生列表
	 */
	public DataPackage getOtmClassStudents(StudentVo studentvo,OtmClassVo otmClassvo,DataPackage dp,String brenchId, String otmClassGradeId,
			ModelVo modelVo,String stuNamegradeSchool, String rcourseHour, String rcourseHourEnd, String stuType);

	/**
	 * APP一对多学生列表
	 */
	public List<MobileStudentVo> getMobileOtmClassStudents(MobileStudentListTo to);

	public Response checkCanUpdateStudentManage(Student student,String StudentManageId,Response response);

	/**
	 * 更新学生表合同产品第一次报读时间
	 * @param student
     */
	public void updateStudentProductFirstTime(Student student);

	/**
	 * 学生转校查找未结算课程
	 * @param studentId
	 * @return
     */
	public Map<String,String> turnCampusWithUnpayMiniCourseByStudentId(String studentId);
	
	/**
	 * 保存潜在学生
	 * @param student
	 */
	public String savePotentialStudent(StudentImportVo vo, String customerId);


	/**
	 * 导入学校数据
	 * @author guoshibo
	 * @param file
     */
	Map<Boolean, String> importStudentSchoolFromExcel(File file) throws Exception ;

	/**
	 * 获取一对一排课信息
	 * @param studentId
	 * @return
	 */
    Map<String, Object> findStudentOneOnOnePaiKeInfo(String studentId);

	/**
	 * 待审核学生学校列表
	 * @param studentSchoolTemp
	 * @param dataPackage
	 * @return
	 */
    DataPackage getSchoolTempList(StudentSchoolTemp studentSchoolTemp, DataPackage dataPackage);

	/**
	 * 根据id查找待审核学校
	 * @param id
	 * @return
	 */
    SchoolTempVo findSchoolTempById(String id);

    void matchingStudentSchool(String id, String matchingSchool);

    void saveNewSchool(StudentSchool studentSchool, String schoolTempId);

	void unvalidSchool(String schoolTempId);

	/**
	 *  @param studentId
	 * @param money
	 * @param remark
	 */
    void rechargeMoney(String studentId, BigDecimal money, String remark);
    
	/**
	 * 开放给教育平台的接口  学生列表
	 * @param dataPackage 封装 pageNum pageSize 
	 * @param studentId
	 * @return
	 */
	Map<String, Object> getStudentInfo(DataPackage dataPackage,String studentId);
	
	/**
	 * 根据班级编号查询该班级内所有学生的信息
	 * @param classId
	 * @return
	 */
	Map<String, Object> getClassStudentInfo(String classId,String type);


	void setStudentRealStatus(StudentStatusMsg msg);



	public List<MobileStudentVo> getMobileStudentList(MobileStudentListTo to) ;


	List getMobileMiniClassStudents(MobileStudentListTo to);

	List getMobilePromiseStudents(MobileStudentListTo to);

	/**
	 * 获取学生地图 地域分析
	 * @param vo
	 * @return
	 */
	Map<String, StudentMap> getStudentAddressAndLatLog(ParamVo vo);

	Student findStudentByRelateNo(String studentRelateNo);

    StudentFollowUpVo getStudentFollowUpById(String id);

	Response delStudentFollowUpById(String id);

	boolean updateStudentContact(StudentChangeVo studentChangeVo, HttpServletResponse response) throws Exception;

	/**
	 * 检查手机号码是否被使用
	 * @param contact
	 * @return
	 */
	boolean isContactUsed(String contact);

	/**
	 * 查询咨询师今日签单的学生信息
	 * @author: duanmenrun
	 * @Title: getStudentDetailByContact 
	 * @Description: TODO 
	 * @throws 
	 * @param user
	 * @param studentContact
	 * @return
	 */
	public List<StudentTransaferVo> getStudentDetailByContact(User user, String studentContact);
	
	/**
	 * 根据学生编号查询学生分公司城市和年级
	 * @param studentId
	 * @return
	 */
	StudentCityGradeVo findStudentCityGradeVoByStudentId(String studentId);
	/**
	 * 培优查询分公司学生
	 * @author: duanmenrun
	 * @Title: getStudentDetailByContactAdvance 
	 * @Description: TODO 
	 * @param user
	 * @param studentContact
	 * @return
	 */
	public List<StudentTransaferVo> getStudentDetailByContactAdvance(User user, String studentContact);

	/**
	 * 根据学管ID跟校区ID获取学生信息
	 * @param userId
	 * @param changeCampusId
	 * @return
	 */
	List<Student> findStuByManegerIdAndCampusId(String userId, String changeCampusId);
}
