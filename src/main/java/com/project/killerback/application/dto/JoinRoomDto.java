package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinRoomDto {
    private final long idRoom;
    private final long idUser;
    private final String pathPlayerImg;
}
