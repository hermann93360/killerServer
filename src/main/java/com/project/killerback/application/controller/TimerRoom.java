package com.project.killerback.application.controller;

import com.project.killerback.application.dto.TimerDto;
import com.project.killerback.domain.model.Room;
import com.project.killerback.domain.model.State;
import com.project.killerback.domain.model.User;
import com.project.killerback.domain.services.MapperService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Timer;
import java.util.TimerTask;

import static com.project.killerback.domain.model.State.DAY;
import static com.project.killerback.domain.model.State.NIGHT;
import static com.project.killerback.domain.services.MapperService.toUserDto;

@AllArgsConstructor
public class TimerRoom {

    private final Timer timer;
    private final int seconds;
    private final Room room;
    private final MainController mainController;

    public TimerRoom(int seconds, Room room, MainController mainController) {
        timer = new Timer();
        this.seconds = seconds;
        this.room = room;
        timer.schedule(new StopTask(), 4000, 1000);
        this.mainController = mainController;
    }


    class StopTask extends TimerTask {
        int counter = seconds;
        @SneakyThrows
        public void run() {
            mainController.send("/socket/counter/" + room.getIdRoom(), new TimerDto(counter));
            counter--;
            if(counter == -1 && !room.someoneWin()) {
                timer.cancel();
                if(room.isDay()){
                    new TimerRoom(10, room, mainController);
                    room.setState(NIGHT);
                    mainController.send("/socket/night/" + room.getIdRoom(), room.getIdRoom());
                }else{
                    User user = room.killUser();
                    new TimerRoom(20, room, mainController);
                    room.setState(DAY);
                    mainController.send("/socket/day/" + room.getIdRoom(), toUserDto(user));
                }
            }
        }
    }
}