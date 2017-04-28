package com.flockinger.spongeblogSP.dto;


import java.util.Objects;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * UserInfoDTO
 */
public class UserInfoDTO  extends ResourceSupport {
  @JsonProperty("userId")
  private Long userId = null;

  @JsonProperty("nickName")
  private String nickName = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("registered")
  private Long registered = null;

  
   /**
   * Unique identifier.
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
   * @return registered
  **/
  @ApiModelProperty(value = "Registration date of User in long.")
  public Long getRegistered() {
    return registered;
  }

  public void setRegistered(Long registered) {
    this.registered = registered;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserInfoDTO userInfoDTO = (UserInfoDTO) o;
    return Objects.equals(this.userId, userInfoDTO.userId) &&
        Objects.equals(this.nickName, userInfoDTO.nickName) &&
        Objects.equals(this.email, userInfoDTO.email) &&
        Objects.equals(this.registered, userInfoDTO.registered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, nickName, email, registered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoDTO {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    nickName: ").append(toIndentedString(nickName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    registered: ").append(toIndentedString(registered)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
