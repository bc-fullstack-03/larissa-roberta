package com.sysmap.parrot.model;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("author")
public class Author {
	private UUID id;
	private String name;

	public Author(User user) {
		this.id = user.getId();
		this.name = user.getName();
	}
}
