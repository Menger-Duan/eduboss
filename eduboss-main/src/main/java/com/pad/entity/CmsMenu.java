package com.pad.entity;

import com.pad.common.CmsUseType;

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
@Table(name= "cms_menu")
public class CmsMenu implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

	private Integer id;
	private CmsUseType useType;
	private String level;
	private Integer parentId;
	private String name;
	private String ico;
	private Integer orderNum;
	private String status;
	private String createUser;
	private String modifyUser;
	private Date createTime;
	private Date modifyTime;
	private Integer allCanSee;
	private String grayIco;
	private Integer showContentNum;
	private Integer showType;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name ="use_type")
	@Enumerated(EnumType.STRING)
	public CmsUseType getUseType() {
		return useType;
	}

	public void setUseType(CmsUseType useType) {
		this.useType = useType;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	@Column(name ="parent_id")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	@Column(name ="order_num")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Column(name="all_can_see")
	public Integer getAllCanSee() {
		return allCanSee;
	}

	public void setAllCanSee(Integer allCanSee) {
		this.allCanSee = allCanSee;
	}

	@Column(name="gray_ico")
	public String getGrayIco() {
		return grayIco;
	}

	public void setGrayIco(String grayIco) {
		this.grayIco = grayIco;
	}

	@Column(name="show_content_num")
	public Integer getShowContentNum() {
		return showContentNum;
	}

	public void setShowContentNum(Integer showContentNum) {
		this.showContentNum = showContentNum;
	}

	@Column(name = "show_type")
	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}
}
