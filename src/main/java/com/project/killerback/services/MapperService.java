package com.project.killerback.services;

import com.project.killerback.dto.ChatDto;
import com.project.killerback.dto.DiscussionDto;
import com.project.killerback.dto.RoomDto;
import com.project.killerback.dto.UserDto;
import com.project.killerback.model.Chat;
import com.project.killerback.model.Discussion;
import com.project.killerback.model.Room;
import com.project.killerback.model.User;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class MapperService {

    public static RoomDto toRoomDto(Room r) {
        return new RoomDto(
                r.getIdRoom(),
                r.getNameRoom(),
                r.getUsers().size(),
                r.getIconPlayers());
    }

    public static UserDto toUserDto(User u) {
        return new UserDto(
                u.getName(),
                u.getId(),
                (u.getRoom() != null ) ? u.getRoom().getIdRoom() : 0,
                u.getPathUserImg(),
                (u.getRoom() != null) ? u.getRoom().getUsers().size() : 0);
    }

    public static List<UserDto> toListUserDto(List<User> ul) {
        return ul.stream()
                .map(MapperService::toUserDto)
                .collect(Collectors.toList());
    }

    public static List<RoomDto> toListRoomDto(List<Room> rl) {
        return rl.stream()
                .map(MapperService::toRoomDto)
                .collect(Collectors.toList());
    }

    public static ChatDto toChatDtoForPublicChat(Chat c) {
        return new ChatDto(c.getMessage(), c.getUser().getName(), c.getUser().getId(), c.getUser().getRoom().getIdRoom(), -1);
    }

    public static List<ChatDto> toListChatDtoForPublicChat(List<Chat> chats) {
        return chats.stream()
                .map(MapperService::toChatDtoForPublicChat)
                .collect(Collectors.toList());
    }

    public static ChatDto toChatDtoForPrivateChat(Chat c) {
        return new ChatDto(c.getMessage(), c.getUser().getName(), c.getUser().getId(), -1, -1);
    }

    public static List<ChatDto> toListChatDtoForPrivateChat(Stack<Chat> chats) {
        return chats.stream()
                .map(MapperService::toChatDtoForPrivateChat)
                .collect(Collectors.toList());
    }

    public static DiscussionDto toDiscussionDto(Discussion discussion) {
        return new DiscussionDto(toListChatDtoForPrivateChat(discussion.getChats()), discussion.getUser1().getId(), discussion.getUser2().getId());
    }
}
