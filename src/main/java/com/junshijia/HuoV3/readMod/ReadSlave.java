package com.junshijia.HuoV3.readMod;

import com.junshijia.HuoV3.util.HuoUtils;
import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.BatchResults;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersRequest;
import com.serotonin.modbus4j.msg.ReadHoldingRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;

public class ReadSlave {
    //modbus para
    private IpParameters ipParameters;
    private ModbusFactory factory;
    private ModbusMaster master;
    private BatchRead<Integer> batch;
    //address ip port
    private int port;
    private String ip;
    private int offset1;
    private int offset2;
    private int id;
    private int realPower;
    private int reactivePower;
    //result
    private int result1;
    private int result2;

    public ReadSlave() {
        this.setIpPortAdd();
        this.batch = new BatchRead<Integer>();
        //master的信息
        this.ipParameters = new IpParameters();
        ipParameters.setHost(this.ip);
        ipParameters.setPort(this.port);
        this.factory = new ModbusFactory();
    }

    private void setMasterAndInit(){
        this.master = factory.createTcpMaster(this.ipParameters, true);
        this.master.setTimeout(4000);
        this.master.setRetries(1);
        boolean flag  = true;
        while(flag) {
            try {
                this.master.init();
                flag = false;
            } catch (ModbusInitException e) {
                System.out.println("slave port:9876 wait 40s and re-initiate...");
                try {
                    Thread.sleep(40000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void setIpPortAdd(){
        this.ip = "192.168.101.244";
        this.port = 9876;
        this.offset1 = 1;
        this.offset2 = 3;
        this.id = 7;
    }

//    public void readFromSlave(){
//        try {
//            master.init();
//            this.readHoldingRegisters(this.master,1,0,2);
//
//        }
//        catch (ModbusInitException e) {
//            e.printStackTrace();
//        }
//        finally {
//            master.destroy();
//        }
//    }

    public void readFromSlave2(){
        boolean flag = true;
        this.setMasterAndInit();
        this.batch.addLocator(0, BaseLocator.holdingRegister(7, 0, DataType.TWO_BYTE_INT_SIGNED));
        this.batch.addLocator(1, BaseLocator.holdingRegister(7, 1, DataType.TWO_BYTE_INT_SIGNED));

        while(flag) {
            try {
                batch.setContiguousRequests(false);
                BatchResults<Integer> results = master.send(batch);
                //System.out.println(results.getValue(0));
                Integer r1 = Integer.valueOf(results.getValue(0).toString());
                float floatResult1 = r1.floatValue()*1.0445F;
                this.result1 = this.KwTo50Mw(floatResult1);
                //System.out.println(result1);

                Integer r2 = Integer.valueOf(results.getValue(1).toString());
                float floatResult2 = r2.floatValue();
                this.result2 = this.KwTo50Mw(floatResult2);
                //System.out.println(r2);
                //System.out.println(r1);
                WriteRegisterRequest request1 = new WriteRegisterRequest(7,2,r1);
                WriteRegisterRequest request2 = new WriteRegisterRequest(7,3,r2);
                master.send(request1);
                master.send(request2);
                flag = false;
            } catch (ModbusTransportException e) {
                System.out.println("reconnect to master 40s latter...");
                this.master.destroy();
                try {
                    Thread.sleep(40000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.setMasterAndInit();
            } catch (ErrorResponseException e) {
                e.printStackTrace();
            } finally {
                this.master.destroy();
            }
        }
    }

    public int getResult1() {
        return result1;
    }

    public int getResult2() {
        return result2;
    }

//    private void readHoldingRegisters(ModbusMaster master, int slaveId, int start, int len) {
//        try {
//            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, start, len);
//            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
//
//            if (response.isException())
//                System.out.println("Exception response: message=" + response.getExceptionMessage());
//            //else
//                //System.out.println(response.getShortData()[0]);
//        }
//        catch (ModbusTransportException e) {
//            e.printStackTrace();
//        }
//    }

    public int KwTo50Mw(float Kw){
        int hundreadMw = (int)Kw/5;
        return hundreadMw;
    }
}
