package com.eduboss.utils;

import com.eduboss.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 检测系统某些信息的工具类
 */
public class CheckSystemUtils {
	private static Logger log= Logger.getLogger(CheckSystemUtils.class);

	/**
	 * 判断是否是用新组织架构角色配置。
	 * @return
	 */
	public static Boolean checkNewOrg(){
		if(StringUtils.isNotBlank(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE)) && "0".equals(PropertiesUtils.getStringValue(Constants.NEW_ORG_ROLE))) {
			return true;
		}
		return false;
	}



}
