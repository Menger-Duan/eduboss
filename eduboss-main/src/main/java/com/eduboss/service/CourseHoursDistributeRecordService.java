package com.eduboss.service;

import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.dto.DataPackage;

public interface CourseHoursDistributeRecordService {

	/**
	 * 根据合同产品id分页查询课时分配流水
	 * @param dp
	 * @param contractProductId
	 * @return
	 */
	DataPackage findPageDistributeRecordByCpId(DataPackage dp, String contractProductId);
	
	/**
	 * 保存或修改课时分配流水
	 * @param record
	 */
	void saveOrUpdateCourseHoursDistributeRecord(CourseHoursDistributeRecord record);
	
}
