package com.flockinger.spongeblogSP.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

@Entity
@Audited
public class Category extends BaseModel {

	@NotNull
	private String name;

	@ManyToOne(optional=true)
	private Category parent;
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY, orphanRemoval = true, cascade={CascadeType.REMOVE})
	private List<Category> subCategories;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.REFRESH })
	private List<Post> posts;

	private Integer rank;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
