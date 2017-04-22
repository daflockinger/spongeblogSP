package com.flockinger.spongeblogSP.dto;

import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * TagPostsDTO
 */
public class TagPostsDTO {
  @SerializedName("name")
  private String name = null;

  @SerializedName("posts")
  private String posts = null;

  public TagPostsDTO name(String name) {
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

  public TagPostsDTO posts(String posts) {
    this.posts = posts;
    return this;
  }

   /**
   * Link to related Posts.
   * @return posts
  **/
  @ApiModelProperty(example = "null", value = "Link to related Posts.")
  public String getPosts() {
    return posts;
  }

  public void setPosts(String posts) {
    this.posts = posts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TagPostsDTO tagPostsDTO = (TagPostsDTO) o;
    return Objects.equals(this.name, tagPostsDTO.name) &&
        Objects.equals(this.posts, tagPostsDTO.posts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, posts);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TagPostsDTO {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    posts: ").append(toIndentedString(posts)).append("\n");
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

