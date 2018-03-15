package com.eduboss.service.dwr;

import java.util.List;
import java.util.Map;

import com.eduboss.domain.MessageRecord;
import com.eduboss.dto.OnlineUser;

public interface DwrMessageManager {

	/**
	 * 初始化DWR
	 * @param userId 用户Id
	 */
	public void onPageLoad(String userId);
	
	/**
	 * 发送信息给单个用户
	 * @param message 消息内容
	 */
	public void sendMessageToSingle(String userId, MessageRecord message);
	
	/**
	 * 发送信息给指定用户
	 * @param sendMap key:userId, value:message
	 */
	public void sendMessageToAssign(Map<String, MessageRecord>sendMap);
	
	/**
	 * 发送信息给所有用户
	 * @param message 消息内容
	 */
	public void sendMessageToAll(MessageRecord message);
	
	/**
	 * 发送信息指定的用户
	 * @param message 消息内容
	 */
	public void sendMessageToUserList(MessageRecord message, List<String> userIdList);
	
	/**
	 * 获取所有在线用户
	 */
	public List<OnlineUser> getAllOnlineUsers();
	
	/**
	 * 检查用户是否在线
	 */
	public boolean isUserOnline(String userId);
}
