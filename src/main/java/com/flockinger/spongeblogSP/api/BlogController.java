package com.flockinger.spongeblogSP.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flockinger.spongeblogSP.dto.BlogDTO;
import com.flockinger.spongeblogSP.dto.Error;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

public interface BlogController {

    @ApiOperation(value = "Delete Blog", notes = "Deletes existing Blog.", response = Void.class, tags={ "Blog", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Blog deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/blog",
        produces = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<?> apiV1BlogDelete() throws EntityIsNotExistingException;


    @ApiOperation(value = "Get Blog", notes = "Returns the Blog entry.", response = BlogDTO.class, tags={ "Blog", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Blog.", response = BlogDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/blog",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1BlogGet() throws EntityIsNotExistingException;

    
    @ApiOperation(value = "Create Blog", notes = "Creates new Blog entry.", response = BlogDTO.class, tags={ "Blog", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created Blog.", response = BlogDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/blog",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<?> apiV1BlogPost(@ApiParam(value = "" ,required=true ) @RequestBody BlogDTO blogEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, DuplicateEntityException;


    @ApiOperation(value = "Update Blog", notes = "Updated a Blog entry.", response = Void.class, tags={ "Blog", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Blog updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/blog",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1BlogPut(@ApiParam(value = "" ,required=true ) @RequestBody BlogDTO blogEdit,
			BindingResult bindingResult) throws DtoValidationFailedException, EntityIsNotExistingException;


    @ApiOperation(value = "Rewind Blog", notes = "Restores previous Blog entry.", response = Void.class, tags={ "Blog", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Previous Blog restored.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/blog/rewind",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1BlogRewindPut() throws NoVersionFoundException;
}
