package com.project.killerback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class RoomsListDto implements Dto {
    private List<RoomDto> list;
}
