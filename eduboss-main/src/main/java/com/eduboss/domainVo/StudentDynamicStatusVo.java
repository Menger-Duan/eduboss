package com.eduboss.domainVo;

import com.eduboss.common.StudentEventType;

public class StudentDynamicStatusVo {
	// Fields
		private String id;
		private StudentEventType dynamicStatusType;
		private String occourTime;
		private String description;
		private String referUrl;
		private String referuserId;
		private String referuserName;
		private String studentName;
		private String studentId;
		private String dynamicStatusTypeName;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public StudentEventType getDynamicStatusType() {
			return dynamicStatusType;
		}
		public void setDynamicStatusType(StudentEventType dynamicStatusType) {
			this.dynamicStatusType = dynamicStatusType;
		}
		public String getOccourTime() {
			return occourTime;
		}
		public void setOccourTime(String occourTime) {
			this.occourTime = occourTime;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getReferUrl() {
			return referUrl;
		}
		public void setReferUrl(String referUrl) {
			this.referUrl = referUrl;
		}
		public String getReferuserId() {
			return referuserId;
		}
		public void setReferuserId(String referuserId) {
			this.referuserId = referuserId;
		}
		public String getReferuserName() {
			return referuserName;
		}
		public void setReferuserName(String referuserName) {
			this.referuserName = referuserName;
		}
		public String getStudentName() {
			return studentName;
		}
		public void setStudentName(String studentName) {
			this.studentName = studentName;
		}
		public String getStudentId() {
			return studentId;
		}
		public void setStudentId(String studentId) {
			this.studentId = studentId;
		}
		public String getDynamicStatusTypeName() {
			return dynamicStatusTypeName;
		}
		public void setDynamicStatusTypeName(String dynamicStatusTypeName) {
			this.dynamicStatusTypeName = dynamicStatusTypeName;
		}
}
