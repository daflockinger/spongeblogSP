package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
/**
 * TagDTO
 */

public class TagDTO  extends ResourceSupport {
  @JsonProperty("tagId")
  private Long tagId = null;

  @JsonProperty("name")
  @NotEmpty
  private String name = null;

  public TagDTO tagId(Long tagId) {
    this.tagId = tagId;
    return this;
  }

   /**
   * Unique identifier.
   * @return tagId
  **/
  @ApiModelProperty(value = "Unique identifier.")
  public Long getTagId() {
    return tagId;
  }

  public void setTagId(Long tagId) {
    this.tagId = tagId;
  }

  public TagDTO name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Tag name.
   * @return name
  **/
  @ApiModelProperty(value = "Tag name.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TagDTO tagDTO = (TagDTO) o;
    return Objects.equals(this.tagId, tagDTO.tagId) &&
        Objects.equals(this.name, tagDTO.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tagId, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TagDTO {\n");
    
    sb.append("    tagId: ").append(toIndentedString(tagId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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
