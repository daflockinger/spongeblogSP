package com.flockinger.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import com.flockinger.model.enums.UserRole;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "login" }) }
,indexes={@Index(columnList="login")})
public class User extends BaseModel{
	
	@NotNull
	@Max(150)
	@Column(columnDefinition="VARCHAR(150)")
	private String login;
	
	@NotNull
	private String password;
	
	private String nickName;
	
	@NotNull
	private Date registered;
	
	@ElementCollection
	@Enumerated(EnumType.STRING)
	private List<UserRole> roles;
	
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public Date getRegistered() {
		return registered;
	}
	public void setRegistered(Date registered) {
		this.registered = registered;
	}
	public List<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}
}
