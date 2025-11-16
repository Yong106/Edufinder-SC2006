package com.sc2006.g5.edufinder.dto.request;

import com.sc2006.g5.edufinder.model.comment.VoteType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contains data needed to manage saved school.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetVoteRequest {

    @NotNull
    private VoteType voteType;
}
