package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto implements Dto {
    private final String name;
    private final long id;
    private final long idRoom;
    private final String pathPlayerImg;
    private final int sizeOfUserInCurrentRoom;

}
