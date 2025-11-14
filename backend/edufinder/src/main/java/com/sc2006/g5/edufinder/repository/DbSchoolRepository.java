package com.sc2006.g5.edufinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.school.DbSchool;

/**
 * JPA Repository for {@link DbSchool}
 */
public interface DbSchoolRepository extends JpaRepository<DbSchool, Long> {
    Optional<DbSchool> findOneByName(String name);
}