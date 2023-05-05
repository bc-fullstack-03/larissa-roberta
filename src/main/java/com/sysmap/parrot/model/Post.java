package com.sysmap.parrot.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document("post")
public class Post {
	@Id
	private UUID id;
	private String description;
	private Boolean image = false;
	private String imageUrl;
	private UUID userId;
	private LocalDateTime dateTime = LocalDateTime.now();
	private List<Comment> comments;
	private List<Like> likes;

	public Post(UUID userId, String description) {
		this.id = UUID.randomUUID();
		this.userId = userId;
		this.description = description;
	}

	public List<Comment> addComment(Comment comment) {
		this.comments.add(comment);
		return this.comments;
	}

	public List<Like> addLike(Like like) {
		this.likes.add(like);
		return this.likes;
	}

}
