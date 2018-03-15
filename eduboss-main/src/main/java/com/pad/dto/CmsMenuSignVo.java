package com.pad.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsMenuSignVo implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsMenuId;

	private Integer cmsSignId;

	private Date createTime;

	private String createUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmsMenuId() {
		return cmsMenuId;
	}

	public void setCmsMenuId(Integer cmsMenuId) {
		this.cmsMenuId = cmsMenuId;
	}
	public Integer getCmsSignId() {
		return cmsSignId;
	}

	public void setCmsSignId(Integer cmsSignId) {
		this.cmsSignId = cmsSignId;
	}
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Override
	public String toString() {
		return "CmsMenuSignVo{" +
			", id=" + id +
			", cmsMenuId=" + cmsMenuId +
			", cmsSignId=" + cmsSignId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
