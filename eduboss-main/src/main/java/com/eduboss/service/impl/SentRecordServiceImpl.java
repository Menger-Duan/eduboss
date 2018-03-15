package com.eduboss.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MsgNo;
import com.eduboss.dao.JdbcTemplateDao;
import com.eduboss.dao.SentRecordDao;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.SentRecord;
import com.eduboss.domain.SystemMessageManage;
import com.eduboss.domain.User;
import com.eduboss.domainVo.SentRecordVo;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.Response;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.SentRecordService;
import com.eduboss.service.UserService;
import com.eduboss.sms.MessageUtil;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.StringUtil;
import com.google.common.collect.Maps;

@Service
public class SentRecordServiceImpl implements SentRecordService {
	
	@Autowired
	private SentRecordDao sentRecordDao;
	
	@Autowired
	private MobileUserService mobileUserService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MobilePushMsgService mobilePushMsgService;
	
    @Autowired
    private JdbcTemplateDao jdbcTemplateDao;
	
	/**
	 * 新增发送系统信息记录
	 */
	public SentRecord saveSentRecord(SystemMessageManage sysMsg, String sendType,  String sendMsg,String sendDetailedMsg, String userId, User msgRecipient, String msgRecipientPhone, MsgNo msgNo) {
		SentRecord record = new SentRecord();
		record.setMsgNo(msgNo);
		record.setMsgName(sysMsg.getMsgName());
		record.setMsgContent(sendMsg);
		record.setDetailedInfromation(sendDetailedMsg);
		record.setMsgType(sysMsg.getMsgType());
		record.setMsgRecipient(msgRecipient);
		record.setMsgRecipientPhone(msgRecipientPhone);
		record.setSendType(sendType);
		record.setSentTime(DateTools.getCurrentDateTime());
		record.setCreateTime(DateTools.getCurrentDateTime());
		record.setCreateUserId(userId);
		record.setModifyUserId(userId);
		record.setModifyTime(DateTools.getCurrentDateTime());
		record.setIsReading("1");
		sentRecordDao.save(record);
		return record;
	}
	
	/**
	 * 新增或修改系统信息发送记录
	 */
	public void saveOrUpdateSentRecord(SentRecord record) {
		if (StringUtils.isNotBlank(record.getId())) {
			sentRecordDao.merge(record);
		} else {
			sentRecordDao.save(record);
		}
	}
	
	/**
	 * 查询系统信息发送记录
	 * @param sentRecordVo
	 * @param dp
	 * @return
	 */
	public DataPackage getSentRecordList(SentRecordVo sentRecordVo , DataPackage dp) {
		List<Criterion> list =new ArrayList<Criterion>();
		if (StringUtils.isNotBlank(sentRecordVo.getStartDate())) {
			list.add(Expression.ge("sentTime", sentRecordVo.getStartDate() + " 00:00:00 "));
		}
		if (StringUtils.isNotBlank(sentRecordVo.getEndDate())) {
			list.add(Expression.le("sentTime", sentRecordVo.getEndDate() + " 23:59:59 "));
		}
		if (null != sentRecordVo.getMsgNo()) {
			list.add(Expression.eq("msgNo", sentRecordVo.getMsgNo()));
		}
		if (StringUtils.isNotBlank(sentRecordVo.getMsgName())) {
			list.add(Expression.like("msgName", "%" + sentRecordVo.getMsgName() + "%"));
		}
		if (StringUtils.isNotBlank(sentRecordVo.getMsgTypeId())) {
			list.add(Expression.eq("msgType.id", sentRecordVo.getMsgTypeId()));
		}
		if (StringUtils.isNotBlank(sentRecordVo.getMsgRecipientName())) {
			list.add(Expression.like("msgRecipient.name", "%" + sentRecordVo.getMsgRecipientName() + "%"));
		}
		if (StringUtils.isNotBlank(sentRecordVo.getMsgRecipientPhone())) {
			list.add(Expression.like("msgRecipientPhone", "%" + sentRecordVo.getMsgRecipientPhone() + "%"));
		}
		if(StringUtils.isNotBlank(sentRecordVo.getSendType())){
			list.add(Expression.eq("sendType", sentRecordVo.getSendType()));
		}
		
		dp = sentRecordDao.findPageByCriteria(dp, HibernateUtils.prepareOrder(dp, "id", "asc"), list);
		List<SentRecord> msgList=(List<SentRecord>) dp.getDatas();
		List<SentRecordVo> sysMsgVoList=HibernateUtils.voListMapping(msgList, SentRecordVo.class);
		dp.setDatas(sysMsgVoList);
		return dp;
	}
	
	/**
	 * 阅读发送的系统信息
	 */
	@Override
	public Response readSentRecord(String recordIds) {
		Response response = new Response();
		String[] recordIdArr = recordIds.split(",");
		for (String recordId: recordIdArr) {
			SentRecord record = sentRecordDao.findById(recordId);
			if (record != null) {
				record.setIsReading("0");
				sentRecordDao.merge(record);
			}
		}
		return response;
	}
	
	/**
	 * 发送系统信息
	 */
	public Response sendRecord(SentRecordVo sentRecordVo) {
		Response response = new Response();
		sentRecordVo.setId(null);
		SentRecord sentRecord = HibernateUtils.voObjectMapping(sentRecordVo, SentRecord.class);
		User user = userService.getCurrentLoginUser();
		String currentUserId = "";
		if (user != null) {
			currentUserId = user.getUserId();
		} else {
			currentUserId = "112233";
		}
		sentRecord.setCreateTime(DateTools.getCurrentDateTime());
		sentRecord.setCreateUserId(currentUserId);
		sentRecord.setModifyTime(DateTools.getCurrentDateTime());
		sentRecord.setModifyUserId(currentUserId);
		sentRecord.setSentTime(DateTools.getCurrentDateTime());
		sentRecord.setIsReading("1");
		//佳学那边没手机号传过来
		if(sentRecordVo.getMsgRecipientPhone()==null || StringUtils.isBlank(sentRecordVo.getMsgRecipientPhone())){
			if(sentRecordVo.getMsgRecipientId()!=null && StringUtils.isNotBlank(sentRecordVo.getMsgRecipientId())){
				User teacher=userService.findUserById(sentRecordVo.getMsgRecipientId());
				if (teacher!=null){
					sentRecord.setMsgRecipientPhone(teacher.getContact());
				}
			}
			
		}
		if (StringUtil.isBlank(sentRecordVo.getMsgRecipientId())) {
			sentRecord.setMsgRecipient(null);
		}
		
		if (sentRecordVo.getSendType().indexOf("SMS") > -1) {
			sentRecord.setSendType("SMS");
			sentRecordDao.save(sentRecord);
			User msgRecipient = userService.findUserById(sentRecordVo.getMsgRecipientId());
			if (msgRecipient!=null && StringUtil.isNotBlank(msgRecipient.getContact())) {
				try {
					String msgContent = sentRecordVo.getMsgContent().replaceAll(" ", "\b");
					MessageUtil.readContentFromGet(msgRecipient.getContact(), msgContent);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		if (StringUtil.isNotBlank(sentRecordVo.getMsgRecipientId()) && sentRecordVo.getSendType().indexOf("SYS_MSG") > -1) {
			sentRecord.setSendType("SYS_MSG");
			sentRecordDao.save(sentRecord);
			MobileUser mobileUser = mobileUserService.findMobileUserByStaffId(sentRecordVo.getMsgRecipientId());
			if (mobileUser != null) {
				mobilePushMsgService.pushMsg(mobileUser, sentRecordVo.getMsgContent(), sentRecord.getId(), sentRecord.getPushMsgType().getValue(), DateTools.getCurrentDateTime(), sentRecord.getSysMsgType(), sentRecord.getDetailId(), sentRecord.getMsgType().getName());
			}
		}
		return response;
	}
	
	/**
	 * 重发系统信息
	 */
	public Response resentRecord(String recordId) {
		SentRecord record = sentRecordDao.findById(recordId);
		SentRecordVo sentRecordVo = HibernateUtils.voObjectMapping(record, SentRecordVo.class);
		return this.sendRecord(sentRecordVo);
	}
	
	/**
	 * APP获取用户的未读信息
	 */
	public DataPackage getNotReadByUserId(String userId,DataPackage dp){
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		String sql = "select * from sent_record where MSG_RECIPIENT= :userId and (IS_READING IS NULL or IS_READING='1') and SEND_TYPE='SYS_MSG' ORDER BY CREATE_TIME DESC";
		dp = sentRecordDao.findPageBySql(sql, dp, true, params);
		List<SentRecord> msgList = (List<SentRecord>) dp.getDatas();
		if (msgList != null && !msgList.isEmpty()) {
		    List<SentRecordVo> sysMsgVoList = HibernateUtils.voListMapping(msgList, SentRecordVo.class, "noMsgRecipient");
		    dp.setDatas(sysMsgVoList);
		}
		return dp;
	}
	
	/**
	 * APP获取用户为读信息条数
	 * 
	 */
	public int getNotReadCountByUserId(String userId){
		String sql="select COUNT(1) from sent_record where MSG_RECIPIENT= :userId and (IS_READING IS NULL or IS_READING='1') and SEND_TYPE='SYS_MSG' ORDER BY CREATE_TIME DESC";
		int count=0;
		Map<String, Object> params = Maps.newHashMap();
		params.put("userId", userId);
		count=sentRecordDao.findCountSql(sql,params);
		return count;
	}
	
}
