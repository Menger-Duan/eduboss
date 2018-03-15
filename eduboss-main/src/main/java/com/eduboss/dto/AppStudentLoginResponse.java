package com.eduboss.dto;

import java.util.List;

import com.eduboss.domain.MobileUser;
import com.eduboss.domain.Resource;
import com.eduboss.domain.User;
import com.eduboss.domainVo.StudentVo;


/**
 * @author ban
 *
 */
public class AppStudentLoginResponse extends Response {
	
	 private StudentVo student;
	
	 private String token;
	 
	 private MobileUser mobileUser;  // 有一个公用的miblieUser
	 
	public StudentVo getStudent() {
		return student;
	}

	public void setStudent(StudentVo vo) {
		this.student = vo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public MobileUser getMobileUser() {
		return mobileUser;
	}

	public void setMobileUser(MobileUser mobileUser) {
		this.mobileUser = mobileUser;
	}

	
	
}
