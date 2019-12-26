package com.junshijia.HuoV3.domain;

import java.io.Serializable;

public class EMS_tshowDataFromDB implements Serializable {
    private Integer WT1_MainState;
    private Integer WT2_MainState;
    private Integer WT3_MainState;
    private Integer WT4_MainState;
    private Integer WT5_MainState;
    private Float WT1_PredictActpower;
    private Float WT2_PredictActpower;
    private Float WT3_PredictActpower;
    private Float WT4_PredictActpower;
    private Float WT5_PredictActpower;

    public Integer getWT1_MainState() {
        return WT1_MainState;
    }

    public void setWT1_MainState(Integer WT1_MainState) {
        this.WT1_MainState = WT1_MainState;
    }

    public Integer getWT2_MainState() {
        return WT2_MainState;
    }

    public void setWT2_MainState(Integer WT2_MainState) {
        this.WT2_MainState = WT2_MainState;
    }

    public Integer getWT3_MainState() {
        return WT3_MainState;
    }

    public void setWT3_MainState(Integer WT3_MainState) {
        this.WT3_MainState = WT3_MainState;
    }

    public Integer getWT4_MainState() {
        return WT4_MainState;
    }

    public void setWT4_MainState(Integer WT4_MainState) {
        this.WT4_MainState = WT4_MainState;
    }

    public Integer getWT5_MainState() {
        return WT5_MainState;
    }

    public void setWT5_MainState(Integer WT5_MainState) {
        this.WT5_MainState = WT5_MainState;
    }

    public Float getWT1_PredictActpower() {
        return WT1_PredictActpower;
    }

    public void setWT1_PredictActpower(Float WT1_PredictActpower) {
        this.WT1_PredictActpower = WT1_PredictActpower;
    }

    public Float getWT2_PredictActpower() {
        return WT2_PredictActpower;
    }

    public void setWT2_PredictActpower(Float WT2_PredictActpower) {
        this.WT2_PredictActpower = WT2_PredictActpower;
    }

    public Float getWT3_PredictActpower() {
        return WT3_PredictActpower;
    }

    public void setWT3_PredictActpower(Float WT3_PredictActpower) {
        this.WT3_PredictActpower = WT3_PredictActpower;
    }

    public Float getWT4_PredictActpower() {
        return WT4_PredictActpower;
    }

    public void setWT4_PredictActpower(Float WT4_PredictActpower) {
        this.WT4_PredictActpower = WT4_PredictActpower;
    }

    public Float getWT5_PredictActpower() {
        return WT5_PredictActpower;
    }

    public void setWT5_PredictActpower(Float WT5_PredictActpower) {
        this.WT5_PredictActpower = WT5_PredictActpower;
    }

    @Override
    public String toString() {
        return "EMS_tshowDataFromDB{" +
                "WT1_MainState=" + WT1_MainState +
                ", WT2_MainState=" + WT2_MainState +
                ", WT3_MainState=" + WT3_MainState +
                ", WT4_MainState=" + WT4_MainState +
                ", WT5_MainState=" + WT5_MainState +
                ", WT1_PredictActpower=" + WT1_PredictActpower +
                ", WT2_PredictActpower=" + WT2_PredictActpower +
                ", WT3_PredictActpower=" + WT3_PredictActpower +
                ", WT4_PredictActpower=" + WT4_PredictActpower +
                ", WT5_PredictActpower=" + WT5_PredictActpower +
                '}';
    }
}
