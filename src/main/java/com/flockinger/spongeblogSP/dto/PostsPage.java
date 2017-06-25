package com.flockinger.spongeblogSP.dto;

import java.util.List;

public class PostsPage {
	private List<PostPreviewDTO> previewPosts;
	private Boolean hasNext;
	private Boolean hasPrevious;
	private Integer totalPages;
	
	
	public List<PostPreviewDTO> getPreviewPosts() {
		return previewPosts;
	}
	public void setPreviewPosts(List<PostPreviewDTO> previewPosts) {
		this.previewPosts = previewPosts;
	}
	public Boolean getHasNext() {
		return hasNext;
	}
	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}
	public Boolean getHasPrevious() {
		return hasPrevious;
	}
	public void setHasPrevious(Boolean hasPrevious) {
		this.hasPrevious = hasPrevious;
	}
	public Integer getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}
	
	
}
