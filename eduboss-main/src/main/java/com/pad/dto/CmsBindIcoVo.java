package com.pad.dto;

import javax.persistence.Column;
import javax.persistence.Id;
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
public class CmsBindIcoVo implements Serializable {

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
	private String menuName;
	private Date createTime;
	private Date modifyTime;
	private String createUser;
	private String modifyUser;
    /**
     * 图片地址
     */
	private String icoUrl;
	private String grayIco;


	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIcoId() {
		return icoId;
	}

	public void setIcoId(Integer icoId) {
		this.icoId = icoId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
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

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public String getIcoUrl() {
		return icoUrl;
	}

	public void setIcoUrl(String icoUrl) {
		this.icoUrl = icoUrl;
	}

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
