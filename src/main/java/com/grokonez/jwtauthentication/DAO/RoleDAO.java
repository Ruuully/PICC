package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Metier;
import com.grokonez.jwtauthentication.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long>  {
}
