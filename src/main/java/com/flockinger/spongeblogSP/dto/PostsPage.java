package com.flockinger.spongeblogSP.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.Valid;

/**
 * PostsPage
 */

public class PostsPage   {
  @JsonProperty("previewPosts")
  private List<PostPreviewDTO> previewPosts = null;

  @JsonProperty("hasNext")
  private Boolean hasNext = null;

  @JsonProperty("hasPrevious")
  private Boolean hasPrevious = null;

  @JsonProperty("totalPages")
  private Integer totalPages = null;

   /**
   * Preview posts
   * @return previewPosts
  **/
  @ApiModelProperty(value = "Preview posts")

  @Valid

  public List<PostPreviewDTO> getPreviewPosts() {
    return previewPosts;
  }

  public void setPreviewPosts(List<PostPreviewDTO> previewPosts) {
    this.previewPosts = previewPosts;
  }

   /**
   * Is a next page existing.
   * @return hasNext
  **/
  @ApiModelProperty(value = "Is a next page existing.")


  public Boolean getHasNext() {
    return hasNext;
  }

  public void setHasNext(Boolean hasNext) {
    this.hasNext = hasNext;
  }

   /**
   * Is a previous page existing.
   * @return hasPrevious
  **/
  @ApiModelProperty(value = "Is a previous page existing.")


  public Boolean getHasPrevious() {
    return hasPrevious;
  }

  public void setHasPrevious(Boolean hasPrevious) {
    this.hasPrevious = hasPrevious;
  }

   /**
   * Total count of pages.
   * @return totalPages
  **/
  @ApiModelProperty(value = "Total count of pages.")


  public Integer getTotalPages() {
    return totalPages;
  }

  public void setTotalPages(Integer totalPages) {
    this.totalPages = totalPages;
  }
 
}
