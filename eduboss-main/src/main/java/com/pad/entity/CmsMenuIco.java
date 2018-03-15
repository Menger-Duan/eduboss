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
@Table(name= "cms_menu_ico")
public class CmsMenuIco implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Version
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

	@Column(name ="create_time",updatable = false,insertable = false)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name ="modify_time",updatable = false,insertable = false)
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

	@Column(name ="publish_user")
	public String getPublishUser() {
		return publishUser;
	}

	public void setPublishUser(String publishUser) {
		this.publishUser = publishUser;
	}

	@Column(name ="modify_user")
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Column(name ="menu_level",updatable = false)
	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

	@Column(name = "menu_id")
	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}
}
