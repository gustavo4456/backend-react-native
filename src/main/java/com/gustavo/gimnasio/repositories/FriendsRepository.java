package com.gustavo.gimnasio.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gustavo.gimnasio.entity.Friends;

@Repository
public interface FriendsRepository extends CrudRepository<Friends, Long>{
    
}
