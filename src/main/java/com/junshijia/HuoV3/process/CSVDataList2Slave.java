package com.junshijia.HuoV3.process;

import com.junshijia.HuoV3.dataTransfer.UpdateCSVDataLists;
import com.junshijia.HuoV3.domain.FeedbackFromMod;
import com.junshijia.HuoV3.extraEMS.TurbineStates;
import com.junshijia.HuoV3.readMod.BackForthWithEMS;
import com.junshijia.HuoV3.switchTurbine.SwitchTurbine;
import com.junshijia.HuoV3.util.HuoUtils;
import com.serotonin.modbus4j.BasicProcessImage;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusSlaveSet;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.tcp.TcpSlave;

public class CSVDataList2Slave implements Runnable{
    //和数据的通道
    UpdateCSVDataLists process;

    //slave所需属性
    private int slavePort;
    private ModbusSlaveSet listener;
    private ModbusFactory modbusFactory;
    private BasicProcessImage[] processImages;

    //和ecs的modbus交互的对象
    private BackForthWithEMS ems;
    private FeedbackFromMod emsFeedback;

    //风机各种状态
    private TurbineStates states;

    //counter，初始0，加到10后初始和ems的modbus通路；之后固定13到14不变
    //利用count管理进程开启的时间，效率很低，将来升级程序改掉
    private int count;


    public CSVDataList2Slave() {
        count = 0;
        //打开和数据库数据的通道
        this.process = new UpdateCSVDataLists();
        //建立slave方式1：通过factory，端口固定为502
        //this.modbusFactory = new ModbusFactory();
        //this.listener = modbusFactory.createTcpSlave(false);
        //建立slave方式2：跳过factory创建指定端口的slave
        this.listener = new TcpSlave(9876,false);
        //建立slave的7个image
        this.setProcessImages();
        //打开和ecs的modbus的通道
        //this.ems = new BackForthWithEMS();
        //开始记录风机状态
        this.states = new TurbineStates();
    }

    @Override
    public void run() {
        boolean flag = true;
        while(flag) {
            try {
                listener.start();
                flag = false;
            } catch (ModbusInitException e) {
                System.out.println("reconnect listener...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void setProcessImages(){
        this.processImages = new BasicProcessImage[7];
        for(int i = 0; i < 7; i++) {
            this.listener.addProcessImage(HuoUtils.getModscanProcessImage(i+1));
            this.processImages[i] = (BasicProcessImage)this.listener.getProcessImage(i+1);
        }
    }

    public void whileLoop(){
        //打开数据库，并得到csvlists，然后用数据库的信息更新lists
        new Thread(process::combine).start();
        //更新没数据的点
        for(int i = 0; i < 7; i++){
            HuoUtils.updateProcessImage4NoData(this.processImages[i],process.getId1to7Lists().get(i).getDataList_noData());
        }
        //给id2-6初始300个1开头的点，方便明阳调试
        for(int i = 1; i < 7; i++){
            HuoUtils.initID2to6(this.processImages[i]);
        }
        //ems的几个特殊值
        HuoUtils.writeEMSConstants(this.processImages[6]);
        //更新有数据的点
        while(true){
            if(this.count==3){
                //打开和ecs的modbus的通道
                this.ems = new BackForthWithEMS();
                new Thread(this.ems).start();
                new Thread(new SwitchTurbine()).start();
            }
            //写风机信息到slave1-7
            for(int i = 0; i < 7; i++){
                HuoUtils.updateProcessImage4HasData(this.processImages[i], process.getId1to7Lists().get(i).getDataList_hasData());
            }

            //HuoUtils.updateProcessImage4HasData(this.processImages[0], process.getId1List().getDataList_hasData());
            //和ems的modbus交互
            if(this.count > 5) {
                //和ems modbus开始交互
                //this.ems.fetchData();
                this.emsFeedback = ems.getFeedbackData();
                HuoUtils.updateProcessImageFromFeedbackData(this.processImages[6], this.emsFeedback);
                //写风机连接到slave id7
                this.setTurbineConnection2EMS();
                //写风机其他状态到slave id7
                this.setOtherTurbineStates2EMS();
                //写风机有功无功上限
                this.setReactivePower2EMS();
                this.setActivePower2EMS();
                this.setTurbineConnection();
                this.count = 7;
            }

            //3.延迟
            synchronized (listener) {
                try {
                    listener.wait(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //明天试试
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            count++;
        }
    }

    //更新和风机的连接,需要改
    private void setTurbineConnection2EMS(){
        int num = 0;
        if(this.process.getFetchEMS().getTshowData().getWT1_MainState()==1)
            num++;
        if(this.process.getFetchEMS().getTshowData().getWT2_MainState()==1)
            num++;
        if(this.process.getFetchEMS().getTshowData().getWT3_MainState()==1)
            num++;
        if(this.process.getFetchEMS().getTshowData().getWT4_MainState()==1)
            num++;
        if(this.process.getFetchEMS().getTshowData().getWT5_MainState()==1)
            num++;
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,33, DataType.FOUR_BYTE_FLOAT,num);
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,35, DataType.FOUR_BYTE_FLOAT,num*4000);
    }

    //统计风机状态和容量
    private void setOtherTurbineStates2EMS(){
        int[] turbineStates = new int[5];
        turbineStates[0] = this.process.getFetchTurbine().getT1Data().getHMI_IReg110().intValue();
        turbineStates[1] = this.process.getFetchTurbine().getT2Data().getHMI_IReg110().intValue();
        turbineStates[2] = this.process.getFetchTurbine().getT3Data().getHMI_IReg110().intValue();
        turbineStates[3] = this.process.getFetchTurbine().getT4Data().getHMI_IReg110().intValue();
        turbineStates[4] = this.process.getFetchTurbine().getT5Data().getHMI_IReg110().intValue();
        //设定状态码
        this.states.setTurbineStates(turbineStates);
        //总结状态
        this.states.processTurbineStates();
        //写状态到modbus slave
        //各种状态和容量
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,37, DataType.FOUR_BYTE_FLOAT,states.getFaultNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,39, DataType.FOUR_BYTE_FLOAT,states.getFaultCap());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,41, DataType.FOUR_BYTE_FLOAT,states.getMaintainNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,43, DataType.FOUR_BYTE_FLOAT,states.getMaintainCap());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,45, DataType.FOUR_BYTE_FLOAT,states.getWaitNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,47, DataType.FOUR_BYTE_FLOAT,states.getWaitCap());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,49, DataType.FOUR_BYTE_FLOAT,states.getLimitNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,51, DataType.FOUR_BYTE_FLOAT,states.getLimitCap());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,53, DataType.FOUR_BYTE_FLOAT,states.getFreeNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,55, DataType.FOUR_BYTE_FLOAT,states.getFreeCap());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,61, DataType.FOUR_BYTE_FLOAT,states.getNonLimitStopNum());
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,63, DataType.FOUR_BYTE_FLOAT,states.getNonLimitStopCap());
    }

    //理论无功功率
    private void setReactivePower2EMS(){
        int num = 0;
        if(this.process.getFetchTurbine().getT1Data().getHMI_IReg1422()>100)
            num++;
        if(this.process.getFetchTurbine().getT2Data().getHMI_IReg1422()>100)
            num++;
        if(this.process.getFetchTurbine().getT3Data().getHMI_IReg1422()>100)
            num++;
        if(this.process.getFetchTurbine().getT4Data().getHMI_IReg1422()>100)
            num++;
        if(this.process.getFetchTurbine().getT5Data().getHMI_IReg1422()>100)
            num++;
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,15, DataType.FOUR_BYTE_FLOAT,(num*1314.74));
        this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,17, DataType.FOUR_BYTE_FLOAT,-(num*1314.74));
    }

    //理论有功功率
    private void setActivePower2EMS(){
        //再算一遍风机状态
        int[] turbineStates = new int[5];
        turbineStates[0] = this.process.getFetchTurbine().getT1Data().getHMI_IReg110().intValue();
        turbineStates[1] = this.process.getFetchTurbine().getT2Data().getHMI_IReg110().intValue();
        turbineStates[2] = this.process.getFetchTurbine().getT3Data().getHMI_IReg110().intValue();
        turbineStates[3] = this.process.getFetchTurbine().getT4Data().getHMI_IReg110().intValue();
        turbineStates[4] = this.process.getFetchTurbine().getT5Data().getHMI_IReg110().intValue();
        //设定状态码
        this.states.setTurbineStates(turbineStates);
        //总结状态
        this.states.processTurbineStates();

        int[] runningFlag = this.states.getRunningFlag();
        float[] activePower = new float[5];
        float activePowerTotal = 0;
        activePower[0] = this.process.getFetchEMS().getTshowData().getWT1_PredictActpower();
        activePower[1] = this.process.getFetchEMS().getTshowData().getWT2_PredictActpower();
        activePower[2] = this.process.getFetchEMS().getTshowData().getWT3_PredictActpower();
        activePower[3] = this.process.getFetchEMS().getTshowData().getWT4_PredictActpower();
        activePower[4] = this.process.getFetchEMS().getTshowData().getWT5_PredictActpower();
        //限功率运行，先用实际值代替
        float[] activePowerLimit = new float[5];
        activePowerLimit[0] = this.process.getFetchTurbine().getT1Data().getHMI_IReg101();
        activePowerLimit[1] = this.process.getFetchTurbine().getT2Data().getHMI_IReg101();
        activePowerLimit[2] = this.process.getFetchTurbine().getT3Data().getHMI_IReg101();
        activePowerLimit[3] = this.process.getFetchTurbine().getT4Data().getHMI_IReg101();
        activePowerLimit[4] = this.process.getFetchTurbine().getT5Data().getHMI_IReg101();
        for(int i = 0; i < runningFlag.length;i++){
            if(runningFlag[i]==1 || runningFlag[i]==3) {
                activePowerTotal += activePower[i];
                if(runningFlag[i]==1){
                    //30101+i
                    processImages[6].setInputRegister(100+i,(short)0);
                }else{
                    processImages[6].setInputRegister(100+i,(short)1);
                }
            }else if(runningFlag[i]==2){
                activePowerTotal += (activePowerLimit[i] + 10F);
                processImages[6].setInputRegister(100+i,(short)2);
            }
 //           System.out.println("flag "+i+": "+runningFlag[i]);
        }
        //this.processImages[6].setNumeric(RegisterRange.INPUT_REGISTER,11, DataType.FOUR_BYTE_FLOAT,activePowerTotal);
    }

    private void setTurbineConnection(){
        boolean[] status = this.process.getStatus();
        for(int i = 0; i < status.length; i++){
            if(status[i] = true) {
                this.processImages[6].setInput(i + 10, true);
            }
        }
    }
}
