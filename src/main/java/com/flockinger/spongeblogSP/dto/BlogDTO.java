package com.flockinger.spongeblogSP.dto;

import java.util.Map;

import com.flockinger.spongeblogSP.model.enums.BlogStatus;

public class BlogDTO {
	
	private Long id;
	private String name;
	private BlogStatus status;
	private Map<String,String> settings;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BlogStatus getStatus() {
		return status;
	}
	public void setStatus(BlogStatus status) {
		this.status = status;
	}
	public Map<String, String> getSettings() {
		return settings;
	}
	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}
}
