package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import com.eduboss.common.PayType;
import com.eduboss.common.ProductType;
import com.eduboss.domain.AccountChargeRecords;
import com.eduboss.domain.Contract;
import com.eduboss.domain.ContractProduct;
import com.eduboss.domainVo.AccountChargeRecordsVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.TimeVo;

public interface AccountChargeRecordsDao extends GenericDAO<AccountChargeRecords, String> {
	
	
	public DataPackage listAccountChargeRecord(DataPackage dp,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo);

	public List getExcelChargeList(DataPackage dp,
											   AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo);
	
	public void save(AccountChargeRecords accountChargeRecords);

	/**
	 * #355小班学生详情课程扣费金额错误 处理：可能有多条扣费记录的 优惠加实收
	 * 取得某个学生小班课程的扣费记录
	 * @param miniClassCourseId
	 * @param studentId
	 * @return
	 */
	public List<AccountChargeRecords> getMiniClassCourseRecordByStudentId(
			String miniClassCourseId, String studentId);
	
	/**
	 * 取得某个学生一对多课程的扣费记录
	 * @param otmClassCourseId
	 * @param studentId
	 * @return
	 */
	public List<AccountChargeRecords> getOtmClassCourseRecordByStudentId(
			String otmClassCourseId, String studentId);

	/**
	 * 判断某合同产品是不是已经扣费啦
	 * @param contractProduct
	 * @return
	 */
	public boolean hasChargeRecord(ContractProduct contractProduct);
	
	/**
	 * 判断某合同是不是已经扣费啦
	 * @param contract
	 * @return
	 */
	public boolean hasChargeRecord(Contract contract);

	/**
	 * 获取 扣费总额
	 * @param conPrd
	 * @return
	 */
	public BigDecimal getChargeAmount(ContractProduct conPrd);
	
	/**
	 * 获取实收扣费总额
	 * @param conPrd
	 * @return
	 */
	public BigDecimal getRealChargeAmount(String id,PayType payType);

	/**
	 * 某个小班课程的总扣费金额
	 * @param miniClassId
	 * @return
	 */
	public BigDecimal getMiniClassConsumeFinance(String miniClassId);
	
	/**
	 * 某个一对多课程的总扣费金额
	 * @param otmClassCourseId
	 * @return
	 */
	public BigDecimal getOtmClassConsumeFinance(String otmClassCourseId);

	/**
	 * 获取小班课程扣费人数
	 * @param miniClassCourseId
	 * @return
	 */
	public int getMiniClassCourseChargeStudentNum(String miniClassCourseId);
	
	/**
	 * 获取一对多课程扣费人数
	 * @param otmClassCourseId
	 * @return
	 */
	public int getOtmClassCourseChargeStudentNum(String otmClassCourseId);

	/**
	 * 查找到 相应的老师的 扣费记录
	 * @param productType
	 * @param teacherId
	 * @return
	 */
	public List<AccountChargeRecords> findChargeRecordsForTeacher(
			String productType, String teacherId);
	
	/**
	 * 查找到 相应的老师的 扣费记录
	 * @param productType
	 * @param teacherId
	 * @param searchDate
	 * @return
	 */
	public List<AccountChargeRecords> findChargeRecordsForTeacher(
			String productType, String teacherId,String searchDate);

	/**
	 * 仅获取小班课程已扣费的人材
	 * @param miniClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getMiniClassCourseChargeStudentList(String miniClassCourseId);
	
	/**
	 * 仅获取一对多课程已扣费的扣费记录
	 * @param miniClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getOtmClassCourseChargeStudentList(String otmClassCourseId);
	
	/**
	 * 根据transactionId获取扣费记录
	 * @param transactionId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByTransactionId(String transactionId);
	
	/**
	 * 根据courseId获取扣费记录（排除有冲销记录的）
	 * @param courseId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByCourseId(String courseId);
	
	/**
	 * 根据courseId计算扣费记录条数
	 * @param courseId
	 * @return
	 */
	public int countAccountChargeRecordsByCourseId(String courseId, ProductType type);
	
	/**
	 * 根据讲座学生ID计算扣费记录条数
	 * @param lectureClassStudentId
	 * @return
	 */
	public int countAccountChargeRecordsByLecStuId(String lectureClassStudentId);
	
	/**
	 * 根据miniClassCourseId获取扣费记录（排除有冲销记录的）
	 * @param miniClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByMiniClassCourseId(String miniClassCourseId);
	
	/**
	 * 根据miniClassCourseId计算扣费记录条数
	 * @param miniClassCourseId
	 * @return
	 */
	public int countAccountChargeRecordsByMiniClassCourseId(String miniClassCourseId);
	
	/**
	 * 根据otmClassCourseId获取扣费记录（排除有冲销记录的）
	 * @param otmClassCourseId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClassCourseId(String otmClassCourseId);
	
	/**
	 * 根据otmClassCourseId计算扣费记录条数
	 * @param otmClassCourseId
	 * @return
	 */
	public int countAccountChargeRecordsByOtmClassCourseId(String otmClassCourseId);
	
	/**
	 * 用于导出Excel
	 * @param dp
	 * @param accountChargeRecordsVo
	 * @param timeVo
	 * @return
	 */
	@Deprecated
	public DataPackage findAccountChargeRecordToExcel(DataPackage dp,
			AccountChargeRecordsVo accountChargeRecordsVo, TimeVo timeVo);
	
	/**
	 * 根据miniClassId和studentId获取扣费记录
	 * @param miniClassCourseId
	 * @param studentId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByMiniClassAndStudent(String miniClassId, String studentId);
	
	/**
	 * 根据一对多ID和学生ID获取扣费记录
	 * @param otmClassId
	 * @param studentId
	 * @return
	 */
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClassAndStudent(String otmClassId, String studentId);
	
	public List<AccountChargeRecords> getAccountChargeRecordsByOtmClass(String otmClassId);
	
	

	/** 
	 * 根据合同产品找扣费记录
	* @param contractProductId
	* @return
	* @author  author :Yao 
	* @date  2016年8月22日 下午7:33:36 
	* @version 1.0 
	*/
	public List<AccountChargeRecords> findAccountRecordsByContractProduct(String contractProductId, String isWashed);

	/**
	 * 根据直播课程id和合同id找到扣费记录
	 *
	 * @param courseId
	 * @param liveId
	 * @param contractId
	 * @return
	 */
    List<AccountChargeRecords> getAccountChargeRecordsByCurriculumId(String courseId, String liveId, String contractId);

    List<AccountChargeRecords> getAccountChargeRecords(ContractProduct targetConPrd);
}
