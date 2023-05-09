package com.sysmap.parrot.service;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sysmap.parrot.dto.request.UserRequest;
import com.sysmap.parrot.dto.response.FollowersResponse;
import com.sysmap.parrot.dto.response.UserResponse;
import com.sysmap.parrot.model.User;

public interface IUserService {
	UserResponse findUserByEmail(String userEmail);

	User findUserById(UUID userId);

	void deleteUser();

	UUID createUser(UserRequest request);

	User updateUser(UserRequest request);

	UserResponse addFollower(String followerEmail);

	UserResponse unfollow(String followerEmail);

	List<FollowersResponse> getUsersToFollow();

	List<UUID> getAllFollowedId();

	void uploadPhotoProfile(MultipartFile photo);

}
