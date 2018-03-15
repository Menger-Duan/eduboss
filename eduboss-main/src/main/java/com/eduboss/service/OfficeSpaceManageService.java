package com.eduboss.service;

import java.util.Map;

import com.eduboss.common.OrganizationType;
import com.eduboss.domainVo.OfficeSpaceManageVo;
import com.eduboss.dto.DataPackage;

public interface OfficeSpaceManageService {

	/**
	 * 查询
	 * @param dataPackage
	 * @param officeSpaceManageVo
	 * @param params
	 * @return
	 */
	DataPackage getOfficeSpaceManageList(DataPackage dataPackage,
			OfficeSpaceManageVo officeSpaceManageVo, Map<String, Object> params);
	
	/**
	 * 新增信息
	 * @param officeSpaceManageVo
	 */
	void saveOfficeSpaceManage(OfficeSpaceManageVo officeSpaceManageVo);
	
	/**
	 * 查找1条信息
	 * @param id
	 * @return
	 */
	OfficeSpaceManageVo findOfficeSpaceById(String id);
	
	/**
	 * 修改
	 * @param classroomManageVo
	 */
	void modifyOfficeSpaceManage(OfficeSpaceManageVo officeSpaceManageVo);
	
	/**
	 * 删除
	 * @param id
	 */
	void deleteOfficeSpaceManage(String id);
	
	
	/**
	 * 添加场地前判断
	 */
	public void beforSaveOfficeSpace( String orgId,String space,String fid) throws Exception;

/**
	 * 场地信息统计
	 */
	DataPackage officeSpaceCountList(DataPackage dataPackage,String campusType,String organizationIdFinder,OrganizationType organizationType);


}
