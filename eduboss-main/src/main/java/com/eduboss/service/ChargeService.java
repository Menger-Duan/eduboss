package com.eduboss.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.eduboss.common.PayType;
import com.eduboss.domain.*;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.domainVo.ChargeOrWashCurriculumVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.dto.TimeVo;

public interface ChargeService {
	
	/**
	 * 扣一对一课程费用
	 * @param courseId
	 * @param auditedCourseHours
	 * @param modifyUser
	 */
	public BigDecimal chargeOneOnOneCourse(String courseId , User user);
	
	/**
	 * 扣每次小班费用， 每上一次课就会扣钱
	 * 先判断 这个课程 是不是 开课的（STARTED）,而且有 剩余资金的
	 * @param miniClassId
	 * @param studentId
	 * @return true 是已经扣费啦， false是还没有扣费
	 */
	public boolean chargeMiniClass(String miniClassStudentAttendentId, User user);
	public boolean chargeMiniClass_Edu(String miniClassStudentAttendentId, User user);
	
	/**
	 * 扣每次一对多费用， 每上一次课就会扣钱
	 * 先判断 这个课程 是不是 开课的（STARTED）,而且有 剩余资金的
	 * @param otmClassStudentAttendentId
	 * @param studentId
	 * @return true 是已经扣费啦， false是还没有扣费
	 */
	public boolean chargeOtmClass(String otmClassStudentAttendentId, User user);
	
	/**
	 * <!-- 转移小班费用，从一对一账户中， 先把小班的钱扣到小班指定的账号池内，并且修改每一个小班的状态-->
	 * 上面的逻辑 已经是不需要了
	 * 当进行报名扣费的时候， 需要确保这个小班的产品是已经 paid状态 且 paidAmount 相等于 planAmount 的时候， 就可以把它的状态改为 上课
	 * @param miniClassId
	 * @param studentId
	 * @return true: 成功转入， false就不成功
	 */
	public boolean chargeToMiniClassAccount(String miniClassId,  String stuId,  User user);
	
	
	/**
	 * 获取扣费记录
	 * @param dataPackage
	 * @param contractVo
	 * @param timeVo
	 * @return
	 */
	public DataPackage findPageMyCharge(DataPackage dataPackage, AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo);
	
	/**
	 * 回滚扣费操作
	 * @param moneyRollbackRecords
	 */
	public void rollbackCharge(MoneyRollbackRecords moneyRollbackRecords);
	
	/**
	 * 冲销扣费
	 * @param moneyWashRecords
	 * @return
	 */
	public void washCharge(MoneyWashRecords moneyWashRecords);
	
	/**
	 * 获取冲销扣费金额
	 * @param transactionId
	 */
	public Map<String, Object> getWashChargeAmount(String transactionId);
	
	/**
	  * 一对一课程审批回滚
	 * @param moneyRollbackRecords
	 * @param courseId
	 */
	public void rollbackCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String courseId);
	
	/**
	 * 小班课程审批回滚
	 * @param moneyRollbackRecords
	 * @param miniClassCourseId
	 * @return
	 */
	public Response rollbackMiniClassCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String miniClassCourseId);
	
	/**
	 * 一对多课程审批回滚
	 * @param moneyRollbackRecords
	 * @param otmClassCourseId
	 * @return
	 */
	public Response rollbackOtmClassCourseCharge(MoneyRollbackRecords moneyRollbackRecords, String otmClassCourseId);
	
	/**
	  * 一对一课程审批冲销
	 * @param moneyWashRecords
	 * @param courseId
	 */
	public void washCourseCharge(MoneyWashRecords moneyWashRecords, String courseId);
	
	/**
	 * 小班课程审批冲销
	 * @param moneyWashRecords
	 * @param miniClassCourseId
	 * @return
	 */
	public Response washMiniClassCourseCharge(MoneyWashRecords moneyWashRecords, String miniClassCourseId);
	
	/**
	 * 一对多课程审批冲销
	 * @param moneyWashRecords
	 * @param otmClassCourseId
	 * @return
	 */
	public Response washOtmClassCourseCharge(MoneyWashRecords moneyWashRecords, String otmClassCourseId);

	/**
	 * 从合同资金池里面 assign 资金到 相应的产品， 先留空， 后面可能有用
	 * @param conProId
	 * @param currentLoginUser
	 */
	public void assignMoneyToOtherProdect(String conProId, User currentLoginUser);

	void updateContractTrigger(BigDecimal detal, ContractProduct contractProduct);

	/**
	 * 判断这个 合同产品 是不是已经产生了扣费啦
	 * @param contractProduct
	 * @return
	 */
	public boolean hasChargeRecordOfContractProduct(
			ContractProduct contractProduct);

	/**
	 * 获取合同产品的消费总额
	 * @param processingProductInHibernate
	 * @return
	 */
	public BigDecimal getChargeAmount(
			ContractProduct processingProductInHibernate);
	
	/**
	 * 获取合同产品的实收消费总额
	 * @param processingProductInHibernate
	 * @return
	 */
	public BigDecimal getRealChargeAmount(String id,PayType payType);

	/**
	 * 对不同老师  不同的类型的扣费的 一个总结汇总
	 * @param productType
	 * @param teacherId
	 * @return
	 */
	public List<AccountChargeRecordsVo> findChargeRecordsForTeacher(
			String productType, String teacherId);
	
	
	/**
	 * 对不同老师  不同的类型的扣费的 一个总结汇总
	 * @param productType
	 * @param teacherId
	 * @param searchDate
	 * @return
	 */
	public List<AccountChargeRecordsVo> findChargeRecordsForTeacher(
			String productType, String teacherId,String searchDate);

	
	/**
	 * 扣费记录导出Excel
	 * @param dataPackage
	 * @param accountChargeRecordsVo
	 * @param timeVo
	 * @return
	 */
	public List findPageMyChargeExcel(DataPackage dataPackage,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo);
	
	/**
	 * 根据id查询资金冲销详情记录
	 * @param id
	 * @return
	 */
	public Map<String, Object> findMoneyWashRecordsByTransactionId(String transactionId);
	
	/**
	 * 修改冲销详情
	 * @param moneyWashRecords
	 */
	public void editMoneyWashRecords(MoneyWashRecords moneyWashRecords);

    void washLiveCharge(Curriculum curriculum);

    void washLiveChargeList(ChargeOrWashCurriculumVo chargeOrWashCurriculumVo);
    
    /** 
     * 根据合同产品找扣费记录
    * @param contractProductId
    * @param isWashed
    * @return
    */
    List<AccountChargeRecords> findAccountRecordsByContractProduct(String contractProductId, String isWashed);
}

