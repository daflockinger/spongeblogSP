package com.flockinger.spongeblogSP.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.flockinger.spongeblogSP.model.enums.BlogStatus;

@Entity
@Audited
public class Blog extends BaseModel{
	
	@NotNull
	private String name;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private BlogStatus status;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@MapKeyColumn(columnDefinition="VARCHAR(150)",length=150)
	private Map<String,String> settings;
	
	
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
