package com.flockinger.spongeblogSP.dto;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

import io.swagger.annotations.ApiModelProperty;
/**
 * PostDTO
 */
public class PostDTO  extends ResourceSupport {
 
  @JsonProperty("postId")
  private Long postId = null;

  @JsonProperty("title")
  @NotEmpty
  private String title = null;

  @JsonProperty("content")
  @NotNull
  private String content = null;

  @JsonProperty("created")
  @NotNull
  @Min(0)
  private Long created = null;

  @JsonProperty("modified")
  @NotNull
  @Min(0)
  private Long modified = null;

  @JsonProperty("status")
  @NotNull
  private PostStatus status = null;

  @JsonProperty("author")
  private UserInfoDTO author = null;

  @JsonProperty("category")
  @NotNull
  private CategoryDTO category = null;

  @JsonProperty("tags")
  @NotNull
  private List<TagDTO> tags = new ArrayList<TagDTO>();


   /**
   * Unique identifier.
   * @return postId
  **/
  @ApiModelProperty(value = "Unique identifier.")
  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

   /**
   * The title of the Blog post.
   * @return title
  **/
  @ApiModelProperty(value = "The title of the Blog post.")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

   /**
   * Post text/html content.
   * @return content
  **/
  @ApiModelProperty(value = "Post text/html content.")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

   /**
   * Creation date of Post in long.
   * @return created
  **/
  @ApiModelProperty(value = "Creation date of Post in long.")
  public Long getCreated() {
    return created;
  }

  public void setCreated(Long created) {
    this.created = created;
  }

   /**
   * Modification date of Post in long.
   * @return modified
  **/
  @ApiModelProperty(value = "Modification date of Post in long.")
  public Long getModified() {
    return modified;
  }

  public void setModified(Long modified) {
    this.modified = modified;
  }

   /**
   * Display status of the Post.
   * @return status
  **/
  @ApiModelProperty(value = "Display status of the Post.")
  public PostStatus getStatus() {
    return status;
  }

  public void setStatus(PostStatus status) {
    this.status = status;
  }

   /**
   * Get author
   * @return author
  **/
  @ApiModelProperty(value = "")
  public UserInfoDTO getAuthor() {
    return author;
  }

  public void setAuthor(UserInfoDTO author) {
    this.author = author;
  }

   /**
   * Get category
   * @return category
  **/
  @ApiModelProperty(value = "")
  public CategoryDTO getCategory() {
    return category;
  }

  public void setCategory(CategoryDTO category) {
    this.category = category;
  }

   /**
   * Tags of Post.
   * @return tags
  **/
  @ApiModelProperty(value = "Tags of Post.")
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
    return Objects.equals(this.postId, postDTO.postId) &&
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
    return Objects.hash(postId, title, content, created, modified, status, author, category, tags);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostDTO {\n");
    
    sb.append("    postId: ").append(toIndentedString(postId)).append("\n");
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
