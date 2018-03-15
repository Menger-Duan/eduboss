package com.eduboss.service;

import java.util.List;
import java.util.Map;

import com.eduboss.common.SessionType;
import com.eduboss.common.SysMsgType;
import com.eduboss.domain.SystemNotice;

import org.springframework.web.multipart.MultipartFile;

import com.eduboss.common.StoredDataType;
import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobileUser;
import com.eduboss.domainVo.MobilePushMsgRecordVo;
import com.eduboss.domainVo.MobilePushMsgSessionVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.dto.Response;



/**
 * @author Administrator
 *
 */
public interface MobilePushMsgService {

	/**
	 * 通过 mobileUserID 获取到通告的 消息列表
	 * @param mobileUserId
	 * @return
	 */
	List<MobilePushMsgRecordVo> getNoticesByMobileUserId(String mobileUserId, int pageNo, int pageSize);
	
	/**
	 * 通过 mobileUserID 获取到 提醒 的 消息列表
	 * @param mobileUserId
	 * @return
	 */
	List<MobilePushMsgRecordVo> getRemindsByMobileUserId(String mobileUserId, int pageNo, int pageSize);
	
	/**
	 * 向 不同的客户 推送公告
	 * @param mobileUserId
	 * @param msgContent
	 * @param msgTitle
     * @return
	 */
	void pushNoticeToMobileUserId(String mobileUserId,
                                  String msgContent, String msgTitle);

	/**
	 * 向 不同的客户 推送通知
	 * @param mobileUserId
	 * @param msgContent
	 */
	void pushRemindToMobileUserId(String mobileUserId, String msgContent);

	/**
	 * 根据不同的 sessionId 获取到  不同课程的 record
	 * @param sessionId
	 * @return
	 */
	List<MobilePushMsgRecordVo> getMsgRecordsBySessionId(String sessionId,int pageNo, int pageSize);

	
	/**
	 * 向 course 生成 text message
	 * @param mobileUserId
	 * @param courseId
	 * @param msgContent
	 */
	void pushCourseTextMsgToUserIdForCourseId(String mobileUserId,
			String courseId, String msgContent);

	/**
	 * 向 course 生成 data message
	 * @param mobileUserId
	 * @param courseId
	 * @param dataFile
	 * @param datatype 
	 */
	void pushCourseDataMsgToUserIdForCourseId(String mobileUserId,
			String courseId, MultipartFile dataFile, StoredDataType datatype);

	/**
	 * 保存图片到 阿里云上
	 * @param myfile1
	 */
	void saveFileToRemoteServer(MultipartFile myfile1, String fileName);

	/**
	 * 通过课程ID 返回  session ID , 有就返回, 没有sessionId 就创建一个
	 * @param courseId
	 * @param courseType
	 * @param mobileUserId 
	 * @return
	 */
	MobilePushMsgSessionVo getSessionByCourseId(String courseId,
			String courseType, String mobileUserId);

	/**
	 * 向 session 生成 text message
	 * @param mobileUserId
	 * @param sessionId
	 * @param msgContent
	 */
	void sendTextMsgFromUserIdForCourseId(String mobileUserId, String sessionId,
			String msgContent);

	/**
	 * 向 session 生成 data message
	 * @param mobileUserId
	 * @param sessionId
	 * @param dataFile
	 * @param datatype
	 */
	void sendDataMsgFromUserIdForCourseId(String mobileUserId, String sessionId,
			MultipartFile dataFile, StoredDataType datatype);

	/**
	 * 通过两个不同的 mobile user id  得到 相应的session ID
	 * @param selectedMobileUserId
	 * @param fromMobileUserId
	 * @return
	 */
	MobilePushMsgSessionVo getSessionByMobileUserIds(
			String selectedMobileUserId, String fromMobileUserId);

	/**
	 * 向 不同的客户 推送公告
	 * @param arrayOfmobileUserId
	 * @param createMobileUserId
	 * @param msgContent
	 */
	void pushNoticeToMobileUserIds(List<String> arrayOfmobileUserId, String createMobileUserId , String msgContent);

	/**
	 * 对不同的 user 推送不同的消息， 需要建立不同的 mobile user 先
	 * @param arrayOfUserId
	 * @param createUserId
	 * @param msgContent
	 */
	void pushNoticeToUserIds(List<String> arrayOfUserId, String createUserId,
			String msgContent);

	/**
	 * 判断是否有需要发送的消息
	 * @return
	 */
	boolean hasNewMsg();

	/**
	 * 获取所有需要发送的消息
	 * @return
	 */
	List<MobilePushMsgRecordVo> getNeedSendRecords();

	/**
	 * 发送消息， 同时  更新消息的状态
	 * @param needSendRecord
	 */
	void pushMessage(MobilePushMsgRecordVo needSendRecord);

	/**
	 * 推送消息
	 * @param user
	 */
	void pushMsg(MobileUser user, String msgContent, String id, String type,String time, SysMsgType sysMsgType, String detailId, String title);

	/**
	 * 向  MobileUser 推送提醒消息
	 * @param arrayOfmobileUserId
	 * @param createMobileUserId
	 * @param msgContent
	 */
	void pushRemindToMobileUserIds(List<String> arrayOfmobileUserId,
			String createMobileUserId, String msgContent);

	/**
	 * 向  User 推送提醒消息
	 * @param arrayOfUserId
	 * @param createUserId
	 * @param msgContent
	 */
	void pushRemindToUserIds(List<String> arrayOfUserId, String createUserId,
			String msgContent);

	/**
	 * 
	 * @param arrayOfUserId
	 * @param arrayOfStuId
	 * @param createUserId
	 * @param msgContent
	 */
	void pushRemindToUserAndStudentIds(List<String> arrayOfUserId,
			List<String> arrayOfStuId, String createUserId, String msgContent);
	
	/**
	 * 根据recordId找到记录
	 * @param recordId
	 * @return
	 */
	MobilePushMsgRecordVo  getMobilePushMsgRecordById(String recordId);

    /**
     * 推送 notice 信息 给 不同的用户和学生
     * @param arrayOfUserId
     * @param arrayOfStuId
     * @param createUserId
     * @param msgContent
     */
    void pushNoticeToUserAndStudentIds(List<String> arrayOfUserId, List<String> arrayOfStuId, String createUserId, String msgContent);

    /**
     * 公用的 获取 消息的一个 方法， 有page 的方法 定义
     * 根据 不同的  mobileUserId, sessionType 还有 是 pageNo 和 pageSize 所有的信息
     * @param mobileUserId
     * @param sessionType
     * @param sessionId
     *@param pageIndex
     * @param pageCount
     * @param order 读取数据的 顺序
     * @return
     */
    List<MobilePushMsgRecordVo> getMsgByMobileUserIdAndSessionType(String mobileUserId, SessionType sessionType, String sessionId, int pageIndex, int pageCount, String order,String lastFetchTime);

    /**
     * 公用的 获取 最新消息纪录的一个 方法
     * @param mobileUserId
     * @param sessionType
     * @param sessionId
     * @param lastFetchTime
     * @return
     */
    List<MobilePushMsgRecordVo> getLastestMsgByMobileUserIdAndSessionType(String mobileUserId, SessionType sessionType, String sessionId, String lastFetchTime);

    /**
     *  根据 系统公告 生成 推送消息 并且推送到不同的人
     * @param systemNotice
     */
    void createPushMsgBySystemNotice(SystemNotice systemNotice);

    /**
     *
     * 判断是否有 一些需要 插入到 待发送 msg 的
     * @return
     */
    boolean hasNeedSendQueueMsg();

    /**
     * 获取 准备进入待发送序列的纪录
     * @return
     */
    List<MobilePushMsgSessionVo> getNeedSendQueueSession();

    /**
     * 推送 校区公告到 推送人列表里面
     * @param sessionVo
     */
    void pushSendQueue(MobilePushMsgSessionVo sessionVo);

	/**
	 * 看是否有 最新的 消息
	 * @param mobileUserId
	 * @param lastFetchTime
	 * @return
	 */
	boolean hasMoreMessageToMobileUserId(String mobileUserId,
			String lastFetchTime);
	
	/**
	 * 获取所有未读的消息概要
	 * @param mobileUserId
	 * @param lastFetchTime
	 * @return
	 */
	List<Map> getTotalCountOfUserUnReadRecord(String mobileUserId,
			String lastFetchTime);

	/**
	 * 根据 recordId 获取 notice 
	 * @param recordId
	 * @return
	 */
	SystemNoticeVo getNoticeByRecordId(String recordId);
	
	
	/**
	 *  向传入的用户id 在ids内的mobileUser推送指定消息
	 * 
	 */
	public void pushMsgToMobileByUserIds(String userIds,String type,String newsId,String msgContent,String time);
}
