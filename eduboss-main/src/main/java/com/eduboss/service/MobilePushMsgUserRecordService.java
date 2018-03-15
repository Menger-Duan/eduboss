package com.eduboss.service;

import java.util.List;
import java.util.Map;





/**
 * @author Administrator
 *
 */
public interface MobilePushMsgUserRecordService {

	/**
	 * add by Yao 
	 * 根据组合Id更改消息记录状态(把状态更改为已读，以及读取时间)
	 * @param mobileUserId
	 * @param sessionId
	 * @param recordId
	 */
	void updateUserRecordStatus(String mobileUserId, String sessionId,String recordId,String sessionType);
	
	/**
	 * 根据组合Id来获取记录条数
	 * @param mobileUserId
	 * @param sessionId
	 * @return
	 */
	String [] getUserRecordCount(String mobileUserId, String sessionId,String sessionType);

	
	
}
