package com.flockinger.spongeblogSP.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flockinger.spongeblogSP.dao.CategoryDAO;
import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dao.TagDAO;
import com.flockinger.spongeblogSP.dao.UserDAO;
import com.flockinger.spongeblogSP.dto.CategoryDTO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.TagDTO;
import com.flockinger.spongeblogSP.dto.UserInfoDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DependencyNotFoundException;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.exception.NoVersionFoundException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.PostTagsAud;
import com.flockinger.spongeblogSP.model.Tag;
import com.flockinger.spongeblogSP.model.enums.PostStatus;
import com.flockinger.spongeblogSP.service.PostService;

@Service
@Transactional
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

	@Override
	public List<PostLink> getAllPosts(Pageable pageable) {
		return map(dao.findAllIdsDistinct(pageable));
	}

	@Override
	public List<PostLink> getAllPostsWithStatus(PostStatus status, Pageable pageable) {
		return map(dao.findIdDistinctByStatus(status, pageable));
	}

	@Override
	public List<PostLink> getPostsFromCategoryId(Long categoryId, Pageable pageable) {
		return map(dao.findDistinctIdByCategoryId(categoryId, pageable));
	}

	@Override
	public List<PostLink> getPostsFromCategoryIdWithStatus(Long categoryId, PostStatus status, Pageable pageable) {
		return map(dao.findDistinctIdByCategoryIdAndStatus(categoryId, status, pageable));
	}

	@Override
	public List<PostLink> getPostsFromAuthorId(Long authorId, Pageable pageable) {
		return map(dao.findDistinctIdByAuthorId(authorId, pageable));
	}

	@Override
	public List<PostLink> getPostsFromAuthorIdWithStatus(Long authorId, PostStatus status, Pageable pageable) {
		return map(dao.findDistinctIdByAuthorIdAndStatus(authorId, status, pageable));
	}

	@Override
	public PostDTO getPost(Long id) throws EntityIsNotExistingException {
		if (!isPostExisting(id)) {
			throw new EntityIsNotExistingException("Post");
		}

		return map(dao.findOne(id));
	}

	@Override
	public PostDTO createPost(PostDTO post) throws DuplicateEntityException, DependencyNotFoundException {
		if (isPostNameExistingAlready(post)) {
			throw new DuplicateEntityException("Post");
		}
		checkDependencies(post);
		
		return map(dao.save(map(post)));
	}

	private boolean isPostNameExistingAlready(PostDTO post) {		
		return dao.findByTitle(post.getTitle()) != null;
	}

	private void checkDependencies(PostDTO post) throws DependencyNotFoundException{
		UserInfoDTO user = post.getAuthor();
		if(user != null && !userDao.exists(user.getId())){
			throw getDependencyException("User",user.getId());
		}
		CategoryDTO category= post.getCategory();
		if(category != null && !categoryDao.exists(category.getId())){
			throw getDependencyException("Category", category.getId());
		}
		List<TagDTO> tags = post.getTags();
		Optional<TagDTO> nonExistingTag = tags.stream().filter(tag -> !tagDao.exists(tag.getId())).findAny();
		
		if(nonExistingTag.isPresent()){
			throw getDependencyException("Tag", nonExistingTag.get().getId());
		}
	}

	private DependencyNotFoundException getDependencyException(String entityName, Long id) {
		return new DependencyNotFoundException(entityName + " wih id: " + id + " is not existing for Post");
	}

	@Override
	public void updatePost(PostDTO post)
			throws EntityIsNotExistingException, DuplicateEntityException, DependencyNotFoundException {
		if (!isPostExisting(post.getId())) {
			throw new EntityIsNotExistingException("Post");
		}
		if(isNewNameDuplicate(post)){
			throw new DuplicateEntityException("Post");
		}
		checkDependencies(post);
		
		dao.save(map(post));
	}

	private boolean isPostExisting(Long id) {
		return dao.exists(id);
	}
	
	private boolean isNewNameDuplicate(PostDTO post){
		Post savedPost = dao.findByTitle(post.getTitle());
		return savedPost != null && post.getId() != savedPost.getId();
	}

	@Override
	public void deletePost(Long id) throws EntityIsNotExistingException {
		if (!isPostExisting(id)) {
			throw new EntityIsNotExistingException("Post");
		}

		dao.delete(id);
	}

	private List<PostLink> map(List<Long> ids) {
		return ids.stream().map(post -> map(post)).collect(Collectors.toCollection(LinkedList::new));
	}

	private PostLink map(Long id) {
		PostLink link = new PostLink();
		link.setId(id);

		return link;
	}

	@Override
	@Transactional
	public void rewind(Long id) throws NoVersionFoundException {
		AuditReader auditReader = AuditReaderFactory.get(em);
		Integer prevVersion = previousVersionNumber(auditReader, id);
		Post previousPost = auditReader.find(Post.class, id, prevVersion);

		List<PostTagsAud> postTags = em
				.createQuery("select p from PostTagsAud p where p.postsId = :postId and p.rev = :revId",
						PostTagsAud.class)
				.setParameter("postId", id).setParameter("revId", prevVersion).getResultList();
		List<Tag> relatedTags = getRealTagsFromIds(postTags);
		previousPost.setTags(relatedTags);

		dao.save(previousPost);
	}

	private Integer previousVersionNumber(AuditReader auditReader, Long id) throws NoVersionFoundException {
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

	private List<Tag> getRealTagsFromIds(List<PostTagsAud> postTags) {
		List<Tag> realTags = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(postTags)) {
			realTags = postTags.stream().map(postTag -> tagDao.findOne(postTag.getTagId()))
					.collect(Collectors.toList());
		}
		return realTags;
	}

	private PostDTO map(Post post) {
		return mapper.map(post, PostDTO.class);
	}

	private Post map(PostDTO post) {
		return mapper.map(post, Post.class);
	}

}
