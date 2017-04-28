package com.flockinger.spongeblogSP.dto;

import org.springframework.hateoas.ResourceSupport;

import io.swagger.annotations.ApiModelProperty;

/**
 * UserInfoDTO
 */
public class UserInfoDTO extends ResourceSupport {

	private Long userId = null;
	private String nickName = null;
	private String email = null;
	private Long registered = null;

	/**
	 * Unique identifier.
	 * 
	 * @return userId
	 **/
	@ApiModelProperty(value = "Unique identifier.")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Display nickname of the User.
	 * 
	 * @return nickName
	 **/
	@ApiModelProperty(value = "Display nickname of the User.")
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * Email of User.
	 * 
	 * @return email
	 **/
	@ApiModelProperty(value = "Email of User.")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Registration date of User in long.
	 * 
	 * @return registered
	 **/
	@ApiModelProperty(value = "Registration date of User in long.")
	public Long getRegistered() {
		return registered;
	}

	public void setRegistered(Long registered) {
		this.registered = registered;
	}
}
