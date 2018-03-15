package com.eduboss.domainVo;

import org.springframework.security.core.GrantedAuthority;



public class ResourceGrantedVo implements  GrantedAuthority {

	private String id;
	private String rurl;//鉴权应该只需要这个

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getRurl() {
		return rurl;
	}
	public void setRurl(String rurl) {
		this.rurl = rurl;
	}
	
	@Override
	public String getAuthority() {
		return this.rurl;
	}
	
}
