package com.flockinger.spongeblogSP.dto;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public class BlogUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3661101676911012333L;
	
	private String password;
	private String username;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean isEnabled = false;
	private List<BlogAuthority> authorities;
	
	
	public boolean isEnabled() {
		return isEnabled;
	}
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public List<BlogAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<BlogAuthority> authorities) {
		this.authorities = authorities;
	}
}
