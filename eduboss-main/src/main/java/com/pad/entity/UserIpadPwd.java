package com.pad.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
@Entity
@Table(name= "user_ipad_pwd")
public class UserIpadPwd implements java.io.Serializable{

    private static final long serialVersionUID = 1L;


	private String userId;
	private String pwd;
	private Date createTime;
	private Date modifyTime;

	@Id
	@Column(name ="USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name ="PWD")
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name ="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name ="modify_time")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		return "UserIpadPwdVo{" +
			", userId=" + userId +
			", pwd=" + pwd +
			", createTime=" + createTime +
			", modifyTime=" + modifyTime +
			"}";
	}
}
