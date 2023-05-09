package com.sysmap.parrot.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("user")
public class User {
	@Id
	private UUID id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime created;
	private Followers followers;
	private String photo;

	public User(String name, String email, String password) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.email = email;
		this.password = password;
		this.created = LocalDateTime.now();
		this.followers = new Followers();
		this.photo = "https://i.imgur.com/zHoVjaF.jpeg";
	}

}
