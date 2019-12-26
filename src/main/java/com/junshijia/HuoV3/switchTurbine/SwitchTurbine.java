package com.junshijia.HuoV3.switchTurbine;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadCoilsRequest;
import com.serotonin.modbus4j.msg.ReadCoilsResponse;
import com.serotonin.modbus4j.msg.WriteCoilRequest;
import com.serotonin.modbus4j.msg.WriteCoilResponse;

public class SwitchTurbine implements Runnable{
    private ModbusMaster master;
    private ModbusMaster mainControl;
    private ModbusFactory factory;

    //主控的ip对应id2-6
    private String[] ips;
    //起停的coil
    private int startBit;
    private int stopBit;

    //flag
    private boolean w8flag;

    public SwitchTurbine() {
        this.setTurbineParam();
        this.factory = new ModbusFactory();
        this.setMasterAndInit();
    }

    private void setMasterAndInit(){
        IpParameters ip = new IpParameters();
        ip.setHost("192.168.101.244");
        ip.setPort(9876);
        this.master = this.factory.createTcpMaster(ip, true);
        this.master.setTimeout(4000);
        this.master.setRetries(1);
        boolean flag  = true;
        while(flag) {
            try {
                this.master.init();
                flag = false;
            } catch (ModbusInitException e) {
                System.out.println("port:9876 init error...");
                System.out.println("wait 60s and reconnecting...");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    private void setTurbineParam(){
        this.ips = new String[5];
        ips[0] = "192.168.101.171";//t28 = wt1
        ips[1] = "192.168.101.170";//t29 = wt2
        ips[2] = "192.168.101.163";//t14 = wt3
        ips[3] = "192.168.101.164";//t37 = wt4
        ips[4] = "192.168.101.169";//tb2 = wt5
        this.startBit = 920;
        this.stopBit = 919;
        this.w8flag = false;
    }

    @Override
    public void run() {
        this.whileLoop();
    }

    public void whileLoop(){
        boolean[] arr;
        while(true) {
            //先等待3秒
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //循环读5个风机的开关信号
            for (int i = 0; i < 5; i++) {
                //这里的i代表id，所以要+2，读slave的状态，值是明阳给的
                arr = this.readSwitchCoils(i+2);
                //arr[0] = start  arr[1] = stop
                if (arr[0] && arr[1]){
                    System.out.println("turbine switch signal error..."+"\n"
                    +"writing both bits to 0...");
                    this.writeSwitchCoil(i+2,4);
                    this.writeSwitchCoil(i+2,5);
                    break;
                }
                //这里的i代表第i个ips
                if(arr[0]){
                    System.out.println("turbine "+(i+1)+" starting...");
                    //写主控
                    //this.writeMainControl(i, startBit);
                    EMSWritesMainControl.updateDB(i,1);
                    //复位slave
                    this.writeSwitchCoil(i+2,3);
                    System.out.println("turbine "+(i+1)+" started...");
                }else if(arr[1]){
                    System.out.println("turbine "+(i+1)+" stopping...");
                    //直接写主控
                    //this.writeMainControl(i, stopBit);
                    //写数据库
                    EMSWritesMainControl.updateDB(i,2);
                    //复位slave
                    this.writeSwitchCoil(i+2,4);
                    System.out.println("turbine "+(i+1)+" stopped...");

                }
            }
        }
    }
    //读slave中coil的4，5
    private boolean[] readSwitchCoils(int idN) {
        boolean flag = true;
        boolean[] array = new boolean[2];//初始化为false
        while(flag) {
            try {
                ReadCoilsRequest request = new ReadCoilsRequest(idN, 3, 2);
                ReadCoilsResponse response = (ReadCoilsResponse) this.master.send(request);

                if (response.isException()) {
                    System.out.println("Exception response:" + response.getExceptionMessage());
                    return array;
                } else {
                    array = response.getBooleanData();
                    flag = false;
                }
            } catch (ModbusTransportException e) {
                System.out.println("port:9876 slave error...");
                System.out.println("30s later reconnecting...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.master.destroy();
                this.setMasterAndInit();
            }
        }
        return array;
    }

    //复位slave中第n个coil
    private void writeSwitchCoil(int idN, int bit){
        boolean flag = true;
        while(flag) {
            try {
                WriteCoilRequest request = new WriteCoilRequest(idN, bit, false);
                WriteCoilResponse response = (WriteCoilResponse) master.send(request);
                if (response.isException())
                    System.out.println("Exception response:" + response.getExceptionMessage());
                flag = false;
            } catch (ModbusTransportException e) {
                System.out.println("port: 9876 slave error...");
                System.out.println("30s later reconnecting...");
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.master.destroy();
                this.setMasterAndInit();
            }
        }
    }

    private void setMainControlAndInit(String ipAddress){
        IpParameters ip = new IpParameters();
        ip.setHost(ipAddress);
        ip.setPort(715);
        this.mainControl = this.factory.createTcpMaster(ip, false);
        this.mainControl.setTimeout(4000);
        this.mainControl.setRetries(1);
        boolean flag  = true;
        while(flag) {
            try {
                this.mainControl.init();
                flag = false;
            } catch (ModbusInitException e) {
                System.out.println("modbus: 715 initiation error...");
                System.out.println("wait 6min and reconnecting...");
                try {
                    Thread.sleep(360000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void writeMainControl(int id, int coilAddress){
        this.setMainControlAndInit(this.ips[id]);
        this.writeMainControlCoils(coilAddress, false, this.ips[id]);
        this.writeMainControlCoils(coilAddress, true, this.ips[id]);
        this.writeMainControlCoils(coilAddress, false, this.ips[id]);
        this.mainControl.destroy();
    }

    private void writeMainControlCoils(int address, boolean bool, String ipAdd){
        boolean flag = true;
        while(flag) {
            try {
                this.w8flag = true;
                WriteCoilRequest request = new WriteCoilRequest(1, address, bool);
                WriteCoilResponse response = (WriteCoilResponse) this.mainControl.send(request);
                if (response.isException())
                    System.out.println("Exception response:" + response.getExceptionMessage());
                flag = false;
                Thread.sleep(400);
            } catch (ModbusTransportException e) {
                System.out.println("modbus: 715 connecting error...");
                System.out.println("trying to reconnect at 6min latter...");
                try {
                    Thread.sleep(360000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.mainControl.destroy();
                this.setMainControlAndInit(ipAdd);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
