package com.eduboss.dto;

import java.util.List;

import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Resource;
import com.eduboss.domain.User;
import com.eduboss.domainVo.ResourceVo;


/**
 * @author ban
 *
 */
public class LoginResponse extends Response {
	
	 private User user;
	 
	 private List<ResourceVo> menuList;
	
	 private String token;
	 
	 private MobileUser mobileUser;  // 有一个公用的miblieUser

	 private String employeeNo;
	 
	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	public List<ResourceVo> getMenuList() {
		return menuList;
	}

	public void setMenuList(List<ResourceVo> menuList) {
		this.menuList = menuList;
	}

	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

	public String getEmployeeNo() {
		return employeeNo;
	}

	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}
}
