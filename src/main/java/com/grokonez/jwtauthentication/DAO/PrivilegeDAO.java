package com.grokonez.jwtauthentication.DAO;

import com.grokonez.jwtauthentication.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeDAO extends JpaRepository<Privilege, Long> {
}
