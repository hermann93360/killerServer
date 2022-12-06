package com.project.killerback.model.player;


public class Boss extends Player{

    public Boss() {
        super();
    }

    @Override
    public void vote(Player player) {
        player.takeVote(2);
    }
}
