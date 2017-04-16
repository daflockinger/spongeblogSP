package com.flockinger.spongeblogSP.dto.link;

public class CategoryLink {
	private Long id;
	
	public CategoryLink(){}
	
	public CategoryLink(Long id){
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
