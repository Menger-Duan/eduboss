package com.eduboss.dao;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.ReceptionistEveryDay;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.domainVo.ReceptionistEveryDayVo;
import com.eduboss.dto.DataPackage;

public interface ReceptionistEveryDayDao extends GenericDAO<ReceptionistEveryDay, String> {
	
	/**
	 * 列表每日信息
	 */
	
	DataPackage getReceptionistEveryDays(DataPackage dataPackage,ReceptionistEveryDayVo receptionistEveryDayVo, Map<String, Object> params);
	
	/**
	 *校区资源利用
	 * @param dataPackage
	 * @param receptionistEveryDayVo
	 * @param params
	 * @return
	 */
	DataPackage shoolResourceList(DataPackage dataPackage,ReceptionistEveryDayVo receptionistEveryDayVo, Map<String, Object> params);
	
	ReceptionistEveryDay findByloginDateAndOrganizationId(String loginDate, String organizationId);
	
	
}
