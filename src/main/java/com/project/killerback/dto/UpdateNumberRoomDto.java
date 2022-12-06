package com.project.killerback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateNumberRoomDto {
    private final long idRoom;
    private final int numberOfUser;
}
