package com.eduboss.dao;

import com.eduboss.domain.SystemConfig;

public interface SystemConfigDao extends GenericDAO<SystemConfig, String>{
	
	//获取集团图片存放路径
	public SystemConfig getSystemPathByGroupId(SystemConfig systemConfig);
	
	//保存集团图片存放路径
	public void saveOrUpdateSystemConfig(SystemConfig systemConfig);

}
