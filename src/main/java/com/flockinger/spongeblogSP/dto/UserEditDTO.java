package com.flockinger.spongeblogSP.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flockinger.spongeblogSP.model.enums.UserRole;

import io.swagger.annotations.ApiModelProperty;
/**
 * UserEditDTO
 */

public class UserEditDTO  extends ResourceSupport {
  @JsonProperty("userId")
  private Long userId = null;

  @JsonProperty("login")
  @NotEmpty
  private String login = null;

  @JsonProperty("password")
  @Length(min=6)
  private String password = null;

  @JsonProperty("nickName")
  @NotEmpty
  private String nickName = null;

  @JsonProperty("email")
  @NotEmpty
  @Email
  private String email = null;

  @JsonProperty("registered")
  @NotNull
  @Min(0)
  private Long registered = null;


  @JsonProperty("roles")
  private List<UserRole> roles = new ArrayList<UserRole>();


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
   * Login name of the User.
   * @return login
  **/
  @ApiModelProperty(value = "Login name of the User.")
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }


   /**
   * Password hash of the User.
   * @return password
  **/
  @ApiModelProperty(value = "Password hash of the User.")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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


   /**
   * Roles of the User (authorizations).
   * @return roles
  **/
  @ApiModelProperty(value = "Roles of the User (authorizations).")
  public List<UserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<UserRole> roles) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserEditDTO userEditDTO = (UserEditDTO) o;
    return Objects.equals(this.userId, userEditDTO.userId) &&
        Objects.equals(this.login, userEditDTO.login) &&
        Objects.equals(this.password, userEditDTO.password) &&
        Objects.equals(this.nickName, userEditDTO.nickName) &&
        Objects.equals(this.email, userEditDTO.email) &&
        Objects.equals(this.registered, userEditDTO.registered) &&
        Objects.equals(this.roles, userEditDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, login, password, nickName, email, registered, roles);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserEditDTO {\n");
    
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    login: ").append(toIndentedString(login)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    nickName: ").append(toIndentedString(nickName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    registered: ").append(toIndentedString(registered)).append("\n");
    sb.append("    roles: ").append(toIndentedString(roles)).append("\n");
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

