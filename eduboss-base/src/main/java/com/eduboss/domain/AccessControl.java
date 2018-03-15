package com.eduboss.domain;
// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;


/**
 * AccessControl entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="access_control")
public class AccessControl  implements java.io.Serializable,GrantedAuthority {


    // Fields    

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String userId;
    private String url;
    private Integer permission;
    private String name;
   // private GrantedAuthority authority;

	/** default constructor */
    public AccessControl() {
    }

    
    /** full constructor */
    public AccessControl(String userId, String url, Integer permission,
			String name) {
		this.userId = userId;
		this.url = url;
		this.permission = permission;
		this.name = name;
	}

	@Override
	@Transient
	public String getAuthority() {
		return url;
	}

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false, length=32)
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}

	@Column(name="userId", length=32)
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name="url", length=100)
	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name="permission")
	public Integer getPermission() {
		return permission;
	}


	public void setPermission(Integer permission) {
		this.permission = permission;
	}

	@Column(name="name", length=100)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
/*	public void setAuthority(GrantedAuthority authority) {
		this.authority = authority;
	}*/



}