package com.eduboss.dao;

import java.util.Map;

import com.eduboss.domain.ClassroomManage;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;

public interface ClassroomManageDao extends GenericDAO<ClassroomManage, String>{

	DataPackage getContractPayManageList(DataPackage dataPackage,
			ClassroomManageVo classroomManageVo, Map<String, Object> params);

}
