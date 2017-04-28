package com.flockinger.spongeblogSP.api;

import java.io.File;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.flockinger.spongeblogSP.dto.ImageLink;
import com.flockinger.spongeblogSP.dto.Error;

import io.swagger.annotations.*;

public interface ImageController {

    @ApiOperation(value = "Get all Images.", notes = "Returns a paginated list of all Images.", response = File.class, tags={ "Images", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns Image.", response = File.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/images",
        produces = { "application/json" },
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1ImagesGet( @ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
         @ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size,
         @ApiParam(value = "Get items from timestamp.") @RequestParam(value = "from", required = false) Long from,
         @ApiParam(value = "Get items until timestamp.") @RequestParam(value = "to", required = false) Long to);


    @ApiOperation(value = "Removes Image.", notes = "Removes Image from DB and storage.", response = Void.class, tags={ "Images", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Image deleted.", response = Void.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/images/{image-key}",
        produces = { "application/json" },
        method = RequestMethod.DELETE)
    ResponseEntity<?> apiV1ImagesImageKeyDelete(@ApiParam(value = "Key of the Image.",required=true ) @PathVariable("imageKey") String imageKey);


    @ApiOperation(value = "Load Image.", notes = "Returns an Image file by key.", response = File.class, tags={ "Images", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns Image.", response = File.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/images/{image-key}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1ImagesImageKeyGet(@ApiParam(value = "Key of the Image.",required=true ) @PathVariable("imageKey") String imageKey);


    @ApiOperation(value = "Load Image.", notes = "Returns the link to the Image by key.", response = ImageLink.class, tags={ "Images", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Returns Image link.", response = ImageLink.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/images/link/{image-key}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<?> apiV1ImagesLinkImageKeyGet(@ApiParam(value = "Key of the Image.",required=true ) @PathVariable("imageKey") String imageKey);


    @ApiOperation(value = "Uploads image file", notes = "Uploads image file and returns their link.", response = ImageLink.class, tags={ "Images", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Image Link.", response = ImageLink.class),
        @ApiResponse(code = 400, message = "Bad request (validation failed).", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized (need to log in / get token).", response = Void.class),
        @ApiResponse(code = 403, message = "Forbidden (no rights to access resource).", response = Void.class),
        @ApiResponse(code = 404, message = "Entity not found.", response = Error.class),
        @ApiResponse(code = 409, message = "Request results in a conflict.", response = Error.class),
        @ApiResponse(code = 500, message = "Internal Server Error.", response = Void.class) })
    @RequestMapping(value = "/api/v1/images",
        produces = { "application/json" }, 
        consumes = { "multipart/form-data" },
        method = RequestMethod.POST)
    ResponseEntity<?> apiV1ImagesPost(@ApiParam(value = "file detail") @RequestPart("file") MultipartFile file);

}
