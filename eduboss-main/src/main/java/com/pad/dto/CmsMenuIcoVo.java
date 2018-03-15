package com.pad.dto;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author Spark
 * @since 2017-11-30
 */
public class CmsMenuIcoVo{

	private Integer id;
    /**
     * 图标版本
     */
	private Integer version;
    /**
     * 发布状态
     */
	private String status;

	private Date createTime;

	private Date modifyTime;
    /**
     * 创建人
     */
	private String createUser;
    /**
     * 发布人
     */
	private String publishUser;
    /**
     * 修改人
     */
	private String modifyUser;

	private Integer menuLevel;

	private Integer menuId;

	private Integer showType;

	public CmsMenuIcoVo() {
	}

	public CmsMenuIcoVo(String status, Integer menuLevel, Integer menuId) {
		this.status = status;
		this.menuLevel = menuLevel;
		this.menuId = menuId;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	private List<CmsBindIcoVo> bindIcoVos;

	public List<CmsBindIcoVo> getBindIcoVos() {
		return bindIcoVos;
	}

	public void setBindIcoVos(List<CmsBindIcoVo> bindIcoVos) {
		this.bindIcoVos = bindIcoVos;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}
}
