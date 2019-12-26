package com.junshijia.HuoV3.domain;

import java.io.Serializable;

public class EMS_fshowDataFromDB implements Serializable {
    private Integer AGC_En;
    private Integer AVC_En;
    private Float SYS_WFrm_ActpSet;
    private Float SYS_WFrm_REActpowerSet;
    private Float SYS_WFrm_ActP;
    private Float SYS_WFrm_REActP;
    private Float SYS_WFrm_WS;
    private Float SYS_WNum;
    private Float SYS_Wcap;
    private Float SYS_BeC_Wcap;
    private Float SYS_WFrm_PreActP;

    @Override
    public String toString() {
        return "EMS_fshowDataFromDB{" +
                "AGC_En=" + AGC_En +
                ", AVC_En=" + AVC_En +
                ", SYS_WFrm_ActpSet=" + SYS_WFrm_ActpSet +
                ", SYS_WFrm_REActpowerSet=" + SYS_WFrm_REActpowerSet +
                ", SYS_WFrm_ActP=" + SYS_WFrm_ActP +
                ", SYS_WFrm_REActP=" + SYS_WFrm_REActP +
                ", SYS_WFrm_WS=" + SYS_WFrm_WS +
                ", SYS_WNum=" + SYS_WNum +
                ", SYS_Wcap=" + SYS_Wcap +
                ", SYS_BeC_Wcap=" + SYS_BeC_Wcap +
                ", SYS_WFrm_PreActP=" + SYS_WFrm_PreActP +
                '}';
    }

    public Float getSYS_WFrm_PreActP() {
        return (SYS_WFrm_PreActP);
    }

    public void setSYS_WFrm_PreActP(Float SYS_WFrm_PreActP) {
        this.SYS_WFrm_PreActP = SYS_WFrm_PreActP;
    }

    public Float getSYS_BeC_Wcap() {
        if (SYS_BeC_Wcap > 20000)
            return (20000F);
        else
            return (SYS_BeC_Wcap/1075);
    }

    public void setSYS_BeC_Wcap(Float SYS_BeC_Wcap) {
        this.SYS_BeC_Wcap = SYS_BeC_Wcap;
    }

    public Float getSYS_WFrm_REActpowerSet() {
        return SYS_WFrm_REActpowerSet;
    }

    public void setSYS_WFrm_REActpowerSet(Float SYS_WFrm_REActpowerSet) {
        this.SYS_WFrm_REActpowerSet = SYS_WFrm_REActpowerSet;
    }

    public Integer getAGC_En() {
        return AGC_En;
    }

    public void setAGC_En(Integer AGC_En) {
        this.AGC_En = AGC_En;
    }

    public Integer getAVC_En() {
        return AVC_En;
    }

    public void setAVC_En(Integer AVC_En) {
        this.AVC_En = AVC_En;
    }

    public Float getSYS_WFrm_ActpSet() {
        return SYS_WFrm_ActpSet*(1.00435F);
    }

    public void setSYS_WFrm_ActpSet(Float SYS_WFrm_ActpSet) {
        this.SYS_WFrm_ActpSet = SYS_WFrm_ActpSet;
    }



    public Float getSYS_WFrm_ActP() {
        return SYS_WFrm_ActP;
    }

    public void setSYS_WFrm_ActP(Float SYS_WFrm_ActP) {
        this.SYS_WFrm_ActP = SYS_WFrm_ActP;
    }

    public Float getSYS_WFrm_REActP() {
        return SYS_WFrm_REActP;
    }

    public void setSYS_WFrm_REActP(Float SYS_WFrm_REActP) {
        this.SYS_WFrm_REActP = SYS_WFrm_REActP;
    }

    public Float getSYS_WFrm_WS() {
        return SYS_WFrm_WS;
    }

    public void setSYS_WFrm_WS(Float SYS_WFrm_WS) {
        this.SYS_WFrm_WS = SYS_WFrm_WS;
    }

    public Float getSYS_WNum() {
        return SYS_WNum;
    }

    public void setSYS_WNum(Float SYS_WNum) {
        this.SYS_WNum = SYS_WNum;
    }

    public Float getSYS_Wcap() {
        if(SYS_Wcap > 20000)
            return 20000F;
        else
            return SYS_Wcap;
    }

    public void setSYS_Wcap(Float SYS_Wcap) {
        this.SYS_Wcap = SYS_Wcap;
    }
}
