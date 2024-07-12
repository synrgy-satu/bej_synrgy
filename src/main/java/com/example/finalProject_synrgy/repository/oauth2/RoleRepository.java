package com.example.finalProject_synrgy.repository.oauth2;

import com.example.finalProject_synrgy.entity.oauth2.Role;
import com.example.finalProject_synrgy.entity.oauth2.RolePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<RolePath> {
    Role findOneByName(String name);

    List<Role> findByNameIn(String[] names);
}
