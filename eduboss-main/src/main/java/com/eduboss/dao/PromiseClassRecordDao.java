package com.eduboss.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.domain.PromiseClassRecord;
import com.eduboss.domainVo.PromiseClassRecordVo;

@Repository
public interface PromiseClassRecordDao extends GenericDAO<PromiseClassRecord,String>{
	

	/**
	 * 合同完结时判断是否还有未扣款的月份的学生
	 * */
	public String getStudentRecordIsInProgress(String studentId);
	
	/**
	 * 根据目标班ID和学生ID查询月结信息
	 * */
	public List<PromiseClassRecordVo> getPromiseClassRecordByStudentIdAndPrmoseClassId(String promiseClassId,String studentId);
	
	
	
	/**
	 * 根据学生ID和目标班状态查询月结信息
	 */
	public List<PromiseClassRecord> getPromiseClassRecordByStudentIdAndStatus(String studentId, String status);
	
	/**
	 * 根据学生ID计算目标班月结扣费课时
	 */
	public BigDecimal countStudentPromiseChargeHoursByStudentId(String studentId);
	
	public List<PromiseClassRecordVo> findPromiseClassRecordByConProIdAndStuId(
			String contractProductId, String studentId);

}
