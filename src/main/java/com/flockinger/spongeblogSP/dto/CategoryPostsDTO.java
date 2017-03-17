package com.flockinger.spongeblogSP.dto;

import java.util.List;

import com.flockinger.spongeblogSP.dto.link.CategoryLink;
import com.flockinger.spongeblogSP.dto.link.PostLink;

public class CategoryPostsDTO {
	
	private String name;
	private CategoryLink parent;
	private List<CategoryLink> subCategories;
	private List<PostLink> posts;
	private Integer rank;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CategoryLink getParent() {
		return parent;
	}

	public void setParent(CategoryLink parent) {
		this.parent = parent;
	}

	public List<CategoryLink> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<CategoryLink> subCategories) {
		this.subCategories = subCategories;
	}

	public List<PostLink> getPosts() {
		return posts;
	}

	public void setPosts(List<PostLink> posts) {
		this.posts = posts;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
}
