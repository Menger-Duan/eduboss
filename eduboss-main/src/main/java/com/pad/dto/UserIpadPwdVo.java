package com.pad.dto;

import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class UserIpadPwdVo implements java.io.Serializable{

    private static final long serialVersionUID = 1L;


	private String userId;
	private Integer pwd;
	private Date createTime;
	private Date modifyTime;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getPwd() {
		return pwd;
	}

	public void setPwd(Integer pwd) {
		this.pwd = pwd;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

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
