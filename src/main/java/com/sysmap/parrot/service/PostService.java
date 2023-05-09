package com.sysmap.parrot.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.sysmap.parrot.dto.request.CommentRequest;
import com.sysmap.parrot.dto.request.PostRequest;
import com.sysmap.parrot.exception.InternalServerErrorException;
import com.sysmap.parrot.model.*;
import com.sysmap.parrot.repository.PostRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PostService implements IPostService {
	@Autowired
	private PostRepository repository;
	@Autowired
	private IFileUploadService fileUploadService;
	@Autowired
	private UserService userService;

	public Post createPost(PostRequest request) throws InternalServerErrorException {
		try {
			var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			Post post = new Post(request.getDescription(), new Author(user));
			return repository.save(post);
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro ao criar um post");
		}
	}

	public void uploadPhotoPost(MultipartFile photo, String postId) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Post post = findPost(postId);
		if (post.getAuthor().getId().equals(user.getId())) {
			if (post != null) {
				if (!photo.isEmpty()) {
					try {
						var fileName = post.getId() + "."
								+ photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1);
						post.setImageUrl(fileUploadService.upload(photo, fileName));
					} catch (Exception e) {
						throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
					}
					repository.save(post);
				} else {
					post.setImageUrl("");
					repository.save(post);
				}
			}
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have authorization to edit this post");
		}
	}

	public List<Post> findAllPosts() {
		return repository.findAll();
	}

	public List<Post> findAllPostsByFollowed(List<UUID> followersId) {
		return repository.findAllByAuthorIdIn(followersId);
	}

	public Post findPost(String postId) {
		return repository.findById(UUID.fromString(postId)).orElseThrow(() -> new NotFoundException("Post não encontrado"));
	}

	public List<Post> findAllByUserId(String userId) throws NotFoundException {
		try {
			return repository.findAllByAuthorId(UUID.fromString(userId));
		} catch (Exception e) {
			log.error(e);
			throw new NotFoundException("Usuário não encontrado");
		}
	}

	public Post updatePost(String postId, PostRequest request) throws InternalServerErrorException {
		try {
			var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			Post post = findPost(postId);
			if (post.getAuthor().getId().equals(user.getId())) {
				post.setDescription(request.getDescription());
				return repository.save(post);
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro ao atualizar o post");
		}
		return null;
	}

	public String likePost(String postId, String userId) throws InternalServerErrorException {
		try {
			var post = repository.findById(UUID.fromString(postId));
			var userLike = userService.findUserById(UUID.fromString(userId));
			var like = new Like(userLike);

			if (post.get().getLikes().contains(like)) {
				post.get().getLikes().remove(like);
				repository.save(post.get());
				return "Like removido!";
			} else {
				post.get().getLikes().add(like);
				repository.save(post.get());
				return "Like adicionado!";
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}
	}

	public void deleteAllUserPostsAndLikes(UUID userId) throws InternalServerErrorException {
		try {
			List<Post> allPosts = findAllPosts();
			for (Post post : allPosts) {
				List<Like> likes = post.getLikes();
				List<Comment> comments = post.getComments();

				for (Comment comment : comments) {
					List<Like> commentLikes = comment.getLike();
					commentLikes.removeIf(like -> like.getUserId().equals(userId));
					comment.setLike(commentLikes);
					comment.setLikesCount(commentLikes.size());
				}

				likes.removeIf(like -> like.getUserId().equals(userId));
				comments.removeIf(c -> c.getAuthor().getId().equals(userId));

				post.setLikes(likes);
				post.setLikesCount(post.getLikes().size());
				post.setComments(comments);
				repository.save(post);

				if (post.getAuthor().getId().equals(userId)) {
					repository.delete(post);
				}
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}
	}

	public void deletePost(String postId) throws InternalServerErrorException {
		try {
			var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			Post post = findPost(postId);
			if (user.getId().equals(post.getAuthor().getId())) {
				repository.delete(post);
			} else {
				throw new InternalServerErrorException("Sem permissão");
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro ao deletar o post");

		}
	}

	public Post newComment(String postId, CommentRequest comment) throws InternalServerErrorException {
		try {
			var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
			Post post = findPost(postId);
			post.getComments().add(new Comment(comment.getComment(), new Author(user)));
			return repository.save(post);
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro ao comentar o post");
		}

	}

	public Post updateComment(String postId, String commentId, CommentRequest updateC) throws InternalServerErrorException {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Post post = findPost(postId);
		try {
			Comment comment = post.getComments().stream().filter(c -> c.getId().equals(UUID.fromString(commentId))).findFirst().get();
		} catch (Exception e) {
			throw new NotFoundException("Comentário não encontrado");
		}
		Comment comment = post.getComments().stream().filter(c -> c.getId().equals(UUID.fromString(commentId))).findFirst().get();
		if (!user.getId().equals(comment.getAuthor().getId())) {
			throw new InternalServerErrorException("Usuário sem permissão");
		}
		comment.setComment(updateC.getComment());
		return repository.save(post);
	}

	public Post deleteComment(String postId, String commentId) throws InternalServerErrorException {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Post post = findPost(postId);
		try {
			Comment comment = post.getComments().stream().filter(c -> c.getId().equals(UUID.fromString(commentId))).findFirst()
					.orElseThrow(() -> new NotFoundException("Comentário não encontrado"));

			if (!user.getId().equals(post.getAuthor().getId()) && !user.getId().equals(comment.getAuthor().getId())) {
				throw new InternalServerErrorException("Usuário sem permissão");
			}

			post.getComments().remove(comment);
			post.setComments(post.getComments());

			return repository.save(post);
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}
	}

	public Post likeComment(String postId, String commentId) throws InternalServerErrorException {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Post post = findPost(postId);
		try {
			Comment comment = post.getComments().stream().filter(c -> c.getId().equals(UUID.fromString(commentId))).findFirst()
					.orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
			if (!comment.getLike().stream().anyMatch(userLike -> userLike.getUserId().equals(user.getId()))) {
				comment.getLike().add(new Like(user));
				comment.setLikesCount(comment.getLike().size());

				return repository.save(post);
			} else {
				throw new InternalServerErrorException("O usuário já curtiu o comentário");
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}
	}

	public Post unlikeComment(String postId, String commentId) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		Post post = findPost(postId);
		try {
			Comment comment = post.getComments().stream().filter(c -> c.getId().equals(UUID.fromString(commentId))).findFirst()
					.orElseThrow(() -> new NotFoundException("Comentário não encontrado"));
			List<Like> likes = comment.getLike();
			if (!likes.removeIf(userLike -> userLike.getUserId().equals(user.getId()))) {
				throw new InternalServerErrorException("O usuário já descurtiu o comentário");
			}
			comment.setLike(likes);
			comment.setLikesCount(comment.getLike().size());
			return repository.save(post);
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}
	}
}
