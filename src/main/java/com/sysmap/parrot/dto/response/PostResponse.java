package com.sysmap.parrot.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sysmap.parrot.model.Post;

import lombok.Data;

@Data
public class PostResponse {
	private UUID id;
	private UUID userId;
	private LocalDateTime date;
	private String description;
	private String photo;

	public PostResponse(Post post) {
		this.id = post.getId();
		this.userId = post.getUserId();
		this.date = post.getDateTime();
		this.description = post.getDescription();
		this.photo = post.getImageUrl();
	}
}
