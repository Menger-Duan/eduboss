package com.eduboss.service;

import com.eduboss.common.MobileType;
import com.eduboss.domain.AppVersion;

public interface AppFunction {

	/**
	 * ���� appId �� ƽ̨���� �����Ƿ������µİ汾
	 * @param appId 
	 * @param mobileType
	 * @return
	 */
	public AppVersion getLastAppVersion(String appId, MobileType mobileType);
	
}
