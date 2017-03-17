package com.flockinger.spongeblogSP.dto;

import java.util.Date;

public class UserInfoDTO extends BaseDTO{
	private String nickName;
	private String email;
	private Date registered;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getRegistered() {
		return registered;
	}
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
}
