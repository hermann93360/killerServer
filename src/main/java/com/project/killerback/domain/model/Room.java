package com.project.killerback.domain.model;

import com.project.killerback.domain.model.player.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Room {

    private long idRoom;
    private String nameRoom;
    private List<User> users;
    private Stack<Chat> chats;
    private int stateRoom;
    private int count;

    private List<Player> associatePlayer;

    private List<String> iconPlayers = new ArrayList<>() {{
        add("playeur1.png");
        add("playeur2.png");
        add("playeur3.png");
        add("playeur4.png");
        add("playeur5.png");
        add("playeur6.png");
    }};
    private List<String> iconPlayersToAttribut;

    private static long idIncrement = 0;
    private final static int MAX_PLAYER = 6;

    public static List<Room> ROOMS = new ArrayList<>();

    public static Room of(String nameRoom){
        return new Room(nameRoom);
    }

    private Room(String nameRoom){
        Collections.shuffle(iconPlayers);
        iconPlayersToAttribut = new ArrayList<>(iconPlayers);
        idIncrement++;

        this.idRoom = idIncrement;
        this.nameRoom = nameRoom;
        this.users = new ArrayList<>();
        this.chats = new Stack<>();

        this.associatePlayer = new ArrayList<>();
        associatePlayer.add(new Boss());
        associatePlayer.add(new Tourist());
        associatePlayer.add(new Voyeur(this));
        associatePlayer.add(new Killer());
        associatePlayer.add(new Villager());
        associatePlayer.add(new Villager());

        ROOMS.add(this);
    }

    public void removeUser(User user){
        this.users.remove(user);
        this.iconPlayersToAttribut.add(user.getPathUserImg());
    }

    private int sizeOfUsers(){
        return this.users.size();
    }

    private boolean canAddUser(){
        return this.sizeOfUsers() < MAX_PLAYER;
    }

    public void addUser(User user) throws Exception {
        if(!canAddUser())
                throw new Exception("can't add a connection in this room");

        this.users.add(user);
        user.setRoom(this);
        setRandomImgPlayer(user);

        if(sizeOfUsers() == MAX_PLAYER){
            this.start();
        }
    }

    private void setRandomImgPlayer(User user){
        Random random = new Random();
        int randomValuePlayer = random.nextInt(iconPlayersToAttribut.size());

        user.setPathUserImg(iconPlayersToAttribut.get(randomValuePlayer));

        iconPlayersToAttribut.remove(randomValuePlayer);
    }

    public void start(){
        setAllPlayer();
        runTimer();
    }


    public void runTimer(){
        new TimerRoom(20, this, true);
    }

    public boolean someoneWin(){
        return this.users
                .stream()
                .filter(x -> !x.getPlayer().isDead())
                .count() == 1;
    }

    public void setAllPlayer(){
        this.users.forEach(user -> user.setPlayer(attributPlayerToUser()));
    }

    private Player attributPlayerToUser() {
        Random random = new Random();
        int randomValue = random.nextInt(this.associatePlayer.size());

        Player player = this.associatePlayer.get(randomValue);
        this.associatePlayer.remove(randomValue);

        return player;
    }

    public void killUser() {
        this.users.sort((o1, o2) -> (o1.getPlayer().getVote() <= o2.getPlayer().getVote()) ? 0 : -1);
        User user = this.users.get(0);
        user.getPlayer().kill();
        this.users.forEach(x -> x.getPlayer().resetVote());

    }

    public List<Chat> getAllChats() {
        return this.chats;
    }

    public void addChat(Chat chat) {
        this.chats.push(chat);
    }

    public void destroy(){
        ROOMS.remove(this);
        //Notification.sendAll("/room/notification", null);
    }

    // Methods statics

    public static Room getRoom(long id) throws Exception {
        Optional<Room> optionalRoom = ROOMS
                .stream()
                .filter(x -> x.getIdRoom() == id)
                .findAny();

        if(optionalRoom.isEmpty())
            throw new Exception("This rooom do not exist");

        return optionalRoom.get();
    }

}
