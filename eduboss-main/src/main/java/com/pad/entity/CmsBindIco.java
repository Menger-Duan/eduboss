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
@Entity
@Table(name= "cms_bind_ico")
public class CmsBindIco implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;
    /**
     * 图片发布表ID
     */
	private Integer icoId;
    /**
     * 菜单Id
     */
	private Integer menuId;
	private Date createTime;
	private Date modifyTime;
	private String createUser;
	private String modifyUser;
    /**
     * 图片地址
     */
	private String icoUrl;

	private  String grayIco;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="ico_id")
	public Integer getIcoId() {
		return icoId;
	}

	public void setIcoId(Integer icoId) {
		this.icoId = icoId;
	}

	@Column(name ="menu_Id")
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	@Column(name ="create_time",insertable = false,updatable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name ="modify_time",insertable = false,updatable = false)
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name ="create_user",updatable = false)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Column(name ="modify_user")
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name ="ico_url")
	public String getIcoUrl() {
		return icoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}

	@Column(name = "gray_ico")
	public String getGrayIco() {
		return grayIco;
	}

	public void setGrayIco(String grayIco) {
		this.grayIco = grayIco;
	}

	@Override
	public String toString() {
		return "CmsBindIcoVo{" +
			", id=" + id +
			", icoId=" + icoId +
			", menuId=" + menuId +
			", createTime=" + createTime +
			", modifyTime=" + modifyTime +
			", createUser=" + createUser +
			", modifyUser=" + modifyUser +
			", icoUrl=" + icoUrl +
			"}";
	}
}
