package com.eduboss.domain;

import java.util.Collection;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/*create by mfxu*/
//@Entity
public class UserDetailsImpl implements UserDetails {

	private User user;
	
	public UserDetailsImpl(User user){
		this.user = user;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return user.getAuthorities();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getAccount();
	}

	@Override
	public boolean isAccountNonExpired() {
//		return user.getAccountExpired() != null ? user.getAccountExpired() == 0 : false;
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
//		return user.getAccountLocked() != null ? user.getAccountLocked() == 0 : false;
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (user.getEnableFlg() != null && user.getEnableFlg() == 1) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	
}
