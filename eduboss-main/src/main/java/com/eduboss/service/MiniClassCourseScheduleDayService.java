package com.eduboss.service;

import java.util.Map;

import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;

public interface MiniClassCourseScheduleDayService {

	/**
	 * 查询
	 * @param dataPackage
	 * @param classroomManageVo
	 * @param params
	 * @return
	 */
	DataPackage getMiniClassCourseScheduleDayList(DataPackage dataPackage, Map<String, Object> params);

}
