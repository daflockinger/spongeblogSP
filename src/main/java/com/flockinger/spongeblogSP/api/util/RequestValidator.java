package com.flockinger.spongeblogSP.api.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.google.common.base.Enums;

@Component
public class RequestValidator {
	
	public void validateRequestBody(BindingResult bindingResult) throws DtoValidationFailedException {
		if (bindingResult.hasErrors()) {
			throw new DtoValidationFailedException("Invalid field entries!", bindingResult.getFieldErrors());
		}
	}
	
	public void assertIdForUpdate(Long id) throws DtoValidationFailedException{
		if(id == null){
			throw new DtoValidationFailedException("Id must not be null on update!", new ArrayList<>());
		}
	}
	
	public <T extends Enum<T>> void assertStatus(Class<T> statusClass, String status) throws DtoValidationFailedException {
		if (!Enums.getIfPresent(statusClass, status).isPresent()) {
			throw new DtoValidationFailedException("Invalid " + statusClass.getSimpleName() + ": " + status, new ArrayList<FieldError>());
		}
	}

	public void assertPaging(Integer page, Integer size) throws DtoValidationFailedException {
		List<FieldError> errors = new ArrayList<FieldError>();
		if (page < 0) {
			errors.add(new FieldError("page", "page", "Page cannot be < 0; "));
		}
		if (size <= 0) {
			errors.add(new FieldError("size", "size", "Size must be > 0; "));
		}

		if (errors.size() > 0) {
			throw new DtoValidationFailedException("Invalid paging settings", errors);
		}
	}
}
