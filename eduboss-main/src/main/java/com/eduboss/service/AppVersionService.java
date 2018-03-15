package com.eduboss.service;

import com.eduboss.domainVo.AppVersionVo;
import com.eduboss.dto.DataPackage;


public interface AppVersionService {

	public DataPackage getAppVersionList(DataPackage datapackage,AppVersionVo appVersionVo
			,String startDate,String endDate);
	
	public String saveOrUpdateAppVersion(AppVersionVo appVersionVo);
	
	public void deleteAppVersion(String appVersionId);
	
	public AppVersionVo findAppVersionVoById(String id);
	
	public void pushAppVersionToMobile (String versionStr,String mobileType);
	
	public AppVersionVo getInfoByMbUserType(String type,String appId,String isUpdate);
}

