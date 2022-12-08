package com.project.killerback.application.controller;

import com.project.killerback.application.dto.ChatDto;
import com.project.killerback.domain.model.Chat;
import com.project.killerback.domain.services.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import static com.project.killerback.domain.services.MapperService.toChatDtoForPublicChat;

@Controller
@AllArgsConstructor
public class PlayController {

    private final RoomService roomService;

    private final SimpMessagingTemplate simpMessagingTemplate;
}

