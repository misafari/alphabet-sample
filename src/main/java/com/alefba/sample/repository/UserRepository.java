package com.alefba.sample.repository;

import com.alefba.sample.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByUsernameAndDeletedAtIsNull(String username);
}
