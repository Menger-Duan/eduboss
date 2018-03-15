package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.common.MessageDeliverType;
import com.eduboss.common.MessageType;
import com.eduboss.domain.MessageDeliverRecordId;

@Service
public interface MessageService {
	
	/**
	 * 发送消息
	 * 入参：消息类型，消息标题，消息内容，接收类型，接口目标(多个目标用英文逗号分隔)
	 */
	public void sendMessage(MessageType messageType, String messageTitle, String mesasge, MessageDeliverType deliverType, String deliverTargets);
	
	/**
	 * 根据条件获取消息列表
	 * 入参：消息对象，里面包含查询条件
	 * 出参：消息列表（在创建消息对象后修改）
	 */
//	public List<PushMessage> getMessage(PushMessage pushMessage);
	
	/**
	 * 消息已读
	 * 入参：消息ID
	 * 把某个对某个消息已读状态保存下来
	 */
	public void readMessage(MessageDeliverRecordId messageId);
	
}
