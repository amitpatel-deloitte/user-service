package com.hashedin.user_service.repository;

import com.hashedin.user_service.model.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findRolesByName(String name);
}
