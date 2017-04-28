package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * TagDTO
 */

public class TagDTO extends ResourceSupport {

	private Long tagId = null;

	@NotEmpty
	private String name = null;

	/**
	 * Unique identifier.
	 * 
	 * @return tagId
	 **/
	@ApiModelProperty(value = "Unique identifier.")
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	/**
	 * Tag name.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "Tag name.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
