package com.project.killerback.domain.model.player;


public class Villager extends Player{

    private static long idIncr = 0;
    private long id;

    public Villager() {
        super();
        idIncr++;
        this.id = idIncr;
    }

    @Override
    public void vote(Player player) {
        player.takeVote(1);
    }
}
