package com.flockinger.spongeblogSP.dto;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.flockinger.spongeblogSP.model.enums.PostStatus;

import io.swagger.annotations.ApiModelProperty;
/**
 * PostDTO
 */
public class PostDTO  extends ResourceSupport {
 
  private Long postId = null;

  @NotEmpty
  private String title = null;

  @NotNull
  private String content = null;

  @NotNull
  private Date created = null;

  @NotNull
  private Date modified = null;

  @NotNull
  private PostStatus status = null;

  private UserInfoDTO author = null;

  @NotNull
  private CategoryDTO category = null;

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
  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

   /**
   * Modification date of Post in long.
   * @return modified
  **/
  @ApiModelProperty(value = "Modification date of Post in long.")
  public Date getModified() {
    return modified;
  }

  public void setModified(Date modified) {
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
}
