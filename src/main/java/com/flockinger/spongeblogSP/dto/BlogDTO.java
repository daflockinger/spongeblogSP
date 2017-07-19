package com.flockinger.spongeblogSP.dto;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;

import com.flockinger.spongeblogSP.model.enums.BlogStatus;

import io.swagger.annotations.ApiModelProperty;

/**
 * BlogDTO
 */
public class BlogDTO extends ResourceSupport {

  @NotEmpty
  private String name = null;

  @NotNull
  private BlogStatus status = null;

  @NotNull
  private Map<String, String> settings = new HashMap<String, String>();

  /**
   * Name of the Blog.
   * 
   * @return name
   **/
  @ApiModelProperty(example = "null", value = "Name of the Blog.")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Status of the Blog.
   * 
   * @return status
   **/
  @ApiModelProperty(example = "null", value = "Status of the Blog.")
  public BlogStatus getStatus() {
    return status;
  }

  public void setStatus(BlogStatus status) {
    this.status = status;
  }


  /**
   * Settings map of the Blog.
   * 
   * @return settings
   **/
  @ApiModelProperty(example = "null", value = "Settings map of the Blog.")
  public Map<String, String> getSettings() {
    return settings;
  }

  public void setSettings(Map<String, String> settings) {
    this.settings = settings;
  }
}
