package com.sc2006.g5.edufinder.model.comment;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an embeddable id for {@link Vote}.
 */
@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteId implements Serializable{
    private Long commentId;
    private Long userId;
}
