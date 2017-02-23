package com.flockinger.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "id", "name" }) }
,indexes={@Index(columnList="name")})
public class Tag extends BaseModel{
	
	@NotNull
	@Max(150)
	@Column(columnDefinition="VARCHAR(150)")
	private String name;
	
	@ManyToMany
	private List<Post> posts;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Post> getPosts() {
		return posts;
	}
	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
}
