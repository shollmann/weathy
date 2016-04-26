
package com.shollmann.weathy.api.model;

public class Wind {
    private Double speed;
    private int deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public int getDegrees() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }

    public int getIntSpeed() {
        return speed.intValue();
    }
}
