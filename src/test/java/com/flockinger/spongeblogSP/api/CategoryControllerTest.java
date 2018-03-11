package com.flockinger.spongeblogSP.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.exception.OrphanedDependingEntitiesException;
import com.flockinger.spongeblogSP.service.CategoryService;
import com.google.common.collect.ImmutableList;

public class CategoryControllerTest extends BaseControllerTest {

  @MockBean
  private CategoryService service;

  @Test
  public void testApiV1CategoriesGet_withFilledDB_shouldReturnAll() throws Exception {
    when(service.getAllCategories()).thenReturn(getFakeCategories());
    
    mockMvc.perform(get("/api/v1/categories").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(3)))
        .andExpect(jsonPath("$[0].name", is("main category")))
        .andExpect(jsonPath("$[0].categoryId", is(1)))
        .andExpect(jsonPath("$[1].categoryId", is(2)))
        .andExpect(jsonPath("$[1].name", is("sub category")))
        .andExpect(jsonPath("$[1].parentId", is(1)));
  }
  
  private List<CategoryDTO> getFakeCategories() {
    CategoryDTO mainCat = new CategoryDTO();
    mainCat.setCategoryId(1l);
    mainCat.setName("main category");
    mainCat.setRank(1);
    CategoryDTO subCat = new CategoryDTO();
    subCat.setCategoryId(2l);
    subCat.setName("sub category");
    subCat.setRank(1);
    subCat.setParentId(1l);
    CategoryDTO funkySubCat = new CategoryDTO();
    funkySubCat.setCategoryId(2l);
    funkySubCat.setName("funky category");
    funkySubCat.setRank(1);
    funkySubCat.setParentId(1l);
    funkySubCat.setPageId(8l);
    return ImmutableList.of(mainCat, subCat, funkySubCat);
  }
  
  
  @Test
  public void testApiV1CategoriesCategoryIdGet_withValidId_shouldReturnCategory() throws Exception {
    when(service.getCategory(anyLong())).thenReturn(getFakeCategories().get(1));
    
    mockMvc.perform(get("/api/v1/categories/2").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$.name", is("sub category")))
        .andExpect(jsonPath("$.rank", is(1)))
        .andExpect(jsonPath("$.categoryId", is(2)))
        .andExpect(jsonPath("$.parentId", is(1)));
  }

  @Test
  public void testApiV1CategoriesCategoryIdGet_withNonExistingId_shouldReturnNotFound()
      throws Exception {
    when(service.getCategory(anyLong())).thenThrow(EntityIsNotExistingException.class);
    
    mockMvc.perform(get("/api/v1/categories/1").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1CategoriesChildrenParentCategoryIdGet_withExistingParent_shouldReturnChildren()
      throws Exception {
    when(service.getCategoriesFromParent(anyLong())).thenReturn(getFakeCategories().subList(1, 3));
    
    mockMvc.perform(get("/api/v1/categories/children/1").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[1].name", is("funky category")));
  }

  @Test
  public void testApiV1CategoriesChildrenParentCategoryIdGet_withNonExistingParent_shouldReturnEmpty()
      throws Exception {
    when(service.getCategoriesFromParent(anyLong())).thenReturn(new ArrayList<>());
    
    mockMvc.perform(get("/api/v1/categories/children/134").contentType(jsonContentType))
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }


  @Test
  public void testApiV1CategoriesCategoryIdDelete_withExisingCategory_shouldDelete()
      throws Exception {
    mockMvc.perform(delete("/api/v1/categories/" + 3l).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).deleteCategory(idCaptor.capture());
    assertEquals("verify category with correct ID is to be deleted", 3l, idCaptor.getValue().longValue());
  }

  @Test
  public void testApiV1CategoriesCategoryIdDelete_withExisingCategoryWithPosts_shouldReturnConflict()
      throws Exception {
    doThrow(OrphanedDependingEntitiesException.class).when(service).deleteCategory(anyLong());
    
    mockMvc.perform(delete("/api/v1/categories/1").contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1CategoriesCategoryIdDelete_withNonExitingCategory_shouldReturnNotFound()
      throws Exception {
    doThrow(EntityIsNotExistingException.class).when(service).deleteCategory(anyLong());
    
    mockMvc.perform(delete("/api/v1/categories/134").contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1CategoriesPost_withValidCategory_shouldCreate() throws Exception {
    when(service.createCategory(any())).thenReturn(createFakeCategory());

    mockMvc.perform(post("/api/v1/categories").content(json(createFakeCategory())).contentType(jsonContentType))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("improved category")))
        .andExpect(jsonPath("$.rank", is(2)))
        .andExpect(jsonPath("$.parentId", is(2)));
  }
  
  private CategoryDTO createFakeCategory() {
    CategoryDTO category = new CategoryDTO();
    category.setName("improved category");
    category.setRank(2);
    category.setParentId(2l);
    return category;
  }

  @Test
  public void testApiV1CategoriesPost_withCategoryWithNullParent_shouldCreate() throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setParentId(null);
    when(service.createCategory(any())).thenReturn(category);
    
    mockMvc.perform(post("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("improved category")))
        .andExpect(jsonPath("$.rank", is(2)));
  }

  @Test
  public void testApiV1CategoriesPost_withCategoryWithNonExistingParent_shouldReturnConflict()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setParentId(22343l);
    when(service.createCategory(any())).thenThrow(DependencyNotFoundException.class);

    mockMvc.perform(post("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1CategoriesPost_withCategoryWithEmptyName_shouldReturnBadRequest()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setName("");

    mockMvc.perform(post("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1CategoriesPost_withCategoryWithNullName_shouldReturnBadRequest()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setName(null);

    mockMvc.perform(post("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }


  @Test
  public void testApiV1CategoriesPut_withValidCategory_shouldUpdate() throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setCategoryId(3l);
    category.setParentId(null);

    mockMvc.perform(put("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<CategoryDTO> categoryCaptor = ArgumentCaptor.forClass(CategoryDTO.class);
    verify(service).updateCategory(categoryCaptor.capture());
    CategoryDTO updatedCategory = categoryCaptor.getValue();
    assertEquals("verify correct updated category name", "improved category", updatedCategory.getName());
    assertEquals("verify correct updated category rank", 2, updatedCategory.getRank().intValue());
    assertNull("verify correct updated parent category",updatedCategory.getParentId());
  }

  @Test
  public void testApiV1CategoriesPut_withNonExistingCategory_shouldReturnNotFound()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setCategoryId(32434l);
    doThrow(EntityIsNotExistingException.class).when(service).updateCategory(any());
    
    mockMvc.perform(put("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void testApiV1CategoriesPut_withCategoryWithNonExistingParent_shouldReturnConflict()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setCategoryId(2l);
    category.setParentId(2343434l);
    doThrow(DependencyNotFoundException.class).when(service).updateCategory(any());

    mockMvc.perform(put("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isConflict());
  }

  @Test
  public void testApiV1CategoriesPut_withCategoryWithEmptyName_shouldReturnBadRequest()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setCategoryId(2l);
    category.setName("");

    mockMvc.perform(put("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1CategoriesPut_withCategoryWithNullName_shouldReturnBadRequest()
      throws Exception {
    CategoryDTO category = createFakeCategory();
    category.setCategoryId(2l);
    category.setName(null);

    mockMvc.perform(put("/api/v1/categories").content(json(category)).contentType(jsonContentType))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testApiV1CategoriesRewindCategoryIdPut_withValidVersions_shouldRewind()
      throws Exception {
    mockMvc.perform(put("/api/v1/categories/rewind/2").contentType(jsonContentType))
        .andExpect(status().isOk());

    ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
    verify(service).rewind(idCaptor.capture());
    assertEquals("verify correct ID of category to rewind", 2l, idCaptor.getValue().longValue());

  }

  @Test
  public void testApiV1CategoriesRewindCategoryIdPut_withNoVersions_shouldReturnConflict()
      throws Exception {
    doThrow(NoVersionFoundException.class).when(service).rewind(anyLong());
    
    mockMvc.perform(put("/api/v1/categories/rewind/1").contentType(jsonContentType))
        .andExpect(status().isConflict());
  }
}
