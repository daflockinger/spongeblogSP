package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

/**
 * CategoryDTO
 */
public class CategoryDTO extends ResourceSupport {

	@JsonProperty("categoryId")
	private Long categoryId = null;

	@JsonProperty("name")
	@NotEmpty
	private String name = null;

	@JsonProperty("parentId")
	private Long parentId = null;

	@JsonProperty("rank")
	private Integer rank = null;


	/**
	 * Unique identifier.
	 * 
	 * @return categoryId
	 **/
	@ApiModelProperty(value = "Unique identifier.")
	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * Category display name.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(value = "Category display name.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Id of parent Category.
	 * 
	 * @return parentId
	 **/
	@ApiModelProperty(value = "Id of parent Category.")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * Determines position of Category.
	 * 
	 * @return rank
	 **/
	@ApiModelProperty(value = "Determines position of Category.")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		CategoryDTO categoryDTO = (CategoryDTO) o;
		return Objects.equals(this.categoryId, categoryDTO.categoryId) && Objects.equals(this.name, categoryDTO.name)
				&& Objects.equals(this.parentId, categoryDTO.parentId) && Objects.equals(this.rank, categoryDTO.rank);
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId, name, parentId, rank);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CategoryDTO {\n");

		sb.append("    categoryId: ").append(toIndentedString(categoryId)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
		sb.append("    rank: ").append(toIndentedString(rank)).append("\n");
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
