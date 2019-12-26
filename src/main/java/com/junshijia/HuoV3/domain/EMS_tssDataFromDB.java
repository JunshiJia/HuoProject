package com.junshijia.HuoV3.domain;

import java.io.Serializable;

public class EMS_tssDataFromDB implements Serializable {
    private Integer WT1;
    private Integer WT2;
    private Integer WT3;
    private Integer WT4;
    private Integer WT5;

    public EMS_tssDataFromDB() {
        this.setWT1(0);
        this.setWT2(0);
        this.setWT3(0);
        this.setWT4(0);
        this.setWT5(0);
    }

    public Integer getWT1() {
        return WT1;
    }

    public void setWT1(Integer WT1) {
        this.WT1 = WT1;
    }

    public Integer getWT2() {
        return WT2;
    }

    public void setWT2(Integer WT2) {
        this.WT2 = WT2;
    }

    public Integer getWT3() {
        return WT3;
    }

    public void setWT3(Integer WT3) {
        this.WT3 = WT3;
    }

    public Integer getWT4() {
        return WT4;
    }

    public void setWT4(Integer WT4) {
        this.WT4 = WT4;
    }

    public Integer getWT5() {
        return WT5;
    }

    public void setWT5(Integer WT5) {
        this.WT5 = WT5;
    }

    @Override
    public String toString() {
        return "EMS_tssDataFromDB{" +
                "WT1=" + WT1 +
                ", WT2=" + WT2 +
                ", WT3=" + WT3 +
                ", WT4=" + WT4 +
                ", WT5=" + WT5 +
                '}';
    }
}
