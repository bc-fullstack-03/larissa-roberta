package com.sysmap.parrot.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import com.sysmap.parrot.model.Followers;
import com.sysmap.parrot.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Larissa Roberta
 * @description Classe de resposta relacionada ao usuário
 */
@Data
@NoArgsConstructor
public class UserResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	private UUID id;
	private String name;
	private String email;
	private Followers followers;
	private LocalDateTime created;
	private String photo;

	public UserResponse(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
		this.id = user.getId();
		this.created = user.getCreated();
		this.followers = user.getFollowers();
		this.photo = user.getPhoto();
	}
}
