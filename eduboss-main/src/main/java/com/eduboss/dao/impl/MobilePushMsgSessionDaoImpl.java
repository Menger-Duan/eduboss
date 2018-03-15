package com.eduboss.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.eduboss.common.MsgStatus;
import com.eduboss.common.SessionType;
import com.eduboss.dao.MobilePushMsgRecordDao;
import com.eduboss.dao.MobilePushMsgSessionDao;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;
import com.eduboss.utils.DateTools;
import com.google.common.collect.Maps;




@Repository("MobilePushMsgSessionDao")

public class MobilePushMsgSessionDaoImpl extends GenericDaoImpl<MobilePushMsgSession, String> implements MobilePushMsgSessionDao {

	private static final Logger log = LoggerFactory.getLogger(MobilePushMsgSessionDaoImpl.class);
	
	@Autowired
	private MobileUserDao mobileUserDao ;
	
	@Override
	public List<MobilePushMsgSession> getNoticeSessionsByMobileUserId(
            String mobileUserId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.NOTICE);
		params.put("mobileUserId", mobileUserId);
		hql.append("from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and createMobileUser.id in ( select user.id from session.mobileUsers user where user.id  = :mobileUserId " );
		return super.findAllByHQL(hql.toString(),params);
	}

	@Override
	public List<MobilePushMsgSession> getRemindSessionsByMobileUserId(
            String mobileUserId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.REMIND);
		params.put("mobileUserId", mobileUserId);
		hql.append("from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and createMobileUser.id in ( select user.id from session.mobileUsers user where user.id  = :mobileUserId " );
		return super.findAllByHQL(hql.toString(),params);
	}

	@Override
	public MobilePushMsgSession createOneSession(SessionType sessionType,
			String mobileUserId) {
		MobilePushMsgSession session =  new MobilePushMsgSession();
		session.setSessionType(sessionType);
		MobileUser user = mobileUserDao.findById(mobileUserId);
		session.getMobileUsers().add(user);
		session.setCreateMobileUser(user);
		session.setCreateTime(DateTools.getCurrentDateTime());
		this.save(session);
		this.flush();
		return session;
	}

	@Override
	public List<MobilePushMsgSession> getCourseSessionsByCourseId(
			String courseId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.COURSE);
		params.put("courseId", courseId);
		hql.append("from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and session.remark = :courseId " );
		return super.findAllByHQL(hql.toString(),params);
	}

	@Override
	public MobilePushMsgSession findOneSessionForCourse(SessionType courseType,
			String courseId, String mobileUserId) {
		boolean  hasSession= this.hasSessionForCourseId(courseId);
		MobilePushMsgSession processedSession = null; 	
		if(hasSession == true) { // 存在session
			processedSession = this.findSessionByCourseId(courseId); 
		} else { //  新建一个session
			processedSession =  this.createOneSession(courseType, mobileUserId);
			processedSession.setRemark(courseId);
			this.flush();
		}
		return processedSession;
	}
	
	
	/**
	 * 用于private 使用获取 courseId 相关联的 sessionId； 如果对外 统一使用 findOneSessionForCourse
	 * @param courseId
	 * @return
	 */
	private MobilePushMsgSession findSessionByCourseId(String courseId) {
		List<MobilePushMsgSession> sessions = this.getCourseSessionsByCourseId(courseId);
		if(sessions.size()>0){
			return sessions.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean hasSessionForCourseId(String courseId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.COURSE);
		params.put("courseId", courseId);
		hql.append("select count(*) from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and session.remark = :courseId " );
		int count = super.findCountHql(hql.toString(),params);
		return count>0;
	}

	@Override
	public MobilePushMsgSession findOneSessionByMobileUserIds(
			String selectedMobileUserId,
			String fromMobileUserId) {
		boolean  hasSession= this.hasConversationSessionForMobileUserIds(selectedMobileUserId, fromMobileUserId);
		MobilePushMsgSession processedSession = null; 	
		if(hasSession == true) { // 存在session
			processedSession = this.findSessionByMobileUserIds(selectedMobileUserId, fromMobileUserId); 
		} else { //  新建一个session
			processedSession =  this.createOneSessionForMobileUserIds(selectedMobileUserId, fromMobileUserId);
			processedSession.setRemark("2");
			this.flush();
		}
		return processedSession;
	}

	@Override
	public MobilePushMsgSession createOneSessionForMobileUserIds(
			String selectedMobileUserId,
			String fromMobileUserId) {
		MobilePushMsgSession session = this.createOneSession(SessionType.CONVERSATION, fromMobileUserId);
		session.getMobileUsers().add(mobileUserDao.findById(selectedMobileUserId));
		return session;
	}

	private MobilePushMsgSession findSessionByMobileUserIds(
			String selectedMobileUserId, String fromMobileUserId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.CONVERSATION);
		params.put("selectedMobileUserId", selectedMobileUserId);
		params.put("fromMobileUserId", fromMobileUserId);
		hql.append("from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and session.remark = '" )
			.append("2" ).append("'")
			.append(" and exists ( from session.mobileUsers user where user.id  = :selectedMobileUserId " )
			.append(" and exists ( from session.mobileUsers user where user.id  = :fromMobileUserId " );
		List<MobilePushMsgSession> sessionList = super.findAllByHQL(hql.toString(),params);
		if(sessionList.size()>0){
			return sessionList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public boolean hasConversationSessionForMobileUserIds(String selectedMobileUserId,
			String fromMobileUserId) {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> params = Maps.newHashMap();
		params.put("sessionType", SessionType.CONVERSATION);
		params.put("selectedMobileUserId", selectedMobileUserId);
		params.put("fromMobileUserId", fromMobileUserId);
		hql.append("select count(*) from MobilePushMsgSession as session where session.sessionType = :sessionType ")
			.append(" and session.remark = '" )
			.append("2" ).append("'")
			.append(" and exists ( from session.mobileUsers user where user.id  = :selectedMobileUserId " )
			.append(" and exists ( from session.mobileUsers user where user.id  = :fromMobileUserId " );
		int count = super.findCountHql(hql.toString(),params);
		return count>0;
	}

	@Override
	public MobilePushMsgSession createOneSession(SessionType sessionType,
			String createMobileUserId, List<String> arrayOfmobileUserId) {
		MobilePushMsgSession session =  new MobilePushMsgSession();
		session.setSessionType(sessionType);
		MobileUser createUser = mobileUserDao.findById(createMobileUserId);
		List<MobileUser> arrayOfMobileUser =  new ArrayList<MobileUser>();
		// 循环读取每一个 mobileUserId 并且建立 一个 session
		for(String mobileUserId : arrayOfmobileUserId  ) {
			MobileUser mobileUser = mobileUserDao.findById(mobileUserId);
			arrayOfMobileUser.add(mobileUser);
		}
		session.setMsgStatus(MsgStatus.NEW);
		session.getMobileUsers().addAll(arrayOfMobileUser);
		session.setCreateMobileUser(createUser);
		session.setCreateTime(DateTools.getCurrentDateTime());
		this.save(session);
		this.flush();
		return session;
	}

	
}
