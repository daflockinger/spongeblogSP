package com.flockinger.spongeblogSP.dto;

import java.util.List;

import com.flockinger.spongeblogSP.dto.link.PostLink;

public class TagPostsDTO {
	private String name;
	private List<PostLink> posts;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<PostLink> getPosts() {
		return posts;
	}
	public void setPosts(List<PostLink> posts) {
		this.posts = posts;
	}
}
