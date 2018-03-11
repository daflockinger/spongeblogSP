package com.flockinger.spongeblogSP.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;

/**
 * UserInfoDTO
 */
public class UserInfoDTO {

  private Long userId = null;
  private String nickName = null;
  private String email = null;
  private Date registered = null;

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
  public Date getRegistered() {
    return registered;
  }

  public void setRegistered(Date registered) {
    this.registered = registered;
  }
}
