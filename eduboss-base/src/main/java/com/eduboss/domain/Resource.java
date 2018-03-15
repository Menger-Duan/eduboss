package com.eduboss.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.security.core.GrantedAuthority;

import com.eduboss.common.RoleResourceType;
import com.eduboss.dto.SelectOptionResponse.NameValue;

/**
 * Resource entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "resource")
public class Resource implements java.io.Serializable, NameValue, GrantedAuthority {

	// Fields

	private String id;
	private String parentId;
	private String rname;
	private RoleResourceType rtype;
	private String rtag;
	private String rurl;
	private String rorder;
	private Integer hasChildren;
	private String title;
	private String rcontent;
	private DataDict stateType;
	
	private String createTime;
	private String createUser;

	private String newUrl;

	
	private Set<Role> roles = new HashSet<Role>();
	
	// Constructors

	/** default constructor */
	public Resource() {
	}
	
	public Resource(String id) {
		this.id = id;
	}

	/** full constructor */
	public Resource(String id, RoleResourceType rtype, String rname) {
		this.id = id;
		this.rtype = rtype;
		this.rname = rname;
	}

	// Property accessors
	@GenericGenerator(name = "resourceIdGenerator", strategy = "com.eduboss.dto.DispNoGenerator")
	@Id
	@GeneratedValue(generator = "resourceIdGenerator")
	@Column(name = "id", unique = true, nullable = false, length = 32)
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "rType", length = 32)
	public RoleResourceType getRtype() {
		return this.rtype;
	}

	public void setRtype(RoleResourceType rtype) {
		this.rtype = rtype;
	}

	@Column(name = "rName", length = 100)
	public String getRname() {
		return this.rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	/**
	 * @return the rtag
	 */
	@Column(name = "RTAG")
	public String getRtag() {
		return rtag;
	}

	/**
	 * @param rtag the rtag to set
	 */
	public void setRtag(String rtag) {
		this.rtag = rtag;
	}

	/**
	 * @return the rurl
	 */
	@Column(name = "RURL")
	public String getRurl() {
		return rurl;
	}

	/**
	 * @param rurl the rurl to set
	 */
	public void setRurl(String rurl) {
		this.rurl = rurl;
	}

	/**
	 * @return the rorder
	 */
	@Column(name = "RORDER")
	public String getRorder() {
		return rorder;
	}

	/**
	 * @param rorder the rorder to set
	 */
	public void setRorder(String rorder) {
		this.rorder = rorder;
	}

	/**
	 * @return the hasChildren
	 */
	@Column(name = "HAS_CHILDREN")
	public Integer getHasChildren() {
		return hasChildren;
	}

	/**
	 * @param hasChildren the hasChildren to set
	 */
	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**
	 * @return the parentId
	 */
	@Column(name = "PARENT_ID")
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "TITLE")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "RCONTENT")
    public String getRcontent() {
		return rcontent;
	}

	public void setRcontent(String rcontent) {
		this.rcontent = rcontent;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STATE_TYPE")
	@NotFound(action=NotFoundAction.IGNORE)
	public DataDict getStateType() {
		return stateType;
	}

	public void setStateType(DataDict stateType) {
		this.stateType = stateType;
	}

	
	@Column(name = "create_time",length=32)
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	@Column(name = "create_user",length=32)
	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(name = "role_resource", joinColumns = { @JoinColumn(name = "resourceID") }, inverseJoinColumns = { @JoinColumn(name = "roleID") })
	@Transient
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Transient
	public String getName() {
		return this.rname;
	}

	@Transient
	public String getValue() {
		return this.id;
	}

	@Transient
	public String getAuthority() {
		return this.rurl;
	}


	@Column(name = "new_url",length=200)
	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}
}