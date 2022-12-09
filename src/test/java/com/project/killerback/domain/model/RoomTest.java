package com.project.killerback.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    User user;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;

    Room room;

    @Test
    void test() throws Exception {
        room = Room.of("NEW");
        user = User.of("Hermenn");
        user2 = User.of("Anais");
        user3 = User.of("Leandra");
        user4 = User.of("Manon");
        user5 = User.of("Jacques");
        user6 = User.of("Remi");

        room.addUser(user6);
        room.addUser(user);
        room.addUser(user2);
        room.addUser(user3);
        room.addUser(user4);
        room.addUser(user5);

        room.setAllPlayer();


        Assertions.assertThat(room.killUser()).isEqualTo(user6);

        Assertions.assertThat(room.killUser()).isEqualTo(user6);

    }

}