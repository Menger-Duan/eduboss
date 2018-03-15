package com.eduboss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MsgStatus;
import com.eduboss.dao.MobilePushMsgUserRecordDao;
import com.eduboss.domain.MobilePushMsgUserRecord;
import com.eduboss.service.MobilePushMsgUserRecordService;
import com.eduboss.utils.DateTools;

@Service("com.eduboss.service.MobilePushMsgUserRecordService")
public class MobilePushMsgUserRecordServiceImpl implements MobilePushMsgUserRecordService {
	
	@Autowired
	private MobilePushMsgUserRecordDao mobilePushMsgUserRecordDao;

	@Override
	public void updateUserRecordStatus(String mobileUserId, String sessionId,
			String recordId,String sessionType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder(" update MOBILE_PUSH_MSG_USER_RECORDS set STATUS='"+MsgStatus.COMPLETE+"',READ_TIME='"+DateTools.getCurrentDate()+"' where 1=1 ");
		if(StringUtils.isNotEmpty(mobileUserId)){
			sql.append(" and MOBILE_USER_ID = :mobileUserId ");
			params.put("mobileUserId", mobileUserId);
		}
		if(StringUtils.isNotEmpty(recordId)){
			sql.append(" and RECORD_ID = :recordId ");
			params.put("recordId", recordId);
		}
		if(StringUtils.isNotEmpty(sessionId)){
			sql.append(" and SESSION_ID = :sessionId ");
			params.put("sessionId", sessionId);
		}
		if(StringUtils.isNotEmpty(sessionType)){
			sql.append(" and SESSION_TYPE = :sessionType ");
			params.put("sessionType", sessionType);
		}
		mobilePushMsgUserRecordDao.excuteSql(sql.toString(), params);
	}

	@Override
	public String [] getUserRecordCount(String mobileUserId, String sessionId,String sessionType) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder(" from MobilePushMsgUserRecord where 1=1 and status='"+MsgStatus.NEW+"'");
		if(StringUtils.isNotEmpty(mobileUserId)){
			sql.append(" and mobileUser.id = :mobileUserId ");
			params.put("mobileUserId", mobileUserId);
		}
		if(StringUtils.isNotEmpty(sessionId)){
			sql.append(" and session.id = :sessionId ");
			params.put("sessionId", sessionId);
		}
		if(StringUtils.isNotEmpty(sessionType)){
			sql.append(" and sessionType = :sessionType ");
			params.put("sessionType", sessionType);
		}
		String [] retArry=new String[3];
		List<MobilePushMsgUserRecord> recordList = mobilePushMsgUserRecordDao.findAllByHQL(sql.toString(), params);
		if(recordList.size()>0){
			MobilePushMsgUserRecord fistRecord=recordList.get(0);
			retArry[0]=String.valueOf(recordList.size());
			retArry[1]=fistRecord.getRecord().getMsgContent();
			retArry[2]=fistRecord.getRecord().getCreateTime();
			return retArry;
		}else{
			return null;
		}
	}
	
}
