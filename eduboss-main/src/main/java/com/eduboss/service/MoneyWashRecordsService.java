package com.eduboss.service;

import com.eduboss.domain.MoneyWashRecords;

public interface MoneyWashRecordsService {

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
