package com.eduboss.service;

import com.eduboss.domainVo.MiniClassCourseVo;


public interface PushMessageService {

	/**
	 * 获取到相应用户的校区公告
	 * @param mobileUserId
	 * @return
	 */
	MiniClassCourseVo getNoticesByUserId(String mobileUserId);

	
}
