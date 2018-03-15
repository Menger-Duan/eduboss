package com.eduboss.domain;


import com.eduboss.utils.DateTools;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;

@Entity
@Table(name = "user_modify_log")
public class UserModifyLog implements java.io.Serializable {

	private Integer id ;
	private String userId;
	private String logInfo;
	private String createUser;
	private String createTime;

	private String createUserName;

	public UserModifyLog() {
	}

	public UserModifyLog(String userId, String logInfo, String createUser) {
		this.userId = userId;
		this.logInfo = logInfo;
		this.createUser = createUser;
	}

	@Id
	@Column(name = "id", unique = false, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "create_time",updatable = false,insertable = false)
	public String getCreateTime() {
		return createTime!=null && createTime.length()>19 ? createTime.substring(0,19) : createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@Column(name = "log_info")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	@Column(name = "create_user")
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	@Transient
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
}