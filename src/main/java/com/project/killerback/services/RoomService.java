package com.project.killerback.services;

import com.project.killerback.dto.RoomDto;
import com.project.killerback.exception.RoomException;
import com.project.killerback.model.Room;
import com.project.killerback.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    public Room addRoom(String name) {
        return Room.of(name);
    }

    public User joinRoom(long idRoom, long idUser) throws Exception {
        Room room = Room.getRoom(idRoom);
        User user = User.getUser(idUser);

        if(userIsInThisRoom(room, idUser)){
            throw new RoomException("This user " + idUser + " is already in this room");
        }

        user.joinRoom(room);
        room.addUser(user);

        return user;
    }

    private boolean userIsInThisRoom(Room room, long idUser) {
        return room.getUsers()
                .stream()
                .anyMatch(u -> u.getId() == idUser);
    }

    public List<Room> getAllRooms() {
        return Room.ROOMS;
    }

    public List<User> getUsersByRoom(long idRoom) throws Exception {
        return Room.getRoom(idRoom).getUsers();
    }
}
