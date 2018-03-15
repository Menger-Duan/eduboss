package com.eduboss.domainVo;

import java.util.List;

import com.eduboss.domain.UserDeptJob;


public class NeedSyncUserVo {

	private String id;
	private String name;
	private String contact;
	private String e_mail;
	private int type;
	private String createTime;
	private String createUser;
	private int isSync;
	List<UserDeptJob> deptJobList;

	private String password;
	private String account;
	private Integer enableFlg;
	private String sex;
	private String workType;
	private String RealName;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getE_mail() {
		return e_mail;
	}
	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public int getIsSync() {
		return isSync;
	}
	public void setIsSync(int isSync) {
		this.isSync = isSync;
	}
	public List<UserDeptJob> getDeptJobList() {
		return deptJobList;
	}
	public void setDeptJobList(List<UserDeptJob> deptJobList) {
		this.deptJobList = deptJobList;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getEnableFlg() {
		return enableFlg;
	}
	public void setEnableFlg(Integer enableFlg) {
		this.enableFlg = enableFlg;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}


}
