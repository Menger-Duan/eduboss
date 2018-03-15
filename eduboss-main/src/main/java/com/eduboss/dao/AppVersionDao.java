package com.eduboss.dao;

import com.eduboss.domain.AppVersion;
import com.eduboss.domainVo.AppVersionVo;

public interface AppVersionDao extends GenericDAO<AppVersion, String> {
	public int getVersionCount(AppVersionVo appVersion);
	
}
