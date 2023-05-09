package com.sysmap.parrot.dto.request;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	private UUID id;
	private String name;
	private String email;
	private String password;
}
