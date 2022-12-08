package com.project.killerback.domain.model.player;


public class Tourist extends Player{

    public Tourist() {
        super();
    }

    @Override
    public void vote(Player player) {
        player.takeVote(0);
        setAlreadyVote(true);
    }
}
