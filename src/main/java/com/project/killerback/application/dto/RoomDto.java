package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RoomDto implements Dto{
    private final long id;
    private final String name;
    private final int numberOfUser;
    private final List<String> icons;
}
