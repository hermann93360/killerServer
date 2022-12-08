package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class VoteDto {
    private final long idMainUser;
    private final long idTargetUser;
    private final long idRoom;
    private final long numberOfVoteTargetUser;
}
