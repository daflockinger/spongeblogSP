package com.flockinger.spongeblogSP.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.Error;

import io.swagger.annotations.*;


public interface PostController {

    @ApiOperation(value = "Posts from User", notes = "Returns all posts from defined User.", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/author/{user-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsAuthorUserIdGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);


    @ApiOperation(value = "Posts from User and Status", notes = "Returns all posts from defined User and Status.", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/author/{user-id}/{status}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsAuthorUserIdStatusGet(@ApiParam(value = "Unique identifier of a User;",required=true ) @PathVariable("userId") Long userId,
        @ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);


    @ApiOperation(value = "Posts from Category", notes = "Returns all posts from defined Category.", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/category/{category-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsCategoryCategoryIdGet(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);


    @ApiOperation(value = "Posts from Category and Status", notes = "Returns all posts from defined Category and Status.", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/category/{category-id}/{status}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsCategoryCategoryIdStatusGet(@ApiParam(value = "Unique identifier of a Category;",required=true ) @PathVariable("categoryId") Long categoryId,
        @ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);


    @ApiOperation(value = "All posts", notes = "Returns all posts (paginated).", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsGet( @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);


    @ApiOperation(value = "Create Post", notes = "Creates new Post entry.", response = PostDTO.class, tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Created Post.", response = PostDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.POST)
    ResponseEntity<?> apiV1PostsPost(@ApiParam(value = "" ,required=true ) @RequestBody PostDTO postEdit);


    @ApiOperation(value = "Delete Post", notes = "Deletes a Post with defined Id.", response = Void.class, tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/{post-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<?> apiV1PostsPostIdDelete(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId);


    @ApiOperation(value = "Get Post", notes = "Fetches Post with defined Id.", response = PostDTO.class, tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post with id.", response = PostDTO.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/{post-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsPostIdGet(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId);


    @ApiOperation(value = "Update Post", notes = "Updated a Post entry.", response = Void.class, tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post updated.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1PostsPut(@ApiParam(value = "" ,required=true ) @RequestBody PostDTO postEdit);


    @ApiOperation(value = "Rewind Post", notes = "Restores previous Post entry.", response = Void.class, tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Previous Post restored.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/rewind/{post-id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PUT)
    ResponseEntity<?> apiV1PostsRewindPostIdPut(@ApiParam(value = "Unique identifier of a Post;",required=true ) @PathVariable("postId") Long postId);


    @ApiOperation(value = "Posts with status", notes = "Returns all posts with defined status.", response = String.class, responseContainer = "List", tags={ "Posts", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Post links.", response = String.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/posts/status/{status}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1PostsStatusStatusGet(@ApiParam(value = "Post Status Id",required=true ) @PathVariable("status") String status,
         @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size);

}
