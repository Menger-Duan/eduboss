package com.eduboss.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.eduboss.common.SessionType;
import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobilePushMsgSession;





public interface MobilePushMsgSessionDao extends GenericDAO<MobilePushMsgSession, String> {



	/**
	 * 通过 moblieUserId 获取到 相应的 公告 session
	 * @param mobileUserId
	 * @return
	 */
	List<MobilePushMsgSession> getNoticeSessionsByMobileUserId(String mobileUserId);

	/**
	 * 通过 moblieUserId 获取到 相应的 提醒 session
	 * @param mobileUserId
	 * @return
	 */
	List<MobilePushMsgSession> getRemindSessionsByMobileUserId(String mobileUserId);

	/**
	 * 创建一个对话， 参与者是 mobileUser
	 * @param notice
	 * @param mobileUserId
	 * @return
	 */
	MobilePushMsgSession createOneSession(SessionType notice,
			String mobileUserId);

	/**
	 * 通过一个courseId 获取到相应的 session comments
	 * @param courseId
	 * @return
	 */
	List<MobilePushMsgSession> getCourseSessionsByCourseId(String courseId);

	/**
	 * 对某一节课查找到相应的 课程session, 有就返回 sessionId， 没有 就创建一个sessionID
	 * @param course
	 * @param courseId
	 * @param mobileUserId
	 * @return
	 */
	MobilePushMsgSession findOneSessionForCourse(SessionType course,
			String courseId, String mobileUserId);

	/**
	 * 判断一个 courseId 是不是已经 存在一个session
	 * @param courseId
	 * @return
	 */
	boolean hasSessionForCourseId(String courseId);

	/**
	 * 通过两个不同的 mobile user id  得到 相应的session ID, 有session就返回, 没有就创建session
	 * @param selectedMobileUserId
	 * @param fromMobileUserId
	 * @return
	 */
	MobilePushMsgSession findOneSessionByMobileUserIds(
			String selectedMobileUserId, String fromMobileUserId);

	/**
	 * 根据 不同的 mobileUserId 创建不同的session
	 * @param selectedMobileUserId
	 * @param fromMobileUserId
	 * @return
	 */
	MobilePushMsgSession createOneSessionForMobileUserIds(
			String selectedMobileUserId,
			String fromMobileUserId);

	/**
	 * 根据不同的 mobileUserId  判断是否存在 对应的sessionId
	 * @param selectedMobileUserId
	 * @param fromMobileUserId
	 * @return
	 */
	boolean hasConversationSessionForMobileUserIds(String selectedMobileUserId,
			String fromMobileUserId);

	/**
	 * 创建一个session， 并且加入不同的创建人 和 加入session 的 userList
	 * @param notice
	 * @param createMobileUserId
	 * @param arrayOfmobileUserId
	 * @return
	 */
	MobilePushMsgSession createOneSession(SessionType notice,
			String createMobileUserId, List<String> arrayOfmobileUserId);
	
}
