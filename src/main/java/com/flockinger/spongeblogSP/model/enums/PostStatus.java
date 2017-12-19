package com.flockinger.spongeblogSP.model.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Display status of the Post.
 */
public enum PostStatus {
  @SerializedName("PUBLIC")
  PUBLIC("PUBLIC"),

  @SerializedName("PRIVATE")
  PRIVATE("PRIVATE"),

  @SerializedName("MAINTENANCE")
  MAINTENANCE("MAINTENANCE"),

  @SerializedName("DELETED")
  DELETED("DELETED"),
  
  @SerializedName("PAGE_PUBLIC")
  PAGE_PUBLIC("PAGE_PUBLIC"),
  
  @SerializedName("PAGE_INACTIVE")
  PAGE_INACTIVE("PAGE_INACTIVE");

  private String value;

  PostStatus(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
