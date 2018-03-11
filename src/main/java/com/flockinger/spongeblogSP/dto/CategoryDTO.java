package com.flockinger.spongeblogSP.dto;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;

/**
 * CategoryDTO
 */
public class CategoryDTO {

  private Long categoryId = null;

  private Long pageId = null;
  
  @NotEmpty
  private String name = null;

  private List<CategoryDTO> children;

  private Long parentId = null;

  private Integer rank = null;

  
  /**
   * ID of the assigned page if it's a page-category (optional field).
   * @return pageId
  **/
  @ApiModelProperty(value = "ID of the assigned page if it's a page-category (optional field).")
  public Long getPageId() {
    return pageId;
  }

  public void setPageId(Long pageId) {
    this.pageId = pageId;
  }

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
   * Children of category
   * 
   * @return children
   */
  public List<CategoryDTO> getChildren() {
    return children;
  }

  /**
   * Children of category
   * 
   * @param children
   */
  public void setChildren(List<CategoryDTO> children) {
    this.children = children;
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
}
