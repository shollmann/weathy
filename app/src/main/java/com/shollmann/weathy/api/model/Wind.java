
package com.shollmann.weathy.api.model;

import java.io.Serializable;

public class Wind implements Serializable {
    private Double speed;
    private Double deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getDegrees() {
        return deg;
    }

    public void setDeg(Double deg) {
        this.deg = deg;
    }

    public int getIntSpeed() {
        if (speed != null) {
            return speed.intValue();
        }
        return 0;
    }

    public int getIntDegrees() {
        if (deg != null) {
            return deg.intValue();
        }
        return 0;
    }
}
