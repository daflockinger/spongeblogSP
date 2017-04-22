package com.flockinger.spongeblogSP.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.flockinger.spongeblogSP.dto.UserEditDTO;
import com.flockinger.spongeblogSP.dto.Error;

import io.swagger.annotations.*;

public interface UserController {

    @ApiOperation(value = "All Users", notes = "Returns all Users.", response = UserEditDTO.class, responseContainer = "List", tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Users.", response = UserEditDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1UsersGet();


    @ApiOperation(value = "Create User", notes = "Creates new User entry.", response = UserEditDTO.class, tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created User.", response = UserEditDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<?> apiV1UsersPost(@ApiParam(value = "" ,required=true ) @RequestBody UserEditDTO userEdit);


    @ApiOperation(value = "Update User", notes = "Updated a User entry.", response = Void.class, tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "User updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1UsersPut(@ApiParam(value = "" ,required=true ) @RequestBody UserEditDTO userEdit);


    @ApiOperation(value = "Rewind User", notes = "Restores previous User entry.", response = Void.class, tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Previous User restored.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users/rewind/{user-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1UsersRewindUserIdPut(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId);


    @ApiOperation(value = "Delete User", notes = "Deletes a User with defined Id.", response = Void.class, tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "User deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users/{user-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<?> apiV1UsersUserIdDelete(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId);


    @ApiOperation(value = "Get User", notes = "Fetches User with defined Id.", response = UserEditDTO.class, tags={ "Users", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "User with id.", response = UserEditDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/users/{user-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1UsersUserIdGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId);

}
