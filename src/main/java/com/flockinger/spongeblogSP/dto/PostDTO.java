package com.flockinger.spongeblogSP.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * PostDTO
 */
public class PostDTO {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("title")
  private String title = null;

  @SerializedName("content")
  private String content = null;

  @SerializedName("created")
  private Long created = null;

  @SerializedName("modified")
  private Long modified = null;

  @SerializedName("status")
  private PostStatus status = null;

  @SerializedName("author")
  private UserInfoDTO author = null;

  @SerializedName("category")
  private CategoryDTO category = null;

  @SerializedName("tags")
  private List<TagDTO> tags = new ArrayList<TagDTO>();

  public PostDTO id(Long id) {
    this.id = id;
    return this;
  }

   /**
   * Unique identifier.
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "Unique identifier.")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PostDTO title(String title) {
    this.title = title;
    return this;
  }

   /**
   * Title of the post.
   * @return title
  **/
  @ApiModelProperty(example = "null", value = "Title of the post.")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public PostDTO content(String content) {
    this.content = content;
    return this;
  }

   /**
   * Post text/html content.
   * @return content
  **/
  @ApiModelProperty(example = "null", value = "Post text/html content.")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public PostDTO created(Long created) {
    this.created = created;
    return this;
  }

   /**
   * Creation date of Post in long.
   * @return created
  **/
  @ApiModelProperty(example = "null", value = "Creation date of Post in long.")
  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

  public PostDTO modified(Long modified) {
    this.modified = modified;
    return this;
  }

   /**
   * Modification date of Post in long.
   * @return modified
  **/
  @ApiModelProperty(example = "null", value = "Modification date of Post in long.")
  public Long getModified() {
    return modified;
  }

  public void setModified(Long modified) {
    this.modified = modified;
  }

  public PostDTO status(PostStatus status) {
    this.status = status;
    return this;
  }

   /**
   * Display status of the Post.
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "Display status of the Post.")
  public PostStatus getStatus() {
    return status;
  }

  public void setStatus(PostStatus status) {
    this.status = status;
  }

  public PostDTO author(UserInfoDTO author) {
    this.author = author;
    return this;
  }

   /**
   * Get author
   * @return author
  **/
  @ApiModelProperty(example = "null", value = "")
  public UserInfoDTO getAuthor() {
    return author;
  }

  public void setAuthor(UserInfoDTO author) {
    this.author = author;
  }

  public PostDTO category(CategoryDTO category) {
    this.category = category;
    return this;
  }

   /**
   * Get category
   * @return category
  **/
  @ApiModelProperty(example = "null", value = "")
  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

  public PostDTO tags(List<TagDTO> tags) {
    this.tags = tags;
    return this;
  }

  public PostDTO addTagsItem(TagDTO tagsItem) {
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Tags of Post.
   * @return tags
  **/
  @ApiModelProperty(example = "null", value = "Tags of Post.")
  public List<TagDTO> getTags() {
    return tags;
  }

  public void setTags(List<TagDTO> tags) {
    this.tags = tags;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostDTO postDTO = (PostDTO) o;
    return Objects.equals(this.id, postDTO.id) &&
        Objects.equals(this.title, postDTO.title) &&
        Objects.equals(this.content, postDTO.content) &&
        Objects.equals(this.created, postDTO.created) &&
        Objects.equals(this.modified, postDTO.modified) &&
        Objects.equals(this.status, postDTO.status) &&
        Objects.equals(this.author, postDTO.author) &&
        Objects.equals(this.category, postDTO.category) &&
        Objects.equals(this.tags, postDTO.tags);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, content, created, modified, status, author, category, tags);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostDTO {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    modified: ").append(toIndentedString(modified)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
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