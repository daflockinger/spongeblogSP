package com.flockinger.spongeblogSP.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

@Entity
@Audited
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) }
,indexes={@Index(columnList="name")})
public class Tag extends BaseModel{
	
	@NotNull
	@Length(max=150)
	private String name;
	
	@ManyToMany(mappedBy="tags",fetch = FetchType.LAZY)
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
