package com.project.killerback.domain.model.player;


import com.project.killerback.domain.model.Discussion;
import com.project.killerback.domain.model.Room;
import com.project.killerback.domain.model.User;

import java.util.List;

public class Voyeur extends Player{

    private Room room;

    public Voyeur(Room room) {
        super();
        this.room = room;
    }

    @Override
    public void vote(Player player) {
        player.takeVote(1);
        setAlreadyVote(true);
    }

    public List<Discussion> viewDiscussions(User userTarget) {
        return userTarget.getDiscussions();
    }
}
