package com.eduboss.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.eduboss.dto.RoleQLConfigSearchVo;
import com.google.common.collect.Maps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.ClassroomManageDao;
import com.eduboss.domain.ClassroomManage;
import com.eduboss.domainVo.ClassroomManageVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.service.RoleQLConfigService;
import com.eduboss.utils.StringUtil;

@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class ClassroomManageDaoImpl extends GenericDaoImpl<ClassroomManage, String> implements ClassroomManageDao{

	@Autowired
	private RoleQLConfigService roleQLConfigService;
	
	public DataPackage getContractPayManageList(DataPackage dataPackage,
			ClassroomManageVo classroomManageVo, Map<String, Object> params) {
		StringBuilder hql = new StringBuilder();
		Map<String, Object> map = Maps.newHashMap();
		hql.append(" from ClassroomManage where 1=1");
		Object o = params.get("branchOrgLevel");
		if (o!=null){
			String branchOrgLevel = o.toString();
			if (StringUtil.isNotBlank(branchOrgLevel)){
				hql.append(" and organization.orgLevel like :branchOrgLevel ");
				map.put("branchOrgLevel", branchOrgLevel+"%");
			}
		}

		
		if(StringUtil.isNotBlank(params.get("organizationId").toString())){
			hql.append(" and organization.id = :organizationId ");
			map.put("organizationId", params.get("organizationId").toString());
		}
		if(StringUtil.isNotBlank(params.get("classroom").toString())){
			hql.append(" and classroom like :classroom ");
			map.put("classroom", "%"+params.get("classroom").toString()+"%");
		}
		if(StringUtil.isNotBlank(params.get("areaStart").toString())){
			hql.append(" and classArea >=  :areaStart ");
			map.put("areaStart", new BigDecimal(params.get("areaStart").toString()));
		}
		if(StringUtil.isNotBlank(params.get("areaEnd").toString())){
			hql.append(" and classArea <= :areaEnd ");
			map.put("areaEnd", new BigDecimal(params.get("areaEnd").toString()));
		}
		if(StringUtil.isNotBlank(params.get("status").toString())){
			hql.append(" and status = :status ");
			map.put("status", Integer.parseInt(params.get("status").toString()));
		}		
		if(StringUtil.isNotBlank(params.get("classType").toString())){
			hql.append(" and classType.id = :classType ");
			map.put("classType", params.get("classType").toString());
		}

		Map sqlMap = new HashMap();
		sqlMap.put("hqlOrg","organization.orgLevel");
		RoleQLConfigSearchVo rvo=new RoleQLConfigSearchVo("教室管理","nsql","hql");
		hql.append(roleQLConfigService.getAppendSqlByOrgAndRoleByConfig(rvo,sqlMap));

		hql.append(" order by modifyTime desc, id desc ");
		return super.findPageByHQL(hql.toString(), dataPackage, true, map);
	}

}
