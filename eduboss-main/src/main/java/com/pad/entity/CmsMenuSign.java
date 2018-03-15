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
@Table(name= "cms_menu_sign")
public class CmsMenuSign implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;

	private Integer cmsMenuId;

	private Integer cmsSignId;

	private Date createTime;

	private String createUser;

	@Id
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name ="cms_menu_id")
	public Integer getCmsMenuId() {
		return cmsMenuId;
	}

	public void setCmsMenuId(Integer cmsMenuId) {
		this.cmsMenuId = cmsMenuId;
	}
	@Column(name ="cms_sign_id")
	public Integer getCmsSignId() {
		return cmsSignId;
	}

	public void setCmsSignId(Integer cmsSignId) {
		this.cmsSignId = cmsSignId;
	}
	@Column(name ="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Column(name ="create_user")
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
