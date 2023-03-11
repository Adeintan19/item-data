package com.buana.itemdata.repository.user;

import com.buana.itemdata.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
Role findByName(String name);
}
