package com.flockinger.spongeblogSP.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class PostTagsAud {
	
	@Id
	private Integer rev;
	
	@Column(name="posts_id")
	private Long postsId;

	@Column(name="tags_id")
	private Long tagId;
	
	private Byte revtype;
}
