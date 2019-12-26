package com.junshijia.HuoV3.domain;

import java.io.Serializable;

public class FeedbackFromMod implements Serializable {
    private int powerUpperBound;
    private int powerLowerBound;
    private int adjustableCap;
    private int powerLimitBit;
    private int limitPowerTurbineNumber;
    private int limitPowerCap;
    private int flag;
    private int reactivePowerFeedback;

    public int getPowerUpperBound() {
        return powerUpperBound;
    }

    public void setPowerUpperBound(int powerUpperBound) {
        this.powerUpperBound = powerUpperBound;
    }

    public int getPowerLowerBound() {
        return powerLowerBound;
    }

    public void setPowerLowerBound(int powerLowerBound) {
        this.powerLowerBound = powerLowerBound;
    }

    public int getAdjustableCap() {
        return adjustableCap;
    }

    public void setAdjustableCap(int adjustableCap) {
        this.adjustableCap = adjustableCap;
    }

    public int getPowerLimitBit() {
        return powerLimitBit;
    }

    public void setPowerLimitBit(int powerLimitBit) {
        this.powerLimitBit = powerLimitBit;
    }

    public int getLimitPowerTurbineNumber() {
        return limitPowerTurbineNumber;
    }

    public void setLimitPowerTurbineNumber(int limitPowerTurbineNumber) {
        this.limitPowerTurbineNumber = limitPowerTurbineNumber;
    }

    public int getLimitPowerCap() {
        return limitPowerCap;
    }

    public void setLimitPowerCap(int limitPowerCap) {
        this.limitPowerCap = limitPowerCap;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getReactivePowerFeedback() {
        return reactivePowerFeedback;
    }

    public void setReactivePowerFeedback(int reactivePowerFeedback) {
        this.reactivePowerFeedback = reactivePowerFeedback;
    }

    @Override
    public String toString() {
        return "FeedbackData{" +
                "powerUpperBound=" + powerUpperBound +
                ", powerLowerBound=" + powerLowerBound +
                ", adjustableCap=" + adjustableCap +
                ", powerLimitBit=" + powerLimitBit +
                ", limitPowerTurbineNumber=" + limitPowerTurbineNumber +
                ", limitPowerCap=" + limitPowerCap +
                ", flag=" + flag +
                ", reactivePowerFeedback=" + reactivePowerFeedback +
                '}';
    }
}
