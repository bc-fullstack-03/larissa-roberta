package com.sysmap.parrot.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sysmap.parrot.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, UUID> {
	List<Post> findAllByAuthorIdIn(List<UUID> followersId);

	List<Post> findAllByAuthorId(UUID uuid);
}
