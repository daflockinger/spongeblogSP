package com.flockinger.spongeblogSP.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * UserEditDTO
 */
public class UserEditDTO {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("login")
  private String login = null;

  @SerializedName("password")
  private String password = null;

  @SerializedName("nickName")
  private String nickName = null;

  @SerializedName("email")
  private String email = null;

  @SerializedName("registered")
  private Long registered = null;

  /**
   * Gets or Sets roles
   */
  public enum RolesEnum {
    @SerializedName("ADMIN")
    ADMIN("ADMIN"),
    
    @SerializedName("AUTHOR")
    AUTHOR("AUTHOR"),
    
    @SerializedName("LOCKED")
    LOCKED("LOCKED");

    private String value;

    RolesEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @SerializedName("roles")
  private List<RolesEnum> roles = new ArrayList<RolesEnum>();

  public UserEditDTO id(Long id) {
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

  public UserEditDTO login(String login) {
    this.login = login;
    return this;
  }

   /**
   * Login name of the User.
   * @return login
  **/
  @ApiModelProperty(example = "null", value = "Login name of the User.")
  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public UserEditDTO password(String password) {
    this.password = password;
    return this;
  }

   /**
   * Password hash of the User.
   * @return password
  **/
  @ApiModelProperty(example = "null", value = "Password hash of the User.")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public UserEditDTO nickName(String nickName) {
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

  public UserEditDTO email(String email) {
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

  public UserEditDTO registered(Long registered) {
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

  public UserEditDTO roles(List<RolesEnum> roles) {
    this.roles = roles;
    return this;
  }

  public UserEditDTO addRolesItem(RolesEnum rolesItem) {
    this.roles.add(rolesItem);
    return this;
  }

   /**
   * Roles of the User (authorizations).
   * @return roles
  **/
  @ApiModelProperty(example = "null", value = "Roles of the User (authorizations).")
  public List<RolesEnum> getRoles() {
    return roles;
  }

  public void setRoles(List<RolesEnum> roles) {
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
    return Objects.equals(this.id, userEditDTO.id) &&
        Objects.equals(this.login, userEditDTO.login) &&
        Objects.equals(this.password, userEditDTO.password) &&
        Objects.equals(this.nickName, userEditDTO.nickName) &&
        Objects.equals(this.email, userEditDTO.email) &&
        Objects.equals(this.registered, userEditDTO.registered) &&
        Objects.equals(this.roles, userEditDTO.roles);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, login, password, nickName, email, registered, roles);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserEditDTO {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

