package com.sc2006.g5.edufinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sc2006.g5.edufinder.model.DbSchool;

public interface DbSchoolRepository extends JpaRepository<DbSchool, Long> {
    List<DbSchool> findByName(String name);
}