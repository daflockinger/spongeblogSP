package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * UserInfoDTO
 */
public class UserInfoDTO {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("nickName")
  private String nickName = null;

  @SerializedName("email")
  private String email = null;

  @SerializedName("registered")
  private Long registered = null;

  public UserInfoDTO id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier.
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "Unique identifier.")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UserInfoDTO nickName(String nickName) {
    this.nickName = nickName;
    return this;
  }

   /**
   * Display nickname of the User.
   * @return nickName
  **/
  @ApiModelProperty(example = "null", value = "Display nickname of the User.")
  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public UserInfoDTO email(String email) {
    this.email = email;
    return this;
  }

   /**
   * Email of User.
   * @return email
  **/
  @ApiModelProperty(example = "null", value = "Email of User.")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserInfoDTO registered(Long registered) {
    this.registered = registered;
    return this;
  }

   /**
   * Registration date of User in long.
   * @return registered
  **/
  @ApiModelProperty(example = "null", value = "Registration date of User in long.")
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
    return Objects.equals(this.id, userInfoDTO.id) &&
        Objects.equals(this.nickName, userInfoDTO.nickName) &&
        Objects.equals(this.email, userInfoDTO.email) &&
        Objects.equals(this.registered, userInfoDTO.registered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nickName, email, registered);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserInfoDTO {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

