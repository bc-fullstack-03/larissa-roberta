package com.sysmap.parrot.model;

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
@Document("user")
public class User {
	@Id
	private UUID id;
	private String name;
	private String email;
	private String password;

	public User(String name, String email, String password) {
		super();
		this.setId();
		this.name = name;
		this.email = email;
		this.password = password;
	}

	protected void setId() {
		this.id = UUID.randomUUID();
	}

	public UUID getId() {
		return this.id;
	}

}
