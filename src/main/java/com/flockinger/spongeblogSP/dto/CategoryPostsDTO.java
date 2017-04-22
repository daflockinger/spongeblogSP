package com.flockinger.spongeblogSP.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.flockinger.spongeblogSP.dto.link.CategoryLink;
import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * CategoryPostsDTO
 */
public class CategoryPostsDTO {
	@SerializedName("id")
	private Long id = null;

	@SerializedName("name")
	private String name = null;

	@SerializedName("parent")
	private Long parent = null;

	@SerializedName("subCategories")
	private List<CategoryLink> subCategories = new ArrayList<CategoryLink>();

	@SerializedName("posts")
	private String posts = null;

	@SerializedName("rank")
	private Integer rank = null;

	public CategoryPostsDTO id(Long id) {
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

	public CategoryPostsDTO name(String name) {
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

	public CategoryPostsDTO parent(Long parent) {
		this.parent = parent;
		return this;
	}

	/**
	 * Id of parent Category.
	 * 
	 * @return parent
	 **/
	@ApiModelProperty(example = "null", value = "Id of parent Category.")
	public Long getParent() {
		return parent;
	}

	public void setParent(Long parent) {
		this.parent = parent;
	}

	public CategoryPostsDTO subCategories(List<CategoryLink> subCategories) {
		this.subCategories = subCategories;
		return this;
	}

	public CategoryPostsDTO addSubCategoriesItem(CategoryLink subCategoriesItem) {
		this.subCategories.add(subCategoriesItem);
		return this;
	}

	/**
	 * Links to child Categories.
	 * 
	 * @return subCategories
	 **/
	@ApiModelProperty(example = "null", value = "Links to child Categories.")
	public List<CategoryLink> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<CategoryLink> subCategories) {
		this.subCategories = subCategories;
	}

	public CategoryPostsDTO posts(String posts) {
		this.posts = posts;
		return this;
	}

	/**
	 * Link to posts within category.
	 * 
	 * @return posts
	 **/
	@ApiModelProperty(example = "null", value = "Link to posts within category.")
	public String getPosts() {
		return posts;
	}

	public void setPosts(String posts) {
		this.posts = posts;
	}

	public CategoryPostsDTO rank(Integer rank) {
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
		CategoryPostsDTO categoryPostsDTO = (CategoryPostsDTO) o;
		return Objects.equals(this.id, categoryPostsDTO.id) && Objects.equals(this.name, categoryPostsDTO.name)
				&& Objects.equals(this.parent, categoryPostsDTO.parent)
				&& Objects.equals(this.subCategories, categoryPostsDTO.subCategories)
				&& Objects.equals(this.posts, categoryPostsDTO.posts)
				&& Objects.equals(this.rank, categoryPostsDTO.rank);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, parent, subCategories, posts, rank);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class CategoryPostsDTO {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    name: ").append(toIndentedString(name)).append("\n");
		sb.append("    parent: ").append(toIndentedString(parent)).append("\n");
		sb.append("    subCategories: ").append(toIndentedString(subCategories)).append("\n");
		sb.append("    posts: ").append(toIndentedString(posts)).append("\n");
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
