package com.flockinger.spongeblogSP.dto;

import io.swagger.annotations.ApiModelProperty;

/**
 * ImageLink
 */
public class ImageLink {

	private String key = null;
	private String link = null;

	/**
	 * Key of the Image (id + '_' +filename).
	 * 
	 * @return key
	 **/
	@ApiModelProperty(example = "null", value = "Key of the Image (id + '_' +filename).")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * URL of the Image.
	 * 
	 * @return link
	 **/
	@ApiModelProperty(example = "null", value = "URL of the Image.")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
