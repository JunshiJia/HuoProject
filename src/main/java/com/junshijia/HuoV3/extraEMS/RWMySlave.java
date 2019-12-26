package com.junshijia.HuoV3.extraEMS;

import com.serotonin.modbus4j.BatchRead;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.locator.BaseLocator;
import com.serotonin.modbus4j.locator.NumericLocator;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsRequest;
import com.serotonin.modbus4j.msg.ReadDiscreteInputsResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;


public class RWMySlave {
    //modbus para
//    private IpParameters ipParameters;
//    private ModbusFactory factory;
//    private ModbusMaster master;
//    //读数据，批量读
//    private BatchRead<Integer> read;
//    //写数据,只能单个写
//    private BaseLocator<Number> write;
//    //address ip port
//    private int port;
//    private String ip;
//    //风机状态类
//    private TurbineStates states;
//    private int[] turbineStates;
//
//    public RWMySlave() {
//        this.states = new TurbineStates();
//        this.turbineStates = new int[5];
//        this.setIpPortAdd();
//        this.ipParameters = new IpParameters();
//        ipParameters.setHost(this.ip);
//        ipParameters.setPort(this.port);
//        this.factory = new ModbusFactory();
//        this.master = factory.createTcpMaster(this.ipParameters, true);
//        this.master.setTimeout(4000);
//        this.master.setRetries(1);
//        this.read = new BatchRead<Integer>();
//        try {
//            this.master.init();
//        } catch (ModbusInitException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void setIpPortAdd(){
//        this.ip = "192.168.101.244";
//        this.port = 9876;
//    }
//
//
//    //读取、更新、发送风机通讯状态
//    private void readTurbineConnection(){
//        int num=0;
//        boolean[] arr = new boolean[5];
//        try {
//            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(7, 5, 5);
//            ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) this.master.send(request);
//            if (response.isException())
//                System.out.println("Exception response: message=" + response.getExceptionMessage());
//            else
//                arr = response.getBooleanData();
//        } catch (ModbusTransportException e) {
//            e.printStackTrace();
//        }
//        for(int i = 0; i<arr.length; i++){
//            if(arr[i]){
//                num++;
//            }
//        }
//        //设置通讯台数
//        states.setNoCommuNum(num);
//        //写回mosbus
//        try {
//            WriteRegisterRequest request = new WriteRegisterRequest(7, 5, states.getNoCommuNum());
//            master.send(request);
//            request = new WriteRegisterRequest(7, 6, states.getNoCommuCap());
//            master.send(request);
//        } catch (ModbusTransportException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //读取、总结风机状态  30073
//    public void readTurbineStates() throws ErrorResponseException, ModbusTransportException {
//        NumericLocator read;
//        for(int i = 0; i < turbineStates.length; i++){
//            read = new NumericLocator(i+2, RegisterRange.INPUT_REGISTER, 73, DataType.FOUR_BYTE_FLOAT);
//            this.turbineStates[i] = master.getValue(read).intValue();
//            this.switchFunc(this.turbineStates[i]);
//        }
//
//    }
//
//
//    private void switchFunc()
}
