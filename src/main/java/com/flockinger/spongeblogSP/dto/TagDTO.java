package com.flockinger.spongeblogSP.dto;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/**
 * TagDTO
 */

public class TagDTO {

  private Long tagId = null;

  @NotEmpty
  private String name = null;

  /**
   * Unique identifier.
   * 
   * @return tagId
   **/
  @ApiModelProperty(value = "Unique identifier.")
  public Long getTagId() {
    return tagId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
  }

  /**
   * Tag name.
   * 
   * @return name
   **/
  @ApiModelProperty(value = "Tag name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
