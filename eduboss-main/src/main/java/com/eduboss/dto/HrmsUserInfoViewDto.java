package com.eduboss.dto;

import com.eduboss.common.UserWorkType;
import com.eduboss.domain.User;
import com.eduboss.utils.DateTools;

import java.util.List;

/**
 * 人事用户数据同步
 */
public class HrmsUserInfoViewDto {
	private String account;
	private String realName;
	private Integer mailStatus;
	private String userName;
	private Integer sex;
	private String contact;
	private String dimissionDate;
	private String employeeNo;
	private Integer enableFlag;
	private String entryDate;
	private String workType;

	private List<StationInfo> stationInfo;


	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(Integer mailStatus) {
		this.mailStatus = mailStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDimissionDate() {
		return dimissionDate;
	}

	public void setDimissionDate(String dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public List<StationInfo> getStationInfo() {
		return stationInfo;
	}

	public void setStationInfo(List<StationInfo> stationInfo) {
		this.stationInfo = stationInfo;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getEnableFlag() {
		return enableFlag;
	}

	public void setEnableFlag(Integer enableFlag) {
		this.enableFlag = enableFlag;
	}

	public class StationInfo {
		private Integer isMajorRole;
		private String orgName;
		private String stationName;

		public Integer getIsMajorRole() {
			return isMajorRole;
		}

		public void setIsMajorRole(Integer isMajorRole) {
			this.isMajorRole = isMajorRole;
		}

		public String getOrgName() {
			return orgName;
		}

		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}

		public String getStationName() {
			return stationName;
		}

		public void setStationName(String stationName) {
			this.stationName = stationName;
		}
	}

	public void pushInfoToUser(User user){
		user.setName(this.getUserName());
		user.setRealName(this.getRealName());
		user.setAccount(this.getAccount());
		user.setMailStatus(this.getMailStatus());
		user.setSex(this.getSex().toString());
		user.setContact(this.getContact());
		user.setEntryDate(this.getEntryDate());
		user.setEnableFlg(this.getEnableFlag());
		user.setWorkType(UserWorkType.valueOf(this.getWorkType()));
		user.setModifyTime(DateTools.getCurrentDateTime());
		user.setEmployeeNo(this.getEmployeeNo());
	}
}
