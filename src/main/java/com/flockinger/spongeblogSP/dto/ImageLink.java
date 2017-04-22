package com.flockinger.spongeblogSP.dto;


import java.util.Objects;

import com.google.gson.annotations.SerializedName;

import io.swagger.annotations.ApiModelProperty;

/**
 * ImageLink
 */
public class ImageLink {
  @SerializedName("key")
  private String key = null;

  @SerializedName("link")
  private String link = null;

  public ImageLink key(String key) {
    this.key = key;
    return this;
  }

   /**
   * Key of the Image (id + '_' +filename).
   * @return key
  **/
  @ApiModelProperty(example = "null", value = "Key of the Image (id + '_' +filename).")
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public ImageLink link(String link) {
    this.link = link;
    return this;
  }

   /**
   * URL of the Image.
   * @return link
  **/
  @ApiModelProperty(example = "null", value = "URL of the Image.")
  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageLink imageLink = (ImageLink) o;
    return Objects.equals(this.key, imageLink.key) &&
        Objects.equals(this.link, imageLink.link);
  }

  @Override
  public int hashCode() {
    return Objects.hash(key, link);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ImageLink {\n");
    
    sb.append("    key: ").append(toIndentedString(key)).append("\n");
    sb.append("    link: ").append(toIndentedString(link)).append("\n");
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

