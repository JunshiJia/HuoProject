package com.junshijia.HuoV3.extraEMS;

public class TurbineStates {
    private int[] turbineStates;
    //故障台数、容量 主状态：51-57
    //30037、39
    private int faultNum;
    private int faultCap;
    //维护台数、容量 主状态：58
    private int maintainNum;
    private int maintainCap;
    //待机台数(相当于正常停机)、容量 主状态：0,1,2,3,11-15
    private int waitNum;
    private int waitCap;
    //限电运行台数、容量 主状态：34-36   5-30005
    private int limitNum;
    private int limitCap;
    //自由发电台数、容量 主状态：31,32,33,37,38,
    private int freeNum;
    private int freeCap;
    //限电停机台数、容量 读slave的10116 = 0 一直是0
    //private int limitStopNum;
    //private float limitStopCap;
    //非限电停机台数、容量 == 故障停机  59，60
    private int nonLimitStopNum;
    private int nonLimitStopCap;
    //运行flag
    private int[] runningFlag;


    public TurbineStates() {
        this.turbineStates = new int[5];
        this.runningFlag = new int[5];
    }

    public void processTurbineStates(){
        this.setInit();
        for(int i=0;i< this.turbineStates.length;i++){
            this.switchCases(this.turbineStates[i],i);
        }
        this.setWaitNum(5-this.limitNum-this.maintainNum-this.freeNum-this.faultNum-this.nonLimitStopNum);
    }

    private void setInit(){
        //假设都待机
        this.setFaultNum(0);
        this.setFreeNum(0);
        this.setLimitNum(0);
        this.setWaitNum(0);
        this.setMaintainNum(0);
        this.setNonLimitStopNum(0);
        for(int i = 0; i<this.runningFlag.length;i++) {
            this.runningFlag[i] = 0;
        }
    }

    private void switchCases(int n, int i){
        if(n >=51 && n<=57){
            this.faultNum++;
            this.faultCap+=4000;
        }
        else if(n == 58){
            this.maintainNum++;
            this.maintainCap+=4000;
        }
        else if(n == 34 || n == 35 || n == 36){
            this.limitNum++;
            this.limitCap+=4000;
            //先功率运行flag=2
            if(n == 34) {//手动限电flag=2
                this.runningFlag[i] = 2;
            }else if (n == 35){//调度限电flag=3
                this.runningFlag[i] = 3;
            }
        }
//        else if(n  >= 11 && n <= 15){
//            this.freeNum++;
//            this.freeCap+=4000;
//        }
        //32提升功率 31并网 33低电压穿越
        else if(n  == 31 || n == 32 || n == 33 || n == 37 || n == 38){
                this.freeNum++;
                this.freeCap+=4000;
                //正常运行flag==1
                this.runningFlag[i] = 1;

        }
        else if(n == 59 || n == 60){
            this.nonLimitStopNum++;
            this.nonLimitStopCap+=4000;
        }
    }

    public void setFaultNum(int faultNum) {
        this.faultNum = faultNum;
        this.faultCap = faultNum*4000;
    }

    public void setNonLimitStopNum(int nonLimitStopNum){
        this.nonLimitStopNum = nonLimitStopNum;
        this.nonLimitStopCap = nonLimitStopNum*4000;
    }

    public void setMaintainNum(int maintainNum) {
        this.maintainNum = maintainNum;
        this.maintainCap = maintainNum*4000;
    }

    public void setWaitNum(int waitNum) {
        this.waitNum = waitNum;
        this.waitCap = waitNum*4000;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
        this.limitCap = limitNum*4000;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
        this.freeCap = freeNum*4000;
    }

    public void setTurbineStates(int[] turbineStates) {
        this.turbineStates = turbineStates;
    }

    public int getMaintainNum() {
        return maintainNum;
    }

    public int getMaintainCap() {
        return maintainCap;
    }


    public int getFaultNum() {
        return faultNum;
    }

    public int getFaultCap() {
        return faultCap;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public int getWaitCap() {
        return waitCap;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public int getLimitCap() {
        return limitCap;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public int getFreeCap() {
        return freeCap;
    }

    public int getNonLimitStopNum() {
        return nonLimitStopNum;
    }

    public int getNonLimitStopCap() {
        return nonLimitStopCap;
    }

    public int[] getRunningFlag() {
        return runningFlag;
    }
}
