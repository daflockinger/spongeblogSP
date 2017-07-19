package com.flockinger.spongeblogSP.model.enums;

import com.google.gson.annotations.SerializedName;

/**
 * Status of the Blog.
 */
public enum BlogStatus {

  @SerializedName("ACTIVE")
  ACTIVE("ACTIVE"),

  @SerializedName("DISABLED")
  DISABLED("DISABLED"),

  @SerializedName("MAINTENANCE")
  MAINTENANCE("MAINTENANCE");

  private String value;

  BlogStatus(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
