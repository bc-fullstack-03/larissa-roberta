package com.sysmap.parrot.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Document("comment")
public class Comment {
	private UUID userId;
	private UUID postId;
	private String description;
	private LocalDateTime createdAt = LocalDateTime.now();
}
