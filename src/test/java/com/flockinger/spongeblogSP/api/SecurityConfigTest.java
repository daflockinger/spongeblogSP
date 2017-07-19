package com.flockinger.spongeblogSP.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.flywaydb.test.annotation.FlywayTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.context.WebApplicationContext;

@FlywayTest(locationsForMigrate = {"/db/testfill/"})
public class SecurityConfigTest extends BaseControllerTest {

  @Value("${security-admin.clientid}")
  private String adminClientId;
  @Value("${security-admin.secretkey}")
  private String adminSecretkey;

  @Value("${security-author.clientid}")
  private String authorClientId;
  @Value("${security-author.secretkey}")
  private String authorSecretkey;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy filterChain;

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).addFilters(filterChain).build();

    SecurityContextHolder.clearContext();
  }


  @Test
  public void testRoot_withUnauthorized_shouldReturn401() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType)).andExpect(status().isUnauthorized());
  }

  @Test
  public void testRoot_withBasicUser_shouldReturn403() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testRoot_withAdmin_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().is3xxRedirection());
  }


  @Test
  public void testSwagger_withUnauthorized_shouldReturn401() throws Exception {
    mockMvc.perform(get("/swagger-ui.html").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testSwagger_withBasicUser_shouldReturn403() throws Exception {
    mockMvc.perform(get("/swagger-ui.html").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  public void testSwagger_withAdmin_shouldReturnCorrect() throws Exception {
    mockMvc.perform(get("/swagger-ui.html").contentType(jsonContentType).headers(getAdminHeaders()))
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
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/blog").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/blog/rewind").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testChangeBlog_withUser_shouldReturn403() throws Exception {
    mockMvc.perform(post("/api/v1/blog").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/blog").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/blog/rewind").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeBlog_withAdmin_shouldReturnWork() throws Exception {
    mockMvc.perform(post("/api/v1/blog").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc.perform(delete("/api/v1/blog").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(put("/api/v1/blog").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(put("/api/v1/blog/rewind").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());
  }


  @Test
  public void testChangeCategory_withAnonymous_shouldReturn401() throws Exception {
    mockMvc.perform(post("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/api/v1/categories/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/categories/rewind/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testChangeCategory_withUser_shouldReturn403() throws Exception {
    mockMvc
        .perform(post("/api/v1/categories").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            delete("/api/v1/categories/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(put("/api/v1/categories").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(
        put("/api/v1/categories/rewind/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeCategory_withAdmin_shouldReturnWork() throws Exception {
    mockMvc
        .perform(post("/api/v1/categories").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            delete("/api/v1/categories/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isConflict());

    mockMvc
        .perform(put("/api/v1/categories").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc.perform(
        put("/api/v1/categorie/rewind/1").contentType(jsonContentType).headers(getAdminHeaders()))
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
   * .contentType(jsonContentType).headers(getAdminHeaders())) .andExpect(status().isBadRequest());
   * 
   * mockMvc.perform(delete("/api/v1/images/1_bla.jpg")
   * .contentType(jsonContentType).headers(getAdminHeaders())) .andExpect(status().isOk()); }
   */



  @Test
  public void testChangeOrWeirdAccessPost_withAnonymous_shouldReturn401() throws Exception {

    mockMvc.perform(get("/api/v1/posts/author/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(post("/api/v1/posts").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/api/v1/posts/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/posts").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/posts/rewind/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeOrWeirdAccessPost_withUser_shouldReturnWork() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/posts/author/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(
        get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(post("/api/v1/posts").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(delete("/api/v1/posts/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(put("/api/v1/posts").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            put("/api/v1/posts/rewind/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isConflict());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeOrWeirdAccessPost_withAdmin_shouldReturnWork() throws Exception {
    mockMvc
        .perform(
            get("/api/v1/posts/author/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/v1/posts/author/1/PUBLIC").contentType(jsonContentType)
        .headers(getAdminHeaders())).andExpect(status().isOk());

    mockMvc.perform(post("/api/v1/posts").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(delete("/api/v1/posts/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(put("/api/v1/posts").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            put("/api/v1/posts/rewind/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isConflict());
  }



  @Test
  public void testChangeTags_withAnonymous_shouldReturn401() throws Exception {

    mockMvc.perform(post("/api/v1/tags").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/api/v1/tags/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/tags").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/tags/rewind/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void testChangeTags_withUser_shouldReturnWork() throws Exception {

    mockMvc.perform(post("/api/v1/tags").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(delete("/api/v1/tags/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/tags").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            put("/api/v1/tags/rewind/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeTags_withAdmin_shouldReturnWork() throws Exception {

    mockMvc.perform(post("/api/v1/tags").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(delete("/api/v1/tags/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(put("/api/v1/tags").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            put("/api/v1/tags/rewind/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isConflict());
  }



  @Test
  public void testChangeUser_withAnonymous_shouldReturn401() throws Exception {

    mockMvc.perform(get("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(post("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(delete("/api/v1/users/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/users").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());

    mockMvc.perform(put("/api/v1/users/rewind/1").contentType(jsonContentType))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeUser_withUser_shouldReturnWork() throws Exception {

    mockMvc.perform(get("/api/v1/users").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(post("/api/v1/users").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(delete("/api/v1/users/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc.perform(put("/api/v1/users").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());

    mockMvc
        .perform(
            put("/api/v1/users/rewind/1").contentType(jsonContentType).headers(getUserHeaders()))
        .andExpect(status().isForbidden());
  }

  @Test
  @FlywayTest(locationsForMigrate = {"/db/testfill/"})
  public void testChangeUser_withAdmin_shouldReturnWork() throws Exception {

    mockMvc.perform(get("/api/v1/users").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/api/v1/users/1").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(post("/api/v1/users").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(delete("/api/v1/users/2").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isOk());

    mockMvc.perform(put("/api/v1/users").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isBadRequest());

    mockMvc
        .perform(
            put("/api/v1/users/rewind/2").contentType(jsonContentType).headers(getAdminHeaders()))
        .andExpect(status().isConflict());
  }


  private HttpHeaders getAdminHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION,
        "Basic " + new String(Base64.encode((adminClientId + ":" + adminSecretkey).getBytes())));
    return headers;
  }

  private HttpHeaders getUserHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION,
        "Basic " + new String(Base64.encode((authorClientId + ":" + authorSecretkey).getBytes())));
    return headers;
  }
}
