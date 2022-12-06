package com.project.killerback.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Error implements Dto {
    private int code;
    private String description;

    @Override
    public String toString() {
        return "Error{" +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
