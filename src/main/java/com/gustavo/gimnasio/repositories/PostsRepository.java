package com.gustavo.gimnasio.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.gimnasio.entity.Posts;

@Repository
public interface PostsRepository extends CrudRepository<Posts, Long> {

}
