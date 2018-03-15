package com.eduboss.dao;

import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.dto.DataPackage;

public interface CourseHoursDistributeRecordDao extends GenericDAO<CourseHoursDistributeRecord, Integer> {

	/**
	 * 根据合同产品id分页查询课时分配流水
	 * @param dp
	 * @param contractProductId
	 * @return
	 */
	DataPackage findPageDistributeRecordByCpId(DataPackage dp, String contractProductId);
	
}
