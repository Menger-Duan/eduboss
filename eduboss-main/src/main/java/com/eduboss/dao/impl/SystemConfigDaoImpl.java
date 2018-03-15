package com.eduboss.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.dao.SystemConfigDao;
import com.eduboss.domain.SystemConfig;
import com.eduboss.domain.User;
import com.eduboss.service.UserService;
import com.eduboss.utils.DateTools;

@Repository
public class SystemConfigDaoImpl extends GenericDaoImpl<SystemConfig, String> implements SystemConfigDao{
	
	@Autowired
	private UserService userSerivce;

	@Override
	public SystemConfig getSystemPathByGroupId(SystemConfig systemConfig) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from SystemConfig where 1=1");
		if(StringUtils.isNotBlank(systemConfig.getTag())){
			hql.append(" and tag = :tag ");
			params.put("tag", systemConfig.getTag());
		}
		if(systemConfig.getGroup() != null && StringUtils.isNotBlank(systemConfig.getGroup().getId())){
			hql.append(" and group.id = :groupId ");
			params.put("groupId", systemConfig.getGroup().getId());
		}
		List<SystemConfig> systemCf = super.findAllByHQL(hql.toString(), params);
		if(systemCf !=null && systemCf.size()>0){
			return systemCf.get(0);
		}
		return null;
	}

	@Override
	public void saveOrUpdateSystemConfig(SystemConfig systemConfig) {
		if(StringUtils.isBlank(systemConfig.getId())){
			systemConfig.setId(null);
		}
		User user = new User();
		if(systemConfig.getId()==null){
			systemConfig.setCreateTime(DateTools.getCurrentDateTime());
			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
			systemConfig.setCreateUser(user);
			super.save(systemConfig);
		}else{
			SystemConfig sc = super.findById(systemConfig.getId());
			sc.setModifyTime(DateTools.getCurrentDateTime());
			user.setUserId(userSerivce.getCurrentLoginUser().getUserId());
			sc.setModifyUser(user);
			if(StringUtils.isNotBlank(systemConfig.getName())){
				sc.setName(systemConfig.getName());
			}
			if(StringUtils.isNotBlank(systemConfig.getValue())){
				sc.setValue(systemConfig.getValue());
			}
			if(StringUtils.isNotBlank(systemConfig.getRemark())){
				sc.setRemark(systemConfig.getRemark());
			}
			super.save(sc);
		}
		
	}

}
