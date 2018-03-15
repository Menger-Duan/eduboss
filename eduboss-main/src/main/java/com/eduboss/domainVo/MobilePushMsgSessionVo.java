package com.eduboss.domainVo;

import com.eduboss.common.SessionType;
import com.eduboss.domain.MobileUser;

public class MobilePushMsgSessionVo {
	// Fields
		private String id;
		private String sessionTypeValue;
		private String sessionTypeName;
		private String remark;
		private String createTime;
		private String createMobileUserId;
		private String createMobileRealUserId;
		private String createMobileUserTypeValue;
		private String createMobileUserTypeName;
		
		private String joinerNames;    // 参与人的姓名字符串  使用了 ### 分别隔开
		private String JoinerIds;     // 参与人的姓名字符串  使用了 ### 分别隔开
		
		
		public String getCreateMobileUserTypeValue() {
			return createMobileUserTypeValue;
		}
		public void setCreateMobileUserTypeValue(String createMobileUserTypeValue) {
			this.createMobileUserTypeValue = createMobileUserTypeValue;
		}
		public String getCreateMobileUserTypeName() {
			return createMobileUserTypeName;
		}
		public void setCreateMobileUserTypeName(String createMobileUserTypeName) {
			this.createMobileUserTypeName = createMobileUserTypeName;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
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
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public String getCreateTime() {
			return createTime;
		}
		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
		public String getCreateMobileUserId() {
			return createMobileUserId;
		}
		public void setCreateMobileUserId(String createMobileUserId) {
			this.createMobileUserId = createMobileUserId;
		}
		public String getCreateMobileRealUserId() {
			return createMobileRealUserId;
		}
		public void setCreateMobileRealUserId(String createMobileRealUserId) {
			this.createMobileRealUserId = createMobileRealUserId;
		}
		public String getJoinerNames() {
			return joinerNames;
		}
		public void setJoinerNames(String joinerNames) {
			this.joinerNames = joinerNames;
		}
		public String getJoinerIds() {
			return JoinerIds;
		}
		public void setJoinerIds(String joinerIds) {
			JoinerIds = joinerIds;
		}
		
		
		
}
