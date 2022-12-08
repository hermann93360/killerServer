package com.project.killerback.application.controller;

import com.project.killerback.application.dto.*;
import com.project.killerback.domain.exception.RoomException;
import com.project.killerback.domain.model.Chat;
import com.project.killerback.domain.model.Discussion;
import com.project.killerback.domain.model.Room;
import com.project.killerback.domain.model.User;
import com.project.killerback.domain.model.player.Player;
import com.project.killerback.domain.services.MapperService;
import com.project.killerback.domain.services.RoomService;
import com.project.killerback.domain.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static com.project.killerback.domain.services.MapperService.*;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
public class MainController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final RoomService roomService;

    private final UserService userService;

    @MessageMapping("/socket/newUser")
    @SendTo("/socket/newUser")
    public UserDto addUser(UserDto userDto) {
        User user = this.userService.addUser(userDto.getName());
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

        //to refacto later

        Room room =  Room.getRoom(joinRoomDto.getIdRoom());

        if(!room.canAddUser()) {
            room.start();
            new TimerRoom(20, room, this);
            this.simpMessagingTemplate.convertAndSend("/socket/startInRoom/" + room.getIdRoom(), toRoomDto(room));
        }
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

    //toRefacto

    @MessageMapping("/socket/vote")
    public void sendPublicChat(VoteDto voteDto) throws Exception {
        Player playerMain = User.getUser(voteDto.getIdMainUser()).getPlayer();
        Player playerTarget = User.getUser(voteDto.getIdTargetUser()).getPlayer();

        if(playerMain.isAlreadyVote()){
            throw new RoomException("This player has already vote for a another player");
        }

        playerMain.vote(playerTarget);
        VoteDto voteDtoResponse = new VoteDto(voteDto.getIdMainUser(), voteDto.getIdTargetUser(), voteDto.getIdRoom(), playerTarget.getVote());

        this.simpMessagingTemplate.convertAndSend("/socket/votePublic/" + voteDto.getIdRoom(), voteDtoResponse);
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

    public void send(String path, Object o){
        this.simpMessagingTemplate.convertAndSend(path, o);
    }
}
