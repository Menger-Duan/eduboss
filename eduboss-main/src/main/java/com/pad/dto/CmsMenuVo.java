package com.pad.dto;

import com.pad.common.CmsUseType;

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
public class CmsMenuVo implements java.io.Serializable {

	private Integer id;
	private CmsUseType useType;
	private String level;
	private Integer parentId;
	private String name;
	private String ico;
	private Integer orderNum=999;
	private String status;
	private String createUser;
	private String modifyUser;
	private Date createTime;
	private Date modifyTime;
	private Integer allCanSee=0;
	private String grayIco;
	private Integer showContentNum=4;
	private List<CmsMenuAuthVo> auths;
	private String orgId;
	private String orgName;
	private Integer showType=0;

	private List<CmsMenuVo> sonMenu;

	public List<CmsMenuVo> getSonMenu() {
		return sonMenu;
	}

	public void setSonMenu(List<CmsMenuVo> sonMenu) {
		this.sonMenu = sonMenu;
	}

	public Integer getShowContentNum() {
		return showContentNum;
	}

	public void setShowContentNum(Integer showContentNum) {
		this.showContentNum = showContentNum;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public List<CmsMenuAuthVo> getAuths() {
		return auths;
	}

	public void setAuths(List<CmsMenuAuthVo> auths) {
		this.auths = auths;
	}

	public String getGrayIco() {
		return grayIco;
	}

	public void setGrayIco(String grayIco) {
		this.grayIco = grayIco;
	}

	public Integer getAllCanSee() {
		return allCanSee;
	}

	public void setAllCanSee(Integer allCanSee) {
		this.allCanSee = allCanSee;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}
}
