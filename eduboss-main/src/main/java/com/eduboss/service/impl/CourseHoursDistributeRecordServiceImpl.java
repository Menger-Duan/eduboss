package com.eduboss.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.dao.CourseHoursDistributeRecordDao;
import com.eduboss.domain.CourseHoursDistributeRecord;
import com.eduboss.domainVo.CourseHoursDistributeRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.CourseHoursDistributeRecordService;
import com.eduboss.utils.HibernateUtils;

@Service
public class CourseHoursDistributeRecordServiceImpl implements CourseHoursDistributeRecordService {

	@Autowired
	private CourseHoursDistributeRecordDao courseHoursDistributeRecordDao;
	
	/**
	 * 根据合同产品id分页查询课时分配流水
	 */
	@Override
	public DataPackage findPageDistributeRecordByCpId(DataPackage dp, String contractProductId) {
		dp = courseHoursDistributeRecordDao.findPageDistributeRecordByCpId(dp, contractProductId);
		List<CourseHoursDistributeRecord> list = (List<CourseHoursDistributeRecord>) dp.getDatas();
		dp.setDatas(HibernateUtils.voListMapping(list, CourseHoursDistributeRecordVo.class));
		return dp;
	}
	
	/**
	 * 保存或修改课时分配流水
	 */
	@Override
	public void saveOrUpdateCourseHoursDistributeRecord(CourseHoursDistributeRecord record) {
		if (record.getId() > 0) {
			courseHoursDistributeRecordDao.merge(record);
		} else {
			courseHoursDistributeRecordDao.save(record);
		}
	}
	
}
