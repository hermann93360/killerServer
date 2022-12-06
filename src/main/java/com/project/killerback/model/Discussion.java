package com.project.killerback.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Stack;

@AllArgsConstructor
@Getter
public class Discussion {
    private static long idIncrement = 0;

    private final long id;
    private Stack<Chat> chats;
    private final User user1;
    private final User user2;

    public Discussion(User user1, User user2) {
        idIncrement++;

        this.user1 = user1;
        this.user2 = user2;
        this.chats = new Stack<>();
        this.id = idIncrement;
    }

    public void addChat(Chat chat){
        chats.push(chat);
    }

    public Chat getLastChat() {
        return this.chats.peek();
    }

    public boolean equal(User user1, User user2){
        return ((user1.getId() == this.user1.getId() ||
                user1.getId() == this.user2.getId())) &&
                ((user2.getId() == this.user1.getId() ||
                  user2.getId() == this.user2.getId()));
    }
}
