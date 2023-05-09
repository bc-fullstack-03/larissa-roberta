package com.sysmap.parrot.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("followers")
public class Followers {
	private List<Author> followersList = new ArrayList<>();
	private List<Author> followingList = new ArrayList<>();
	private Integer followersCount;
	private Integer followingCount;

	public Followers() {
		this.followersCount = 0;
		this.followingCount = 0;
	}
}
