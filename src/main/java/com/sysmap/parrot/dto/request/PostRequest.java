package com.sysmap.parrot.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class PostRequest {
	private UUID id;
	private String title;
	private String description;
	private String photoUri;

}
