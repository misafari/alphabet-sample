package com.alefba.sample.repository;

import com.alefba.sample.model.Role;
import com.alefba.sample.model.RoleName;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
