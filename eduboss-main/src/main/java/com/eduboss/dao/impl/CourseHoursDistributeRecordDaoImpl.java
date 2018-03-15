package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.eduboss.dao.CourseHoursDistributeRecordDao;
import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.dto.DataPackage;

@Repository
public class CourseHoursDistributeRecordDaoImpl extends GenericDaoImpl<CourseHoursDistributeRecord, Integer> implements CourseHoursDistributeRecordDao {

	/**
	 * 根据合同产品id分页查询课时分配流水
	 */
	@Override
	public DataPackage findPageDistributeRecordByCpId(DataPackage dp, String contractProductId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from CourseHoursDistributeRecord where contractProductId = :contractProductId ";
		hql += " order by createTime desc ";
		params.put("contractProductId", contractProductId);
		return super.findPageByHQL(hql, dp, true, params);
	}
	
}
