package com.eduboss.domainVo;

import org.springframework.security.core.GrantedAuthority;

import com.eduboss.dto.SelectOptionResponse.NameValue;



public class ResourceVo implements  NameValue, GrantedAuthority {

	private String id;
	private String rname;
	private String rurl;
	private String title;
	private String rcontent;
	private String stateType;
	private String stateTypeName;

	private String createTime;
	private String createUserId;

	private String parentId;
	private String rtype;
	private String rtag;
	private String rorder;
	private Integer hasChildren;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getRurl() {
		return rurl;
	}
	public void setRurl(String rurl) {
		this.rurl = rurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRcontent() {
		return rcontent;
	}
	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}
	public String getStateType() {
		return stateType;
	}
	public void setStateType(String stateType) {
		this.stateType = stateType;
	}
	public String getStateTypeName() {
		return stateTypeName;
	}
	public void setStateTypeName(String stateTypeName) {
		this.stateTypeName = stateTypeName;
	}

	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUserId() {
		return createUserId;
	}
	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}



	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getRtype() {
		return rtype;
	}
	public void setRtype(String rtype) {
		this.rtype = rtype;
	}
	public String getRtag() {
		return rtag;
	}
	public void setRtag(String rtag) {
		this.rtag = rtag;
	}
	public String getRorder() {
		return rorder;
	}
	public void setRorder(String rorder) {
		this.rorder = rorder;
	}
	public Integer getHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}
	@Override
	public String getAuthority() {
		return this.rurl;
	}
	@Override
	public String getName() {
		return this.rname;
	}
	@Override
	public String getValue() {
		return this.id;
	}


}
