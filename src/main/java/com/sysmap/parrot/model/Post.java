package com.sysmap.parrot.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("post")
public class Post {
	@Id
	private UUID id;
	private String description;
	private String imageUrl;
	private Author author;
	private UUID userId;
	private LocalDateTime dateTime = LocalDateTime.now();
	private List<Comment> comments = new ArrayList<>();
	private List<Like> likes = new ArrayList<>();
	private Integer likesCount;

	public Post(String description, Author author) {
	}

	public Post(String title, String description, Author author) {
		this.id = UUID.randomUUID();
		this.dateTime = LocalDateTime.now();
		this.imageUrl = "";
		this.description = description;
		this.author = author;
		this.likesCount = 0;
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
