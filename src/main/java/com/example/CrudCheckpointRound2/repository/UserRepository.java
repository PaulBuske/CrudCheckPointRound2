package com.example.CrudCheckpointRound2.repository;

import com.example.CrudCheckpointRound2.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
    Boolean existsAllByEmail(String email);

}
