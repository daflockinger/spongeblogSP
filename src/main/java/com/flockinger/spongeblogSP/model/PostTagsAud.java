package com.flockinger.spongeblogSP.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PostTagsAud {
	
	@Id
	private Integer rev;
	
	@Column(name="posts_id")
	private Long postsId;

	@Column(name="tags_id")
	private Long tagId;
	
	private Byte revtype;
	
	public Integer getRev() {
		return rev;
	}
	public void setRev(Integer rev) {
		this.rev = rev;
	}
	public Byte getRevtype() {
		return revtype;
	}
	public void setRevtype(Byte revtype) {
		this.revtype = revtype;
	}
	public Long getPostsId() {
		return postsId;
	}
	public void setPostsId(Long postsId) {
		this.postsId = postsId;
	}
	public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
}
