package com.flockinger.spongeblogSP.dto;

import java.util.Date;
import java.util.List;

import com.flockinger.spongeblogSP.model.enums.PostStatus;

public class PostDTO extends BaseDTO{
	private String title;
	private String content;
	private Date created;
	private Date modified;
	
	private PostStatus status;
	
	private UserInfoDTO author;
	
	private CategoryDTO category;
	
	private List<TagDTO> tags;

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public PostStatus getStatus() {
		return status;
	}

	public void setStatus(PostStatus status) {
		this.status = status;
	}

	public UserInfoDTO getAuthor() {
		return author;
	}

	public void setAuthor(UserInfoDTO author) {
		this.author = author;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public List<TagDTO> getTags() {
		return tags;
	}

	public void setTags(List<TagDTO> tags) {
		this.tags = tags;
	}
}
