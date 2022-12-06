package com.project.killerback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Stack;

@AllArgsConstructor
@Getter
public class DiscussionDto implements Dto {
    private final List<ChatDto> listChat;
    private final long idUser1;
    private final long idUser2;
}
