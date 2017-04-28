package com.flockinger.spongeblogSP.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.TagPostsDTO;
import com.flockinger.spongeblogSP.exception.DtoValidationFailedException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.dto.Error;

import io.swagger.annotations.*;

public interface TagController {

    @ApiOperation(value = "All Tags", notes = "Returns all Tags.", response = TagDTO.class, responseContainer = "List", tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Tags.", response = TagDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1TagsGet();


    @ApiOperation(value = "Create Tag", notes = "Creates new Tag entry.", response = TagDTO.class, tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created Tag.", response = TagDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<?> apiV1TagsPost(@ApiParam(value = "" ,required=true ) @RequestBody TagDTO tagEdit, 
			BindingResult bindingResult)
    		throws DuplicateEntityException, DtoValidationFailedException;


    @ApiOperation(value = "Update Tag", notes = "Updated a Tag entry.", response = Void.class, tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Tag updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1TagsPut(@ApiParam(value = "" ,required=true ) @RequestBody TagDTO tagEdit, 
			BindingResult bindingResult) 
    		throws EntityIsNotExistingException, DtoValidationFailedException, DuplicateEntityException;


    @ApiOperation(value = "Rewind Tag", notes = "Restores previous Tag entry.", response = Void.class, tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Previous Tag restored.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags/rewind/{tagId}",
        produces = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1TagsRewindTagIdPut(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) throws NoVersionFoundException;


    @ApiOperation(value = "Delete Tag", notes = "Deletes a Tag with defined Id.", response = Void.class, tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Tag deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags/{tagId}",
        produces = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<?> apiV1TagsTagIdDelete(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) throws EntityIsNotExistingException;


    @ApiOperation(value = "Get Tag", notes = "Fetches Tag with defined Id.", response = TagPostsDTO.class, tags={ "Tags", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Tag with id.", response = TagPostsDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/tags/{tagId}",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1TagsTagIdGet(@ApiParam(value = "Unique identifier of a Tag;",required=true ) @PathVariable("tagId") Long tagId) throws EntityIsNotExistingException;

}
