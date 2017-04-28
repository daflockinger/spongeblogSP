package com.flockinger.spongeblogSP.api.impl;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.flockinger.spongeblogSP.api.ImageController;
import com.flockinger.spongeblogSP.dto.ImageLink;

import io.swagger.annotations.ApiParam;

@RestController
public class ImageControllerImpl implements ImageController {

	public ResponseEntity<?> apiV1ImagesGet(
			@ApiParam(value = "Page number from that on entities are returned.") @RequestParam(value = "page", required = false) Integer page,
			@ApiParam(value = "Entities per page.") @RequestParam(value = "size", required = false) Integer size,
			@ApiParam(value = "Get items from timestamp.") @RequestParam(value = "from", required = false) Long from,
			@ApiParam(value = "Get items until timestamp.") @RequestParam(value = "to", required = false) Long to) {
		// do some magic!
		return new ResponseEntity<File>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1ImagesImageKeyDelete(
			@ApiParam(value = "Key of the Image.", required = true) @PathVariable("imageKey") String imageKey) {
		// do some magic!
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1ImagesImageKeyGet(
			@ApiParam(value = "Key of the Image.", required = true) @PathVariable("imageKey") String imageKey) {
		// do some magic!
		return new ResponseEntity<File>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1ImagesLinkImageKeyGet(
			@ApiParam(value = "Key of the Image.", required = true) @PathVariable("imageKey") String imageKey) {
		// do some magic!
		return new ResponseEntity<ImageLink>(HttpStatus.OK);
	}

	public ResponseEntity<?> apiV1ImagesPost(@ApiParam(value = "file detail") @RequestPart("file") MultipartFile file) {
		// do some magic!
		
		System.out.println();
		return new ResponseEntity<ImageLink>(HttpStatus.OK);
	}

}
