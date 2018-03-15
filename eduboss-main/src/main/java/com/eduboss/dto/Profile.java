package com.eduboss.dto;

import java.io.Serializable;
import java.util.List;

public class Profile implements Serializable{

	private static final long serialVersionUID = 8310093491754840495L;
	private String avatarUrl;
	private Integer stat;
	private String userId;
	private Integer userRank;
	private List<String> userRole;
	private String username;
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public Integer getStat() {
		return stat;
	}
	public void setStat(Integer stat) {
		this.stat = stat;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Integer getUserRank() {
		return userRank;
	}
	public void setUserRank(Integer userRank) {
		this.userRank = userRank;
	}
	public List<String> getUserRole() {
		return userRole;
	}
	public void setUserRole(List<String> userRole) {
		this.userRole = userRole;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	
}
