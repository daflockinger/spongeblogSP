package com.flockinger.spongeblogSP.service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.flockinger.spongeblogSP.dao.PostDAO;
import com.flockinger.spongeblogSP.dto.PostDTO;
import com.flockinger.spongeblogSP.dto.link.PostLink;
import com.flockinger.spongeblogSP.exception.DuplicateEntityException;
import com.flockinger.spongeblogSP.exception.EntityIsNotExistingException;
import com.flockinger.spongeblogSP.model.Post;
import com.flockinger.spongeblogSP.model.enums.PostStatus;

@Service
@Transactional
public class PostServiceImpl implements PostService {

	@Autowired
	private PostDAO dao;

	@Autowired
	private ModelMapper mapper;

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
	public PostDTO createPost(PostDTO post) throws DuplicateEntityException {
		if (isPostNameExistingAlready(post)) {
			throw new DuplicateEntityException("Post");
		}

		return map(dao.save(map(post)));
	}

	private boolean isPostNameExistingAlready(PostDTO post) {
		return dao.findByTitle(post.getTitle()) != null;
	}

	@Override
	public void updatePost(PostDTO post) throws EntityIsNotExistingException, DuplicateEntityException {
		if (!isPostExisting(post.getId())) {
			throw new EntityIsNotExistingException("Post");
		}

		dao.save(map(post));
	}

	private boolean isPostExisting(Long id) {
		return dao.exists(id);
	}

	@Override
	public void deletePost(PostDTO post) throws EntityIsNotExistingException {
		if (!isPostExisting(post.getId())) {
			throw new EntityIsNotExistingException("Post");
		}

		dao.delete(post.getId());
	}

	private List<PostLink> map(List<Long> ids) {
		return ids.stream().map(post -> map(post)).collect(Collectors.toCollection(LinkedList::new));
	}

	private PostLink map(Long id) {
		PostLink link = new PostLink();
		link.setId(id);

		return link;
	}

	private PostDTO map(Post post) {
		return mapper.map(post, PostDTO.class);
	}

	private Post map(PostDTO post) {
		return mapper.map(post, Post.class);
	}

}
