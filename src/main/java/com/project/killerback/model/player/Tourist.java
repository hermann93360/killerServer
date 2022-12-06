package com.project.killerback.model.player;


public class Tourist extends Player{

    public Tourist() {
        super();
    }

    @Override
    public void vote(Player player) {
        player.takeVote(0);
    }
}
