package com.eduboss.service;

import org.springframework.stereotype.Service;

import com.eduboss.common.MsgNo;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SentRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;

@Service
public interface SentRecordService {

	/**
	 * 新增系统信息发送记录
	 * @param sysmsg
	 * @param sendType
	 * @param msgRecipient
	 * @param msgRecipientPhone
	 * @return SentRecord
	 */
	public SentRecord saveSentRecord(SystemMessageManage sysMsg, String sendType, String sendMsg,String sendDetailedMsg, String currentUserId, User msgRecipient, String msgRecipientPhone, MsgNo msgNo);
	
	/**
	 * 新增或修改系统信息发送记录
	 * @param record
	 */
	public void saveOrUpdateSentRecord(SentRecord record);
	
	/**
	 * 查询系统信息发送记录
	 * @param sentRecordVo
	 * @param dp
	 * @return
	 */
	public DataPackage getSentRecordList(SentRecordVo sentRecordVo , DataPackage dp);
	
	/**
	 * 阅读发送的系统信息
	 * @param recordId
	 * @return
	 */
	public Response readSentRecord(String recordIds);
	
	/**
	 * 发送系统信息
	 * @param sentRecordVo
	 * @return
	 */
	public Response sendRecord(SentRecordVo sentRecordVo);
	
	/**
	 * 重发系统信息
	 * @param recordId
	 * @return
	 */
	public Response resentRecord(String recordId);
	
	
	public DataPackage getNotReadByUserId(String userId,DataPackage dp);
	
	public int getNotReadCountByUserId(String userId);
}
