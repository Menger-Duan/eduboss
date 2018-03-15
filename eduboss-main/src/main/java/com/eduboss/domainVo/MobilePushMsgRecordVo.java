package com.eduboss.domainVo;

import com.eduboss.common.MobileUserType;
import com.eduboss.common.SessionType;
import com.eduboss.domain.MobilePushMsgSession;
import com.eduboss.domain.MobileUser;

public class MobilePushMsgRecordVo {
	// Fields
		private String id;
		private String sessionId;
		private String sessionName;
		private String sessionTypeValue;
		private String sessionTypeName;
		private SessionType sessionType; 
//		private String sessionType;
		
		private String createTime;
		
		private MobileUserType senderType;
		private String senderName; 
		private String senderId;
		//private MobileUser receiver; 
		private String msgContent;
		private String msgTitle; 
		
		private String dataTypeValue;
		private String dataTypeName;
		
		private String joinerNames;  //  这个session 所有人的 name的字符串
		private String joinerIds;  //  这个session 所有人的 mobileUserId的字符串
		
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSessionId() {
			return sessionId;
		}
		public void setSessionId(String sessionId) {
			this.sessionId = sessionId;
		}
		public String getSessionTypeValue() {
			return sessionTypeValue;
		}
		public void setSessionTypeValue(String sessionTypeValue) {
			this.sessionTypeValue = sessionTypeValue;
		}
		public String getSessionTypeName() {
			return sessionTypeName;
		}
		public void setSessionTypeName(String sessionTypeName) {
			this.sessionTypeName = sessionTypeName;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getSenderName() {
			return senderName;
		}
		public void setSenderName(String senderName) {
			this.senderName = senderName;
		}
		public String getSenderId() {
			return senderId;
		}
		public void setSenderId(String senderId) {
			this.senderId = senderId;
		}
		public String getMsgContent() {
			return msgContent;
		}
		public void setMsgContent(String msgContent) {
			this.msgContent = msgContent;
		}
		public String getDataTypeValue() {
			return dataTypeValue;
		}
		public void setDataTypeValue(String dataTypeValue) {
			this.dataTypeValue = dataTypeValue;
		}
		public String getDataTypeName() {
			return dataTypeName;
		}
		public void setDataTypeName(String dataTypeName) {
			this.dataTypeName = dataTypeName;
		}
		public MobileUserType getSenderType() {
			return senderType;
		}
		public void setSenderType(MobileUserType senderType) {
			this.senderType = senderType;
		}
		public String getMsgTitle() {
			return msgTitle;
		}
		public void setMsgTitle(String msgTitle) {
			this.msgTitle = msgTitle;
		}
		public String getSessionName() {
			return sessionName;
		}
		public void setSessionName(String sessionName) {
			this.sessionName = sessionName;
		}
		public SessionType getSessionType() {
			return sessionType;
		}
		public void setSessionType(SessionType sessionType) {
			this.sessionType = sessionType;
		}
		public String getJoinerNames() {
			return joinerNames;
		}
		public void setJoinerNames(String joinerNames) {
			this.joinerNames = joinerNames;
		}
		public String getJoinerIds() {
			return joinerIds;
		}
		public void setJoinerIds(String joinerIds) {
			this.joinerIds = joinerIds;
		}
		
		
}
