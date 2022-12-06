package com.project.killerback.domain.services;

import com.project.killerback.domain.model.Chat;
import com.project.killerback.domain.model.Discussion;
import com.project.killerback.domain.model.Room;
import com.project.killerback.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static com.project.killerback.domain.model.Room.getRoom;
import static com.project.killerback.domain.model.User.getUser;

@Service
public class UserService {

    public List<User> getAllUsers() {
        return User.USERS;
    }

    public Optional<Room> removeUser(long idUser) throws Exception {
        User user = getUser(idUser);
        Optional<Room> optionalRoom;

        if(user.getRoom() != null) {
            optionalRoom = Optional.of(user.getRoom());
        }else{
            optionalRoom = Optional.empty();
        }
        user.destroy();

        return optionalRoom;
    }

    public Chat sendPublicChat(String message, long idUser) throws Exception {
        User user = getUser(idUser);
        Chat chat = new Chat(user, message);
        user.speakInChat(chat);
        return chat;
    }

    public Stack<Chat> getChatsInRoom(long idRoom) throws Exception {
        Room room = getRoom(idRoom);
        return room.getChats();
    }

    public Discussion sendPrivateChat(String message, long idSender, long idDestination) throws Exception {
        User userSender = getUser(idSender);
        User userRecipient = getUser(idDestination);

        Chat chat = new Chat(userSender, message);

        userSender.speakWithAnotherPlayer(chat, userRecipient);

        return userSender.getDiscussionsWith(userRecipient);
    }

    public User addUser(String name) {
        return User.of(name);
    }
}
