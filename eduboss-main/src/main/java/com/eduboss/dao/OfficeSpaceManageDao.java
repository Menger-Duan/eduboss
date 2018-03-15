package com.eduboss.dao;

import java.util.Map;

import com.eduboss.common.OrganizationType;
import com.eduboss.domain.OfficeSpaceManage;
import com.eduboss.domainVo.OfficeSpaceManageVo;
import com.eduboss.dto.DataPackage;

public interface OfficeSpaceManageDao extends GenericDAO<OfficeSpaceManage, String>{

	DataPackage getOfficeSpaceManageList(DataPackage dataPackage,
			OfficeSpaceManageVo officeSpaceManageVo, Map<String, Object> params);

	/**
	 * 按组织架构找到场地信息
	 * @param id
	 * @return
	 */
	OfficeSpaceManageVo findOfficeSpaceByOrgId(String id);
	
	/**
	 * 添加场地前判断
	 */
	public int beforSaveOfficeSpace( String orgId,String space,String fid);
	
	/**
	 * 场地信息统计
	 */
	DataPackage officeSpaceCountList(DataPackage dataPackage,String campusType,String organizationIdFinder,OrganizationType organizationType);
}

	

