package com.eduboss.dao;

import com.eduboss.domain.MoneyWashRecords;

public interface MoneyWashRecordsDao extends GenericDAO<MoneyWashRecords, String> {
	
	/**
	 * 根据transactionId查找冲销详情记录
	 * @param transactionId
	 * @return
	 */
	public MoneyWashRecords findByTransactionId(String transactionId);
	
	/**
	 * 根据courseId查询冲销详情
	 * @param courseId
	 * @return
	 */
	public MoneyWashRecords findMoneyWashRecordsByCourseId(String courseId);
	
	/**
	 * 根据miniCourseId查询冲销详情
	 * @param miniCourseId
	 * @return
	 */
	public MoneyWashRecords findMoneyWashRecordsByMiniCourseId(String miniCourseId);
	
	/**
	 * 根据otmCourseId查询冲销详情
	 * @param otmCourseId
	 * @return
	 */
	public MoneyWashRecords findMoneyWashRecordsByOtmCourseId(String otmCourseId);
	
}
