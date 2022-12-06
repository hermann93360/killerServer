package com.project.killerback.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.killerback.dto.*;
import com.project.killerback.model.Chat;
import com.project.killerback.model.Discussion;
import com.project.killerback.model.Room;
import com.project.killerback.model.User;
import com.project.killerback.services.MapperService;
import com.project.killerback.services.RoomService;
import com.project.killerback.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.core.MessageSendingOperations;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.project.killerback.services.MapperService.*;

@RestController
@CrossOrigin(origins = "*")
public class MainController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RoomService roomService;

    private final UserService userService;


    public MainController(SimpMessagingTemplate simpMessagingTemplate, RoomService roomService, UserService userService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.roomService = roomService;
        this.userService = userService;
    }

    @MessageMapping("/socket/newUser")
    @SendTo("/socket/newUser")
    public UserDto addUser(UserDto userDto) {
        User user = User.of(userDto.getName());
        return new UserDto(user.getName(), user.getId(), 0, null, -1);
    }

    @MessageMapping("/socket/newRoom")
    @SendTo("/socket/newRoom")
    public RoomDto newRoom(RoomDto roomDto) {
        Room room = roomService.addRoom(roomDto.getName());
        return new RoomDto(room.getIdRoom(), room.getNameRoom(), 0, room.getIconPlayersToAttribut());
    }

    @MessageMapping("/socket/joinRoom")
    public void joinRoom(JoinRoomDto joinRoomDto) throws Exception {
        User user = this.roomService.joinRoom(joinRoomDto.getIdRoom(), joinRoomDto.getIdUser());

        this.simpMessagingTemplate.convertAndSend(
                "/socket/someoneJoined/" + joinRoomDto.getIdRoom(),
                new JoinRoomDto(
                        user.getRoom().getIdRoom(),
                        joinRoomDto.getIdUser(),
                        user.getPathUserImg())
        );

        this.simpMessagingTemplate.convertAndSend("/socket/someoneJoinAnyRoom", new UpdateNumberRoomDto(joinRoomDto.getIdRoom(), user.getRoom().getUsers().size()));
    }

    @MessageMapping("/socket/disconnect")
    public void disconnect(UserDto userDto) throws Exception {
        System.out.println("exit");
        Optional<Room> room = this.userService.removeUser(userDto.getId());
        room.ifPresent(value -> this.simpMessagingTemplate.convertAndSend("/socket/someoneLeaveAnyRoom", new UpdateNumberRoomDto(value.getIdRoom(), value.getUsers().size())));
    }

    @MessageMapping("/socket/sendPublicChat")
    public void sendPublicChat(ChatDto chatDto) throws Exception {
        Chat chat = this.userService.sendPublicChat(chatDto.getMessage(), chatDto.getIdSender());
        this.simpMessagingTemplate.convertAndSend("/socket/publicChat/" + chatDto.getIdDestination(), toChatDtoForPublicChat(chat));
    }

    @MessageMapping("/socket/sendPrivateChat")
    public void sendPrivateChat(ChatDto chatDto) throws Exception {
        System.out.println("j'envoie");
        Discussion discussion = this.userService.sendPrivateChat(chatDto.getMessage(), chatDto.getIdSender(), chatDto.getIdDestination());
        Chat lastChat = discussion.getLastChat();

        this.simpMessagingTemplate.convertAndSend("/socket/privateChat/" + chatDto.getIdDestination(), toDiscussionDto(discussion));
        this.simpMessagingTemplate.convertAndSend("/socket/privateChat/" + chatDto.getIdSender(), toDiscussionDto(discussion));
        this.simpMessagingTemplate.convertAndSend("/socket/privateChatNotif/" + chatDto.getIdDestination(), toChatDtoForPrivateChat(lastChat));
    }

    @GetMapping("/get/users")
    public ResponseEntity<List<UserDto>> getUsers() {
       List<User> users = this.userService.getAllUsers();
       return ResponseEntity.ok(toListUserDto(users));
    }

    @GetMapping("/get/rooms")
    public ResponseEntity<List<RoomDto>> getRooms() {
        List<Room> room = this.roomService.getAllRooms();
        return ResponseEntity.ok(toListRoomDto(room));
    }

    @GetMapping("/get/users/{idRoom}")
    public ResponseEntity<List<UserDto>> getUserInRoom(@PathVariable long idRoom) throws Exception {
        List<User> users = this.roomService.getUsersByRoom(idRoom);
        return ResponseEntity.ok(toListUserDto(users));
    }

    @GetMapping("/get/chats/{idRoom}")
    public ResponseEntity<List<ChatDto>> getChatsInRoom(@PathVariable long idRoom) throws Exception {
        Stack<Chat> chats = this.userService.getChatsInRoom(idRoom);
        return ResponseEntity.ok(toListChatDtoForPublicChat(chats));
    }
}
