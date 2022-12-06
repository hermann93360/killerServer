package com.project.killerback.model;

import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;

public class TimerRoom {

    private final Timer timer;
    private final int seconds;
    private final Room room;
    private boolean day;

    public TimerRoom(int seconds, Room room, boolean day) {
        timer = new Timer();
        this.seconds = seconds;
        this.room = room;
        this.day = day;
        timer.schedule(new StopTask(), 2000, 1000);
    }


    class StopTask extends TimerTask {
        int counter = seconds;
        @SneakyThrows
        public void run() {
            //Notification.sendInRoom("/get/time/notification", room.getIdRoom(), new TimerDto(counter));
            System.out.println(counter);
            counter--;

            if(counter == 0 && !room.someoneWin()) {
                timer.cancel();
                if(day){
                    day = false;
                    //Notification.sendInRoom("/play/night", null,room.getIdRoom());

                    new TimerRoom(10, room, day);
                }else{
                    day = true;
                    //Notification.sendInRoom("/play/day", null, room.getIdRoom());

                    room.killUser();
                    new TimerRoom(20, room, day);
                }
            }
        }
    }
}