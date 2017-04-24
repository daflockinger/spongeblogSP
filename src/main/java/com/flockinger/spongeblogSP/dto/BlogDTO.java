package com.flockinger.spongeblogSP.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.flockinger.spongeblogSP.model.enums.BlogStatus;
import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * BlogDTO
 */
public class BlogDTO extends ResourceSupport {

	@SerializedName("name")
	@NotEmpty
	private String name = null;

	@SerializedName("status")
	@NotNull
	private BlogStatus status = null;

	@SerializedName("settings")
	@NotNull
	private Map<String, String> settings = new HashMap<String, String>();

	public BlogDTO name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Name of the Blog.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(example = "null", value = "Name of the Blog.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BlogDTO status(BlogStatus status) {
		this.status = status;
		return this;
	}

	/**
	 * Status of the Blog.
	 * 
	 * @return status
	 **/
	@ApiModelProperty(example = "null", value = "Status of the Blog.")
	public BlogStatus getStatus() {
		return status;
	}

	public void setStatus(BlogStatus status) {
		this.status = status;
	}

	public BlogDTO settings(Map<String, String> settings) {
		this.settings = settings;
		return this;
	}

	public BlogDTO putSettingsItem(String key, String settingsItem) {
		this.settings.put(key, settingsItem);
		return this;
	}

	/**
	 * Settings map of the Blog.
	 * 
	 * @return settings
	 **/
	@ApiModelProperty(example = "null", value = "Settings map of the Blog.")
	public Map<String, String> getSettings() {
		return settings;
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		BlogDTO blogDTO = (BlogDTO) o;
		return Objects.equals(this.name, blogDTO.name) && Objects.equals(this.status, blogDTO.status)
				&& Objects.equals(this.settings, blogDTO.settings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, status, settings);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class BlogDTO {\n");

		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    status: ").append(toIndentedString(status)).append("\n");
		sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * Convert the given object to string with each line indented by 4 spaces
	 * (except the first line).
	 */
	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
