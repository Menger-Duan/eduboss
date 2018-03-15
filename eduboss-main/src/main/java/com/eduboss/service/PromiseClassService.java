package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.eduboss.domainVo.*;
import com.eduboss.dto.PromiseChargeSearchDto;
import com.eduboss.dto.PromiseStudentSearchDto;
import org.springframework.stereotype.Service;

import com.eduboss.common.ProductType;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domain.PromiseClass;
import com.eduboss.domain.PromiseClassDetailRecord;
import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domain.PromiseStudent;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;


/**
 * 目标班
 * @author laiyongchang
 * */
@Service
public interface PromiseClassService {
	
	/**
	 * 查询目标班报名和管理列表
	 * */
	public DataPackage getPromiseClassList(PromiseClass promiseClass, DataPackage dataPackage);
	
	/**
	 * 根据ID查询目标班
	 */
	public PromiseClass findPromiseClassById(String promiseClassId);
	
	
	/**
	 * 查询目标班学生管理（班主任）
	 * */
	public DataPackage getPromiseStudentList(PromiseStudentSearchDto dto, DataPackage dataPackage);
	
	/**
	 * 保存新增目标班信息
	 * */
	public PromiseClass savePromiseClass(PromiseClass promiseClass);

	public List getPromiseSubjectDetailList(String promiseStudentId);
	
	/**
	 * 删除目标班信息
	 * */
	public void deletePromiseClassByProClaId(String promiseClassId);
	
	/**
	 * 学生报名目标班
	 * */
	public void savaPromiveClassStudent(PromiseStudent promiseStudent);
	
	/**
	 * 目标班退班
	 * */
	public void cancelPromiseClassApply(String studentId,String promiseClassId,String contractProductId);
	
	/**
	 * 查询符合报名目标班的学生
	 * */
	public List<PromiseClassApplyVo> getStudentWantList(ProductType type,String gradeId,String promiseClassId,String status,String studentId,String studentName);
	
	/**
	 * 根据目标班ID查询所有学生
	 * */
	public DataPackage getStudentListByPromiseClassId(Map params,DataPackage dataPackage);
	

	/**
	 * 根据合同产品获取所有学生
	 * */
	public List<PromiseStudent> getStudentListByContractProduct(ContractProduct contractProduct);
	
	/**
	 * 删除目标班学生
	 * */
	public void deletePromiseStudent(PromiseStudent promiseStudent);
	
	/**
	 * 保存月结记录
	 * */
	public PromiseClassRecord savaPromiseClassRecord(PromiseClassRecord promiseClassRecord,PromiseClassDetailRecord detail,String contractProductId);
	
	/**
	 * 保存月结记录详细信息
	 * */
	public void savePrmoseClassDetailRecord(PromiseClassDetailRecord detail,String recordId);
	
	/**
	 * 对目标班结课
	 * */
	public void endPromiseClass(String promiseClassId);
	
	/**
	 * 合同完结时判断是否还有未扣款的月份的学生
	 * */
	public String getStudentRecordIsInProgress(String studentId,String contractProductId);
	
	/**
	 * 根据目标班ID和学生ID查询月结记录信息
	 * */
	public List<PromiseClassRecordVo> findPrmoseClassRecordByProClaIdAndStuId(String promiseClassId,String studentId);
	
	/**
	 * 根据目标班ID和学生ID删除所有月结记录信息
	 * */
	public void deletePrmoseClassRecordByProClaIdAndStuId(String promiseClassId,String studentId);
	
	/**
	 * 班主任学生管理界面点详情按钮，查出该学生的合同信息
	 * */
	public List<PromiseClassApplyVo> findStudentContractInfo(String studentId,String contracProductId);
	
	/**
	 * 合同完结
	 * */
	public void endPromiseClassContract(PromiseClassRecord record,String resultStatus);
	
	/**
	 * 查询学生月结详细信息
	 * */
	public List<PromiseClassDetailRecordVo>  findStudentMonthlyDetailInfo(PromiseClassRecord record);
	
	/**
	 * 查询目标班学生基本信息
	 * */
	public List<PromiseClassApplyVo> getPromiseStudentInfo(PromiseStudent promiseStudent);

	public List<PromiseClassRecordVo> findPromiseClassRecordByConProIdAndStuId(
			String contractProductId, String studentId);
	
	public void promiseClassAutoCount(Integer startDate,Integer month,String countYearMonth,String lastDate);

	/**
	 * 根据目标班学生ID获取能跳转的目标班校区列表
	 * @param promiseStudentId
	 * @return
	 */
	public List<OrganizationVo> getCanTurnToClassOrganization(
			String promiseStudentId);

	/**
	 * 根据校区获取目标班
	 * @param campusId
	 * @return
	 */
	public List<PromiseClassVo> getPromiseClassByCampus(String campusId);

	/**
	 * 转换目标班
	 * @param promiseStudentId  原目标班学生ID
	 * @param turnPromiseClassId 新目标班ID
	 * @return
	 */
	public Response turnPromiseClass(String promiseStudentId,
			String turnPromiseClassId,String newCourseDate);

    Response savePromiseSubject(TreeSet<PromiseClassSubjectVo> subjectVos, String promiseStudentId);

	Response savePromiseToMiniClass(PromiseClassSubjectVo vo);

	DataPackage getPromiseSubjectList(DataPackage dp, String promiseStudentId);

	DataPackage findMiniClassByPromiseSubjectId(DataPackage dp, Integer promiseSubjectId,String teacherId,String organizationId,String name);

	Response deleteStudentFromClass(Integer id);

	PromiseStudentCustomerVo findStudentInfoByPromiseStudentId(String promiseStudentId);

    Response startAuditPromiseStudent(String promiseStudentId, String resultStatus);

	Response confirmAuditPromiseStudent(int auditId, String resultStatus,String remark);


	/**
	 * 获取精英班合同信息
	 * @param contractProductId
	 * @return
	 */
	Map<Object,Object> getEcsContractInfo(String contractProductId);

	Map<Object,Object> getEcsContractChargeInfo(String contractProductId);

	DataPackage getEcsContractChargeList(String contractProductId,PromiseChargeSearchDto dto,DataPackage dataPackage );

	Response savaPromiseReturnAuditInfo(PromiseStudent promiseStudent);

	/**
	 * 判断是否是2019年及以后的目标班合同产品
	 * @param targetConPrd
	 * @return
	 */
	boolean isYearAfter2018(ContractProduct targetConPrd);

}
