package com.pad.entity;

import javax.persistence.*;

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
@Entity@Table(name= "cms_content_sign")
public class CmsContentSign implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsContentId;

	private Integer cmsSignId;

	private Date createTime;

	private String createUser;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="cms_content_id")
	public Integer getCmsContentId() {
		return cmsContentId;
	}

	public void setCmsContentId(Integer cmsContentId) {
		this.cmsContentId = cmsContentId;
	}

	@Column(name ="cms_sign_id")
	public Integer getCmsSignId() {
		return cmsSignId;
	}

	public void setCmsSignId(Integer cmsSignId) {
		this.cmsSignId = cmsSignId;
	}

	@Column(name ="create_time",insertable = false,updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name ="create_user",updatable = false)
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
