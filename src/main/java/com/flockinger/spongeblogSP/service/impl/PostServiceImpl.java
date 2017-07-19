package com.flockinger.spongeblogSP.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.PostPreviewDTO;
import com.flockinger.spongeblogSP.dto.PostsPage;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PostService;

@Service
public class PostServiceImpl implements PostService {

  @Autowired
  private PostDAO dao;

  @Autowired
  private ModelMapper mapper;

  @Autowired
  private TagDAO tagDao;

  @Autowired
  private CategoryDAO categoryDao;

  @Autowired
  private UserDAO userDao;

  @PersistenceContext
  private EntityManager em;

  @Value("${blog.preview-text-size}")
  private Integer previewTextSize;

  @Override
  @Transactional(readOnly = true)
  public PostsPage getAllPosts(Pageable pageable) {
    return map(dao.findAll(pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getAllPostsWithStatus(PostStatus status, Pageable pageable) {
    return map(dao.findByStatus(status, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromCategoryId(Long categoryId, Pageable pageable) {
    return map(dao.findByCategoryId(categoryId, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromCategoryIdWithStatus(Long categoryId, PostStatus status,
      Pageable pageable) {
    return map(dao.findByCategoryIdAndStatus(categoryId, status, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromAuthorId(Long authorId, Pageable pageable) {
    return map(dao.findByAuthorId(authorId, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromAuthorIdWithStatus(Long authorId, PostStatus status,
      Pageable pageable) {
    return map(dao.findByAuthorIdAndStatus(authorId, status, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromTagId(Long tagId, Pageable pageable) {
    return map(dao.findByTagsId(tagId, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostsPage getPostsFromTagIdWithStatus(Long tagId, PostStatus status, Pageable pageable) {
    return map(dao.findByTagsIdAndStatus(tagId, status, pageable));
  }

  @Override
  @Transactional(readOnly = true)
  public PostDTO getPost(Long id) throws EntityIsNotExistingException {
    if (!isPostExisting(id)) {
      throw new EntityIsNotExistingException("Post");
    }

    return map(dao.findOne(id));
  }

  @Override
  @Transactional
  public PostDTO createPost(PostDTO post)
      throws DuplicateEntityException, DependencyNotFoundException {
    if (isPostNameExistingAlready(post)) {
      throw new DuplicateEntityException("Post");
    }
    checkDependencies(post);

    return map(dao.save(map(post)));
  }

  private boolean isPostNameExistingAlready(PostDTO post) {
    return dao.findByTitle(post.getTitle()) != null;
  }

  private void checkDependencies(PostDTO post) throws DependencyNotFoundException {
    UserInfoDTO user = post.getAuthor();
    if (user != null && !userDao.exists(user.getUserId())) {
      throw getDependencyException("User", user.getUserId());
    }
    CategoryDTO category = post.getCategory();
    if (category != null && !categoryDao.exists(category.getCategoryId())) {
      throw getDependencyException("Category", category.getCategoryId());
    }
    List<TagDTO> tags = post.getTags();
    Optional<TagDTO> nonExistingTag =
        tags.stream().filter(tag -> !tagDao.exists(tag.getTagId())).findAny();

    if (nonExistingTag.isPresent()) {
      throw getDependencyException("Tag", nonExistingTag.get().getTagId());
    }
  }

  private DependencyNotFoundException getDependencyException(String entityName, Long id) {
    return new DependencyNotFoundException(
        entityName + " wih id: " + id + " is not existing for Post");
  }

  @Override
  @Transactional
  public void updatePost(PostDTO post)
      throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
    if (!isPostExisting(post.getPostId())) {
      throw new EntityIsNotExistingException("Post");
    }
    if (isNewNameDuplicate(post)) {
      throw new DuplicateEntityException("Post");
    }
    checkDependencies(post);

    dao.save(map(post));
  }

  private boolean isPostExisting(Long id) {
    return dao.exists(id);
  }

  private boolean isNewNameDuplicate(PostDTO post) {
    Post savedPost = dao.findByTitle(post.getTitle());
    return savedPost != null && post.getPostId() != savedPost.getId();
  }

  @Override
  @Transactional
  public void deletePost(Long id) throws EntityIsNotExistingException {
    if (!isPostExisting(id)) {
      throw new EntityIsNotExistingException("Post");
    }

    dao.delete(id);
  }

  private List<PostPreviewDTO> map(List<Post> posts) {
    return posts.stream().map(post -> mapPreview(post))
        .collect(Collectors.toCollection(LinkedList::new));
  }

  private PostPreviewDTO mapPreview(Post post) {
    PostPreviewDTO preview = mapper.map(post, PostPreviewDTO.class);
    preview.setPartContent(StringUtils.abbreviate(post.getContent(), previewTextSize));
    return preview;
  }

  @Override
  @Transactional
  public void rewind(Long id) throws NoVersionFoundException {
    AuditReader auditReader = AuditReaderFactory.get(em);
    Integer prevVersion = previousVersionNumber(auditReader, id);
    Post previousPost = auditReader.find(Post.class, id, prevVersion);
    List<Tag> tags = em.createNamedQuery("Tag.findFromPreviousPost", Tag.class)
        .setParameter("postId", id).setParameter("revId", prevVersion).getResultList();

    previousPost.setTags(tags);
    dao.save(previousPost);
  }

  private Integer previousVersionNumber(AuditReader auditReader, Long id)
      throws NoVersionFoundException {
    @SuppressWarnings("unchecked")
    List<Integer> revs = auditReader.createQuery()
        .forRevisionsOfEntity(Post.class, Post.class.getName(), false, true)
        .addProjection(AuditEntity.revisionNumber()).addOrder(AuditEntity.revisionNumber().desc())
        .add(AuditEntity.id().eq(id)).getResultList();
    if (revs == null || revs.size() < 2) {
      throw new NoVersionFoundException("No previous version found of Post");
    }
    return revs.get(1);
  }

  private PostsPage map(Page<Post> page) {
    PostsPage postPage = mapper.map(page, PostsPage.class);
    postPage.setPreviewPosts(map(page.getContent()));
    postPage.setHasNext(page.hasNext());
    postPage.setHasPrevious(page.hasPrevious());
    return postPage;
  }

  private PostDTO map(Post post) {
    return mapper.map(post, PostDTO.class);
  }

  private Post map(PostDTO post) {
    return mapper.map(post, Post.class);
  }

}
