package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class TimerDto implements Dto{
    private final int timer;
}
