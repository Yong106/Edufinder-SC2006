package com.sc2006.g5.edufinder.repository;

import java.util.Optional;

import com.sc2006.g5.edufinder.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.user.User;

/**
 * JPA Repository for {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByUsername(String username);
    Long countByRole(Role role);
}
