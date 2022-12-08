package com.project.killerback.domain.model;

public enum State {

    NOT_STARTED("NOT_STARTED"),
    NIGHT("NIGHT"),
    DAY("DAY");

    State(String state) {
    }

    public boolean isDay() {
        return this.equals(DAY);
    }

    public boolean isNight() {
        return this.equals(NIGHT);
    }
}
