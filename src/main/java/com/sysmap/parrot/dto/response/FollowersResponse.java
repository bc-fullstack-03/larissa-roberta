package com.sysmap.parrot.dto.response;

import java.util.UUID;

import com.sysmap.parrot.model.User;

import lombok.Data;

@Data
public class FollowersResponse {
	private UUID id;
	private String name;
	private String photo;
	private String email;
	private Integer followersCount;
	private Integer followingCount;

	public FollowersResponse(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.photo = user.getPhoto();
		this.email = user.getEmail();
		this.followersCount = user.getFollowers().getFollowersCount();
		this.followingCount = user.getFollowers().getFollowingCount();
	}
}