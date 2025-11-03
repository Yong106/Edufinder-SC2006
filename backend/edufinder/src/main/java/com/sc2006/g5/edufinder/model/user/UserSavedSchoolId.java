package com.sc2006.g5.edufinder.model.user;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSavedSchoolId implements Serializable {
    private Long userId;
    private Long schoolId;
}
