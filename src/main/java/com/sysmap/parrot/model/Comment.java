package com.sysmap.parrot.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("comment")
public class Comment {
	private String comment;
	private LocalDateTime date;
	private UUID userId;
	private List<Like> like;
	private Integer likesCount;
	private UUID id;
	private Author author;
	private UUID postId;

	public Comment(String comment, Author author) {
		this.id = UUID.randomUUID();
		this.comment = comment;
		this.date = LocalDateTime.now();
		this.author = author;
		this.likesCount = 0;
	}
}
