package com.eduboss.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eduboss.common.MessageDeliverType;
import com.eduboss.dao.MessageDeliverRecordDao;
import com.eduboss.dao.MessageRecordDao;
import com.eduboss.dao.OrganizationDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.MessageDeliverRecord;
import com.eduboss.domain.MessageDeliverRecordId;
import com.eduboss.domain.MessageRecord;
import com.eduboss.domain.User;
import com.eduboss.dto.DataPackage;
import com.eduboss.dto.MessageVo;
import com.eduboss.service.MessageRecordService;

@Service
public class MessageRecordServiceImpl implements MessageRecordService{
	
	@Autowired
	MessageRecordDao messageRecordDao;
	
	@Autowired
	MessageDeliverRecordDao messageDeliverRecordDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	private OrganizationDao organizationDao;

	@Override
	public DataPackage getMessageRecordList(MessageRecord messageRecord, DataPackage dp,MessageVo messageVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from MessageRecord mr ";
		String hqlWhere="";
		if(messageRecord.getMessageType()!=null) {
			hqlWhere+=" and mr.messageType = :messageType ";
			params.put("messageType", messageRecord.getMessageType());
		}
		if(StringUtils.isNotEmpty(messageRecord.getTitle())) {
			hqlWhere+=" and mr.title like :title ";
			params.put("title", "%" + messageRecord.getTitle() + "%");
		}
		if(StringUtils.isNotEmpty(messageRecord.getContent())) {
			hqlWhere+=" and mr.content like :content ";
			params.put("content", "%" + messageRecord.getContent() + "%");
		}
		if(StringUtils.isNotEmpty(messageVo.getStartDate())) {
			hqlWhere+=" and mr.sendTime >= :startDate ";
			params.put("startDate", messageVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(messageVo.getEndDate())) {
			hqlWhere+=" and mr.sendTime <= :endDate ";
			params.put("endDate", messageVo.getEndDate());
		}
		
		if(StringUtils.isNotEmpty(messageVo.getSendName())){
			hqlWhere+=" and mr.sendUserId in (select userId  from User u where u.name like :sendName )";
			params.put("sendName", "%" + messageVo.getSendName() + "%");
		}
		
		if(StringUtils.isNotEmpty(messageVo.getDeliverName())){
			hqlWhere+=" and  mr.messageId in (select messageId from MessageDeliverRecord where " +
					"userId in(select userId from User where name like :deliverName) " +
					") ";
			params.put("deliverName", "%" + messageVo.getDeliverName() + "%");
		}
		
		if(!"".equals(hqlWhere)){
			hqlWhere="where "+hqlWhere.substring(4);
		}
		hql+=hqlWhere;
		
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by mr."+dp.getSidx()+" "+dp.getSord();
		} 
		
		dp=messageRecordDao.findPageByHQL(hql, dp, true, params);
		
		List<MessageRecord> list= (List<MessageRecord>) dp.getDatas();
		for(MessageRecord message:list){
			if(StringUtils.isNotEmpty(message.getSendUserId()))
				message.setSendUserName(userDao.findById(message.getSendUserId()).getName());
			if(MessageDeliverType.SINGLE.equals(message.getDeliverType()) && StringUtils.isNotEmpty(message.getDeliverTagetId())){
				User user=userDao.findById(message.getDeliverTagetId());
				if(user!=null)
					message.setDeliverTagetName(user.getName());
			}
			if(MessageDeliverType.LIST_USER.equals(message.getDeliverType()) && StringUtils.isNotEmpty(message.getDeliverTagetId())){
				String tagName="";
				for(String id:message.getDeliverTagetId().split(","))
					tagName+=","+userDao.findById(message.getDeliverTagetId()).getName();
				message.setDeliverTagetName(tagName.substring(1));
			}
			if((MessageDeliverType.BRENCH.equals(message.getDeliverType()) 
					|| MessageDeliverType.CAMPUS.equals(message.getDeliverType()) 
					|| MessageDeliverType.DEPARTMENT.equals(message.getDeliverType()) 
					)
					&& StringUtils.isNotEmpty(message.getDeliverTagetId())){
				String tagName="";
				for(String id:message.getDeliverTagetId().split(","))
					tagName+=","+organizationDao.findById(id).getName();
				message.setDeliverTagetName(tagName.substring(1));
			}
			if(MessageDeliverType.ALL.equals(message.getDeliverType()) )
				message.setDeliverTagetName("所有人");
		}
		return dp;
	}

	@Override
	public MessageRecord findMessageRecordById(String id) {
		return messageRecordDao.findById(id);
	}

	@Override
	public void deleteMessageRecord(MessageRecord messageRecord) {
		messageRecordDao.delete(messageRecord);
	}

	@Override
	public void saveOrUpdateMessageRecord(MessageRecord messageRecord) {
		messageRecordDao.save(messageRecord);
	}

	@Override
	public DataPackage getMessageDeliverRecordList(MessageDeliverRecord messageDeliverRecord, DataPackage dp,MessageVo messageVo) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql=" from MessageDeliverRecord mdr ";
		String hqlWhere="";
		if(StringUtils.isNotEmpty(messageVo.getTitle())) {
			hqlWhere+=" and mdr.messageRecord.title like :title ";
			params.put("title", "%" + messageVo.getTitle() + "%");
		}
		if(StringUtils.isNotEmpty(messageVo.getContent())) {
			hqlWhere+=" and mdr.messageRecord.content like :content ";
			params.put("content", "%" + messageVo.getContent() + "%");
		}
		if(StringUtils.isNotEmpty(messageVo.getSendName())){
			hqlWhere+=" and mdr.messageRecord.sendUserId in (select userId from User where name like :sendName )";
			params.put("sendName", "%" + messageVo.getSendName() + "%");
		}
		
		if(StringUtils.isNotEmpty(messageVo.getStartDate())) {
			hqlWhere+=" and mdr.sendTime >= :startDate ";
			params.put("startDate", messageVo.getStartDate());
		}
		if(StringUtils.isNotEmpty(messageVo.getEndDate())) {
			hqlWhere+=" and mdr.sendTime <= :dndDate ";
			params.put("dndDate", messageVo.getEndDate());
		}
		
		
		if(StringUtils.isNotEmpty(messageVo.getDeliverName())) {
			hqlWhere+=" and mdr.user.name like :deliverName ";
			params.put("deliverName", "%" + messageVo.getDeliverName() + "%");
		}

		
		if(!"".equals(hqlWhere)){
			hqlWhere="where "+hqlWhere.substring(4);
		}
		hql+=hqlWhere;
		if (StringUtils.isNotBlank(dp.getSord())
				&& StringUtils.isNotBlank(dp.getSidx())) {
			hql+=" order by mdr."+dp.getSidx()+" "+dp.getSord();
		} 
		dp=messageDeliverRecordDao.findPageByHQL(hql, dp, true, params);
		
		List<MessageDeliverRecord> list= (List<MessageDeliverRecord>) dp.getDatas();
		for(MessageDeliverRecord message:list){
			if(StringUtils.isNotEmpty(message.getMessageRecord().getSendUserId()))
				message.getMessageRecord().setSendUserName(userDao.findById(message.getMessageRecord().getSendUserId()).getName());
			if(message.getUser()!=null)
				message.setReadUserName(userDao.findById(message.getUser().getUserId()).getName());
		}
		return dp;
	}
	
	public List<MessageDeliverRecord> getMessageDeliverrecordByUserId(String userid){
		
		return messageDeliverRecordDao.findByCriteria(Expression.eq("userId", userid));
	}
	
	@Override
	public void deleteMessageDeliverRecord(MessageDeliverRecord messageDeliverRecord) {
		messageDeliverRecordDao.delete(messageDeliverRecord);
	}

	@Override
	public void saveOrUpdateMessageDeliverRecord(MessageDeliverRecord messageDeliverRecord) {
		messageDeliverRecordDao.save(messageDeliverRecord);
	}

	@Override
	public MessageDeliverRecord findMessageDeliverRecordById(MessageDeliverRecordId id) {
		return messageDeliverRecordDao.findById(id);
	}
}
