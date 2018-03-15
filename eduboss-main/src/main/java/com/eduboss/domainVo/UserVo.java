package com.eduboss.domainVo;

import javax.persistence.Transient;

import com.eduboss.dto.SelectOptionResponse.NameValue;

public class UserVo implements NameValue{

	private String userId;
	private String name;
	private String contact;
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	@Transient
	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return this.userId;
	}
	
}
