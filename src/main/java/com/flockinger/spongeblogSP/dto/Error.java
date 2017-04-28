package com.flockinger.spongeblogSP.dto;

import java.util.HashMap;
import java.util.Map;

import io.swagger.annotations.ApiModelProperty;

/**
 * Error
 */
public class Error {

	private Integer code = null;
	private String message = null;
	private Map<String, String> fields = new HashMap<String, String>();

	/**
	 * Get code
	 * 
	 * @return code
	 **/
	@ApiModelProperty(value = "")
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * Get message
	 * 
	 * @return message
	 **/
	@ApiModelProperty(value = "")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Invalid fields.
	 * 
	 * @return fields
	 **/
	@ApiModelProperty(value = "Invalid fields.")
	public Map<String, String> getFields() {
		return fields;
	}

	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
}