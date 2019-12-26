package com.junshijia.HuoV3;


import com.junshijia.HuoV3.process.CSVDataList2Slave;
import com.junshijia.HuoV3.switchTurbine.SwitchTurbine;


public class Main {
    public static void main(String[] args) {
        //modbus slave初始化
        CSVDataList2Slave slave = new CSVDataList2Slave();

        //listener开启
        new Thread(slave).start();

        //更新list和slave
        slave.whileLoop();

    }
}
