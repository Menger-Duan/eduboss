package com.pad.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsContentSignVo implements Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsContentId;

	private Integer cmsSignId;

	private Date createTime;

	private String createUser;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmsContentId() {
		return cmsContentId;
	}

	public void setCmsContentId(Integer cmsContentId) {
		this.cmsContentId = cmsContentId;
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
		return "CmsContentSignVo{" +
			", id=" + id +
			", cmsContentId=" + cmsContentId +
			", cmsSignId=" + cmsSignId +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
