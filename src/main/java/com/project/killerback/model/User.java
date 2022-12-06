package com.project.killerback.model;

import com.project.killerback.model.player.Player;
import lombok.Getter;
import lombok.Setter;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class User {

    private static long idIncrement = 0;

    private long id;

    private String name;

    private Room room;

    private Player player;

    private List<Discussion> discussions;

    private String pathUserImg;

    public static List<User> USERS = new ArrayList<>();

    public static User of(String name) {
        return new User(name);
    }

    private User(String name) {
        idIncrement++;

        this.name = name;
        this.id = idIncrement;
        this.discussions = new ArrayList<>();

        USERS.add(this);
       // initData();

        //Transfer transfer = new Transfer("/set/id", new UserDto(id), RESPONSE);
        //this.send(transfer);

    }

    public void joinRoom(Room room){
        this.room = room;
    }

    public void speakInChat(Chat chat){
        this.room.addChat(chat);
    }

    public Optional<Discussion> discussionWithUserExist(User user){
        return this.discussions
                .stream()
                .filter(x -> x.equal(this, user))
                .findAny();
    }

    public void addDiscussion(Discussion discussion){
        this.discussions.add(discussion);
    }

    public void speakWithAnotherPlayer(Chat chat, User recipientUser){
        Optional<Discussion> optionalDiscussion = this.discussionWithUserExist(recipientUser);
        Discussion discussion;

        if(optionalDiscussion.isEmpty()){
            discussion = new Discussion(this, recipientUser);
            this.addDiscussion(discussion);
            recipientUser.addDiscussion(discussion);
        }else{
            discussion = optionalDiscussion.get();
        }

        discussion.addChat(chat);
    }

    public Discussion getDiscussionsWith(User user){
        return this.discussions
                .stream()
                .filter(x -> x.equal(this, user))
                .findAny()
                .orElse(null);
    }

    public void destroy() throws Exception {
        if(this.room != null){
            this.room.removeUser(this);
        }
        USERS.remove(this);
       // Notification.sendInRoom("/room/user/notification", null, this.room.getIdRoom());
        //Notification.sendAll("/join/room/all/notification", new RoomDto(this.room.getIdRoom(), this.room.getNameRoom(), this.room.getUsers().size()));
    }

    // Methods static

    public static void addUser(User user){
        USERS.add(user);
    }

    public static User getUser(long id) throws Exception {
        Optional<User> optionalConnection = USERS
                .stream()
                .filter(x -> x.getId() == id)
                .findAny();

        if(optionalConnection.isEmpty())
            throw new Exception("This " + id + " user do not exist");

        return optionalConnection.get();
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", room=" + room +
                ", player=" + player +
                '}';
    }

}
