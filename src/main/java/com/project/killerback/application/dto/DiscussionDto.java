package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class DiscussionDto implements Dto {
    private final List<ChatDto> listChat;
    private final long idUser1;
    private final long idUser2;
}
