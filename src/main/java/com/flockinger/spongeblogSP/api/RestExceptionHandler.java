package com.flockinger.spongeblogSP.api;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.flockinger.spongeblogSP.dto.Error;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
 
    
    @ExceptionHandler(value = {EntityIsNotExistingException.class})
    protected ResponseEntity<Object> handleNotFound(Exception ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorModel(ex), 
          new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
    
    @ExceptionHandler(value = {DtoValidationFailedException.class})
    protected ResponseEntity<Object> handleBadRequest(DtoValidationFailedException ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorValidatedModel(ex), 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    
    @ExceptionHandler(value = {DuplicateEntityException.class, OrphanedDependingEntitiesException.class, DependencyNotFoundException.class
    		, NoVersionFoundException.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        
    	return handleExceptionInternal(ex, createErrorModel(ex), 
          new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
        
    private Error createErrorValidatedModel(DtoValidationFailedException validationException){
    	Error error = createErrorModel(validationException);
    	
    	List<FieldError> fieldErrors = validationException.getErrors();
    	Map<String, String> fieldMap = fieldErrors.stream().collect(Collectors.toMap(FieldError::getField, value -> 
    	{return value.getDefaultMessage() + "[" + value.getRejectedValue() + "]";}));
    	
    	error.setFields(fieldMap);
    	return error;
    }
    
    private Error createErrorModel(Exception exception){
    	Error error = new Error();
    	error.setMessage(exception.getMessage());
    	
    	return error;
    }
}
