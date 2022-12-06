package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ChatsListDto implements Dto{
    private final List<ChatDto> list;
}
