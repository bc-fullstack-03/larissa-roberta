package com.sysmap.parrot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.alexaforbusiness.model.NotFoundException;
import com.sysmap.parrot.dto.request.UserRequest;
import com.sysmap.parrot.dto.response.FollowersResponse;
import com.sysmap.parrot.dto.response.UserResponse;
import com.sysmap.parrot.exception.InternalServerErrorException;
import com.sysmap.parrot.model.Author;
import com.sysmap.parrot.model.User;
import com.sysmap.parrot.repository.UserRepository;
import com.sysmap.parrot.validation.EmailValidation;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserService implements IUserService {
	@Autowired
	private UserRepository repository;
	@Autowired
	private IFileUploadService fileUploadService;
	@Autowired
	private IPostService postService;

	public UserResponse findUserByEmail(String userEmail) {
		try {
			User getUser = repository.findUserByEmail(userEmail);
			if (getUser == null) {
				throw new NotFoundException("Usuário não encontrado");
			} else {
				UserResponse userResponse = new UserResponse(getUser);
				return userResponse;
			}
		} catch (Exception e) {
			log.error(e);
			throw new InternalServerErrorException("Ocorreu um erro");
		}

	}

	public User findUserById(UUID userId) {
		User getUser = repository.findById(userId).get();
		if (getUser == null) {
			throw new NotFoundException("Usuário não encontrado");
		} else {
			return getUser;
		}
	}

	public void deleteUser() {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

		postService.deleteAllUserPostsAndLikes(user.getId());

		List<Author> followers = user.getFollowers().getFollowingList();
		for (Author f : followers) {
			User followerUser = repository.findById(f.getId()).orElse(null);
			if (followerUser != null) {
				followerUser.getFollowers().getFollowersList().removeIf(u -> u.getId().equals(user.getId()));
				followerUser.getFollowers().setFollowersCount(followerUser.getFollowers().getFollowersList().size());
				repository.save(followerUser);
			}
		}
		List<Author> following = user.getFollowers().getFollowersList();
		for (Author f : following) {
			User followingUser = repository.findById(f.getId()).orElse(null);
			if (followingUser != null) {
				followingUser.getFollowers().getFollowingList().removeIf(u -> u.getId().equals(user.getId()));
				followingUser.getFollowers().setFollowingCount(followingUser.getFollowers().getFollowingList().size());
				repository.save(followingUser);
			}
		}
		repository.deleteById(user.getId());
	}

	public UUID createUser(UserRequest request) {
		if (EmailValidation.isValidEmail(request.getEmail())) {
			if (repository.findUserByEmail(request.getEmail()) == null) {
				var user = new User(request.getName(), request.getEmail(), request.getPassword());
				repository.save(user);
				return user.getId();
			}
			throw new InternalServerErrorException("E-mail já cadastrado");
		}
		throw new InternalServerErrorException("Insira um E-mail válido");
	}

	public void uploadPhotoProfile(MultipartFile photo) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (!photo.isEmpty()) {
			try {
				var fileName = user.getId() + "." + photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".") + 1);
				user.setPhoto(fileUploadService.upload(photo, fileName));
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
			}
			repository.save(user);
		} else {
			user.setPhoto("");
			repository.save(user);
		}
	}

	public User updateUser(UserRequest request) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (EmailValidation.isValidEmail(request.getEmail())) {
			if (user.getEmail().equals(request.getEmail()) || repository.findUserByEmail(request.getEmail()) == null) {
				user.setEmail(request.getEmail());
			} else {
				throw new InternalServerErrorException("E-mail já cadastrado");
			}
		} else {
			throw new InternalServerErrorException("Insira um E-mail válido");
		}
		return repository.save(user);
	}

	public UserResponse addFollower(String followerEmail) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		User follower = findUserById(findUserByEmail(followerEmail).getId());
		if (!user.getId().equals(follower.getId())) {
			if (user.getFollowers().getFollowingList().stream().anyMatch(f -> f.getId().equals(follower.getId()))) {
				throw new InternalServerErrorException("Você já segue esse usuário");
			}
			Author followUser = new Author(follower);
			user.getFollowers().getFollowingList().add(followUser);
			user.getFollowers().setFollowingCount(user.getFollowers().getFollowingList().size());

			Author followerUser = new Author(user);
			var userF = findUserById(follower.getId());
			userF.getFollowers().getFollowersList().add(followerUser);
			userF.getFollowers().setFollowersCount(userF.getFollowers().getFollowersList().size());
			repository.save(user);
			repository.save(userF);
			return findUserByEmail(user.getEmail());
		} else {
			throw new InternalServerErrorException("Você não pode seguir você mesmo!");
		}
	}

	public UserResponse unfollow(String followerEmail) {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		User follower = findUserById(findUserByEmail(followerEmail).getId());
		if (user.getFollowers().getFollowingList().removeIf(f -> f.getId().equals(follower.getId()))
				&& follower.getFollowers().getFollowersList().removeIf(f -> f.getId().equals(user.getId()))) {
			user.getFollowers().setFollowingCount(user.getFollowers().getFollowingList().size());
			follower.getFollowers().setFollowersCount(follower.getFollowers().getFollowersList().size());
			repository.save(user);
			repository.save(follower);
			return new UserResponse(user);
		}
		throw new NotFoundException("Usuário não encontrado");
	}

	public List<FollowersResponse> getUsersToFollow() {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		var exceptionIds = user.getFollowers().getFollowingList().stream().map(f -> f.getId()).collect(Collectors.toList());
		exceptionIds.add(user.getId());

		var usersListToFollow = repository.findAllByIdNotIn(exceptionIds);

		return usersListToFollow.stream().map(u -> new FollowersResponse(u)).collect(Collectors.toList());
	}

	public List<UUID> getAllFollowedId() {
		var user = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		if (user.getFollowers().getFollowingList().size() == 0) {
			List<UUID> followerId = new ArrayList<>(List.of(user.getId()));
			return followerId;
		}
		var followerId = user.getFollowers().getFollowingList().stream().map(f -> f.getId()).collect(Collectors.toList());
		followerId.add(user.getId());
		return followerId;
	}

}
