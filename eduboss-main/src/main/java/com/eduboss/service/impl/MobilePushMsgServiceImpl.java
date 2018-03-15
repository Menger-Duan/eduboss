package com.eduboss.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.yun.channel.auth.ChannelKeyPair;
import com.baidu.yun.channel.client.BaiduChannelClient;
import com.baidu.yun.channel.exception.ChannelClientException;
import com.baidu.yun.channel.exception.ChannelServerException;
import com.baidu.yun.channel.model.PushUnicastMessageRequest;
import com.baidu.yun.channel.model.PushUnicastMessageResponse;
import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.eduboss.common.MobileType;
import com.eduboss.common.MobileUserType;
import com.eduboss.common.MsgStatus;
import com.eduboss.common.PushMsgType;
import com.eduboss.common.SessionType;
import com.eduboss.common.StoredDataType;
import com.eduboss.common.SysMsgType;
import com.eduboss.dao.MobilePushMsgRecordDao;
import com.eduboss.dao.MobilePushMsgSessionDao;
import com.eduboss.dao.MobilePushMsgUserRecordDao;
import com.eduboss.dao.MobileUserDao;
import com.eduboss.dao.StudentDao;
import com.eduboss.dao.SystemNoticeDao;
import com.eduboss.dao.UserDao;
import com.eduboss.domain.MobilePushMsgRecord;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Student;
import com.eduboss.domain.SystemNotice;
import com.eduboss.domain.User;
import com.eduboss.domainVo.MobilePushMsgRecordVo;
import com.eduboss.domainVo.MobilePushMsgSessionVo;
import com.eduboss.domainVo.SystemNoticeVo;
import com.eduboss.exception.ApplicationException;
import com.eduboss.service.MobilePushMsgService;
import com.eduboss.service.MobileUserService;
import com.eduboss.service.UserService;
import com.eduboss.utils.AliyunOSSUtils;
import com.eduboss.utils.DateTools;
import com.eduboss.utils.HibernateUtils;
import com.eduboss.utils.MessagePushingUtil;
import com.eduboss.utils.StringUtil;

@Service("com.eduboss.service.MobilePushMsgService")
public class MobilePushMsgServiceImpl implements MobilePushMsgService {
	
	Logger log = Logger.getLogger(MobilePushMsgService.class);
    
	@Autowired
	private MobileUserDao mobileUserDao;
	
	@Autowired
	private MobilePushMsgRecordDao msgRecordDao ;
	
	@Autowired
	private MobilePushMsgSessionDao sessionDao ;
	
	@Autowired
	private MobileUserService mobileUserService; 
	
	@Autowired
	private MobilePushMsgUserRecordDao msgUserRecordDao ;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private UserDao userDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemNoticeDao systemNoticeDao;
    
    // 为IOS的百度推送转换PushMsgType
    private String getPushMsgType(String typeValue) {
    	switch (typeValue) {
			case "SYSTEM_NOTICE":
				return "SN";
			case "APP_VERSION":
				return "AV";
			case "SYSTEM_EMAIL":
				return "SE";
			case "SYSTEM_MESSAGE":
				return "SM";
			case "SYSTEM_TRAINING":
				return "ST";
			default:
				return "SN";
		}
    }
    
    // 为IOS的百度推送转换SysMsgType
    private String getSysMsgType(String typeValue) {
    	switch (typeValue) {
			case "ONE_ON_ONE_COURSE":
				return "OOO";
			case "MINI_CLASS":
				return "MC";
			case "OTM_CLASS":
				return "OC";
			case "ONLINE_TASK":
				return "OT";
			case "ONLINE_EXAM":
				return "OE";
			case "OTHERS":
				return "OTH";
			default:
				return "OTH";
    	}
    }
    
    @Override
	public List<MobilePushMsgRecordVo> getNoticesByMobileUserId(String mobileUserId, int pageNo, int pageSize) {
		
		List<MobilePushMsgSession> sessionList =   sessionDao.getNoticeSessionsByMobileUserId(mobileUserId);
		List<MobilePushMsgRecord> recordList =  msgRecordDao.getRecordBySessions(sessionList, pageNo, pageSize);  
		List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
		return recordVos;
	}
	
	@Override
	public List<MobilePushMsgRecordVo> getRemindsByMobileUserId(String mobileUserId, int pageNo, int pageSize) {
		List<MobilePushMsgSession> sessionList =   sessionDao.getRemindSessionsByMobileUserId(mobileUserId);
		List<MobilePushMsgRecord> recordList =  msgRecordDao.getRecordBySessions(sessionList, pageNo, pageSize);  
		List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
		return recordVos;
	}
	
	@Override
	public void pushNoticeToMobileUserId(String mobileUserId,
                                         String msgContent, String msgTitle) {
		
		MobilePushMsgSession session = sessionDao.createOneSession(SessionType.NOTICE, mobileUserId);

		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		msg.setMsgContent(msgContent);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
	}

	@Override
	public void pushRemindToMobileUserId(String mobileUserId, String msgContent) {
		MobilePushMsgSession session = sessionDao.createOneSession(SessionType.REMIND, mobileUserId);

		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		msg.setMsgContent(msgContent);
		msg.setMsgStatus(MsgStatus.NEW);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
		
	}

	@Override
	public List<MobilePushMsgRecordVo> getMsgRecordsBySessionId(String sessionId, int pageNo, int pageSize) {
		MobilePushMsgSession session = sessionDao.findById(sessionId);
		if(session!= null){
			List<MobilePushMsgSession> sessionList = new ArrayList<MobilePushMsgSession>();
			sessionList.add(session);
			List<MobilePushMsgRecord> recordList =  msgRecordDao.getRecordBySessions(sessionList, pageNo, pageSize);
			List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
			for(MobilePushMsgRecordVo recordVo : recordVos) {
				String name = this.getUserNameByMobileUserIdAndType(recordVo.getSenderId(), recordVo.getSenderType() );
				recordVo.setSenderName(name);
			}
			
			return recordVos;
		} else {
			return new ArrayList();
		}
		
		
		
//		List<MobilePushMsgSession> sessionList =   sessionDao.getCourseSessionsByCourseId(courseId);
//		if(sessionList.size() > 0 ) {
//			List<MobilePushMsgRecord> recordList =  msgRecordDao.getRecordBySessions(sessionList);  
//			List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
//			return recordVos;
//		} else {
//			return null;
//		}
	}

    /**
     * * 根据不同 userID 和 type 可以 获取到不同的userName， 因为有 老师和学生的存在
     * @param mobileUserId
     * @param userType
     * @return
     */
	private String getUserNameByMobileUserIdAndType(String mobileUserId,
			MobileUserType userType) {
		
		if(MobileUserType.STUDENT_USER.equals(userType)) {
			MobileUser mobileUser = mobileUserDao.findById(mobileUserId);
			Student student = studentDao.findById(mobileUser.getUserId());
			return student==null?"":student.getName(); 
		}else{
			MobileUser mobileUser = mobileUserDao.findById(mobileUserId);
			User user = userDao.findById(mobileUser.getUserId());
			return user==null?"":user.getName();
		} 
		
	}

	@Override
	public void pushCourseTextMsgToUserIdForCourseId(String mobileUserId,
			String courseId, String msgContent) {
		
		MobilePushMsgSession session = sessionDao.findOneSessionForCourse(SessionType.COURSE, courseId, mobileUserId);

		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		msg.setMsgContent(msgContent);
		msg.setDataType(StoredDataType.TEXT);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
	}

	@Override
	public void pushCourseDataMsgToUserIdForCourseId(String mobileUserId,
			String courseId, MultipartFile dataFile, StoredDataType datatype) {
		
		MobilePushMsgSession session = sessionDao.findOneSessionForCourse(SessionType.COURSE, courseId, mobileUserId);
		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		String fileName = UUID.randomUUID().toString();
		msg.setMsgContent(fileName);
		this.saveFileToRemoteServer(dataFile, fileName);
		msg.setDataType(datatype);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
		
	}
	
	@Override
	public void saveFileToRemoteServer(MultipartFile myfile1, String fileName)  {
		try {
//			boolean isUploadFinish = FileUtil.readInputStreamToFile(myfile1.getInputStream(), myfile1.getOriginalFilename());
			AliyunOSSUtils.put(fileName, myfile1.getInputStream(), myfile1.getSize());
		} catch (IOException e) {
			e.printStackTrace();
			throw new ApplicationException("出错了");
		}
	}

	@Override
	public MobilePushMsgSessionVo getSessionByCourseId(String courseId, String courseType, String mobileUserId) {
		MobilePushMsgSession session = sessionDao.findOneSessionForCourse(SessionType.COURSE, courseId, mobileUserId);
		MobilePushMsgSessionVo sessionVo = HibernateUtils.voObjectMapping(session, MobilePushMsgSessionVo.class);
		return sessionVo;
	}

	@Override
	public void sendTextMsgFromUserIdForCourseId(String mobileUserId,
			String sessionId, String msgContent) {
		MobilePushMsgSession session =  sessionDao.findById(sessionId);
		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		msg.setMsgStatus(MsgStatus.NEW);
		msg.setMsgContent(msgContent);
		msg.setDataType(StoredDataType.TEXT);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);	
	}

	@Override
	public void sendDataMsgFromUserIdForCourseId(String mobileUserId,
			String sessionId, MultipartFile dataFile, StoredDataType datatype) {
		MobilePushMsgSession session =  sessionDao.findById(sessionId);
		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(mobileUserId);
		msg.setSender(user);
		msg.setMsgStatus(MsgStatus.NEW);
		String fileName = UUID.randomUUID().toString();
		msg.setMsgContent(fileName);
		this.saveFileToRemoteServer(dataFile, fileName);
		msg.setDataType(datatype);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
		
	}

	@Override
	public MobilePushMsgSessionVo getSessionByMobileUserIds(
			String selectedMobileUserId, String fromMobileUserId) {
		MobilePushMsgSession session = sessionDao.findOneSessionByMobileUserIds(selectedMobileUserId, fromMobileUserId);
		MobilePushMsgSessionVo sessionVo = HibernateUtils.voObjectMapping(session, MobilePushMsgSessionVo.class);
		StringBuffer joinerNamesSB = new StringBuffer(), joinerIdsSB = new StringBuffer();
				
		for (MobileUser mUser : session.getMobileUsers()) {
			joinerNamesSB.append(
					this.getUserNameByMobileUserIdAndType(mUser.getId(),
							mUser.getUserType())).append("###");
			joinerIdsSB.append(mUser.getId()).append("###");
		}
		sessionVo.setJoinerNames(joinerNamesSB.toString());
		sessionVo.setJoinerIds(joinerIdsSB.toString());
		return sessionVo;
	}

	@Override
	public void pushNoticeToMobileUserIds(List<String> arrayOfmobileUserId, String createMobileUserId, 
			String msgContent) {
		pushNormalMsgToMobileUserIds(arrayOfmobileUserId, createMobileUserId,
				msgContent, SessionType.NOTICE);
	}

	private void pushNormalMsgToMobileUserIds(List<String> arrayOfmobileUserId,
			String createMobileUserId, String msgContent, SessionType sessionType) {
		MobilePushMsgSession session = sessionDao.createOneSession(sessionType, createMobileUserId, arrayOfmobileUserId );
		MobilePushMsgRecord msg  = new MobilePushMsgRecord();
		MobileUser user = mobileUserDao.findById(createMobileUserId);
		msg.setMsgStatus(MsgStatus.NEW);
		msg.setSender(user);
		msg.setMsgContent(msgContent);
		msg.setSession(session);
		msg.setCreateTime(DateTools.getCurrentDateTime());
		msgRecordDao.save(msg);
	}
	
	@Override
	public void pushNoticeToUserIds(List<String> arrayOfUserId, String createUserId, 
			String msgContent) {
		List<String> arrayOfmobileUserId = new ArrayList<String>();
		String createMobileUserId = mobileUserService.findMobileUserByStaffId(createUserId).getId();
		for(String userId : arrayOfUserId) {
			arrayOfmobileUserId.add(mobileUserService.findMobileUserByStaffId(userId).getId());
		}
		this.pushNoticeToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
	}

	@Override
	public boolean hasNewMsg() {
		StringBuffer hql = new StringBuffer(); 
		hql.append("select count(*) from MobilePushMsgRecord as record ").append("where ")
		.append(" record.msgStatus = '").append(MsgStatus.NEW).append("'");
		int count = msgRecordDao.findCountHql(hql.toString(), new HashMap<String, Object>());
		return count>0;
	}

	@Override
	public List<MobilePushMsgRecordVo> getNeedSendRecords() {
		StringBuffer hql = new StringBuffer(); 
		// 所有的消息 都是从 这里发出
		hql.append("from MobilePushMsgRecord as record ").append("where ")
			.append(" record.msgStatus = '").append(MsgStatus.NEW).append("'");
		List<MobilePushMsgRecordVo> listOfRecordVo = HibernateUtils.voListMapping(msgRecordDao.findAllByHQL(hql.toString(), new HashMap<String, Object>()), MobilePushMsgRecordVo.class);
		return listOfRecordVo;
	}

	@Override
	public void pushMessage(MobilePushMsgRecordVo needSendRecord) {
		MobilePushMsgRecord record = msgRecordDao.findById(needSendRecord.getId());
		if(record!=null){
			// 读出 不同 user 的channel Id 和  user id
			if(record.getSession()!=null){
				Set<MobileUser> targetUsers = record.getSession().getMobileUsers();
				//如果是聊天不必推送给自己
				String sender="";
				if(record.getSession().getSessionType()!=null && record.getSession().getSessionType()==SessionType.CONVERSATION && record.getSender()!=null){
					sender=record.getSender().getId();
				}
				for(MobileUser user: targetUsers) {
					//如果是聊天不必推送给自己
					if(!user.getId().equals(sender)){
						this.pushMsg(user, record.getMsgContent(),"","","", null, "", "星火管理端");
					}
		//			MobilePushMsgUserRecord mp=new MobilePushMsgUserRecord();//  推送记录表，add by Yao  用于手机端推送记录
		//			mp.setMobileUser(user);
		//			mp.setPushTime(DateTools.getCurrentDateTime());
		//			mp.setRecord(record);
		//			mp.setSession(record.getSession());
		//			mp.setStatus(MsgStatus.NEW);
		//			mp.setSessionType(record.getSession().getSessionType());
		//			msgUserRecordDao.save(mp);
		
				}
				
			}
			
			// 同时更新 record 的记录
			record.setMsgStatus(MsgStatus.COMPLETE);
		}
		
	}
	
	@Override
	public void pushMsg(MobileUser user, String msgContent, String id, String type,String time, SysMsgType sysMsgType, String detailId, String title) {
		//System.out.println(user.getMobileType().name() + user.getUserType().name() + user.getUserId());
		if(StringUtils.isNotBlank(user.getPlatFormChannelId()) && StringUtils.isNotBlank(user.getPlatFormUserId()) && user.getUserType()!=null) {
			String apiKey = null; String secretKey = null;
            int env = MessagePushingUtil.ENV.equals("dev")? 1 : 2;
            int mobileType = 1;
			if(user.getMobileType().equals(MobileType.IOS) && user.getUserType().equals(MobileUserType.STAFF_USER)){  // IOS 老师端
				apiKey = MessagePushingUtil.APIKEY_IOS_TEACHERAPP;
				secretKey = MessagePushingUtil.SECRETKEY_IOS_TEACHERAPP;
				mobileType = 4;
			} else if(user.getMobileType().equals(MobileType.IOS) && user.getUserType().equals(MobileUserType.STUDENT_USER)){ // IOS 学生端
				apiKey = MessagePushingUtil.APIKEY_IOS_STUAPP;
				secretKey = MessagePushingUtil.SECRETKEY_IOS_STUAPP;
				mobileType = 4;
			}else if(user.getMobileType().equals(MobileType.IOS) && user.getUserType().equals(MobileUserType.MANAGE)){ // IOS 管理端
				apiKey = MessagePushingUtil.APIKEY_IOS_MANAGEAPP;
				secretKey = MessagePushingUtil.SECRETKEY_IOS_MANAGEAPP;
				mobileType = 4;
			} else if(user.getMobileType().equals(MobileType.ANDROID) && user.getUserType().equals(MobileUserType.STAFF_USER)){ // android 老师端 
				apiKey = MessagePushingUtil.APIKEY_ANDROID_TEACHERAPP;
				secretKey = MessagePushingUtil.SECRETKEY_ANDROID_TEACHERAPP;
				mobileType = 3;
			} else if(user.getMobileType().equals(MobileType.ANDROID) && user.getUserType().equals(MobileUserType.STUDENT_USER)){// android 学生端
				apiKey = MessagePushingUtil.APIKEY_ANDROID_STUAPP;
				secretKey = MessagePushingUtil.SECRETKEY_ANDROID_STUAPP;
				mobileType = 3;
			}else if(user.getMobileType().equals(MobileType.ANDROID) && user.getUserType().equals(MobileUserType.MANAGE)){// android 管理端
				apiKey = MessagePushingUtil.APIKEY_ANDROID_MANAGEAPP;
				secretKey = MessagePushingUtil.SECRETKEY_ANDROID_MANAGEAPP;
				mobileType = 3;
			}
			//消息公告的title 传入PC端定义的title
			if(type.equals(PushMsgType.SYSTEM_NOTICE.toString())){
				if(StringUtils.isNotBlank(id)){
					SystemNotice notice=systemNoticeDao.findById(id);
					if(notice!=null){						
						title=notice.getTitle();
						if(title.length()>12){
							title=title.substring(0, 12);
							title+="...";
						}
					}else{
						title="星火管理端";
					}
					
				}				
			}
			if(mobileType==4){//IOS推送
				if(id != null && StringUtils.isNotBlank(id) && type != null && StringUtils.isNotBlank(type) && time!=null && StringUtils.isNotBlank(time)){
					this.pushIOSMsg(user.getPlatFormChannelId(), user.getPlatFormUserId(), msgContent, apiKey, secretKey, env, mobileType, id, type ,time, title, sysMsgType, detailId);
				}else{
					this.pushIOSMsg(user.getPlatFormChannelId(), user.getPlatFormUserId(), msgContent, apiKey, secretKey, env, mobileType,title );
				}
				
			}else{//Android推送
				if(id != null && StringUtils.isNotBlank(id) && type != null && StringUtils.isNotBlank(type) && time!=null && StringUtils.isNotBlank(time) ){
					this.pushAndroidMsg(user.getPlatFormChannelId(), user.getPlatFormUserId(), msgContent, apiKey, secretKey, env, mobileType,title, id, type , time, sysMsgType, detailId);
				}else{
					this.pushAndroidMsg(user.getPlatFormChannelId(), user.getPlatFormUserId(), msgContent, apiKey, secretKey, env, mobileType,title);
				}
				
			}
		}
	}

	
	
	/**IOS推送
	 * @param platFormChannelId
	 * @param platFormUserId
	 * @param msgContent
	 * @param apiKey
	 * @param secretKey
	 * @param env  1: dev; 2: prd
	 * @param mobileType 
	 */
	private void pushIOSMsg(String platFormChannelId, String platFormUserId,
			String msgContent, String apiKey, String secretKey, int env, int mobileType, String title) {
		
		if(StringUtils.isNotBlank(platFormChannelId) && StringUtils.isNotBlank(platFormUserId) ) {			
			/*
	         * @brief 推送单播通知(IOS APNS) message_type = 1 (默认为0)
	         */
	        // 1. 设置developer平台的ApiKey/SecretKey
//	        String apiKey = "UxDKepfHDQbqrbwKhKKDldkT";
//	        String secretKey = "l7GpUZ9zhzEbAvVXy5HlqBOB8tuBEcNS";
	        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

	        // 2. 创建BaiduChannelClient对象实例
	        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

	        // 3. 若要了解交互细节，请注册YunLogHandler类
	        channelClient.setChannelLogHandler(new YunLogHandler() {
	            @Override
	            public void onHandle(YunLogEvent event) {
	                //System.out.println(event.getMessage());
	            }
	        });

	        try {

	            // 4. 创建请求类对象
	            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
	            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
	            request.setDeviceType(mobileType); // device_type => 1: web 2: pc 3:android
	                                      // 4:ios 5:wp
	            request.setDeployStatus(env); // DeployStatus => 1: Developer 2:
	                                        // Production
	            request.setChannelId(Long.valueOf(platFormChannelId));
	            request.setUserId(platFormUserId);
	            
	            if(mobileType == 4) {  // 如果IOS 就是消息推送
	            	request.setMessageType(1);
	            } else {			// 如果是 android 就是消息透传
	            	request.setMessageType(0);
	            }
	            
	            request.setMessage("{\"aps\":{\"alert\":\"" + msgContent + "\",\"badge\":1,\"sound\":\"ttt\"}},\"title\":\"" + title + "\" ");
	            // 5. 调用pushMessage接口
	            PushUnicastMessageResponse response = channelClient
	                    .pushUnicastMessage(request);

	            // 6. 认证推送成功
				log.info("push amount : " + response.getSuccessAmount()+" msg :"+ request.getMessage());

	        } catch (ChannelClientException e) {
	            // 处理客户端错误异常
	            e.printStackTrace();
	        } catch (ChannelServerException e) {
	            // 处理服务端错误异常
//	            System.out.println(String.format(
//	                    "request_id: %d, error_code: %d, error_message: %s",
//	                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	        }		
		}
		
		
	}
	
	
	/**
	 * Android推送
	 * @param platFormChannelId
	 * @param platFormUserId
	 * @param msgContent
	 * @param apiKey
	 * @param secretKey
	 * @param env  1: dev; 2: prd
	 * @param mobileType 
	 */
	@SuppressWarnings("unchecked")
	private void pushAndroidMsg(String platFormChannelId, String platFormUserId,
			String msgContent, String apiKey, String secretKey, int env, int mobileType,String title) {
		
		if(StringUtils.isNotBlank(platFormChannelId) && StringUtils.isNotBlank(platFormUserId) ) {			
			/*
	         * @brief 推送单播通知(IOS APNS) message_type = 1 (默认为0)
	         */
	        // 1. 设置developer平台的ApiKey/SecretKey
//	        String apiKey = "UxDKepfHDQbqrbwKhKKDldkT";
//	        String secretKey = "l7GpUZ9zhzEbAvVXy5HlqBOB8tuBEcNS";
	        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

	        // 2. 创建BaiduChannelClient对象实例
	        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

	        // 3. 若要了解交互细节，请注册YunLogHandler类
	        channelClient.setChannelLogHandler(new YunLogHandler() {
	            @Override
	            public void onHandle(YunLogEvent event) {
	                //System.out.println(event.getMessage());
	            }
	        });

	        try {

	            // 4. 创建请求类对象
	            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
	            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
	            request.setDeviceType(mobileType); // device_type => 1: web 2: pc 3:android
	                                      // 4:ios 5:wp
	            request.setDeployStatus(env); // DeployStatus => 1: Developer 2:
	                                        // Production
	            request.setChannelId(Long.valueOf(platFormChannelId));
	            request.setUserId(platFormUserId);	          
	            request.setMessageType(1);
	            
	            
	            
	          //创建 Android的通知				
				JSONObject notification = new JSONObject();
				notification.put("title", title);
				notification.put("description",msgContent);
				notification.put("notification_builder_id", 0);
				notification.put("notification_basic_style", 5);//1角标数字 ，2震动,3震动+角标 4声音，5声音+角标			
				JSONObject jsonCustormCont = new JSONObject();
				jsonCustormCont.put("key", "value"); //自定义内容，key-value
				notification.put("custom_content", jsonCustormCont);				
	            request.setMessage(notification.toString());
	            // 5. 调用pushMessage接口
	            PushUnicastMessageResponse response = channelClient
	                    .pushUnicastMessage(request);

	            // 6. 认证推送成功
	            //System.out.println("push amount : " + response.getSuccessAmount());

	        } catch (ChannelClientException e) {
	            // 处理客户端错误异常
	            e.printStackTrace();
	        } catch (ChannelServerException e) {
	            // 处理服务端错误异常
//	            System.out.println(String.format(
//	                    "request_id: %d, error_code: %d, error_message: %s",
//	                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	        }	        
		}
		
		
	}

	@Override
	public void pushRemindToMobileUserIds(List<String> arrayOfmobileUserId,
			String createMobileUserId, String msgContent) {
		pushNormalMsgToMobileUserIds(arrayOfmobileUserId, createMobileUserId,
				msgContent, SessionType.REMIND);
	}

	@Override
	public void pushRemindToUserIds(List<String> arrayOfUserId,
			String createUserId, String msgContent) {
		List<String> arrayOfmobileUserId = new ArrayList<String>();
		String createMobileUserId = mobileUserService.findMobileUserByStaffId(createUserId).getId();
		for(String userId : arrayOfUserId) {
			arrayOfmobileUserId.add(mobileUserService.findMobileUserByStaffId(userId).getId());
		}
		this.pushRemindToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
	}

	@Override
	public void pushRemindToUserAndStudentIds(List<String> arrayOfUserId,
			List<String> arrayOfStuId, String createUserId, String msgContent) {
		List<String> arrayOfmobileUserId = new ArrayList<String>();
		String createMobileUserId = mobileUserService.findMobileUserByStaffId(createUserId).getId();
		for(String userId : arrayOfUserId) {
			arrayOfmobileUserId.add(mobileUserService.findMobileUserByStaffId(userId).getId());
		}
		for(String userId : arrayOfStuId) {
			arrayOfmobileUserId.add(mobileUserService.findMobileUserByStuId(userId).getId());
		}
		this.pushRemindToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
	}
	@Override
	public MobilePushMsgRecordVo getMobilePushMsgRecordById(String recordId) {
		return HibernateUtils.voObjectMapping(msgRecordDao.findById(recordId), MobilePushMsgRecordVo.class);
	}

    @Override
    public void pushNoticeToUserAndStudentIds(List<String> arrayOfUserId, List<String> arrayOfStuId, String createUserId, String msgContent) {
        List<String> arrayOfmobileUserId = new ArrayList<String>();
        String createMobileUserId = mobileUserService.findMobileUserByStaffId(createUserId).getId();
        for(String userId : arrayOfUserId) {
            arrayOfmobileUserId.add(mobileUserService.findMobileUserByStaffId(userId).getId());
        }
        for(String userId : arrayOfStuId) {
            arrayOfmobileUserId.add(mobileUserService.findMobileUserByStuId(userId).getId());
        }
        this.pushNoticeToMobileUserIds(arrayOfmobileUserId, createMobileUserId, msgContent);
    }

    @Override
    public List<MobilePushMsgRecordVo> getMsgByMobileUserIdAndSessionType(String mobileUserId, SessionType sessionType, String sessionId, int pageNo, int pageSize, String order,String lastFetchTime) {
        List<MobilePushMsgSession> sessionList = null;
        MobilePushMsgSession session =null;
        if(sessionType.equals(SessionType.NOTICE)  ) {
            sessionList =   sessionDao.getNoticeSessionsByMobileUserId(mobileUserId);
        } else if(sessionType.equals(SessionType.REMIND)) {
            sessionList =   sessionDao.getRemindSessionsByMobileUserId(mobileUserId);
        } else if(sessionType.equals(SessionType.CONVERSATION) || sessionType.equals(SessionType.COURSE)) {
            session = sessionDao.findById(sessionId);
            sessionList = new ArrayList<MobilePushMsgSession>();
            sessionList.add(session);
        } else {
            return new ArrayList<MobilePushMsgRecordVo>();
        }       
        List<MobilePushMsgRecord> recordList=null;
        if(sessionType.equals(SessionType.CONVERSATION)){//只取session存在人对话的记录                	
        	 recordList =  msgRecordDao.getConversationRecordBySessionsForPage(session,pageNo, pageSize,lastFetchTime);
        }else{
        	 recordList =  msgRecordDao.getRecordBySessionsForPage(sessionList, pageNo, pageSize,lastFetchTime);
        }
        if("asc".equals(order)) {
        	Collections.reverse(recordList); 
        }
        List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
        for(MobilePushMsgRecordVo recordVo : recordVos) {
        	String name = this.getUserNameByMobileUserIdAndType(recordVo.getSenderId(), recordVo.getSenderType() );
        	recordVo.setSenderName(name);
        }
        return recordVos;
    }

    @Override
    public List<MobilePushMsgRecordVo> getLastestMsgByMobileUserIdAndSessionType(String mobileUserId, SessionType sessionType, String sessionId, String lastFetchTime) {
        List<MobilePushMsgSession> sessionList = null;
        if(sessionType.equals(SessionType.NOTICE)  ) {
            sessionList =   sessionDao.getNoticeSessionsByMobileUserId(mobileUserId);
        } else if(sessionType.equals(SessionType.REMIND)) {
            sessionList =   sessionDao.getRemindSessionsByMobileUserId(mobileUserId);
        } else if(sessionType.equals(SessionType.CONVERSATION)) {
            MobilePushMsgSession session = sessionDao.findById(sessionId);
            sessionList = new ArrayList<MobilePushMsgSession>();
            sessionList.add(session);
        } else {
            return new ArrayList<MobilePushMsgRecordVo>();
        }
        List<MobilePushMsgRecord> recordList =  msgRecordDao.getLastestRecordBySessions(sessionList, lastFetchTime);
        List<MobilePushMsgRecordVo> recordVos = HibernateUtils.voListMapping(recordList, MobilePushMsgRecordVo.class);
        return recordVos;
    }

    @Override
    public void createPushMsgBySystemNotice(SystemNotice systemNotice) {
        String createMobileUserId = mobileUserService.findMobileUserByStaffId(systemNotice.getCreateUser().getUserId()).getId();
        String contentStr = systemNotice.getContent();
        if(contentStr.length() >= 1000) {
        	contentStr = contentStr.substring(0, 1000);
        }
        contentStr= contentStr.replaceAll("<p>","");
        contentStr = contentStr.replaceAll("</p>","");
        this.createNotice(createMobileUserId,  systemNotice.getId() ,contentStr , systemNotice.getTitle());
    }

    @Override
    public boolean hasNeedSendQueueMsg() {
        StringBuffer hql = new StringBuffer();
        hql.append("select count(*) from MobilePushMsgSession as session ").append("where ")
                .append(" session.msgStatus = '").append(MsgStatus.NEED_SEND_QUEUE).append("'");
        int count = sessionDao.findCountHql(hql.toString(), new HashMap<String, Object>());
        return count>0? true: false;
    }

    @Override
    public List<MobilePushMsgSessionVo> getNeedSendQueueSession() {
        StringBuffer hql = new StringBuffer();
        hql.append("from MobilePushMsgSession as session ").append("where ")
                .append(" session.msgStatus = '").append(MsgStatus.NEED_SEND_QUEUE).append("'");
        List<MobilePushMsgSession> sessions = sessionDao.findAllByHQL(hql.toString(), new HashMap<String, Object>());
        List<MobilePushMsgSessionVo> vos = HibernateUtils.voListMapping(sessions, MobilePushMsgSessionVo.class);
        return vos;
    }

    @Override
    public void pushSendQueue(MobilePushMsgSessionVo sessionVo) {
		// 根据不同的role 和 organ 插入不同的 session User 的里面
		MobilePushMsgSession session = sessionDao.findById(sessionVo.getId());
		session.setMsgStatus(MsgStatus.NEW);//先改变状态
//		Set<MobileUser> mobileUsers = session.getMobileUsers();

		// 获取role 和 organID
		String noticeId = session.getRemark();
//		SystemNotice notice = systemNoticeDao.findById(noticeId);
//		
//		List<User> userList =  userService.getUserListForMobileByRoleAndOrg(notice.getRole(), notice.getOrganization());
//		
//		for (User user : userList) {
//			MobileUser mobileUser =   mobileUserService.findMobileUserByStaffId(user.getUserId());
//			mobileUsers.add(mobileUser);
//		}	
		//将公告插入MOBILE_PUSH_MSG_SESSION_USER表
		if(noticeId!=null){
			mobileUserService.saveSessionUser(noticeId, sessionVo.getId());
		}
		for(MobilePushMsgRecord record : session.getRecords()) {
			record.setMsgStatus(MsgStatus.NEW);
		}
    }

    /**
     * 因为 校区公告比较特殊， 需要一次性发送很多信息， 所以发送的信息都是留待 单例里面实现
     * @param systemNoticeId
     * @param createMobileUserId
     * @param msgTitle
     * @param msgContent
     */
    private void createNotice(String createMobileUserId, String systemNoticeId, String msgContent, String msgTitle) {

        MobilePushMsgSession session = sessionDao.createOneSession(SessionType.NOTICE, createMobileUserId);
        // 设置 系统公告 ID
        session.setMsgStatus(MsgStatus.NEED_SEND_QUEUE);
        session.setRemark(systemNoticeId);

        MobilePushMsgRecord msg  = new MobilePushMsgRecord();
        MobileUser user = mobileUserDao.findById(createMobileUserId);
        msg.setMsgStatus(MsgStatus.NEED_SEND_QUEUE);
        msg.setSender(user);
        msg.setMsgContent(msgContent);
        msg.setMsgTitle(msgTitle);
        msg.setSession(session);
        msg.setCreateTime(DateTools.getCurrentDateTime());
        msgRecordDao.save(msg);
    }

	@Override
	public boolean hasMoreMessageToMobileUserId(String mobileUserId,
			String lastFetchTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(); 
		hql.append("select count(*) from MobilePushMsgRecord as record inner join record.session.mobileUsers as muser ").append("where ")
			.append("record.session.sessionType in ('")
			.append(SessionType.NOTICE).append("','")
			.append(SessionType.REMIND).append("','")
			.append(SessionType.CONVERSATION).append("'")
			.append(") and ")
			.append("  record.createTime > :lastFetchTime ")
			.append(" and muser.id = :mobileUserId ")
			;
		params.put("lastFetchTime", lastFetchTime);
		params.put("mobileUserId", mobileUserId);
		int count = msgRecordDao.findCountHql(hql.toString(), params);
		return count>0;
	}

	@Override
	public List<Map> getTotalCountOfUserUnReadRecord(String mobileUserId,
			String lastFetchTime) {
		Map noticeMap = this.getUnReadRecordInfoBySession(mobileUserId, SessionType.NOTICE, lastFetchTime, null);
		Map remindMap = this.getUnReadRecordInfoBySession(mobileUserId, SessionType.REMIND, lastFetchTime, null);
		List conversationList = this.getUnReadConversationRecordSummaryGroupByPage(mobileUserId, lastFetchTime);
		List<Map> returnList = new ArrayList<Map>();
		if(noticeMap!=null) {
			returnList.add(noticeMap);
		}
		if(remindMap!=null) {
			returnList.add(remindMap);
		}
		if(conversationList!=null && conversationList.size() > 0) {
			returnList.addAll(conversationList);
			//returnList.add(conversationMap);
		}
		return returnList;
	}

	private List getUnReadConversationRecordSummaryGroupByPage(
			String mobileUserId, String lastFetchTime) {
		// 获取到 未读消息的 session
		List<MobilePushMsgSession> sessionList = this.getAllUnReadConversationSessions(mobileUserId, lastFetchTime);
		List<Map> returnList = new ArrayList<Map>();
		Map resultMap; 
		for(MobilePushMsgSession session : sessionList) {
			 resultMap = this.getUnReadRecordInfoBySession(mobileUserId, SessionType.CONVERSATION, lastFetchTime, session.getId());
			 if(resultMap!=null) {
				 returnList.add(resultMap);
			 } 
		}
		return returnList;
	}

	/**
	 * 获取到 unread 的 conversation 的对话session 信息
	 * @param mobileUserId
	 * @param lastFetchTime
	 * @return
	 */
	private List<MobilePushMsgSession> getAllUnReadConversationSessions(
			String mobileUserId, String lastFetchTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer sessionSB = new StringBuffer(); 
		//MobilePushMsgRecord
		sessionSB.append("select distinct session from MobilePushMsgSession as session inner join  session.records as record " +
				"join session.mobileUsers as muser ").append("where ")
		.append("session.sessionType in ('")
		.append(SessionType.CONVERSATION).append("'")
		.append(") and ")
		.append("  record.createTime > :lastFetchTime ")
		.append(" and muser.id = :mobileUserId ")
		;
		params.put("lastFetchTime", lastFetchTime);
		params.put("mobileUserId", mobileUserId);
		return this.sessionDao.findAllByHQL(sessionSB.toString(), params);
	}

	/**
	 * 根据不同类型获得不同的 最近的一条记录 和 未读总数
	 * @param mobileUserId
	 * @param lastFetchTime
	 * @param sessionId   如果是 对话信息 就是加入 session id的信息进去
	 * @return
	 */
	private Map getUnReadRecordInfoBySession(String mobileUserId,
			SessionType sessionType, String lastFetchTime, String sessionId) {
		// 总数
		if(sessionType == SessionType.CONVERSATION ) {
			StringBuffer countSB = new StringBuffer(); 
			countSB.append("select count(*) from MobilePushMsgRecord as record inner join record.session.mobileUsers as muser ").append("where ")
			.append("record.session.sessionType in (:sessionType")
			.append(") and ")
			.append("  record.createTime > :lastFetchTime ")
			.append(" and muser.id = :mobileUserId ")
			.append(" and record.session.id = :sessionId ")  // 如果是对话的话， 可以是加入 session ID
			;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionType", sessionType);
			params.put("lastFetchTime", lastFetchTime);
			params.put("mobileUserId", mobileUserId);
			params.put("sessionId", sessionId);
			int count = msgRecordDao.findCountHql(countSB.toString(), params);
			if (count > 0) {
				// 最近的一条记录
				StringBuffer recentSB = new StringBuffer();
				recentSB.append("select record from MobilePushMsgRecord as record inner join record.session.mobileUsers as muser ").append("where ")
				.append("record.session.sessionType in (:sessionType")
				.append(") and ")
				.append("  record.createTime > :lastFetchTime ")
				.append(" and muser.id = :mobileUserId ")
				.append(" and record.session.id = :sessionId ")   // 如果是对话的话， 可以是加入 session ID
				.append(" order by  record.createTime desc ");
				;
				
				// 要判断是否为空
				List recordList = msgRecordDao.findLimitHql(
						recentSB.toString(), 1, params);
				MobilePushMsgRecord pushRecord = (MobilePushMsgRecord) recordList
						.get(0);
				
				MobilePushMsgRecordVo msgRecordVo = HibernateUtils.voObjectMapping(
						pushRecord, MobilePushMsgRecordVo.class);
				this.setupMobilePushMsgRecordVo(msgRecordVo);
				msgRecordVo.setSenderName(this.getUserNameByMobileUserIdAndType(msgRecordVo.getSenderId(), msgRecordVo.getSenderType()));
				if(sessionType ==  SessionType.CONVERSATION) {
					StringBuffer joinerNamesSB = new StringBuffer(), joinerIdsSB = new StringBuffer();;
					for(MobileUser mUser :pushRecord.getSession().getMobileUsers()) {
						joinerNamesSB.append(this.getUserNameByMobileUserIdAndType(mUser.getId(), mUser.getUserType())).append("###");
						joinerIdsSB.append(mUser.getId()).append("###");
					}
					msgRecordVo.setJoinerNames(joinerNamesSB.toString());
					msgRecordVo.setJoinerIds(joinerIdsSB.toString());
				}
				Map returnMap = new HashMap();
				returnMap.put("count", count);
				returnMap.put("lastRecord", msgRecordVo);
				returnMap.put("sessionType", sessionType);
				return returnMap;
			}
			return null; 

		} else {
			StringBuffer countSB = new StringBuffer(); 
			countSB.append("select count(*) from MobilePushMsgRecord as record inner join record.session.mobileUsers as muser ").append("where ")
			.append("record.session.sessionType in (:sessionType")
			.append(") and ")
			.append("  record.createTime > :lastFetchTime ")
			.append(" and muser.id = :mobileUserId ")
			;
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("sessionType", sessionType);
			params.put("lastFetchTime", lastFetchTime);
			params.put("mobileUserId", mobileUserId);
			int count = msgRecordDao.findCountHql(countSB.toString(), params);
			if (count > 0) {
				// 最近的一条记录
				StringBuffer recentSB = new StringBuffer();
				recentSB.append("select record from MobilePushMsgRecord as record inner join record.session.mobileUsers as muser ").append("where ")
				.append("record.session.sessionType in (:sessionType")
				.append(") and ")
				.append("  record.createTime > :lastFetchTime ")
				.append(" and muser.id = :mobileUserId ")
				.append(" order by  record.createTime desc ");
				;
				
				// 要判断是否为空
				List recordList = msgRecordDao.findLimitHql(
						recentSB.toString(), 1, params);
				MobilePushMsgRecord pushRecord = (MobilePushMsgRecord) recordList
						.get(0);
				
				MobilePushMsgRecordVo msgRecordVo = HibernateUtils.voObjectMapping(
						pushRecord, MobilePushMsgRecordVo.class);
				this.setupMobilePushMsgRecordVo(msgRecordVo);
				msgRecordVo.setSenderName(this.getUserNameByMobileUserIdAndType(msgRecordVo.getSenderId(), msgRecordVo.getSenderType()));
				if(sessionType ==  SessionType.CONVERSATION) {
					StringBuffer senderNamesSB = new StringBuffer();
					for(MobileUser mUser :pushRecord.getSession().getMobileUsers()) {
						senderNamesSB.append(this.getUserNameByMobileUserIdAndType(mUser.getId(), mUser.getUserType())).append("###");
					}
					msgRecordVo.setJoinerNames(senderNamesSB.toString());
				}
				Map returnMap = new HashMap();
				returnMap.put("count", count);
				returnMap.put("lastRecord", msgRecordVo);
				returnMap.put("sessionType", sessionType);
				return returnMap;
			}
			return null; 
			
		}
	}

	/**
	 * 用于 setup push Record vo 的值， 例如 发送人， sessionName... 用于发送给 mobile 前端
	 * @param msgRecordVo
	 */
	private void setupMobilePushMsgRecordVo(MobilePushMsgRecordVo msgRecordVo) {
		msgRecordVo.setSenderName(this.getUserNameByMobileUserIdAndType(msgRecordVo.getSenderId(), msgRecordVo.getSenderType()));
		switch(msgRecordVo.getSessionType()) {
		case CONVERSATION:
			msgRecordVo.setSessionName(msgRecordVo.getSenderName());
			break;
		case NOTICE:
			msgRecordVo.setSessionName("校区公告");
			break;
		case REMIND:
			msgRecordVo.setSessionName("提醒通知");
			break;
		}
	}

	@Override
	public SystemNoticeVo getNoticeByRecordId(String recordId) {
		MobilePushMsgRecord record =msgRecordDao.findById(recordId);
		String noticeId = record.getSession().getRemark();
		if(StringUtil.isNotBlank(noticeId )) {
			SystemNotice notite = systemNoticeDao.findById(noticeId); 
			SystemNoticeVo vo= HibernateUtils.voObjectMapping(notite, SystemNoticeVo.class);
			return vo;
		} else {
			return null;
		}
	}
	
	/**IOS推送  加入id和type
	 * @param platFormChannelId
	 * @param platFormUserId
	 * @param msgContent
	 * @param apiKey
	 * @param secretKey
	 * @param env  1: dev; 2: prd
	 * @param mobileType 
	 * @param id 
	 * @param type 
	 */
	private void pushIOSMsg(String platFormChannelId, String platFormUserId,
			String msgContent, String apiKey, String secretKey, int env, int mobileType,String id,String type, String time, String title, SysMsgType sysMsgType, String detailId) {
		
		if(StringUtils.isNotBlank(platFormChannelId) && StringUtils.isNotBlank(platFormUserId) ) {			
			/*
	         * @brief 推送单播通知(IOS APNS) message_type = 1 (默认为0)
	         */
	        // 1. 设置developer平台的ApiKey/SecretKey
//	        String apiKey = "UxDKepfHDQbqrbwKhKKDldkT";
//	        String secretKey = "l7GpUZ9zhzEbAvVXy5HlqBOB8tuBEcNS";
	        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

	        // 2. 创建BaiduChannelClient对象实例
	        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

	        // 3. 若要了解交互细节，请注册YunLogHandler类
	        channelClient.setChannelLogHandler(new YunLogHandler() {
	            @Override
	            public void onHandle(YunLogEvent event) {
	                //System.out.println(event.getMessage());
	            }
	        });

	        try {

	            // 4. 创建请求类对象
	            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
	            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
	            request.setDeviceType(mobileType); // device_type => 1: web 2: pc 3:android
	                                      // 4:ios 5:wp
	            request.setDeployStatus(env); // DeployStatus => 1: Developer 2:
	                                        // Production
	            request.setChannelId(Long.valueOf(platFormChannelId));
	            request.setUserId(platFormUserId);
	            
	            if(mobileType == 4) {  // 如果IOS 就是消息推送
	            	request.setMessageType(1);
	            } else {			// 如果是 android 就是消息透传
	            	request.setMessageType(0);
	            }
	            
	            String sysMsgTypeValue = "";
	            if (sysMsgType != null) {
	            	sysMsgTypeValue = sysMsgType.getValue();
	            }
	            
	            if (msgContent.length() > 39) {
	            	msgContent = msgContent.substring(0, 38);
	            }
	            
	            
	            
	            request.setMessage("{\"aps\":{\"alert\":\"" +msgContent  + "\",\"badge\":1,\"sound\":\"ttt\"},\"id\":\"" + id + "\",\"typ\":\"" + getPushMsgType(type) + "\",\"tit\":\"" + title + "\",\"smt\":\"" + getSysMsgType(sysMsgTypeValue) + "\",\"dId\":\"" + detailId + "\"}");
	            // 5. 调用pushMessage接口
	            PushUnicastMessageResponse response = channelClient
	                    .pushUnicastMessage(request);

	            // 6. 认证推送成功
				log.info("push amount : " + response.getSuccessAmount()+" msg :"+ request.getMessage());

	        } catch (ChannelClientException e) {
	            // 处理客户端错误异常
	            e.printStackTrace();
	        } catch (ChannelServerException e) {
	            // 处理服务端错误异常
	            System.out.println(String.format(
	                    "request_id: %d, error_code: %d, error_message: %s",
	                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	        }		
		}
		
		
	}
	
	
	/**
	 * Android推送 加入id和type
	 * @param platFormChannelId
	 * @param platFormUserId
	 * @param msgContent
	 * @param apiKey
	 * @param secretKey
	 * @param env  1: dev; 2: prd
	 * @param mobileType 
	 */
	@SuppressWarnings("unchecked")
	private void pushAndroidMsg(String platFormChannelId, String platFormUserId,
			String msgContent, String apiKey, String secretKey, int env, int mobileType,String title,String id,String type, String time, SysMsgType sysMsgType, String detailId) {
		
		if(StringUtils.isNotBlank(platFormChannelId) && StringUtils.isNotBlank(platFormUserId) ) {			
			/*
	         * @brief 推送单播通知(IOS APNS) message_type = 1 (默认为0)
	         */
	        // 1. 设置developer平台的ApiKey/SecretKey
//	        String apiKey = "UxDKepfHDQbqrbwKhKKDldkT";
//	        String secretKey = "l7GpUZ9zhzEbAvVXy5HlqBOB8tuBEcNS";
	        ChannelKeyPair pair = new ChannelKeyPair(apiKey, secretKey);

	        // 2. 创建BaiduChannelClient对象实例
	        BaiduChannelClient channelClient = new BaiduChannelClient(pair);

	        // 3. 若要了解交互细节，请注册YunLogHandler类
	        channelClient.setChannelLogHandler(new YunLogHandler() {
	            @Override
	            public void onHandle(YunLogEvent event) {
	                //System.out.println(event.getMessage());
	            }
	        });

	        try {

	            // 4. 创建请求类对象
	            // 手机端的ChannelId， 手机端的UserId， 先用1111111111111代替，用户需替换为自己的
	            PushUnicastMessageRequest request = new PushUnicastMessageRequest();
	            request.setDeviceType(mobileType); // device_type => 1: web 2: pc 3:android
	                                      // 4:ios 5:wp
	            request.setDeployStatus(env); // DeployStatus => 1: Developer 2:
	                                        // Production
	            request.setChannelId(Long.valueOf(platFormChannelId));
	            request.setUserId(platFormUserId);	          
	            request.setMessageType(1);
	            
	            
	            
	          //创建 Android的通知				
				JSONObject notification = new JSONObject();
				notification.put("title", title);
				notification.put("description",msgContent);
				notification.put("notification_builder_id", 0);
				notification.put("notification_basic_style", 5);//1角标数字 ，2震动,3震动+角标 4声音，5声音+角标					
				JSONObject jsonCustormCont = new JSONObject();
				jsonCustormCont.put("id", id);
				jsonCustormCont.put("type", type);
				jsonCustormCont.put("time", time);
				String sysMsgTypeValue = "";
	            if (sysMsgType != null) {
	            	sysMsgTypeValue = sysMsgType.getValue();
	            }
				jsonCustormCont.put("sysMsgType", sysMsgTypeValue);
				jsonCustormCont.put("detailId", detailId);
//				jsonCustormCont.put("key", "value"); //自定义内容，key-value
				notification.put("custom_content", jsonCustormCont);
	            request.setMessage(notification.toString());
	            // 5. 调用pushMessage接口
	            PushUnicastMessageResponse response = channelClient
	                    .pushUnicastMessage(request);

	            // 6. 认证推送成功
	            System.out.println("push amount : " + response.getSuccessAmount());

	        } catch (ChannelClientException e) {
	            // 处理客户端错误异常
	            e.printStackTrace();
	        } catch (ChannelServerException e) {
	            // 处理服务端错误异常
//	            System.out.println(String.format(
//	                    "request_id: %d, error_code: %d, error_message: %s",
//	                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
	        }	        
		}
		
		
	}
	public static void main(String[] args){
		//Android推送
		//pushAndroidMsg("3863072880857899959", "1117546230853099870", "推送内容", "xbXpXBKp3TT8FVGAeVlH3Yl1", "yvAnw6LuTlrVHO9xWEc0xfpCOgUknHPc", 1, 3  );
		
		//IOS推送
		//pushIOSMsg("4844603661939964477", "948608614675535682", "发送推送通知", "1puMsmxa0duNGO7Bxpv024l8", "ZOhw4DIBZvx77quYxpoEdNthkBv3fH6X", 1, 4  );
		
	}

	/**
	 * 根据ids获得对应的mobileUser用户，推送消息
	 */
	@Override
	public void pushMsgToMobileByUserIds(String userIds, String type, String newsId,String msgContent,String time) {
		if (StringUtils.isNotBlank(userIds)) {
			String[] idArray = userIds.split(",");
			for (int i = 0; i < idArray.length; i++) {
				User user=userDao.findById(idArray[i]);
				MobileUser mobileUser=mobileUserDao.findMobileUserByUserId(user.getUserId());
				if(mobileUser!=null){
					this.pushMsg(mobileUser, msgContent, newsId, type,time, null, "", "星火管理端");
				}				
			}
		} else {
			throw new ApplicationException("传入的用户id不允许为空");
		}
		
	}
}
