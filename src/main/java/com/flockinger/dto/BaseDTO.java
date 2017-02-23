package com.flockinger.dto;

import javax.validation.constraints.NotNull;

public class BaseDTO {
	
	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
