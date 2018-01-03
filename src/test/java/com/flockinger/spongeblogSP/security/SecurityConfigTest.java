package com.flockinger.spongeblogSP.security;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;

import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockitoTestExecutionListener;
import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
@ActiveProfiles(profiles = {"default", "test"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    FlywayTestExecutionListener.class, MockitoTestExecutionListener.class,
    ResetMocksTestExecutionListener.class, WithSecurityContextTestExecutionListener.class})
@FlywayTest(invokeCleanDB = true, locationsForMigrate = {"/db/migration","/db/testfill/"})
public class SecurityConfigTest {
  
  private MockMvc mockMvc;
  
  protected MediaType jsonContentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy filterChain;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .addFilters(filterChain).build();

    SecurityContextHolder.clearContext();
  }


  
  @Test
  public void testRoot_withUnauthorized_shouldBeRedirectedToSwaggerPage() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType)).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  public void testRoot_withBasicUser_shouldBeRedirectedToSwaggerPage() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType) )
        .andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  public void testRoot_withAdmin_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType))
        .andExpect(status().is3xxRedirection());
  }


  @Test
  public void testSwagger_withUnauthorized_shouldReturn401() throws Exception {
    mockMvc.perform(get("/swagger-ui.html").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  public void testSwagger_withBasicUser_shouldReturn403() throws Exception {
    mockMvc
        .perform(
            get("/swagger-ui.html").contentType(jsonContentType) )
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  public void testSwagger_withAdmin_shouldReturnCorrect() throws Exception {
    mockMvc
        .perform(
            get("/swagger-ui.html").contentType(jsonContentType)  )
        .andExpect(status().isOk());
  }


  @Test
  public void testGetBlog_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/blog").contentType(jsonContentType)).andExpect(status().isOk());
  }

  @Test
  public void testGetCategories_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetCategory_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/categories/1").contentType(jsonContentType))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetCagegoryChildren_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/categories/children/1").contentType(jsonContentType))
        .andExpect(status().isOk());
  }


  // TODO activate when implementedd
  /*
   * @Test public void testGetImages_withAnonymous_shouldReturnCorrect() throws Exception {
   * mockMvc.perform(get("/api/v1/images") .contentType(jsonContentType))
   * .andExpect(status().isOk()); }
   * 
   * @Test public void testGetImagesWithKey_withAnonymous_shouldReturnCorrect() throws Exception {
   * mockMvc.perform(get("/api/v1/images/1_bla.jpg") .contentType(jsonContentType))
   * .andExpect(status().isOk()); }
   * 
   * @Test public void testGetImageLink_withAnonymous_shouldReturnCorrect() throws Exception {
   * mockMvc.perform(get("/api/v1/images/link/1_bla.jpg") .contentType(jsonContentType))
   * .andExpect(status().isOk()); }
   */



  @Test
  public void testGetPosts_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts").contentType(jsonContentType)).andExpect(status().isOk());
  }

  @Test
  public void testGetPost_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/1").contentType(jsonContentType)).andExpect(status().isOk());
  }

  @Test
  public void testGetPostsStatus_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/status/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk());
  }


  @Test
  public void testGetPostsTag_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1").contentType(jsonContentType))
        .andExpect(status().isOk());
  }


  @Test
  public void testGetPostsTagStatus_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/tag/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetPostsCategory_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1").contentType(jsonContentType))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetPostsCategoryStatus_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/posts/category/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isOk());
  }

  @Test
  public void testGetTags_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/tags").contentType(jsonContentType)).andExpect(status().isOk());
  }

  @Test
  public void testGetTag_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/tags/1").contentType(jsonContentType)).andExpect(status().isOk());
  }

  @Test
  public void testGetUserInfo_withAnonymous_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/api/v1/users/info/1").contentType(jsonContentType))
        .andExpect(status().isOk());
  }



  @Test
  public void testChangeBlog_withAnonymous_shouldReturn401() throws Exception {
    mockMvc.perform(post("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/blog/rewind").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  public void testChangeBlog_withUser_shouldReturn403() throws Exception {
    mockMvc
        .perform(post("/api/v1/blog").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            delete("/api/v1/blog").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/blog").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            put("/api/v1/blog/rewind").contentType(jsonContentType) )
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeBlog_withAdmin_shouldReturnWork() throws Exception {
    mockMvc
        .perform(post("/api/v1/blog").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/blog").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(put("/api/v1/blog").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/blog/rewind")
            .contentType(jsonContentType)  )
        .andExpect(status().isOk());
  }


  @Test
  public void testChangeCategory_withAnonymous_shouldReturn401() throws Exception {
    mockMvc.perform(post("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/categories/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/categories/rewind/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  public void testChangeCategory_withUser_shouldReturn403() throws Exception {
    mockMvc
        .perform(
            post("/api/v1/categories").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(delete("/api/v1/categories/1")
            .contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            put("/api/v1/categories").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/categories/rewind/1")
            .contentType(jsonContentType) )
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeCategory_withAdmin_shouldReturnWork() throws Exception {
    mockMvc
        .perform(post("/api/v1/categories")
            .contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(delete("/api/v1/categories/1")
            .contentType(jsonContentType)  )
        .andExpect(status().isConflict());

    mockMvc
        .perform(
            put("/api/v1/categories").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/categorie/rewind/1")
            .contentType(jsonContentType)  )
        .andExpect(status().isNotFound());
  }



  // TODO activate when images implemented
  /*
   * @Test public void testChangeImage_withAnonymous_shouldReturn401() throws Exception {
   * mockMvc.perform(post("/api/v1/images") .contentType(jsonContentType))
   * .andExpect(status().isUnauthorized());
   * 
   * mockMvc.perform(delete("/api/v1/images/1_bla.jpg") .contentType(jsonContentType))
   * .andExpect(status().isUnauthorized()); }
   * 
   * @Test public void testChangeImage_withUser_shouldReturn403() throws Exception {
   * mockMvc.perform(post("/api/v1/images") .contentType(jsonContentType).headers(getUserHeaders()))
   * .andExpect(status().isForbidden());
   * 
   * mockMvc.perform(delete("/api/v1/images/1_bla.jpg")
   * .contentType(jsonContentType).headers(getUserHeaders())) .andExpect(status().isForbidden()); }
   * 
   * @Test
   * 
   * @FlywayTest(locationsForMigrate = { "/db/testfill/" }) public void
   * testChangeImage_withAdmin_shouldReturnWork() throws Exception {
   * mockMvc.perform(post("/api/v1/images")
   * .contentType(jsonContentType)) .andExpect(status().isBadRequest());
   * 
   * mockMvc.perform(delete("/api/v1/images/1_bla.jpg")
   * .contentType(jsonContentType)) .andExpect(status().isOk()); }
   */



  @Test
  public void testChangeOrWeirdAccessPost_withAnonymous_shouldReturn401() throws Exception {
    mockMvc.perform(post("/api/v1/posts").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/posts/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/posts").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/posts/rewind/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeOrWeirdAccessPost_withUser_shouldReturnWork() throws Exception {
    mockMvc
        .perform(get("/api/v1/posts/author/1")
            .contentType(jsonContentType) )
        .andExpect(status().isOk());

    mockMvc
        .perform(get("/api/v1/posts/author/1/PUBLIC")
            .contentType(jsonContentType) )
        .andExpect(status().isOk());

    mockMvc
        .perform(post("/api/v1/posts").contentType(jsonContentType) )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/posts/1").contentType(jsonContentType) )
        .andExpect(status().isOk());

    mockMvc
        .perform(put("/api/v1/posts").contentType(jsonContentType) )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/posts/rewind/1")
            .contentType(jsonContentType) )
        .andExpect(status().isConflict());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeOrWeirdAccessPost_withAdmin_shouldReturnWork() throws Exception {
    mockMvc
        .perform(get("/api/v1/posts/author/1")
            .contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType)
      ).andExpect(status().isOk());

    mockMvc
        .perform(
            post("/api/v1/posts").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/posts/1").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(put("/api/v1/posts").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/posts/rewind/1")
            .contentType(jsonContentType)  )
        .andExpect(status().isConflict());
  }



  @Test
  public void testChangeTags_withAnonymous_shouldReturn401() throws Exception {

    mockMvc.perform(post("/api/v1/tags").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/tags/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/tags").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/tags/rewind/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  public void testChangeTags_withUser_shouldReturnWork() throws Exception {

    mockMvc
        .perform(post("/api/v1/tags").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            delete("/api/v1/tags/1").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/tags").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/tags/rewind/1")
            .contentType(jsonContentType) )
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeTags_withAdmin_shouldReturnWork() throws Exception {

    mockMvc
        .perform(post("/api/v1/tags").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/tags/1").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(put("/api/v1/tags").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/tags/rewind/1")
            .contentType(jsonContentType)  )
        .andExpect(status().isConflict());
  }



  @Test
  public void testChangeUser_withAnonymous_shouldReturn401() throws Exception {

    mockMvc.perform(get("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(post("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/users/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/users/rewind/1").contentType(jsonContentType))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"AUTHOR"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeUser_withUser_shouldReturnWork() throws Exception {

    mockMvc
        .perform(get("/api/v1/users").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            get("/api/v1/users/1").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(post("/api/v1/users").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            delete("/api/v1/users/1").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/users").contentType(jsonContentType) )
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/users/rewind/1")
            .contentType(jsonContentType) )
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(authorities = {"ADMIN"})
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeUser_withAdmin_shouldReturnWork() throws Exception {

    mockMvc
        .perform(get("/api/v1/users").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(
            get("/api/v1/users/1").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(
            post("/api/v1/users").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/users/2").contentType(jsonContentType)  )
        .andExpect(status().isOk());

    mockMvc
        .perform(put("/api/v1/users").contentType(jsonContentType)  )
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/users/rewind/2")
            .contentType(jsonContentType)  )
        .andExpect(status().isConflict());
  }
}
