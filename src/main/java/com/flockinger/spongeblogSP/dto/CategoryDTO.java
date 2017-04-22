package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * CategoryDTO
 */
public class CategoryDTO {

	@SerializedName("id")
	private Long id = null;

	@SerializedName("name")
	private String name = null;

	@SerializedName("parentId")
	private Long parentId = null;

	@SerializedName("rank")
	private Integer rank = null;

	public CategoryDTO id(Long id) {
		this.id = id;
		return this;
	}

	/**
	 * Unique identifier.
	 * 
	 * @return id
	 **/
	@ApiModelProperty(example = "null", value = "Unique identifier.")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CategoryDTO name(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Category display name.
	 * 
	 * @return name
	 **/
	@ApiModelProperty(example = "null", value = "Category display name.")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CategoryDTO parentId(Long parentId) {
		this.parentId = parentId;
		return this;
	}

	/**
	 * Id of parent Category.
	 * 
	 * @return parentId
	 **/
	@ApiModelProperty(example = "null", value = "Id of parent Category.")
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public CategoryDTO rank(Integer rank) {
		this.rank = rank;
		return this;
	}

	/**
	 * Determines position of Category.
	 * 
	 * @return rank
	 **/
	@ApiModelProperty(example = "null", value = "Determines position of Category.")
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
		return Objects.equals(this.id, categoryDTO.id) && Objects.equals(this.name, categoryDTO.name)
				&& Objects.equals(this.parentId, categoryDTO.parentId) && Objects.equals(this.rank, categoryDTO.rank);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, parentId, rank);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CategoryDTO {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
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
