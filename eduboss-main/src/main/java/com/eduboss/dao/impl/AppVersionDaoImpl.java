package com.eduboss.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eduboss.dao.AppVersionDao;
import com.eduboss.domain.AppVersion;
import com.eduboss.domainVo.AppVersionVo;
import com.google.common.collect.Maps;
import java.util.Map;


@Transactional  //每一个业务方法开始时都会打开一个事务
@Repository     //标识为bean
public class AppVersionDaoImpl extends GenericDaoImpl<AppVersion, String> implements AppVersionDao {

	@Override
	public int getVersionCount(AppVersionVo appVersion){
		StringBuffer sql=new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		sql.append("select count(1) from app_version where 1=1");
		if(StringUtils.isNotBlank(appVersion.getAppId()) && StringUtils.isNotBlank(appVersion.getMobileType().toString())){
			sql.append(" and app_id = :appId and mobile_type= :mobileType ");
			params.put("appId", appVersion.getAppId());
			params.put("mobileType", appVersion.getMobileType());
		}
		if(StringUtils.isNotBlank(appVersion.getId())){
			sql.append(" and id!= :id ");
			params.put("id", appVersion.getId());
		}		
		int count=findCountSql(sql.toString(), params);
		return count;
	}
	
}
