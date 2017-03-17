package com.flockinger.spongeblogSP.dto;

public class TagDTO extends BaseDTO{
	private String name;
	private Long postCount;
	
	public Long getPostCount() {
		return postCount;
	}
	public void setPostCount(Long postCount) {
		this.postCount = postCount;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
