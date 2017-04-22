package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * TagDTO
 */
public class TagDTO {
  @SerializedName("id")
  private Long id = null;

  @SerializedName("name")
  private String name = null;

  public TagDTO id(Long id) {
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

  public TagDTO name(String name) {
    this.name = name;
    return this;
  }

   /**
   * Tag name.
   * @return name
  **/
  @ApiModelProperty(example = "null", value = "Tag name.")
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
    return Objects.equals(this.id, tagDTO.id) &&
        Objects.equals(this.name, tagDTO.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TagDTO {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

