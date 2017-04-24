package com.flockinger.spongeblogSP.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.nio.file.Files;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MvcResult;

public class ImageControllerTest extends BaseControllerTest{

	@Test
	public void testImageUpload() throws Exception{
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("image.jpg").getFile());
		
		MockMultipartFile multipartFile =
                new MockMultipartFile("file", "image.jpg", "image/jpeg", Files.readAllBytes(file.toPath()));
		
		MvcResult result = 
		mockMvc.perform(fileUpload("/api/v1/images").file(multipartFile))
                .andExpect(status().isOk())
                .andReturn();
		
	}
}
