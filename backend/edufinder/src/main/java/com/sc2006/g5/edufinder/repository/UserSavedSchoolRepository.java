package com.sc2006.g5.edufinder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sc2006.g5.edufinder.model.UserSavedSchool;
import com.sc2006.g5.edufinder.model.embedded_id.UserSavedSchoolId;

public interface UserSavedSchoolRepository extends JpaRepository<UserSavedSchool, UserSavedSchoolId> {
    @Query("SELECT uss.id.schoolId FROM UserSavedSchool uss WHERE uss.id.userId = :userId")
    List<Long> findSchoolIdsByUserId(@Param("userId") Long userId);

    boolean existsByUserIdAndSchoolId(Long userId, Long schoolId);
    void deleteByUserIdAndSchoolId(Long userId, Long schoolId);
}
