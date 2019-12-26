package com.junshijia.HuoV3.readMod;



import com.junshijia.HuoV3.domain.FeedbackFromMod;
import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.ip.IpParameters;
import com.serotonin.modbus4j.msg.ReadInputRegistersRequest;
import com.serotonin.modbus4j.msg.ReadInputRegistersResponse;
import com.serotonin.modbus4j.msg.WriteRegisterRequest;
import com.serotonin.modbus4j.msg.WriteRegisterResponse;
import org.apache.log4j.Logger;

public class SendData2EMSMod {
    private IpParameters ipParameters;
    private ModbusFactory factory;
    private ModbusMaster master;
    private int port;
    private String ip;
    private int offset1;
    private int offset2;
    private int id;
    private int realPower;
    private int reactivePower;
    private FeedbackFromMod feedbackData;
    private Logger logger;


    public SendData2EMSMod() {
        this.feedbackData = new FeedbackFromMod();
        this.setIpPortAdd();
        this.ipParameters = new IpParameters();
        this.ipParameters.setHost(this.ip);
        this.ipParameters.setPort(this.port);
        this.factory = new ModbusFactory();
        this.logger = Logger.getLogger(SendData2EMSMod.class);
    }

    private void setIpPortAdd(){
        this.ip = "192.168.101.244";
        this.port = 711;
        this.offset1 = 0;
        this.offset2 = 1;
        this.id = 1;
    }

    private void setMasterandInit(){

        this.master = factory.createTcpMaster(this.ipParameters, true);
        this.master.setTimeout(6000);
        this.master.setRetries(1);
        boolean flag  = true;
        while(flag) {
            try {
                master.init();
                flag = false;
            } catch (ModbusInitException e) {
                System.out.println("wait 6min and reconnecting to 711...");
                try {
                    Thread.sleep(360000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void setRealPower(int realPower) {
        this.realPower = realPower;
    }

    public void setReactivePower(int reactivePower) {
        this.reactivePower = reactivePower;
    }

    //单位百兆瓦
    public void send2Mod(){
        this.writeRegister(1,0,this.realPower);
        this.writeRegister(1,1,this.reactivePower);
    }

    public FeedbackFromMod getFeedbackData() {
        return feedbackData;
    }

    public void send2ModAndReadFeedback(){

        this.writeRegister(1,0,this.realPower);
        this.writeRegister(1,1,this.reactivePower);
        //得到feedback 30001-30008
        this.readInputRegistersFeedback(1,0,feedbackData);
    }

    private void writeRegister(int slaveId, int offset, int value) {
        boolean flag = true;
        this.setMasterandInit();
        while(flag) {
            try {
                WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
                WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
                if (response.isException())
                    System.out.println("Exception response: message=" + response.getExceptionMessage());
                else {
                    flag = false;
                }
            } catch (ModbusTransportException e) {
                System.out.println("wait 2min and reconnecting...");
                this.master.destroy();
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.setMasterandInit();
            } finally {
                this.master.destroy();
            }
        }
    }

    //读3开头的单个reg
    private void readInputRegistersFeedback(int slaveId, int start, FeedbackFromMod data) {
        boolean flag = true;
        this.setMasterandInit();
        int len = 8;
        while(flag) {
            try {
                ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, start, len);
                ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);

                if (response.isException())
                    System.out.println("Exception response: message=" + response.getExceptionMessage());
                else {
                    //System.out.println(Arrays.toString(response.getShortData()));
                    data.setPowerUpperBound(response.getShortData()[0] * 200);
                    data.setPowerLowerBound(response.getShortData()[1] * 200);
                    data.setAdjustableCap(response.getShortData()[2] * 200);
                    data.setPowerLimitBit(response.getShortData()[3]);
                    data.setLimitPowerTurbineNumber(response.getShortData()[4]);
                    data.setLimitPowerCap(response.getShortData()[5] * 200);
                    data.setFlag(response.getShortData()[6]);
                    data.setReactivePowerFeedback(response.getShortData()[7]);
                    flag = false;
                }
            } catch (ModbusTransportException e) {
                System.out.println("reconnect 2min latter...");
                logger.error("read port:711 error, trying to reconnect 2 min latter...");
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                this.setMasterandInit();
            }finally {
                master.destroy();
            }
        }
    }

}
