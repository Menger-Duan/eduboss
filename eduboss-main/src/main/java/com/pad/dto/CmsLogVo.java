package com.pad.dto;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsLogVo implements java.io.Serializable{

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsId;
	private String content;

	private String oprateType;

	private Date createTime;

	private String createUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCmsId() {
		return cmsId;
	}

	public void setCmsId(Integer cmsId) {
		this.cmsId = cmsId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOprateType() {
		return oprateType;
	}

	public void setOprateType(String oprateType) {
		this.oprateType = oprateType;
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
		return "CmsLogVo{" +
			", id=" + id +
			", cmsId=" + cmsId +
			", content=" + content +
			", oprateType=" + oprateType +
			", createTime=" + createTime +
			", createUser=" + createUser +
			"}";
	}
}
