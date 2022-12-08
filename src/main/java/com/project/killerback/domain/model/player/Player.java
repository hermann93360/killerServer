package com.project.killerback.domain.model.player;


import lombok.Getter;

@Getter
public abstract class Player {

    private boolean alive;
    private int vote;
    private static final int MAX_PLAYER_TYPE = 4;
    private boolean alreadyVote;


    public Player() {
        this.alive = true;
        this.alreadyVote = false;
        vote = 0;
    }

    public void takeVote(int vote){
        this.vote += vote;
    }

    public void setAlreadyVote(boolean vote) {
        this.alreadyVote = vote;
    }
    public abstract void vote(Player player);

    public boolean isDead(){
        return !this.alive;
    }

    public void kill(){
        this.alive = false;
    }

    public void resetVote() {
        this.vote = 0;
        setAlreadyVote(false);
    }
}
