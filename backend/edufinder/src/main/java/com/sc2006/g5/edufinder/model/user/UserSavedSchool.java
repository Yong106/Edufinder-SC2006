package com.sc2006.g5.edufinder.model.user;

import com.sc2006.g5.edufinder.model.school.DbSchool;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_saved_schools")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedSchool {

    @EmbeddedId
    private UserSavedSchoolId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("schoolId")
    @JoinColumn(name = "school_id")
    private DbSchool school;
}
