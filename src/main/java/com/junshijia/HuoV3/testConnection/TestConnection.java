package com.junshijia.HuoV3.testConnection;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;

import java.util.Arrays;

public class TestConnection implements Runnable{
    private int address;
    private ModbusMaster master;



    public TestConnection() {
        IpParameters ip = new IpParameters();
        ip.setHost("192.168.101.244");
        ip.setPort(9876);
        ModbusFactory factory = new ModbusFactory();
        this.master = factory.createTcpMaster(ip,false);
        this.master.setTimeout(4000);
        this.master.setRetries(1);
        try {
            this.master.init();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            master.init();
        } catch (ModbusInitException e) {
            e.printStackTrace();
        }

        //this.readCoil(this.master,1,0,1);



    }


}
