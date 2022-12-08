package com.project.killerback.domain.model.player;


public class Killer extends Player{

    private boolean useKill;

    public Killer() {
        super();
        this.useKill = false;
    }

    @Override
    public void vote(Player player) {
        player.takeVote(1);
        setAlreadyVote(true);
    }

    public void kill(Player player) throws Exception {
        if(useKill){
            throw new Exception("The killer has already kill a player");
        }else{
            useKill = true;
            player.kill();
        }
    }
}
