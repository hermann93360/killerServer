package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatDto implements Dto {
    private final String message;
    private final String nameSender;
    private final long idSender;
    private final long idDestination;
    private final long idDiscussion;

}
