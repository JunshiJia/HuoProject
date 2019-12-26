package com.junshijia.HuoV3.domain;

import java.io.Serializable;
import java.sql.Timestamp;

public class EMS_enDataFromDB implements Serializable {
    private Integer AP_Control_EN;
    private Integer RP_Control_EN;
    private Integer AP_Manual_Val_Set;
    private Integer RP_Manual_Val_Set;

    public Integer getAP_Control_EN() {
        return AP_Control_EN;
    }

    public void setAP_Control_EN(Integer AP_Control_EN) {
        this.AP_Control_EN = AP_Control_EN;
    }

    public Integer getRP_Manual_Val_Set() {
        return RP_Manual_Val_Set;
    }

    public void setRP_Manual_Val_Set(Integer RP_Manual_Val_Set) {
        this.RP_Manual_Val_Set = RP_Manual_Val_Set;
    }

    public Integer getAP_Manual_Val_Set() {
        return AP_Manual_Val_Set;
    }

    public void setAP_Manual_Val_Set(Integer AP_Manual_Val_Set) {
        this.AP_Manual_Val_Set = AP_Manual_Val_Set;
    }

    public Integer getRP_Control_EN() {
        return RP_Control_EN;
    }

    public void setRP_Control_EN(Integer RP_Control_EN) {
        this.RP_Control_EN = RP_Control_EN;
    }

    @Override
    public String toString() {
        return "EMS_enDataFromDB{" +
                "AP_Control_EN=" + AP_Control_EN +
                ", RP_Control_EN=" + RP_Control_EN +
                ", AP_Manual_Val_Set=" + AP_Manual_Val_Set +
                ", RP_Manual_Val_Set=" + RP_Manual_Val_Set +
                '}';
    }
}
